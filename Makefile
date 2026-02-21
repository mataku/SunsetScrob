.PHONY: generate_compose_reports prepare_release_notes

generate_compose_reports:
	./gradlew assembleDebug -PcomposeCompilerReports=true

prepare_release_notes:
	bundle exec fastlane android prepare_release_notes
