package com.mataku.scrobscrob.account.ui

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Check
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.mataku.scrobscrob.account.R
import com.mataku.scrobscrob.account.ui.state.ThemeSelectorState
import com.mataku.scrobscrob.core.entity.AppTheme
import com.mataku.scrobscrob.ui_common.SunsetTextStyle
import com.mataku.scrobscrob.ui_common.organism.ContentHeader

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ThemeSelectorScreen(
    state: ThemeSelectorState
) {
    val uiState = state.uiState
    uiState.theme?.let { currentTheme ->
        LazyColumn(content = {
            stickyHeader {
                ContentHeader(text = stringResource(id = R.string.title_theme_selector))
            }
            items(AppTheme.values()) {
                ThemeCell(theme = it, selected = it == currentTheme, onTapTheme = { currentTheme ->
                    state.changeTheme(currentTheme)
                })
            }
        })
    }
}

@Composable
private fun ThemeCell(
    theme: AppTheme,
    selected: Boolean,
    onTapTheme: (AppTheme) -> Unit
) {
    Row(modifier = Modifier
        .fillMaxWidth()
        .clickable {
            onTapTheme.invoke(theme)
        }
        .padding(16.dp)
    ) {
        Text(
            text = theme.displayName,
            style = SunsetTextStyle.body1,
            modifier = Modifier.weight(1F)
        )

        if (selected) {
            Image(
                imageVector = Icons.Outlined.Check,
                contentDescription = "selected theme",
                colorFilter = ColorFilter.tint(
                    color = MaterialTheme.colors.onSurface
                )
            )
        }
    }
}