package android.app;

import android.content.Context;

public class PowerManager {
    public static final int PARTIAL_WAKE_LOCK = 1;
    public class WakeLock {
        public void acquire() {}
        public void acquire(long timeout) {}
        public void release() {}
        public boolean isHeld() { return false; }
    }
    public WakeLock newWakeLock(int flags, String tag) { return new WakeLock(); }
}
