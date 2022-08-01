package com.mataku.scrobscrob.app.ui.molecule

import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.mataku.scrobscrob.ui_common.SunsetBottomNavItem
import com.mataku.scrobscrob.ui_common.SunsetTextStyle
import com.mataku.scrobscrob.ui_common.style.LocalAppTheme
import com.mataku.scrobscrob.ui_common.style.SunsetTheme
import com.mataku.scrobscrob.ui_common.style.accentColor

@Composable
fun SunsetBottomNavigation(navController: NavController) {
    BottomNavigation(
        backgroundColor = MaterialTheme.colors.primary
    ) {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.route
        SunsetBottomNavItem.values().forEach { item ->
            BottomNavigationItem(
                label = {
                    val textColor = if (currentRoute == item.screenRoute) {
                        MaterialTheme.colors.onPrimary
                    } else {
                        MaterialTheme.colors.onPrimary.copy(
                            alpha = 0.4F
                        )
                    }
                    Text(
                        text = item.title, style = SunsetTextStyle.caption.copy(
                            color = textColor,
                            fontSize = 12.sp
                        )
                    )
                },
                icon = {
                    val iconColor = if (currentRoute == item.screenRoute) {
                        LocalAppTheme.current.accentColor()
                    } else {
                        MaterialTheme.colors.onPrimary.copy(
                            alpha = 0.4F
                        )
                    }
                    when (item) {
                        SunsetBottomNavItem.SCROBBLE, SunsetBottomNavItem.TOP_ALBUMS, SunsetBottomNavItem.TOP_ARTISTS -> {
                            Icon(
                                painterResource(item.iconDrawable!!),
                                contentDescription = item.title,
                                tint = iconColor
                            )
                        }
                        SunsetBottomNavItem.ACCOUNT -> {
                            Icon(
                                imageVector = Icons.Default.AccountCircle,
                                contentDescription = item.title,
                                tint = iconColor
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
                }
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