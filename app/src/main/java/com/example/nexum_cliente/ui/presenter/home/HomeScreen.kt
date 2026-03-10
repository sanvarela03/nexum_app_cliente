package com.example.nexum_cliente.ui.presenter.home

import android.annotation.SuppressLint
import android.content.res.Configuration
import android.os.Build
import android.view.Gravity
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.nexum_cliente.ui.components.AppToolBar
import com.example.nexum_cliente.ui.components.HomeBottomBar
import com.example.nexum_cliente.ui.components.NavigationDrawerBody
import com.example.nexum_cliente.ui.components.NavigationDrawerHeader
import com.example.nexum_cliente.ui.navigation.graphs.Graph
import com.example.nexum_cliente.ui.navigation.graphs.homeGraph
import com.example.nexum_cliente.ui.navigation.rutes.DrawerRoutes
import com.example.nexum_cliente.ui.navigation.rutes.HomeRoutes
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch

@RequiresApi(Build.VERSION_CODES.O)
@SuppressLint("RestrictedApi")
@Composable
fun HomeScreen(
    homeViewModel: HomeViewModel = hiltViewModel(),
) {
    var currentUserId by rememberSaveable { mutableStateOf<Long?>(null) }

    LaunchedEffect(Unit) {
        currentUserId = homeViewModel.getUserId().firstOrNull()
    }

    val homeNavController = rememberNavController()
    val scope = rememberCoroutineScope()
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val state = homeViewModel.state
    val context = LocalContext.current

    LaunchedEffect(key1 = Unit) {
        homeViewModel.refreshClient(fetchFromRemote = true)
    }

    LaunchedEffect(state.errorMessage) {
        if (state.errorMessage.isNotEmpty()) {
            val toast = Toast.makeText(context, "Error: ${state.errorMessage}", Toast.LENGTH_SHORT)
            toast.setGravity(Gravity.BOTTOM, 0, 300)
            toast.show()
        }
    }

    currentUserId?.let {
        HomeScreenContent(
            navController = homeNavController,
            drawerState = drawerState,
            state = state,
            onDrawerNavigate = { route ->
                scope.launch { drawerState.close() }
                homeNavController.navigate(route) {
                    popUpTo(route) { inclusive = true }
                }
            },
            onBottomNavigate = { route ->
                homeNavController.navigate(route) {
                    popUpTo(homeNavController.graph.findStartDestination().id) {
                        saveState = true
                    }
                    restoreState = true
                    launchSingleTop = true
                }
            },
            onNavButtonClicked = {
                scope.launch { drawerState.apply { if (isClosed) open() else close() } }
            },
            onSignOut = {
                homeViewModel.onEvent(HomeEvent.SignOutBtnClicked)
            },
            homeGraph = {
                homeGraph(
                    navController = homeNavController,
                    homeViewModel = homeViewModel,
                    currentUserId = currentUserId
                )
            }
        )
    }
}

@Composable
private fun HomeScreenContent(
    navController: NavHostController,
    drawerState: DrawerState,
    state: HomeState,
    onDrawerNavigate: (DrawerRoutes) -> Unit,
    onBottomNavigate: (HomeRoutes) -> Unit,
    onNavButtonClicked: () -> Unit,
    onSignOut: () -> Unit,
    homeGraph: NavGraphBuilder.() -> Unit
) {
    val configuration = LocalConfiguration.current
    val isPortrait = configuration.orientation == Configuration.ORIENTATION_PORTRAIT
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    val mainScreens = listOf(
        HomeRoutes.CategoriesScreen,
        HomeRoutes.RequestsScreen,
        HomeRoutes.WalletScreen,
        HomeRoutes.NotificationsScreen
    )

    val showBars = mainScreens.any { route ->
        currentDestination?.hierarchy?.any { it.hasRoute(route::class) } == true
    } && isPortrait

    ModalNavigationDrawer(
        drawerState = drawerState,
        gesturesEnabled = showBars,
        drawerContent = {
            ModalDrawerSheet(modifier = Modifier.width(200.dp)) {
                NavigationDrawerHeader(
                    value = "${state.client?.firstName} ${state.client?.lastName}",
                    name = state.client?.email,
                    profileImgURL = state.client?.profilePictureUrl
                )
                NavigationDrawerBody(
                    items = DrawerNavigation.entries,
                    navigateTo = onDrawerNavigate
                )
            }
        }
    ) {
        Scaffold(
            modifier = Modifier.background(Color.White),
            topBar = {
                if (showBars) {
                    AppToolBar(
                        toolbarTitle = "Bienvenido a Nexum",
                        signOutButtonClicked = onSignOut,
                        navButtonClicked = onNavButtonClicked
                    )
                }
            },
            bottomBar = {
                HomeBottomBar(
                    navigationItems = BottomNavigation.entries,
                    currentDestination = currentDestination,
                    navigateTo = onBottomNavigate
                )
            },
            // IMPORTANTE: Evita que la BottomBar suba con el teclado
            contentWindowInsets = WindowInsets(0)
        ) { contentPadding ->
            Surface(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.White)
                    .padding(contentPadding)
                    // Consumimos el padding para que el chat no lo duplique
                    .consumeWindowInsets(contentPadding)
            ) {
                NavHost(
                    modifier = Modifier.background(Color.White),
                    navController = navController,
                    startDestination = Graph.Home,
                    builder = homeGraph
                )
            }
        }
    }
}
