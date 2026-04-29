#!/usr/bin/env bash
set -euo pipefail

cd "$(dirname "$0")/.."

OUTPUT_DIR="app/build/outputs/e2e-recordings"
OUTPUT_FILE="$OUTPUT_DIR/e2e.mp4"
DEVICE_FILE="/sdcard/e2e.mp4"
GRADLE_TASK="${1:-:app:connectedDebugAndroidTest}"

mkdir -p "$OUTPUT_DIR"

adb shell screenrecord \
  --bit-rate 800000 \
  --size 540x960 \
  --time-limit 180 \
  "$DEVICE_FILE" &
RECORD_PID=$!

set +e
./gradlew "$GRADLE_TASK" -PforAndroidTest=true
RESULT=$?
set -e

adb shell pkill -SIGINT screenrecord 2>/dev/null || true
wait "$RECORD_PID" 2>/dev/null || true

adb pull "$DEVICE_FILE" "$OUTPUT_FILE" >/dev/null 2>&1 || true
adb shell rm -f "$DEVICE_FILE" 2>/dev/null || true

echo "Recording saved to: $OUTPUT_FILE"
exit $RESULT
