package com.dellnaresh.controller;

import com.dellnaresh.common.remote.RemotePlayerService;
import com.dellnaresh.common.remote.entities.BadimintionHire;
import com.dellnaresh.common.remote.entities.Player;
import org.json.JSONObject;
import org.json.simple.parser.JSONParser;

import javax.annotation.security.RolesAllowed;
import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.Response;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.Date;

/**
 * Created by nareshm on 9/05/2015.
 */
@WebServlet(name = "CourtHireServlet",urlPatterns = "/hirecourt")
@RolesAllowed("Manager")
public class CourtHireServlet extends HttpServlet {
    @EJB
    RemotePlayerService remotePlayerService;
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
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
        String courtName = (String) joPlayer.get("courtName");
        Date dateHired = (Date) joPlayer.get("dateHired");
         Date stateTime = (Date) joPlayer.get("stateTime");
         Date endTime = (Date) joPlayer.get("endTime");
         long moneyPaid = (long) joPlayer.get("moneyPaid");
        BadimintionHire badimintionHire=new BadimintionHire();
        badimintionHire.setCourtName(courtName);
        badimintionHire.setDateHired(dateHired);
        badimintionHire.setStartTime(stateTime);
        badimintionHire.setEndTime(endTime);
        badimintionHire.setMoneyPaid(moneyPaid);
        String name = request.getUserPrincipal().getName();
        if(name==null || name.isEmpty()){
            response.sendError(Response.Status.UNAUTHORIZED.getStatusCode(),"User Not logged in");
            log("User Not logged in");
        }
        try {
            Player player = remotePlayerService.getPlayer(name);
            badimintionHire.setPayer(player);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
       doPost(request,response);
    }
}
