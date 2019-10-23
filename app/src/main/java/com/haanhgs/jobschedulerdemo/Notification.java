package com.haanhgs.jobschedulerdemo;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import androidx.core.app.NotificationCompat;

public class Notification {

    private static final int REQUEST_CODE = 1979;
    private static final String CHANNEL_ID = "com.haanhgs.jobschedulerdemo_id";
    private static final String CHANNEL_NAME = "com.haanhgs.jobschedulerdemo_id";

    private static void createChannel(NotificationManager manager){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O && manager != null){
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, CHANNEL_NAME,
                    NotificationManager.IMPORTANCE_HIGH);
            channel.setDescription("Notification");
            channel.enableVibration(true);
            channel.enableLights(true);
            channel.setLightColor(Color.GREEN);
            manager.createNotificationChannel(channel);
        }
    }

    private static PendingIntent createIntent(){
        Intent intent = new Intent(App.context(), MainActivity.class );
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setAction(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_LAUNCHER);
        return PendingIntent.getActivity(App.context(), REQUEST_CODE, intent,
                PendingIntent.FLAG_UPDATE_CURRENT);
    }

    private static NotificationCompat.Builder createBuilder(){
        return new NotificationCompat.Builder(App.context(), CHANNEL_ID)
                .setDefaults(NotificationCompat.DEFAULT_ALL)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setAutoCancel(true)
                .setContentIntent(createIntent())
                .setSmallIcon(R.drawable.notif)
                .setContentTitle("Be awared")
                .setContentText("Just forget");
    }

    public static void createNotification(int id){
        NotificationManager manager =
                (NotificationManager)App.context().getSystemService(Context.NOTIFICATION_SERVICE);
        createChannel(manager);
        if (manager != null){
            manager.notify(id, createBuilder().build());
        }
    }
}
