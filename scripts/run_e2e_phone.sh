#!/usr/bin/env bash
#
# Run :app:pixel6Api35DebugAndroidTest on the phone GMD. TestScreenshotRule
# publishes a `*-failed.png` via PlatformTestStorage on test failure, which
# AGP 9.x pulls back to:
#
#   app/build/intermediates/managed_device_android_test_additional_output/
#     debugAndroidTest/pixel6Api35DebugAndroidTest/<TestClass>-<method>-failed.png
#
# Usage:
#   bash scripts/run_e2e_phone.sh [extra ./gradlew args]
#
# CI passes `-Pandroid.testoptions.manageddevices.emulator.gpu=swiftshader_indirect`
# for headless software rendering.

set -euo pipefail

REPO_ROOT="$(cd "$(dirname "${BASH_SOURCE[0]}")/.." && pwd)"
cd "$REPO_ROOT"

OUT_DIR="app/build/intermediates/managed_device_android_test_additional_output/debugAndroidTest/pixel6Api35DebugAndroidTest"

./gradlew :app:cleanManagedDevices --unused-only

set +e
./gradlew :app:pixel6Api35DebugAndroidTest "$@"
RESULT=$?
set -e

echo
if [ -d "$OUT_DIR" ]; then
  echo "Test artifacts under $OUT_DIR:"
  find "$OUT_DIR" -type f -name '*.png' -print | sed 's/^/  /'
else
  echo "No additional_test_output directory was produced."
fi

exit $RESULT
