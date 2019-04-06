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


class PlayActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_play)

         if (checkCameraHardware(this)){
             init()
         }
        }

    private fun init(){
        if (camera == null){
            println("camera is null")
        }

    }


    private fun checkCameraHardware(context: Context): Boolean {
        return context.packageManager.hasSystemFeature(PackageManager.FEATURE_CAMERA)
    }

    override fun onResume() {
        super.onResume()

        val permissions = arrayOf(Manifest.permission.CAMERA)
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_DENIED){
            ActivityCompat.requestPermissions(this, permissions, 1)
        }

       camera.start()
    }

    override fun onPause() {
       camera.stop()
        super.onPause()
    }





}
