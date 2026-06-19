package com.jyoti.moodbitenew.ui.components

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jyoti.moodbitenew.model.MoodMeta
import com.jyoti.moodbitenew.model.MoodType
import com.jyoti.moodbitenew.ui.theme.BgCard
import com.jyoti.moodbitenew.ui.theme.BgDark
import com.jyoti.moodbitenew.ui.theme.TextPrimary

@Composable
fun MoodGrid(
    moods: List<MoodMeta>,
    selectedMood: MoodType?,
    onMoodSelected: (MoodMeta) -> Unit
) {
    // Chunk moods into rows of 3 to avoid using LazyVerticalGrid inside a verticalScroll
    val rows = moods.chunked(3)

    Column(
        verticalArrangement = Arrangement.spacedBy(12.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        rows.forEach { rowMoods ->
            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                rowMoods.forEach { mood ->
                    val isSelected = mood.type == selectedMood
                    MoodCard(
                        mood = mood,
                        isSelected = isSelected,
                        onMoodSelected = onMoodSelected,
                        modifier = Modifier.weight(1f)
                    )
                }
                // Fill empty slots if row is not full to maintain grid alignment
                if (rowMoods.size < 3) {
                    repeat(3 - rowMoods.size) {
                        Spacer(modifier = Modifier.weight(1f))
                    }
                }
            }
        }
    }
}

@Composable
private fun MoodCard(
    mood: MoodMeta,
    isSelected: Boolean,
    onMoodSelected: (MoodMeta) -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .aspectRatio(1F)
            .clickable { onMoodSelected(mood) }
            .animateContentSize(),
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected) mood.primaryColor else BgCard
        ),
        border = if (isSelected) BorderStroke(2.dp, mood.primaryColor) else null,
        shape = RoundedCornerShape(20.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(mood.emoji, fontSize = 32.sp)
            Text(
                mood.label,
                fontSize = 12.sp,
                color = if (isSelected) BgDark else TextPrimary,
                fontWeight = FontWeight.Bold
            )
        }
    }
}
