package com.jyoti.moodbitenew.ui.screens

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import com.jyoti.moodbitenew.engine.FaceEmotionDetector
import com.jyoti.moodbitenew.model.MoodType
import com.jyoti.moodbitenew.ui.theme.AccentGreen
import com.jyoti.moodbitenew.ui.theme.BgDark
import com.jyoti.moodbitenew.ui.theme.TextPrimary
import com.jyoti.moodbitenew.ui.theme.TextSecondary
import com.jyoti.moodbitenew.utils.ImageUtils
import com.jyoti.moodbitenew.viewmodel.MoodViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.withContext

@Composable
fun CameraMoodDetectionScreen(
    navController: NavController,
    viewModel: MoodViewModel
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    var isCameraPermissionGranted by remember { mutableStateOf(false) }
    var detectedMood by remember { mutableStateOf<MoodType?>(null) }
    var detectionConfidence by remember { mutableStateOf(0.0f) }
    var isAnalyzing by remember { mutableStateOf(false) }
    var showAnalysisResult by remember { mutableStateOf(false) }

    val cameraProviderFuture = remember { ProcessCameraProvider.getInstance(context) }
    val previewView = remember { PreviewView(context) }
    val imageCapture = remember { ImageCapture.Builder().build() }
    var cameraProvider by remember { mutableStateOf<ProcessCameraProvider?>(null) }

    val scope = rememberCoroutineScope()

    // Request camera permission
    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        isCameraPermissionGranted = isGranted
    }

    LaunchedEffect(Unit) {
        val permission = Manifest.permission.CAMERA
        isCameraPermissionGranted = ContextCompat.checkSelfPermission(
            context, permission
        ) == PackageManager.PERMISSION_GRANTED

        if (!isCameraPermissionGranted) {
            permissionLauncher.launch(permission)
        }
    }

    LaunchedEffect(isCameraPermissionGranted) {
        if (isCameraPermissionGranted) {
            cameraProviderFuture.addListener({
                val provider = cameraProviderFuture.get()
                cameraProvider = provider

                val preview = Preview.Builder().build().also {
                    it.setSurfaceProvider(previewView.surfaceProvider)
                }

                val cameraSelector = CameraSelector.DEFAULT_FRONT_CAMERA

                try {
                    provider.unbindAll()
                    provider.bindToLifecycle(
                        lifecycleOwner, cameraSelector, preview, imageCapture
                    )
                } catch (exc: Exception) {
                    exc.printStackTrace()
                }
            }, ContextCompat.getMainExecutor(context))
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(BgDark)
    ) {
        if (isCameraPermissionGranted) {
            // Camera Preview
            AndroidView(
                modifier = Modifier.fillMaxSize(),
                factory = { previewView }
            )

            // Header with close button
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    "📷 Detect Your Mood",
                    style = MaterialTheme.typography.headlineSmall,
                    color = TextPrimary
                )
                IconButton(onClick = { navController.popBackStack() }) {
                    Icon(Icons.Default.Close, contentDescription = "Close", tint = TextPrimary)
                }
            }

            // Instructions
            Column(
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .padding(top = 80.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth(0.9f)
                        .padding(16.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = Color.Black.copy(alpha = 0.7f)
                    ),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            "Position your face in the center",
                            color = TextPrimary,
                            fontSize = 14.sp,
                            textAlign = TextAlign.Center
                        )
                        Text(
                            "Look at the camera naturally",
                            color = TextSecondary,
                            fontSize = 12.sp,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }

            // Capture button at bottom
            Column(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth()
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Button(
                    onClick = {
                        isAnalyzing = true
                        captureAndAnalyze(
                            imageCapture,
                            context,
                            scope
                        ) { mood, confidence ->
                            detectedMood = mood
                            detectionConfidence = confidence
                            showAnalysisResult = true
                            isAnalyzing = false
                        }
                    },
                    enabled = !isAnalyzing,
                    modifier = Modifier
                        .size(72.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = AccentGreen),
                    shape = RoundedCornerShape(36.dp)
                ) {
                    Text(
                        if (isAnalyzing) "⏳" else "📸",
                        fontSize = 32.sp
                    )
                }

                if (isAnalyzing) {
                    Spacer(modifier = Modifier.height(16.dp))
                    CircularProgressIndicator(color = AccentGreen)
                    Text(
                        "Analyzing your mood...",
                        color = TextSecondary,
                        fontSize = 12.sp,
                        modifier = Modifier.padding(top = 8.dp)
                    )
                }
            }
        } else {
            // Permission denied
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(32.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    "Camera Permission Required",
                    style = MaterialTheme.typography.headlineMedium,
                    color = TextPrimary,
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    "MoodBite needs camera access to detect your mood from facial expressions.",
                    color = TextSecondary,
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(24.dp))
                Button(
                    onClick = { permissionLauncher.launch(Manifest.permission.CAMERA) },
                    colors = ButtonDefaults.buttonColors(containerColor = AccentGreen)
                ) {
                    Text("Grant Permission", color = BgDark)
                }
            }
        }
    }

    // Show detection result dialog
    if (showAnalysisResult && detectedMood != null) {
        AlertDialog(
            onDismissRequest = { showAnalysisResult = false },
            title = { Text("Mood Detected! 🎉") },
            text = {
                Column {
                    Text(
                        "Detected Mood: ${detectedMood!!.name}",
                        style = MaterialTheme.typography.bodyLarge
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        "Confidence: ${(detectionConfidence * 100).toInt()}%",
                        style = MaterialTheme.typography.bodyMedium,
                        color = TextSecondary
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        "Would you like to get food recommendations for this mood?",
                        style = MaterialTheme.typography.bodySmall,
                        color = TextSecondary
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        viewModel.selectMood(detectedMood!!)
                        viewModel.analyzeAndNavigate()
                        showAnalysisResult = false
                        navController.navigate("result") {
                            popUpTo("camera") { inclusive = true }
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = AccentGreen)
                ) {
                    Text("Get Food Plan", color = BgDark)
                }
            },
            dismissButton = {
                Button(
                    onClick = { showAnalysisResult = false },
                    colors = ButtonDefaults.outlinedButtonColors()
                ) {
                    Text("Retake Photo")
                }
            }
        )
    }
}

private fun captureAndAnalyze(
    imageCapture: ImageCapture,
    context: Context,
    scope: CoroutineScope,
    onResult: (MoodType, Float) -> Unit
){
    val outputDirectory = File(context.cacheDir, "camera_captures").apply {
        mkdirs()
    }
    val photoFile = File(outputDirectory, "mood_${System.currentTimeMillis()}.jpg")

    val outputFileOptions = ImageCapture.OutputFileOptions.Builder(photoFile).build()

    imageCapture.takePicture(
        outputFileOptions,
        ContextCompat.getMainExecutor(context),
        object : ImageCapture.OnImageSavedCallback {
            override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
                scope.launch(Dispatchers.Default) {
                    try {
                       /* val bitmap = BitmapFactory.decodeFile(photoFile.absolutePath)
                        if (bitmap != null) {
                            val (mood, confidence) = FaceEmotionDetector.detectMoodFromImage(bitmap)
                            onResult(mood, confidence)
                        }
                        Log.d(
                            "MoodDebug",
                            "Saved image at: ${photoFile.absolutePath}"
                        )
                        //photoFile.delete() // Clean up*/
                        val bitmap =
                            ImageUtils.getCorrectlyRotatedBitmap(
                                photoFile.absolutePath
                            )

                        Log.d(
                            "MoodDebug",
                            """
    ========= CAPTURED IMAGE =========
    Path = ${photoFile.absolutePath}
    Exists = ${photoFile.exists()}
    Size = ${photoFile.length()}
    Width = ${bitmap?.width}
    Height = ${bitmap?.height}
    ==================================
    """.trimIndent()
                        )

                        if (bitmap != null) {

                            val (mood, confidence) =
                                FaceEmotionDetector.detectMoodFromImage(bitmap)

                            withContext(Dispatchers.Main) {
                                onResult(mood, confidence)
                            }
                        }

// Keep image for debugging
                        Log.d(
                            "MoodDebug",
                            "Image saved at: ${photoFile.absolutePath}"
                        )

// Uncomment later
// photoFile.delete()
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
            }

            override fun onError(exception: ImageCaptureException) {
                exception.printStackTrace()
            }
        }
    )
}