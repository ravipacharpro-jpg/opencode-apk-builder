package androidx.appcompat.app;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;

public class AppCompatActivity extends Activity {
    public AppCompatActivity() { super(); }
    public void onCreate(Bundle savedInstanceState) { super.onCreate(savedInstanceState); }
    public void onDestroy() { super.onDestroy(); }
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
    public void setContentView(int layoutResID) { super.setContentView(layoutResID); }
    public void setContentView(android.view.View view) { super.setContentView(view); }
}
