package com.haanhgs.jobscheduler;

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
    private final Context context;
    private final NotificationManager manager;

    public Notification(Context context) {
        this.context = context;
        manager = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
    }

    private void createChannel(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O && manager != null){
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, CHANNEL_NAME,
                    NotificationManager.IMPORTANCE_HIGH);
            channel.setDescription("Notification");
            channel.enableVibration(true);
            channel.enableLights(true);
            channel.setLightColor(Color.RED);
            manager.createNotificationChannel(channel);
        }
    }

    private PendingIntent getPendingIntent(){
        Intent intent = new Intent(context, MainActivity.class);
        intent.setAction(Intent.ACTION_MAIN);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addCategory(Intent.CATEGORY_LAUNCHER);
        return PendingIntent.getActivity(context, REQUEST_CODE, intent,
                PendingIntent.FLAG_UPDATE_CURRENT);
    }

    private NotificationCompat.Builder getBuilder(){
        return new NotificationCompat.Builder(context, CHANNEL_ID)
                .setDefaults(NotificationCompat.DEFAULT_ALL)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setAutoCancel(true)
                .setContentIntent(getPendingIntent())
                .setSmallIcon(R.drawable.notif)
                .setContentTitle("Be informed")
                .setContentText("hoho");
    }

    public void createNotification(int id){
        createChannel();
        if (manager != null) manager.notify(id, getBuilder().build());
    }

}
