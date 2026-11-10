package com.opencode.mobile;

import android.os.Bundle;
import android.view.*;
import android.view.Gravity;
import android.widget.*;
import androidx.appcompat.app.*;

public class AdbPairActivity extends AppCompatActivity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        LinearLayout root = new LinearLayout(this);
        root.setOrientation(LinearLayout.VERTICAL);
        root.setGravity(Gravity.CENTER_HORIZONTAL);
        root.setPadding(32, 48, 32, 32);
        root.setBackgroundColor(0xFF000000);

        TextView title = new TextView(this);
        title.setText("ADB Pairing");
        title.setTextSize(26);
        title.setTypeface(null, android.graphics.Typeface.BOLD);
        title.setTextColor(0xFFFFFFFF);
        root.addView(title);

        TextView sub = new TextView(this);
        sub.setText("One-time setup. Enables app install, screen control,\nand device automation — all from OpenCode.");
        sub.setTextSize(16);
        sub.setTextColor(0xFF8E8E93);
        sub.setGravity(Gravity.CENTER);
        sub.setLineSpacing(4, 1.15f);
        root.addView(sub);

        Button pair = new Button(this);
        pair.setText("Pair Device (Wireless Debugging)");
        pair.setTextColor(0xFFFFFFFF);
        pair.setTextSize(17);
        pair.setAllCaps(false);
        pair.setBackgroundColor(0xFF0A84FF);
        root.addView(pair);

        TextView note = new TextView(this);
        note.setText("Go to Settings → System → Developer options →\nWireless debugging, then pair with the code shown.");
        note.setTextSize(13);
        note.setTextColor(0xFF8E8E93);
        note.setGravity(Gravity.CENTER);
        note.setLineSpacing(2, 1.1f);
        root.addView(note);

        setContentView(root);
    }
}
