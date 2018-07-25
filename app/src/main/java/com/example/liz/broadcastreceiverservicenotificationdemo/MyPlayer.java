package com.example.liz.broadcastreceiverservicenotificationdemo;

import android.content.Context;
import android.media.MediaPlayer;

import java.util.ArrayList;
import java.util.List;



public class MyPlayer{
    private MediaPlayer mMediaPlayer;
    private List<Song> mArrSong;
    private boolean mState = false;
    private int mPosition = 0;
    private Context mContext;

    public MyPlayer(Context context) {
        this.mContext = context;
        mArrSong = new ArrayList<>();
        mArrSong.add(new Song(String.valueOf(R.string.lip),R.raw.lip));
        mArrSong.add(new Song(String.valueOf(R.string.sainguoisaithoidiem),R.raw.sainguoisaithoidiem));
        mArrSong.add(new Song(String.valueOf(R.string.yeuduong),R.raw.yeuduong));
        createSong();
    }

    public String getSongName(){
        return mArrSong.get(mPosition).getName();
    }

    public void play(){
        if (!mMediaPlayer.isPlaying()) mMediaPlayer.start();
    }
    public void stop(){
        if(mMediaPlayer.isPlaying()) mMediaPlayer.stop();
    }
    public boolean isSongPlaying(){
        return mMediaPlayer != null && mMediaPlayer.isPlaying();
    }

    public void preSong(){
        if(mPosition == 0){
            mPosition = mArrSong.size();
        }
        mPosition--;
        stop();
        createSong();
        play();
    }
    public void nextSong(){
        if(mPosition == mArrSong.size()-1){
            mPosition = -1;
        }
        mPosition++;
        stop();
        createSong();
        play();
    }
    public void playPauseSong() {
        if (!mState) {
            mState = true;
            mMediaPlayer.start();
        } else {
            mState = false;
            mMediaPlayer.pause();
        }
    }
    public void createSong(){
        mMediaPlayer = MediaPlayer.create(mContext, mArrSong.get(mPosition).getDirectory());
    }
    public int getTimeTotal(){
        return mMediaPlayer.getDuration();
    }
    public void seekTo(int position){
        mMediaPlayer.seekTo(position);
    }
    public int updateTime(){
        return mMediaPlayer.getCurrentPosition();
    }
    public void onCompletion(){
        mMediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                nextSong();
                getTimeTotal();
                updateTime();
            }
        });
    }
}
