package com.benahita.slooz;

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

public class IncomingSmsReceiver extends BroadcastReceiver {

    private static final String ACTION_SNOOZ = "Action_Snooz";
    // Get the object of SmsManager
    final SmsManager sms = SmsManager.getDefault();

    // Notification
    private final String CHANNEL_ID = "sms_notification";

    // Broadcast TAG
    private static final String TAG = "IncomingSmsReceiver";

    public void onReceive(Context context, Intent intent) {

        if (Intent.ACTION_BOOT_COMPLETED.equals(intent.getAction())) {
            StringBuilder sb = new StringBuilder();
            sb.append("Action: " + intent.getAction() + "\n");
            sb.append("URI: " + intent.toUri(Intent.URI_INTENT_SCHEME).toString() + "\n");
            String log = sb.toString();
            Log.d(TAG, log);
            Toast.makeText(context, log, Toast.LENGTH_LONG).show();

            // Start my service
            Intent it = new Intent(context, MyService.class);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                Log.d(TAG, "Service started by the broadcast, Android 8 et plus");
                context.startForegroundService(it);
            }else{
                Log.d(TAG, "Service started by the broadcast, Android 7 et moins");
                context.startService(it);
            }
        }

        // Retrieves a map of extended data from the intent.
        final Bundle bundle = intent.getExtras();

        try {
            if (bundle != null) {
                final Object[] pdusObj = (Object[]) bundle.get("pdus");

                if(!(pdusObj == null)){
                    assert pdusObj != null;
                }

                for (Object o : pdusObj) {

                    SmsMessage currentMessage = SmsMessage.createFromPdu((byte[]) o);

                    String senderNum = currentMessage.getDisplayOriginatingAddress();
                    String message = currentMessage.getDisplayMessageBody();

                    Log.i("SmsReceiver", "senderNum: " + senderNum + "; message: " + message);

                    //*********************** Notification**********************
                    if(senderNum.equals("209") || senderNum.equals("2ToiAMoi") || senderNum.equals("e-recharge")){

                        if(senderNum.equals("e-recharge")){

                            String[] split = message.split("Ar");
                            //String firstSubString = split[0];
                            String valCredit = split[1];
                            String notif_e_recharge = "Bonjour ! Vous venez de recharger "+ valCredit +"Ar votre solde.";

                            int NOTIFICATION_ID = 2;
                            String messageIntegral = "Sloozé pour pouvoir bénéficiera les offres de forfaits.";
                            Intent snoozIntent = new Intent(context, MainActivity.class);// Create an explicit intent for an Activity in your app
                            snoozIntent.setAction(ACTION_SNOOZ);

                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                snoozIntent.putExtra(EXTRA_NOTIFICATION_ID, 0);
                            }

                            PendingIntent snoozPendingIntent = PendingIntent.getActivity(context, 0, snoozIntent, 0);
                            createNotificationChannel(context); // pour Android v8.0 et plus
                            NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID);

                            builder.setPriority(NotificationCompat.PRIORITY_HIGH);
                            builder.setSmallIcon(R.drawable.ic_twotone_perm_phone_msg_24);
                            builder.setContentTitle(notif_e_recharge);
                            builder.setStyle(new NotificationCompat.BigTextStyle().bigText(messageIntegral));
                            builder.addAction(R.drawable.ic_launcher_background, "Sloozer",
                                    snoozPendingIntent); // lorsque l'utilisateur tapera sur le btn snoozer

                            NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(context);
                            notificationManagerCompat.notify(NOTIFICATION_ID, builder.build());

                            //*********************** Lancer l'appli ***************
                            Intent it = new Intent(context, MainActivity.class);
                            it.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            context.startActivity(it);
                        }else {

                            int NOTIFICATION_ID = 2;
                            String messageTitle = "Bonjour !";
                            String messageContentExtrait = "Vous venez de recharger votre solde...";
                            String messageIntegral = "Vous venez de recharger votre compte.\nSloozer pour pouvoir bénéficier les offres de forfaits.";

                            Intent snoozIntent = new Intent(context, MainActivity.class);// Create an explicit intent for an Activity in your app
                            snoozIntent.setAction(ACTION_SNOOZ);

                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                snoozIntent.putExtra(EXTRA_NOTIFICATION_ID, 0);
                            }

                            PendingIntent snoozPendingIntent = PendingIntent.getActivity(context, 0, snoozIntent, 0);
                            createNotificationChannel(context); // pour Android v8.0 et plus

                            NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID);
                            builder.setPriority(NotificationCompat.PRIORITY_HIGH);
                            builder.setSmallIcon(R.drawable.ic_twotone_perm_phone_msg_24);
                            builder.setContentTitle(messageTitle);
                            builder.setContentText(messageContentExtrait);
                            builder.setStyle(new NotificationCompat.BigTextStyle().bigText(messageIntegral));
                            builder.addAction(R.drawable.ic_launcher_background, "Sloozer",
                                    snoozPendingIntent); // lorsque l'utilisateur tapera sur le btn snoozer

                            builder.setContentIntent(snoozPendingIntent); // == 10

                            NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(context);
                            notificationManagerCompat.notify(NOTIFICATION_ID, builder.build());

                            //*********************** Lancer l'appli ***************
                            Intent it = new Intent(context, MainActivity.class);
                            it.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            context.startActivity(it);
                        }
                    }
                } // end for loop
            } // bundle is null

        } catch (Exception e) {
            Log.e("SmsReceiver", "Exception smsReceiver" +e);

        }
    }

    private void createNotificationChannel(Context context)
    {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
        {
            CharSequence name = "Snooz notification";
            String description = "Notification à chaque message de crédit entrant";
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setShowBadge(true); // set false to disable badges, Oreo exclusive
            channel.setDescription(description);
            NotificationManager notificationManager = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
            assert notificationManager != null;
            notificationManager.createNotificationChannel(channel);
        }
    }
}
