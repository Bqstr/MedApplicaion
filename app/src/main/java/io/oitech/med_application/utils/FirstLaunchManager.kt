package io.oitech.med_application.utils

import android.content.SharedPreferences
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FirstLaunchManager
@Inject constructor(private val sharedPreferences: SharedPreferences) {

    companion object {
        private const val KEY_BOOLEAN_VALUE = "FIRST_LAUNCH"
    }

    fun setBooleanValue(value: Boolean) {
        sharedPreferences.edit().putBoolean(KEY_BOOLEAN_VALUE, value).apply()
    }

    fun getBooleanValue(): Boolean {
        return sharedPreferences.getBoolean(KEY_BOOLEAN_VALUE, true) // Default value: false
    }


}