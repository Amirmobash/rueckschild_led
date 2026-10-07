package com.example.rueckschildled;

import android.content.Context;
import android.content.SharedPreferences;

/** Owns persistence so activities do not need to know individual preference keys. */
public final class SignPreferences {
    private static final String FILE_NAME = "sign_settings";

    private static final String KEY_MESSAGE = "message";
    private static final String KEY_TEXT_COLOR = "text_color";
    private static final String KEY_SPEED = "speed";
    private static final String KEY_TEXT_SIZE = "text_size";
    private static final String KEY_PIXEL_SIZE = "pixel_size";
    private static final String KEY_UPPERCASE = "uppercase";
    private static final String KEY_MAX_BRIGHTNESS = "max_brightness";
    private static final String KEY_BLINK_ARROW = "blink_arrow";
    private static final String KEY_ROTATE_90 = "rotate_90";
    private static final String KEY_ROTATE_180 = "rotate_180";

    private final SharedPreferences preferences;
    private final String defaultMessage;

    public SignPreferences(Context context) {
        preferences = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
        defaultMessage = context.getString(R.string.default_message);
    }

    public SignSettings load() {
        return new SignSettings(
                preferences.getString(KEY_MESSAGE, defaultMessage),
                preferences.getInt(KEY_TEXT_COLOR, SignSettings.ORANGE),
                preferences.getInt(KEY_SPEED, 55),
                preferences.getInt(KEY_TEXT_SIZE, 52),
                preferences.getInt(KEY_PIXEL_SIZE, 5),
                preferences.getBoolean(KEY_UPPERCASE, true),
                preferences.getBoolean(KEY_MAX_BRIGHTNESS, true),
                preferences.getBoolean(KEY_BLINK_ARROW, true),
                preferences.getBoolean(KEY_ROTATE_90, false),
                preferences.getBoolean(KEY_ROTATE_180, false));
    }

    public void save(SignSettings settings) {
        preferences.edit()
                .putString(KEY_MESSAGE, settings.getMessage())
                .putInt(KEY_TEXT_COLOR, settings.getTextColor())
                .putInt(KEY_SPEED, settings.getSpeedPercent())
                .putInt(KEY_TEXT_SIZE, settings.getTextSizePercent())
                .putInt(KEY_PIXEL_SIZE, settings.getPixelSize())
                .putBoolean(KEY_UPPERCASE, settings.isUppercase())
                .putBoolean(KEY_MAX_BRIGHTNESS, settings.isMaxBrightness())
                .putBoolean(KEY_BLINK_ARROW, settings.isBlinkArrow())
                .putBoolean(KEY_ROTATE_90, settings.isRotate90())
                .putBoolean(KEY_ROTATE_180, settings.isRotate180())
                .apply();
    }
}
