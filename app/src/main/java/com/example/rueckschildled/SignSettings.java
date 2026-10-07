package com.example.rueckschildled;

import android.graphics.Color;

/** Immutable snapshot of the settings used by the display screen. */
public final class SignSettings {
    public static final int ORANGE = Color.rgb(255, 138, 0);
    public static final int RED = Color.rgb(255, 48, 48);

    private final String message;
    private final int textColor;
    private final int speedPercent;
    private final int textSizePercent;
    private final int pixelSize;
    private final boolean uppercase;
    private final boolean maxBrightness;
    private final boolean blinkArrow;
    private final boolean rotate90;
    private final boolean rotate180;

    public SignSettings(
            String message,
            int textColor,
            int speedPercent,
            int textSizePercent,
            int pixelSize,
            boolean uppercase,
            boolean maxBrightness,
            boolean blinkArrow,
            boolean rotate90,
            boolean rotate180) {
        this.message = sanitizeMessage(message);
        this.textColor = textColor == RED ? RED : ORANGE;
        this.speedPercent = clamp(speedPercent, 0, 100);
        this.textSizePercent = clamp(textSizePercent, 0, 100);
        this.pixelSize = clamp(pixelSize, 2, 16);
        this.uppercase = uppercase;
        this.maxBrightness = maxBrightness;
        this.blinkArrow = blinkArrow;
        this.rotate90 = rotate90;
        this.rotate180 = rotate180;
    }

    public String getMessage() {
        return message;
    }

    public int getTextColor() {
        return textColor;
    }

    public int getSpeedPercent() {
        return speedPercent;
    }

    public int getTextSizePercent() {
        return textSizePercent;
    }

    public int getPixelSize() {
        return pixelSize;
    }

    public boolean isUppercase() {
        return uppercase;
    }

    public boolean isMaxBrightness() {
        return maxBrightness;
    }

    public boolean isBlinkArrow() {
        return blinkArrow;
    }

    public boolean isRotate90() {
        return rotate90;
    }

    public boolean isRotate180() {
        return rotate180;
    }

    public int getRotationDegrees() {
        int degrees = 0;
        if (rotate90) {
            degrees += 90;
        }
        if (rotate180) {
            degrees += 180;
        }
        return degrees % 360;
    }

    /** Roughly 60–500 physical pixels per second: about 5× from a comfortable baseline. */
    public float getScrollSpeedPxPerSecond() {
        return 60f + (speedPercent / 100f) * 440f;
    }

    /** 56–220sp, intentionally broad for small and large tablets. */
    public float getTextSizeSp() {
        return 56f + (textSizePercent / 100f) * 164f;
    }

    private static String sanitizeMessage(String value) {
        if (value == null) {
            return "";
        }
        return value.trim();
    }

    private static int clamp(int value, int min, int max) {
        return Math.max(min, Math.min(max, value));
    }
}
