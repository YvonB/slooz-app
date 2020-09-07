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
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    //private static final String sReceiveSmsPerm = Manifest.permission.RECEIVE_SMS;

    // Btn close
    private TextView mCloseBtn;

    //Page indicator du slider
    LinearLayout sliderDotspanel;
    private int dotscount;
    private ImageView[] dots;

    // Defining Permission codes.
    // We can give any value
    // but unique for each permission.
    private static final int RECEIVE_SMS_CODE = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Check permission for SMS
        checkPermission(Manifest.permission.RECEIVE_SMS, RECEIVE_SMS_CODE);

        // Suppr les notifs 2 et 3
        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(getApplicationContext());
        notificationManagerCompat.cancel(2);
        notificationManagerCompat.cancel(3);


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
        }, 4000);

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


        // First opening of the app
        SharedPreferences prefs = getSharedPreferences("prefs", MODE_PRIVATE);
        boolean firstStart = prefs.getBoolean("firstStart", true);

        if (firstStart) {
            firstStartMethod();
        }

        // Click on close
        mCloseBtn.setOnClickListener( new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                closeSnooz(); // In fact, run this app in background
            }
        });

    }

    // Function to check and request permission.
    public void checkPermission(String permission, int requestCode)
    {
        if (ContextCompat.checkSelfPermission(MainActivity.this, permission)
                == PackageManager.PERMISSION_DENIED) {

            // Requesting the permission
            ActivityCompat.requestPermissions(MainActivity.this,
                    new String[] { permission },
                    requestCode);
        }
        else {
            makeToast("Permission already granted");
            runInBackground();
        }
    }

    // This function is called when the user accepts or decline the permission.
    // Request Code is used to check which permission called this function.
    // This request code is provided when the user is prompt for permission.

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults)
    {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == RECEIVE_SMS_CODE) {
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                makeToast("SMS Permission Granted");
                runInBackground();
                // suppr not 1
            }
            else {
                makeToast("SMS Permission Denied");
                welcomeNotification(); // == demande de permission
            }
        }
    }

    // On simule le btn "retour" du telephone lorsque un user ferme Snooz
    private void closeSnooz() {
        exitApp();
    }

    // user ferme lapp depuis le btn retour
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        // Supprimer la notif sur le status bar
        int NOTIFICATION_ID = 2;
        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(getApplicationContext());
        notificationManagerCompat.cancel(NOTIFICATION_ID);
    }

    public void firstStartMethod(){
        SharedPreferences prefs = getSharedPreferences("prefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean("firstStart", false);
        editor.apply();

        checkPermission(Manifest.permission.RECEIVE_SMS, RECEIVE_SMS_CODE);
        welcomeNotification(); // == demande de permission
        makeToast("First start");
        finish();
    }

    public void welcomeNotification()
    {
        createNotificationChannel(getApplicationContext());

        Intent appPermIntent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, Uri.fromParts("package", this.getPackageName(), null));
        PendingIntent appPermPendingIntent = PendingIntent.getActivity(this, 0, appPermIntent, 0);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(), "Notification de bienvenue");
        builder.setPriority(NotificationCompat.PRIORITY_HIGH);
        builder.setSmallIcon(R.mipmap.ic_launcher);
        builder.setOngoing(true); // on veut que si l'utilisateur ne veut pas de cette notif, le seul moyen de s'en debarasser sera d'aller dans le sttings des autorisations.
        builder.setContentTitle("Bienvenue !");
        builder.setStyle(new NotificationCompat.BigTextStyle().bigText("Merci d'avoir installé Slooz. Pour fonctionner correctement, il a besoin de certains autorisations. On vous recommande de les activer pour que slooz fonctionne correctement."));
        builder.addAction(R.mipmap.ic_launcher_round, "Accorder les autorisations", appPermPendingIntent);

        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(getApplicationContext());
        notificationManagerCompat.notify(1, builder.build());
    }

    private void createNotificationChannel(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "Notification de bienvenue de slooz app";
            String description = "Notification pour donner des infos et explications au premier lancement de l'app";
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel channel = new NotificationChannel("Notification de bienvenue", name, importance);
            channel.setShowBadge(true); // set false to disable badges, Oreo exclusive
            channel.setDescription(description);
            NotificationManager notificationManager = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
            assert notificationManager != null;
            notificationManager.createNotificationChannel(channel);
        }
    }

    public void runInBackground(){
        startService(new Intent(getApplicationContext(), MyService.class));
    }

    // Method to Close the application with the Exit button
    public void exitApp(){
        // TODO Auto-generated method stub
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(MainActivity.this);
        alertDialogBuilder.setMessage(R.string.exit_dialog_title);
        alertDialogBuilder.setPositiveButton(R.string.positive_btn_exit_dialog,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {

                        makeToast("Merci, vous êtes le meilleur !");

                        onBackPressed();
                    }
                });
        alertDialogBuilder.setNegativeButton(R.string.negative_btn_exit_dialog,new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //do nothing
            }
        });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    // Tools
    public void makeToast(String message){
        Toast.makeText(MainActivity.this, message, Toast.LENGTH_LONG).show();
    }

}
