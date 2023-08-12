package com.mataku.scrobscrob.ui_common.organism

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.LibraryMusic
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.airbnb.android.showkase.annotation.ShowkaseComposable
import com.mataku.scrobscrob.ui_common.SunsetBottomNavItem
import com.mataku.scrobscrob.ui_common.style.LocalAppTheme
import com.mataku.scrobscrob.ui_common.style.SunsetThemePreview
import com.mataku.scrobscrob.ui_common.style.accentColor

@Composable
fun SunsetNavigationBar(
  navController: NavHostController,
  navigateToScrobble: () -> Unit,
  navigateToTopAlbums: () -> Unit,
  navigateToTopArtists: () -> Unit,
  navigateToAccount: () -> Unit,

  ) {
  val backStackEntry = navController.currentBackStackEntryAsState()
  val route = backStackEntry.value?.destination?.route
  val navItems = remember {
    SunsetBottomNavItem.values()
  }
  NavigationBar(
    containerColor = MaterialTheme.colorScheme.primary
  ) {
    navItems.forEach { item ->
      val selected = item.screenRoute == route

      val iconColor = if (selected) {
        LocalAppTheme.current.accentColor()
      } else {
        MaterialTheme.colorScheme.onPrimary.copy(
          alpha = 0.4F
        )
      }
      NavigationBarItem(
        selected = selected,
        onClick = {
          if (selected) return@NavigationBarItem

          when (item) {
            SunsetBottomNavItem.SCROBBLE -> {
              navigateToScrobble.invoke()
            }

            SunsetBottomNavItem.ACCOUNT -> {
              navigateToAccount.invoke()
            }

            SunsetBottomNavItem.TOP_ARTISTS -> {
              navigateToTopArtists.invoke()
            }

            SunsetBottomNavItem.TOP_ALBUMS -> {
              navigateToTopAlbums.invoke()
            }
          }
        },
        label = {
          Text(
            text = item.title,
            fontWeight = FontWeight.SemiBold
          )
        },
        icon = {
          when (item) {
            SunsetBottomNavItem.SCROBBLE -> {
              Icon(
                painterResource(item.iconDrawable!!),
                contentDescription = item.title,
                modifier = Modifier
                  .size(23.dp)
                  .padding(bottom = 1.dp),
                tint = iconColor
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
        }
      )
    }
  }
}

@Preview(showBackground = true)
@Composable
@ShowkaseComposable(name = "SunsetNavigationBar", group = "Navigation bar")
fun SunsetNavigationBarPreview() {
  SunsetThemePreview {
    Surface {
      SunsetNavigationBar(
        navController = NavHostController(LocalContext.current),
        navigateToScrobble = {},
        navigateToAccount = {},
        navigateToTopAlbums = {},
        navigateToTopArtists = {}
      )
    }
  }
}
