package com.example.nexum_cliente.ui.presenter.home

import android.annotation.SuppressLint
import android.content.res.Configuration
import android.os.Build
import android.view.Gravity
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.width
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
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
import android.content.Intent
import com.example.nexum_cliente.ui.navigation.rutes.JobOfferRoutes
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
    initialIntent: Intent? = null
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

    // Manejo de deep link desde notificaciones
    LaunchedEffect(initialIntent) {
        if (initialIntent != null) {
            val type = initialIntent.getStringExtra("type")
            val jobOfferId = initialIntent.getStringExtra("jobOfferId")

            if (type == "NEW_JOB_APPLICATION" && jobOfferId != null) {
                homeNavController.navigate(JobOfferRoutes.JobApplicationsScreen(jobOfferId = jobOfferId))
                initialIntent.removeExtra("type")
                initialIntent.removeExtra("jobOfferId")
                initialIntent.removeExtra("jobApplicationId")
            } else if (type == "WORK_COMPLETED" || type == "MEETING_VERIFIED" || type == "MEETING_SCHEDULED") {
                homeNavController.navigate(DrawerRoutes.ContractsScreen)
                initialIntent.removeExtra("type")
                initialIntent.removeExtra("contractId")
            }
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

    val mainScreens = MAIN_SCREENS

    // Aplicamos remember para seguir mejores prácticas sin alterar la estructura de insets
    val showBars = remember(currentDestination, isPortrait) {
        mainScreens.any { route ->
            currentDestination?.hierarchy?.any { it.hasRoute(route::class) } == true
        } && isPortrait
    }

    val scope = rememberCoroutineScope()
    BackHandler(enabled = drawerState.isOpen) {
        scope.launch { drawerState.close() }
    }

    ModalNavigationDrawer(
        drawerState = drawerState,
        gesturesEnabled = drawerState.isOpen, // Solo habilitado cuando está abierto para cerrarlo sin colisionar con mapas al estar cerrado
        drawerContent = {
            ModalDrawerSheet(
                modifier = Modifier.width(200.dp),
                drawerContainerColor = MaterialTheme.colorScheme.surface,
                drawerContentColor = MaterialTheme.colorScheme.onSurface
            ) {
                NavigationDrawerHeader(
                    value = listOfNotNull(state.client?.firstName, state.client?.lastName)
                        .filter { it.isNotBlank() }
                        .joinToString(" ")
                        .ifBlank { null },
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
            modifier = Modifier.fillMaxSize(),
            containerColor = MaterialTheme.colorScheme.background,
            contentColor = MaterialTheme.colorScheme.onBackground,
            contentWindowInsets = WindowInsets.safeDrawing,
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
                if (showBars) {
                    HomeBottomBar(
                        navigationItems = BottomNavigation.entries,
                        currentDestination = currentDestination,
                        navigateTo = onBottomNavigate
                    )
                }
            },
            // IMPORTANTE: Evita que la BottomBar suba con el teclado
//            contentWindowInsets = WindowInsets(0)
        ) { contentPadding ->
            Surface(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(contentPadding)
                    // Consumimos el padding para que el chat no lo duplique
                    .consumeWindowInsets(contentPadding),
                color = MaterialTheme.colorScheme.background,
                contentColor = MaterialTheme.colorScheme.onBackground
            ) {
                NavHost(
                    modifier = Modifier.background(MaterialTheme.colorScheme.background),
                    navController = navController,
                    startDestination = Graph.Home,
                    builder = homeGraph
                )
            }
        }
    }
}
