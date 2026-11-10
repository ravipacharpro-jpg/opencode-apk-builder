package com.opencode.mobile;

import android.os.Bundle;
import android.content.Intent;
import android.view.*;
import android.view.Gravity;
import android.view.View.OnClickListener;
import android.widget.*;
import androidx.appcompat.app.*;

public class OnboardingActivity extends AppCompatActivity {
    private int page = 0;
    private final String[] titles = {
        "Welcome to OpenCode",
        "Your API Key",
        "Permission to access files",
        "ADB — your device, under control"
    };
    private final String[] bodies = {
        "The world's best AI coding agent, now on your phone.\n" +
        "Same power as desktop — in an app that feels like it belongs here.",
        "Paste your API key once (Anthropic, OpenAI, Gemini, Groq…).\n" +
        "Stored locally. Never leaves your device.",
        "OpenCode reads and writes your projects. Grant file access\n" +
        "so it can edit code, run tests, and explore your repos.",
        "One-time ADB pairing lets OpenCode install apps, read screen,\n" +
        "and control your device — all from inside this app."
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        LinearLayout root = new LinearLayout(this);
        root.setOrientation(LinearLayout.VERTICAL);
        root.setGravity(Gravity.CENTER);
        root.setPadding(32, 64, 32, 32);
        root.setBackgroundColor(0xFF000000);

        ImageView logo = new ImageView(this);
        LinearLayout.LayoutParams llp = new LinearLayout.LayoutParams(120, 120);
        llp.topMargin = 40; llp.bottomMargin = 32;
        root.addView(logo, llp);

        TextView title = new TextView(this);
        title.setText(titles[0]);
        title.setTextSize(28);
        title.setTypeface(null, android.graphics.Typeface.BOLD);
        title.setTextColor(0xFFFFFFFF);
        title.setGravity(Gravity.CENTER);
        title.setPadding(0, 24, 0, 8);
        root.addView(title);

        TextView body = new TextView(this);
        body.setText(bodies[0]);
        body.setTextSize(17);
        body.setTextColor(0xFF8E8E93);
        body.setGravity(Gravity.CENTER);
        body.setLineSpacing(4, 1.15f);
        body.setPadding(0, 0, 0, 40);
        root.addView(body);

        Button next = new Button(this);
        next.setText("Continue");
        next.setTextColor(0xFFFFFFFF);
        next.setTextSize(17);
        next.setAllCaps(false);
        next.setPadding(32, 16, 32, 16);
        next.setBackgroundColor(0xFF0A84FF);
        root.addView(next);

        next.setOnClickListener((OnClickListener) v -> {
            page++;
            if (page >= 4) {
                Prefs.setBoolean(this, Prefs.KEY_ONBOARDED, true);
                Intent i = new Intent(this, MainActivity.class);
                startActivity(i);
                finish();
            } else {
                title.setText(titles[page]);
                body.setText(bodies[page]);
                if (page == 3) next.setText("Get Started");
            }
        });

        setContentView(root);
    }
}
