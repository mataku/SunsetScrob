release_notes:
	$(eval VERSION=$(shell grep 'versionCode' app/build.gradle.kts | awk -F "= " '{print $$2}'))
	cp fastlane/metadata/android/en-US/changelogs/97.txt fastlane/metadata/android/en-US/changelogs/$(VERSION).txt
	cp fastlane/metadata/android/ja-JP/changelogs/97.txt fastlane/metadata/android/ja-JP/changelogs/$(VERSION).txt