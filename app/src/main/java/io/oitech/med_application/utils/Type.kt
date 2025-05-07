package io.oitech.med_application.utils

import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.googlefonts.GoogleFont
import io.oitech.med_application.R





object Fonts {
    val lightFontInter = FontFamily(
        Font(R.font.inter_light,weight =FontWeight.Light)
    )

    val regularFontInter = FontFamily(
        Font(R.font.inter_regular, weight = FontWeight.Normal)
    )

    val mediumFontInter = FontFamily(
        Font(R.font.inter_medium, weight = FontWeight.Medium)
    )
    val semiBaldFontInter = FontFamily(
        Font(R.font.inter_semibald, weight = FontWeight.SemiBold)
    )

    val baldFontInter = FontFamily(
        Font(R.font.inter_bald, weight = FontWeight.Bold)
    )

    val extraBaldFontInter = FontFamily(
        Font(R.font.inter_extrabald,weight = FontWeight.ExtraBold)
    )
}

// Set of Material typography styles to start with
//val Typography = Typography(
//    bodyLarge = TextStyle(
//        fontFamily = FontFamily.Default,
//        fontWeight = FontWeight.Normal,
//        fontSize = 16.sp,
//        lineHeight = 24.sp,
//        letterSpacing = 0.5.sp
//    )
    /* Other default text styles to override
    titleLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 22.sp,
        lineHeight = 28.sp,
        letterSpacing = 0.sp
    ),
    labelSmall = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Medium,
        fontSize = 11.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.5.sp
    )
    */
