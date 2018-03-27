package utils;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Build;
import android.preference.PreferenceManager;
import android.support.v4.app.NotificationCompat;

import com.android.volley.VolleyError;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import tech.smartcrypto.neeraj.coin.Alert;
import tech.smartcrypto.neeraj.coin.AlertDao;
import tech.smartcrypto.neeraj.coin.Coin;
import tech.smartcrypto.neeraj.coin.CoinDao;
import tech.smartcrypto.neeraj.coin.CoinStaticData;
import tech.smartcrypto.neeraj.coin.CoinStaticDataDao;
import tech.smartcrypto.neeraj.coin.MainActivity;
import tech.smartcrypto.neeraj.coin.R;

/**
 * Created by neerajlajpal on 05/02/18.
 */

public class UtilFunctions {

//    public static int getResourseId(Context context, String pVariableName, String pResourcename, String pPackageName) throws RuntimeException {
//        try {
//            return context.getResources().getIdentifier(pVariableName, pResourcename, pPackageName);
//        } catch (Exception e) {
//            return -1;
//        }
//    }

//    public static void addCoinToDb(Coin coin, Context ctx){
//        if(ctx == null) return;
//        Runnable task = () -> {
//            AppDatabase appDatabase = DatabaseHandler.getInstance(ctx);
//            appDatabase.coinDao().insertOne(coin);
//            updateWatchlistCoinData(ctx);
//        };
//        Thread thread = new Thread(task);
//        thread.start();
//    }

    public static void saveCoinsToWatchlistDB(Coin[] coins, Context ctx) {
        if(ctx == null || coins == null || coins.length < 1) return;
        Runnable task = () -> {
            AppDatabase appDatabase = DatabaseHandler.getInstance(ctx);
            appDatabase.coinDao().updateCoins(coins);
        };
        Thread thread = new Thread(task);
        thread.start();
    }

    public static void updateWatchlistCoinData(Context ctx) {
        updateWatchlistCoinData(ctx, "");
    }

    public static void updateWatchlistCoinData(Context ctx, String coinId){
        if(ctx == null) return;
        Runnable task = new Runnable() {
            @Override
            public void run() {
                AppDatabase appDatabase = DatabaseHandler.getInstance(ctx);
                List<CoinDao.CoinIdExCurr> coinIdList;
                if(coinId == null || coinId.isEmpty()) {
                    coinIdList = appDatabase.coinDao().getAllIds();
                }
                else {
                    coinIdList = appDatabase.coinDao().getSingleId(coinId);
                }

                if (coinIdList == null || coinIdList.isEmpty()) {
                    return;
                }
                upDateCoinDBFromServerData(coinIdList, ctx.getApplicationContext());
            }
        };
        Thread thread = new Thread(task);
        thread.start();
    }

    public static void upDateCoinDBFromServerData(List<CoinDao.CoinIdExCurr> coinIdList, Context ctx) {
        if(coinIdList == null || coinIdList.size() < 1) return;
        String url = "http://www.smartcrypto.tech/coinprice/?q=";
        StringBuilder stringBuilder = new StringBuilder();
        for(CoinDao.CoinIdExCurr coin: coinIdList) {
            stringBuilder.append(coin.coinId).append("__").append(coin.ex).append("__").append(coin.curr).append(",");
        }
        stringBuilder.deleteCharAt(stringBuilder.length()-1);
        ServerInteractionHandler.getDataFromServer(url + stringBuilder, ctx,
                new VolleyCallback() {
                    @Override
                    public void onSuccessResponse(JSONObject result) {
                        if(ctx != null) {
                            try {
                                Coin[] coinResult = getCoinsDynamicDataFromJson(result);
                                saveCoinsToWatchlistDB(coinResult, ctx);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                    @Override
                    public void onErrorResponse(VolleyError result) {
                        //on error response
                    }
                });
    }

    public static void upDateAlertDBFromServerData(List<AlertDao.CoinIdExCurr> coinIdList, Context ctx) {
        if(coinIdList == null || coinIdList.size() < 1) return;
        String url = "http://www.smartcrypto.tech/coinprice/?q=";
        StringBuilder stringBuilder = new StringBuilder();
        for(AlertDao.CoinIdExCurr coin: coinIdList) {
            stringBuilder.append(coin.coinId).append("__").append(coin.ex).append("__").append(coin.curr).append(",");
        }
        stringBuilder.deleteCharAt(stringBuilder.length()-1);
        ServerInteractionHandler.getDataFromServer(url + stringBuilder, ctx,
                new VolleyCallback() {
                    @Override
                    public void onSuccessResponse(JSONObject result) {
                        if(ctx != null) {
                            try {
                                Coin[] coinResult = getCoinsDynamicDataFromJson(result);

//                                StringBuilder coinIdsForToast = new StringBuilder();
                                for(Coin coin: coinResult) {

//                                    coinIdsForToast.append("\n");
//                                    coinIdsForToast.append(coin.getCId());
                                    DatabaseHandler.getInstance(ctx).alertDao().updateCoinPrice(coin.getCp(), coin.getCId(), coin.getEx(), coin.getCurr());
                                }

//                                Toast.makeText(ctx, coinIdsForToast,Toast.LENGTH_SHORT).show();

                                checkAndTriggerAlerts(ctx);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                    @Override
                    public void onErrorResponse(VolleyError result) {
                        //on error response
                    }
                });
    }

    public static void updateCoinsInAlertsDB(Context ctx) {
        if(ctx == null) return;
        Runnable task = () -> {
            List<AlertDao.CoinIdExCurr> coinDetails = DatabaseHandler.getInstance(ctx).alertDao().getAllCoinIdExCurr();
            upDateAlertDBFromServerData(coinDetails, ctx);
        };
        Thread thread = new Thread(task);
        thread.start();
    }

    public static void checkAndTriggerAlerts(Context ctx) {
        List<Alert> alerts = DatabaseHandler.getInstance(ctx).alertDao().getAllList();
        for(Alert alert: alerts) {
            int toTriggerAlert = triggerAlert(alert);
            if(toTriggerAlert > 0) {
                showNotifAndResetAlertState(alert, toTriggerAlert, ctx);
            }
            handleContAlert(alert);
            updateAlert(alert, ctx);
        }
    }


    public static int triggerAlert(Alert alert) {
        if(alert.isSh() && alert.getHp() > 0.0f && alert.getCp() > alert.getHp())
            return 2;
        else if (alert.isSl() && alert.getLp() > 0.0f && alert.getCp() < alert.getLp())
            return 1;
        else
            return 0;
    }

    public static void showNotif(Alert alert, int toTriggerAlert, Context ctx) {
        NotificationManager notificationManager = (NotificationManager) ctx.getSystemService(Context.NOTIFICATION_SERVICE);
        String NOTIFICATION_CHANNEL_ID = "my_channel_id_01";

        String content;
        if(toTriggerAlert == 2) {
            content = "Above "+ alert.getHp() + " at " + alert.getCp();
        } else if(toTriggerAlert == 1) {
            content = "Below "+ alert.getLp() + " at " + alert.getCp();
        } else
            return;

        String title = DatabaseHandler.getInstance(ctx).coinStaticDataDao().getNameById(alert.getCoinId());

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
                .setSmallIcon(R.mipmap.ic_launcher)
                .setLargeIcon(BitmapFactory.decodeResource(ctx.getResources(),
                        R.mipmap.ic_launcher))
                .setPriority(Notification.PRIORITY_HIGH)
                .setContentTitle(title)
                .setContentText(content)
                .setContentInfo("Alert")
                .setAutoCancel(true)
                .setContentIntent(pendingIntent);

        notificationManager.notify(NotificationID.getID(), notificationBuilder.build());
    }

    public static void showNotifAndResetAlertState(Alert alert, int toTriggerAlert, Context ctx) {
        if(toTriggerAlert == 2) {
            alert.setSh(false);
        }
        else if(toTriggerAlert == 1) {
            alert.setSl(false);
        }
        showNotif(alert, toTriggerAlert, ctx);
    }

    public static void handleContAlert(Alert alert) {
        if(!alert.isSh() && !alert.isOneTime() && alert.getCp() < alert.getHp())
            alert.setSh(true);

        if(!alert.isSl() && !alert.isOneTime() && alert.getCp() > alert.getLp())
            alert.setSl(true);
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
        Thread thread = new Thread(task);
        thread.start();
    }

    public static void deleteAlert(Alert alert, Context ctx) {
        if(ctx == null) return;
        Runnable task = () -> {
            DatabaseHandler.getInstance(ctx).alertDao().delete(alert);
        };
        Thread thread = new Thread(task);
        thread.start();
    }

    public static void deleteCoinById(Context ctx, String coinId) {
        if(ctx == null) return;
        Runnable task = () -> {
            DatabaseHandler.getInstance(ctx).coinDao().deleteByCoinId(coinId);
        };
        Thread thread = new Thread(task);
        thread.start();
    }

    public static void updateAlert(Alert alert, Context ctx) {
        if(ctx == null) return;
        Runnable task = () -> {
            DatabaseHandler.getInstance(ctx).alertDao().updateAlert(alert);
        };
        Thread thread = new Thread(task);
        thread.start();

    }

//    public static Coin[] convertJSONObjectToCoinsArray(JSONObject resp) {
//        JSONArray coinData = resp.optJSONArray("coinData");
//        if(coinData != null && coinData.length() > 0) {
//            int length = coinData.length();
//            Coin[] result = new Coin[length];
//            JSONObject coinJSON;
//            Coin coin;
//            for(int i = 0; i < length; i++) {
//                coin = null;
//                coinJSON = coinData.optJSONObject(i);
//                if(coinJSON != null) {
//                            coin = new Coin();
//                    try {
//                        String id = coinJSON.getString("id");
//                        if(id == null) continue;
////                        coin.setId(id);
////                        coin.setCurrentPrice((float) coinJSON.optDouble("cp"));
////                        coin.setCurrency(coinJSON.optString("currency"));
////                        coin.setName(coinJSON.optString("name"));
////                        coin.setOpenPrice((float) coinJSON.optDouble("op"));
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    }
//                }
//                if(coin != null) {
//                    result[i] = coin;
//                }
//            }
//            return result;
//        }
//        return null;
//    }

    public static String formatFloatTo4Decimals(float f) {
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

    public static String formatFloatTo4DecimalsPercentage(float f) {
        String tmp =  String.format(Locale.getDefault(),"%.2f", f);
        return NumberFormat.getNumberInstance(Locale.getDefault()).format(Float.parseFloat(tmp));
    }

    public static float deformatStringToFloat(String s) {
        NumberFormat fmt = NumberFormat.getInstance(Locale.getDefault());
        ((DecimalFormat)fmt).setParseBigDecimal(true);

        try {
            return ((BigDecimal)fmt.parse(s)).floatValue();
        } catch (ParseException e) {
            return 0.0f;
        }
    }

    public static Intent handleDeviceManufacturer(Context ctx) {
        try {
            String autoRunPermission = getDataFromSharedPref(ctx, "a_r_p");
            if(autoRunPermission != null && !autoRunPermission.isEmpty())
                return null;
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
            addDataToSharedPref("a_r_p", "1", ctx);
            return intent;
        } catch (Exception e) {
            //do nothing
            return null;
        }
    }

    public static void addDataToSharedPref(Map<String, String> map, Context ctx) {
        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(ctx).edit();

        for(Map.Entry<String, String> entry : map.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            if(key == null || key.isEmpty()) continue;
            editor.putString(key, value);
        }
        editor.apply();
    }

    public static void addDataToSharedPref(String key, String value, Context ctx) {
        if(key == null || key.isEmpty()) return;
        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(ctx).edit();
        editor.putString(key, value);
        editor.apply();
    }

    public static String getDataFromSharedPref(Context ctx, String key) {
        return PreferenceManager.getDefaultSharedPreferences(ctx).getString(key, null);
    }

    public static void addCoinsToDB(Coin[] coins, Context ctx) {
        if(ctx == null) return;
        Runnable task = () -> {
            DatabaseHandler.getInstance(ctx).coinDao().insertAll(coins);
        };
        //task.run();
        Thread thread = new Thread(task);
        thread.start();
    }

    public static void addCoinsStaticDataToDB(JSONObject jsonObject, Context ctx) throws JSONException {
        if(ctx == null) return;
        CoinStaticData[] coinStaticData = getCoinStaticDataFromJson(jsonObject);
        if(coinStaticData == null || coinStaticData.length <= 0) return;
        Runnable task = () -> {
            AppDatabase appDatabase = DatabaseHandler.getInstance(ctx);
            appDatabase.coinStaticDataDao().insertAll(coinStaticData);
        };
        Thread thread = new Thread(task);
        thread.start();
    }

    public static CoinStaticData[] getCoinStaticDataFromJson(JSONObject jsonObject) throws JSONException {
        if(jsonObject == null) return null;
        JSONObject coinsData = jsonObject.optJSONObject("coinsData");
        if(coinsData == null || coinsData.length() == 0) return null;
        ArrayList<CoinStaticData> result = new ArrayList<>();
        for (String key : iteratorToIterable(coinsData.keys())) {
            JSONObject coinJson = coinsData.optJSONObject(key);
            if(coinJson != null && coinJson.length() > 0) {
                for(String ex : iteratorToIterable(coinJson.optJSONObject("e").keys())) {
                    JSONArray currencies = coinJson.optJSONObject("e").optJSONArray(ex);
                    if(currencies == null || currencies.length() == 0) continue;
                    for(int i = 0; i < currencies.length(); i++) {
                        String currency = (String) currencies.opt(i);
                        if(currency == null || currency.isEmpty()) continue;
                        CoinStaticData tmp = new CoinStaticData();
                        tmp.setCoinId(key);
                        tmp.setCoinName(coinJson.optString("name"));
                        tmp.setEx(ex);
                        tmp.setCurr(currency);
                        result.add(tmp);
                    }
                }

            }
        }
        return result.toArray(new CoinStaticData[result.size()]);
    }

    public static Coin[] getCoinsDynamicDataFromJson(JSONObject jsonObject) throws JSONException {
        if(jsonObject == null) return null;
        JSONObject coinsData = jsonObject.optJSONObject("coinData");
        if(coinsData == null || coinsData.length() == 0) return null;
        ArrayList<Coin> result = new ArrayList<>();
        for (String key : iteratorToIterable(coinsData.keys())) {
            JSONObject coinJson = coinsData.optJSONObject(key);
            if(coinJson != null && coinJson.length() > 0) {
                for(String ex : iteratorToIterable(coinJson.keys())) {
                    JSONObject exJson = coinJson.optJSONObject(ex);
                    if(exJson != null && exJson.length() > 0) {
                        for(String curr : iteratorToIterable(exJson.keys())) {
                            JSONObject coinDetails = exJson.optJSONObject(curr);
                            if(coinDetails != null && coinDetails.length() > 0) {
                                Coin tmp = new Coin();
                                tmp.setCId(key);
                                tmp.setEx(ex);
                                tmp.setCurr(curr);
                                tmp.setCp((float) coinDetails.optDouble("cp", 0));
                                tmp.setOp((float) coinDetails.optDouble("op", 0));
                                result.add(tmp);
                            }
                        }
                    }


//                    JSONArray currencies = coinJson.optJSONObject("e").optJSONArray(ex);
//                    if(currencies == null || currencies.length() == 0) continue;
//                    for(int i = 0; i < currencies.length(); i++) {
//                        String currency = (String) currencies.opt(i);
//                        if(currency == null || currency.isEmpty()) continue;
//                        CoinStaticData tmp = new CoinStaticData();
//                        tmp.setCoinId(key);
//                        tmp.setCoinName(coinJson.optString("name"));
//                        tmp.setEx(ex);
//                        tmp.setCurr(currency);
//                        result.add(tmp);
//                    }
                }

            }
        }
        return result.toArray(new Coin[result.size()]);
    }

    private static<T> Iterable<T> iteratorToIterable(Iterator<T> iterator) {
        return () -> iterator;
    }

    public static HashMap<String,Coin> convertListToHash(List<Coin> coins) {
        if(coins == null || coins.isEmpty()) {
            return null;
        }
        HashMap<String, Coin> result = new HashMap<>(coins.size());
        for(Coin coin: coins) {
            result.put(coin.getEx(), coin);
        }
        return result;
    }

    public static LinkedHashMap<String, List<Coin>> getCoinsHashFromList(List<Coin> coinList) {
        LinkedHashMap<String, List<Coin>> result = new LinkedHashMap<>();
        if(coinList == null || coinList.size() < 1) return result;
        for(Coin coin: coinList) {
            if(result.get(coin.getCId()) == null) {
                result.put(coin.getCId(), new ArrayList<Coin>());
            }
            result.get(coin.getCId()).add(coin);
        }
        return result;
    }

    public static Map<String, String> getIdNameHash(List<CoinStaticDataDao.CoinIdAndName> list) {
        Map<String, String> result = new HashMap<>(list.size());
        if(list.size() < 1) return result;
        for(CoinStaticDataDao.CoinIdAndName coin: list) {
            result.put(coin.coinId, coin.coinName);
        }
        return result;
    }

    public static List<CoinStaticDataDao.CoinIdAndName> removeAlreadyAddedCoins(Context ctx) {
        List<CoinStaticDataDao.CoinIdAndName> coins = DatabaseHandler.getInstance(ctx).coinStaticDataDao().getAllCoinIdAndNameList();
        List<String> alreadyAddedCoins = DatabaseHandler.getInstance(ctx).coinDao().getAllCoinIds();
        ArrayList<CoinStaticDataDao.CoinIdAndName> result = new ArrayList<>();

        for(CoinStaticDataDao.CoinIdAndName coinIdAndName: coins) {
            boolean found = false;
            for(String coinId: alreadyAddedCoins) {
                if(coinIdAndName.coinId.equalsIgnoreCase(coinId)){
                    found = true;
                    break;
                }
            }
            if(!found) {
                result.add(coinIdAndName);
            }
        }
        return result;
    }

    public static String capitalizeFirstChar(String original) {
        if (original == null || original.length() == 0) {
            return original;
        }
        return original.substring(0, 1).toUpperCase() + original.substring(1);
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
