package com.webview.space.notification;

import android.app.Notification;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.onesignal.notifications.IActionButton;
import com.onesignal.notifications.IDisplayableMutableNotification;
import com.onesignal.notifications.INotificationReceivedEvent;
import com.onesignal.notifications.INotificationServiceExtension;
import com.webview.space.R;
import com.webview.space.room.AppDatabase;
import com.webview.space.room.DAO;
import com.webview.space.room.table.NotificationEntity;

@SuppressWarnings("unused")
public class OneSignalServiceExtension implements INotificationServiceExtension {

    @Override
    public void onNotificationReceived(INotificationReceivedEvent event) {
        Context context = event.getContext();
        IDisplayableMutableNotification notification = event.getNotification();

        if (notification.getActionButtons() != null) {
            for (IActionButton button : notification.getActionButtons()) {
                // you can modify your action buttons here
            }
        }
        
        saveToDatabase(context, notification);

        // this is an example of how to modify the notification by changing the background color to blue
        NotificationCompat.Extender extender = new NotificationCompat.Extender() {
            @NonNull
            @Override
            public NotificationCompat.Builder extend(@NonNull NotificationCompat.Builder builder) {
                builder.setColor(context.getResources().getColor(R.color.colorPrimary));
                builder.setSmallIcon(R.drawable.ic_stat_onesignal_default);
                builder.setDefaults(Notification.DEFAULT_LIGHTS);
                builder.setAutoCancel(true);
                return builder;
            }
        };
        notification.setExtender(extender);
    }

    private static void saveToDatabase(Context context, IDisplayableMutableNotification notification) {
        DAO dao = AppDatabase.getDb(context).get();
        NotificationEntity notificationEntity = dao.getNotification(notification.getSentTime());
        if (notificationEntity == null) notificationEntity = new NotificationEntity();
        notificationEntity.id = notification.getSentTime();
        notificationEntity.title = notification.getTitle();
        notificationEntity.content = notification.getBody();
        notificationEntity.image = notification.getBigPicture();
        notificationEntity.link = notification.getLaunchURL();
        notificationEntity.created_at = System.currentTimeMillis();
        dao.insertNotification(notificationEntity);
    }

}
