package com.benahita.sooz;

import android.Manifest;
import android.annotation.SuppressLint;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationManagerCompat;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

public class FullscreenActivity extends AppCompatActivity {

    private static final String receiveSmsPerm = Manifest.permission.RECEIVE_SMS;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fullscreen);

        // slide
        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
        viewPager.setAdapter(new CustomPagerAdapter(this));

        // Premier ouverture de lapp
        SharedPreferences prefs = getSharedPreferences("prefs", MODE_PRIVATE);
        boolean firstStart = prefs.getBoolean("firstStart", true);

        if (firstStart) {
            firstStartMethode();
        }

    }

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
                    if ( RuntimePermissionUtil.checkPermissonGranted(FullscreenActivity.this, receiveSmsPerm)) {
                       runInBackground();
                       readSMS();
                    }
                }

                @Override
                public void onPermissionDenied() {
                    Toast.makeText(FullscreenActivity.this, "L'aplication risque de ne pas fonctionner !", Toast.LENGTH_LONG).show();
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
        boolean receiveSmsPermission = RuntimePermissionUtil.checkPermissonGranted(this, receiveSmsPerm);

        if (receiveSmsPermission) {
            runInBackground();
            readSMS();
        }else{
            RuntimePermissionUtil.requestPermission(
                    FullscreenActivity.this,
                    receiveSmsPerm,
                    100
            );
        }
    }

    public void runInBackground(){
        startService(new Intent(getApplicationContext(), MyService.class));
    }

    public void readSMS(){
        Toast.makeText(FullscreenActivity.this, "Hamaky SMS !", Toast.LENGTH_LONG).show();
    }



}
