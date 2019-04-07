package com.example.cphacks19

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Handler
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_play.*


class PlayActivity : AppCompatActivity() {


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

        var counter = 5
        tv_counter.text = counter.toString()
        val handler = Handler()
        handler.postDelayed(object : Runnable {
            override fun run() {

                counter--
                tv_counter.text = counter.toString()


                if (counter > 0) {

                    handler.postDelayed(this, 1000)
                }


            }
        }, 1000)  //the time is in miliseconds

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
