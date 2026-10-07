#!/usr/bin/env bash
set -euo pipefail
cd "$(dirname "$0")"

: "${ANDROID_SDK_ROOT:=${ANDROID_HOME:-}}"
if [[ -z "${ANDROID_SDK_ROOT}" ]]; then
  echo "Android SDK not found. Set ANDROID_SDK_ROOT or ANDROID_HOME." >&2
  exit 1
fi

if [[ ! -d "${ANDROID_SDK_ROOT}/platforms/android-34" ]]; then
  echo "Android SDK Platform 34 is missing." >&2
  exit 1
fi

if command -v gradle >/dev/null 2>&1; then
  GRADLE=gradle
elif [[ -x ".tools/gradle-8.7/bin/gradle" ]]; then
  GRADLE=.tools/gradle-8.7/bin/gradle
else
  echo "Gradle 8.7 not found. Install Gradle 8.7 or run the Windows bootstrap script on Windows." >&2
  exit 1
fi

printf 'sdk.dir=%s\n' "${ANDROID_SDK_ROOT//\\/\\\\}" > local.properties
"$GRADLE" --no-daemon clean assembleDebug
cp -f app/build/outputs/apk/debug/app-debug.apk Rueckschild-LED.apk
echo "Built: $(pwd)/Rueckschild-LED.apk"
