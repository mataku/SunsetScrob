package ext

import com.android.build.api.dsl.CommonExtension

fun CommonExtension.androidLintConfiguration() {
  // Fail the build on lint errors only in CI; locally we keep the build green
  // and surface issues via reports / IDE highlights.
  lint.abortOnError = System.getenv("CI") == "true"
  lint.textReport = true
  lint.xmlReport = false
  lint.disable.add("GradleDependency")
  lint.disable.add("ObsoleteLintCustomCheck")
  // MainActivity is provided via Metro's AppComponentFactory binding; the
  // default "no-arg constructor" expectation does not apply.
  lint.disable.add("Instantiatable")
}
