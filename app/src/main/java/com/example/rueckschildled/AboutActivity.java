package com.example.rueckschildled;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

public final class AboutActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(buildContentView());
    }

    private View buildContentView() {
        ScrollView scroll = new ScrollView(this);
        scroll.setBackgroundColor(Color.BLACK);

        LinearLayout content = new LinearLayout(this);
        content.setOrientation(LinearLayout.VERTICAL);
        int padding = dp(24);
        content.setPadding(padding, padding, padding, padding);
        scroll.addView(content);

        TextView title = text(R.string.about_title, 26, Color.WHITE);
        title.setGravity(Gravity.CENTER_HORIZONTAL);
        content.addView(title);

        TextView description = text(R.string.about_description, 16, Color.LTGRAY);
        description.setGravity(Gravity.CENTER_HORIZONTAL);
        content.addView(description, withTop(dp(14)));

        TextView creatorLabel = text(R.string.about_creator_label, 14, Color.GRAY);
        creatorLabel.setGravity(Gravity.CENTER_HORIZONTAL);
        content.addView(creatorLabel, withTop(dp(24)));

        TextView creator = text(R.string.about_creator_name, 22, SignSettings.ORANGE);
        creator.setGravity(Gravity.CENTER_HORIZONTAL);
        content.addView(creator);

        TextView version = text(R.string.about_version, 14, Color.GRAY);
        version.setGravity(Gravity.CENTER_HORIZONTAL);
        content.addView(version, withTop(dp(6)));

        content.addView(linkButton(R.string.about_github, "https://github.com/Amirmobash"), withTop(dp(28)));
        content.addView(linkButton(R.string.about_linkedin, "https://www.linkedin.com/in/amirmobasher/"), withTop(dp(8)));
        content.addView(linkButton(R.string.about_book, "https://www.amazon.sg/Mein-Computer-lernt-von-Machine-Learning-Projekt/dp/3695757825"), withTop(dp(8)));

        Button back = new Button(this);
        back.setText(R.string.about_back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        content.addView(back, withTop(dp(24)));

        return scroll;
    }

    private Button linkButton(int textRes, final String url) {
        Button button = new Button(this);
        button.setText(textRes);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openUrl(url);
            }
        });
        return button;
    }

    private void openUrl(String url) {
        try {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
        } catch (ActivityNotFoundException error) {
            Toast.makeText(this, R.string.link_error, Toast.LENGTH_LONG).show();
        }
    }

    private TextView text(int textRes, float sizeSp, int color) {
        TextView view = new TextView(this);
        view.setText(textRes);
        view.setTextSize(sizeSp);
        view.setTextColor(color);
        return view;
    }

    private LinearLayout.LayoutParams withTop(int marginTop) {
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        params.topMargin = marginTop;
        return params;
    }

    private int dp(int value) {
        return Math.round(value * getResources().getDisplayMetrics().density);
    }
}
