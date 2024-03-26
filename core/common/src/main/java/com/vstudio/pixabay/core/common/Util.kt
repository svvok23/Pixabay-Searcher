package com.vstudio.pixabay.core.common

import android.content.Context
import android.content.Intent
import android.net.Uri

fun Context.shareIntent(text: String, shareTitle: String) {
    val intent = Intent(Intent.ACTION_SEND).apply {
        type = "text/plain"
        putExtra(Intent.EXTRA_TEXT, text)
    }
    val chooser = Intent.createChooser(intent, shareTitle)
    startActivity(chooser)
}

fun Context.startUrlIntent(destinationUrl: String) {
    val intent = Intent(Intent.ACTION_VIEW)
    intent.data = Uri.parse(destinationUrl)
    startActivity(intent)
}