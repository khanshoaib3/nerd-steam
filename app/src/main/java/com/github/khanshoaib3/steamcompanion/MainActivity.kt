package com.github.khanshoaib3.steamcompanion

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.github.khanshoaib3.steamcompanion.data.scraper.SteamChartsScraper
import com.github.khanshoaib3.steamcompanion.ui.SteamCompanionApp
import com.github.khanshoaib3.steamcompanion.ui.theme.SteamCompanionTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SteamCompanionTheme {
                SteamCompanionApp()
            }
        }
    }
}