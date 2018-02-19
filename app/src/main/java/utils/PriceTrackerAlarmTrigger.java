package utils;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.PowerManager;
import android.os.SystemClock;
import android.util.Log;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by neerajlajpal on 09/02/18.
 */


public class PriceTrackerAlarmTrigger extends BroadcastReceiver {
    public static int ALARM_TIME_FREQUENCY = 12;

    @Override
    public void onReceive (final Context context, Intent intent) {
        scheduleExactAlarm(context, (AlarmManager)context.getSystemService(Context.ALARM_SERVICE));

        PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
        PowerManager.WakeLock wl = null;
        if (pm != null) {
            wl = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "PriceTrackerWakelock");
        }
        wl.acquire(ALARM_TIME_FREQUENCY*1000L);

        Handler handler = new Handler();
        Runnable periodicUpdate = new Runnable() {
            @Override
            public void run() {
                AppDatabase appDatabase = DatabaseHandler.getInstance(context);
                List<String> coinList = appDatabase.alertDao().getAllCoinIds();
                if (coinList == null || coinList.isEmpty()) return;
                Set<String> coinIdSet = new HashSet<>(coinList);
                ServerInteractionHandler.getCoinsDataFromServerForAlertDB(coinIdSet, context.getApplicationContext());
            }
        };

        handler.post(periodicUpdate);
        wl.release();
    }

    public static void scheduleExactAlarm(Context context, AlarmManager alarms) {
        Intent i=new Intent(context, PriceTrackerAlarmTrigger.class);
        PendingIntent pi= PendingIntent.getBroadcast(context, 0, i, 0);
        alarms.setExact(AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime()+ALARM_TIME_FREQUENCY*1000-SystemClock.elapsedRealtime()%1000, pi);
    }

    public static void cancelAlarm(Context context, AlarmManager alarms) {
        Intent i=new Intent(context, PriceTrackerAlarmTrigger.class);
        PendingIntent pi=PendingIntent.getBroadcast(context, 0, i, 0);
        alarms.cancel(pi);
    }
}

