package com.tcp.smarttasks.ui.navigation

import android.net.Uri
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.google.gson.Gson
import com.tcp.smarttasks.domain.model.Task
import com.tcp.smarttasks.presentation.splash.TaskSplashScreen
import com.tcp.smarttasks.presentation.task_detail.TaskDetailScreen
import com.tcp.smarttasks.presentation.task_list.TaskListScreen

@Composable
fun TaskAppNavigation(innerPadding: PaddingValues) {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "splashScreen") {
        composable("splashScreen") {
            TaskSplashScreen(innerPadding = innerPadding) {
                //callback when splash screen time ends
                //navigate to task list screen
                navController.navigate("taskList") {
                    popUpTo("splashScreen") { inclusive = true } // Remove splash from backstack
                }
            }
        }
        composable("taskList") {
            TaskListScreen(
                innerPadding = innerPadding,
                onTaskClick = { task ->
                    val taskId = task.id
                    navController.navigate("taskDetail/$taskId")
                }
            )
        }
        composable("taskDetail/{taskId}") { backStackEntry ->
            val taskId = backStackEntry.arguments?.getString("taskId")
            taskId?.let { id ->
                TaskDetailScreen(
                    innerPadding = innerPadding,
                    taskId = id,
                    onBack = { navController.popBackStack() }
                )
            }
        }
    }
}
