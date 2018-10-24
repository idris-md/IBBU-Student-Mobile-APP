package com.ibbumobile;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

public class SyncService extends Service {
    private static final Object sSyncAdapterLock = new Object();
    private static SyncAdapter sIbbuMobileSyncAdapter = null;

    @Override
    public void onCreate() {
        Log.d("SunshineSyncService", "onCreate - SunshineSyncService");
        synchronized (sSyncAdapterLock) {
            if (sIbbuMobileSyncAdapter == null) {
                sIbbuMobileSyncAdapter = new SyncAdapter(getApplicationContext(), true);
            }
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return sIbbuMobileSyncAdapter.getSyncAdapterBinder();
    }
}