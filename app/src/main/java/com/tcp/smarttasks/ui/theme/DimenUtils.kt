package com.tcp.smarttasks.ui.theme

import android.content.res.Resources
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

fun pxToDp(px: Int): Dp {
    return (px / Resources.getSystem().displayMetrics.density).toInt().dp
}