package com.example.neuxum_cliente.ui.navigation.graphs

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import androidx.navigation.toRoute
import com.example.neuxum_cliente.ui.navigation.rutes.DrawerRoutes
import com.example.neuxum_cliente.ui.navigation.rutes.HomeRoutes
import com.example.neuxum_cliente.ui.navigation.rutes.JobOfferRoutes
import com.example.neuxum_cliente.ui.presenter.categories.CategoriesScreen
import com.example.neuxum_cliente.ui.presenter.job_offer.JobOfferScreen
import com.example.neuxum_cliente.ui.presenter.notifications.NotificationsScreen
import com.example.neuxum_cliente.ui.presenter.profile.ProfileScreen
import com.example.neuxum_cliente.ui.presenter.requests.RequestsScreen
import com.example.neuxum_cliente.ui.presenter.settings.SettingsScreen
import com.example.neuxum_cliente.ui.presenter.wallet.WalletScreen

/**
 * @author Santiago Varela Daza
 * @email svarela03@uan.edu.co
 * @github https://github.com/sanvarela03
 * @since 8/4/2025
 * @version 1.0
 */
fun NavGraphBuilder.homeGraph(navController: NavController) {
    navigation<Graph.HomeGraph>(startDestination = HomeRoutes.CategoriesScreen) {
        composable<HomeRoutes.CategoriesScreen> {
            CategoriesScreen(navController = navController)
        }
        composable<HomeRoutes.RequestsScreen> {
            RequestsScreen()
        }
        composable<HomeRoutes.WalletScreen> {
            WalletScreen()
        }

        composable<HomeRoutes.NotificationsScreen> {
            NotificationsScreen()
        }
        composable<DrawerRoutes.ProfileScreen> {
            ProfileScreen()
        }

        composable<DrawerRoutes.SettingsScreen> {
            SettingsScreen()
        }

        composable<JobOfferRoutes.JobOfferScreen> {
            val args = it.toRoute<JobOfferRoutes.JobOfferScreen>()
            JobOfferScreen(categoryId = args.categoryId)
        }
    }
}