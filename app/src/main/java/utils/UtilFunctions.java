package utils;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

import tech.smartcrypto.neeraj.coin.Alert;
import tech.smartcrypto.neeraj.coin.Coin;
import tech.smartcrypto.neeraj.coin.MainActivity;
import tech.smartcrypto.neeraj.coin.R;

/**
 * Created by neerajlajpal on 05/02/18.
 */

public class UtilFunctions {

    public static int getResourseId(Context context, String pVariableName, String pResourcename, String pPackageName) throws RuntimeException {
        try {
            return context.getResources().getIdentifier(pVariableName, pResourcename, pPackageName);
        } catch (Exception e) {
            return -1;
            //throw new RuntimeException("Error getting Resource ID.", e);
        }
    }

    public static void addCoinToDb(Coin coin, Context ctx){
        if(ctx == null) return;
        Runnable task = () -> {
            AppDatabase appDatabase = DatabaseHandler.getInstance(ctx);
            appDatabase.coinDao().insertOne(coin);
            updateWatchlistCoinData(ctx);
        };

        //task.run();

        Thread thread = new Thread(task);
        thread.start();
    }

    public static void saveCoinsToWatchlistDB(Coin[] coins, Context ctx) {
        if(ctx == null) return;
        Runnable task = () -> {
            AppDatabase appDatabase = DatabaseHandler.getInstance(ctx);
            appDatabase.coinDao().updateCoins(coins);
        };

        //task.run();

        Thread thread = new Thread(task);
        thread.start();
    }

    public static void updateWatchlistCoinData(Context ctx){
        if(ctx == null) return;
        Runnable task = new Runnable() {
            @Override
            public void run() {
                AppDatabase appDatabase = DatabaseHandler.getInstance(ctx);
                List<Coin> coinList = appDatabase.coinDao().getAllList();
                if (coinList == null || coinList.isEmpty()) return;
                Set<Coin> coinIdSet = new HashSet<>(coinList);
                ServerInteractionHandler.getCoinsDataFromServer(coinIdSet, ctx.getApplicationContext());
            }
        };

        //task.run();

        Thread thread = new Thread(task);
        thread.start();
    }

    public static void updateCoinsInAlertsDB(Coin[] coins, Context ctx) {
        if(ctx == null) return;
        //TODO remove toast
        //*******************
        StringBuilder coinIdsForToast = new StringBuilder();
        for(Coin coin : coins) {
            coinIdsForToast.append("\n");
            coinIdsForToast.append(coin.getId());
        }
        Toast.makeText(ctx, coinIdsForToast,Toast.LENGTH_SHORT).show();

        //*******************
        Runnable task = () -> {
            for(Coin coin: coins) {
                DatabaseHandler.getInstance(ctx).alertDao().updateCoinPrice(coin.getId(), coin.getCurrentPrice());
            }
            List<Alert> alerts = DatabaseHandler.getInstance(ctx).alertDao().getAllList();
            for(Alert alert: alerts) {
                int toTriggerAlert = triggerAlert(alert);
                if(toTriggerAlert > 0) {
                    showNotifAndResetAlertState(alert, toTriggerAlert, ctx);
                }
                handleContAlert(alert);
                updateAlert(alert, ctx);
            }
        };
        Thread thread = new Thread(task);
        thread.start();
    }

    public static int triggerAlert(Alert alert) {
        if(alert.isStatusMax() && alert.getHighPrice() > 0.0f && alert.getCurrentPrice() > alert.getHighPrice())
            return 2;
        else if (alert.isStatusMin() && alert.getLowPrice() > 0.0f && alert.getCurrentPrice() < alert.getLowPrice())
            return 1;
        else
            return 0;
    }

    public static void showNotif(Alert alert, int toTriggerAlert, Context ctx) {
        NotificationManager notificationManager = (NotificationManager) ctx.getSystemService(Context.NOTIFICATION_SERVICE);
        String NOTIFICATION_CHANNEL_ID = "my_channel_id_01";

        String title = "";
        String content = "";
        if(toTriggerAlert == 2) {
            title = alert.getCoinId() + "  above  " + alert.getHighPrice();
        } else if(toTriggerAlert == 1) {
            title = alert.getCoinId() + "  below  " + alert.getLowPrice();
        } else
            return;

        content = "at  " + alert.getCurrentPrice();

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

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(ctx, NOTIFICATION_CHANNEL_ID);
        Intent notificationIntent = new Intent(ctx, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(ctx, 0,
                notificationIntent, 0);
        notificationBuilder.setAutoCancel(false)
                .setDefaults(Notification.DEFAULT_ALL)
                //.setOngoing(true)
                //.setWhen(System.currentTimeMillis())
                .setSmallIcon(R.drawable.bch)
                .setPriority(Notification.PRIORITY_DEFAULT)
                .setContentTitle(title)
                .setContentText(content)
                .setContentInfo("Alert")
                .setAutoCancel(true)
                .setContentIntent(pendingIntent);

        notificationManager.notify(NotificationID.getID(), notificationBuilder.build());
    }

    public static void showNotifAndResetAlertState(Alert alert, int toTriggerAlert, Context ctx) {
        if(toTriggerAlert == 2) {
            alert.setStatusMax(false);
        }
        else if(toTriggerAlert == 1) {
            alert.setStatusMin(false);
        }
        showNotif(alert, toTriggerAlert, ctx);
    }

    public static void handleContAlert(Alert alert) {
        if(!alert.isStatusMax() && !alert.isOneTime() && alert.getCurrentPrice() < alert.getHighPrice())
            alert.setStatusMax(true);

        if(!alert.isStatusMin() && !alert.isOneTime() && alert.getCurrentPrice() > alert.getLowPrice())
            alert.setStatusMin(true);
    }

    public static Alert getAlertFromDB(int id, Context ctx) {
        if(ctx == null) return null;
        // TODO use loadermanager
        return DatabaseHandler.getInstance(ctx).alertDao().loadAById(id);
        //        Runnable task = () -> {
        //            alert =
        //        };
        //        //task.run();
        //        Thread thread = new Thread(task);
        //        thread.start();
        //    }
    }

    public static void addAlert(Alert alert, Context ctx) {
        if(ctx == null) return;
        Runnable task = () -> {
            DatabaseHandler.getInstance(ctx).alertDao().insertOne(alert);
        };
        //task.run();
        Thread thread = new Thread(task);
        thread.start();
    }

    public static void deleteAlert(Alert alert, Context ctx) {
        if(ctx == null) return;
        Runnable task = () -> {
            DatabaseHandler.getInstance(ctx).alertDao().delete(alert);
        };
        //task.run();
        Thread thread = new Thread(task);
        thread.start();
    }

    public static void updateAlert(Alert alert, Context ctx) {
        if(ctx == null) return;
        Runnable task = () -> {
            DatabaseHandler.getInstance(ctx).alertDao().updateAlert(alert);
        };
        //task.run();
        Thread thread = new Thread(task);
        thread.start();

    }

    public static Coin[] convertJSONObjectToCoinsArray(JSONObject resp) {
        JSONArray coinData = resp.optJSONArray("coinData");
        if(coinData != null && coinData.length() > 0) {
            int length = coinData.length();
            Coin[] result = new Coin[length];
            JSONObject coinJSON;
            Coin coin;
            for(int i = 0; i < length; i++) {
                coin = null;
                coinJSON = coinData.optJSONObject(i);
                if(coinJSON != null) {
                            coin = new Coin();
                    try {
                        String id = coinJSON.getString("id");
                        if(id == null) continue;
                        coin.setId(id);
                        coin.setCurrentPrice((float) coinJSON.optDouble("cp"));
                        coin.setCurrency(coinJSON.optString("currency"));
                        coin.setName(coinJSON.optString("name"));
                        coin.setOpenPrice((float) coinJSON.optDouble("op"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                if(coin != null) {
                    result[i] = coin;
                }
            }
            return result;
        }
        return null;
    }

    public static String formatFloatTo4Decimals(float f) {
        //DecimalFormat formatter = new DecimalFormat("#,###,###");

        //f = Float.parseFloat(tmp);
        if(f < 1) {
            String tmp =  String.format(Locale.getDefault(),"%.6f", f);
            return NumberFormat.getNumberInstance(Locale.getDefault()).format(Float.parseFloat(tmp));
        } else if(f < 10) {
            String tmp =  String.format(Locale.getDefault(),"%.4f", f);
            return NumberFormat.getNumberInstance(Locale.getDefault()).format(Float.parseFloat(tmp));
        } else if(f < 1000) {
            String tmp =  String.format(Locale.getDefault(),"%.2f", f);
            return NumberFormat.getNumberInstance(Locale.getDefault()).format(Float.parseFloat(tmp));
        } else {
            String tmp = String.format(Locale.getDefault(), "%.0f", f);
            return NumberFormat.getNumberInstance(Locale.getDefault()).format(Float.parseFloat(tmp));
        }
    }

    public static float deformatStringToFloat(String s) {
        NumberFormat fmt = NumberFormat.getInstance(Locale.getDefault());
        ((DecimalFormat)fmt).setParseBigDecimal(true);

        try {
            return ((BigDecimal)fmt.parse(s)).floatValue();
        } catch (ParseException e) {
            return 0.0f;//
        }
    }

    public static Intent handleDeviceManufacturer() {
        try {
            Intent intent = new Intent();
            String manufacturer = Build.MANUFACTURER;
            String brand = Build.BRAND;
            if ("xiaomi".equalsIgnoreCase(manufacturer) || "xiaomi".equalsIgnoreCase(brand)) {
                intent.setComponent(new ComponentName("com.miui.securitycenter", "com.miui.permcenter.autostart.AutoStartManagementActivity"));
            } else if ("oppo".equalsIgnoreCase(manufacturer) || "oppo".equalsIgnoreCase(brand)) {
                intent.setComponent(new ComponentName("com.coloros.safecenter", "com.coloros.safecenter.permission.startup.StartupAppListActivity"));
            } else if ("vivo".equalsIgnoreCase(manufacturer) || "vivo".equalsIgnoreCase(brand)) {
                intent.setComponent(new ComponentName("com.vivo.permissionmanager", "com.vivo.permissionmanager.activity.BgStartUpManagerActivity"));
            } else {
                intent = null;
            }
            return intent;
        } catch (Exception e) {
            //do nothing
            return null;
        }
    }



    // schedule the start of the service every 10 - 30 seconds
//    public static void scheduleJob(Context context) {
//        ComponentName serviceComponent = new ComponentName(context, AlertService.class);
//        JobInfo.Builder builder = new JobInfo.Builder(0, serviceComponent);
//        builder.setMinimumLatency(1 * 1000); // wait at least
//        builder.setOverrideDeadline(3 * 1000); // maximum delay
//        builder.setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY);
//        //builder.setRequiresDeviceIdle(true); // device should be idle
//        //builder.setRequiresCharging(false); // we don't care if the device is charging or not
//        JobScheduler jobScheduler = context.getSystemService(JobScheduler.class);
//        jobScheduler.schedule(builder.build());
//    }
}
