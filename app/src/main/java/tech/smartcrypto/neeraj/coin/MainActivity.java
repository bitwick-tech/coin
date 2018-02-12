package tech.smartcrypto.neeraj.coin;

import android.content.ComponentName;
import android.content.Intent;
import android.os.Build;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import utils.UtilFunctions;

public class MainActivity extends AppCompatActivity {
    public static final String EXTRA_MESSAGE = "tech.smartcrypto.neeraj.coin.MESSAGE";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Find the view pager that will allow the user to swipe between fragments
        ViewPager viewPager = findViewById(R.id.viewpager);

        // Create an adapter that knows which fragment should be shown on each page
        SimpleFragmentPagerAdapter adapter = new SimpleFragmentPagerAdapter(this, getSupportFragmentManager());

        // Set the adapter onto the view pager
        viewPager.setAdapter(adapter);

        // Give the TabLayout the ViewPager
        TabLayout tabLayout = findViewById(R.id.sliding_tabs);
        tabLayout.setupWithViewPager(viewPager);

        //TODO start service from here by calling broadcast receiver
        if(!AlertService.isServiceRunning){
            Intent intent = UtilFunctions.handleDeviceManufacturer();
            if  (intent != null) {
                Toast.makeText(getApplicationContext(),"Give permission to not miss any alert for price change.",Toast.LENGTH_LONG).show();
                startActivity(intent);
            }
        }
    }

    @Override
    protected  void onStart(){
        super.onStart();
        if(!AlertService.isServiceRunning) {
            Intent service = new Intent(MainActivity.this, AlertService.class);
            startService(service);
        }
    }

}
