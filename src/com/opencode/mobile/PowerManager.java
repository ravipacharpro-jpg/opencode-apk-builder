package com.opencode.mobile;

import android.app.*;
import android.content.*;

public class PowerManager {
    public static void acquireWakeLock(Context c) {
        android.os.PowerManager pm = (android.os.PowerManager) c.getSystemService(Context.POWER_SERVICE);
        android.os.PowerManager.WakeLock wl = pm.newWakeLock(
            android.os.PowerManager.PARTIAL_WAKE_LOCK, "OpenCode::WakeLock");
        wl.acquire(10 * 60 * 1000);
    }
}
