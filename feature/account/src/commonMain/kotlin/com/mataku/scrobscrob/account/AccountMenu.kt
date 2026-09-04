package com.mataku.scrobscrob.account

import com.mataku.scrobscrob.account.generated.resources.Res
import com.mataku.scrobscrob.account.generated.resources.description_scrobble
import com.mataku.scrobscrob.account.generated.resources.item_app_version
import com.mataku.scrobscrob.account.generated.resources.item_clear_cache
import com.mataku.scrobscrob.account.generated.resources.item_license
import com.mataku.scrobscrob.account.generated.resources.item_logout
import com.mataku.scrobscrob.account.generated.resources.item_logout_description
import com.mataku.scrobscrob.account.generated.resources.item_privacy_policy
import com.mataku.scrobscrob.account.generated.resources.item_theme
import com.mataku.scrobscrob.account.generated.resources.title_scrobble
import org.jetbrains.compose.resources.StringResource

enum class AccountMenu(
  val titleRes: StringResource,
  val descriptionRes: StringResource?
) {
  SCROBBLE(
    titleRes = Res.string.title_scrobble,
    descriptionRes = Res.string.description_scrobble
  ),
  THEME(
    titleRes = Res.string.item_theme,
    descriptionRes = null
  ),
  CLEAR_CACHE(
    titleRes = Res.string.item_clear_cache,
    descriptionRes = null
  ),
  LOGOUT(
    titleRes = Res.string.item_logout,
    descriptionRes = Res.string.item_logout_description
  ),
  LICENSE(
    titleRes = Res.string.item_license,
    descriptionRes = null
  ),
  PRIVACY_POLICY(
    titleRes = Res.string.item_privacy_policy,
    descriptionRes = null
  ),
  APP_VERSION(
    titleRes = Res.string.item_app_version,
    descriptionRes = null
  );
}
