package com.jyoti.moodbitenew.ui.app

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.jyoti.moodbitenew.ui.screens.CameraMoodDetectionScreen
import com.jyoti.moodbitenew.ui.screens.DietPlanScreen
import com.jyoti.moodbitenew.ui.screens.MoodSelectionScreen
import com.jyoti.moodbitenew.ui.screens.ResultScreen
import com.jyoti.moodbitenew.ui.screens.SplashScreen
import com.jyoti.moodbitenew.ui.screens.VoiceConversationScreen
import com.jyoti.moodbitenew.viewmodel.MoodViewModel

@Composable
fun MoodBiteApp() {

    val navController = rememberNavController()
    val moodViewModel: MoodViewModel = viewModel()

    NavHost(
        navController = navController,
        startDestination = "splash"
    ) {

        composable("splash") {
            SplashScreen(navController)
        }

        composable("home") {
            MoodSelectionScreen(
                navController = navController,
                viewModel = moodViewModel
            )
        }

        composable("result") {
            ResultScreen(
                navController = navController,
                viewModel = moodViewModel
            )
        }

        composable("diet") {
            DietPlanScreen(
                navController = navController,
                viewModel = moodViewModel
            )
        }

        composable("camera") {
            CameraMoodDetectionScreen(
                navController = navController,
                viewModel = moodViewModel
            )
        }
        composable("voice") {
            VoiceConversationScreen(
                navController = navController,
                viewModel = moodViewModel)
        }
    }
}