/***************************************************************
Copyright (c) 2015 swethavs

[This program is licensed under the "MIT License"]

Please see the file COPYING in the source

distribution of this software for license terms.
****************************************************************/


package com.swetha.easypark;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
 
public class NotificationPublisher extends BroadcastReceiver {
 
    public static String NOTIFICATION_ID = "notification-id";
    public static String NOTIFICATION = "notification";
 
    public void onReceive(Context context, Intent intent) {
 
        NotificationManager notificationManager = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
        Log.i("NotificationPublisher","Inside onReceive");
        Notification notification = intent.getParcelableExtra(NOTIFICATION);
        int id = intent.getIntExtra(NOTIFICATION_ID, 0);
        notificationManager.notify(id, notification);
 
    }
}