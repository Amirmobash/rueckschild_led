# Rückschild LED

A lightweight Android LED-style scrolling sign for old tablets — designed for a tablet mounted on a backpack or bag.

Eine leichte Android-App im LED-Stil für alte Tablets — gedacht für ein Tablet, das an einem Rucksack oder einer Tasche befestigt wird.

**Developer / Entwickler:** Amir Mobasher  
**GitHub:** https://github.com/Amirmobash  
**LinkedIn:** https://www.linkedin.com/in/amirmobasher/  
**Book / Buch:** https://www.amazon.sg/Mein-Computer-lernt-von-Machine-Learning-Projekt/dp/3695757825  
**Repository:** https://github.com/Amirmobash/rueckschild_led

---

## English

### Overview

Rückschild LED turns an old Android tablet into a high-contrast scrolling message display. The screen stays completely black while bright pixel-style text moves across the display. It is designed to remain easy to read in darkness while using older Android hardware.

Example message:

`WORK CALLS! PLEASE PASS ON MY LEFT`

### Features

- Supports **Android 4.0 / API 14 and newer**
- Full black background for maximum contrast
- Bright pixel-style scrolling text
- Text color selectable between **bright orange** and **red**
- Very large **blinking red left arrow**
- Scrolling speed adjustable up to approximately **5× faster** than the original maximum
- Adjustable text size and pixel size
- Optional automatic uppercase text
- Optional maximum screen brightness
- **Portrait mode** and **90° landscape mode**
- Optional **180° rotation** for upside-down mounting
- 90° and 180° modes can be combined
- Settings are saved automatically
- Screen stays awake while the sign is running
- Full-screen display where supported by the Android device
- German-language app interface
- No Internet permission required by the app
- No AndroidX and no third-party runtime libraries
- Built-in **About / Developer** section with links to GitHub, LinkedIn and the developer's book

### How to use

1. Open the app.
2. Enter the message you want to display.
3. Select orange or red text.
4. Adjust speed, text size and pixel size.
5. Enable the large blinking red left arrow if desired.
6. Select portrait or 90° mode depending on how the tablet is mounted.
7. Tap **ANZEIGE STARTEN**.
8. To return to settings, press the Android Back button or long-press the display for about 1.5 seconds.

### Build the APK on Windows

#### Requirements

- Windows 10 or Windows 11
- Android Studio or Android SDK
- Android SDK Platform 34
- Internet connection for the first build

The supplied build script uses a local **portable Eclipse Temurin JDK 17**, so an installed Java 25 does not interfere with the build.

Run:

```bat
BUILD_WITH_JDK17_V6.bat
```

On the first run, the script downloads the required portable JDK 17 and Gradle 8.7 into the local `.tools` directory.

After a successful build, the installable APK is created as:

```text
Rueckschild-LED.apk
```

### Install on the tablet

You can copy `Rueckschild-LED.apk` to the tablet and open it there. Older Android versions may require enabling installation from **Unknown sources**.

Alternatively, enable USB debugging and install the APK using ADB.

### Technical details

- Language: Java
- `minSdk 14` — Android 4.0
- `targetSdk 28`
- `compileSdk 34`
- Android Gradle Plugin 8.5.2
- Gradle 8.7
- Portable Eclipse Temurin JDK 17 for Windows builds
- No AndroidX
- No third-party runtime dependencies

The pixel effect is generated programmatically: text is rendered into a low-resolution bitmap buffer and then scaled up with filtering disabled, producing sharp, blocky pixels.

### Safety note

Mount the tablet securely and use the display only where it is safe and legal. Do not let the screen or mounting setup obstruct your movement or visibility.

---

## Deutsch

### Übersicht

Rückschild LED verwandelt ein altes Android-Tablet in eine kontrastreiche Laufschrift-Anzeige. Der Bildschirm bleibt vollständig schwarz, während heller Text im Pixel-Stil über das Display läuft. Die App ist speziell dafür ausgelegt, auch im Dunkeln gut lesbar zu sein und auf älterer Android-Hardware zu funktionieren.

Beispieltext:

`ARBEIT RUFT! BITTE LINKS VORBEI`

### Funktionen

- Unterstützt **Android 4.0 / API 14 und neuer**
- Komplett schwarzer Hintergrund für maximalen Kontrast
- Helle Pixel-Laufschrift
- Textfarbe wählbar zwischen **leuchtendem Orange** und **Rot**
- Sehr großer **blinkender roter Linkspfeil**
- Geschwindigkeit bis ungefähr **5× schneller** als das ursprüngliche Maximum
- Textgröße und Pixelgröße einstellbar
- Automatische GROSSBUCHSTABEN optional
- Maximale Bildschirmhelligkeit optional
- **Hochformat** und **90°-Querformat**
- Optionale **180°-Drehung** für eine umgekehrte Montage
- 90°- und 180°-Modus können kombiniert werden
- Einstellungen werden automatisch gespeichert
- Bildschirm bleibt während der Anzeige eingeschaltet
- Vollbildmodus, soweit vom Gerät unterstützt
- Deutsche Benutzeroberfläche
- Keine Internet-Berechtigung für die App erforderlich
- Kein AndroidX und keine zusätzlichen Laufzeit-Bibliotheken
- Integrierter Bereich **Über / Entwickler** mit GitHub-, LinkedIn- und Buch-Link

### Bedienung

1. App öffnen.
2. Gewünschten Text eingeben.
3. Orange oder Rot als Textfarbe auswählen.
4. Geschwindigkeit, Textgröße und Pixelgröße einstellen.
5. Bei Bedarf den großen blinkenden roten Linkspfeil aktivieren.
6. Je nach Montage Hochformat oder 90°-Modus auswählen.
7. **ANZEIGE STARTEN** drücken.
8. Zurück zu den Einstellungen: Android-Zurück-Taste drücken oder die Anzeige etwa 1,5 Sekunden lang gedrückt halten.

### APK unter Windows bauen

#### Voraussetzungen

- Windows 10 oder Windows 11
- Android Studio oder Android SDK
- Android SDK Platform 34
- Internetverbindung beim ersten Build

Das mitgelieferte Build-Skript verwendet ein lokales **portables Eclipse Temurin JDK 17**. Ein bereits installiertes Java 25 stört den Build daher nicht.

Starten:

```bat
BUILD_WITH_JDK17_V6.bat
```

Beim ersten Start lädt das Skript das benötigte portable JDK 17 und Gradle 8.7 in den lokalen Ordner `.tools` herunter.

Nach erfolgreichem Build wird die installierbare APK erzeugt:

```text
Rueckschild-LED.apk
```

### Installation auf dem Tablet

`Rueckschild-LED.apk` auf das Tablet kopieren und dort öffnen. Bei älteren Android-Versionen muss eventuell die Installation aus **Unbekannten Quellen** erlaubt werden.

Alternativ kann die APK bei aktiviertem USB-Debugging über ADB installiert werden.

### Technische Details

- Programmiersprache: Java
- `minSdk 14` — Android 4.0
- `targetSdk 28`
- `compileSdk 34`
- Android Gradle Plugin 8.5.2
- Gradle 8.7
- Portables Eclipse Temurin JDK 17 für Windows-Builds
- Kein AndroidX
- Keine Drittanbieter-Laufzeitabhängigkeiten

Der Pixel-Look wird programmatisch erzeugt: Der Text wird zuerst in einen niedrig aufgelösten Bitmap-Puffer gezeichnet und anschließend ohne Filterung vergrößert. Dadurch entstehen deutlich sichtbare, scharfe Pixel.

### Sicherheitshinweis

Das Tablet sicher befestigen und die Anzeige nur dort verwenden, wo dies sicher und zulässig ist. Display und Halterung dürfen Bewegung und Sicht nicht beeinträchtigen.

---

## About the developer / Über den Entwickler

**Amir Mobasher**

- GitHub: https://github.com/Amirmobash
- LinkedIn: https://www.linkedin.com/in/amirmobasher/
- Book on Amazon / Buch bei Amazon: https://www.amazon.sg/Mein-Computer-lernt-von-Machine-Learning-Projekt/dp/3695757825
- Project repository / Projekt-Repository: https://github.com/Amirmobash/rueckschild_led

---

## License

No license file is currently included in this repository. Add a license before distributing or accepting third-party contributions if you want to define explicit reuse terms.
