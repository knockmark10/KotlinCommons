package com.ia.mchaveza.kotlin_library

import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager
import java.lang.UnsupportedOperationException

/**
 * Created by mchaveza on 19/12/2017.
 */
class SharedPreferencesManager(mContext: Context) {

    val sharedPreferences: SharedPreferences = PreferenceManager.getDefaultSharedPreferences(mContext)

    fun setSharedPreference(key: String, value: Any) {
        when (value) {
            is Int -> sharedPreferences.edit().putInt(key, value).apply()
            is Float -> sharedPreferences.edit().putFloat(key, value).apply()
            is Long -> sharedPreferences.edit().putLong(key, value).apply()
            is Boolean -> sharedPreferences.edit().putBoolean(key, value).apply()
            is String -> sharedPreferences.edit().putString(key, value).apply()
            else -> throw UnsupportedOperationException("Value type not supported")
        }
    }

    inline fun <reified T : Any> getSharedPreference(key: String, defaultValue: T? = null): T =
            when (T::class) {
                Int::class -> sharedPreferences.getInt(key, defaultValue as? Int ?: -1) as T
                Float::class -> sharedPreferences.getFloat(key, defaultValue as? Float ?: -1f) as T
                Long::class -> sharedPreferences.getLong(key, defaultValue as? Long ?: -1L) as T
                Boolean::class -> sharedPreferences.getBoolean(key, defaultValue as? Boolean ?: false) as T
                String::class -> sharedPreferences.getString(key, defaultValue as? String ?: "") as T
                else -> throw UnsupportedOperationException("Value type not supported")
            }

    fun clearPreferences(preferenceName: String) =
            sharedPreferences.edit().remove(preferenceName).apply()

    @Deprecated("Use generic getSharedPreference method")
    fun getStringPreference(stringPreference: String, defaultValue: String = "") =
            sharedPreferences.getString(stringPreference, defaultValue)

    @Deprecated("Use generic getSharedPreference method")
    fun getBooleanPreference(stringPreference: String, defaultValue: Boolean = false) =
            sharedPreferences.getBoolean(stringPreference, defaultValue)

    @Deprecated("Use generic getSharedPreference method")
    fun getIntPreference(stringPreference: String, defaultValue: Int = -1) =
            sharedPreferences.getInt(stringPreference, defaultValue)

    @Deprecated("Use generic getSharedPreference method")
    fun getFloatPreference(stringPreference: String, defaultValue: Float = 0f) =
            sharedPreferences.getFloat(stringPreference, defaultValue)

    @Deprecated("Use generic setSharedPreference method")
    fun setStringPreference(preferenceName: String, preference: String) =
            sharedPreferences.edit().putString(preferenceName, preference).apply()

    @Deprecated("Use generic setSharedPreference method")
    fun setBooleanPreference(preferenceName: String, preference: Boolean) =
            sharedPreferences.edit().putBoolean(preferenceName, preference).apply()

    @Deprecated("Use generic setSharedPreference method")
    fun setIntPreference(preferenceName: String, preference: Int) =
            sharedPreferences.edit().putInt(preferenceName, preference).apply()

    @Deprecated("Use generic setSharedPreference method")
    fun setFloatPreference(preferenceName: String, preference: Float) =
            sharedPreferences.edit().putFloat(preferenceName, preference).apply()

}