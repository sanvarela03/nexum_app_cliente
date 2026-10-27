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
import com.example.nexum_cliente.ui.navigation.rutes.PlaygroundRoutes
import com.example.nexum_cliente.ui.presenter.categories.CategoriesScreen
import com.example.nexum_cliente.ui.presenter.chat.ChatScreen
import com.example.nexum_cliente.ui.presenter.conversations.ConversationsScreen
import com.example.nexum_cliente.ui.presenter.home.HomeViewModel
import com.example.nexum_cliente.ui.presenter.job_applications.JobApplicationsScreen
import com.example.nexum_cliente.ui.presenter.job_offer.JobOfferScreen
import com.example.nexum_cliente.ui.presenter.mercado_pago_checkout.MercadoPagoCheckout
import com.example.nexum_cliente.ui.presenter.notifications.NotificationsScreen
import com.example.nexum_cliente.ui.presenter.playground.CreditCardFormPreview
import com.example.nexum_cliente.ui.presenter.playground.PlaygroundScreen
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
                    navController.navigate(JobOfferRoutes.TrackingScreen(offerId))
                }
            )
        }
        composable<HomeRoutes.WalletScreen> {
            WalletScreen()
        }

        composable<HomeRoutes.NotificationsScreen> {
            NotificationsScreen(
                onNavigateToApplications = { jobOfferId ->
                    navController.navigate(JobOfferRoutes.JobApplicationsScreen(jobOfferId = jobOfferId))
                }
            )
        }
        composable<DrawerRoutes.ProfileScreen> {
            ProfileScreen(
                homeViewModel = homeViewModel
            )
        }

        composable<DrawerRoutes.SettingsScreen> {
            SettingsScreen()
        }

        composable<DrawerRoutes.SafetyScreen> {
            MercadoPagoCheckout(
                onCancel = {}
            )
        }

        composable<DrawerRoutes.ContractsScreen> {
            com.example.nexum_cliente.ui.presenter.contracts.ContractsScreen(
                onOpenChat = { workerUserId, workerRole ->
                    navController.navigate(
                        HomeRoutes.ChatScreen(
                            conversationId = "new",
                            receiverId = workerUserId,
                            receiverRole = workerRole,
                            currentUserId = currentUserId.toString()
                        )
                    )
                },
                onNavigateBack = { navController.navigateUp() }
            )
        }


        composable<DrawerRoutes.PlaygroundScreen> {
            PlaygroundScreen(
                onNavigateTo = { route -> navController.navigate(route) }
            )
        }

        composable<PlaygroundRoutes.CreditCardForm> {
            CreditCardFormPreview(
                onNavigateBack = { navController.navigateUp() }
            )
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
                categoryName = args.categoryName,
                onNavigateBack = { navController.navigateUp() },
                onNavigateToTracking = { offerId ->
                    navController.navigate(JobOfferRoutes.TrackingScreen(offerId)) {
                        popUpTo(JobOfferRoutes.JobOfferScreen(args.categoryId, args.categoryName)) { inclusive = true }
                    }
                }
            )
        }

        composable<JobOfferRoutes.TrackingScreen> {
            val args = it.toRoute<JobOfferRoutes.TrackingScreen>()
            com.example.nexum_cliente.ui.presenter.job_offer_tracking.JobOfferTrackingScreen(
                jobOfferId = args.jobOfferId,
                onNavigateBack = {
                    if (!navController.popBackStack()) {
                        navController.navigate(HomeRoutes.CategoriesScreen) {
                            popUpTo(Graph.Home) { inclusive = false }
                            launchSingleTop = true
                        }
                    }
                },
                onNavigateToCheckout = { jobAppId, amount ->
                    navController.navigate(
                        JobOfferRoutes.CheckoutScreen(
                            jobApplicationId = jobAppId,
                            agreedAmount = amount
                        )
                    ) {
                        launchSingleTop = true
                    }
                }
            )
        }

        composable<JobOfferRoutes.JobApplicationsScreen> {
            JobApplicationsScreen(
                onNavigateBack = { navController.navigateUp() },
                onNavigateToCheckout = { jobAppId, amount ->
                    navController.navigate(
                        JobOfferRoutes.CheckoutScreen(
                            jobApplicationId = jobAppId,
                            agreedAmount = amount
                        )
                    ) {
                        launchSingleTop = true
                    }
                }
            )
        }

        composable<JobOfferRoutes.CheckoutScreen> { backStackEntry ->
            val args = backStackEntry.toRoute<JobOfferRoutes.CheckoutScreen>()
            MercadoPagoCheckout(
                jobApplicationId = args.jobApplicationId,
                agreedAmount = args.agreedAmount,
                onCancel = { navController.navigateUp() },
                onContractCreated = { contractId ->
                    navController.navigate(DrawerRoutes.ContractsScreen) {
                        popUpTo(HomeRoutes.CategoriesScreen) { inclusive = false }
                        launchSingleTop = true
                    }
                }
            )
        }

    }
}