package com.tcp.smarttasks

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.tcp.smarttasks.ui.navigation.TaskAppNavigation
import com.tcp.smarttasks.ui.theme.SmartTasksTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        // Install the splash screen before calling super.onCreate
        val splashScreen = installSplashScreen()

        splashScreen.setKeepOnScreenCondition {
            false // Set it to `false` to let the splash screen disappear immediately
        }

        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SmartTasksTheme {
                Scaffold(
                    modifier = Modifier
                        .fillMaxSize()
                ) { innerPadding ->
                    TaskAppNavigation(innerPadding = innerPadding)
                }
            }
        }
    }
}