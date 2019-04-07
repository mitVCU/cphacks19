package com.example.cphacks19

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.util.Log

import kotlin.concurrent.schedule
import com.google.android.cameraview.CameraView

import kotlinx.android.synthetic.main.activity_play.*
import android.os.Environment.DIRECTORY_PICTURES
import android.graphics.BitmapFactory
import android.widget.Toast
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.OutputStream
import java.util.*
import android.support.v4.view.accessibility.AccessibilityEventCompat.setAction
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Matrix
import android.net.Uri
import android.util.AttributeSet
import android.widget.ImageView


class PlayActivity : AppCompatActivity() {


    private val TAG = "Play activity"
    private lateinit var model : EmotionViewModel
    private var emotionName = "neutral"
    private var score = 0f

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_play)
        model = EmotionViewModel()
        model.getEmotionLiveData().observe(this, android.arch.lifecycle.Observer { value -> kotlin.run{
            Log.d(TAG, "observing data change")
            score += value!!.toFloat()
            Toast.makeText(this, model.getEmotionLiveData().value!!.toString(),Toast.LENGTH_SHORT).show()
        }})
        if (checkCameraHardware(this)) {
            init()
        }
    }

    private fun init() {
        if (camera == null) {
            println("camera is null")
        }

        camera.facing = CameraView.FACING_FRONT
        camera.addCallback(getCameraCallback())
        camera.start()
        startTimer()
    }

    private fun getCameraCallback(): CameraView.Callback {
        return object : CameraView.Callback() {

            override fun onCameraOpened(cameraView: CameraView?) {
                Log.d(TAG, "onCameraOpened")
            }

            override fun onCameraClosed(cameraView: CameraView?) {
                Log.d(TAG, "onCameraClosed")
            }

            override fun onPictureTaken(cameraView: CameraView, data: ByteArray) {
                Log.d(TAG, "onPictureTaken " + data.size)
                cameraView.handler.post {
                    val file = File(
                        getExternalFilesDir(DIRECTORY_PICTURES),
                        "picture.jpg"
                    )
                    var os: OutputStream? = null
                    try {
                        os = FileOutputStream(file)
                        os.write(data)
                        os.close()
                        var bitmap = BitmapFactory.decodeFile(file.path)
                        val bOutput: Bitmap
                        val matrix = Matrix()
                        matrix.preScale(1.0f, -1.0f)
                        bOutput = Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)

                        Log.d(TAG,"image height: "+bitmap.height+" ,image width: "+bitmap.width)
                        FaceRecognizer.detectAndSetEmotionValue(bOutput, emotionName,this@PlayActivity)
                    } catch (e: IOException) {
                        Log.w(TAG, "Cannot write to $file", e)
                    } finally {
                        if (os != null) {
                            try {
                                os.close()
                            } catch (e: IOException) {
                                // Ignore
                            }

                        }
                    }
                }
            }
        }
    }

    fun newIntent(context: Context, image: Bitmap, file: File): Intent {
        val intent = Intent(Intent.ACTION_VIEW, Uri.fromFile(file))
        intent.setDataAndType(Uri.fromFile(file), "image/*")
        return intent
    }

    private fun startTimer() {
        Log.d(TAG, "Scheduling take picture")
        Timer().schedule(3000){
            camera.takePicture()
        }
    }

    private fun checkCameraHardware(context: Context): Boolean {
        return context.packageManager.hasSystemFeature(PackageManager.FEATURE_CAMERA)
    }

    override fun onResume() {
        super.onResume()

        val permissions = arrayOf(Manifest.permission.CAMERA)
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(this, permissions, 1)
        }

        camera.start()
    }

    override fun onPause() {
        camera.stop()
        super.onPause()
    }
}



