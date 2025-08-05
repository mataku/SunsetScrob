package com.mataku.scrobscrob.account

import androidx.annotation.StringRes

enum class AccountMenu(
  @param:StringRes val titleRes: Int,
  @param:StringRes val descriptionRes: Int
) {
  SCROBBLE(
    titleRes = R.string.title_scrobble,
    descriptionRes = R.string.description_scrobble
  ),
  THEME(
    titleRes = R.string.item_theme,
    descriptionRes = 0
  ),
  CLEAR_CACHE(
    titleRes = R.string.item_clear_cache,
    descriptionRes = 0
  ),
  LOGOUT(
    titleRes = R.string.item_logout,
    descriptionRes = R.string.item_logout_description
  ),
  LICENSE(
    titleRes = R.string.item_license,
    descriptionRes = 0
  ),
  PRIVACY_POLICY(
    titleRes = R.string.item_privacy_policy,
    descriptionRes = 0
  ),
  APP_VERSION(
    titleRes = R.string.item_app_version,
    descriptionRes = 0
  );
}
