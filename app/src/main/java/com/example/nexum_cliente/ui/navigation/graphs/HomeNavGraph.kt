package com.example.nexum_cliente.ui.navigation.graphs

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import androidx.navigation.toRoute
import com.example.nexum_cliente.ui.navigation.rutes.DrawerRoutes
import com.example.nexum_cliente.ui.navigation.rutes.HomeRoutes
import com.example.nexum_cliente.ui.navigation.rutes.JobOfferRoutes
import com.example.nexum_cliente.ui.presenter.categories.CategoriesScreen
import com.example.nexum_cliente.ui.presenter.chat.ChatScreen
import com.example.nexum_cliente.ui.presenter.conversations.ConversationsScreen
import com.example.nexum_cliente.ui.presenter.home.HomeViewModel
import com.example.nexum_cliente.ui.presenter.job_offer.JobOfferScreen
import com.example.nexum_cliente.ui.presenter.mercado_pago_checkout.MercadoPagoCheckout
import com.example.nexum_cliente.ui.presenter.profile.ProfileScreen
import com.example.nexum_cliente.ui.presenter.requests.RequestsScreen
import com.example.nexum_cliente.ui.presenter.settings.SettingsScreen
import com.example.nexum_cliente.ui.presenter.wallet.WalletScreen

/**
 * @author Santiago Varela Daza
 * @email svarela03@uan.edu.co
 * @github https://github.com/sanvarela03
 * @since 8/4/2025
 * @version 1.0
 */
@RequiresApi(Build.VERSION_CODES.O)
fun NavGraphBuilder.homeGraph(
    navController: NavController,
    homeViewModel: HomeViewModel,
    currentUserId: Long? = null
) {
    navigation<Graph.Home>(startDestination = HomeRoutes.CategoriesScreen) {
        composable<HomeRoutes.CategoriesScreen> {
            CategoriesScreen(navController = navController)
        }
        composable<HomeRoutes.RequestsScreen> {
            RequestsScreen(
                navigateToTracking = { offerId ->
                    navController.navigate(
                        JobOfferRoutes.TrackingScreen(offerId)
                    ) {
                        popUpTo(HomeRoutes.CategoriesScreen) { inclusive = true }
                    }
                }
            )
        }
        composable<HomeRoutes.WalletScreen> {
            WalletScreen()
        }

        composable<HomeRoutes.NotificationsScreen> {
//            NotificationsScreen()
            MercadoPagoCheckout()
        }
        composable<DrawerRoutes.ProfileScreen> {
            ProfileScreen(
                homeViewModel = homeViewModel
            )
        }

        composable<DrawerRoutes.SettingsScreen> {
            SettingsScreen()
        }
        composable<HomeRoutes.ConversationsScreen> {
            ConversationsScreen(
                currentUserId = currentUserId.toString(),
                onConversationClick = { conversationId, otherUserId, otherUserRole ->
                    navController.navigate(
                        HomeRoutes.ChatScreen(
                            conversationId = conversationId,
                            receiverId = otherUserId,
                            receiverRole = otherUserRole,
                            currentUserId = currentUserId.toString()
                        )
                    )
                }
            )
        }
        composable<HomeRoutes.ChatScreen> { backStackEntry ->
            val args = backStackEntry.toRoute<HomeRoutes.ChatScreen>()
            ChatScreen(
                conversationId = args.conversationId,
                receiverId = args.receiverId,
                receiverRole = args.receiverRole,
                currentUserId = args.currentUserId,
                onNavigateBack = { navController.navigateUp() }
            )
        }

            composable<JobOfferRoutes.JobOfferScreen> {
            val args = it.toRoute<JobOfferRoutes.JobOfferScreen>()
            JobOfferScreen(
                categoryId = args.categoryId,
                onNavigateToTracking = { offerId ->
                    navController.navigate(JobOfferRoutes.TrackingScreen(offerId)) {
                        popUpTo(JobOfferRoutes.JobOfferScreen(args.categoryId)) { inclusive = true }
                    }
                }
            )
        }

        composable<JobOfferRoutes.TrackingScreen> {
            val args = it.toRoute<JobOfferRoutes.TrackingScreen>()
            com.example.nexum_cliente.ui.presenter.job_offer_tracking.JobOfferTrackingScreen(
                jobOfferId = args.jobOfferId,
                onNavigateBack = { navController.navigateUp() }
            )
        }

    }
}