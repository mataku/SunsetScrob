package com.mataku.scrobscrob.app.testing

import com.mataku.scrobscrob.account.generated.resources.item_logout
import com.mataku.scrobscrob.account.generated.resources.item_privacy_policy
import com.mataku.scrobscrob.auth.generated.resources.web_auth_sign_in
import kotlinx.coroutines.runBlocking
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.getString
import com.mataku.scrobscrob.account.generated.resources.Res as AccountRes
import com.mataku.scrobscrob.auth.generated.resources.Res as AuthRes

// Anchors that come from translated Compose Resources. Resolving them through the same resources
// the app renders keeps the suite passing on a device whose locale is not English.
internal fun signInLabel(): String = stringOf(AuthRes.string.web_auth_sign_in)

internal fun logoutLabel(): String = stringOf(AccountRes.string.item_logout)

internal fun privacyPolicyLabel(): String = stringOf(AccountRes.string.item_privacy_policy)

private fun stringOf(resource: StringResource): String = runBlocking { getString(resource) }
