#!/usr/bin/env bash
set -euo pipefail

cd "$(dirname "$0")/.."

readonly RELEASE_NOTE="Update dependencies"

version_name=$(grep 'appVersionName =' build-logic/convention/src/main/java/ApplicationConventionPlugin.kt \
  | awk -F'= ' '{print $2}' | tr -d '"' | tr -d '[:space:]')

IFS='.' read -r major minor patch <<< "$version_name"
version_code=$(( major * 1000000 + minor * 1000 + patch ))

for locale in en-US ja-JP; do
  printf '%s\n' "$RELEASE_NOTE" > "fastlane/metadata/android/${locale}/changelogs/${version_code}.txt"
done

echo "Prepared release notes for ${version_name} (${version_code})"
