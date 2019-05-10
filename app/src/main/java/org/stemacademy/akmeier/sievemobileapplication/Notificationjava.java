package org.stemacademy.akmeier.sievemobileapplication;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;


public class Notificationjava extends BroadcastReceiver {


    @Override
    public void onReceive(Context context, Intent intent) {
        // This is the broadcast receiver, and this is what it does when it receives stuff
        Log.d("onReceive","Success!");
        Intent intent1=new Intent(context, AlarmsIntentService.class);
        context.startService(intent1);
    }
}
