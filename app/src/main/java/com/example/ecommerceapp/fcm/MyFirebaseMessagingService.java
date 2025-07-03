package com.example.ecommerceapp.fcm;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.example.ecommerceapp.MainActivity; // Default activity to open
import com.example.ecommerceapp.R;
import com.example.ecommerceapp.ui.order.OrderDetailActivity; // Example for order-specific notification
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Map;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = "MyFirebaseMsgService";
    public static final String CHANNEL_ID_GENERAL = "general_notifications";
    public static final String CHANNEL_NAME_GENERAL = "General Notifications";

    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        Log.d(TAG, "From: " + remoteMessage.getFrom());

        String title = "ECommerce App"; // Default title
        String body = "You have a new message."; // Default body

        // Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0) {
            Log.d(TAG, "Message data payload: " + remoteMessage.getData());
            // You can process data here. Example: extract specific keys
            // title = remoteMessage.getData().getOrDefault("title", title);
            // body = remoteMessage.getData().getOrDefault("body", body);
            // String orderId = remoteMessage.getData().get("order_id");
            // if (orderId != null) handleOrderNotification(title, body, orderId); else ...
        }

        // Check if message contains a notification payload.
        if (remoteMessage.getNotification() != null) {
            Log.d(TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody());
            title = remoteMessage.getNotification().getTitle() != null ? remoteMessage.getNotification().getTitle() : title;
            body = remoteMessage.getNotification().getBody() != null ? remoteMessage.getNotification().getBody() : body;
        }

        // For simulation, we'll use fixed title/body or data from a direct call
        // In a real scenario, these would come from the RemoteMessage
        sendNotification(title, body, remoteMessage.getData());
    }

    @Override
    public void onNewToken(@NonNull String token) {
        super.onNewToken(token);
        Log.d(TAG, "Refreshed token: " + token);
        // If you implement token management, send it to your app server.
        // sendRegistrationToServer(token);
    }

    /**
     * Create and show a simple notification containing the received FCM message.
     *
     * @param messageTitle FCM message title.
     * @param messageBody FCM message body.
     * @param data FCM data payload.
     */
    public void sendNotification(String messageTitle, String messageBody, Map<String, String> data) {
        Intent intent;
        // Example: Check for an order_id in the data payload to customize intent
        String orderId = data.get("order_id");
        if (orderId != null && !orderId.isEmpty()) {
            intent = new Intent(this, OrderDetailActivity.class);
            intent.putExtra(OrderDetailActivity.EXTRA_ORDER_ID, orderId);
            // Add orderId to intent extras
        } else {
            intent = new Intent(this, MainActivity.class); // Default to MainActivity
        }
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT | PendingIntent.FLAG_IMMUTABLE);

        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(this, CHANNEL_ID_GENERAL)
                        .setSmallIcon(R.mipmap.ic_launcher) // Replace with your app's notification icon
                        .setContentTitle(messageTitle)
                        .setContentText(messageBody)
                        .setAutoCancel(true)
                        .setSound(defaultSoundUri)
                        .setContentIntent(pendingIntent)
                        .setPriority(NotificationCompat.PRIORITY_HIGH); // For heads-up notification

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        // Since android Oreo notification channel is needed.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID_GENERAL,
                    CHANNEL_NAME_GENERAL,
                    NotificationManager.IMPORTANCE_HIGH);
            notificationManager.createNotificationChannel(channel);
        }

        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());
    }

    /**
     * Call this method to simulate receiving a notification.
     * This is for testing purposes as we don't have a real FCM backend.
     */
    public static void simulatePushNotification(Context context, String title, String body, Map<String, String> data) {
        Intent intent;
        String orderId = data.get("order_id");

        if (orderId != null && !orderId.isEmpty()) {
            intent = new Intent(context, OrderDetailActivity.class);
            intent.putExtra(OrderDetailActivity.EXTRA_ORDER_ID, orderId);
        } else {
            intent = new Intent(context, MainActivity.class);
        }
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent,
                PendingIntent.FLAG_ONE_SHOT | PendingIntent.FLAG_IMMUTABLE);

        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(context, CHANNEL_ID_GENERAL)
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setContentTitle(title)
                        .setContentText(body)
                        .setAutoCancel(true)
                        .setSound(defaultSoundUri)
                        .setContentIntent(pendingIntent)
                        .setPriority(NotificationCompat.PRIORITY_HIGH);

        NotificationManager notificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID_GENERAL,
                    CHANNEL_NAME_GENERAL,
                    NotificationManager.IMPORTANCE_HIGH);
            notificationManager.createNotificationChannel(channel);
        }
        notificationManager.notify((int) System.currentTimeMillis(), notificationBuilder.build()); // Unique ID for each test notification
    }
}
