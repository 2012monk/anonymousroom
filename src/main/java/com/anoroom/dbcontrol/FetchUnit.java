package com.anoroom.dbcontrol;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;

import static com.anoroom.dbcontrol.HandleConnection.getConnect;
import static com.anoroom.dbcontrol.HandleConnection.close;

public class FetchUnit {

    public static HashMap<String, String> fetchRoom() {
        HashMap<String, String> map = null;
        String sql = "SELECT * FROM ANO_ROOM_ACTIVE WHERE ACTIVE='T' ORDER BY CRT_DATE DESC";
        Connection conn = getConnect();
        PreparedStatement prst = null;
        ResultSet rs = null;
        try {
            map = new HashMap<>();
            prst = conn.prepareStatement(sql);
            rs = prst.executeQuery();
            while (rs.next()){
                map.put(rs.getString("ROOM_ID"),
                        rs.getString("ROOM_NAME"));
            }
            return map;
        }catch (Exception e){
            e.printStackTrace();
        }
        return map;
    }

}
