package com.example.cphacks19

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.util.Log

import com.google.android.cameraview.CameraView

import kotlinx.android.synthetic.main.activity_play.*
import android.widget.Toast
import android.content.DialogInterface
import android.support.annotation.StringRes
import android.os.Environment.DIRECTORY_PICTURES
import android.R.layout
import android.app.AlertDialog
import android.app.Dialog
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Environment
import android.os.Handler
import android.support.v4.app.DialogFragment
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.OutputStream


class PlayActivity : AppCompatActivity() {


    private val TAG = "Play activity"
    private lateinit var model : EmotionViewModel
    private var emotionName = "anger"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_play)

        if (checkCameraHardware(this)) {
            init()
        }
    }

    private fun init() {
        if (camera == null) {
            println("camera is null")
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


    private val mCallback = object : CameraView.Callback() {

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
                    var bitmap = BitmapFactory.decodeFile("/path/images/image.jpg")
                    var recognizer = FaceRecognizer(this@PlayActivity)
                    recognizer.detectAndSetEmotionValue(bitmap,emotionName)
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



