package com.moyee.android

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Chat
import androidx.compose.material.icons.outlined.Map
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.QueueMusic
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavController
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.moyee.android.ui.theme.MoyeeTheme
import com.moyee.android.util.MoyeePermission
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Timber.plant(Timber.DebugTree())

        setContent {
            MoyeeTheme {
                MoyeeApp()
            }
        }
    }
}

@Composable
fun MoyeeApp(viewModel: MainViewModel = viewModel()) {
    val isFirstAccess by viewModel.isFirstAccess.collectAsState()
    var showMainScreen by remember { mutableStateOf(false) }

    if (showMainScreen) {
        MainScreen()
    } else {
        CheckFirstAccess(
            navigateToMain = { showMainScreen = true },
            updateFirstAccessStatus = { viewModel.updateFirstAccessStatus(false) },
            isFirstAccess = isFirstAccess,
        )
    }
}

@Composable
fun MainScreen() {
    val navController = rememberNavController()

    Scaffold(
        bottomBar = {
            MoyeeBottomNavigationView(
                items = listOf(Screen.Map, Screen.Chat, Screen.Playlist, Screen.User),
                navController = navController,
                onItemClick = { screen ->
                    navController.navigate(screen.route) {
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }

                        launchSingleTop = true
                        restoreState = true
                    }
                }
            )
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Screen.Map.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(Screen.Map.route) { MapScreen() }
            composable(Screen.Chat.route) { }
            composable(Screen.Playlist.route) { }
            composable(Screen.User.route) { }
        }
    }
}

sealed class Screen(val route: String, val icon: ImageVector) {
    object Map : Screen("map", Icons.Outlined.Map)
    object Chat : Screen("chat", Icons.Outlined.Chat)
    object Playlist : Screen("playlist", Icons.Outlined.QueueMusic)
    object User : Screen("user", Icons.Outlined.Person)
}

@Composable
fun MoyeeBottomNavigationView(
    items: List<Screen>,
    navController: NavController,
    onItemClick: (Screen) -> Unit,
) {
    BottomNavigation(backgroundColor = MaterialTheme.colors.onBackground) {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentDestination = navBackStackEntry?.destination

        items.forEach { screen ->
            BottomNavigationItem(
                icon = { Icon(imageVector = screen.icon, contentDescription = screen.route) },
                selected = currentDestination?.hierarchy?.any { it.route == screen.route } == true,
                onClick = { onItemClick.invoke(screen) },
                selectedContentColor = MaterialTheme.colors.primary,
                unselectedContentColor = MaterialTheme.colors.onPrimary
            )
        }
    }
}

@Composable
private fun CheckFirstAccess(
    navigateToMain: () -> Unit,
    updateFirstAccessStatus: () -> Unit,
    isFirstAccess: Boolean,
) {
    if (isFirstAccess) {
        RequirePermissionScreen(
            moyeePermission = MoyeePermission.FOREGROUND_LOCATION,
            onAllPermissionsGranted = {
                navigateToMain.invoke()
                updateFirstAccessStatus.invoke()
            }
        )
    } else {
        navigateToMain.invoke()
    }
}