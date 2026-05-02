#!/usr/bin/env bash
#
# Run :app:pixelTabletApi35DebugAndroidTest. TestScreenshotRule publishes
# PNG screenshots via PlatformTestStorage, which AGP 9.x pulls back to:
#
#   app/build/intermediates/managed_device_android_test_additional_output/
#     debugAndroidTest/pixelTabletApi35DebugAndroidTest/<TestClass>-<method>-<label>.png
#
# (AGP < 9 used app/build/outputs/managed_device_android_test_additional_output/
#  — the path moved in 9.x.)
#
# Usage:
#   bash scripts/run_e2e_tablet.sh [extra ./gradlew args]

set -euo pipefail

REPO_ROOT="$(cd "$(dirname "${BASH_SOURCE[0]}")/.." && pwd)"
cd "$REPO_ROOT"

OUT_DIR="app/build/intermediates/managed_device_android_test_additional_output/debugAndroidTest/pixelTabletApi35DebugAndroidTest"

./gradlew :app:cleanManagedDevices --unused-only

set +e
./gradlew :app:pixelTabletApi35DebugAndroidTest \
  -PincludeLargeScreenE2E=true \
  "$@"
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
