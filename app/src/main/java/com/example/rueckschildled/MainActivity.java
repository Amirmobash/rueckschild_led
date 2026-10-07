package com.example.rueckschildled;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.InputType;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.SeekBar;
import android.widget.TextView;

public final class MainActivity extends Activity {
    private static final int ORANGE_RADIO_ID = 1001;
    private static final int RED_RADIO_ID = 1002;

    private SignPreferences signPreferences;
    private EditText messageInput;
    private RadioGroup colorGroup;
    private SeekBar speedBar;
    private SeekBar textSizeBar;
    private SeekBar pixelSizeBar;
    private TextView speedValue;
    private TextView textSizeValue;
    private TextView pixelSizeValue;
    private CheckBox uppercaseBox;
    private CheckBox brightnessBox;
    private CheckBox arrowBox;
    private CheckBox rotate90Box;
    private CheckBox rotate180Box;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        signPreferences = new SignPreferences(this);
        setContentView(buildContentView());
        populateForm(signPreferences.load());
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (messageInput != null) {
            signPreferences.save(readForm());
        }
    }

    private View buildContentView() {
        ScrollView scrollView = new ScrollView(this);
        scrollView.setFillViewport(true);
        scrollView.setBackgroundColor(Color.BLACK);

        LinearLayout content = new LinearLayout(this);
        content.setOrientation(LinearLayout.VERTICAL);
        int padding = dp(20);
        content.setPadding(padding, padding, padding, dp(32));
        scrollView.addView(content, new ScrollView.LayoutParams(
                ScrollView.LayoutParams.MATCH_PARENT,
                ScrollView.LayoutParams.WRAP_CONTENT));

        TextView title = createText(getString(R.string.settings_title), 28, Color.WHITE);
        title.setGravity(Gravity.CENTER_HORIZONTAL);
        content.addView(title, matchWrap());

        TextView subtitle = createText(getString(R.string.settings_subtitle), 15, Color.LTGRAY);
        subtitle.setGravity(Gravity.CENTER_HORIZONTAL);
        content.addView(subtitle, matchWrapWithTop(dp(4)));

        TextView hint = createText(getString(R.string.settings_hint), 13, Color.GRAY);
        hint.setGravity(Gravity.CENTER_HORIZONTAL);
        content.addView(hint, matchWrapWithTop(dp(6)));

        content.addView(sectionLabel(R.string.message_label), matchWrapWithTop(dp(22)));
        messageInput = new EditText(this);
        messageInput.setSingleLine(false);
        messageInput.setMinLines(2);
        messageInput.setMaxLines(4);
        messageInput.setHint(R.string.message_hint);
        messageInput.setTextColor(Color.WHITE);
        messageInput.setHintTextColor(Color.GRAY);
        messageInput.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_SENTENCES | InputType.TYPE_TEXT_FLAG_MULTI_LINE);
        content.addView(messageInput, matchWrap());

        content.addView(sectionLabel(R.string.text_color_label), matchWrapWithTop(dp(18)));
        colorGroup = new RadioGroup(this);
        colorGroup.setOrientation(RadioGroup.VERTICAL);
        RadioButton orange = createRadioButton(ORANGE_RADIO_ID, R.string.text_color_orange, SignSettings.ORANGE);
        RadioButton red = createRadioButton(RED_RADIO_ID, R.string.text_color_red, SignSettings.RED);
        colorGroup.addView(orange);
        colorGroup.addView(red);
        content.addView(colorGroup, matchWrap());

        speedValue = addSeekSection(content, R.string.speed_label, 100, new SeekValueFormatter() {
            @Override
            public String format(int progress) {
                return Math.round(60f + (progress / 100f) * 440f) + " px/s";
            }
        });
        speedBar = (SeekBar) speedValue.getTag();

        textSizeValue = addSeekSection(content, R.string.text_size_label, 100, new SeekValueFormatter() {
            @Override
            public String format(int progress) {
                return Math.round(56f + (progress / 100f) * 164f) + " sp";
            }
        });
        textSizeBar = (SeekBar) textSizeValue.getTag();

        pixelSizeValue = addSeekSection(content, R.string.pixel_size_label, 14, new SeekValueFormatter() {
            @Override
            public String format(int progress) {
                return (progress + 2) + " px";
            }
        });
        pixelSizeBar = (SeekBar) pixelSizeValue.getTag();

        uppercaseBox = createCheckBox(R.string.uppercase_label);
        brightnessBox = createCheckBox(R.string.brightness_label);
        arrowBox = createCheckBox(R.string.blink_arrow_label);
        rotate90Box = createCheckBox(R.string.landscape_label);
        rotate180Box = createCheckBox(R.string.rotate_label);
        content.addView(uppercaseBox, matchWrapWithTop(dp(12)));
        content.addView(brightnessBox, matchWrap());
        content.addView(arrowBox, matchWrap());
        content.addView(rotate90Box, matchWrap());
        content.addView(rotate180Box, matchWrap());

        Button startButton = new Button(this);
        startButton.setText(R.string.start_button);
        startButton.setTextSize(18);
        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signPreferences.save(readForm());
                startActivity(new Intent(MainActivity.this, SignActivity.class));
            }
        });
        content.addView(startButton, matchWrapWithTop(dp(20)));

        Button aboutButton = new Button(this);
        aboutButton.setText(R.string.about_button);
        aboutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, AboutActivity.class));
            }
        });
        content.addView(aboutButton, matchWrapWithTop(dp(8)));

        return scrollView;
    }

    private void populateForm(SignSettings settings) {
        messageInput.setText(settings.getMessage());
        colorGroup.check(settings.getTextColor() == SignSettings.RED ? RED_RADIO_ID : ORANGE_RADIO_ID);
        speedBar.setProgress(settings.getSpeedPercent());
        textSizeBar.setProgress(settings.getTextSizePercent());
        pixelSizeBar.setProgress(settings.getPixelSize() - 2);
        uppercaseBox.setChecked(settings.isUppercase());
        brightnessBox.setChecked(settings.isMaxBrightness());
        arrowBox.setChecked(settings.isBlinkArrow());
        rotate90Box.setChecked(settings.isRotate90());
        rotate180Box.setChecked(settings.isRotate180());
    }

    private SignSettings readForm() {
        int textColor = colorGroup.getCheckedRadioButtonId() == RED_RADIO_ID
                ? SignSettings.RED
                : SignSettings.ORANGE;
        return new SignSettings(
                messageInput.getText().toString(),
                textColor,
                speedBar.getProgress(),
                textSizeBar.getProgress(),
                pixelSizeBar.getProgress() + 2,
                uppercaseBox.isChecked(),
                brightnessBox.isChecked(),
                arrowBox.isChecked(),
                rotate90Box.isChecked(),
                rotate180Box.isChecked());
    }

    private TextView addSeekSection(
            LinearLayout parent,
            int labelRes,
            int max,
            final SeekValueFormatter formatter) {
        LinearLayout row = new LinearLayout(this);
        row.setOrientation(LinearLayout.HORIZONTAL);
        row.setGravity(Gravity.CENTER_VERTICAL);
        parent.addView(row, matchWrapWithTop(dp(16)));

        TextView label = createText(getString(labelRes), 15, Color.WHITE);
        row.addView(label, new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f));

        final TextView value = createText("", 14, Color.LTGRAY);
        value.setGravity(Gravity.RIGHT);
        row.addView(value, new LinearLayout.LayoutParams(dp(90), LinearLayout.LayoutParams.WRAP_CONTENT));

        final SeekBar seekBar = new SeekBar(this);
        seekBar.setMax(max);
        parent.addView(seekBar, matchWrap());
        value.setTag(seekBar);
        value.setText(formatter.format(seekBar.getProgress()));

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                value.setText(formatter.format(progress));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });
        return value;
    }

    private TextView sectionLabel(int stringRes) {
        TextView label = createText(getString(stringRes), 16, Color.WHITE);
        label.setPadding(0, 0, 0, dp(4));
        return label;
    }

    private RadioButton createRadioButton(int id, int textRes, int color) {
        RadioButton button = new RadioButton(this);
        button.setId(id);
        button.setText(textRes);
        button.setTextColor(color);
        return button;
    }

    private CheckBox createCheckBox(int textRes) {
        CheckBox box = new CheckBox(this);
        box.setText(textRes);
        box.setTextColor(Color.WHITE);
        return box;
    }

    private TextView createText(String text, float sizeSp, int color) {
        TextView view = new TextView(this);
        view.setText(text);
        view.setTextSize(sizeSp);
        view.setTextColor(color);
        return view;
    }

    private LinearLayout.LayoutParams matchWrap() {
        return new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
    }

    private LinearLayout.LayoutParams matchWrapWithTop(int marginTop) {
        LinearLayout.LayoutParams params = matchWrap();
        params.topMargin = marginTop;
        return params;
    }

    private int dp(int value) {
        return Math.round(value * getResources().getDisplayMetrics().density);
    }

    private interface SeekValueFormatter {
        String format(int progress);
    }
}
