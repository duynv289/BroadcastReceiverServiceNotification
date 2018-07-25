package com.example.liz.broadcastreceiverservicenotificationdemo;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.Build;
import android.os.IBinder;
import android.support.annotation.RequiresApi;
import android.widget.RemoteViews;

public class PlaySongService extends Service {
    private IBinder mIBinder;
    private MyPlayer myPlayer;
    private Notification mNotification;
    private Notification.Builder mBuilder;

    private static final String PAUSE     = "PAUSE";
    private static final String PREVIOUS  = "PREVIOUS";
    private static final String NEXT      = "NEXT";
    private static int NOTIFICATION_ID    = 1;
    private static int CODE_PRE           = 11;
    private static int CODE_PAUSE         = 12;
    private static int CODE_NEXT          = 13;



    public PlaySongService() {
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onCreate() {
        myPlayer= new MyPlayer(this);
        mIBinder= new BoundService();
        mBuilder = new Notification.Builder(getApplicationContext());
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if(intent != null){
            String action = intent.getAction();
            if(action != null){
                switch (action){
                    case PAUSE:
                        myPlayer.playPauseSong();
                        if(myPlayer.isSongPlaying()){
                            myPlayer.stop();
                        }
                        break;
                    case PREVIOUS:
                        myPlayer.preSong();
                        break;
                    case NEXT:
                        myPlayer.nextSong();
                        break;
                }
            }
        }
        return START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mIBinder;
    }
    @Override
    public boolean onUnbind(Intent intent) {
        myPlayer.stop();
        return super.onUnbind(intent);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
    public class BoundService extends Binder{
        public PlaySongService getService(){
            return PlaySongService.this;
        }
    }
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    public void showNotification() {
        Intent intent;
        PendingIntent pendingIntent;
        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        RemoteViews notificationView = new RemoteViews(getPackageName(), R.layout.notification_layout);

        intent = new Intent(this,MainActivity.class);
        pendingIntent = PendingIntent.getActivity(getApplicationContext(),
                11, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        notificationView.setOnClickPendingIntent(R.id.linear_notification, pendingIntent);

        intent = new Intent(PAUSE);
        pendingIntent = PendingIntent.getActivity(getApplicationContext(),
                CODE_PAUSE, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        notificationView.setOnClickPendingIntent(R.id.button_notify_play, pendingIntent);
        if (myPlayer.isSongPlaying()) {
            notificationView.setImageViewResource(R.id.button_notify_play,
                    R.drawable.ic_play_arrow_black_24dp);
        }else{
            notificationView.setImageViewResource(R.id.button_notify_play,
                    R.drawable.ic_pause_black_24dp);
        }


        intent = new Intent(PREVIOUS);
        pendingIntent = PendingIntent.getActivity(getApplicationContext(),
                CODE_PRE, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        notificationView.setOnClickPendingIntent(R.id.button_notify_previous, pendingIntent);

        intent = new Intent(NEXT);
        pendingIntent = PendingIntent.getActivity(getApplicationContext(),
                CODE_NEXT, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        notificationView.setOnClickPendingIntent(R.id.button_notify_next, pendingIntent);

        mNotification = mBuilder
                .setSmallIcon(R.drawable.logo).setOngoing(true)
                .setWhen(System.currentTimeMillis())
                .setContent(notificationView)
                .setDefaults(Notification.FLAG_NO_CLEAR)
                .build();
        if (notificationManager != null) {
            notificationManager.notify(NOTIFICATION_ID, mNotification);
        }
        startForeground(NOTIFICATION_ID, mNotification);
    }
    public void updateNotification(String songName){
        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        mNotification.contentView.setTextViewText(R.id.text_notifyname, songName);
        if (notificationManager != null) {
            notificationManager.notify(NOTIFICATION_ID, mNotification);
        }
    }
    public void stopNotification(){
        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        if (notificationManager != null) {
            notificationManager.cancel(NOTIFICATION_ID);
        }
    }
}

