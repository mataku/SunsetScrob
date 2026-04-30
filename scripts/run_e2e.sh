#!/usr/bin/env bash
set -euo pipefail

cd "$(dirname "$0")/.."

OUTPUT_DIR="app/build/outputs/e2e-recordings"
OUTPUT_FILE="$OUTPUT_DIR/e2e.mp4"
GRADLE_TASK="${1:-:app:pixel6Api35DebugAndroidTest}"

mkdir -p "$OUTPUT_DIR"

(
  for _ in $(seq 1 120); do
    if adb devices | grep -qE 'emulator-[0-9]+[[:space:]]+device$'; then
      if [ "$(adb shell getprop sys.boot_completed 2>/dev/null | tr -d '\r')" = "1" ]; then
        break
      fi
    fi
    sleep 2
  done
  adb shell screenrecord --output-format=h264 --bit-rate 800000 --size 540x960 --time-limit 180 - 2>/dev/null \
    | ffmpeg -y -hide_banner -loglevel error -framerate 30 -i - -c:v copy "$OUTPUT_FILE" || true
) &
RECORD_PID=$!

set +e
./gradlew :app:cleanManagedDevices --unused-only
./gradlew "$GRADLE_TASK"
RESULT=$?
set -e

pkill -SIGINT -f "adb shell screenrecord" 2>/dev/null || true
kill -SIGINT "$RECORD_PID" 2>/dev/null || true
wait "$RECORD_PID" 2>/dev/null || true

echo "Recording saved to: $OUTPUT_FILE"
exit $RESULT