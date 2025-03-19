package com.github.khanshoaib3.steamcompanion.ui.screen.detail

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun GameDetailScreen(
    modifier: Modifier = Modifier,
    appId: Int?,
) {
    val viewModel = hiltViewModel<GameDetailViewModel>()
    val gameData by viewModel.gameData.collectAsState()
    val scope = rememberCoroutineScope()

    LaunchedEffect(scope) {
        viewModel.updateAppId(appId)
    }

    Surface(color = MaterialTheme.colorScheme.surface, modifier = modifier) {
        Column(
            Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Text(
                text = "Details page for ${gameData.content?.data?.name ?: "Empty"}",
                fontSize = 24.sp,
            )
            Spacer(Modifier.size(16.dp))
            Text(
                text = "TODO: Add great details here"
            )
        }
    }
}