package com.anoroom.model;

import java.io.Serializable;
import java.util.UUID;

public class AnoMessage implements Serializable {

    public enum MsgType {
        ENTER, MSG, CREATE, OUT
    }
    private MsgType type;
    private String roomId;
    private String roomName;
    private String msg;
    private String senderId;


    public AnoMessage(){}

    public AnoMessage(String roomName, String roomId){
        this.roomName = roomName;
        this.roomId = roomId;
    }

    public AnoMessage(MsgType type, String roomId, String roomName, String msg, String senderId) {
        this.type = type;
        if (roomId == null | roomId.equals(""))
            this.roomId = UUID.randomUUID().toString();
        else this.roomId = roomId;
        this.roomName = roomName;
        this.msg = msg;
        this.senderId = senderId;
    }

    public void setType(MsgType type) {
        this.type = type;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }
    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }

    @Override
    public String toString() {
        return "AnoMessage{" +
                "type=" + type +
                ", roomId='" + roomId + '\'' +
                ", roomName='" + roomName + '\'' +
                ", msg='" + msg + '\'' +
                ", senderId='" + senderId + '\'' +
                '}';
    }

    public String getRoomId() {
        return roomId;
    }
    public MsgType getType() {
        return type;
    }

    public String getMsg() {
        return msg;
    }

    public String getSenderId() {
        return senderId;
    }

    public String getRoomName() {return roomName;}
}
