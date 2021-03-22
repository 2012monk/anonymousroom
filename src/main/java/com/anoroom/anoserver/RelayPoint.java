package com.anoroom.anoserver;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.DataInputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.Map;

@WebServlet(name = "RelayPoint", value = "/1")
public class RelayPoint extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.sendRedirect("./roomlist.html");


    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String roomName = request.getParameter("roomname").toString();
        System.out.println(roomName);
        response.sendRedirect("./room.html"+"?roomname="+roomName);
    }
}
