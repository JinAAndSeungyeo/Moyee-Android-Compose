package com.moyee.android

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Chat
import androidx.compose.material.icons.outlined.Map
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.QueueMusic
import androidx.compose.ui.graphics.vector.ImageVector
import com.moyee.android.ui.theme.MoyeeTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MoyeeTheme {

            }
        }
    }
}

sealed class Screen(val route: String, val icon: ImageVector) {
    object Map : Screen("map", Icons.Outlined.Map)
    object Chat : Screen("chat", Icons.Outlined.Chat)
    object Playlist : Screen("playlist", Icons.Outlined.QueueMusic)
    object User : Screen("user", Icons.Outlined.Person)
}