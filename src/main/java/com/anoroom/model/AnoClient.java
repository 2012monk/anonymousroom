package com.anoroom.model;

import javax.websocket.Session;
import java.io.OutputStream;
import java.util.UUID;

public class AnoClient {

    private final String userId;
    private final Session session;
    private OutputStream out;

    public AnoClient(Session session){
        this.session = session;
        this.userId = UUID.randomUUID().toString();
    }

    public String getUserId() {
        return userId;
    }

    public void sendMsg(String msg){
        try{
            session.getBasicRemote().sendText(msg);
        }catch (Exception e){
            e.printStackTrace();
        }
    }


}
