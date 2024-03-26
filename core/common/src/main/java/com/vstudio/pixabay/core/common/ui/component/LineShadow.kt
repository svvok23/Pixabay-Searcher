package com.vstudio.pixabay.core.common.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Spacer
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import com.vstudio.pixabay.core.common.ui.theme.PixabayTheme

@Composable
fun LineShadow(modifier: Modifier = Modifier) {
    val brush = Brush.verticalGradient(
        listOf(
            (if (isSystemInDarkTheme()) Color.Black else Color.LightGray).copy(alpha = 0.15f),
            Color.Transparent
        )
    )
    Spacer(modifier = modifier.background(brush))
}

@Preview(showBackground = true)
@Composable
private fun LineShadowPreview() {
    PixabayTheme {
        LineShadow()
    }
}