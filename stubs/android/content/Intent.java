package android.content;

public class Intent {
    public Intent() {}
    public Intent(Context packageContext, Class<?> cls) {}
    public Intent setAction(String action) { return this; }
    public Intent putExtra(String name, String value) { return this; }
    public Intent putExtra(String name, int value) { return this; }
    public Intent addFlags(int flags) { return this; }
    public String getAction() { return null; }
    public String getStringExtra(String name) { return null; }
}
