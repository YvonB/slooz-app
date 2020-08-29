package com.benahita.slooz;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

public class MyService extends Service {

    private static final int NOTIF_ID = 2;
    private static final String NOTIF_CHANNEL_ID = "Channel_Id";

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId){
        startForeground();
        return super.onStartCommand(intent, flags, startId);
        //return START_REDELIVER_INTENT;
    }

    private void startForeground() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createNotificationChannel(getApplicationContext());
        }
        startForeground(NOTIF_ID, new NotificationCompat.Builder(this,
                NOTIF_CHANNEL_ID) // don't forget create a notification channel first
                .setOngoing(true) // notif not deletable on panel
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentText("Slooz fonctionne en arrière-plan")
                .build());
    }

    private void createNotificationChannel(Context context) {
        // Créer le canal de notification, mais uniquement sur l'API 26+ car
        // la classe NotificationChannel est nouvelle et ne se trouve pas dans la bibliothèque de support
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "Slooz persistant notification";
            String description = "Notification toujours visible";
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel channel = new NotificationChannel(NOTIF_CHANNEL_ID, name, importance);
            channel.setDescription(description);
            // Enregistrez la chaîne dans le système ; vous ne pouvez pas en changer l'importance
            // ou d'autres comportements de notification après cette
            NotificationManager notificationManager = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
            assert notificationManager != null;
            notificationManager.createNotificationChannel(channel);
        }
    }
}
