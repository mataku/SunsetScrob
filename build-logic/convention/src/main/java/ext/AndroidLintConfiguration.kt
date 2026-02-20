package ext

import com.android.build.api.dsl.CommonExtension

fun CommonExtension.androidLintConfiguration() {
  lint.abortOnError = false
  lint.textReport = true
  lint.xmlReport = false
  lint.disable.add("GradleDependency")
  lint.disable.add("ObsoleteLintCustomCheck")
}
