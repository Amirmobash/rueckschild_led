@echo off
setlocal EnableExtensions
cd /d "%~dp0"

set "SDK=%ANDROID_SDK_ROOT%"
if not defined SDK set "SDK=%ANDROID_HOME%"
if not defined SDK if exist "%LOCALAPPDATA%\Android\Sdk" set "SDK=%LOCALAPPDATA%\Android\Sdk"

if not exist "Rueckschild-LED.apk" (
    echo Rueckschild-LED.apk fehlt. Zuerst build_apk.bat starten.
    pause
    exit /b 1
)

if not defined SDK (
    echo Android SDK nicht gefunden.
    pause
    exit /b 1
)

if not exist "%SDK%\platform-tools\adb.exe" (
    echo adb.exe fehlt. Im Android SDK Manager "Android SDK Platform-Tools" installieren.
    pause
    exit /b 1
)

echo Tablet per USB verbinden und USB-Debugging aktivieren.
"%SDK%\platform-tools\adb.exe" install -r "%~dp0Rueckschild-LED.apk"
echo.
pause
endlocal
