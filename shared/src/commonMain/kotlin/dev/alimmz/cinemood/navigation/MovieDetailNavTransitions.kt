package dev.alimmz.cinemood.navigation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.ContentTransform
import androidx.compose.animation.SizeTransform
import androidx.compose.animation.core.FiniteAnimationSpec
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.VisibilityThreshold
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.ui.unit.IntSize
import androidx.navigation3.scene.Scene
import androidx.navigation3.ui.NavDisplay
import dev.alimmz.cinemood.core.presentation.components.SharedTransitionAnimations

fun movieDetailNavMetadata(): Map<String, Any> {
    return NavDisplay.transitionSpec { movieDetailForwardTransition() } +
        NavDisplay.popTransitionSpec { movieDetailBackwardTransition() } +
        NavDisplay.predictivePopTransitionSpec { movieDetailBackwardTransition() }
}

fun sharedNavSizeTransform(): SizeTransform {
    return SizeTransform(
        clip = false,
        sizeAnimationSpec = { _, _ ->
            spring(
                dampingRatio = 0.86f,
                stiffness = Spring.StiffnessMediumLow,
                visibilityThreshold = IntSize.VisibilityThreshold,
            )
        },
    )
}

private fun AnimatedContentTransitionScope<Scene<*>>.movieDetailForwardTransition(): ContentTransform {
    return sharedFadeContentTransform()
}

private fun AnimatedContentTransitionScope<Scene<*>>.movieDetailBackwardTransition(): ContentTransform {
    return sharedFadeContentTransform()
}

private fun AnimatedContentTransitionScope<Scene<*>>.sharedFadeContentTransform(): ContentTransform {
    val animationSpec: FiniteAnimationSpec<Float> = tween(
        durationMillis = SharedTransitionAnimations.NAV_FADE_DURATION_MS,
        easing = SharedTransitionAnimations.EASING,
    )
    return fadeIn(animationSpec) togetherWith fadeOut(animationSpec)
}
