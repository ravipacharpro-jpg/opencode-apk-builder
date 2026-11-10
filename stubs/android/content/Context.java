package android.content;

import android.content.pm.ApplicationInfo;

public class Context {
    public static final String POWER_SERVICE = "power";
    public static final int MODE_PRIVATE = 0;
    public Object getSystemService(String name) { return null; }
    public SharedPreferences getSharedPreferences(String name, int mode) { return null; }
    public android.content.ContentResolver getContentResolver() { return null; }
    public ClassLoader getClassLoader() { return getClass().getClassLoader(); }
    public ApplicationInfo getApplicationInfo() { return null; }
    public void startActivity(Intent intent) {}
}
