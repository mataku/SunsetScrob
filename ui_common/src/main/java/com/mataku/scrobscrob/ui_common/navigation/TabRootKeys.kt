package com.mataku.scrobscrob.ui_common.navigation

import androidx.compose.runtime.Immutable
import kotlinx.serialization.Serializable

@Immutable
@Serializable
data object HomeRootKey : SunsetNavKey

@Immutable
@Serializable
data object DiscoverRootKey : SunsetNavKey

@Immutable
@Serializable
data object AccountRootKey : SunsetNavKey
