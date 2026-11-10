package android.content;

public class ContextWrapper extends Context {
    public ContextWrapper(Context base) { }
    public Object getSystemService(String name) { return null; }
    public SharedPreferences getSharedPreferences(String name, int mode) { return null; }
    public android.content.ContentResolver getContentResolver() { return null; }
}
