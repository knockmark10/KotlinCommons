package com.ia.mchaveza.kotlin_commons

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.ia.mchaveza.kotlin_library.DateManager

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        DateManager.currentDateInMillis

    }
}
