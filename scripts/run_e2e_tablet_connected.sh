#!/usr/bin/env bash
#
# Run :app:connectedDebugAndroidTest against a manually-started Pixel
# Tablet emulator (or any connected device wide enough for the
# `@LargeScreenE2E` flow). TestScreenshotRule publishes a `*-failed.png`
# via PlatformTestStorage on test failure, which Gradle pulls back to:
#
#   app/build/outputs/connected_android_test_additional_output/<flavor>/
#     connected/<device>/<TestClass>-<method>-failed.png
#
# Use this when you want a tablet emulator you can watch live (eg. via
# Android Studio's emulator window). For headless GMD use
# scripts/run_e2e_tablet.sh instead.
#
# Usage:
#   bash scripts/run_e2e_tablet_connected.sh [extra ./gradlew args]

set -euo pipefail

REPO_ROOT="$(cd "$(dirname "${BASH_SOURCE[0]}")/.." && pwd)"
cd "$REPO_ROOT"

OUT_DIR="app/build/outputs/connected_android_test_additional_output"

set +e
./gradlew :app:connectedDebugAndroidTest \
  -PincludeLargeScreenE2E=true \
  "$@"
RESULT=$?
set -e

echo
if [ -d "$OUT_DIR" ]; then
  echo "Test artifacts under $OUT_DIR:"
  find "$OUT_DIR" -type f -name '*.png' -print | sed 's/^/  /'
else
  echo "No connected_android_test_additional_output directory was produced."
fi

exit $RESULT
