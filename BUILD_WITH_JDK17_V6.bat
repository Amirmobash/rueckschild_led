#!/usr/bin/env python3
"""Apply small, reviewable UX and robustness fixes to Rückschild LED.

Usage: python3 apply_humanize.py /path/to/rueckschild_led
       python3 apply_humanize.py /path/to/rueckschild_led --check

No external dependencies. Existing files are backed up as *.before-humanize.
"""
from pathlib import Path
import argparse
import sys

APP = 'app/src/main/java/com/example/rueckschildled/'

CHANGES = {
    APP + 'MainActivity.java': [
        (
            '''        messageInput.setHint(R.string.message_hint);
        messageInput.setTextColor(Color.WHITE);''',
            '''        messageInput.setHint(R.string.message_hint);
        // Give TalkBack a meaningful name even on older Android releases.
        messageInput.setContentDescription(getString(R.string.message_label));
        messageInput.setTextColor(Color.WHITE);'''
        ),
        (
            '''        Button startButton = new Button(this);
        startButton.setText(R.string.start_button);
        startButton.setTextSize(18);
        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signPreferences.save(readForm());
                startActivity(new Intent(MainActivity.this, SignActivity.class));
            }
        });''',
            '''        Button startButton = new Button(this);
        startButton.setText(R.string.start_button);
        startButton.setTextSize(18);
        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SignSettings settings = readForm();
                if (settings.getMessage().length() == 0) {
                    messageInput.setError(getString(R.string.message_required));
                    messageInput.requestFocus();
                    return;
                }
                signPreferences.save(settings);
                startActivity(new Intent(MainActivity.this, SignActivity.class));
            }
        });'''
        ),
        (
            '''                return Math.round(60f + (progress / 100f) * 440f) + " px/s";''',
            '''                if (progress < 34) {
                    return getString(R.string.speed_slow);
                }
                if (progress < 68) {
                    return getString(R.string.speed_normal);
                }
                return getString(R.string.speed_fast);'''
        ),
    ],
    APP + 'SignSettings.java': [
        (
            '''        return value.trim();''',
            '''        // The editor accepts multiple lines, but the LED sign scrolls one line.
        // Collapse pasted line breaks, tabs and repeated spaces to keep text readable.
        return value.trim().replaceAll("\\\\s+", " ");'''
        ),
    ],
    'app/src/main/res/values/strings.xml': [
        ('<string name="settings_subtitle">Pixel-Laufschrift für ein altes Android-Tablet</string>',
         '<string name="settings_subtitle">Mach dein Tablet zu einer leuchtenden Nachrichtentafel.</string>'),
        ('<string name="settings_hint">Zurück-Taste oder 1,5 Sekunden auf die Anzeige drücken = Einstellungen</string>',
         '<string name="settings_hint">Zurück zu den Einstellungen? Tippe auf „Zurück“ oder halte die Anzeige 1,5 Sekunden gedrückt.</string>'),
        ('<string name="message_label">Text</string>',
         '<string name="message_label">Was möchtest du anzeigen?</string>'),
        ('<string name="message_hint">z. B. ARBEIT RUFT! BITTE LINKS VORBEI</string>',
         '<string name="message_hint">z. B. Bitte links vorbeigehen – danke!</string>'),
        ('<string name="default_message">ARBEIT RUFT! BITTE LINKS VORBEI</string>',
         '<string name="default_message">BITTE LINKS VORBEI – DANKE!</string>'),
        ('<string name="text_color_label">Textfarbe</string>',
         '<string name="text_color_label">Welche Farbe soll leuchten?</string>'),
        ('<string name="text_color_orange">Leuchtendes Orange</string>',
         '<string name="text_color_orange">Orange</string>'),
        ('<string name="speed_label">Geschwindigkeit</string>',
         '<string name="speed_label">Wie schnell soll der Text laufen?</string>'),
        ('<string name="text_size_label">Textgröße</string>',
         '<string name="text_size_label">Schriftgröße</string>'),
        ('<string name="pixel_size_label">Pixelgröße</string>',
         '<string name="pixel_size_label">LED-Punktgröße</string>'),
        ('<string name="uppercase_label">Text automatisch in GROSSBUCHSTABEN</string>',
         '<string name="uppercase_label">Alles in Großbuchstaben anzeigen</string>'),
        ('<string name="brightness_label">Maximale Bildschirmhelligkeit</string>',
         '<string name="brightness_label">Bildschirm auf maximale Helligkeit stellen</string>'),
        ('<string name="blink_arrow_label">Riesiger blinkender roter Linkspfeil</string>',
         '<string name="blink_arrow_label">Blinkenden Pfeil nach links anzeigen</string>'),
        ('<string name="landscape_label">Anzeige zusätzlich um 90° drehen (Querformat)</string>',
         '<string name="landscape_label">Anzeige um 90° drehen</string>'),
        ('<string name="rotate_label">Anzeige zusätzlich um 180° drehen</string>',
         '<string name="rotate_label">Zusätzlich um 180° drehen</string>'),
        ('<string name="start_button">ANZEIGE STARTEN</string>',
         '<string name="start_button">Laufschrift starten</string>'),
        ('<string name="about_button">ÜBER / KONTAKT</string>',
         '<string name="about_button">Über die App</string>'),
        ('<string name="about_title">ÜBER DEN ENTWICKLER</string>',
         '<string name="about_title">Über Rückschild LED</string>'),
        ('<string name="about_description">Eine kleine, gut sichtbare LED-Laufschrift für ältere Android-Tablets.</string>',
         '<string name="about_description">Aus einem älteren Android-Tablet wird eine gut lesbare, bewegte Nachrichtentafel – einfach einstellen und loslegen.</string>'),
        ('<string name="about_creator_name">Amir Mobasher</string>',
         '<string name="about_creator_name">Amir Mobasheraghdam</string>'),
        ('<string name="about_back">ZURÜCK</string>',
         '<string name="about_back">Zurück zu den Einstellungen</string>'),
        ('<string name="link_error">Link konnte auf diesem Gerät nicht geöffnet werden.</string>',
         '<string name="link_error">Leider konnte ich den Link auf diesem Gerät nicht öffnen.</string>'),
        (
            '    <string name="text_color_red">Rot</string>',
            '''    <string name="text_color_red">Rot</string>
    <string name="message_required">Bitte gib zuerst eine Nachricht ein.</string>
    <string name="speed_slow">Langsam</string>
    <string name="speed_normal">Mittel</string>
    <string name="speed_fast">Schnell</string>'''
        ),
    ],
}

README = '''# Rückschild LED

**A bright, easy-to-read scrolling sign for an older Android tablet.** Mount a tablet securely on a bag or backpack, choose a message, and let the screen do the talking.

Created by **Amir Mobasheraghdam** · [GitHub](https://github.com/Amirmobash) · [LinkedIn](https://www.linkedin.com/in/amirmobasher/)

## Get started

1. Install the Android app on a tablet running **Android 4.0 (API 14) or newer**.
2. Open **Rückschild LED** and type the message you want people to see.
3. Choose orange or red, adjust the scrolling speed, and set your preferred text and LED-pixel sizes.
4. Tap **Laufschrift starten** to show the full-screen sign.
5. To change anything, press **Back** or hold a finger on the sign for **1.5 seconds**.

**Tip:** Short, friendly messages are usually easiest to read while moving. Line breaks and extra spaces in pasted text are automatically combined into one scrolling line. If the message is empty, the app asks you to enter one instead of showing a blank screen.

## What it can do

- Bright orange or red LED-style, pixelated text on a black background
- An optional blinking left arrow
- Adjustable speed, text size and LED pixel size
- Optional uppercase text and maximum screen brightness
- 90° and 180° display rotation (also combinable)
- Settings saved automatically; sign stays awake while active
- German-language interface with clearer labels and feedback
- Works offline; **no Internet permission** and no third-party runtime dependencies

## Build the app

This is a Java/Gradle Android project (`minSdk 14`, `targetSdk 28`, `compileSdk 34`, AGP 8.5.2, Gradle 8.7).

**Windows:** Install Android SDK Platform 34 and run `BUILD_WITH_JDK17_V6.bat`. The helper script sets up JDK 17 and Gradle 8.7 if needed.

**Linux / macOS:** Install JDK 17, Gradle 8.7 and Android SDK Platform 34. Set `ANDROID_SDK_ROOT`, then run:

```bash
./build_apk.sh
```

The scripts generate `Rueckschild-LED.apk`. The APK checked into the repository is a prebuilt binary and is **not automatically refreshed** when you change source code. Rebuild before distributing a changed version.

Main Java sources live under `app/src/main/java/com/example/rueckschildled/`: `MainActivity` handles settings; `SignActivity` hosts the fullscreen screen; `LedSignView` renders the animation; `SignSettings` and `SignPreferences` handle configuration; `AboutActivity` shows credits and useful links.

## Notes

Please mount the tablet safely, keep it from blocking anyone's view, and use the sign only where local rules permit.

**License:** No license is currently included; publishing source code is not the same as granting redistribution permissions. Add a suitable license if you want others to reuse the project.
'''


def main():
    parser = argparse.ArgumentParser(description=__doc__)
    parser.add_argument('repository', type=Path, help='Path to your cloned rueckschild_led repository')
    parser.add_argument('--check', action='store_true', help='Verify applicability without making changes')
    args = parser.parse_args()
    root = args.repository.expanduser().resolve()
    if not (root / 'app' / 'build.gradle').is_file():
        parser.error('Not a Rückschild LED project: app/build.gradle is missing.')

    # Validate the entire patch first so there are no half-finished changes.
    updated = {}
    for relative, replacements in CHANGES.items():
        path = root / relative
        if not path.is_file():
            parser.error('Required file is missing: ' + relative)
        content = path.read_text(encoding='utf-8')
        for old, new in replacements:
            if content.count(new) == 1:
                # Already updated; safe to run the script more than once.
                continue
            count = content.count(old)
            if count != 1:
                parser.error(f'{relative}: expected one matching code section; found {count}. '
                             'This patch is intended for the repository main branch reviewed on 2026-10-27.')
            content = content.replace(old, new, 1)
        updated[path] = content

    readme = root / 'README.md'
    if not readme.is_file():
        parser.error('README.md not found.')
    updated[readme] = README
    if args.check:
        print('OK: all changes match the source files. Nothing was changed.')
        return 0

    for path, content in updated.items():
        backup = path.with_name(path.name + '.before-humanize')
        if not backup.exists():
            backup.write_bytes(path.read_bytes())
        path.write_text(content, encoding='utf-8')
        print('Updated:', path.relative_to(root))
    print('\nDone. Review with `git diff` and rebuild the APK. No prebuilt APK was changed.')
    return 0


if __name__ == '__main__':
    sys.exit(main())
