package com.benahita.sooz;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

public class StartMyServiceOnBootReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        /*if(Intent.ACTION_BOOT_COMPLETED.equals(intent.getAction())){
            Intent i = new Intent(context, MyService.class);
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(i);
        }*/
        Intent i = new Intent(context, MyService.class);

        if(Build.VERSION.SDK_INT  >= Build.VERSION_CODES.O){
            context.startForegroundService(i);
        }else
        {
            context.startService(i);
        }
        Log.i("Dem auto ", "a commencé");;
    }
}
