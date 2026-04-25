.PHONY: generate_compose_reports prepare_release_notes

generate_compose_reports:
	./gradlew assembleDebug -PcomposeCompilerReports=true

prepare_release_notes:
	bash scripts/prepare_release_notes.sh
