package com.ngo_request.ngo_request.Firebase_FCM;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.util.Log;


import com.google.firebase.messaging.RemoteMessage;
import com.ngo_request.ngo_request.MainActivity;
import com.ngo_request.ngo_request.R;


public class FirebaseMessagingService extends com.google.firebase.messaging.FirebaseMessagingService {
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        showNotification(remoteMessage.getData().get("message"), remoteMessage.getData().get("ngo_name"),
                remoteMessage.getData().get("contact_number"));

        Log.d(remoteMessage.getData().get("ambulance_name"),remoteMessage.getData().get("contact_number"));


    }

    private void showNotification(String name, String lat, String longi) {


        SharedPreferences.Editor put = getSharedPreferences("REQ", MODE_PRIVATE).edit();
        put.putBoolean("stat", true);
        put.putString("name", lat);
        put.putString("phno", longi);
        put.commit();


        Intent i = new Intent(this, MainActivity.class);

        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        Bundle b = new Bundle();
        b.putString("name", name);
        b.putString("name", lat);
        b.putString("phno", longi);
        b.putInt("stat", 1);

        i.putExtras(b);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, i, PendingIntent.FLAG_UPDATE_CURRENT);


        Bitmap icon = BitmapFactory.decodeResource(getResources(),
                R.mipmap.ic_launcher);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                .setLargeIcon(icon)
                .setSmallIcon(R.drawable.round_white)
                .setAutoCancel(true)
                .setContentTitle(name)
                .setContentIntent(pendingIntent)
                .setSmallIcon(R.drawable.common_google_signin_btn_icon_dark);


        NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        Notification notification = builder.build();
        notification.defaults |= Notification.DEFAULT_SOUND;
        notification.defaults |= Notification.DEFAULT_VIBRATE;
        manager.notify(0, notification);

        manager.notify(0, builder.build());

    }


}