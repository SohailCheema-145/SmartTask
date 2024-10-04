package com.tcp.smarttasks.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.tcp.smarttasks.R

// Define font families
val AmsiPro = FontFamily(
    Font(R.font.amsipro_regular), // replace with your actual font resource name
    Font(R.font.amsipro_bold, FontWeight.Bold) // replace with your actual font resource name
)

// Set of Material typography styles to start with
val Typography = Typography(
    bodyLarge = TextStyle(
        fontFamily = AmsiPro,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
    ),
    bodyMedium = TextStyle(
        fontFamily = AmsiPro,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        color = StBrown
    ),
    titleMedium = TextStyle(
        fontFamily = AmsiPro,
        fontWeight = FontWeight.Bold,
        color = StRed
    ),
    titleLarge = TextStyle(
        fontFamily = AmsiPro,
        fontWeight = FontWeight.Bold,
        color = StOrange
    ),
    labelSmall = TextStyle(
        fontFamily = AmsiPro,
        fontWeight = FontWeight.Normal,
        color = StBrown
    ),
    headlineLarge = TextStyle(
        fontFamily = AmsiPro,
        fontWeight = FontWeight.Bold,
        color = StRed
    ),
    headlineMedium = TextStyle(
        fontFamily = AmsiPro,
        fontWeight = FontWeight.Bold,
        color = Color.White
    )

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
)