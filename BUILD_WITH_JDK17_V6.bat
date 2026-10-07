@echo off
setlocal EnableExtensions EnableDelayedExpansion
cd /d "%~dp0"

set "TOOLS=%CD%\.tools"
set "GRADLE_VERSION=8.7"
set "GRADLE_DIR=%TOOLS%\gradle-%GRADLE_VERSION%"
set "JDK_DIR=%TOOLS%\jdk-17"

if not exist "%TOOLS%" mkdir "%TOOLS%"

set "SDK=%ANDROID_SDK_ROOT%"
if not defined SDK set "SDK=%ANDROID_HOME%"
if not defined SDK if exist "%LOCALAPPDATA%\Android\Sdk" set "SDK=%LOCALAPPDATA%\Android\Sdk"

if not defined SDK (
    echo [ERROR] Android SDK nicht gefunden.
    echo Installiere Android Studio oder setze ANDROID_SDK_ROOT.
    exit /b 1
)

if not exist "%SDK%\platforms\android-34" (
    echo [ERROR] Android SDK Platform 34 fehlt.
    echo Bitte in Android Studio unter SDK Manager installieren.
    exit /b 1
)

if not exist "%JDK_DIR%\bin\java.exe" (
    echo [1/3] Lade portables Eclipse Temurin JDK 17...
    powershell -NoProfile -ExecutionPolicy Bypass -Command ^
      "$ProgressPreference='SilentlyContinue';" ^
      "$api='https://api.adoptium.net/v3/assets/latest/17/hotspot?architecture=x64&heap_size=normal&image_type=jdk&jvm_impl=hotspot&os=windows&vendor=eclipse';" ^
      "$asset=(Invoke-RestMethod $api)[0];" ^
      "$url=$asset.binary.package.link;" ^
      "Invoke-WebRequest -UseBasicParsing $url -OutFile '%TOOLS%\jdk17.zip';" ^
      "Expand-Archive -Force '%TOOLS%\jdk17.zip' '%TOOLS%\jdk17-unpack';" ^
      "$folder=Get-ChildItem '%TOOLS%\jdk17-unpack' -Directory | Select-Object -First 1;" ^
      "Move-Item -Force $folder.FullName '%JDK_DIR%';" ^
      "Remove-Item -Recurse -Force '%TOOLS%\jdk17-unpack','%TOOLS%\jdk17.zip'"
    if errorlevel 1 exit /b 1
)

if not exist "%GRADLE_DIR%\bin\gradle.bat" (
    echo [2/3] Lade Gradle %GRADLE_VERSION%...
    powershell -NoProfile -ExecutionPolicy Bypass -Command ^
      "$ProgressPreference='SilentlyContinue';" ^
      "Invoke-WebRequest -UseBasicParsing 'https://services.gradle.org/distributions/gradle-%GRADLE_VERSION%-bin.zip' -OutFile '%TOOLS%\gradle.zip';" ^
      "Expand-Archive -Force '%TOOLS%\gradle.zip' '%TOOLS%';" ^
      "Remove-Item -Force '%TOOLS%\gradle.zip'"
    if errorlevel 1 exit /b 1
)

set "JAVA_HOME=%JDK_DIR%"
set "PATH=%JAVA_HOME%\bin;%PATH%"

> local.properties echo sdk.dir=%SDK:\=\\%

echo [3/3] Baue Debug-APK...
call "%GRADLE_DIR%\bin\gradle.bat" --no-daemon clean assembleDebug
if errorlevel 1 (
    echo [ERROR] Build fehlgeschlagen.
    exit /b 1
)

copy /Y "app\build\outputs\apk\debug\app-debug.apk" "Rueckschild-LED.apk" >nul

echo.
echo [OK] Fertig: %CD%\Rueckschild-LED.apk
exit /b 0
