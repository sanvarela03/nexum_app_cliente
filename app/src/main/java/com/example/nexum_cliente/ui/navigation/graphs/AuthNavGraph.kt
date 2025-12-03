package com.example.nexum_cliente.ui.navigation.graphs

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.example.nexum_cliente.ui.navigation.rutes.AuthRoutes
import com.example.nexum_cliente.ui.presenter.sign_in.SignInScreen
import com.example.nexum_cliente.ui.presenter.sign_up.screens.SignUpBirthDateScreen
import com.example.nexum_cliente.ui.presenter.sign_up.screens.SignUpCellphoneScreen
import com.example.nexum_cliente.ui.presenter.sign_up.screens.SignUpCityScreen
import com.example.nexum_cliente.ui.presenter.sign_up.screens.SignUpIDScreen
import com.example.nexum_cliente.ui.presenter.sign_up.screens.SignUpPasswordScreen
import com.example.nexum_cliente.ui.presenter.sign_up.screens.SignUpScreen
import com.example.nexum_cliente.ui.presenter.sign_up.screens.SignUpUploadDocumentScreen
import com.example.nexum_cliente.ui.presenter.sign_up.screens.SignUpUserDataScreen
import com.example.nexum_cliente.ui.presenter.sign_up.screens.SignUpProfilePictureScreen

/**
 * @author Santiago Varela Daza
 * @email svarela03@uan.edu.co
 * @github https://github.com/sanvarela03
 * @since 7/23/2025
 * @version 1.0
 */
import androidx.compose.animation.*
import androidx.compose.runtime.Composable
import androidx.navigation.NavBackStackEntry
import com.example.nexum_cliente.ui.presenter.sign_up.SignUpViewModel

// A small alias so types below are shorter

private const val ANIMATION_DURATION = 1000
private const val SLIDE_OFFSET = 1000

// Type alias for cleaner code
private typealias AnimationScope = AnimatedContentTransitionScope<NavBackStackEntry>

data class ScreenTransitions(
    val enterTransition: AnimationScope.() -> EnterTransition? = { Transitions.defaultEnterTransition() },
    val exitTransition: AnimationScope.() -> ExitTransition? = { Transitions.defaultExitTransition() },
    val popEnterTransition: AnimationScope.() -> EnterTransition? = { Transitions.defaultPopEnterTransition() },
    val popExitTransition: AnimationScope.() -> ExitTransition? = { Transitions.defaultPopExitTransition() }
)

/**
 * Default transition animations for consistency
 */
object Transitions {

    fun defaultEnterTransition(): EnterTransition =
        slideInHorizontally(
            animationSpec = tween(ANIMATION_DURATION),
            initialOffsetX = { SLIDE_OFFSET }
        )

    fun defaultExitTransition(): ExitTransition =
        slideOutHorizontally(
            animationSpec = tween(ANIMATION_DURATION),
            targetOffsetX = { -SLIDE_OFFSET }
        )

    fun defaultPopEnterTransition(): EnterTransition =
        slideInHorizontally(
            animationSpec = tween(ANIMATION_DURATION),
            initialOffsetX = { SLIDE_OFFSET },
            
        )

    fun defaultPopExitTransition(): ExitTransition =
        slideOutHorizontally(
            animationSpec = tween(ANIMATION_DURATION),
            targetOffsetX = { -SLIDE_OFFSET }
        )
}


inline fun <reified T : Any> NavGraphBuilder.composableWithTransitions(
    transitions: ScreenTransitions = ScreenTransitions(),
    noinline content: @Composable (NavBackStackEntry) -> Unit
) {
    composable<T>(
        enterTransition = transitions.enterTransition,
        exitTransition = transitions.exitTransition,
        popEnterTransition = transitions.popEnterTransition,
        popExitTransition = transitions.popExitTransition,
    ) { backStackEntry ->
        content(backStackEntry)
    }
}


fun NavGraphBuilder.authGraph(
    navController: NavController,
    viewModel: SignUpViewModel
) {
    navigation<Graph.InitialGraph>(startDestination = AuthRoutes.SignUpScreen) {
        composable<AuthRoutes.SignInScreen> { SignInScreen(go = { navController.navigate(it) }) }
        composableWithTransitions<AuthRoutes.SignUpScreen>
//        (
//            enterTransition = {
//                slideIntoContainer(
//                    AnimatedContentTransitionScope.SlideDirection.Right,
//                    tween(1000)
//                )
//            },
//            exitTransition = {
//                slideOutOfContainer(
//                    AnimatedContentTransitionScope.SlideDirection.Left,
//                    tween(1000)
//                )
//            },
//            popEnterTransition = {
//                slideIntoContainer(
//                    AnimatedContentTransitionScope.SlideDirection.Right,
//                    tween(1000)
//                )
//            },
//            popExitTransition = {
//                slideOutOfContainer(
//                    AnimatedContentTransitionScope.SlideDirection.Left,
//                    tween(1000)
//                )
//            }
//        )
        { SignUpScreen(go = { navController.navigate(it) }, viewModel = viewModel) }
        composable<AuthRoutes.SignUpUserDataScreen>(
            enterTransition = {
                slideIntoContainer(
                    AnimatedContentTransitionScope.SlideDirection.Left,
                    tween(1000)
                )
            },
            exitTransition = {
                slideOutOfContainer(
                    AnimatedContentTransitionScope.SlideDirection.Right,
                    tween(1000)
                )
            },
            popEnterTransition = {
                slideIntoContainer(
                    AnimatedContentTransitionScope.SlideDirection.Right,
                    tween(1000)
                )
            },
            popExitTransition = {
                slideOutOfContainer(
                    AnimatedContentTransitionScope.SlideDirection.Right,
                    tween(1000)
                )
            }
        ) { SignUpUserDataScreen(go = { navController.navigate(it) }, viewModel = viewModel) }
        composable<AuthRoutes.SignUpCellphoneScreen>(
            enterTransition = {
                slideIntoContainer(
                    AnimatedContentTransitionScope.SlideDirection.Left,
                    tween(1000)
                )
            },
            popExitTransition = {
                slideOutOfContainer(
                    AnimatedContentTransitionScope.SlideDirection.Right,
                    tween(1000)
                )
            }
        ) {
            SignUpCellphoneScreen(go = {
                navController.navigate(
                    it
                )
            }, viewModel = viewModel)
        }
        composable<AuthRoutes.SignUpBirthdayScreen> {
            SignUpBirthDateScreen(go = {
                navController.navigate(
                    it
                )
            }, viewModel = viewModel)
        }
        composable<AuthRoutes.SignUpCityScreen> { SignUpCityScreen(go = { navController.navigate(it) }, viewModel = viewModel) }
        composable<AuthRoutes.SignUpIDScreen> { SignUpIDScreen(go = { navController.navigate(it) }, viewModel = viewModel) }
        composable<AuthRoutes.SignUpUploadDocumentScreen> {
            SignUpUploadDocumentScreen(go = {
                navController.navigate(
                    it
                )
            }, viewModel = viewModel)
        }
        composable<AuthRoutes.SignUpPasswordScreen> {
            SignUpPasswordScreen(go = {
                navController.navigate(
                    it
                )
            }, viewModel = viewModel)
        }
        composable<AuthRoutes.SignUpProfilePictureScreen> {
            SignUpProfilePictureScreen(go = {
                navController.navigate(
                    it
                )
            }, viewModel = viewModel)
        }
    }
}
