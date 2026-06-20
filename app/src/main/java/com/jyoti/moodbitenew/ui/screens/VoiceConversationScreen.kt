package com.jyoti.moodbitenew.ui.screens

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.MicExternalOn
import androidx.compose.material.icons.filled.MicOff
import androidx.compose.material.icons.filled.ThumbUp
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
import java.util.*

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
    var detectedMoodName by remember { mutableStateOf("") }
    var detectedConfidence by remember { mutableStateOf(0f) }
    var statusMessage by remember { mutableStateOf("") }

    val voiceAnalyzer = remember {
        VoiceEmotionAnalyzer(context)
    }

    var speechRecognizer by remember { mutableStateOf<SpeechRecognizer?>(null) }

    LaunchedEffect(Unit) {
        speechRecognizer = if (SpeechRecognizer.isRecognitionAvailable(context)) {
            SpeechRecognizer.createSpeechRecognizer(context)
        } else {
            null
        }
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

    LaunchedEffect(isRecording) {
        if (isRecording) {
            recordingTime = 0
            while (isRecording) {
                delay(1000)
                recordingTime++
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(BgDark)
    ) {
        if (!micPermissionGranted) {
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
                    "MoodBite needs microphone access to analyze your voice.",
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
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
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
                            "Speak naturally for 5-20 seconds.",
                            style = MaterialTheme.typography.bodySmall,
                            color = TextSecondary,
                            textAlign = TextAlign.Center
                        )
                    }
                }

                if (isRecording) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(100.dp)
                            .padding(vertical = 24.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
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

                    Text(
                       // "Recording: ${recordingTime}s",
                        text = "Press to start speak",
                        style = MaterialTheme.typography.bodyLarge,
                        color = AccentGreen,
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(24.dp))
                }

                if (statusMessage.isNotEmpty()) {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 16.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = Color(0xFF1E2130)
                        ),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text(
                            statusMessage,
                            style = MaterialTheme.typography.bodyMedium,
                            color = AccentGreen,
                            modifier = Modifier.padding(12.dp),
                            textAlign = TextAlign.Center
                        )
                    }
                }

                if (transcription.isNotEmpty()) {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 16.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = Color(0xFF1E2130)
                        ),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Text(
                                "You said:",
                                style = MaterialTheme.typography.labelSmall,
                                color = TextSecondary
                            )
                            Text(
                                "\"$transcription\"",
                                style = MaterialTheme.typography.bodyMedium,
                                color = TextPrimary,
                                modifier = Modifier.padding(top = 8.dp)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.weight(1f))

                if (!isProcessing && !isRecording) {
                    Button(
                        onClick = {
                            Log.d("VoiceScreen", "🔴 Starting recording...")
                            isRecording = true
                            recordingTime = 0
                            transcription = ""
                            statusMessage = ""

                            val audioFile = File(
                                context.cacheDir,
                                "voice_${System.currentTimeMillis()}.m4a"
                            )
                            voiceAnalyzer.startRecording(audioFile.absolutePath)
                        },
                        modifier = Modifier.fillMaxWidth().padding(16.dp),
                    //   shape = RoundedCornerShape(40.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = AccentGreen)
                    ) /*{
                        Icon(
                            imageVector = Icons.Default.Mic,
                            contentDescription = "Start",
                            modifier = Modifier.size(40.dp),
                            tint = BgDark
                        )
                    }*/
                    {
                        Text("Let's Start", fontSize = 32.sp, color = BgDark)
                    }
                } else if (isRecording) {
                    Button(
                        onClick = {
                            Log.d("VoiceScreen", "⏹️ Stopping recording...")
                            isRecording = false
                            val metrics = voiceAnalyzer.stopRecording()
                            voiceMetrics = metrics

                            Log.d("VoiceScreen", """
                                Recording stopped:
                                • Duration: ${metrics.recordingDuration}ms
                                • Speech Rate: ${metrics.speechRate.toInt()} WPM
                            """.trimIndent())

                            isProcessing = true
                            statusMessage = "🎙️ Ready to speak...."

                            // Start speech recognition
                            scope.launch {
                                try {
                                    Log.d("VoiceScreen", "→ Starting speech recognition...")
                                    var recognitionComplete = false

                                    startSpeechRecognition(
                                        speechRecognizer = speechRecognizer!!,
                                        context = context,
                                        onListeningStarted = {
                                            statusMessage = "🎤 Listening..."
                                        },
                                        onResult = { recognizedText ->
                                            transcription = recognizedText
                                            recognitionComplete = true
                                            Log.d("VoiceScreen", "✓ Got transcription: \"$recognizedText\"")
                                        },
                                        onError = { error ->
                                            statusMessage = "Error: $error"
                                            recognitionComplete = true
                                            Log.e("VoiceScreen", "✗ Error: $error")
                                        }
                                    )

                                    // Wait for recognition to complete
                                    var waitTime = 0
                                    while (!recognitionComplete && waitTime < 8000) {
                                        delay(100)
                                        waitTime += 100
                                    }

                                    Log.d("VoiceScreen", "Recognition complete. Transcription: \"$transcription\"")

                                    // Check if we have transcription
                                    if (transcription.isEmpty()) {
                                        statusMessage = "No speech recognized. Please try again."
                                        isProcessing = false
                                        return@launch
                                    }

                                    // Analyze sentiment
                                    val sentiment = voiceAnalyzer.analyzeSentiment(transcription)
                                    Log.d("VoiceScreen", """
                                        Sentiment:
                                        • Positive: ${(sentiment.positive * 100).toInt()}%
                                        • Negative: ${(sentiment.negative * 100).toInt()}%
                                    """.trimIndent())

                                    // Detect mood
                                    val (mood, confidence) = voiceAnalyzer.detectMoodFromVoice(
                                        voiceMetrics!!,
                                        sentiment,
                                        transcription
                                    )

                                    Log.d("VoiceScreen", """
                                        ✓ Mood Detected:
                                        • Mood: $mood
                                        • Confidence: ${(confidence * 100).toInt()}%
                                    """.trimIndent())

                                    // Update ViewModel and show result
                                    viewModel.selectMood(mood)
                                    detectedMoodName = mood.name
                                    detectedConfidence = confidence
                                    statusMessage = ""
                                    showResult = true
                                    isProcessing = false

                                } catch (e: Exception) {
                                    Log.e("VoiceScreen", "Exception: ${e.message}", e)
                                    statusMessage = "Error occurred"
                                    isProcessing = false
                                }
                            }
                        },
                        modifier = Modifier.size(80.dp),
                        shape = RoundedCornerShape(40.dp),
                        colors = ButtonDefaults.buttonColors(Color(0xFF34D399))
                    ) {
                        Icon(
                            imageVector = Icons.Default.MicExternalOn,
                            contentDescription = "Stop",
                            modifier = Modifier.size(40.dp),
                            tint = Color.White
                        )
                    }
                }

                if (isProcessing) {
                    CircularProgressIndicator(
                        color = AccentGreen,
                        modifier = Modifier.size(60.dp)
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        "Analyzing mood...",
                        color = TextSecondary
                    )
                }

                Spacer(modifier = Modifier.height(48.dp))
            }
        }
    }

    // Result Dialog - ONLY shown when mood is detected
    if (showResult) {
        AlertDialog(
            onDismissRequest = { showResult = false },
            title = { Text("Mood Detected! 🎉") },
            text = {
                Column {
                    Text(
                        "Your Mood: $detectedMoodName",
                        style = MaterialTheme.typography.bodyLarge,
                        color = TextPrimary,
                        fontWeight = androidx.compose.ui.text.font.FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        "Confidence: ${(detectedConfidence * 100).toInt()}%",
                        style = MaterialTheme.typography.bodyMedium,
                        color = TextSecondary
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    if (transcription.isNotEmpty()) {
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            colors = CardDefaults.cardColors(
                                containerColor = Color(0xFF1E2130)
                            ),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Column(modifier = Modifier.padding(12.dp)) {
                                Text(
                                    "You said:",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = TextSecondary
                                )
                                Text(
                                    "\"$transcription\"",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = TextPrimary,
                                    modifier = Modifier.padding(top = 8.dp)
                                )
                            }
                        }
                        Spacer(modifier = Modifier.height(12.dp))
                    }
                    if (voiceMetrics != null) {
                        Text(
                            "Voice Analysis:",
                            style = MaterialTheme.typography.labelSmall,
                            color = TextSecondary
                        )
                        Text(
                            "Speech Rate: ${voiceMetrics!!.speechRate.toInt()} WPM\n" +
                                    "Pitch: ${voiceMetrics!!.averagePitch.toInt()} Hz\n" +
                                    "Stress Level: ${(voiceMetrics!!.voiceStress * 100).toInt()}%\n" +
                                    "Energy: ${(voiceMetrics!!.voiceEnergyLevel * 100).toInt()}%",
                            style = MaterialTheme.typography.labelSmall,
                            color = TextPrimary,
                            modifier = Modifier.padding(top = 8.dp)
                        )
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        "Get personalized food recommendations based on your mood?",
                        style = MaterialTheme.typography.bodySmall,
                        color = TextSecondary
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        Log.d("VoiceScreen", "User confirmed - navigating to food results")
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
                        Log.d("VoiceScreen", "User cancelled - resetting")
                        showResult = false
                        transcription = ""
                        statusMessage = ""
                    },
                    colors = ButtonDefaults.outlinedButtonColors()
                ) {
                    Text("Try Again")
                }
            }
        )
    }

    DisposableEffect(Unit) {
        onDispose {
            voiceAnalyzer.release()
            speechRecognizer?.destroy()
        }
    }
}

private fun startSpeechRecognition(
    speechRecognizer: SpeechRecognizer,
    context: android.content.Context,
    onListeningStarted: () -> Unit = {},
    onResult: (String) -> Unit,
    onError: (String) -> Unit
) {
    var latestPartialText = ""
    Log.d("SpeechRecognition", "🎙️ Initializing speech recognizer...")

    val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
        putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
        putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.ENGLISH)
        putExtra(RecognizerIntent.EXTRA_PARTIAL_RESULTS, true)
        putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 5)
        putExtra(RecognizerIntent.EXTRA_SPEECH_INPUT_COMPLETE_SILENCE_LENGTH_MILLIS, 5000)
        putExtra(RecognizerIntent.EXTRA_SPEECH_INPUT_POSSIBLY_COMPLETE_SILENCE_LENGTH_MILLIS, 3000)
        putExtra(RecognizerIntent.EXTRA_SPEECH_INPUT_MINIMUM_LENGTH_MILLIS, 1000)
        putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE, context.packageName)
    }

    speechRecognizer.setRecognitionListener(
        object : RecognitionListener {
            override fun onReadyForSpeech(params: android.os.Bundle?) {
                Log.d("SpeechRecognition", "✓ Ready for speech")
                onListeningStarted()
            }

            override fun onBeginningOfSpeech() {
                Log.d("SpeechRecognition", "→ Beginning of speech")
            }

            override fun onRmsChanged(rmsdB: Float) {}
            override fun onBufferReceived(buffer: ByteArray?) {}

            override fun onEndOfSpeech() {
                Log.d("SpeechRecognition", "→ End of speech")
                if (latestPartialText.isNotBlank()) {
                    Log.d("SpeechRecognition", "Using partial: $latestPartialText")
                    onResult(latestPartialText)
                }
            }

            override fun onError(error: Int) {
                val errorMsg = when (error) {
                    SpeechRecognizer.ERROR_NO_MATCH -> "No speech recognized"
                    SpeechRecognizer.ERROR_SPEECH_TIMEOUT -> "No input detected"
                    SpeechRecognizer.ERROR_NETWORK -> "Network error"
                    SpeechRecognizer.ERROR_AUDIO -> "Audio error"
                    else -> "Recognition error"
                }
                Log.e("SpeechRecognition", "✗ Error: $errorMsg")
                if (latestPartialText.isNotBlank()) {
                    onResult(latestPartialText)
                } else {
                    onError(errorMsg)
                }
            }

            override fun onResults(results: android.os.Bundle?) {
                try {
                    val matches = results?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
                    if (!matches.isNullOrEmpty() && matches[0].isNotEmpty()) {
                        Log.d("SpeechRecognition", "✓ Final result: ${matches[0]}")
                        onResult(matches[0])
                    } else if (latestPartialText.isNotBlank()) {
                        Log.d("SpeechRecognition", "Using partial instead of final")
                        onResult(latestPartialText)
                    } else {
                        onError("No speech recognized")
                    }
                } catch (e: Exception) {
                    Log.e("SpeechRecognition", "Error: ${e.message}")
                    if (latestPartialText.isNotBlank()) {
                        onResult(latestPartialText)
                    } else {
                        onError("Error processing speech")
                    }
                }
            }

            override fun onPartialResults(partialResults: android.os.Bundle?) {
                try {
                    val partials = partialResults?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
                    if (!partials.isNullOrEmpty()) {
                        latestPartialText = partials[0]
                        Log.d("SpeechRecognition", "~ Partial: $latestPartialText")
                    }
                } catch (e: Exception) {
                    Log.e("SpeechRecognition", "Partial error: ${e.message}")
                }
            }

            override fun onEvent(eventType: Int, params: android.os.Bundle?) {}
        }
    )

    try {
        speechRecognizer.startListening(intent)
        Log.d("SpeechRecognition", "✓ Listening started")
    } catch (e: Exception) {
        Log.e("SpeechRecognition", "Failed to start: ${e.message}")
        onError("Failed to start speech recognition")
    }
}