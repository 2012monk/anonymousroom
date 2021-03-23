package com.anoroom.anoserver;

import com.anoroom.anocontrol.AnoService;
import com.anoroom.model.AnoRoom;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;

@ServerEndpoint("/ano/{roomname}")
public class AnoServer {
    ObjectMapper mapper = new ObjectMapper();
    AnoService service = new AnoService();

    @OnOpen
    public void open(Session session, @PathParam("roomname")String roomName,
                     @PathParam("method")String isCreate){
        AnoRoom room = service.findRoomByName(roomName);
        if (room == null) {
            room = service.createRoom(roomName);
            System.out.println("room created!   "+room.getRoomName());
        }
        room.handleClient(session, service);
//        service.update();
    }



    @OnMessage
    public void message(Session usrSession, String reqMsg) {
        service.sendMsgAll(usrSession, reqMsg);
//        service.update();
    }

    @OnClose
    public void close(Session session, @PathParam("roomname")String roomName) {
        service.pullClient(session, roomName);
    }

    @OnError
    public void err(Throwable t) {
        t.printStackTrace();
    }
}
