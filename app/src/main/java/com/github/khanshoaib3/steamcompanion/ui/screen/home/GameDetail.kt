package com.github.khanshoaib3.steamcompanion.ui.screen.home

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

@Composable
fun GameDetail(appId: Int?) {
    val text = "Selected game with id: ${appId ?: 0}"
    Log.d("GameDetail", text)
    Surface(color = MaterialTheme.colorScheme.surface) {
        Column(
            Modifier.Companion
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Text(
                text = "Details page for $text",
                fontSize = 24.sp,
            )
            Spacer(Modifier.Companion.size(16.dp))
            Text(
                text = "TODO: Add great details here"
            )
        }
    }
}