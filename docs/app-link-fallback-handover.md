# Handover: recovering from App Link verification failure

## Background

While testing web login on the 1.30.0 internal build, the Chrome Custom Tab stayed on `https://sunsetscrob.mataku.com/auth/lastfm?token=…` instead of handing the callback back to `MainActivity`.

Two separate causes were found and both are now resolved on the test device:

1. `assetlinks.json` did not list the Play app signing key (`BE:6B:E8:…`) at the time the app was installed, and Google's Digital Asset Links cache held the stale file for 24 hours (the file is served with `Cache-Control: max-age=86400`).
2. Once the server side was correct (`assetlinks:check` returns `"linked": true`), the test device was offline, so the GMS verification agent failed with `AppLinksAsyncVerifierV2: Check request failed: null` and **recorded that failure as a persistent error state** (`pm get-app-links` showed `1024` instead of `verified`). `pm verify-app-links --re-verify` alone does not clear a recorded error; `pm reset-app-links` followed by `verify-app-links --re-verify` was required.

Cause 2 is the reason this handover exists. Domain verification runs asynchronously after install and is effectively one-shot: a transient network failure, data saver, or battery restrictions at that moment leaves the app permanently unverified, and the user has no `adb`. In that state the App Link never fires, the Custom Tab stays on the callback page, and there is currently no way for the user to recover or to complete sign-in.

## Already done (web side, `sunsetscrob.mataku.com` repo)

The callback page now renders an `intent://` button when a well-formed token is present:

```
intent://sunsetscrob.mataku.com/auth/lastfm?token=<TOKEN>#Intent;scheme=https;package=com.mataku.scrobscrob;end
```

`package=` is pinned, so no other app can intercept it, and it works regardless of the domain verification state. The token is only echoed when it matches `^[A-Za-z0-9_-]{1,128}$`.

Known limitation: the button targets `com.mataku.scrobscrob` only, so it will not open a `com.mataku.scrobscrob.dev` build.

## What needs doing (this repo)

Give the user an in-app path out of the unverified state, so that sign-in is not a dead end when the App Link does not fire.

- Detect, before or around the sign-in flow, whether `sunsetscrob.mataku.com` is actually verified for this package on this device.
- When it is not verified, surface that to the user and route them to the system screen where they can enable link handling manually, rather than letting them tap "Sign in with Last.fm" and get stranded on the callback page.
- Consider using the "the Custom Tab closed without a token ever arriving" signal as the trigger for that guidance. Today `LoginViewModel.onWebAuthResult` maps `LastFmWebAuthResult.Closed` to `Unit`, so the user silently returns to the login screen with no explanation and no next step.

Constraint to confirm first: `AndroidSdkConfiguration.kt` sets `minSdk = 30`, while `dumpsys package` reports `minSdk=32` for the installed 1.30.0 APK. The relevant platform APIs for reading verification state landed in API 31, so establish which minimum actually applies before designing around them.

## Reproducing and verifying

Put the device in the broken state:

```
adb shell svc wifi disable && adb shell svc data disable
adb shell pm reset-app-links com.mataku.scrobscrob
adb shell pm verify-app-links --re-verify com.mataku.scrobscrob
adb shell pm get-app-links com.mataku.scrobscrob   # sunsetscrob.mataku.com: 1024
```

Restore it:

```
adb shell svc wifi enable
adb shell pm reset-app-links com.mataku.scrobscrob
adb shell pm verify-app-links --re-verify com.mataku.scrobscrob
adb shell pm get-app-links com.mataku.scrobscrob   # sunsetscrob.mataku.com: verified
```

Check that the callback intent reaches the app:

```
adb shell am start -a android.intent.action.VIEW -c android.intent.category.BROWSABLE \
  -d 'https://sunsetscrob.mataku.com/auth/lastfm?token=probe123'
```

Watch the verification agent:

```
adb logcat -c && adb shell pm verify-app-links --re-verify com.mataku.scrobscrob
adb logcat -d | grep -iE 'applink|domainverif|IntentFilterVer'
```

## Other risks noted during the investigation, not yet addressed

- `WebAuthCustomTabs.intent()` does not call `setPackage`, so which browser opens the Custom Tab is left to device settings. Browsers other than Chrome handle App Link handoff differently, and a browser without Custom Tabs support falls back to a full-screen VIEW intent.
- The manifest uses `android:path="/auth/lastfm"` (exact match) and `LastFmWebAuth.tokenFromCallback` rejects anything whose path differs. A trailing slash introduced by Last.fm would break both at once.
- `assetlinks.json` lists both `com.mataku.scrobscrob` and `com.mataku.scrobscrob.dev` for the same host and path. On a device with both installed, which app receives the callback is ambiguous.
