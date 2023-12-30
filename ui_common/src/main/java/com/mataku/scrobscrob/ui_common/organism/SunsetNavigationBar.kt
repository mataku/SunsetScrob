package com.mataku.scrobscrob.ui_common.organism

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.BlurredEdgeTreatment
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.RoundRect
import androidx.compose.ui.geometry.toRect
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.PathMeasure
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.mataku.scrobscrob.core.entity.AppTheme
import com.mataku.scrobscrob.ui_common.SunsetBottomNavItem
import com.mataku.scrobscrob.ui_common.style.LocalAppTheme
import com.mataku.scrobscrob.ui_common.style.SunsetThemePreview
import com.mataku.scrobscrob.ui_common.style.backgroundColor

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
  val selectedItem =
    SunsetBottomNavItem.entries.find { it.screenRoute == route } ?: SunsetBottomNavItem.SCROBBLE

  SunsetBottomNavigation(
    tabs = SunsetBottomNavItem.entries,
    selectedItem = selectedItem,
    onTabSelected = { item ->
      if (item == selectedItem) return@SunsetBottomNavigation

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
    }
  )
}

@Preview
@Composable
fun SunsetNavigationBarPreview() {
  SunsetThemePreview {
    Surface {
      SunsetNavigationBar(
        navController = NavHostController(LocalContext.current),
        navigateToScrobble = {},
        navigateToAccount = {},
        navigateToTopAlbums = {},
        navigateToTopArtists = {},
      )
    }
  }
}

@Preview
@Composable
fun SunsetNavigationBarLightPreview() {
  SunsetThemePreview(theme = AppTheme.LIGHT) {
    Surface {
      SunsetNavigationBar(
        navController = NavHostController(LocalContext.current),
        navigateToScrobble = {},
        navigateToAccount = {},
        navigateToTopAlbums = {},
        navigateToTopArtists = {},
      )
    }
  }
}

@Composable
fun SunsetBottomNavigation(
  tabs: List<SunsetBottomNavItem>,
  selectedItem: SunsetBottomNavItem,
  onTabSelected: (SunsetBottomNavItem) -> Unit,
  modifier: Modifier = Modifier,
) {
  Box(
    modifier = modifier
      .padding(vertical = 16.dp, horizontal = 48.dp)
      .fillMaxWidth()
      .height(64.dp)
      .border(
        width = Dp.Hairline,
        brush = Brush.verticalGradient(
          colors = listOf(
            MaterialTheme.colorScheme.onSecondary.copy(alpha = .4f),
            MaterialTheme.colorScheme.onSecondary.copy(alpha = .2f),
          ),
        ),
        shape = CircleShape
      )
  ) {

    val selectedTabIndex = SunsetBottomNavItem.entries.indexOf(selectedItem)
    val animatedSelectedTabIndex by animateFloatAsState(
      targetValue = selectedTabIndex.toFloat(), label = "animatedSelectedTabIndex",
      animationSpec = spring(
        stiffness = Spring.StiffnessLow,
        dampingRatio = Spring.DampingRatioLowBouncy,
      )
    )

    val animatedColor by animateColorAsState(
      targetValue = LocalAppTheme.current.backgroundColor(),
      label = "animatedColor",
      animationSpec = spring(
        stiffness = Spring.StiffnessLow,
      )
    )

    Canvas(
      modifier = Modifier
        .fillMaxSize()
        .clip(CircleShape)
        .blur(50.dp, edgeTreatment = BlurredEdgeTreatment.Unbounded)
    ) {
      val tabWidth = size.width / SunsetBottomNavItem.entries.size
      drawCircle(
        color = animatedColor.copy(alpha = .6f),
        radius = size.height / 2,
        center = Offset(
          (tabWidth * animatedSelectedTabIndex) + tabWidth / 2,
          size.height / 2
        )
      )
    }

    Canvas(
      modifier = Modifier
        .fillMaxSize()
        .clip(CircleShape)
        .alpha(.90F)
        .background(MaterialTheme.colorScheme.primary)
    ) {
      val path = Path().apply {
        addRoundRect(RoundRect(size.toRect(), CornerRadius(size.height)))
      }
      val length = PathMeasure().apply { setPath(path, false) }.length

      val tabWidth = size.width / SunsetBottomNavItem.entries.size
      drawPath(
        path,
        brush = Brush.horizontalGradient(
          colors = listOf(
            animatedColor.copy(alpha = 0f),
            animatedColor.copy(alpha = 1f),
            animatedColor.copy(alpha = 1f),
            animatedColor.copy(alpha = 0f),
          ),
          startX = tabWidth * animatedSelectedTabIndex,
          endX = tabWidth * (animatedSelectedTabIndex + 1),
        ),
        style = Stroke(
          width = 6f,
          pathEffect = PathEffect.dashPathEffect(
            intervals = floatArrayOf(length / 2, length)
          )
        )
      )
    }
    BottomBarTabs(
      tabs = tabs,
      selectedTab = selectedItem,
      onTabSelected = onTabSelected
    )
  }
}

@Composable
private fun BottomBarTabs(
  tabs: List<SunsetBottomNavItem>,
  selectedTab: SunsetBottomNavItem,
  onTabSelected: (SunsetBottomNavItem) -> Unit,
) {
  Row(
    modifier = Modifier
      .fillMaxSize(),
  ) {
    for (tab in tabs) {
      val alpha by animateFloatAsState(
        targetValue = if (selectedTab == tab) 1f else .50f,
        label = "alpha"
      )
      val scale by animateFloatAsState(
        targetValue = if (selectedTab == tab) 1f else .98f,
        visibilityThreshold = .000001f,
        animationSpec = spring(
          stiffness = Spring.StiffnessLow,
          dampingRatio = Spring.DampingRatioMediumBouncy,
        ),
        label = "scale"
      )
      Column(
        modifier = Modifier
          .scale(scale)
          .alpha(alpha)
          .fillMaxHeight()
          .weight(1f)
          .clickable(
            interactionSource = remember { MutableInteractionSource() },
            indication = null,
            enabled = true,
            onClickLabel = "${tab.title} click",
            role = null
          ) {
            onTabSelected(tab)
          },
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
      ) {
        if (tab == SunsetBottomNavItem.SCROBBLE) {
          Icon(painterResource(id = tab.iconDrawable!!), contentDescription = "tab ${tab.title}")
        } else {
          Icon(imageVector = tab.icon!!, contentDescription = "tab ${tab.title}")
        }
        Text(
          text = tab.title,
          fontSize = 12.sp
        )
      }
    }
  }
}
