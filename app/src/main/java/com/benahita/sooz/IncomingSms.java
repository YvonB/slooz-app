package com.benahita.sooz;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.util.Log;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import static android.app.Notification.EXTRA_NOTIFICATION_ID;

public class IncomingSms extends BroadcastReceiver {

    // Get the object of SmsManager
    final SmsManager sms = SmsManager.getDefault();

    // Notification
    private final String CHANNEL_ID = "sms_notification";

    public void onReceive(Context context, Intent intent) {

        // Retrieves a map of extended data from the intent.
        final Bundle bundle = intent.getExtras();

        try {
            if (bundle != null) {
                final Object[] pdusObj = (Object[]) bundle.get("pdus");

                assert pdusObj != null;
                for (Object o : pdusObj) {

                    SmsMessage currentMessage = SmsMessage.createFromPdu((byte[]) o);

                    String senderNum = currentMessage.getDisplayOriginatingAddress();
                    String message = currentMessage.getDisplayMessageBody();

                    Log.i("SmsReceiver", "senderNum: " + senderNum + "; message: " + message);

                    //*********************** Notification**********************

                    // Create an explicit intent for an Activity in your app
                    Intent fullScreenIntent = new Intent(context, FullscreenActivity.class);
                    fullScreenIntent.setAction("Action snooz");
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        fullScreenIntent.putExtra(EXTRA_NOTIFICATION_ID, 0);
                    }
                    PendingIntent fullScreenPendingIntent = PendingIntent.getActivity(context, 0, fullScreenIntent, 0);
                    createNotificationChannel(context); // pour Android v8.0 et plus
                    NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID);
                    builder.setSmallIcon(R.mipmap.ic_launcher);
                    builder.setContentTitle("Snooz");
                    builder.setContentText("Nouveau message de crédit entrant.");
                    builder.setPriority(NotificationCompat.PRIORITY_HIGH);
                    // Définir l'intention qui se déclenchera lorsque l'utilisateur tapera sur la notification
                    builder.setContentIntent(fullScreenPendingIntent);
                    builder.addAction(R.drawable.ic_launcher_background, "Snooziava",
                            fullScreenPendingIntent);
                    builder.setAutoCancel(true);


                    NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(context);
                    int NOTIFICATION_ID = 001;
                    notificationManagerCompat.notify(NOTIFICATION_ID, builder.build());


                    //*********************** Lancer l'appli ***************
                    Intent it = new Intent(context, FullscreenActivity.class);
                    it.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(it);

                    // ******************** Show Alert *********************
                    int duration = Toast.LENGTH_LONG;
                    Toast toast = Toast.makeText(context,
                            "Num de l'envoyeur : " + senderNum + ", Contenu du mess : " + message, duration);
                    toast.show();

                } // end for loop
            } // bundle is null

        } catch (Exception e) {
            Log.e("SmsReceiver", "Exception smsReceiver" +e);

        }
    }

    private void createNotificationChannel(Context context) {
        // Créer le canal de notification, mais uniquement sur l'API 26+ car
        // la classe NotificationChannel est nouvelle et ne se trouve pas dans la bibliothèque de support
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "Nom du Canal de notification";
            String description = "Lorem ipsum dolor sit amet";
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            // Enregistrez la chaîne dans le système ; vous ne pouvez pas en changer l'importance
            // ou d'autres comportements de notification après cette
            NotificationManager notificationManager = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
            assert notificationManager != null;
            notificationManager.createNotificationChannel(channel);
        }
    }

}
