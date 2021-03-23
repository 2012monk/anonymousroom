package com.anoroom.dbcontrol;

import com.anoroom.model.AnoMessage;
import com.anoroom.model.AnoRoom;

import javax.websocket.Session;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.Date;

import static com.anoroom.dbcontrol.HandleConnection.getConnect;
import static com.anoroom.dbcontrol.HandleConnection.close;

public class StoreUnit {

    private static final StringBuilder sb = new StringBuilder();


    public int sendMsgLog(AnoMessage msg) {
        Date date = new Date();
        String sql = "INSERT INTO ANO_MESSAGE (ROOM_ID,MSG_TYPE,MSG_BODY, USER_ID,MSG_SEND_DATE)" +
                "VALUES (?,?,?,?,?)";
        Connection conn = getConnect();
        try {
            PreparedStatement prst = conn.prepareStatement(sql);
            prst.setString(1, msg.getRoomId());
            prst.setString(2, msg.getType().name());
            prst.setString(3, msg.getMsg());
            prst.setString(4, msg.getSenderId());
            prst.setDate(5, new java.sql.Date(date.getTime()));
            int cnt = prst.executeUpdate();
            if (cnt>0) conn.commit();
            close(conn);
            close(prst);
            return cnt;
        }catch (Exception e){
            e.printStackTrace();
        }
        return -1;
    }

    public int sendNewRoom(AnoRoom room) {
        String sql = "INSERT INTO ANO_ROOM_ACTIVE (ROOM_ID, ROOM_NAME, ACTIVE) VALUES (?,?,?)";
        Connection conn = getConnect();
        try {
            PreparedStatement prst = conn.prepareStatement(sql);
            prst.setString(1, room.getRoomId());
            prst.setString(2, room.getRoomName());
            prst.setString(3, "T");
            int cnt = prst.executeUpdate();
            if (cnt > 0) conn.commit();
            close(conn);
            close(prst);
            return cnt;
        }catch (Exception e){
            e.printStackTrace();
        }
        return -1;
    }
    public int sendNewRoom(String roomId, String roomName) {
        String sql = "INSERT INTO ANO_ROOM_ACTIVE (ROOM_ID, ROOM_NAME, ACTIVE) VALUES (?,?,?)";
        Connection conn = getConnect();
        try {
            PreparedStatement prst = conn.prepareStatement(sql);
            prst.setString(1, roomId);
            prst.setString(2, roomName);
            prst.setString(3, "T");
            int cnt = prst.executeUpdate();
            if (cnt > 0) conn.commit();
            close(conn);
            close(prst);
            return cnt;
        }catch (Exception e){
            e.printStackTrace();
        }
        return -1;
    }

    public int destroyRoom (AnoRoom room) {
        Date date = new Date();
        String sql = "UPDATE ANO_ROOM_ACTIVE SET ACTIVE='F', DSTR_DATE=? WHERE ROOM_ID='"+room.getRoomId()+"'";
        Connection conn = getConnect();
        try {
            PreparedStatement prst = conn.prepareStatement(sql);
            prst.setDate(1, new java.sql.Date(date.getTime()));
            int cnt = prst.executeUpdate();
            if (cnt > 0) conn.commit();
            close(conn);
            close(prst);
            return cnt;
        }catch (Exception e){
            e.printStackTrace();
        }
        return -1;
    }

    public int destroyRoom (String roomId) {
        Date date = new Date();
        String sql = "UPDATE ANO_ROOM_ACTIVE SET ACTIVE='F', DSTR_DATE=? WHERE ROOM_ID="+roomId;
        Connection conn = getConnect();
        try {
            PreparedStatement prst = conn.prepareStatement(sql);
            prst.setDate(1, new java.sql.Date(date.getTime()));
            int cnt = prst.executeUpdate();
            if (cnt > 0) conn.commit();
            close(conn);
            close(prst);
            return cnt;
        }catch (Exception e){
            e.printStackTrace();
        }
        return -1;
    }

    public int sendUserInfo(Session session) {
        Date date = new Date();
        String sql = "INSERT INTO ANO_USER_DATA (USER_ID, IP, ACT_DATE) VALUES (?,?,?)";
        Connection conn = getConnect();
        try{
            PreparedStatement prst = conn.prepareStatement(sql);
            prst.setString(1,(String) session.getUserProperties().get("senderId"));
            prst.setString(2, "yet implemented");
            prst.setDate(3, new java.sql.Date(date.getTime()));
            int cnt = prst.executeUpdate();
            if (cnt > 0) conn.commit();
            close(conn);
            close(prst);
            return cnt;

        }catch (Exception e){
            e.printStackTrace();
        }
        return -1;
    }

}
