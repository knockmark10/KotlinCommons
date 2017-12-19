package com.ia.mchaveza.kotlin_commons

import android.content.DialogInterface
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.util.Log
import com.ia.mchaveza.kotlin_library.DialogBuilder
import com.ia.mchaveza.kotlin_library.SharedPreferencesManager

class MainActivity : AppCompatActivity(), DialogBuilder.SimpleAlertAction {
    override fun onAccept(dialog: DialogInterface, id: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    lateinit var sharedPreferences: SharedPreferencesManager
    lateinit var dialogBuilder: DialogBuilder

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        sharedPreferences = SharedPreferencesManager(this)
        dialogBuilder = DialogBuilder(this)

        sharedPreferences.setStringPreference("Estrenos", "prueba")

        dialogBuilder.buildOneButtonDialog("Titulo", "Mensaje", "Acepto", object : DialogBuilder.SimpleAlertAction{
            override fun onAccept(dialog: DialogInterface, id: Int) {
                dialog.dismiss()
            }
        })
    }
}
