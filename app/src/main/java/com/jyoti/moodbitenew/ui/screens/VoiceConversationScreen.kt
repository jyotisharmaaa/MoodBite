package com.jyoti.moodbitenew.ui.screens

import android.Manifest
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.MicOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import com.jyoti.moodbitenew.engine.VoiceEmotionAnalyzer
import com.jyoti.moodbitenew.ui.theme.BgDark
import com.jyoti.moodbitenew.ui.theme.AccentGreen
import com.jyoti.moodbitenew.ui.theme.TextPrimary
import com.jyoti.moodbitenew.ui.theme.TextSecondary
import com.jyoti.moodbitenew.viewmodel.MoodViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.io.File

@Composable
fun VoiceConversationScreen(
    navController: NavController,
    viewModel: MoodViewModel
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    var isRecording by remember { mutableStateOf(false) }
    var isProcessing by remember { mutableStateOf(false) }
    var micPermissionGranted by remember { mutableStateOf(false) }
    var recordingTime by remember { mutableStateOf(0) }
    var voiceMetrics by remember { mutableStateOf<VoiceEmotionAnalyzer.VoiceMetrics?>(null) }
    var transcription by remember { mutableStateOf("") }
    var showResult by remember { mutableStateOf(false) }

    val voiceAnalyzer = remember {
        VoiceEmotionAnalyzer(context)
    }

    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        micPermissionGranted = isGranted
    }

    LaunchedEffect(Unit) {
        val permission = Manifest.permission.RECORD_AUDIO
        micPermissionGranted = ContextCompat.checkSelfPermission(
            context, permission
        ) == PackageManager.PERMISSION_GRANTED

        if (!micPermissionGranted) {
            permissionLauncher.launch(permission)
        }
    }

    // Timer for recording duration
    LaunchedEffect(isRecording) {
        if (isRecording) {
            while (isRecording) {
                delay(1000)
                recordingTime++
            }
        } else {
            recordingTime = 0
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(BgDark)
    ) {
        if (!micPermissionGranted) {
            // Permission denied screen
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(32.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Mic,
                    contentDescription = "Microphone",
                    modifier = Modifier.size(64.dp),
                    tint = AccentGreen
                )
                Spacer(modifier = Modifier.height(24.dp))
                Text(
                    "Microphone Permission Required",
                    style = MaterialTheme.typography.headlineMedium,
                    color = TextPrimary,
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    "MoodBite needs microphone access to analyze your voice and detect your mood.",
                    color = TextSecondary,
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(24.dp))
                Button(
                    onClick = { permissionLauncher.launch(Manifest.permission.RECORD_AUDIO) },
                    colors = ButtonDefaults.buttonColors(containerColor = AccentGreen)
                ) {
                    Text("Grant Permission", color = BgDark)
                }
            }
        } else {
            // Main conversation screen
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Header
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 24.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        "🎙️ Voice Mood Detection",
                        style = MaterialTheme.typography.headlineSmall,
                        color = TextPrimary
                    )
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.Close, contentDescription = "Close", tint = TextPrimary)
                    }
                }

                // Instructions
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 24.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = Color(0xFF1E2130)
                    ),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            "Tell me: \"How are you feeling today?\"",
                            style = MaterialTheme.typography.titleMedium,
                            color = TextPrimary,
                            textAlign = TextAlign.Center
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            "Speak naturally for 5-30 seconds. I'll analyze your tone, emotion, and mood.",
                            style = MaterialTheme.typography.bodySmall,
                            color = TextSecondary,
                            textAlign = TextAlign.Center
                        )
                    }
                }

                // Recording visualizer (animated bars)
                if (isRecording) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(100.dp)
                            .padding(vertical = 24.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(4.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            repeat(5) { index ->
                                val barHeight = (40 + (index * 15)) % 80
                                Box(
                                    modifier = Modifier
                                        .width(8.dp)
                                        .height(barHeight.dp)
                                        .background(
                                            color = AccentGreen,
                                            shape = RoundedCornerShape(4.dp)
                                        )
                                        .animateContentSize()
                                )
                            }
                        }
                    }
                }

                // Recording time
                if (isRecording) {
                    Text(
                        "Recording: ${recordingTime}s",
                        style = MaterialTheme.typography.bodyLarge,
                        color = AccentGreen,
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(24.dp))
                }

                Spacer(modifier = Modifier.weight(1f))

                // Microphone button
                if (!isProcessing) {
                    Button(
                        onClick = {
                            if (isRecording) {
                                // Stop recording
                                val audioFile = File(
                                    context.cacheDir,
                                    "voice_${System.currentTimeMillis()}.wav"
                                )
                                val metrics = voiceAnalyzer.stopRecording()
                                voiceMetrics = metrics
                                isRecording = false
                                isProcessing = true

                                // Process the audio
                                scope.launch {
                                    try {
                                        delay(1500) // Simulate processing

                                        // For demo: set dummy transcription
                                        // In production: use Google Speech-to-Text or Agora STT
                                        transcription = "I'm feeling quite good today, had a nice morning"

                                        // Analyze sentiment
                                        val sentiment = voiceAnalyzer.analyzeSentiment(transcription)

                                        // Detect mood
                                        val (mood, confidence) = voiceAnalyzer.detectMoodFromVoice(
                                            metrics,
                                            sentiment
                                        )

                                        // Update ViewModel
                                        viewModel.selectMood(mood)

                                        showResult = true
                                        isProcessing = false
                                    } catch (e: Exception) {
                                        isProcessing = false
                                    }
                                }
                            } else {
                                // Start recording
                                val audioFile = File(
                                    context.cacheDir,
                                    "voice_${System.currentTimeMillis()}.wav"
                                )
                                voiceAnalyzer.startRecording(audioFile.absolutePath)
                                isRecording = true
                                recordingTime = 0
                            }
                        },
                        modifier = Modifier.size(80.dp),
                        shape = RoundedCornerShape(40.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (isRecording) Color(0xFFFF6B6B) else AccentGreen
                        )
                    ) {
                        Icon(
                            imageVector = if (isRecording) Icons.Default.MicOff else Icons.Default.Mic,
                            contentDescription = if (isRecording) "Stop" else "Start",
                            modifier = Modifier.size(40.dp),
                            tint = if (isRecording) Color.White else BgDark
                        )
                    }
                }

                // Processing indicator
                if (isProcessing) {
                    CircularProgressIndicator(
                        color = AccentGreen,
                        modifier = Modifier.size(60.dp)
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        "Analyzing your voice...",
                        color = TextSecondary,
                        textAlign = TextAlign.Center
                    )
                }

                Spacer(modifier = Modifier.height(48.dp))
            }
        }
    }

    // Show result dialog
    if (showResult) {
        AlertDialog(
            onDismissRequest = { showResult = false },
            title = { Text("Mood Detected! 🎉") },
            text = {
                Column {
                    Text(
                        "Detected Mood: ${viewModel.uiState.value.selectedMood?.name ?: "Unknown"}",
                        style = MaterialTheme.typography.bodyLarge
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        "Confidence: ${(voiceMetrics?.voiceStress?.times(100)?.toInt() ?: 0)}%",
                        style = MaterialTheme.typography.bodyMedium,
                        color = TextSecondary
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        "Transcription: \"$transcription\"",
                        style = MaterialTheme.typography.labelSmall,
                        color = TextSecondary
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        "Would you like food recommendations for this mood?",
                        style = MaterialTheme.typography.bodySmall,
                        color = TextSecondary
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        viewModel.analyzeAndNavigate()
                        showResult = false
                        navController.navigate("result") {
                            popUpTo("voice") { inclusive = true }
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = AccentGreen)
                ) {
                    Text("Get Food Plan", color = BgDark)
                }
            },
            dismissButton = {
                Button(
                    onClick = {
                        showResult = false
                        navController.popBackStack()
                    },
                    colors = ButtonDefaults.outlinedButtonColors()
                ) {
                    Text("Try Again")
                }
            }
        )
    }

    // Cleanup
    DisposableEffect(Unit) {
        onDispose {
            voiceAnalyzer.release()
        }
    }
}