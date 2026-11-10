package android.app;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;

public class Service extends ContextWrapper {
    public Service() { super(null); }
    public void onCreate() {}
    public int onStartCommand(Intent intent, int flags, int startId) { return 0; }
    public void onDestroy() {}
    public IBinder onBind(Intent intent) { return null; }
    public void startForeground(int id, android.app.Notification notification) {}
    public void stopForeground(boolean remove) {}
    public Object getSystemService(String name) { return null; }
}
