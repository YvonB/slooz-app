package com.benahita.slooz;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class ServiceRestarterBroadcastReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i(ServiceRestarterBroadcastReceiver.class.getSimpleName(), "Service Stops! Oops!!!!");
        context.startService(new Intent(context, MyService.class));
    }
}
