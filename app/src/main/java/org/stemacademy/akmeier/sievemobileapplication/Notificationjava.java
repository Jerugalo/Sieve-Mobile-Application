package org.stemacademy.akmeier.sievemobileapplication;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;



public class Notificationjava extends BroadcastReceiver {


    @Override
    public void onReceive(Context context, Intent intent) {
        // This is the broadcast receiver, and this is what it does when it recieves stuff

        Intent intent1=new Intent(context, AlarmsIntentService.class);
        context.startService(intent1);
    }
}
