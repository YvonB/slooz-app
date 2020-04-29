package com.benahita.sooz;

import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

public class MyService extends Service {

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId){
        Toast.makeText(MyService.this, "Commence en arrière plan !", Toast.LENGTH_LONG).show();

        //return super.onStartCommand(intent, flags, startId);
        return START_REDELIVER_INTENT;
    }

    /*private void startForeground() {
        Intent notificationIntent = new Intent(this, FullscreenActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0,
                notificationIntent, 0);
        startForeground(NOTIF_ID, new NotificationCompat.Builder(this,
                NOTIF_CHANNEL_ID) // don't forget create a notification channel first
                .setOngoing(true)
                .setSmallIcon(R.drawable.ic_launcher_background)
                .setContentTitle(getString(R.string.app_name))
                .setContentText("Snooz fonctionne en arrière-plan")
                .setContentIntent(pendingIntent)
                .build());
    }*/
}
