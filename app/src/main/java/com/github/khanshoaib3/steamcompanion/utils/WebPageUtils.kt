package com.github.khanshoaib3.steamcompanion.utils

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.core.net.toUri

// https://developer.android.com/guide/components/intents-common#ViewUrl
fun OpenWebPage(url: String, context: Context) {
    val webpage: Uri = url.toUri()
    val intent = Intent(Intent.ACTION_VIEW, webpage)
    context.startActivity(intent)
}