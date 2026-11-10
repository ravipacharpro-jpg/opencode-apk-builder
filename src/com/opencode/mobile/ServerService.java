package com.opencode.mobile;

import android.app.*;
import android.content.*;
import android.os.IBinder;

public class ServerService extends Service {
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return 1; // START_STICKY
    }

    @Override
    public IBinder onBind(Intent intent) { return null; }
}
