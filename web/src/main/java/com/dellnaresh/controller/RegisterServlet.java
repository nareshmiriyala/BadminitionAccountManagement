package com.dellnaresh.controller;

import com.dellnaresh.common.remote.RemotePlayerService;
import com.dellnaresh.common.remote.entities.Player;
import org.json.JSONObject;

import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

/**
 * Created by nareshm on 2/05/2015.
 */
@WebServlet(name = "RegisterServlet",urlPatterns = {"/register"})
public class RegisterServlet extends HttpServlet {

   @EJB
    RemotePlayerService remotePlayerService;
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {

            JSONObject jObj = new JSONObject(request.getParameter("player")); // this parses the json
            Iterator it = jObj.keys(); //gets all the keys
            Map<String,String> map=new HashMap<>();
            while(it.hasNext())
            {
                String key = (String) it.next(); // get key
                Object o = jObj.get(key); // get value
                map.put(key, (String) o);
            }

            String firstname = (String) map.get("firstname");
            callCreatePlayer(firstname);
            response.setContentType("text/html");
            PrintWriter out = response.getWriter();
            out.write("A new user " + firstname + " has been created.");
            out.flush();
            out.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void callCreatePlayer(String firstName) throws Exception {
        Player player = new Player();
        player.setActive((byte) 1);
        player.setAddress("64 Brady Road");
        player.setContactNo(466097852l);
        player.setFirstName(firstName);
        player.setLastName("Chandra");
        player.setEmailID("ncmiriyala@gmail.com");
        player.setJoiningDate(new Timestamp(new Date().getTime()));

        remotePlayerService.createPlayer(player);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    doPost(request,response);
    }
}
