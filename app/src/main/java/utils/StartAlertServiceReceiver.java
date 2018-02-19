package utils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import tech.smartcrypto.neeraj.coin.AlertService;
import tech.smartcrypto.neeraj.coin.MainActivity;

/**
 * Created by neerajlajpal on 07/02/18.
 */

public class StartAlertServiceReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        //Log.d("onReceive", "boot up message ********");
        //UtilFunctions.scheduleJob(context);
//        if(!AlertService.isServiceRunning) {
//            Intent service = new Intent(MainActivity.this, AlertService.class);
//            startService(service);
//        }
    }

}

