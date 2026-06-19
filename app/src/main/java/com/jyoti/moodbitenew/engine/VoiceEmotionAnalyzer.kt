package com.jyoti.moodbitenew.engine

import android.content.Context
import android.media.MediaRecorder
import android.os.Build
import android.speech.RecognitionListener
import android.speech.SpeechRecognizer
import android.util.Log
import com.jyoti.moodbitenew.model.MoodType
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlin.math.abs

/**
 * Analyzes voice patterns for mood detection
 * Measures: tone, pitch, speech rate, stress levels, sentiment
 */
class VoiceEmotionAnalyzer(private val context: Context) {

    private var mediaRecorder: MediaRecorder? = null
    private var speechRecognizer: SpeechRecognizer? = null
    private var recordingStartTime: Long = 0
    private var isRecording = false

    private val _voiceMetrics = MutableStateFlow(VoiceMetrics())
    val voiceMetrics: StateFlow<VoiceMetrics> = _voiceMetrics

    private val _transcription = MutableStateFlow("")
    val transcription: StateFlow<String> = _transcription

    data class VoiceMetrics(
        val speechRate: Float = 0f,           // words per minute
        val averagePitch: Float = 0f,         // frequency in Hz
        val voiceStress: Float = 0f,          // 0.0-1.0 stress indicator
        val pauseDuration: Long = 0L,         // pause time in ms
        val loudnessLevel: Float = 0f,        // 0.0-1.0
        val emoProbabilities: EmotionalProbabilities = EmotionalProbabilities()
    )

    data class EmotionalProbabilities(
        val happy: Float = 0f,
        val sad: Float = 0f,
        val angry: Float = 0f,
        val anxious: Float = 0f,
        val calm: Float = 0f,
        val stressed: Float = 0f,
        val tired: Float = 0f
    )

    /**
     * Start recording voice
     */
    fun startRecording(outputPath: String) {
        try {
            mediaRecorder = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                MediaRecorder(context)
            } else {
                @Suppress("DEPRECATION")
                MediaRecorder()
            }

            mediaRecorder?.apply {
                setAudioSource(MediaRecorder.AudioSource.MIC)
                setOutputFormat(MediaRecorder.OutputFormat.DEFAULT)
                setAudioEncoder(MediaRecorder.AudioEncoder.DEFAULT)
                setOutputFile(outputPath)
                prepare()
                start()
            }

            recordingStartTime = System.currentTimeMillis()
            isRecording = true
            Log.d("VoiceAnalyzer", "Recording started")

        } catch (e: Exception) {
            Log.e("VoiceAnalyzer", "Recording error: ${e.message}")
        }
    }

    /**
     * Stop recording and analyze
     */
    fun stopRecording(): VoiceMetrics {
        return try {
            mediaRecorder?.apply {
                stop()
                release()
            }
            mediaRecorder = null
            isRecording = false

            val recordingDuration = System.currentTimeMillis() - recordingStartTime
            Log.d("VoiceAnalyzer", "Recording stopped. Duration: ${recordingDuration}ms")

            // Analyze metrics
            analyzeVoiceMetrics(recordingDuration)

        } catch (e: Exception) {
            Log.e("VoiceAnalyzer", "Stop recording error: ${e.message}")
            VoiceMetrics()
        }
    }

    /**
     * Analyze voice metrics from recording
     */
    private fun analyzeVoiceMetrics(durationMs: Long): VoiceMetrics {
        // This is a simplified analysis. In production, use Agora's audio analysis APIs
        val metrics = calculateVoiceCharacteristics(durationMs)
        _voiceMetrics.value = metrics
        return metrics
    }

    /**
     * Calculate voice characteristics
     */
    private fun calculateVoiceCharacteristics(durationMs: Long): VoiceMetrics {
        // Simulated metrics (in production, these come from audio analysis)
        // Duration < 3s: might indicate anxiety/rushed
        // Duration > 20s: calm/relaxed
        // Fast speech (>150 WPM): energetic/stressed
        // Slow speech (<100 WPM): tired/sad

        val estimatedWordCount = (durationMs / 500).toFloat()
        val speechRate = (estimatedWordCount / (durationMs / 60000f))

        // Estimate stress from speech rate
        val voiceStress = when {
            speechRate > 180f -> 0.8f  // Very fast = stressed/energetic
            speechRate > 140f -> 0.6f  // Fast = energetic
            speechRate > 100f -> 0.3f  // Normal = calm
            else -> 0.5f               // Slow = tired/sad
        }

        // Create emotional probabilities based on analysis
        val emoProbabilities = EmotionalProbabilities(
            happy = if (speechRate > 140f && voiceStress < 0.7f) 0.7f else 0.2f,
            sad = if (speechRate < 100f) 0.6f else 0.2f,
            anxious = if (voiceStress > 0.7f) 0.75f else 0.2f,
            calm = if (speechRate in 100f..140f && voiceStress < 0.4f) 0.8f else 0.2f,
            stressed = voiceStress,
            tired = if (speechRate < 100f && voiceStress < 0.5f) 0.7f else 0.2f,
            angry = if (voiceStress > 0.9f) 0.6f else 0.1f
        )

        return VoiceMetrics(
            speechRate = speechRate,
            averagePitch = 150f + (voiceStress * 50f),  // Stress raises pitch
            voiceStress = voiceStress,
            pauseDuration = (durationMs * 0.1f).toLong(),
            loudnessLevel = 0.6f + (voiceStress * 0.3f),
            emoProbabilities = emoProbabilities
        )
    }

    /**
     * Set transcription from speech-to-text
     */
    fun setTranscription(text: String) {
        _transcription.value = text
        Log.d("VoiceAnalyzer", "Transcription: $text")
    }

    /**
     * Analyze sentiment from transcribed text
     */
    fun analyzeSentiment(text: String): SentimentScore {
        // Simple keyword-based sentiment analysis
        // In production: use Google ML Kit NLP or external API

        val positiveKeywords = listOf(
            "happy", "excited", "great", "wonderful", "amazing", "love",
            "good", "excellent", "perfect", "fantastic", "blessed", "grateful"
        )

        val negativeKeywords = listOf(
            "sad", "depressed", "angry", "frustrated", "tired", "exhausted",
            "bad", "terrible", "awful", "horrible", "hate", "miserable"
        )

        val anxiousKeywords = listOf(
            "anxious", "worried", "nervous", "scared", "afraid", "panic",
            "stressed", "overwhelmed", "tense", "uneasy"
        )

        val lowerText = text.toLowerCase()
        val words = lowerText.split(" ")

        val positiveCount = words.count { it in positiveKeywords }
        val negativeCount = words.count { it in negativeKeywords }
        val anxiousCount = words.count { it in anxiousKeywords }

        val totalKeywords = positiveCount + negativeCount + anxiousCount
        val sentiment = if (totalKeywords == 0) {
            SentimentScore(neutral = 1.0f)
        } else {
            SentimentScore(
                positive = positiveCount.toFloat() / totalKeywords,
                negative = negativeCount.toFloat() / totalKeywords,
                anxious = anxiousCount.toFloat() / totalKeywords,
                neutral = 1f - (positiveCount + negativeCount + anxiousCount).toFloat() / totalKeywords
            )
        }

        return sentiment
    }

    data class SentimentScore(
        val positive: Float = 0f,
        val negative: Float = 0f,
        val anxious: Float = 0f,
        val neutral: Float = 0f
    )

    /**
     * Detect mood from combined voice + text analysis
     */
    fun detectMoodFromVoice(
        voiceMetrics: VoiceMetrics,
        sentimentScore: SentimentScore
    ): Pair<MoodType, Float> {

        // Weight the different factors
        val voiceWeight = 0.5f
        val sentimentWeight = 0.5f

        // Calculate weighted mood probabilities
        val moodScores = mapOf(
            MoodType.HAPPY to (voiceMetrics.emoProbabilities.happy * voiceWeight +
                    sentimentScore.positive * sentimentWeight),
            MoodType.SAD to (voiceMetrics.emoProbabilities.sad * voiceWeight +
                    sentimentScore.negative * sentimentWeight),
            MoodType.ANXIOUS to (voiceMetrics.emoProbabilities.anxious * voiceWeight +
                    sentimentScore.anxious * sentimentWeight),
            MoodType.STRESSED to (voiceMetrics.voiceStress * voiceWeight +
                    sentimentScore.anxious * 0.3f * sentimentWeight),
            MoodType.TIRED to (voiceMetrics.emoProbabilities.tired * voiceWeight +
                    sentimentScore.neutral * 0.3f * sentimentWeight),
            MoodType.ENERGETIC to (voiceMetrics.emoProbabilities.happy * 0.7f * voiceWeight +
                    sentimentScore.positive * sentimentWeight),
            MoodType.CALM to (voiceMetrics.emoProbabilities.calm * voiceWeight +
                    sentimentScore.neutral * sentimentWeight)
        )

        // Find mood with highest score
        val result = moodScores.maxByOrNull { it.value }

        val detectedMood = result?.key ?: MoodType.CALM
        val confidence = result?.value ?: 0.5f

        Log.d("VoiceAnalyzer", "Detected mood: $detectedMood (confidence: $confidence)")

        return Pair(detectedMood, confidence)
    }

    fun release() {
        try {
            mediaRecorder?.release()
            mediaRecorder = null
            speechRecognizer?.destroy()
            speechRecognizer = null
        } catch (e: Exception) {
            Log.e("VoiceAnalyzer", "Release error: ${e.message}")
        }
    }
}