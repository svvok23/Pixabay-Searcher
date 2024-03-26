package com.vstudio.pixabay.feature.search.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.vstudio.pixabay.core.common.ui.component.shimmerEffect
import com.vstudio.pixabay.core.common.ui.theme.PixabayTheme

@Composable
fun EmptyCard(modifier: Modifier = Modifier) {
    ElevatedCard(
        modifier = modifier,
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 4.dp),
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .shimmerEffect()
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun EmptyCardPreview() {
    PixabayTheme {
        EmptyCard()
    }
}