package com.ia.mchaveza.kotlin_library

import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager

/**
 * Created by mchaveza on 19/12/2017.
 */
class SharedPreferencesManager(context: Context) {

    private val mContext: Context
    private val sharedPreferences: SharedPreferences

    init {
        mContext = context
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(mContext)
    }

    fun getStringPreference(stringPreference: String) =
            sharedPreferences.getString(stringPreference, null)

    fun getStringPreference(stringPreference: String, defaultValue: String) =
            sharedPreferences.getString(stringPreference, defaultValue)

    fun getBooleanPreference(stringPreference: String, defaultValue: Boolean) =
            sharedPreferences.getBoolean(stringPreference, defaultValue)

    fun getIntPreference(stringPreference: String) =
            sharedPreferences.getInt(stringPreference, -1)

    fun setStringPreference(preferenceName: String, preference: String) =
            sharedPreferences.edit().putString(preferenceName, preference).apply()

    fun setBooleanPreference(preferenceName: String, preference: Boolean) =
            sharedPreferences.edit().putBoolean(preferenceName, preference).apply()

    fun setIntPreference(preferenceName: String, preference: Int) =
            sharedPreferences.edit().putInt(preferenceName, preference).apply()

    fun clearPreferences(preferenceName: String) =
            sharedPreferences.edit().remove(preferenceName).apply()

}