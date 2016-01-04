package com.myservice.helper;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v4.app.NotificationCompat;

import com.myservice.view.activity.MainActivity;

/**
 * Created by AlexGP on 01/04/2016.
 */
public class NotificationHelper {

    private static int NOTIFICATION_ID = 1;

    public static void showNotification(Context context){
        NotificationManager notificationManager = (NotificationManager)
                context.getSystemService(Context.NOTIFICATION_SERVICE);

        int icon = 0;
        CharSequence tickerText = "MyService";
        CharSequence contentTitle = "MyService em execução.";
        CharSequence contentText = "MyService";

        Notification.Builder builder = null;
        NotificationCompat.Builder supportBuilder = null;
        Notification notification = null;

        Intent notificationIntent = new Intent(context, MainActivity.class);
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        PendingIntent contentIntent = PendingIntent.getActivity(context,
                0, notificationIntent, 0);

        if(Build.VERSION.SDK_INT <= Build.VERSION_CODES.ICE_CREAM_SANDWICH){
            supportBuilder = new NotificationCompat.Builder(context);

            supportBuilder.setContentIntent(contentIntent)
                    .setSmallIcon(icon)
                    .setWhen(System.currentTimeMillis())
                    .setAutoCancel(false)
                    .setContentTitle(contentTitle)
                    .setContentText(contentText)
                    .setAutoCancel(false)
                    .setOngoing(true);

            notification = supportBuilder.build();
        } else {
           builder = new Notification.Builder(context);

           builder.setContentIntent(contentIntent)
                    .setSmallIcon(icon)
                    .setWhen(System.currentTimeMillis())
                    .setAutoCancel(false)
                    .setContentTitle(contentTitle)
                    .setContentText(contentText)
                    .setAutoCancel(false)
                    .setOngoing(true);

           notification = builder.build();
        }

        notificationManager.notify(NOTIFICATION_ID, notification);
    }

    public static void dismissNotification(Context context){
        NotificationManager notificationManager = (NotificationManager)
                context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancel(NOTIFICATION_ID);
    }
}