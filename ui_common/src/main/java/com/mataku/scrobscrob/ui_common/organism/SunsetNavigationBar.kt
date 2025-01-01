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
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.mataku.scrobscrob.core.entity.AppTheme
import com.mataku.scrobscrob.ui_common.SunsetBottomNavItem
import com.mataku.scrobscrob.ui_common.style.LocalAppTheme
import com.mataku.scrobscrob.ui_common.style.SunsetThemePreview
import com.mataku.scrobscrob.ui_common.style.backgroundColor
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList

@Composable
fun SunsetNavigationBar(
  navController: NavHostController,
  navigateToAccount: () -> Unit,
  navigateToDiscover: () -> Unit,
  navigateToHome: () -> Unit,
  modifier: Modifier = Modifier,
  hasNavigationBarScreen: Boolean = true,
) {
  val backStackEntry = navController.currentBackStackEntryAsState()
  val route = backStackEntry.value?.destination?.route
  val selectedItem = SunsetBottomNavItem.currentItem(route?.split("?")?.get(0))

  if (hasNavigationBarScreen) {
    SunsetBottomNavigation(
      tabs = SunsetBottomNavItem.entries.toImmutableList(),
      selectedItem = selectedItem,
      onTabSelected = { item ->
        if (item == selectedItem) return@SunsetBottomNavigation

        when (item) {
          SunsetBottomNavItem.ACCOUNT -> {
            navigateToAccount.invoke()
          }

          SunsetBottomNavItem.DISCOVER -> {
            navigateToDiscover.invoke()
          }

          SunsetBottomNavItem.HOME -> {
            navigateToHome.invoke()
          }
        }
      },
      modifier = modifier
    )
  }
}

@Preview
@Composable
private fun SunsetNavigationBarPreview() {
  SunsetThemePreview {
    Surface {
      Scaffold(
        bottomBar = {
          SunsetNavigationBar(
            navController = NavHostController(LocalContext.current),
            navigateToAccount = {},
            navigateToDiscover = {},
            navigateToHome = {},
            hasNavigationBarScreen = true
          )
        }
      ) {
        Box(
          modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(it)
        )
      }
    }
  }
}

@Preview
@Composable
private fun SunsetNavigationBarLightPreview() {
  SunsetThemePreview(theme = AppTheme.LIGHT) {
    Surface {
      SunsetNavigationBar(
        navController = NavHostController(LocalContext.current),
        navigateToAccount = {},
        navigateToDiscover = {},
        navigateToHome = {},
        modifier = Modifier,
        hasNavigationBarScreen = true
      )
    }
  }
}

@Composable
fun SunsetBottomNavigation(
  tabs: ImmutableList<SunsetBottomNavItem>,
  selectedItem: SunsetBottomNavItem?,
  onTabSelected: (SunsetBottomNavItem) -> Unit,
  modifier: Modifier = Modifier,
) {
  val screenDp = LocalConfiguration.current.screenWidthDp
  val horizontalPadding = screenDp.dp - 208.dp
  Box(
    modifier = modifier
      .fillMaxWidth()
      .padding(
        horizontal = horizontalPadding / 2,
        vertical = 16.dp,
      )
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
      ),
    contentAlignment = Alignment.Center
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
        .alpha(.85F)
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
  tabs: ImmutableList<SunsetBottomNavItem>,
  selectedTab: SunsetBottomNavItem?,
  onTabSelected: (SunsetBottomNavItem) -> Unit,
) {
  Row(
    modifier = Modifier
      .fillMaxSize()
      .padding(
        horizontal = 8.dp
      ),
  ) {
    for (tab in tabs) {
      val alpha by animateFloatAsState(
        targetValue = if (selectedTab == tab) 1f else .50f,
        label = "alpha"
      )
      val scale by animateFloatAsState(
        targetValue = if (selectedTab == tab) 1f else .85f,
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
        if (tab == SunsetBottomNavItem.HOME) {
          Icon(
            imageVector = ImageVector.vectorResource(id = tab.iconDrawable!!),
            contentDescription = "tab ${tab.title}"
          )
        } else {
          Icon(imageVector = tab.icon!!, contentDescription = "tab ${tab.title}")
        }
      }
    }
  }
}
