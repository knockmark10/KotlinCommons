package com.ia.mchaveza.kotlin_library

import android.content.Context
import android.preference.PreferenceManager

/**
 * Created by mchaveza on 19/12/2017.
 */
class SharedPreferencesManager(mContext: Context) {

    private val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(mContext)

    fun getStringPreference(stringPreference: String, defaultValue: String = "") =
            sharedPreferences.getString(stringPreference, defaultValue)

    fun getBooleanPreference(stringPreference: String, defaultValue: Boolean = false) =
            sharedPreferences.getBoolean(stringPreference, defaultValue)

    fun getIntPreference(stringPreference: String, defaultValue: Int = -1) =
            sharedPreferences.getInt(stringPreference, defaultValue)

    fun getFloatPreference(stringPreference: String, defaultValue: Float = 0f) =
            sharedPreferences.getFloat(stringPreference, defaultValue)

    fun setStringPreference(preferenceName: String, preference: String) =
            sharedPreferences.edit().putString(preferenceName, preference).apply()

    fun setBooleanPreference(preferenceName: String, preference: Boolean) =
            sharedPreferences.edit().putBoolean(preferenceName, preference).apply()

    fun setIntPreference(preferenceName: String, preference: Int) =
            sharedPreferences.edit().putInt(preferenceName, preference).apply()

    fun setFloatPreference(preferenceName: String, preference: Float) =
            sharedPreferences.edit().putFloat(preferenceName, preference).apply()

    fun clearPreferences(preferenceName: String) =
            sharedPreferences.edit().remove(preferenceName).apply()

}