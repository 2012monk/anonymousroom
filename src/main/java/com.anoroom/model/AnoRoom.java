package com.anoroom.model;

import com.anoroom.anocontrol.AnoService;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.websocket.Session;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class AnoRoom {

    private static final ObjectMapper mapper = new ObjectMapper();
    private final String roomId;
    private final String roomName;
    private final Set<Session> memberList;
    private final ConcurrentHashMap<String, Session> list = new ConcurrentHashMap<>();
    private AnoMessage msg;
    private AnoService service;



    public Set<String> getUserSet() {
        Set<String> s = Collections.synchronizedSet(new HashSet<>());
        for (Session ses:memberList){
            s.add((String) ses.getUserProperties().get("senderId"));
        }
        return s;
    }

    public AnoRoom (String roomName){
        this.roomName = roomName;
        this.roomId = UUID.randomUUID().toString();
        msg = new AnoMessage(roomName, roomId);
        this.memberList = new HashSet<>();
    }

//    public AnoRoom (String roomName, AnoService service){
//        this.service = service;
//    }

    public boolean isEmpty() {

        return memberList.isEmpty();
    }



    public void addClient (Session session) {
        memberList.add(session);
    }

    public void handleClient (Session session, AnoService service) {
        if (memberList.contains(session)) memberList.remove(session);
        else {
            try {
                String senderID = UUID.randomUUID().toString();
                session.getUserProperties().put("senderId", senderID);
//                msg.setSenderId(senderID);
//                String json = mapper.writeValueAsString(msg);
//                session.getBasicRemote().sendText(json);


                memberList.add(session);
                System.out.println(senderID + "client Add" + getRoomName());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public String getMemberList() {
        StringBuilder sb = new StringBuilder();
        for (Session s:memberList){
            sb.append(s.getUserProperties().get("senderId"));
        }
        return sb.toString();
    }

    public String getRoomId() {
        return roomId;
    }

    public String getRoomName() {
        return roomName;
    }

    public void sendMsgRoom (String reqMsg) {
        for (Session a: memberList){
            try {
                a.getBasicRemote().sendText(reqMsg);
            }catch (Exception e){};
        }
    }


}
