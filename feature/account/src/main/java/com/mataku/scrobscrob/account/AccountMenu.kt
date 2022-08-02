package com.mataku.scrobscrob.account

import androidx.annotation.StringRes

enum class AccountMenu(
  @StringRes val titleRes: Int,
  @StringRes val descriptionRes: Int
) {
  THEME(
    titleRes = R.string.item_theme,
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
  );
}
