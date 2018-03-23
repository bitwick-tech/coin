package tech.smartcrypto.neeraj.coin;

import android.app.AlarmManager;
import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import utils.PriceTrackerAlarmTrigger;

import static utils.PriceTrackerAlarmTrigger.scheduleExactAlarm;

public class AlertService extends Service {
    public static boolean isServiceRunning = false;
    private static final String LOG_TAG = "ForegroundService";
    private Intent myIntent;

    private NotificationManager mNotificationManager;
    private static final int NOTIFICATION_ID = 2310;

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
        //Log.d(LOG_TAG, "foreground service started ********");
        myIntent = intent;
        isServiceRunning = true;
        scheduleExactAlarm(this, (AlarmManager)this.getSystemService(Context.ALARM_SERVICE));
        showNotification();
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        isServiceRunning = false;
        Log.i(LOG_TAG, "In onDestroy");
        PriceTrackerAlarmTrigger.cancelAlarm(this, (AlarmManager)this.getSystemService(Context.ALARM_SERVICE));
    }

    @Override
    public IBinder onBind(Intent intent) {
        // Used only in case of bound services.
        return null;
    }

    private void showNotification() {
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        String NOTIFICATION_CHANNEL_ID = "my_channel_id_01";

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, "My Notifications", NotificationManager.IMPORTANCE_HIGH);

            // Configure the notification channel.
            notificationChannel.setDescription("Channel description");
            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.RED);
            notificationChannel.setVibrationPattern(new long[]{0, 1000, 500, 1000});
            notificationChannel.enableVibration(true);
            notificationManager.createNotificationChannel(notificationChannel);
        }

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID);
        Intent notificationIntent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0,
                notificationIntent, 0);
        notificationBuilder.setAutoCancel(false)
                .setDefaults(Notification.DEFAULT_ALL)
                .setOngoing(true)
                //.setWhen(System.currentTimeMillis())
                .setSmallIcon(R.mipmap.ic_launcher)
                .setLargeIcon(BitmapFactory.decodeResource(this.getResources(),
                        R.mipmap.ic_launcher))
                .setPriority(Notification.PRIORITY_MAX)
                .setContentTitle(getResources().getString(R.string.app_name))
                .setContentText("Real-time alerts service")
                .setContentInfo("Info")
                .setContentIntent(pendingIntent);

        startForeground(NOTIFICATION_ID, notificationBuilder.build());

        //notificationManager.notify(/*notification id*/1, notificationBuilder.build());
    }


}