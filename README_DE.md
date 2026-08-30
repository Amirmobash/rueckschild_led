# Rückschild LED

Eine sehr leichte Android-App für ein altes Tablet, das im Hochformat an einem Rucksack oder einer Tasche befestigt wird. Die Anzeige ist komplett schwarz; die Laufschrift wird in sehr hellem Orange pixelig dargestellt.

## Funktionen

- Android 4.0 / API 14 oder neuer
- Hochformat fest eingestellt
- komplett schwarzer Hintergrund
- sehr helle orange Pixel-Laufschrift
- Text frei einstellbar
- Geschwindigkeit einstellbar, jetzt bis zu 5× schneller als das frühere Maximum
- fester blinkender Linkspfeil `<<<` (abschaltbar)
- Textgröße einstellbar
- Pixelgröße einstellbar
- automatische GROSSBUCHSTABEN optional
- maximale Bildschirmhelligkeit optional
- 180°-Drehung für die Montage optional
- Einstellungen werden dauerhaft gespeichert
- Bildschirm bleibt während der Anzeige eingeschaltet
- Vollbildmodus; Navigationsleisten werden soweit vom Android-Gerät unterstützt ausgeblendet
- keine Internet-Berechtigung und keine zusätzlichen App-Bibliotheken

## Bedienung

1. App öffnen.
2. Text eingeben, zum Beispiel `ARBEIT RUFT! BITTE LINKS VORBEI`.
3. Geschwindigkeit, Textgröße und Pixelgröße einstellen.
4. `ANZEIGE STARTEN` drücken.
5. Zurück zu den Einstellungen: Android-Zurück-Taste drücken oder die Anzeige etwa 1,5 Sekunden lang gedrückt halten.

## APK unter Windows bauen

### Voraussetzungen

- Windows 10/11
- Android Studio oder Android SDK
- Android SDK Platform 34 installiert
- JDK 17 bis 21 (empfohlen: JDK 17; Java 22+ nicht fuer Gradle 8.7 verwenden)
- Internet beim ersten Build (Gradle und Android-Gradle-Plugin werden heruntergeladen)

### Einfachster Weg

Doppelklick auf:

`build_apk.bat`

Die Datei sucht das Android SDK automatisch, waehlt selbst ein kompatibles JDK 17 bis 21 (bevorzugt das JDK von Android Studio), laedt Gradle 8.7 bei Bedarf herunter und fuehrt einen Debug-Build aus.

Nach erfolgreichem Build liegt die installierbare Datei hier:

`Rueckschild-LED.apk`

### Android SDK Platform 34 installieren

In Android Studio:

`Tools` -> `SDK Manager` -> `SDK Platforms` -> `Android 14 (API 34)` aktivieren -> `Apply`

Für die Installation per USB außerdem unter `SDK Tools` die `Android SDK Platform-Tools` installieren.

## APK auf das Tablet installieren

Variante A: `Rueckschild-LED.apk` auf das Tablet kopieren und dort öffnen. Auf alten Android-Versionen muss gegebenenfalls "Unbekannte Quellen" erlaubt werden.

Variante B: USB-Debugging aktivieren, Tablet per USB verbinden und `install_apk.bat` starten.

## Technische Details

- `minSdk 14` = Android 4.0
- `targetSdk 28`
- `compileSdk 34`
- Android Gradle Plugin 8.5.2
- Gradle 8.7
- reine Java-App, kein AndroidX, keine Drittanbieter-Abhängigkeiten

Der Pixel-Look entsteht nicht durch eine Bilddatei: Der Text wird in einen kleinen Bitmap-Puffer gezeichnet und anschließend mit deaktivierter Filterung auf die volle Bildschirmgröße skaliert. Dadurch bleiben die Pixel deutlich und scharf.

## WICHTIG: Java-25-Fehler / portable JDK 17

Falls Windows bereits Java 25 installiert hat, ist das kein Problem mehr. Die aktuelle `build_apk.bat` verwendet das System-Java absichtlich **nicht**.

Beim ersten Start lädt sie automatisch ein portables **Eclipse Temurin JDK 17** nach `.tools\jdk17` herunter und setzt `JAVA_HOME` nur für diesen Build darauf. Danach zeigt die BAT vor dem Build ausdrücklich `java -version` und `gradle --version` an.

Die richtige Ausgabe muss bei JVM/Java **17** anzeigen. Wenn dort Java 25 steht, wurde eine alte BAT-Datei gestartet. Verwende in diesem Paket `build_apk.bat` oder `build_apk_PORTABLE_JDK17.bat`.

### Empfohlener Start

Um Verwechslungen mit einer alten BAT-Datei auszuschließen, starte **`BUILD_WITH_JDK17.bat`**. Diese Datei ist identisch mit der aktuellen `build_apk.bat`, trägt aber einen eindeutigen Namen.

Beim ersten Lauf werden JDK 17 und Gradle in den lokalen Ordner `.tools` geladen. Das bereits auf Windows installierte Java 25 darf installiert bleiben und wird nicht verändert.

## V3-Hinweis: Windows-Pfadfehler beim Java-Test
Falls eine ältere Version nach `JAVA_HOME: ...jdk-17...` mit
`Das System kann den angegebenen Pfad nicht finden` abbricht, verwende die
V3-Datei `BUILD_WITH_JDK17.bat`. Die Java-17-Prüfung startet `java.exe` jetzt
direkt und verwendet keinen `FOR /F`-Pipe-Aufruf mehr.


## V4 – schnellere Laufschrift und blinkender Linkspfeil

- Geschwindigkeitsregler erweitert: `1..500`; Maximum ca. 1300 px/s und damit etwa 5× schneller als das alte Maximum.
- Neuer fester blinkender Linkspfeil `<<<` am unteren Rand der Anzeige.
- Der Pfeil blinkt etwa alle 400 ms und kann in den Einstellungen abgeschaltet werden.
- Neuer kurzer Standardtext: `ARBEIT RUFT! BITTE LINKS VORBEI`.
- Alte gespeicherte Standardmeldung `BITTE LINKS VORBEI` wird automatisch auf den neuen Standardtext migriert.

## V5 – riesiger roter Pfeil, Textfarbe und 90°-Modus

- Der Linkspfeil ist jetzt ein eigener, sehr großer geometrischer Pfeil und immer kräftig rot.
- Der Pfeil blinkt weiterhin ungefähr alle 400 ms und kann abgeschaltet werden.
- Die Textfarbe kann in den Einstellungen zwischen **Leuchtendes Orange** und **Rot** gewählt werden.
- Neuer **90°-Modus (Querformat)** für die Verwendung des Tablets im Landscape-Modus.
- Die vorhandene 180°-Drehung kann zusätzlich mit dem 90°-Modus kombiniert werden, falls das Tablet andersherum befestigt ist.
- Die Einstellungen bleiben auf Deutsch und werden dauerhaft gespeichert.
- Versionsstand: 1.2 / versionCode 3.

Zum Bauen unter Windows am besten `BUILD_WITH_JDK17_V5.bat` starten.
