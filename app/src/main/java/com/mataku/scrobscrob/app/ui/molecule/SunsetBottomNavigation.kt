package com.mataku.scrobscrob.app.ui.molecule

import androidx.annotation.DrawableRes
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.mataku.scrobscrob.R
import com.mataku.scrobscrob.ui_common.style.Colors

@Composable
fun SunsetBottomNavigation(navController: NavController) {
    BottomNavigation() {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.route
        SunsetBottomNavItem.values().forEach { item ->
            BottomNavigationItem(
                label = { Text(text = item.title) },
                icon = {
                    Icon(
                        painterResource(item.iconDrawable),
                        contentDescription = item.title
                    )
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
                selectedContentColor = Colors.LastFmColor,
                unselectedContentColor = Color.Black.copy(0.4f)
            )
        }
    }
}

enum class SunsetBottomNavItem(
    @DrawableRes val iconDrawable: Int,
    val title: String,
    val screenRoute: String
) {
    SCROBBLE(
        R.drawable.ic_last_fm_logo,
        "Scrobble",
        "scrobble"
    ),
    TOP_ALBUMS(
        R.drawable.ic_album_black_24px,
        "Top Albums",
        "top_albums"
    ),
    TOP_ARTISTS(
        R.drawable.ic_account_circle_black,
        "Top Artists",
        "top_artists"
    )
}