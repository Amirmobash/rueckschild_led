package com.example.rueckschildled;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.os.SystemClock;
import android.view.MotionEvent;
import android.view.View;

import java.util.Locale;

/**
 * Full-screen renderer for the LED-style sign.
 *
 * The scene is drawn into a deliberately small bitmap and then enlarged with filtering disabled.
 * That keeps the pixel blocks crisp without shipping a custom font or graphics dependency.
 */
public final class LedSignView extends View {
    private static final long FRAME_DELAY_MS = 16L;
    private static final long ARROW_BLINK_MS = 450L;
    private static final long LONG_PRESS_TO_EXIT_MS = 1500L;

    private final SignSettings settings;
    private final Paint textPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Paint arrowPaint = new Paint();
    private final Paint bitmapPaint = new Paint();
    private final Path arrowPath = new Path();
    private final Rect destination = new Rect();

    private Bitmap bufferBitmap;
    private Canvas bufferCanvas;
    private int bufferWidth;
    private int bufferHeight;
    private int logicalWidth;
    private int logicalHeight;

    private float scrollOffset;
    private long lastFrameTime;
    private long touchDownTime;
    private float touchDownX;
    private float touchDownY;
    private boolean touchStillEligible;
    private boolean animating;
    private OnExitRequestedListener exitRequestedListener;

    private final Runnable exitRunnable = new Runnable() {
        @Override
        public void run() {
            if (touchStillEligible && SystemClock.uptimeMillis() - touchDownTime >= LONG_PRESS_TO_EXIT_MS) {
                touchStillEligible = false;
                if (exitRequestedListener != null) {
                    exitRequestedListener.onExitRequested();
                }
            }
        }
    };

    public LedSignView(Context context, SignSettings settings) {
        super(context);
        this.settings = settings;
        setBackgroundColor(Color.BLACK);
        setKeepScreenOn(true);
        setFocusable(true);
        setClickable(true);

        textPaint.setColor(settings.getTextColor());
        textPaint.setStyle(Paint.Style.FILL);
        textPaint.setSubpixelText(false);
        textPaint.setLinearText(false);

        arrowPaint.setColor(Color.RED);
        arrowPaint.setStyle(Paint.Style.FILL);
        arrowPaint.setAntiAlias(false);

        bitmapPaint.setFilterBitmap(false);
        bitmapPaint.setDither(false);
    }

    public void setOnExitRequestedListener(OnExitRequestedListener listener) {
        exitRequestedListener = listener;
    }

    public void resumeAnimation() {
        if (!animating) {
            animating = true;
            lastFrameTime = 0L;
            postInvalidateOnAnimationCompat();
        }
    }

    public void pauseAnimation() {
        animating = false;
        removeCallbacks(exitRunnable);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        resumeAnimation();
    }

    @Override
    protected void onDetachedFromWindow() {
        pauseAnimation();
        releaseBuffer();
        super.onDetachedFromWindow();
    }

    @Override
    protected void onSizeChanged(int width, int height, int oldWidth, int oldHeight) {
        super.onSizeChanged(width, height, oldWidth, oldHeight);
        configureBuffer(width, height);
        scrollOffset = 0f;
        lastFrameTime = 0L;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (bufferCanvas == null || logicalWidth <= 0 || logicalHeight <= 0) {
            return;
        }

        updateScrollPosition();
        drawLowResolutionFrame();
        drawScaledAndRotated(canvas);

        if (animating) {
            postInvalidateDelayed(FRAME_DELAY_MS);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
                touchDownTime = SystemClock.uptimeMillis();
                touchDownX = event.getX();
                touchDownY = event.getY();
                touchStillEligible = true;
                removeCallbacks(exitRunnable);
                postDelayed(exitRunnable, LONG_PRESS_TO_EXIT_MS);
                return true;

            case MotionEvent.ACTION_MOVE:
                float dx = Math.abs(event.getX() - touchDownX);
                float dy = Math.abs(event.getY() - touchDownY);
                float threshold = 24f * getResources().getDisplayMetrics().density;
                if (dx > threshold || dy > threshold) {
                    cancelPendingExit();
                }
                return true;

            case MotionEvent.ACTION_UP:
                cancelPendingExit();
                performClick();
                return true;

            case MotionEvent.ACTION_CANCEL:
                cancelPendingExit();
                return true;

            default:
                return super.onTouchEvent(event);
        }
    }

    @Override
    public boolean performClick() {
        super.performClick();
        return true;
    }

    private void configureBuffer(int width, int height) {
        releaseBuffer();
        if (width <= 0 || height <= 0) {
            return;
        }

        int rotation = settings.getRotationDegrees();
        boolean quarterTurn = rotation == 90 || rotation == 270;
        logicalWidth = quarterTurn ? height : width;
        logicalHeight = quarterTurn ? width : height;

        int pixelSize = settings.getPixelSize();
        bufferWidth = Math.max(1, (logicalWidth + pixelSize - 1) / pixelSize);
        bufferHeight = Math.max(1, (logicalHeight + pixelSize - 1) / pixelSize);

        bufferBitmap = Bitmap.createBitmap(bufferWidth, bufferHeight, Bitmap.Config.ARGB_8888);
        bufferCanvas = new Canvas(bufferBitmap);

        float scaledDensity = getResources().getDisplayMetrics().scaledDensity;
        float textSizePx = settings.getTextSizeSp() * scaledDensity;
        textPaint.setTextSize(Math.max(8f, textSizePx / pixelSize));
    }

    private void updateScrollPosition() {
        long now = SystemClock.uptimeMillis();
        if (lastFrameTime == 0L) {
            lastFrameTime = now;
            return;
        }

        long elapsedMs = now - lastFrameTime;
        lastFrameTime = now;
        // Avoid a huge jump when the app resumes after a pause.
        elapsedMs = Math.min(elapsedMs, 100L);

        float speedInBufferPixels = settings.getScrollSpeedPxPerSecond() / settings.getPixelSize();
        scrollOffset += speedInBufferPixels * elapsedMs / 1000f;
    }

    private void drawLowResolutionFrame() {
        bufferCanvas.drawColor(Color.BLACK);

        String message = settings.getMessage();
        if (settings.isUppercase()) {
            message = message.toUpperCase(Locale.getDefault());
        }
        if (message.length() == 0) {
            message = " ";
        }

        float textWidth = textPaint.measureText(message);
        float x = bufferWidth - scrollOffset;
        float y = verticalBaseline(textPaint, bufferHeight);
        bufferCanvas.drawText(message, x, y, textPaint);

        if (x + textWidth < 0f) {
            scrollOffset = 0f;
        }

        if (settings.isBlinkArrow() && isArrowVisible()) {
            drawLeftArrow(bufferCanvas);
        }
    }

    private void drawLeftArrow(Canvas canvas) {
        float margin = Math.max(2f, bufferWidth * 0.015f);
        float width = Math.max(12f, bufferWidth * 0.19f);
        float top = bufferHeight * 0.16f;
        float bottom = bufferHeight * 0.84f;
        float centerY = bufferHeight * 0.50f;
        float shaftTop = bufferHeight * 0.37f;
        float shaftBottom = bufferHeight * 0.63f;
        float tailX = margin + width;
        float headJoinX = margin + width * 0.48f;

        arrowPath.reset();
        arrowPath.moveTo(margin, centerY);
        arrowPath.lineTo(headJoinX, top);
        arrowPath.lineTo(headJoinX, shaftTop);
        arrowPath.lineTo(tailX, shaftTop);
        arrowPath.lineTo(tailX, shaftBottom);
        arrowPath.lineTo(headJoinX, shaftBottom);
        arrowPath.lineTo(headJoinX, bottom);
        arrowPath.close();
        canvas.drawPath(arrowPath, arrowPaint);
    }

    private boolean isArrowVisible() {
        long phase = SystemClock.uptimeMillis() / ARROW_BLINK_MS;
        return phase % 2L == 0L;
    }

    private void drawScaledAndRotated(Canvas canvas) {
        int rotation = settings.getRotationDegrees();
        canvas.save();
        switch (rotation) {
            case 90:
                canvas.translate(getWidth(), 0f);
                canvas.rotate(90f);
                break;
            case 180:
                canvas.translate(getWidth(), getHeight());
                canvas.rotate(180f);
                break;
            case 270:
                canvas.translate(0f, getHeight());
                canvas.rotate(270f);
                break;
            default:
                break;
        }

        destination.set(0, 0, logicalWidth, logicalHeight);
        canvas.drawBitmap(bufferBitmap, null, destination, bitmapPaint);
        canvas.restore();
    }

    private static float verticalBaseline(Paint paint, int height) {
        Paint.FontMetrics metrics = paint.getFontMetrics();
        return (height - (metrics.bottom - metrics.top)) / 2f - metrics.top;
    }

    private void cancelPendingExit() {
        touchStillEligible = false;
        removeCallbacks(exitRunnable);
    }

    private void releaseBuffer() {
        if (bufferBitmap != null) {
            bufferBitmap.recycle();
        }
        bufferBitmap = null;
        bufferCanvas = null;
    }

    private void postInvalidateOnAnimationCompat() {
        // API 14 is supported, so use a small delayed invalidation instead of Choreographer.
        postInvalidateDelayed(FRAME_DELAY_MS);
    }

    public interface OnExitRequestedListener {
        void onExitRequested();
    }
}
