package com.biprangshu.weathersnap.navigation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.navigation.NavBackStackEntry

private const val DURATION_HORIZONTAL = 350
private const val DURATION_MODAL_ENTER = 400
private const val DURATION_MODAL_EXIT = 300
private const val DURATION_SCALE = 280
private const val PARALLAX_FACTOR = 0.28f

internal typealias EnterFn = AnimatedContentTransitionScope<NavBackStackEntry>.() -> EnterTransition
internal typealias ExitFn = AnimatedContentTransitionScope<NavBackStackEntry>.() -> ExitTransition


//for saved report screen entry
internal val horizontalEnter: EnterFn = {
    slideIntoContainer(
        towards = AnimatedContentTransitionScope.SlideDirection.Start,
        animationSpec = tween(DURATION_HORIZONTAL, easing = LinearOutSlowInEasing),
    )
}

//for saved report screen exit
internal val horizontalExit: ExitFn = {
    slideOutOfContainer(
        towards = AnimatedContentTransitionScope.SlideDirection.Start,
        animationSpec = tween(DURATION_HORIZONTAL, easing = FastOutLinearInEasing),
        targetOffset = { (it * PARALLAX_FACTOR).toInt() },
    )
}


internal val horizontalPopEnter: EnterFn = {
    slideIntoContainer(
        towards = AnimatedContentTransitionScope.SlideDirection.End,
        animationSpec = tween(DURATION_HORIZONTAL, easing = LinearOutSlowInEasing),
        initialOffset = { (it * PARALLAX_FACTOR).toInt() },
    )
}

internal val horizontalPopExit: ExitFn = {
    slideOutOfContainer(
        towards = AnimatedContentTransitionScope.SlideDirection.End,
        animationSpec = tween(DURATION_HORIZONTAL, easing = FastOutLinearInEasing),
    )
}


//modal entry, for all screen create, camera etc
internal val modalEnter: EnterFn = {
    slideIntoContainer(
        towards = AnimatedContentTransitionScope.SlideDirection.Up,
        animationSpec = tween(DURATION_MODAL_ENTER, easing = LinearOutSlowInEasing),
    )
}

// all modal exit
internal val modalExit: ExitFn = {
    fadeOut(
        animationSpec = tween(DURATION_MODAL_ENTER, easing = FastOutLinearInEasing),
        targetAlpha = 0.82f,
    )
}

internal val modalPopEnter: EnterFn = {
    fadeIn(
        animationSpec = tween(DURATION_MODAL_EXIT, easing = LinearOutSlowInEasing),
        initialAlpha = 0.82f,
    )
}

internal val modalPopExit: ExitFn = {
    slideOutOfContainer(
        towards = AnimatedContentTransitionScope.SlideDirection.Down,
        animationSpec = tween(DURATION_MODAL_EXIT, easing = FastOutLinearInEasing),
    )
}


//transistion from confirmation to weather screen
internal val scalePopExit: ExitFn = {
    scaleOut(
        animationSpec = tween(DURATION_SCALE, easing = FastOutLinearInEasing),
        targetScale = 0.90f,
    ) + fadeOut(
        animationSpec = tween(DURATION_SCALE, easing = FastOutLinearInEasing),
    )
}

internal val scalePopEnter: EnterFn = {
    scaleIn(
        animationSpec = tween(DURATION_SCALE, easing = LinearOutSlowInEasing),
        initialScale = 0.95f,
    ) + fadeIn(
        animationSpec = tween(DURATION_SCALE, easing = LinearOutSlowInEasing),
    )
}
