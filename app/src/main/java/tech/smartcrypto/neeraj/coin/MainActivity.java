package tech.smartcrypto.neeraj.coin;

import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Intent;
import android.os.Build;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.google.android.gms.ads.MobileAds;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import okhttp3.internal.Util;
import tech.smartcrypto.neeraj.coin.fragments.WatchlistFragment;
import utils.PriceTrackerAlarmTrigger;
import utils.ServerInteractionHandler;
import utils.UtilFunctions;
import utils.VolleyCallback;

public class MainActivity extends AppCompatActivity {
    public static final String EXTRA_MESSAGE = "tech.smartcrypto.neeraj.coin.MESSAGE";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //TODO check version and update if newer version is present
        //get coin static data

        // Find the view pager that will allow the user to swipe between fragments
        ViewPager viewPager = findViewById(R.id.viewpager);

        // Create an adapter that knows which fragment should be shown on each page
        SimpleFragmentPagerAdapter adapter = new SimpleFragmentPagerAdapter(this, getSupportFragmentManager());

        // Set the adapter onto the view pager
        viewPager.setAdapter(adapter);

        // Give the TabLayout the ViewPager
        TabLayout tabLayout = findViewById(R.id.sliding_tabs);
        tabLayout.setupWithViewPager(viewPager);

//        MobileAds.initialize(this, "ca-app-pub-3940256099942544");
        MobileAds.initialize(this, "ca-app-pub-5632531601458324~9378626108");


        //TODO start service from here by calling broadcast receiver
        if(!AlertService.isServiceRunning){
            Intent intent = UtilFunctions.handleDeviceManufacturer(this);
            if  (intent != null) {
                try{
                    startActivity(intent);
                    Toast.makeText(getApplicationContext(),"Give permission to not miss any alert.",Toast.LENGTH_LONG).show();
                }
                catch (ActivityNotFoundException exception) {
                }
            }
        }

        String userCurrency = UtilFunctions.getDataFromSharedPref(this, "defaultCurrency");
        if(userCurrency == null || userCurrency.isEmpty()) {
            Map<String, String> currency = new HashMap<>(1);
            currency.put("defaultCurrency", "inr");
            UtilFunctions.addDataToSharedPref(currency, this);
        }

    }

    @Override
    protected  void onStart(){
        super.onStart();
        if(!AlertService.isServiceRunning) {
            Intent service = new Intent(MainActivity.this, AlertService.class);
            startService(service);
        }

        //get and set alert and watchlist update frequency and static data version handling
        ServerInteractionHandler.getFrequencyDataFromServer(getApplicationContext());
    }

}
