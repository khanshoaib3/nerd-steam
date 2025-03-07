package com.github.khanshoaib3.steamcompanion.ui

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.github.khanshoaib3.steamcompanion.ui.screen.home.Home
import com.github.khanshoaib3.steamcompanion.ui.theme.SteamCompanionTheme

@Composable
fun SteamCompanionApp(modifier: Modifier = Modifier) {
    Scaffold { innerPadding ->
        Home(modifier = modifier.padding(innerPadding))
    }
}

@Preview
@Composable
private fun SteamCompanionAppPreview() {
    SteamCompanionTheme {
        SteamCompanionApp()
    }
}