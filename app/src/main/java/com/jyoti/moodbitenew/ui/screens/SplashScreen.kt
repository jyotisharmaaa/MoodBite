package com.jyoti.moodbitenew.ui.screens

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.jyoti.moodbitenew.ui.theme.BgDark
import com.jyoti.moodbitenew.ui.theme.TextPrimary
import com.jyoti.moodbitenew.ui.theme.TextSecondary
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(navController: NavController) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(BgDark),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            // Animated emoji
            Text("🥗", fontSize = 80.sp)

            // App name with fade-in
            Text(
                "MoodBite",
                style = MaterialTheme.typography.displayMedium,
                color = TextPrimary,
                modifier = Modifier.animateContentSize()
            )

            Text(
                "Eat for how you feel ✨",
                style = MaterialTheme.typography.bodyMedium,
                color = TextSecondary
            )
        }
    }

    // Auto-navigate after 2.5s
    LaunchedEffect(Unit) {
        delay(2500)
        navController.navigate("home") {
            popUpTo("splash") { inclusive = true }
        }
    }
}