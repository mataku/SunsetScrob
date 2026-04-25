package ext

import com.android.build.api.dsl.CommonExtension

fun CommonExtension.androidLintConfiguration() {
  // Fail the build on lint errors only in CI; locally we keep the build green
  // and surface issues via reports / IDE highlights.
  lint.abortOnError = System.getenv("CI") == "true"
  lint.textReport = true
  // Emit XML reports as well so CI can aggregate failures across modules
  // without relying on the human-readable text format.
  lint.xmlReport = true
  // Lint test sources rarely add value (e.g. constructing a ViewModel directly in
  // a test composable is intentional, not a bug), skip them entirely.
  lint.ignoreTestSources = true
  lint.disable.add("GradleDependency")
  lint.disable.add("ObsoleteLintCustomCheck")
  // MainActivity is provided via Metro's AppComponentFactory binding; the
  // default "no-arg constructor" expectation does not apply.
  lint.disable.add("Instantiatable")
}
