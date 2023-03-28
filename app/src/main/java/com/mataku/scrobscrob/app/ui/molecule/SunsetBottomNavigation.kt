package com.mataku.scrobscrob.app.ui.molecule

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.LibraryMusic
import androidx.compose.material.icons.filled.Settings
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.mataku.scrobscrob.account.ui.navigation.navigateToAccount
import com.mataku.scrobscrob.album.ui.navigation.navigateToTopAlbums
import com.mataku.scrobscrob.artist.ui.navigation.navigateToTopArtists
import com.mataku.scrobscrob.ui_common.SunsetBottomNavItem
import com.mataku.scrobscrob.ui_common.SunsetTextStyle
import com.mataku.scrobscrob.ui_common.navigateToScrobble
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
            SunsetBottomNavItem.SCROBBLE -> {
              Icon(
                painterResource(item.iconDrawable!!),
                contentDescription = item.title,
                tint = iconColor,
                modifier = Modifier
                  .size(23.dp)
                  .padding(bottom = 1.dp)
              )
            }
            SunsetBottomNavItem.TOP_ALBUMS -> {
              Icon(
                imageVector = Icons.Default.LibraryMusic,
                contentDescription = item.title,
                tint = iconColor
              )
            }
            SunsetBottomNavItem.TOP_ARTISTS -> {
              Icon(
                imageVector = Icons.Default.AccountCircle,
                contentDescription = item.title,
                tint = iconColor
              )
            }
            SunsetBottomNavItem.ACCOUNT -> {
              Icon(
                imageVector = Icons.Default.Settings,
                contentDescription = item.title,
                tint = iconColor
              )
            }
          }
        },
        selected = currentRoute == item.screenRoute,
        onClick = {
          when (item) {
            SunsetBottomNavItem.SCROBBLE -> {
              navController.navigateToScrobble()
            }
            SunsetBottomNavItem.TOP_ALBUMS -> {
              navController.navigateToTopAlbums()
            }
            SunsetBottomNavItem.TOP_ARTISTS -> {
              navController.navigateToTopArtists()
            }
            SunsetBottomNavItem.ACCOUNT -> {
              navController.navigateToAccount()
            }
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
