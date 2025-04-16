package io.oitech.med_application.utils

import android.content.SharedPreferences
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale

object Utils {


    fun getStringForSelectedTime(dateTime:LocalDateTime):String{
            val formatter = DateTimeFormatter.ofPattern("EEEE, MMM d, yyyy | hh:mm a", Locale.ENGLISH)//for now
            return dateTime.format(formatter)
    }

    fun getTimeFromString(string:String):LocalDateTime{
        val formatter = DateTimeFormatter.ofPattern("EEEE, MMM d, yyyy | hh:mm a", Locale.ENGLISH)//for now

        val parsedDateTime = LocalDateTime.parse(string, formatter)
        return parsedDateTime
    }



    fun Modifier.noRippleClickable(enabled: Boolean = true, onClick: () -> Unit): Modifier = composed {
        clickable(
            indication = null,
            interactionSource = remember { MutableInteractionSource() },
            enabled = enabled
        ) {
            onClick()
        }
    }




}
