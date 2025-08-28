package com.example.neuxum_cliente.ui.navigation.graphs

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.example.neuxum_cliente.ui.navigation.rutes.AuthRoutes
import com.example.neuxum_cliente.ui.presenter.sign_in.SignInScreen
import com.example.neuxum_cliente.ui.presenter.sign_up.screens.SignUpBirthDateScreen
import com.example.neuxum_cliente.ui.presenter.sign_up.screens.SignUpCellphoneScreen
import com.example.neuxum_cliente.ui.presenter.sign_up.screens.SignUpCityScreen
import com.example.neuxum_cliente.ui.presenter.sign_up.screens.SignUpIDScreen
import com.example.neuxum_cliente.ui.presenter.sign_up.screens.SignUpScreen
import com.example.neuxum_cliente.ui.presenter.sign_up.screens.SignUpUserDataScreen

/**
 * @author Santiago Varela Daza
 * @email svarela03@uan.edu.co
 * @github https://github.com/sanvarela03
 * @since 7/23/2025
 * @version 1.0
 */
fun NavGraphBuilder.authGraph(
    navController: NavController
) {


    navigation<Graph.InitialGraph>(startDestination = AuthRoutes.SignInScreen) {
        composable<AuthRoutes.SignInScreen>(

        ) { SignInScreen(go = { navController.navigate(it) }) }
        composable<AuthRoutes.SignUpScreen>(
            exitTransition = {
                slideOutOfContainer(
                    AnimatedContentTransitionScope.SlideDirection.Left,
                    tween(1000)
                )
            },
            popEnterTransition = {
                slideIntoContainer(
                    AnimatedContentTransitionScope.SlideDirection.Right,
                    tween(1000)
                )
            }
        ) { SignUpScreen(go = { navController.navigate(it) }) }
        composable<AuthRoutes.SignUpUserDataScreen>(
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
            SignUpUserDataScreen(go = {
                navController.navigate(
                    it
                )
            })
        }
        composable<AuthRoutes.SignUpCellphoneScreen> {
            SignUpCellphoneScreen(go = {
                navController.navigate(
                    it
                )
            })
        }
        composable<AuthRoutes.SignUpBirthdayScreen> {
            SignUpBirthDateScreen(go = {
                navController.navigate(
                    it
                )
            })
        }
        composable<AuthRoutes.SignUpCityScreen> { SignUpCityScreen(go = { navController.navigate(it) }) }
        composable<AuthRoutes.SignUpIDScreen> { SignUpIDScreen(go = { navController.navigate(it) }) }
    }
}