package io.oitech.med_application.chatbot

import android.content.Context
import java.util.Calendar

object ChatbotGreetingManager {

    private const val PREFS_NAME = "chatbot_prefs"
    private const val KEY_LAST_GREETING_TIME = "last_greeting_time"

    fun hasGreetedToday(context: Context): Boolean {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val lastGreeted = prefs.getLong(KEY_LAST_GREETING_TIME, 0L)
        val now = System.currentTimeMillis()

        val calendarNow = Calendar.getInstance().apply { timeInMillis = now }
        val calendarLast = Calendar.getInstance().apply { timeInMillis = lastGreeted }

        return calendarNow.get(Calendar.YEAR) == calendarLast.get(Calendar.YEAR) &&
                calendarNow.get(Calendar.DAY_OF_YEAR) == calendarLast.get(Calendar.DAY_OF_YEAR)
    }

    fun markGreetedToday(context: Context) {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        prefs.edit().putLong(KEY_LAST_GREETING_TIME, System.currentTimeMillis()).apply()
    }

    fun generateGreeting(userName: String): String {
        return "👋 Hello $userName! I'm your medical assistant.\n\nTell me your symptoms and I'll try to help you."
    }
}