package com.example.minitiktok.recycler;

public class MessageData {

    String msgName;
    String msgTime;
    String msgContent;
    int imageID;

    public MessageData(String msgName, String msgContent, String msgTime, int imageID) {
        this.msgName = msgName;
        this.msgTime = msgTime;
        this.msgContent = msgContent;
        this.imageID = imageID;
    }
}