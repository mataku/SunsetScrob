package com.mataku.scrobscrob.app.ui.molecule

import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.mataku.scrobscrob.ui_common.SunsetBottomNavItem
import com.mataku.scrobscrob.ui_common.style.Colors
import com.mataku.scrobscrob.ui_common.style.SunsetTheme

@Composable
fun SunsetBottomNavigation(navController: NavController) {
    BottomNavigation() {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.route
        SunsetBottomNavItem.values().forEach { item ->
            BottomNavigationItem(
                label = { Text(text = item.title) },
                icon = {
                    when (item) {
                        SunsetBottomNavItem.SCROBBLE, SunsetBottomNavItem.TOP_ALBUMS, SunsetBottomNavItem.TOP_ARTISTS -> {
                            Icon(
                                painterResource(item.iconDrawable!!),
                                contentDescription = item.title
                            )
                        }
                        SunsetBottomNavItem.ACCOUNT -> {
                            Icon(
                                imageVector = Icons.Default.AccountCircle,
                                contentDescription = item.title
                            )
                        }
                    }
                },
                selected = currentRoute == item.screenRoute,
                onClick = {
                    navController.navigate(item.screenRoute) {
                        navController.graph.startDestinationRoute?.let { screenRoute ->
                            popUpTo(screenRoute) {
                                saveState = true
                            }
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                selectedContentColor = Colors.Lime,
                unselectedContentColor = Color.LightGray.copy(alpha = 0.4F)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SunsetBottomNavPreview() {
    SunsetTheme {
        Surface() {
            SunsetBottomNavigation(navController = NavController(LocalContext.current))
        }
    }
}