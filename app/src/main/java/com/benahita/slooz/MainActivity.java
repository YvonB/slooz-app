package com.benahita.slooz;

import android.Manifest;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.ViewPager;

import android.app.ActivityManager;
import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;

public class MainActivity extends AppCompatActivity {

    private static final int NOTIFICATION_ID = 2; //credit's sms notification ID
    private static final int ACTION_MANAGE_OVERLAY_PERMISSION_REQUEST_CODE = 1001;

    //Page indicator du slider
    LinearLayout sliderDotspanel;
    private int dotscount;
    private ImageView[] dots;

    // Defining Permission codes.
    // We can give any value
    // but unique for each permission.
    private static final int RECEIVE_SMS_CODE = 100;

    // Btn close
    private TextView mCloseBtn;

    // Tools for clear notification
    private boolean delNotif = false;

    private boolean isMeilleur = false;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Check permission for SMS
        checkPermission(Manifest.permission.RECEIVE_SMS, RECEIVE_SMS_CODE);
        
        // Check always on top permission
        checkAlwaysOnTopPermission();

        Log.d("mNotif", "On Create !");

        // Btn close
        TextView mCloseBtn = (TextView) findViewById(R.id.activity_main_close_btn);

        // Slogan mi derive tena
        final TextView sloganAppTopTv = (TextView) findViewById(R.id.slogan_app_top_activity_main);
        final TextView sloganAppBottomTv = (TextView) findViewById(R.id.slogan_app_bottom_activity_main);
        sloganAppTopTv.postDelayed(new Runnable() {
            public void run() {
                sloganAppTopTv.setVisibility(View.GONE);
                sloganAppBottomTv.setVisibility(View.GONE);
            }
        }, 8000);

        // =================================== slide ======================
        ViewPager viewPager = findViewById(R.id.activity_main_viewpager);
        viewPager.setAdapter(new CustomPagerAdapter(getSupportFragmentManager()));

        sliderDotspanel = (LinearLayout) findViewById(R.id.activity_main_slider_dots); // Les dots
        dotscount = new CustomPagerAdapter(getSupportFragmentManager()).getCount();

        dots = new ImageView[dotscount];

        for(int i = 0; i < dotscount; i++){

            dots[i] = new ImageView(this);
            dots[i].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.nonactive_dot));

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);

            params.setMargins(8, 0, 8, 0);

            sliderDotspanel.addView(dots[i], params);

        }

        dots[0].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.active_dot));

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

                for(int i = 0; i< dotscount; i++){
                    dots[i].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.nonactive_dot));
                }

                dots[position].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.active_dot));

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        // ================================Fin slide ================================

        // Click on close
        mCloseBtn.setOnClickListener( new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                View view = findViewById(R.id.activity_main_frame_layout);
                String message = "Quitter slooz ?";
                int duration = Snackbar.LENGTH_SHORT;

                showSnackbar(view, message, duration);


            }
        });

    }

    private void checkAlwaysOnTopPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!Settings.canDrawOverlays(this)) {
                Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                        Uri.parse("package:" + getPackageName()));
                startActivityForResult(intent, ACTION_MANAGE_OVERLAY_PERMISSION_REQUEST_CODE);
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("mNotif", "On Resume !");

        delNotif = true;
    }

    private void showSnackbar(View view, String message, int duration) {
        // Create snackbar
        final Snackbar snackbar = Snackbar.make(view, message, duration);

        // Set an action on it, and a handler
        snackbar.setAction("Oui", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isMeilleur = true;
                snackbar.dismiss();
                closeSlooz(); // In fact, run this app in background
            }
        });

        snackbar.show();
    }

    // Function to check and request permission.
    public void checkPermission(String permission, int requestCode)
    {
        if (ContextCompat.checkSelfPermission(MainActivity.this, permission)
                != PackageManager.PERMISSION_GRANTED) {

            // Requesting the permission
            ActivityCompat.requestPermissions(MainActivity.this,
                    new String[] { permission },
                    requestCode);
        }
        else {
            //Permission already granted
            runSloozHeadService();
        }
    }

    // This function is called when the user accepts or decline the permission.
    // Request Code is used to check which permission called this function.
    // This request code is provided when the user is prompt for permission.

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults)
    {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == RECEIVE_SMS_CODE)
        {
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED)
            {
                Log.d("Permission", "SMS permission granted !");

                // First opening of the app
                SharedPreferences prefs = getSharedPreferences("prefs", MODE_PRIVATE);
                boolean firstStart = prefs.getBoolean("firstStart", true);

                if (firstStart) {
                    Log.d("App", "First start");
                    firstStartMethod();
                }else{
                    runSloozHeadService();
                }
            }
            else
                {
                    Log.d("Permission", "SMS permission denied !");
                    // So re requesting the permission
                }
        }
    }

    private void closeSlooz() {
        onBackPressed();
    }

    //Return btn pressed
    @Override
    public void onBackPressed()
    {
        // 1. Close simulation
        super.onBackPressed();

        // 2. clear credit's sms notification
        clearSmsCreditNotification();
    }

    public void clearSmsCreditNotification()
    {
        // Incoming SMS id
        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(getApplicationContext());
        notificationManagerCompat.cancel(NOTIFICATION_ID);
    }

    public void firstStartMethod()
    {
        SharedPreferences prefs = getSharedPreferences("prefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean("firstStart", false);
        editor.apply();

        welcomeMethod();
    }

    public void welcomeMethod()
    {
        makeToast("Bienvenu");

        // start service for the first start
        MyService mMyService = new MyService(getApplicationContext());
        Intent mServiceIntent = new Intent(getApplicationContext(), mMyService.getClass());

        if (!isMyServiceRunning(mMyService.getClass())) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                Log.i ("isMyServiceRunning?", "Starting the service in Android 8.0 et plus");
                startForegroundService(mServiceIntent);
            }else{
                Log.i ("isMyServiceRunning?", "Starting the service in Android < version 7");
                startService(mServiceIntent);
            }
        }else{
            Log.i("isMyServiceRunning?", "Service already runnig");
        }
    }

    public void runSloozHeadService()
    {
        // For service // avec content de vous revoir
        MyService mMyService = new MyService(getApplicationContext());
        Intent mServiceIntent = new Intent(getApplicationContext(), mMyService.getClass());

        if (!isMyServiceRunning(mMyService.getClass())) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                Log.i ("isMyServiceRunning?", "Starting the service in Android 8.0 et plus");
                startForegroundService(mServiceIntent);
                makeToast("Content de vous revoir");
            }else{
                Log.i ("isMyServiceRunning?", "Starting the service in Android < version 7");
                startService(mServiceIntent);
                makeToast("Content de vous revoir");
            }
        }else{
            Log.i("isMyServiceRunning?", "Service already runnig");
        }

    }

    private boolean isMyServiceRunning(Class<? extends MyService> aClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (aClass.getName().equals(service.service.getClassName())) {
                Log.i ("isMyServiceRunning?", true+"");
                return true;
            }
        }
        Log.i ("isMyServiceRunning?", false+"");
        return false;
    }

    // If the app is closed by his x close btn
    // Or the app is closed in the recents app lists
    // Otherwise it is necessary to check the app's setting on battery usage or RAM usage because there may be some restrictions.
    @Override
    protected void onDestroy() {

        Log.i("MainActivity", "On destroy called");

        // For service
        MyService mMyService = new MyService(getApplicationContext());
        Intent mServiceIntent = new Intent(getApplicationContext(), mMyService.getClass());

        if (!isMyServiceRunning(mMyService.getClass())) {

            Log.i("isMyServiceRunning ?", "App closed, service has died, Start the service in 1... 2...3... ");

            Intent restartServiceIntent = new Intent(getApplicationContext(), MyService.class);
            PendingIntent restartServicePendingIntent = PendingIntent.getService(this, 1, restartServiceIntent, PendingIntent.FLAG_ONE_SHOT);
            getApplicationContext().getSystemService(Context.ALARM_SERVICE);
            AlarmManager alarmService = (AlarmManager)getApplicationContext().getSystemService(Context.ALARM_SERVICE);
            alarmService.set(AlarmManager.ELAPSED_REALTIME, SystemClock.elapsedRealtime() + 1000, restartServicePendingIntent);
        }else{
            // Doing nothing, because service is already runnig !
            Log.i("isMyServiceRunning ?", "App closed but our service still runnig");
        }

        // clear Notif raha efa nisokatra le app (premier plan == onResum called)
        if(delNotif){
            clearSmsCreditNotification();
        }

        if(isMeilleur){
            makeToast("Merci, vous êtes le meilleur !");
        }


        super.onDestroy();

    }

    // Tools
    public void makeToast(String message)
    {
        Toast.makeText(MainActivity.this, message, Toast.LENGTH_LONG).show();
    }
}
