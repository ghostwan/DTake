package com.ghostwan.dtake;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class ServiceReceiver extends BroadcastReceiver {

    public static final String ACTION_BOOT_COMPLETED = "android.intent.action.BOOT_COMPLETED";
    public static final String TAG = "ServiceReceiver";

    public ServiceReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {

        String action = intent.getAction();
        switch (action) {
            case ACTION_BOOT_COMPLETED:
                Intent serviceIntent = new Intent(context, DTakeService.class);
                context.startService(serviceIntent);
                break;
        }

    }
}
