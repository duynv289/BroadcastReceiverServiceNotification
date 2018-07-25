package com.example.liz.broadcastreceiverservicenotificationdemo;

import android.content.Context;
import android.media.MediaPlayer;

import java.util.ArrayList;
import java.util.List;



public class MyPlayer{
    private MediaPlayer mediaPlayer;
    private List<Song> arrSong;
    private boolean STATE = false;
    private int mPosition = 0;
    private Context mContext;

    public MyPlayer(Context context) {
        this.mContext = context;
        arrSong = new ArrayList<>();
        arrSong.add(new Song("L.I.P",R.raw.lip));
        arrSong.add(new Song("Sai Người Sai Thời Điểm",R.raw.sainguoisaithoidiem));
        arrSong.add(new Song("Yêu Đương",R.raw.yeuduong));
        createSong();
    }

    public String getSongName(){
        return arrSong.get(mPosition).getName();
    }

    public void play(){
        if (mediaPlayer.isPlaying() == false) mediaPlayer.start();
    }
    public void stop(){
        if(mediaPlayer.isPlaying()) mediaPlayer.stop();
    }
    public boolean isSongPlaying(){
        return mediaPlayer != null && mediaPlayer.isPlaying();
    }

    public void preSong(){
        if(mPosition == 0){
            mPosition = arrSong.size();
        }
        mPosition--;
        stop();
        createSong();
        play();
    }
    public void nextSong(){
        if(mPosition == arrSong.size()-1){
            mPosition = -1;
        }
        mPosition++;
        stop();
        createSong();
        play();
    }
    public void playPauseSong() {
        if (!STATE) {
            STATE = true;
            mediaPlayer.start();
        } else {
            STATE = false;
            mediaPlayer.pause();
        }
    }
    public void createSong(){
        mediaPlayer = MediaPlayer.create(mContext,arrSong.get(mPosition).getDirectory());
    }
    public int getTimeTotal(){
        return mediaPlayer.getDuration();
    }
    public void seekTo(int position){
        mediaPlayer.seekTo(position);
    }
    public int updateTime(){
        return mediaPlayer.getCurrentPosition();
    }
    public void onCompletion(){
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                nextSong();
                getTimeTotal();
                updateTime();
            }
        });
    }
}
