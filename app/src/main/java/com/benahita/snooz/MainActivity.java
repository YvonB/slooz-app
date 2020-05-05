package com.benahita.snooz;

import android.Manifest;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationManagerCompat;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private static final String sReceiveSmsPerm = Manifest.permission.RECEIVE_SMS;

    // Btn close
    private TextView mCloseBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Btn close
        TextView textViewClose = (TextView) findViewById(R.id.activity_main_close_btn);

        // slide
        ViewPager viewPager = (ViewPager) findViewById(R.id.activity_main_viewpager);
        viewPager.setAdapter(new CustomPagerAdapter(this));

        // Premier ouverture de lapp
        SharedPreferences prefs = getSharedPreferences("prefs", MODE_PRIVATE);
        boolean firstStart = prefs.getBoolean("firstStart", true);

        if (firstStart) {
            firstStartMethode();
        }

        // On clique sur close
        textViewClose.setOnClickListener( new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                closeSnooz(); // En fait, run snooz in background
            }
        });

    }

    // On simule le btn "retour" du telephone lorsque un user ferme Snooz
    private void closeSnooz() {
        onBackPressed();
    }

    // user ferme lapp depuis le btn retour
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        // Supprimer la notif sur le status bar
        int NOTIFICATION_ID = 1;
        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(getApplicationContext());
        notificationManagerCompat.cancel(NOTIFICATION_ID);
    }

    // Résultat de la demande d'autorisation faite au début de la demande
    @Override
    public void onRequestPermissionsResult(
            int requestCode,
            @NonNull final String[] permissions,
            @NonNull final int[] grantResults
    ) {
        if (requestCode == 100) {
            RuntimePermissionUtil.onRequestPermissionsResult(grantResults, new RPResultListener() {
                @Override
                public void onPermissionGranted() {
                    if ( RuntimePermissionUtil.checkPermissonGranted(MainActivity.this, sReceiveSmsPerm)) {
                       runInBackground();
                       readSMS();
                    }
                }

                @Override
                public void onPermissionDenied() {
                    Toast.makeText(MainActivity.this, "L'aplication risque de ne pas fonctionner !", Toast.LENGTH_LONG).show();
                }
            });
        }
    }

    public void firstStartMethode(){
        SharedPreferences prefs = getSharedPreferences("prefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean("firstStart", false);
        editor.apply();

        checkSmsPermision();
        runInBackground();
        readSMS();
        finish();
    }

    private void checkSmsPermision() {
        boolean receiveSmsPermission = RuntimePermissionUtil.checkPermissonGranted(this, sReceiveSmsPerm);

        if (receiveSmsPermission) {
            runInBackground();
            readSMS();
        }else{
            RuntimePermissionUtil.requestPermission(
                    MainActivity.this,
                    sReceiveSmsPerm,
                    100
            );
        }
    }

    public void runInBackground(){
        startService(new Intent(getApplicationContext(), MyService.class));
    }

    public void readSMS(){
        Toast.makeText(MainActivity.this, "Hamaky SMS !", Toast.LENGTH_LONG).show();
    }
}
