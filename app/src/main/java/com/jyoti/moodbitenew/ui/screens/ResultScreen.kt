package com.jyoti.moodbitenew.ui.screens
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.jyoti.moodbitenew.ui.components.FoodCard
import com.jyoti.moodbitenew.ui.theme.AccentGreen
import com.jyoti.moodbitenew.ui.theme.BgCard
import com.jyoti.moodbitenew.ui.theme.BgDark
import com.jyoti.moodbitenew.ui.theme.TextPrimary
import com.jyoti.moodbitenew.ui.theme.TextSecondary
import androidx.compose.foundation.shape.RoundedCornerShape
import com.jyoti.moodbitenew.viewmodel.MoodViewModel
import com.jyoti.moodbitenew.ALL_MOODS
import com.jyoti.moodbitenew.ui.theme.HeadingColor
import androidx.activity.compose.BackHandler

@Composable
fun ResultScreen(
    navController: NavController,
    viewModel: MoodViewModel
) {
    val uiState by viewModel.uiState.collectAsState()
    val profile = uiState.profile ?: return
    val mood = ALL_MOODS.first { it.type == profile.mood }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        mood.primaryColor.copy(alpha = 0.15f),
                        BgDark
                    )
                )
            )
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            // Header
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 20.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(onClick = {
                        viewModel.resetAnalysis()

                        navController.navigate("home") {
                            popUpTo("home") {
                                inclusive = false
                            }
                            launchSingleTop = true
                        }
                    }) {
                        Text("←", fontSize = 24.sp)
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Column {
                        Text(
                            profile.mood.name.uppercase(),
                            style = MaterialTheme.typography.headlineSmall,
                            color = HeadingColor
                        )
                        Text(
                            profile.neurotransmitter,
                            style = MaterialTheme.typography.labelMedium,
                            color = mood.primaryColor
                        )
                    }
                }
            }

            // Large emoji + description
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp),
                    colors = CardDefaults.cardColors(containerColor = BgCard),
                    shape = RoundedCornerShape(20.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(mood.emoji, fontSize = 64.sp)
                        Text(
                            profile.description,
                            style = MaterialTheme.typography.bodyMedium,
                            color = TextSecondary,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }

            // AI Insight
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = mood.primaryColor.copy(alpha = 0.1f)
                    ),
                    border = BorderStroke(1.dp, mood.primaryColor.copy(alpha = 0.3f))
                ) {
                    Text(
                        "💡 ${uiState.aiInsight}",
                        modifier = Modifier.padding(16.dp),
                        style = MaterialTheme.typography.bodyMedium,
                        color = mood.primaryColor
                    )
                }
            }

            // Foods header
            item {
                Text(
                    "🍽️ Recommended Foods",
                    style = MaterialTheme.typography.headlineSmall,
                    color = TextPrimary,
                    modifier = Modifier.padding(vertical = 12.dp)
                )
            }

            // Food list with staggered animations
            items(profile.foods.size) { index ->
                FoodCard(
                    food = profile.foods[index],
                    moodColor = mood.primaryColor,
                    delay = index * 80L
                )
                Spacer(modifier = Modifier.height(12.dp))
            }

            // Activities
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 16.dp),
                    colors = CardDefaults.cardColors(containerColor = BgCard)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            "🎯 Activities for You",
                            style = MaterialTheme.typography.headlineSmall,
                            color = TextPrimary
                        )
                        profile.activities.forEach { activity ->
                            Text(
                                activity,
                                style = MaterialTheme.typography.bodyMedium,
                                color = TextSecondary,
                                modifier = Modifier.padding(top = 8.dp)
                            )
                        }
                    }
                }
            }

            // Action buttons
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Button(
                        onClick = {
                            viewModel.resetAnalysis()

                            navController.navigate("home") {
                                popUpTo("home") {
                                    inclusive = false
                                }
                                launchSingleTop = true
                            }
                        },
                        modifier = Modifier
                            .weight(1f)
                            .height(48.dp),
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = TextSecondary),
                        border = BorderStroke(1.dp, TextSecondary)
                    ) {
                        Text("Change Mood")
                    }

                    Button(
                        onClick = { navController.navigate("diet") },
                        modifier = Modifier
                            .weight(1f)
                            .height(48.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = AccentGreen)
                    ) {
                        Text("📋 Diet Plan", color = BgDark)
                    }
                }
            }
        }
    }
    BackHandler {

        viewModel.resetAnalysis()

        navController.navigate("home") {
            popUpTo("home") {
                inclusive = false
            }
            launchSingleTop = true
        }
    }
}
