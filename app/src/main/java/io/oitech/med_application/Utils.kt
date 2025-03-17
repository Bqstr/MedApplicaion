package io.oitech.med_application

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale

object Utils {
    fun getItemForSelectedTime(dateTime:LocalDateTime):String{
            val formatter = DateTimeFormatter.ofPattern("EEEE, MMM d, yyyy | hh:mm a", Locale.ENGLISH)//for now
            return dateTime.format(formatter)
    }
}