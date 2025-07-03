package com.example.ecommerceapp;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;

import com.example.ecommerceapp.fcm.MyFirebaseMessagingService;

public class ECommerceApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        createNotificationChannels();
    }

    private void createNotificationChannels() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // General Notifications Channel
            NotificationChannel generalChannel = new NotificationChannel(
                    MyFirebaseMessagingService.CHANNEL_ID_GENERAL,
                    MyFirebaseMessagingService.CHANNEL_NAME_GENERAL,
                    NotificationManager.IMPORTANCE_HIGH
            );
            generalChannel.setDescription("General app notifications, promotions, and updates.");
            // You can set other channel properties here like sound, vibration, lights etc.
            // generalChannel.enableLights(true);
            // generalChannel.setLightColor(Color.RED);
            // generalChannel.enableVibration(true);
            // generalChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});


            // Example: Order Updates Channel (if you want separate channels)
            // NotificationChannel orderChannel = new NotificationChannel(
            //         "order_updates_channel",
            //         "Order Updates",
            //         NotificationManager.IMPORTANCE_DEFAULT
            // );
            // orderChannel.setDescription("Notifications about your order status.");

            NotificationManager manager = getSystemService(NotificationManager.class);
            if (manager != null) {
                manager.createNotificationChannel(generalChannel);
                // manager.createNotificationChannel(orderChannel);
            }
        }
    }
}
