package android.app;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.os.Bundle;

public class Activity extends ContextWrapper {
    public Activity() { super(null); }
    public void setContentView(int layoutResID) {}
    public void setContentView(android.view.View view) {}
    public void finish() {}
    public void startActivity(Intent intent) {}
    public Context getApplicationContext() { return this; }
    public Object getSystemService(String name) { return null; }
    public android.content.ContentResolver getContentResolver() { return null; }
    public void onCreate(Bundle savedInstanceState) {}
    public void onDestroy() {}
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {}
}
