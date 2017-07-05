package com.project.tk.o0613_friendlychat.fcm;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.project.tk.o0613_friendlychat.R;
import com.project.tk.o0613_friendlychat.activity.chatroom.ChatRoomActivity;
import com.project.tk.o0613_friendlychat.util.SharedPre;

/**
 * Created by conscious on 2017-06-20.
 */
public class MyFirebaseMessagingService extends FirebaseMessagingService {
    private final static String TAG = "FCM_MESSAGE";

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        if (remoteMessage.getNotification() != null) {
            String body = remoteMessage.getNotification().getBody();
            String tag = remoteMessage.getNotification().getTag();
            String sign = remoteMessage.getData().get("sign");
            Log.d(TAG, "Notification Body: " + body);
        }

        //추가한것
//        sendNotification(remoteMessage.getData().get("message"));
//        String messageBody = remoteMessage.getNotification().getBody();
        sendNotification(remoteMessage);
    }


    private void sendNotification(RemoteMessage remoteMessage) {
        Intent intent = new Intent(this, ChatRoomActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent, PendingIntent.FLAG_UPDATE_CURRENT);

//        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        Uri defaultSoundUri = Uri.parse(SharedPre.getInstance().getString(SharedPre.URI_RINGTONE,null));
//        Uri defaultSoundUri = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.sms_alert);


        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setTicker("ticker")
                .setWhen(System.currentTimeMillis())
                .setNumber(15)
                .setContentTitle(remoteMessage.getNotification().getTitle())
                .setContentText(remoteMessage.getNotification().getBody())
//                .setDefaults(Notification.DEFAULT_SOUND | Notification.DEFAULT_VIBRATE)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);

        NotificationManager nm = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        nm.notify(0 /* ID of notification */, notificationBuilder.build());

    }

}
