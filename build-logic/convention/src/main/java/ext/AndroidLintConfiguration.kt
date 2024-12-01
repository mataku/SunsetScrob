package ext

import com.android.build.api.dsl.CommonExtension

fun CommonExtension<*, *, *, *, *, *>.androidLintConfiguration() {
  lint {
    abortOnError = false
    textReport = true
    xmlReport = false
    disable.add("GradleDependency")
    disable.add("ObsoleteLintCustomCheck")
  }
}
