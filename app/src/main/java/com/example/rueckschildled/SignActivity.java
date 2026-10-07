package com.example.rueckschildled;

import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

public final class SignActivity extends Activity {
    private LedSignView signView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        SignSettings settings = new SignPreferences(this).load();
        configureWindow(settings);

        signView = new LedSignView(this, settings);
        signView.setOnExitRequestedListener(new LedSignView.OnExitRequestedListener() {
            @Override
            public void onExitRequested() {
                finish();
            }
        });
        setContentView(signView);
    }

    @Override
    protected void onResume() {
        super.onResume();
        enterFullscreen();
        if (signView != null) {
            signView.resumeAnimation();
        }
    }

    @Override
    protected void onPause() {
        if (signView != null) {
            signView.pauseAnimation();
        }
        super.onPause();
    }

    private void configureWindow(SignSettings settings) {
        getWindow().addFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN
                        | WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        WindowManager.LayoutParams attributes = getWindow().getAttributes();
        attributes.screenBrightness = settings.isMaxBrightness()
                ? WindowManager.LayoutParams.BRIGHTNESS_OVERRIDE_FULL
                : WindowManager.LayoutParams.BRIGHTNESS_OVERRIDE_NONE;
        getWindow().setAttributes(attributes);
    }

    private void enterFullscreen() {
        View decorView = getWindow().getDecorView();
        int flags = View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            flags |= View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                    | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
        }
        decorView.setSystemUiVisibility(flags);
    }
}
