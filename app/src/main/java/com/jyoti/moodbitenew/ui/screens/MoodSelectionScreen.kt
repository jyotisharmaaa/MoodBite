package com.jyoti.moodbitenew.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.jyoti.moodbitenew.ALL_MOODS
import com.jyoti.moodbitenew.ui.components.MoodGrid
import com.jyoti.moodbitenew.ui.theme.AccentGreen
import com.jyoti.moodbitenew.ui.theme.BgDark
import com.jyoti.moodbitenew.ui.theme.TextHint
import com.jyoti.moodbitenew.ui.theme.TextPrimary
import com.jyoti.moodbitenew.ui.theme.TextSecondary
import com.jyoti.moodbitenew.viewmodel.MoodViewModel

@Composable
fun MoodSelectionScreen(
    navController: NavController,
    viewModel: MoodViewModel
) {

    val uiState by viewModel.uiState.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BgDark)
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        // Camera Card
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .clickable {
                    navController.navigate("camera")
                },
            colors = CardDefaults.cardColors(
                containerColor = AccentGreen.copy(alpha = 0.15f)
            ),
            border = BorderStroke(1.dp, AccentGreen),
            shape = RoundedCornerShape(16.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {

                Text("📷", fontSize = 32.sp)

                Spacer(modifier = Modifier.width(12.dp))

                Column(
                    modifier = Modifier.weight(1f)
                ) {

                    Text(
                        text = "Detect with Camera",
                        style = MaterialTheme.typography.titleMedium,
                        color = AccentGreen,
                        fontWeight = FontWeight.Bold
                    )

                    Text(
                        text = "AI will analyze your facial expression",
                        style = MaterialTheme.typography.bodySmall,
                        color = TextSecondary
                    )
                }

                Text(
                    "→",
                    fontSize = 22.sp,
                    color = AccentGreen
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Voice Card
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .clickable {
                    navController.navigate("voice")
                },
            colors = CardDefaults.cardColors(
                containerColor = Color(0xFFFBBF24).copy(alpha = 0.15f)
            ),
            border = BorderStroke(
                1.dp,
                Color(0xFFFBBF24)
            ),
            shape = RoundedCornerShape(16.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {

                Text("🎙️", fontSize = 32.sp)

                Spacer(modifier = Modifier.width(12.dp))

                Column(
                    modifier = Modifier.weight(1f)
                ) {

                    Text(
                        text = "Voice Conversation",
                        style = MaterialTheme.typography.titleMedium,
                        color = Color(0xFFFBBF24),
                        fontWeight = FontWeight.Bold
                    )

                    Text(
                        text = "Highest accuracy - speak naturally",
                        style = MaterialTheme.typography.bodySmall,
                        color = TextSecondary
                    )
                }

                Text(
                    "→",
                    fontSize = 22.sp,
                    color = Color(0xFFFBBF24)
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "Welcome \uD83E\uDD1D️",
            style = MaterialTheme.typography.displaySmall,
            color = TextPrimary
        )

        Text(
            text = "How are you feeling right now?",
            style = MaterialTheme.typography.bodyLarge,
            color = TextSecondary
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Mood Grid
        MoodGrid(
            moods = ALL_MOODS,
            selectedMood = uiState.selectedMood,
            onMoodSelected = {
                viewModel.selectMood(it.type)
            }
        )

        Spacer(modifier = Modifier.height(24.dp))

        if (uiState.isAnalyzing) {

            CircularProgressIndicator(
                color = AccentGreen,
                modifier = Modifier.size(32.dp)
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "🤖 AI analyzing your mood...",
                color = TextSecondary
            )

            Spacer(modifier = Modifier.height(16.dp))
        }

        Button(
            onClick = {
                viewModel.analyzeAndNavigate()
            },
            enabled = uiState.selectedMood != null &&
                    !uiState.isAnalyzing,
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = AccentGreen,
                disabledContainerColor = TextHint
            )
        ) {
            Text(
                "✨ Get My Food Plan",
                color = BgDark,
                fontWeight = FontWeight.Bold
            )
        }
    }

    LaunchedEffect(uiState.analysisComplete) {
        if (uiState.analysisComplete) {
            navController.navigate("result")
        }
    }
}