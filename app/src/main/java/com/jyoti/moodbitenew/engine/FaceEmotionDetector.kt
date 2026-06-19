package com.jyoti.moodbitenew.engine

import android.graphics.Bitmap
import android.util.Log
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.face.FaceDetection
import com.google.mlkit.vision.face.FaceDetector
import com.google.mlkit.vision.face.FaceDetectorOptions
import com.jyoti.moodbitenew.model.MoodType
import com.google.mlkit.vision.face.Face
import kotlinx.coroutines.tasks.await
import kotlin.math.abs


/**
 * Face Emotion Detection using ML Kit
 * Analyzes facial landmarks to estimate emotional state
 */
object FaceEmotionDetector {

   /* private val options = FaceDetectorOptions.Builder()
        .setPerformanceMode(FaceDetectorOptions.PERFORMANCE_MODE_FAST)
        .setLandmarkMode(FaceDetectorOptions.LANDMARK_MODE_ALL)
        .setClassificationMode(FaceDetectorOptions.CLASSIFICATION_MODE_ALL)
        .build()*/
   private val options = FaceDetectorOptions.Builder()
       .setPerformanceMode(
           FaceDetectorOptions.PERFORMANCE_MODE_ACCURATE
       )
       .setClassificationMode(
           FaceDetectorOptions.CLASSIFICATION_MODE_ALL
       )
       .setLandmarkMode(
           FaceDetectorOptions.LANDMARK_MODE_ALL
       )
       .enableTracking()
       .build()

    private val detector: FaceDetector = FaceDetection.getClient(options)

    /**
     * Analyze bitmap and detect emotion
     * Returns the detected mood and confidence score
     */
    suspend fun detectMoodFromImage(bitmap: Bitmap): Pair<MoodType, Float> {

        return try {

            Log.d(
                "MoodDebug",
                "Bitmap Size = ${bitmap.width} x ${bitmap.height}"
            )

            val image = InputImage.fromBitmap(bitmap, 0)
/*
            val faces = detector.process(image).await()*/
            val faces = detector.process(image).await()

            Log.d(
                "MoodDebug",
                """
    ===== FACE DETECTION =====
    Bitmap Width = ${bitmap.width}
    Bitmap Height = ${bitmap.height}
    Faces Found = ${faces.size}
    ==========================
    """.trimIndent()
            )

            faces.forEachIndexed { index, face ->

                Log.d(
                    "MoodDebug",
                    """
        Face #$index

        BoundingBox = ${face.boundingBox}

        Smile = ${face.smilingProbability}
        LeftEye = ${face.leftEyeOpenProbability}
        RightEye = ${face.rightEyeOpenProbability}

        EulerX = ${face.headEulerAngleX}
        EulerY = ${face.headEulerAngleY}
        EulerZ = ${face.headEulerAngleZ}
        """.trimIndent()
                )
            }

            Log.d(
                "MoodDebug",
                "Faces Detected = ${faces.size}"
            )

            if (faces.isEmpty()) {

                Log.d(
                    "MoodDebug",
                    "No face detected. Returning CALM."
                )

                return Pair(MoodType.NO_FACE_DETECTED, 0.0f)
            }

            analyzeFace(faces[0])

        } catch (e: Exception) {

            Log.e(
                "MoodDebug",
                "Detection Error",
                e
            )

            Pair(MoodType.CALM, 0.0f)
        }
    }

    /**
     * Analyze facial features to estimate mood
     * Uses smile probability, head position, and eye openness
     */
    private fun analyzeFace(face: Face): Pair<MoodType, Float> {

        val smileProb = face.smilingProbability ?: 0f
        val leftEyeOpen = face.leftEyeOpenProbability ?: 0.5f
        val rightEyeOpen = face.rightEyeOpenProbability ?: 0.5f

        val headEulerX = face.headEulerAngleX
        val headEulerY = face.headEulerAngleY
        val headEulerZ = face.headEulerAngleZ

        // Debug Logs
        Log.d(
            "MoodDebug",
            """
        ========= FACE ANALYSIS =========
        Smile Probability : $smileProb
        Left Eye Open     : $leftEyeOpen
        Right Eye Open    : $rightEyeOpen
        Head Pitch (X)    : $headEulerX
        Head Yaw (Y)      : $headEulerY
        Head Roll (Z)     : $headEulerZ
        ================================
        """.trimIndent()
        )

        val result = when {

            // ENERGETIC
            smileProb > 0.65f &&
                    leftEyeOpen > 0.85f &&
                    rightEyeOpen > 0.85f -> {

                Pair(MoodType.ENERGETIC, 0.9f)
            }

            // HAPPY
            smileProb > 0.55f -> {

                Pair(MoodType.HAPPY, smileProb)
            }

            // TIRED
            leftEyeOpen < 0.45f ||
                    rightEyeOpen < 0.45f -> {

                Pair(
                    MoodType.TIRED,
                    1f - maxOf(leftEyeOpen, rightEyeOpen)
                )
            }

            // SAD
            smileProb < 0.20f -> {

                Pair(MoodType.SAD, 0.75f)
            }

            // ANXIOUS
            smileProb < 0.35f &&
                    abs(headEulerY) > 15f -> {

                Pair(MoodType.ANXIOUS, 0.7f)
            }

            // STRESSED
            smileProb in 0.20f..0.45f -> {

                Pair(MoodType.STRESSED, 0.65f)
            }

            // CALM
            else -> {

                Pair(MoodType.CALM, 0.6f)
            }
        }

        Log.d(
            "MoodDebug",
            """
        ======= MOOD RESULT =======
        Mood       : ${result.first}
        Confidence : ${result.second}
        ==========================
        """.trimIndent()
        )

        return result
    }


    /**
     * Get detailed face metrics for debugging/advanced features
     */
    data class FaceMetrics(
        val smileProbability: Float,
        val leftEyeOpenProbability: Float,
        val rightEyeOpenProbability: Float,
        val headEulerX: Float,
        val headEulerY: Float,
        val headEulerZ: Float,
        val boundingBox: String
    )

    suspend fun getFaceMetrics(bitmap: Bitmap): FaceMetrics? {
        return try {
            val image = InputImage.fromBitmap(bitmap, 0)
            val faces = detector.process(image).await()
            if (faces.isEmpty()) return null

            val face = faces[0]
            FaceMetrics(
                smileProbability = face.smilingProbability ?: 0.0f,
                leftEyeOpenProbability = face.leftEyeOpenProbability ?: 0.0f,
                rightEyeOpenProbability = face.rightEyeOpenProbability ?: 0.0f,
                headEulerX = face.headEulerAngleX,
                headEulerY = face.headEulerAngleY,
                headEulerZ = face.headEulerAngleZ,
                boundingBox = "x:${face.boundingBox.left},y:${face.boundingBox.top},w:${face.boundingBox.width()},h:${face.boundingBox.height()}"
            )
        } catch (e: Exception) {
            null
        }
    }
}