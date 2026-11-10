package com.opencode.mobile;

import android.os.Bundle;
import android.view.*;
import android.view.Gravity;
import android.widget.*;
import androidx.appcompat.app.*;

public class SettingsActivity extends AppCompatActivity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        LinearLayout root = new LinearLayout(this);
        root.setOrientation(LinearLayout.VERTICAL);
        root.setPadding(24, 48, 24, 24);
        root.setBackgroundColor(0xFF000000);

        TextView title = new TextView(this);
        title.setText("Settings");
        title.setTextSize(24);
        title.setTypeface(null, android.graphics.Typeface.BOLD);
        title.setTextColor(0xFFFFFFFF);
        root.addView(title);

        CheckBox dark = new CheckBox(this);
        dark.setText("Dark Mode");
        dark.setChecked(true);
        dark.setTextSize(17);
        root.addView(dark);

        Button reset = new Button(this);
        reset.setText("Reset API Keys");
        reset.setTextColor(0xFFFFFFFF);
        reset.setTextSize(16);
        reset.setAllCaps(false);
        reset.setBackgroundColor(0xFF0A84FF);
        root.addView(reset);

        setContentView(root);
    }
}
