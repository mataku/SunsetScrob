if github.branch_for_base == 'master'
  fail '`whatsnew` is not committed!' if !git.modified_files.include?('app/src/main/play/ja-JP/whatsnew')
end

android_lint.gradle_task = "lintDebug"
android_lint.report_file = "app/build/reports/lint-results-debug.xml"
android_lint.filtering = true
android_lint.lint(inline_mode: true)
