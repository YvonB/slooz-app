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

    // Tools for clear notification
    private boolean delNotif = false;

    // Tools for vous êtes le meilleur toast
    private boolean isMeilleur = false;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        Log.d("mNotif", "On Create !");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // pour les versions V 6.0 et plus, // Pour les V 5.x midina permissions auto accordées
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            // Check permission for SMS
            checkPermission(Manifest.permission.RECEIVE_SMS, RECEIVE_SMS_CODE);

            // Check always on top permission
            checkAlwaysOnTopPermission();
        }else{
            // Quand est-ce qu'on affiche le Message "Content de vous revoir ??"
            Intent launchIntent = getApplication().getPackageManager().getLaunchIntentForPackage(getApplication().getPackageName());
            assert launchIntent != null;
            Log.d("ACTION_LAUNCHER", String.valueOf(launchIntent.getAction()));
            if((String.valueOf(launchIntent.getAction()).equals("android.intent.action.MAIN"))){makeToast("Content de vous revoir");};
        }

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
                String message = "Quiter ?";
                int duration = Snackbar.LENGTH_SHORT;

                showSnackbar(view, message, duration);
            }
        });

    }

    private void checkAlwaysOnTopPermission()
    {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!Settings.canDrawOverlays(this)) {
                Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                        Uri.parse("package:" + getPackageName()));
                startActivityForResult(intent, ACTION_MANAGE_OVERLAY_PERMISSION_REQUEST_CODE);

                makeToast("Ce serai plus fun que vous accepter cette authorisation");
            }
        }
    }

    // Check Overlay permission for the result
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == ACTION_MANAGE_OVERLAY_PERMISSION_REQUEST_CODE) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (Settings.canDrawOverlays(this)) {
                    // permission granted...
                    Log.d("DrawOverApps", "permission granted");
                } else {
                    // permission not granted...
                    Log.d("DrawOverApps", "permission not granted");
                }
            }
        }else{
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("mNotif", "On Resume !");

        delNotif = true;

        // pour les versions V 6.0 et plus, // Pour les V 5.x midina permissions auto accordées
        /*if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            // Check permission for SMS
            if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.RECEIVE_SMS)
                    != PackageManager.PERMISSION_GRANTED) {
                makeToast("Cette authorisation est capitale pour Slooz, veuillez accepter s'il vous plait");
                // Requesting the permission
                ActivityCompat.requestPermissions(MainActivity.this,
                        new String[] { Manifest.permission.RECEIVE_SMS },
                        RECEIVE_SMS_CODE);
            }

            // Check always on top permission
            //checkAlwaysOnTopPermission();
        }*/
    }

    /*
    @Override
    protected void onRestart() {

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            // Check permission for SMS
            if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.RECEIVE_SMS)
                    != PackageManager.PERMISSION_GRANTED) {
                //makeToast("Cette authorisation est capitale pour Slooz, veuillez accepter s'il vous plait");
                // Requesting the permission
                ActivityCompat.requestPermissions(MainActivity.this,
                        new String[] { Manifest.permission.RECEIVE_SMS },
                        RECEIVE_SMS_CODE);
            }

            // Check always on top permission
            //checkAlwaysOnTopPermission();
        }

        super.onRestart();
    } */

    private void showSnackbar(View view, String message, int duration)
    {
        // Create snackbar
        final Snackbar snackbar = Snackbar.make(view, message, duration);

        // Set an action on it, and a handler
        snackbar.setAction("Oui", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isMeilleur = true;
                snackbar.dismiss();
                finish(); // CLOSE SLOOZ
                overridePendingTransition(0, 0);
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

            // Quand est-ce qu'on affiche le Message "Content de vous revoir ??"
            Intent launchIntent = getApplication().getPackageManager().getLaunchIntentForPackage(getApplication().getPackageName());
            assert launchIntent != null;
            Log.d("ACTION_LAUNCHER", String.valueOf(launchIntent.getAction()));
            if((String.valueOf(launchIntent.getAction()).equals("android.intent.action.MAIN"))){makeToast("Content de vous revoir");};
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

                // Vars utils fo first opening of the app
                SharedPreferences prefs = getSharedPreferences("prefs", MODE_PRIVATE);
                boolean firstStart = prefs.getBoolean("firstStart", true);

                if (firstStart) {
                    Log.d("AppFirst", "First start");
                    doStuffOnFirstStart();
                }else{
                    Log.d("AppFirst", "NOT First start");
                    runSloozHeadService();
                }
            }
            else
                {
                    Log.d("Permission", "SMS permission denied !");

                    // Explain to the user that the feature is unavailable because
                    // the features requires a permission that the user has denied
                    View view = findViewById(R.id.activity_main_frame_layout);
                    String message = "Slooz ne fonctionnera pas sans cette authorisation";
                    String btn = "Paramètres";
                    int duration = 5000;

                    makeSanckbar(view, message, btn, duration);
                }
        }
    }

    //Return btn pressed
    @Override
    public void onBackPressed()
    {
        // 1. Trigger close btn click
        TextView mCloseBtn = (TextView) findViewById(R.id.activity_main_close_btn);
        mCloseBtn.performClick();

        // 2. clear credit's sms notification
        clearSmsCreditNotification();

    }

    public void clearSmsCreditNotification()
    {
        // Incoming SMS id
        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(getApplicationContext());
        notificationManagerCompat.cancel(NOTIFICATION_ID);
    }

    public void doStuffOnFirstStart()
    {
        SharedPreferences prefs = getSharedPreferences("prefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean("firstStart", false);
        editor.apply();

        checkIfMyServiceIsRunnigOrNot(); // start service for the first start
        welcomeMessage(); // Puis bienvenu
    }

    private void checkIfMyServiceIsRunnigOrNot()
    {
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

    public void welcomeMessage() { makeToast("Bienvenu"); }

    public void runSloozHeadService()
    {
        // For service // avec content de vous revoir
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

        // Alarm for restart service
        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.RECEIVE_SMS)
                == PackageManager.PERMISSION_GRANTED) {
            MyService mMyService = new MyService(getApplicationContext());
            //Intent mServiceIntent = new Intent(getApplicationContext(), mMyService.getClass());

            if (!isMyServiceRunning(mMyService.getClass())) {

                Log.i("isMyServiceRunning ?", "App closed, service has died, Start the service in 1... 2...3... ");

                Intent restartServiceIntent = new Intent(getApplicationContext(), MyService.class);
                PendingIntent restartServicePendingIntent = PendingIntent.getService(this, 1, restartServiceIntent, PendingIntent.FLAG_ONE_SHOT);
                getApplicationContext().getSystemService(Context.ALARM_SERVICE);
                AlarmManager alarmService = (AlarmManager)getApplicationContext().getSystemService(Context.ALARM_SERVICE);
                alarmService.set(AlarmManager.ELAPSED_REALTIME, SystemClock.elapsedRealtime() + 1000, restartServicePendingIntent);
        }

        }else{
            // Doing nothing, because service is already runnig !
            Log.i("isMyServiceRunning ?", "App closed but our service still runnig");
        }

        // clear Notif raha efa nisokatra le app (premier plan == onResum called)
        if(delNotif){clearSmsCreditNotification();}

        if(isMeilleur && (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.RECEIVE_SMS)
                == PackageManager.PERMISSION_GRANTED)){makeToast("Merci, vous êtes le meilleur !");}

        super.onDestroy();
    }

    // Toast maker
    public void makeToast(String message)
    {
        Toast.makeText(MainActivity.this, message, Toast.LENGTH_LONG).show();
    }

    // Snackbar maker
    public void makeSanckbar(View view, String message, String btn, int duration)
    {
        // Create snackbar
        final Snackbar snackbar = Snackbar.make(view, message, duration);

        // Set an action on it, and a handler
        snackbar.setAction(btn, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                snackbar.dismiss();
               // Lien vers paramètre pour les permissions
            }
        });

        snackbar.show();
    }
}
