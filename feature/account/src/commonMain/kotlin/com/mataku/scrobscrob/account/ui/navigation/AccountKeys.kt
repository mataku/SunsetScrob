package com.mataku.scrobscrob.account.ui.navigation

import androidx.compose.runtime.Immutable
import com.mataku.scrobscrob.ui_common.navigation.SunsetNavKey
import kotlinx.serialization.Serializable

@Immutable @Serializable data object AccountKey : SunsetNavKey
@Immutable @Serializable data object ScrobbleSettingKey : SunsetNavKey
@Immutable @Serializable data object ThemeSelectorKey : SunsetNavKey
@Immutable @Serializable data object LicenseKey : SunsetNavKey
