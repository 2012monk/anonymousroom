package com.anoroom.model;

import java.io.Serializable;
import java.util.Date;
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
    private Date sendDate;

    public AnoMessage(){}

    public AnoMessage(String roomName, String roomId){
        this.roomName = roomName;
        this.roomId = roomId;
        this.sendDate = new Date();
    }

    public AnoMessage(MsgType type, String roomId, String roomName, String msg, String senderId, Date sendDate) {
        this.type = type;
        if (roomId == null | roomId.equals(""))
            this.roomId = UUID.randomUUID().toString();
        else this.roomId = roomId;
        this.roomName = roomName;
        this.msg = msg;
        this.senderId = senderId;
        if (sendDate != null){
            this.sendDate = sendDate;
        }
        this.sendDate = new Date();
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

    public Date getSendDate() {
        return sendDate;
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
}
