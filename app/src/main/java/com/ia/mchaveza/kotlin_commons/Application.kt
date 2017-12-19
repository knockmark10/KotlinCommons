package com.ia.mchaveza.kotlin_commons

import android.app.Application
import com.facebook.stetho.Stetho

/**
 * Created by mchaveza on 19/12/2017.
 */
class Application : Application() {

    override fun onCreate() {
        super.onCreate()
        Stetho.initializeWithDefaults(this)
    }


}