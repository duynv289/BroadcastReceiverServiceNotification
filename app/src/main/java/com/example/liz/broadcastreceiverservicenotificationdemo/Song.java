package com.example.liz.broadcastreceiverservicenotificationdemo;


public class Song {
    private String name;
    private int directory;

    public Song(String name, int directory) {
        this.name = name;
        this.directory = directory;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getDirectory() {
        return directory;
    }

    public void setDirectory(int directory) {
        this.directory = directory;
    }
}
