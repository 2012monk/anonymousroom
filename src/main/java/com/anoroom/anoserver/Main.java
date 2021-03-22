package com.anoroom.anoserver;

import com.anoroom.anocontrol.AnoService;
import com.anoroom.model.AnoRoom;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import javax.websocket.Session;
import java.io.*;
import java.util.*;

@WebServlet(name = "Main", value = "/main/*")
public class Main extends HttpServlet {
    private static final ObjectMapper mapper = new ObjectMapper();
//    private static final ArrayList<String> roomList = new ArrayList<>();
    private static final Map<String, Set<String>> userList = new HashMap<>();
    private static final AnoService service = new AnoService();

//    public void updateRoomList(String name) {
//        for (String s:roomList){
//            System.out.println(s);
//        }
//        System.out.println(name+"   room added complete");
//        roomList.add(name);
//    }
//
//    public void updateRoomList(Set<String> s){
//        roomList.clear();
//        roomList.addAll(s);
//    }


    public void updateUserList(String roomId, Set<String> userSet) {
        userList.put(roomId, userSet);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        service.update();
        String query = request.getPathInfo();
        String param = request.getQueryString();
        response.setContentType("application/x-www-form-urlencoded");
        if (query.equals("/roomList")) {
            String json = mapper.writeValueAsString(userList);
            response.getWriter().write(json);
        }

        if (query.equals("/userList")) {
            String roomName = param.split("=")[1];
            List<String> usrs = new ArrayList<>(userList.get(roomName));

            String json = mapper.writeValueAsString(usrs);
            response.getWriter().write(json);
//            System.out.println(json);
        }

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("asdf");
    }
}
