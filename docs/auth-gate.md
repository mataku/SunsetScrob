# Auth Gate

How the app switches between the login flow and the authenticated flow,
and why `LoginScreen` does **not** drive that transition itself.

## Summary

`LoginScreen` only persists the username (a side effect on
`UsernameDataStore`). The screen transition happens because
`SunsetMainScreen` observes `MainViewModel.state.username` as a Flow and
flips its `if (isAuthenticated)` branch in response.

```kotlin
// app/.../SunsetMainScreen.kt
if (isAuthenticated) {
  SunsetTabHost { homeGraph(); albumGraph(); ... }   // BackStack A
} else {
  SunsetNavHost { authGraph(); commonGraph(); ... }  // BackStack B
}
```

## Why `LoginScreen` cannot navigate directly

`LoginScreen` and `HomeScreen` live under different `SunsetNavBackStack`s.
Each side of the `if` builds its own `SunsetNavHost`, so from
`LoginScreen`:

- `navigate(HomeKey)` would throw at runtime — `HomeKey` is not
  registered in the unauthenticated `SunsetNavHost`.
- `popBackStack()` is meaningless — `LoginKey` is the only entry in
  that stack.

There is no in-stack way for `LoginScreen` to complete the transition.
The signal must reach the parent (`SunsetMainScreen`), which owns the
`if` branch.

## Reactive Flow vs. one-shot callback — what we picked and why

**(a) Reactive Flow (chosen)**

1. `LoginViewModel` writes to `UsernameDataStore` on success.
2. `MainViewModel.state` collects `UsernameRepository.usernameFlow()`
   and re-emits.
3. `SunsetMainScreen`'s `if (isAuthenticated)` flips.

Pros:
- Login, logout, and session expiry all flow through the same path —
  symmetric and self-correcting.
- Treats auth as global app state with a single source of truth
  (`DataStore`); UI is just an observer. Matches the Compose / Nav 3
  recommended pattern.
- `LoginScreen` does not need to know the post-login destination
  (`HomeKey`) — keeps modules loosely coupled.

Cons:
- A cold or one-shot upstream Flow breaks the chain silently
  (see "Known footgun" below).
- The two `if` branches build separate composition trees, so a
  cross-branch shared-element animation would need extra plumbing.

**(b) One-shot callback (rejected)**

Add `LoginScreen(onLoginSuccess: () -> Unit)`. `SunsetMainScreen` keeps
a `var hasJustLoggedIn` and the `if` becomes
`if (isAuthenticated || hasJustLoggedIn)`.

Rejected because logout, session expiry, and process-death restoration
**still need the reactive path**. Keeping both means the same flip
fires twice, with no guaranteed ordering, for no real benefit. The
"explicit trigger is easier to follow" argument is already covered by
treating "`LoginViewModel` writes the username" as the visible
side-effect — readers can trace the cause from there.

**(c) Single back stack with `LoginKey` and `HomeKey` together
(rejected)**

Would let `LoginScreen` call `replaceTop(HomeKey)` directly, but
collapses `SunsetTabHost`'s per-tab independent back stacks. We need
the per-tab history preservation, so we keep the split.

## Known footgun: cold Flow leak

The reactive auth gate relies on the observed Flow being **hot /
continuously subscribed**. During the Nav 3 migration this was briefly
broken because `UsernameRepository.asyncUsername()` was a one-shot
flow:

```kotlin
// before — emits once, then completes
fun asyncUsername(): Flow<String?> = flow {
  emit(usernameDataStore.username())
}
```

Reading the initial value with `.first()` worked, but updates from
`UsernameDataStore.setUsername(...)` never reached
`MainViewModel.state`, so `state.username` stayed stuck on its initial
value. The Nav 2 code did not surface this bug because `LoginScreen`
called `navigateToHomeFromAuth` explicitly.

Fix:

```kotlin
// added — hot subscription on the DataStore Flow
fun usernameFlow(): Flow<String?> =
  context.dataStore.data.map { it[USERNAME_KEY] }
```

`MainViewModel.init` was rewritten to
`combine(currentTheme(), usernameFlow())` so any username write
re-emits the state (commit `f1b5c8df`).

## Open follow-up

`TopAlbumsViewModel`, `TopArtistsViewModel`, and `AccountViewModel`
still read the username via the cold `asyncUsername()` in field
initializers. That is fine for one-shot startup reads, but if any of
those screens needs to react to logout in-place (e.g. clear content
without restarting the activity), it should switch to `usernameFlow()`.
