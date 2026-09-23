package dev.alimmz.cinemood.core.presentation.components

import androidx.compose.animation.BoundsTransform
import androidx.compose.animation.EnterExitState
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.VisibilityThreshold
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.ui.geometry.Rect

object SharedTransitionAnimations {
    const val NAV_FADE_DURATION_MS: Int = 380
    const val BOUNDS_DURATION_MS: Int = 480
    const val CONTENT_ENTER_DURATION_MS: Int = 480
    const val CONTENT_STAGGER_DELAY_MS: Int = 80
    const val OVERLAY_ENTER_DELAY_MS: Int = 200
    const val DETAIL_SECTION_TOP_RADIUS_DP: Int = 28
    val EASING = FastOutSlowInEasing
}

@OptIn(ExperimentalSharedTransitionApi::class)
val SharedElementBoundsTransform: BoundsTransform = BoundsTransform { _, _ ->
    spring(
        dampingRatio = 0.86f,
        stiffness = Spring.StiffnessMediumLow,
        visibilityThreshold = Rect.VisibilityThreshold,
    )
}

fun sharedDetailContentEnterTransition(): EnterTransition {
    return fadeIn(
        animationSpec = tween(
            durationMillis = SharedTransitionAnimations.CONTENT_ENTER_DURATION_MS,
            delayMillis = SharedTransitionAnimations.OVERLAY_ENTER_DELAY_MS,
            easing = SharedTransitionAnimations.EASING,
        ),
    ) + slideInVertically(
        animationSpec = tween(
            durationMillis = SharedTransitionAnimations.CONTENT_ENTER_DURATION_MS,
            delayMillis = SharedTransitionAnimations.OVERLAY_ENTER_DELAY_MS,
            easing = SharedTransitionAnimations.EASING,
        ),
        initialOffsetY = { fullHeight -> fullHeight / 3 },
    )
}

fun sharedDetailSectionContentEnterTransition(delayMillis: Int = 0): EnterTransition {
    return fadeIn(
        animationSpec = tween(
            durationMillis = 360,
            delayMillis = delayMillis,
            easing = SharedTransitionAnimations.EASING,
        ),
    ) + slideInVertically(
        animationSpec = tween(
            durationMillis = 400,
            delayMillis = delayMillis,
            easing = SharedTransitionAnimations.EASING,
        ),
        initialOffsetY = { fullHeight -> fullHeight / 5 },
    )
}

fun sharedDetailContentExitTransition(): ExitTransition {
    return fadeOut(
        animationSpec = tween(
            durationMillis = SharedTransitionAnimations.NAV_FADE_DURATION_MS,
            easing = SharedTransitionAnimations.EASING,
        ),
    ) + slideOutVertically(
        animationSpec = tween(
            durationMillis = SharedTransitionAnimations.NAV_FADE_DURATION_MS,
            easing = SharedTransitionAnimations.EASING,
        ),
        targetOffsetY = { fullHeight -> fullHeight / 10 },
    )
}

fun sharedDetailPlayButtonEnterTransition(): EnterTransition {
    return fadeIn(
        animationSpec = tween(
            durationMillis = 280,
            delayMillis = 220,
            easing = SharedTransitionAnimations.EASING,
        ),
    ) + scaleIn(
        initialScale = 0.88f,
        animationSpec = spring(
            dampingRatio = 0.9f,
            stiffness = Spring.StiffnessMedium,
        ),
    )
}

fun sharedDetailPlayButtonExitTransition(): ExitTransition {
    return fadeOut(
        animationSpec = tween(
            durationMillis = 180,
            easing = SharedTransitionAnimations.EASING,
        ),
    ) + scaleOut(
        targetScale = 0.92f,
        animationSpec = tween(
            durationMillis = 180,
            easing = SharedTransitionAnimations.EASING,
        ),
    )
}

fun sharedDetailOverlayEnterTransition(): EnterTransition {
    return fadeIn(
        animationSpec = tween(
            durationMillis = 260,
            delayMillis = 120,
            easing = SharedTransitionAnimations.EASING,
        ),
    )
}

fun shouldRenderSharedOverlay(
    currentState: EnterExitState,
    targetState: EnterExitState,
): Boolean {
    return currentState != EnterExitState.PostExit && targetState != EnterExitState.PostExit
}
