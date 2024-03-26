package com.vstudio.pixabay.core.common.ui

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut

enum class ScaleTransitionDirection {
    INWARDS, OUTWARDS
}

private const val duration = 250
private const val delay = 0

fun scaleIntoContainer(
    direction: ScaleTransitionDirection = ScaleTransitionDirection.INWARDS,
    initialScale: Float = if (direction == ScaleTransitionDirection.OUTWARDS) 0.5f else 0.9f
): EnterTransition {
    return scaleIn(
        animationSpec = tween(durationMillis = duration, delayMillis = delay),
        initialScale = initialScale
    ) + fadeIn(animationSpec = tween(duration, delayMillis = delay))
}

fun scaleOutOfContainer(
    direction: ScaleTransitionDirection = ScaleTransitionDirection.OUTWARDS,
    targetScale: Float = if (direction == ScaleTransitionDirection.INWARDS) 0.5f else 1.1f
): ExitTransition {
    return scaleOut(
        animationSpec = tween(durationMillis = duration, delayMillis = delay),
        targetScale = targetScale
    ) + fadeOut(tween(delayMillis = delay))
}