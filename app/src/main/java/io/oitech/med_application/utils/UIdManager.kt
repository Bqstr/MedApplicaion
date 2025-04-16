package io.oitech.med_application.utils

import android.content.SharedPreferences
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UIdManager @Inject constructor(private val sharedPreferences: SharedPreferences) {

    companion object {
        private const val UIdKEY_VALUE = "UIdKEY_VALUE"
    }

    fun setUId(value: String) {
        sharedPreferences.edit().putString(UIdKEY_VALUE, value).apply()
    }

    fun getUId(): String {
        return sharedPreferences.getString(UIdKEY_VALUE, "") ?:""// Default value: false
    }


}