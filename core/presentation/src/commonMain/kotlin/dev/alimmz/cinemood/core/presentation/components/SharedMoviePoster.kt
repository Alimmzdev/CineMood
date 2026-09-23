package dev.alimmz.cinemood.core.presentation.components

import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.EnterExitState
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.VisibilityThreshold
import androidx.compose.animation.core.animateDp
import androidx.compose.animation.core.spring
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

object SharedMoviePosterDefaults {
    val cardCornerRadius: Dp = 16.dp
    val verticalCardCornerRadius: Dp = 24.dp
    val detailCornerRadius: Dp = 0.dp
}

@Composable
fun rememberAnimatedPosterCornerRadius(
    animatedVisibilityScope: AnimatedVisibilityScope,
    listCornerRadius: Dp,
    isDetailDestination: Boolean,
): Dp {
    val transition = animatedVisibilityScope.transition

    val animatedRadius by transition.animateDp(
        transitionSpec = {
            spring(
                dampingRatio = 0.86f,
                stiffness = Spring.StiffnessMediumLow,
                visibilityThreshold = Dp.VisibilityThreshold,
            )
        },
        label = "posterCornerRadius",
    ) { state ->
        if (isDetailDestination) {
            when (state) {
                EnterExitState.Visible ->
                    SharedMoviePosterDefaults.detailCornerRadius

                EnterExitState.PreEnter,
                EnterExitState.PostExit ->
                    listCornerRadius
            }
        } else {
            when (state) {
                EnterExitState.Visible ->
                    listCornerRadius

                EnterExitState.PreEnter,
                EnterExitState.PostExit ->
                    SharedMoviePosterDefaults.detailCornerRadius
            }
        }
    }

    return animatedRadius.coerceAtLeast(0.dp)
}

@Composable
@OptIn(ExperimentalSharedTransitionApi::class)
fun SharedTransitionScope.sharedMoviePosterModifier(
    posterKey: String,
    animatedVisibilityScope: AnimatedVisibilityScope,
    cornerRadius: Dp,
): Modifier {

    require(cornerRadius.value >= 0f) {
        "Negative radius: $cornerRadius"
    }

    return Modifier
        .sharedElement(
            sharedContentState = rememberSharedContentState(key = posterKey),
            animatedVisibilityScope = animatedVisibilityScope,
            boundsTransform = SharedElementBoundsTransform,
        )
        .clip(RoundedCornerShape(cornerRadius))
}

fun moviePosterKey(movieId: Int): String {
    return "movie_poster_$movieId"
}
