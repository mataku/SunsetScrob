release_notes:
	$(eval VERSION=$(shell grep 'versionCode' build-logic/convention/src/main/java/ApplicationConventionPlugin.kt | awk -F "= " '{print $$2}'))
	cp fastlane/metadata/android/en-US/changelogs/97.txt fastlane/metadata/android/en-US/changelogs/$(VERSION).txt
	cp fastlane/metadata/android/ja-JP/changelogs/97.txt fastlane/metadata/android/ja-JP/changelogs/$(VERSION).txt

generate_compose_reports:
	./gradlew assembleDebug -PcomposeCompilerReports=true
