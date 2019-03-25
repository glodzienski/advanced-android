package com.example.twentyfive

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.view.MotionEventCompat
import android.util.Log
import android.view.MotionEvent
import kotlinx.android.synthetic.main.activity_moviment.*

class MovimentActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_moviment)

        val TAG = "TAG: "

        imageView2.setOnClickListener {
            Log.d(TAG, "click curto")
            imageView2.setImageResource(android.R.color.holo_blue_bright)
        }

        imageView2.setOnLongClickListener {
            Log.d(TAG, "click longo")
            imageView2.setImageResource(android.R.color.holo_orange_dark)

            true
        }

        imageView.setOnTouchListener { v, event ->
            val action = MotionEventCompat.getActionMasked(event)

            when (action) {
                MotionEvent.ACTION_DOWN -> {
                    Log.d(TAG, "Action Down")
                    imageView.setImageResource(android.R.color.holo_purple)
                    true
                }
                MotionEvent.ACTION_UP -> {
                    Log.d(TAG, "Action Up")
                    imageView.setImageResource(android.R.color.holo_green_dark)
                    true
                }
                else -> super.onTouchEvent(event)
            }
        }
    }
}
