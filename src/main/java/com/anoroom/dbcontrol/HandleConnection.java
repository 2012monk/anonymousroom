package com.anoroom.dbcontrol;

import com.anoroom.model.AnoMessage;
import com.anoroom.model.AnoRoom;

import java.sql.*;
import java.util.Arrays;
import java.util.Collections;
import java.util.StringJoiner;
import java.util.UUID;
import java.util.stream.Collector;

public class HandleConnection {
    private static final String _url;
    private static final String usr;
    private static final String pw;
    static {
        _url = "jdbc:oracle:thin:@localhost:1521:XE";
        usr = "anoadmin";
        pw = "a1234";
        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");
        }catch (ClassNotFoundException e){
            e.printStackTrace();
        }
    }


    public static Connection getConnect() {
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(_url, usr, pw);
            conn.setAutoCommit(false);
        }catch (SQLException e){
            e.printStackTrace();
        }
        return conn;
    }

    public static int insertQuery (String table, String[] args, String[] value) {
        int cnt = 0;
        StringBuilder sql = new StringBuilder();
        StringJoiner sj = new StringJoiner(",","(",")");
        StringJoiner ssj = new StringJoiner(",","(",")");
        for (String s:value){
//            sj.add(s);
            sj.add("?");
        }
        for (String s:args) {
            ssj.add(s);
        }
        sql.append("INSERT INTO ");
        sql.append(table).append(ssj.toString()).append(" ");
        sql.append("VALUES ").append(sj.toString()).append(" ");
        System.out.println(sql.toString()+ Arrays.toString(value));

        Connection conn = getConnect();
        try {
            PreparedStatement prst = conn.prepareStatement(sql.toString());
            for (int i=0;i<value.length;i++){
                prst.setString(i+1, value[i]);
            }
            cnt = prst.executeUpdate();
            if (cnt > 0) conn.commit();
            close(prst);
            close(conn);
        }catch (Exception e){}
        return cnt;
    }

    public static void selectQuery (String table, String[] args){

    }

    public static ResultSet selectQuery (String table) {
        String sql = "SELECT * FROM " + table;
        Connection conn = getConnect();
        PreparedStatement prst = null;
        ResultSet rs = null;
        try {
            prst = conn.prepareStatement(sql);
            rs = prst.executeQuery();
        }catch (Exception e) {
            e.printStackTrace();
        }
        return rs;
    }

    public static void main(String[] args) {

        AnoMessage msg = new AnoMessage("firstRoom","5f74026f-d876-4f5e-b064-ea8f456fd49d");
        msg.setMsg("first");
        msg.setType(AnoMessage.MsgType.OUT);
        msg.setSenderId(UUID.randomUUID().toString());



        Connection conn = getConnect();
        try {
            PreparedStatement prst = conn.prepareStatement("INSERT INTO ANO_MESSAGE (ROOM_ID,MSG_TYPE,MSG_BODY, USER_ID,MSG_SEND_DATE)" +
                    "VALUES (?,?,?,?,?)");
            prst.setString(1, msg.getRoomId());
            prst.setString(2, msg.getType().name());
            prst.setString(3, msg.getMsg());
            prst.setString(4, msg.getRoomId());
            prst.setDate(5, new java.sql.Date(msg.getSendDate().getTime()));
            System.out.println(prst.executeUpdate());
            close(conn);
            close(prst);
        }catch (Exception e){
            e.printStackTrace();
        }
    }


    public static void parseQuery(String query){

    }

    public static void close(Connection conn){
        try {
            if (conn != null) conn.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    public static void close (PreparedStatement prst) {
        try {
            if (prst != null) prst.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    public static void close (ResultSet rs) {
        try {
            if (rs != null) rs.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

}
