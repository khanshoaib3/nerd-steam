package com.github.khanshoaib3.steamcompanion.ui.screen.detail

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel

private const val TAG = "GameDetail"

@Composable
fun GameDetailScreen(
    modifier: Modifier = Modifier,
    appId: Int?,
) {
    val viewModel = hiltViewModel<GameDetailViewModel, GameDetailViewModel.GameDetailViewModelFactory> { factory ->
            factory.create(appId)
        }
    Log.d(TAG, viewModel.getText())
    Surface(color = MaterialTheme.colorScheme.surface) {
        Column(
            Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Text(
                text = "Details page for ${viewModel.getText()}",
                fontSize = 24.sp,
            )
            Spacer(Modifier.Companion.size(16.dp))
            Text(
                text = "TODO: Add great details here"
            )
        }
    }
}