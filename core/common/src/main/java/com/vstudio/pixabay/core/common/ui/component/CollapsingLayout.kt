package com.vstudio.pixabay.core.common.ui.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import com.vstudio.pixabay.core.common.ui.component.CollapsingLayoutStyle.Default
import com.vstudio.pixabay.core.common.ui.theme.PixabayTheme
import kotlin.math.roundToInt

enum class CollapsingLayoutStyle {
    Default,
    Sticky
}

@Composable
fun CollapsingLayout(
    style: CollapsingLayoutStyle = Default,
    collapsingTop: @Composable BoxScope.() -> Unit,
    bodyContent: @Composable BoxScope.() -> Unit,
    modifier: Modifier = Modifier,
) {
    var collapsingTopHeight by rememberSaveable { mutableFloatStateOf(0f) }
    var offset by rememberSaveable { mutableFloatStateOf(0f) }

    fun calculateOffset(delta: Float): Offset {
        val oldOffset = offset
        val newOffset = (oldOffset + delta).coerceIn(-collapsingTopHeight, 0f)
        offset = newOffset

        return Offset(0f, newOffset - oldOffset)
    }

    val nestedScrollConnection = remember {
        object : NestedScrollConnection {
            override fun onPreScroll(
                available: Offset,
                source: NestedScrollSource,
            ): Offset {
                return if (style == Default) {
                    when {
                        available.y >= 0 -> Offset.Zero
                        offset == -collapsingTopHeight -> Offset.Zero
                        else -> calculateOffset(available.y)
                    }
                } else {
                    when {
                        available.y >= 0 && offset == 0f -> Offset.Zero
                        else -> calculateOffset(available.y)
                    }
                }
            }

            override fun onPostScroll(
                consumed: Offset,
                available: Offset,
                source: NestedScrollSource,
            ): Offset {
                return when {
                    available.y <= 0 -> Offset.Zero
                    offset == 0f -> Offset.Zero
                    else -> calculateOffset(available.y)
                }
            }
        }
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .nestedScroll(nestedScrollConnection),
    ) {
        Box(
            modifier = Modifier
                .onSizeChanged { size ->
                    collapsingTopHeight = size.height.toFloat()
                }
                .offset {
                    IntOffset(x = 0, y = offset.roundToInt())
                },
            content = collapsingTop,
        )
        Box(
            modifier = Modifier
                .offset {
                    IntOffset(x = 0, y = (collapsingTopHeight + offset).roundToInt())
                },
            content = bodyContent,
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun CollapsingLayoutPreview() {
    PixabayTheme {
        CollapsingLayout(
            collapsingTop = {},
            bodyContent = {}
        )
    }
}