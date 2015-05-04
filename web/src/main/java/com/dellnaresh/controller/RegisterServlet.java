package com.dellnaresh.controller;

import com.dellnaresh.common.remote.RemotePlayerService;
import com.dellnaresh.common.remote.entities.Player;
import org.json.JSONObject;
import org.json.simple.parser.JSONParser;

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

/**
 * Created by nareshm on 2/05/2015.
 */
@WebServlet(name = "RegisterServlet", urlPatterns = {"/register"})
public class RegisterServlet extends HttpServlet {

    @EJB
    RemotePlayerService remotePlayerService;

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            StringBuffer sb = new StringBuffer();
            try {
                BufferedReader reader = request.getReader();
                String line = null;
                while ((line = reader.readLine()) != null) {
                    sb.append(line);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            JSONParser parser = new JSONParser();
            JSONObject joPlayer = null;
            joPlayer = new JSONObject(sb.toString());

            String firstname = (String) joPlayer.get("firstname");
            String lastName = (String) joPlayer.get("lastname");
            String password = (String) joPlayer.get("password");
            String email = (String) joPlayer.get("email");
            Integer contactno = (Integer) joPlayer.get("contactno");
            String address = (String) joPlayer.get("address");

            callCreatePlayer(firstname, lastName, email, contactno, address, password);
            response.setContentType("text/html");
            PrintWriter out = response.getWriter();
            out.write("A new user " + firstname + " has been created.");
            out.flush();
            out.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void callCreatePlayer(String firstName, String lastName, String email, long conactno, String address, String password) throws Exception {
        Player player = new Player();
        player.setActive((byte) 1);
        player.setAddress(address);
        player.setContactNo(conactno);
        player.setFirstName(firstName);
        player.setLastName(lastName);
        player.setEmailID(email);
        player.setJoiningDate(new Timestamp(new Date().getTime()));

        remotePlayerService.createPlayer(player);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }
}
