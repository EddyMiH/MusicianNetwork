package com.musapp.musicapp.firebase_messaging_notifications;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.app.NotificationCompat;
import android.util.Log;


import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.musapp.musicapp.R;
import com.musapp.musicapp.activities.AppMainActivity;
import com.musapp.musicapp.currentinformation.CurrentUser;
import com.musapp.musicapp.firebase.DBAccess;
import com.musapp.musicapp.firebase_repository.FirebaseRepository;
import com.musapp.musicapp.model.User;
import com.musapp.musicapp.preferences.RememberPreferences;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;


public class MyFirebaseMessagingService extends FirebaseMessagingService {
/*
    @Override
    public void onNewToken(String s) {
        DBAccess.setToken(s);
        CurrentUser.getCurrentUser().setToken(s);
        FirebaseRepository.updateCurrentUserToken(s);
        Log.d("Nottttt","Token ["+s+"]");

        sendRegistrationToServer(s);
    }




    private void sendRegistrationToServer(String token) {
        // TODO: Implement this method to send token to your app server.

        Intent intent = new Intent(this, AppMainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 1410 , intent,
                PendingIntent.FLAG_ONE_SHOT);

        Uri defaultSoundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.ic_launcher_background)
                .setContentTitle("FCM Message")
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(1410 , notificationBuilder.build());
    }*/

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);



        loadNotificationToFirebase(remoteMessage.getData().get("commenterId"), remoteMessage);


    }

    private void loadNotificationToFirebase(final String userPrimaryKey, final RemoteMessage remoteMessage){
        Map<String, String> data = remoteMessage.getData();
        final com.musapp.musicapp.model.Notification pushedNotification;
        pushedNotification = new com.musapp.musicapp.model.Notification(data.get("commenterId"), data.get("postId"),
                data.get("body"), data.get("date"), data.get("commenterImageUrl"), data.get("title") );


       FirebaseRepository.getUserByPrimaryKey(userPrimaryKey, new ValueEventListener() {
           @Override
           public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
               List<com.musapp.musicapp.model.Notification> notifications = new ArrayList<> ();
               if(dataSnapshot.child("notifications").getValue() != null)
                  notifications.addAll((ArrayList<com.musapp.musicapp.model.Notification>)dataSnapshot.child("notifications").getValue());
               notifications.add(pushedNotification);
               FirebaseRepository.updateUserNotificationListById(userPrimaryKey, notifications, new OnSuccessListener<Void>() {
                   @Override
                   public void onSuccess(Void aVoid) {
                       createNotification(remoteMessage);
                   }
               });

           }

           @Override
           public void onCancelled(@NonNull DatabaseError databaseError) {

           }
       });
    }

    private void createNotification(RemoteMessage remoteMessage){

        if(!remoteMessage.getData().get("tag").equals(RememberPreferences.getUser(this))){
            return;
        }

        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);


        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, "technoWeb")
                .setSmallIcon(R.drawable.ic_launcher_background)
                .setContentTitle(remoteMessage.getData().get("title") + " has commented your post")
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher_background))
                .setContentText(remoteMessage.getData().get("body"))
                .setAutoCancel(true)
                .setColor(0xffff7700)
                .setVibrate(new long[]{100, 100, 100, 100})
                .setPriority(Notification.PRIORITY_MAX)
                .setSound(defaultSoundUri);
        Intent resultIntent = new Intent(this, AppMainActivity.class);


        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        stackBuilder.addParentStack(AppMainActivity.class);
        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent =
                stackBuilder.getPendingIntent(
                        0,
                        PendingIntent.FLAG_ONE_SHOT
                );


        notificationBuilder.setContentIntent(resultPendingIntent);
        NotificationManager mNotificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);


        mNotificationManager.notify(new Random().nextInt(1000), notificationBuilder.build());
    }




}
