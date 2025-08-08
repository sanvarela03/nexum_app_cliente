package com.example.neuxum_cliente.ui.presenter.home

import android.annotation.SuppressLint
import android.content.res.Configuration
import android.view.Gravity
import android.widget.Toast
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.neuxum_cliente.ui.componets.AppToolBar
import com.example.neuxum_cliente.ui.componets.HomeBottomBar
import com.example.neuxum_cliente.ui.componets.NavigationDrawerBody
import com.example.neuxum_cliente.ui.componets.NavigationDrawerHeader
import com.example.neuxum_cliente.ui.navigation.graphs.Graph
import com.example.neuxum_cliente.ui.navigation.graphs.homeGraph
import com.example.neuxum_cliente.ui.navigation.rutes.HomeRoutes
import kotlinx.coroutines.launch

/**
 * @author Santiago Varela Daza
 * @email svarela03@uan.edu.co
 * @github https://github.com/sanvarela03
 * @since 7/30/2025
 * @version 1.0
 */
@SuppressLint("RestrictedApi")
@Composable
fun HomeScreen(
    homeViewModel: HomeViewModel = hiltViewModel(),
) {
    val homeNavController = rememberNavController()
    val state = homeViewModel.state
    val client = state.clientEntity
    val scope = rememberCoroutineScope()
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val configuration = LocalConfiguration.current
    val isLandscape = configuration.orientation == Configuration.ORIENTATION_LANDSCAPE
    val isPortrait = configuration.orientation == Configuration.ORIENTATION_PORTRAIT

    val screens = listOf(
        HomeRoutes.CategoriesScreen,
        HomeRoutes.RequestsScreen,
        HomeRoutes.WalletScreen,
        HomeRoutes.NotificationsScreen
    )

    LaunchedEffect(key1 = Unit) {
        homeViewModel.updateClient(fetchFromRemote = true)
    }

    if (state.errorMessage.isNotEmpty()) {
        ErrorMsgToast(state)
    }


    val navBackStackEntry = homeNavController.currentBackStackEntryAsState().value
    val currentDestination = navBackStackEntry?.destination

    val showTopBar = screens.any { destination ->
        currentDestination?.hierarchy?.any {
            it.hasRoute(destination::class)
        } == true
    } && isPortrait


    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            if (showTopBar) {
                ModalDrawerSheet(
                    modifier = Modifier.width(200.dp)
                ) {
                    NavigationDrawerHeader(
                        value = client?.firstName + " " + client?.lastName,
                        name = client?.email,
                        profileImgURL = client?.imgUrl
                    )
                    NavigationDrawerBody(DrawerNavigation.entries, navigateTo = {
                        scope.launch { drawerState.apply { if (isClosed) open() else close() } }
                        homeNavController.navigate(it) {
                            popUpTo(it) {
                                inclusive = true
                            }
                        }
                    })
                }
            }
        }
    ) {
        Scaffold(
            topBar = {
                if (showTopBar) {
                    AppToolBar(
                        toolbarTitle = "Bienvenido a Nexum",
                        signOutButtonClicked = {
                            homeViewModel.onEvent(HomeEvent.SignOutBtnClicked)
                        },
                        navButtonClicked = {
                            scope.launch { drawerState.apply { if (isClosed) open() else close() } }
                        }
                    )
                }
            },
            bottomBar = {
                HomeBottomBar(
                    navigationItems = BottomNavigation.entries,
                    currentDestination = currentDestination,
                    navigateTo = {
                        homeNavController.navigate(it) {
                            popUpTo(homeNavController.graph.findStartDestination().id) {
                                saveState = true
                            }
                            restoreState = true
                            launchSingleTop = true
                        }
//                        {
//                            popUpTo(homeNavController.graph.findStartDestination().id) {
//                                saveState = true
//                            }
//                            restoreState = true
//                            launchSingleTop = true
//                        }
                    }
                )
            }
        ) { contentPadding ->
            Surface(
                modifier = Modifier
                    .fillMaxSize()
//                    .background(Color.White)
                    .padding(contentPadding)
            ) {
                NavHost(
                    navController = homeNavController,
                    startDestination = Graph.HomeGraph
                ) {
                    homeGraph(navController = homeNavController)
                }
            }
        }

    }
}

@Composable
private fun ErrorMsgToast(state: HomeState) {
    val context = LocalContext.current.applicationContext
    val toast =
        Toast.makeText(
            context,
            "Error: ${state.errorMessage}",
            Toast.LENGTH_SHORT
        )
    toast.setGravity(Gravity.BOTTOM, 0, 300)
    toast.show()
}

