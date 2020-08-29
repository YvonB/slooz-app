package com.benahita.slooz;

import android.Manifest;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.ViewPager;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private static final String sReceiveSmsPerm = Manifest.permission.RECEIVE_SMS;

    // Btn close
    private TextView mCloseBtn;

    //Page indicator du slider
    LinearLayout sliderDotspanel;
    private int dotscount;
    private ImageView[] dots;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        runInBackground();
        setContentView(R.layout.activity_main);

        // Btn close
        TextView textViewClose = (TextView) findViewById(R.id.activity_main_close_btn);

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
        exitApp();
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

    // Methode permettant de Fermer l'application avec le bouton Exit
    public void exitApp(){
        // TODO Auto-generated method stub
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(MainActivity.this);
        alertDialogBuilder.setMessage(R.string.exit_dialog_title);
        alertDialogBuilder.setPositiveButton(R.string.positive_btn_exit_dialog,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {

                        Toast.makeText(MainActivity.this, "Merci vous êtes meilleur !", Toast.LENGTH_LONG).show();

                        onBackPressed();
                    }
                });
        alertDialogBuilder.setNegativeButton(R.string.negative_btn_exit_dialog,new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //restartActivity();
            }
        });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }
}
