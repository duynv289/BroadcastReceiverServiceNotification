package com.example.liz.broadcastreceiverservicenotificationdemo;

import android.annotation.SuppressLint;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;

import java.text.SimpleDateFormat;


public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private ImageButton imageButton_back;
    private ImageButton imageButton_previous;
    private ImageButton imageButton_next;
    private ImageButton imageButton_play;
    private SeekBar mSeekBar;
    private TextView mTextViewSongName;
    private TextView mTextViewCurrentTime;
    private TextView mTextViewTotalTime;


    @SuppressLint("SimpleDateFormat")
    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("mm:ss");

    private ServiceConnection serviceConnection;
    private Intent intent;
    private PlaySongService mPlaySongService;
    private MyPlayer myPlayer;
    private Handler mHandler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        myPlayer = new MyPlayer(MainActivity.this);
        serviceConnection = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
                PlaySongService.BoundService binder = (PlaySongService.BoundService) iBinder;
                mPlaySongService = binder.getService();
            }

            @Override
            public void onServiceDisconnected(ComponentName componentName) {
            }
        };

        if (intent == null) {
            intent = new Intent(MainActivity.this,PlaySongService.class);
            bindService(intent,serviceConnection,BIND_AUTO_CREATE);
            startService(intent);
        }
        imageButton_play.setOnClickListener(this);
        imageButton_next.setOnClickListener(this);
        imageButton_previous.setOnClickListener(this);
        mSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                mTextViewSongName.setText(myPlayer.getSongName());
                mTextViewTotalTime.setText(simpleDateFormat.format(myPlayer.getTimeTotal()));
                mSeekBar.setMax(myPlayer.getTimeTotal());
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                myPlayer.seekTo(mSeekBar.getProgress());
                mTextViewCurrentTime.setText(simpleDateFormat.format(myPlayer.updateTime()));
            }
        });

    }


    private void initView() {
        imageButton_play        = findViewById(R.id.button_play);
        imageButton_back        = findViewById(R.id.button_back);
        imageButton_next        = findViewById(R.id.button_next);
        imageButton_previous    = findViewById(R.id.button_previous);
        mTextViewSongName       = findViewById(R.id.text_SongName);
        mTextViewCurrentTime    = findViewById(R.id.text_CurrentTime);
        mTextViewTotalTime      = findViewById(R.id.text_TotalTime);
        mSeekBar                = findViewById(R.id.seekbar_media);
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onClick(View view) {
        myPlayer.playPauseSong();
        switch (view.getId()){
            case R.id.button_next :
                myPlayer.nextSong();

                break;
            case R.id.button_previous :
                myPlayer.preSong();
                break;
        }
        if(myPlayer.isSongPlaying()){
            imageButton_play.setImageResource(R.drawable.ic_pause_black_24dp);
        }else{
            imageButton_play.setImageResource(R.drawable.ic_play_arrow_black_24dp);
        }
        mPlaySongService.showNotification();
        mPlaySongService.updateNotification(myPlayer.getSongName());

        mHandler = new Handler();
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mTextViewCurrentTime.setText(simpleDateFormat.format(myPlayer.updateTime()));
                mSeekBar.setProgress(myPlayer.updateTime());
                myPlayer.onCompletion();
                mHandler.postDelayed(this,1000);
            }
        },100);

    }

    @Override
    protected void onStop() {
        super.onStop();
        try {
            Thread.sleep(10000);
            unbindService(serviceConnection);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
