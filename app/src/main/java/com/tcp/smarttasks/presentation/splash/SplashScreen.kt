package com.tcp.smarttasks.presentation.splash

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import com.tcp.smarttasks.R
import kotlinx.coroutines.delay
import network.chaintech.sdpcomposemultiplatform.sdp

@Composable
fun TaskSplashScreen(innerPadding: PaddingValues, onSplashTimeEnded: () -> Unit) {
    var isLoading by remember { mutableStateOf(true) }

    LaunchedEffect(Unit) {
        delay(3000) // 3-second delay for splash
        isLoading = false
        onSplashTimeEnded.invoke()
    }
    if (isLoading) {
        //splash screen design
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(innerPadding)
                .padding(horizontal = 50.sdp)
                .padding(top = 80.sdp),
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Image(painter = painterResource(R.drawable.logo), contentDescription = "")
            Image(
                painter = painterResource(R.drawable.intro_illustration),
                contentDescription = "",
                modifier = Modifier
                    .height(200.sdp)
                    .fillMaxWidth(),
                contentScale = ContentScale.FillBounds
            )
        }
    }
}