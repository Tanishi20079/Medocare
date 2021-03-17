package com.example.medocare.SetReminder;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;


import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;


import com.example.medocare.R;

import static android.content.Context.NOTIFICATION_SERVICE;


public class AlarmReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context,"rem")
                .setSmallIcon(R.drawable.ic_launcher_background)
                .setContentTitle("Reminder Alert")
                .setContentText("Take your medicine")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);
        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(context);
        notificationManagerCompat.notify(200,builder.build());
    }
}