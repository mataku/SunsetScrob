package com.mataku.scrobscrob.album.ui.screen

import android.annotation.SuppressLint
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.mataku.scrobscrob.album.R
import com.mataku.scrobscrob.album.ui.molecule.TopAlbum
import com.mataku.scrobscrob.album.ui.state.TopAlbumsScreenState
import com.mataku.scrobscrob.core.entity.AlbumInfo
import com.mataku.scrobscrob.core.entity.AppTheme
import com.mataku.scrobscrob.ui_common.molecule.FilteringFloatingButton
import com.mataku.scrobscrob.ui_common.molecule.LoadingIndicator
import com.mataku.scrobscrob.ui_common.organism.ContentHeader
import com.mataku.scrobscrob.ui_common.organism.FilteringBottomSheet
import com.mataku.scrobscrob.ui_common.organism.InfiniteLoadingIndicator
import com.mataku.scrobscrob.ui_common.style.BOTTOM_APP_BAR_HEIGHT
import com.mataku.scrobscrob.ui_common.style.LocalAppTheme
import com.mataku.scrobscrob.ui_common.style.sunsetBackgroundGradient
import kotlinx.collections.immutable.ImmutableSet
import kotlinx.coroutines.launch

@SuppressLint("UnusedMaterialScaffoldPaddingParameter", "UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterialApi::class, ExperimentalMaterial3Api::class)
@Composable
fun TopAlbumsScreen(
  state: TopAlbumsScreenState
) {
  val uiState = state.uiState

  val contentWidth = state.contentWidth

  val sheetState = rememberModalBottomSheetState(ModalBottomSheetValue.Hidden)
  val coroutineScope = rememberCoroutineScope()
  BackHandler(sheetState.isVisible) {
    coroutineScope.launch {
      sheetState.hide()
    }
  }

  ModalBottomSheetLayout(
    sheetContent = {
      FilteringBottomSheet(
        selectedTimeRangeFiltering = uiState.timeRangeFiltering,
        onClick = {
          coroutineScope.launch {
            sheetState.hide()
          }
          state.updateTimeRangeFiltering(it)
        },
        modifier = Modifier
          .padding(bottom = BOTTOM_APP_BAR_HEIGHT)
      )
    },
    sheetState = sheetState,
    sheetBackgroundColor = MaterialTheme.colorScheme.background
  ) {
    Scaffold(
      floatingActionButton = {
        FilteringFloatingButton(
          onClick = {
            coroutineScope.launch {
              sheetState.show()
            }
          },
          modifier = Modifier
            .padding(bottom = BOTTOM_APP_BAR_HEIGHT)
        )
      }
    ) {
      TopAlbumsContent(
        albums = uiState.topAlbums,
        hasNext = uiState.hasNext,
        imageSize = contentWidth.dp,
        padding = contentWidth.dp - 20.dp,
        onUrlTap = {
          state.onTapAlbum(it)
        },
        onScrollEnd = {
          state.onScrollEnd()
        }
      )
    }
  }

  if (uiState.isLoading && uiState.topAlbums.isEmpty()) {
    Box(
      modifier = Modifier.fillMaxSize(),
      contentAlignment = Alignment.Center
    ) {
      LoadingIndicator(
        modifier = Modifier
      )
    }
  }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun TopAlbumsContent(
  albums: ImmutableSet<AlbumInfo>,
  hasNext: Boolean,
  imageSize: Dp,
  padding: Dp,
  onUrlTap: (String) -> Unit,
  onScrollEnd: () -> Unit
) {
  LazyColumn(
    content = {
      stickyHeader {
        ContentHeader(text = stringResource(id = R.string.menu_top_albums))
      }

      items(albums.chunked(2)) {
        val rightItem = if (it.size == 1) null else it[1]
        TopAlbumsGridRow(
          leftItem = it[0],
          rightItem = rightItem,
          imageSize = imageSize - 24.dp,
          onAlbumTap = onUrlTap,
          modifier = Modifier.padding(horizontal = 8.dp)
        )
      }
      if (hasNext) {
        item {
          InfiniteLoadingIndicator(
            onScrollEnd = onScrollEnd,
            padding = padding
          )
        }
      }
    },
    modifier = if (LocalAppTheme.current == AppTheme.SUNSET) {
      Modifier
        .fillMaxSize()
        .background(
          brush = sunsetBackgroundGradient
        )
        .padding(bottom = BOTTOM_APP_BAR_HEIGHT)
    } else {
      Modifier
        .fillMaxSize()
        .padding(bottom = BOTTOM_APP_BAR_HEIGHT)
    }
  )
}

@Composable
private fun TopAlbumsGridRow(
  leftItem: AlbumInfo,
  rightItem: AlbumInfo?,
  imageSize: Dp,
  onAlbumTap: (String) -> Unit,
  modifier: Modifier
) {
  Row(modifier = modifier.fillMaxWidth()) {
    TopAlbum(
      album = leftItem, imageSize = imageSize, onAlbumTap = {
        onAlbumTap(leftItem.url)
      },
      modifier = Modifier.weight(1F, fill = false)
    )
    rightItem?.let {
      TopAlbum(
        album = it, imageSize = imageSize, onAlbumTap = {
          onAlbumTap(it.url)
        },
        modifier = Modifier.weight(1F, fill = false)
      )
    }
  }
}
