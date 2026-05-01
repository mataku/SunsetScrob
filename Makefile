.PHONY: generate_compose_reports prepare_release_notes

generate_compose_reports:
	./gradlew assembleDebug -PcomposeCompilerReports=true

regenerate_golden_images:
	./gradlew recordRoborazziDebug -q --no-configuration-cache -PonlyScreenshotTest=true

prepare_release_notes:
	bash scripts/prepare_release_notes.sh
