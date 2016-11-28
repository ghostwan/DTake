package com.ghostwan.dtake;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.*;
import android.os.Handler;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.util.Log;
import com.ghostwan.dtake.entity.Take;

public class DTakeService extends Service {

    private static final String TAG = "DTakeService";
    public static final String ACTION_COUNT = "com.ghostwan.dtake.COUNT";

    public static final int OK_COLOR = 0x05c41f;
    private static final int OK_RESOURCE = R.drawable.ic_good_vector;
    public static final int KO_COLOR = 0xf40408;
    private static final int NOTIFY_COLOR = 0x4286f4;
    private static final int KO_RESOURCE = R.drawable.ic_bad_vector;
    private static final int ID = 1234;

    private NotificationManager notificationManager;


    private Handler handler = new Handler();

    private long lastTake = -1;
    private long MINUTE = 60 * 1000;

    private Runnable updateRunnable = new Runnable() {
        @Override
        public void run() {
            updateNotification();
            String prefUpdateTime = sharedPreferences.getString(SettingsActivity.UPDATE_TIME, SettingsActivity.DEFAULT_VALUE_UPDATE_TIME);
            long updateTime = Integer.parseInt(prefUpdateTime) * MINUTE;
            handler.postDelayed(updateRunnable, updateTime);
        }
    };
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private boolean isOkToTake;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.i(TAG, "Service started");

        notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getBaseContext());

        lastTake = sharedPreferences.getLong(SettingsActivity.LAST_TAKE_PREF, -1);

        registerReceiver(takeReceiver, new IntentFilter(ACTION_COUNT));
        IntentFilter intentFilter = new IntentFilter(Intent.ACTION_SCREEN_ON);
        intentFilter.addAction(Intent.ACTION_SCREEN_OFF);
        registerReceiver(screenReceiver, intentFilter);
        updateNotification();
        handler.post(updateRunnable);
    }

    private void updateNotification () {

        String prefTimeLimit = sharedPreferences.getString(SettingsActivity.TIME_LIMIT, SettingsActivity.DEFAULT_VALUE_TIME_LIMIT);
        long timeLimit = Integer.parseInt(prefTimeLimit) * MINUTE;

        long currentTime = System.currentTimeMillis();
        long timeDifference = (currentTime - lastTake);

        isOkToTake = lastTake == -1 || timeDifference > timeLimit;

        String lastTakenString = Util.getDurationBreakdown(timeDifference);
        int count = Take.countTakeToday();
        String countTakenString = getResources().getQuantityString(R.plurals.number_pill_taken,count, count,  Util.getThingPreference(this, true, true));

        Notification.Builder notif = new Notification.Builder(getApplicationContext());
        notif.setSmallIcon(isOkToTake ? OK_RESOURCE : KO_RESOURCE);
        notif.setContentTitle(Util.getThing(this, isOkToTake? R.string.ok_text : R.string.ko_text, countTakenString ));
        if(lastTake != -1)
            notif.setContentText(Util.getThing(this, R.string.last_pill_taken, lastTakenString));
        else
            notif.setContentText(Util.getThing(this, R.string.no_taken));
        notif.setOngoing(true);
        notif.setColor(isOkToTake ? OK_COLOR : KO_COLOR);
        notif.setOnlyAlertOnce(true);
        notif.setStyle(new Notification.MediaStyle());
        Intent i = new Intent(getApplicationContext(), DTakeActivity_.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(),0,i,PendingIntent.FLAG_UPDATE_CURRENT);
        notif.setContentIntent(pendingIntent);
        Intent takeReceive = new Intent();
        takeReceive.setAction(ACTION_COUNT);
        PendingIntent pendingIntentYes = PendingIntent.getBroadcast(this, 12345, takeReceive, PendingIntent.FLAG_UPDATE_CURRENT);
        notif.addAction(R.drawable.ic_add_vector, getString(isOkToTake ? R.string.take : R.string.take_anyway), pendingIntentYes);
        notificationManager.notify(ID, notif.build());
    }

    private void notifyCount() {
        Notification.Builder notif = new Notification.Builder(getApplicationContext());
        notif.setSmallIcon(R.drawable.ic_add_vector);
        notif.setContentTitle(Util.getUpperThing(this, R.string.pill_taken));
        notif.setOngoing(true);
        notif.setColor(NOTIFY_COLOR);
        notif.setOnlyAlertOnce(true);

        notificationManager.notify(ID, notif.build());
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                updateNotification();
            }
        }, 3000);
    }

    private BroadcastReceiver takeReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.i(TAG, "Count + 1");
            Take take = new Take(isOkToTake);
            take.save();
            lastTake = System.currentTimeMillis();
            notifyCount();
            editor = sharedPreferences.edit();
            editor.putLong(SettingsActivity.LAST_TAKE_PREF, lastTake);
            Log.i(TAG, "Save preferences : "+editor.commit());
        }
    };

    private BroadcastReceiver screenReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(Intent.ACTION_SCREEN_OFF)) {
                handler.removeCallbacks(updateRunnable);
            } else if (intent.getAction().equals(Intent.ACTION_SCREEN_ON)) {
                handler.post(updateRunnable);
                updateNotification();
            }

        }
    };

    @Override
    public void onDestroy() {
        super.onDestroy();
        notificationManager.cancel(ID);
        unregisterReceiver(takeReceiver);
        unregisterReceiver(screenReceiver);
    }
}
