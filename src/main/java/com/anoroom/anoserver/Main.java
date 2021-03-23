package com.anoroom.anoserver;

import com.anoroom.anocontrol.AnoService;
import com.anoroom.model.AnoRoom;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import javax.websocket.Session;
import java.io.*;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.*;

@WebServlet(name = "Main", value = "/main/*")
public class Main extends HttpServlet {
    private static final ObjectMapper mapper = new ObjectMapper();
//    private static final ArrayList<String> roomList = new ArrayList<>();
    private static final Map<String, Set<String>> userList = new HashMap<>();
    private static final LinkedList<String[]> roomList = new LinkedList<>();
    private static final AnoService service = new AnoService();



    public void updateUserList(String roomId, Set<String> userSet) {
        userList.put(roomId, userSet);
    }

    public void updateRoomList(ArrayList<String[]> roomName){
        roomList.clear();
        roomList.addAll(roomName);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        service.update();
        response.setCharacterEncoding("UTF-8");
        String query = request.getPathInfo();
        String param = request.getQueryString();
        response.setContentType("application/x-www-form-urlencoded");
        if (query.equals("/roomList")) {
            String json = mapper.writeValueAsString(roomList);
            response.getWriter().write(json);
        }

        if (query.equals("/userList")) {
            String roomName = URLDecoder.decode(param.split("=")[1]);
            String roomId = param.split("=")[1];

            String json = mapper.writeValueAsString(userList.get(roomId));
            response.getWriter().write(json);
//            System.out.println(json);
        }

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    }
}
