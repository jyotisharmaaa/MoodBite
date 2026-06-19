package com.jyoti.moodbitenew

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import com.jyoti.moodbitenew.ui.app.MoodBiteApp
import com.jyoti.moodbitenew.ui.theme.MoodBiteNewTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MoodBiteNewTheme {
                MoodBiteApp()
            }
        }
    }
}