# Rückschild LED

A lightweight Android LED-style scrolling sign for old tablets, designed for a tablet mounted on a backpack or bag.

**Developer / Entwickler:** Amir Mobasher  
**GitHub:** https://github.com/Amirmobash  
**LinkedIn:** https://www.linkedin.com/in/amirmobasher/  
**Book / Buch:** https://www.amazon.sg/Mein-Computer-lernt-von-Machine-Learning-Projekt/dp/3695757825  
**Repository:** https://github.com/Amirmobash/rueckschild_led

## Features

- Android 4.0 / API 14 and newer
- Black fullscreen background with bright pixel-style scrolling text
- Orange or red text
- Large blinking red left arrow
- Adjustable speed, text size, and pixel size
- Optional automatic uppercase
- Optional maximum brightness
- 90° and 180° rotation; both may be combined
- Settings saved automatically
- Screen kept awake while the sign is active
- Long-press the sign for about 1.5 seconds, or press Back, to return to settings
- German-language UI
- No Internet permission
- No AndroidX and no third-party runtime dependencies
- About/developer screen with external GitHub, LinkedIn, and book links

## Project structure

```text
app/src/main/java/com/example/rueckschildled/
  MainActivity.java        settings UI
  SignActivity.java        fullscreen host
  LedSignView.java         pixel renderer + scrolling animation
  SignPreferences.java     SharedPreferences persistence
  SignSettings.java        immutable settings snapshot
  AboutActivity.java       developer/about screen
```

## Build on Windows

Requirements:

- Windows 10/11
- Android Studio or Android SDK
- Android SDK Platform 34
- Internet connection for the first command-line build

Run:

```bat
BUILD_WITH_JDK17_V6.bat
```

The script downloads a portable Eclipse Temurin JDK 17 and Gradle 8.7 into `.tools` when missing, then builds:

```text
Rueckschild-LED.apk
```

## Build on Linux/macOS

With JDK 17, Gradle 8.7, and Android SDK Platform 34 installed:

```bash
./build_apk.sh
```

## Install by ADB on Windows

After building:

```bat
install_apk.bat
```

## Technical details

- Java
- `minSdk 14`
- `targetSdk 28`
- `compileSdk 34`
- Android Gradle Plugin 8.5.2
- Gradle 8.7
- Application ID: `com.example.rueckschildled`

The LED effect is generated programmatically. Each frame is rendered to a smaller bitmap and scaled to the physical screen with filtering disabled, so the blocks remain sharp.

## Refactor notes

See `docs/REFACTORING_REPORT.md` for what was reconstructed and what could/could not be verified. The reusable senior-refactor prompt is in `docs/SENIOR_REFACTOR_PROMPT.md`.

## Safety

Mount the tablet securely and use the display only where it is safe and legal. Do not let the screen or mounting setup obstruct movement or visibility.

## License

No license file is included. Choose and add a license before third-party redistribution or accepting outside contributions if you want explicit reuse terms.
