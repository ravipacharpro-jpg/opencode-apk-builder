package android.content.pm;

public class PackageManager {
    public static final int PERMISSION_GRANTED = 0;
    public static final int PERMISSION_DENIED = -1;
    public int checkPermission(String permName, String pkgName) { return PERMISSION_GRANTED; }
}
