package com.anoroom.anocontrol;

import com.anoroom.anoserver.Main;
import com.anoroom.dbcontrol.FetchUnit;
import com.anoroom.dbcontrol.StoreUnit;
import com.anoroom.model.AnoMessage;
import com.anoroom.model.AnoRoom;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.websocket.Session;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class AnoService {

//    private static BlockingQueue<AnoMessage> buffer = new ArrayBlockingQueue<>(2048);
    private static final ObjectMapper mapper = new ObjectMapper();
    private static final ConcurrentHashMap<String, AnoRoom> roomList = new ConcurrentHashMap<>();
    private static final Main mainServlet = new Main();
    private final StoreUnit store = new StoreUnit();

    static {
        HashMap<String, String> set = FetchUnit.fetchRoom();
        for (String s:set.keySet()) {
            roomList.put(s, new AnoRoom(set.get(s),s));
        }
    }


    public AnoRoom createRoom(String roomName) {
        AnoRoom room = new AnoRoom(roomName);
        roomList.put(room.getRoomId(), room);
        mainServlet.updateUserList(roomName, room.getUserSet());
        System.out.println(store.sendNewRoom(room));
        return room;
    }

    public AnoRoom findRoomByName(String roomName){
        for (AnoRoom room: roomList.values()){
            if (room.getRoomName().equals(roomName)){
                return room;
            }
        }
        return null;
    }

    public AnoRoom findRoomById (String roomId) {
        return roomList.get(roomId);
    }


    public void update() {
        ArrayList<String[]> a = new ArrayList<>();
        for (AnoRoom r: roomList.values()){
            if (r.isEmpty()) {
                roomList.remove(r.getRoomId());
                store.destroyRoom(r);
            }else{
                Set<String > s= r.getUserSet();
                mainServlet.updateUserList(r.getRoomId(),  s);
//                a.add(r.getRoomName());

                a.add(new String[]{r.getRoomName(), String.valueOf(r.getUserSet().size())});
            }
        }

        mainServlet.updateRoomList(a);

    }

    public void pullClient(Session session, String roomName){
        String senderId = (String) session.getUserProperties().get("senderId");
        AnoRoom room = findRoomByName(roomName);
        AnoMessage anoMsg = null;
        try{
            anoMsg = new AnoMessage();
            anoMsg.setType(AnoMessage.MsgType.OUT);
            anoMsg.setRoomId(room.getRoomId());
            anoMsg.setMsg(senderId.split("-")[0]+" just left the room bye....");
            anoMsg.setSenderId("server");

            String json = mapper.writeValueAsString(anoMsg);
            room.handleClient(session, this);
            room.sendMsgRoom(json);
            update();
        }catch (Exception e){
            e.printStackTrace();
        }
    }


    public  void sendMsgAll(Session session, String msg){
        AnoMessage parsedMsg = null;
        String sendrID = (String) session.getUserProperties().get("senderId");
        try {
            parsedMsg = mapper.readValue(msg, AnoMessage.class);

            AnoMessage.MsgType msgType = parsedMsg.getType();
            String roomId = parsedMsg.getRoomId();
            AnoRoom room = null;
            if (roomId == null) {
                room = findRoomByName(parsedMsg.getRoomName());
            }else{
                room = roomList.get(roomId);

            }
            String vid = sendrID.split("-")[0];
            parsedMsg.setSenderId(sendrID);

            System.out.println(parsedMsg);
            switch (msgType){
                case ENTER:
//                    parsedMsg.setSenderId("Server");
//                    parsedMsg.setMsg(vid+" : Entered say hi!");
                    parsedMsg.setMsg("now i'm entered HI!");
                    parsedMsg.setRoomId(room.getRoomId());
                    break;
                case OUT:
//                    parsedMsg.setSenderId("Server");
//                    parsedMsg.setMsg(vid+" : left the room bye...");
                    parsedMsg.setMsg("i'm leaving this room don't miss me");
                    room.handleClient(session, this);
                    break;
                case MSG:
                    parsedMsg.setSenderId(sendrID);
                    break;
            }
            System.out.println(store.sendMsgLog(parsedMsg));
            System.out.println(parsedMsg);


            String json = mapper.writeValueAsString(parsedMsg);
            room.sendMsgRoom(json);
        }catch (Exception e){
            e.printStackTrace();
        }
    }






}
