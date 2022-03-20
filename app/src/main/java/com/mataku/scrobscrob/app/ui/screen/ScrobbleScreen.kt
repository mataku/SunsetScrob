package com.mataku.scrobscrob.app.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.mataku.scrobscrob.app.ui.molecule.ScrobbleTopBar
import com.mataku.scrobscrob.app.ui.molecule.ScrobbleView
import com.mataku.scrobscrob.app.ui.viewmodel.ScrobbleViewModel
import com.mataku.scrobscrob.core.api.endpoint.RecentTrack
import com.mataku.scrobscrob.ui_common.style.Colors

@Composable
fun ScrobbleScreen(
    navController: NavController,
    viewModel: ScrobbleViewModel
) {
    val uiState = viewModel.uiState
    Scaffold(
        topBar = {
            ScrobbleTopBar(navController = navController)
        },
        bottomBar = {
            
        }
    ) {
        ScrobbleContent(
            recentTracks = uiState.recentTracks,
            hasNext = uiState.hasNext,
            onUrlTap = {},
            onScrollEnd = {}
        )
    }
}

@Composable
private fun ScrobbleContent(
    recentTracks: List<RecentTrack>,
    hasNext: Boolean,
    onUrlTap: (String) -> Unit,
    onScrollEnd: () -> Unit
) {
    LazyColumn(
        content = {
            items(recentTracks) {
                ScrobbleView(recentTrack = it)
            }
        },
        modifier = Modifier
            .background(Colors.ContentBackground)
            .fillMaxSize()
    )
}