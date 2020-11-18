package com.benahita.slooz;

import android.app.ActivityManager;
import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.os.SystemClock;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

public class MyService extends Service {

    private static final int NOTIF_ID = 3;
    private static final String NOTIF_CHANNEL_ID = "Notification de service de slooz";

    // Constructor
    Context context;
    public MyService(Context applicationContext) {
        super();
        context = applicationContext;
        Log.i("HERE", "here service created!");
    }
    public MyService() {
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId)
    {
        createNotification();
        //return super.onStartCommand(intent, flags, startId);
        return START_STICKY;
    }

    // Au cas ou
    @Override
    public void onDestroy() {
        super.onDestroy();

        Log.i("EXIT", "ondestroy!");
        Intent broadcastIntent = new Intent("ac.in.SloozActivity.RestartService");
        sendBroadcast(broadcastIntent);
    }

    //Restart the service when the app is closed in the recents app lists
    /*@Override
    public void onTaskRemoved(Intent rootIntent) {

        Log.i("onTaskRemoved", "All recent app cleared");

        // For service
        MyService mMyService = new MyService(getApplicationContext());
        Intent mServiceIntent = new Intent(getApplicationContext(), mMyService.getClass());

        if (!isMyServiceRunning(mMyService.getClass())) {

            Log.i("isMyServiceRunning ?", "App removed from the recents app lists, service has died, trying to start our service in 1... 2... 3...");

            Intent restartServiceIntent = new Intent(getApplicationContext(), MyService.class);
            PendingIntent restartServicePendingIntent = PendingIntent.getService(this, 1, restartServiceIntent, PendingIntent.FLAG_ONE_SHOT);
            getApplicationContext().getSystemService(Context.ALARM_SERVICE);
            AlarmManager alarmService = (AlarmManager)getApplicationContext().getSystemService(Context.ALARM_SERVICE);
            alarmService.set(AlarmManager.ELAPSED_REALTIME, SystemClock.elapsedRealtime() + 1000, restartServicePendingIntent);
        }else {
            // Doing nothing, because service is already runnig !
            Log.i(" isMyServiceRunning ?", "App removed from the recents app lists but our service still runnig");
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
    }*/

    private void createNotification()
    {
        createNotificationChannel(getApplicationContext());
        startForeground(NOTIF_ID, new NotificationCompat.Builder(this,
                NOTIF_CHANNEL_ID) // don't forget create a notification channel first
                .setContentTitle("Slooz vous facilite la vie")
                .setSmallIcon(R.drawable.ic_notification)
                .setStyle(new NotificationCompat.BigTextStyle().bigText("Désormais, vous avez tous les codes de forfait mobile existants dans votre téléphone."))
                .build());
        Log.i("startForeground", "Service's notification");
    }

    private void createNotificationChannel(Context context)
    {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
        {
            CharSequence name = "Notification toujours visible";
            String description = "Notification persistant tant que slooz est opérationnel";
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel channel = new NotificationChannel(NOTIF_CHANNEL_ID, name, importance);
            channel.setShowBadge(true); // set false to disable badges, Oreo exclusive
            channel.setDescription(description);
            NotificationManager notificationManager = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
            assert notificationManager != null;
            notificationManager.createNotificationChannel(channel);
        }
    }
}
