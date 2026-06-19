package com.jyoti.moodbitenew.ui.screens
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.jyoti.moodbitenew.model.MoodMeta
import com.jyoti.moodbitenew.model.MoodType
import com.jyoti.moodbitenew.ui.theme.BgCard
import com.jyoti.moodbitenew.ui.theme.BgDark
import com.jyoti.moodbitenew.ui.theme.TextPrimary
import com.jyoti.moodbitenew.ui.theme.TextSecondary
import com.jyoti.moodbitenew.viewmodel.MoodViewModel
import com.jyoti.moodbitenew.ALL_MOODS
import com.jyoti.moodbitenew.ui.theme.HeadingColor

@Composable
fun DietPlanScreen(
    navController: NavController,
    viewModel: MoodViewModel
) {
    val uiState by viewModel.uiState.collectAsState()
    val profile = uiState.profile ?: return
    val plan = profile.dietPlan
    val mood = ALL_MOODS.first { it.type == profile.mood }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(BgDark)
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxSize()
        ) {
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(6.dp)
                        .background(mood.primaryColor)
                )
            }

            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Text("←", fontSize = 24.sp)
                    }
                    Text(
                        "${mood.emoji} ${profile.mood.name} Diet Plan",
                        style = MaterialTheme.typography.headlineSmall,
                        color = TextPrimary,
                        modifier = Modifier.weight(1f)
                    )
                }
            }

            // Each meal in a card
            item {
                DietMealCard("🌅 Breakfast", plan.breakfast, mood.primaryColor)
                DietMealCard("🍎 Morning Snack", plan.morningSnack, mood.primaryColor)
                DietMealCard("☀️ Lunch", plan.lunch, mood.primaryColor)
                DietMealCard("🌤️ Evening Snack", plan.eveningSnack, mood.primaryColor)
                DietMealCard("🌙 Dinner", plan.dinner, mood.primaryColor)
                DietMealCard("💧 Hydration", plan.hydration, Color(0xFF60A5FA))

                // Foods to avoid
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = Color(0xFFFF6B6B).copy(alpha = 0.1f)
                    ),
                    border = BorderStroke(1.dp, Color(0xFFFF6B6B).copy(alpha = 0.3f))
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text("❌ Foods to Avoid", color = Color(0xFFFF6B6B), fontWeight = FontWeight.Bold)
                        plan.avoid.forEach { item ->
                            Text("  • $item", color = TextSecondary, fontSize = 12.sp)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun DietMealCard(time: String, meal: String, color: Color) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        colors = CardDefaults.cardColors(containerColor = BgCard)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(time, color = color, fontWeight = FontWeight.Bold)
            Text(meal, style = MaterialTheme.typography.bodySmall, color = TextSecondary)
        }
    }
}