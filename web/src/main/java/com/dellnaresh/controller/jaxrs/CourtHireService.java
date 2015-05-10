package com.dellnaresh.controller.jaxrs;

import com.dellnaresh.common.remote.RemotePlayerService;
import com.dellnaresh.common.remote.entities.BadimintionHire;
import javax.ejb.EJB;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;

/**
 * Created by nareshm on 10/05/2015.
 */
// The Java class will be hosted at the URI path "/courthire"
@Path("/hirecourt")
public class CourtHireService {

    @EJB
    RemotePlayerService remotePlayerService;
    // The Java method will process HTTP GET requests
    @GET
    // The Java method will produce content identified by the MIME Media type "text/plain"
    @Produces("text/plain")
    public String getClichedMessage() {
        // Return some cliched textual content
        return "Hello World";
    }

    @POST
    @Consumes("application/json")
    public Response hireCourt(BadimintionHire hire){
        try {
            remotePlayerService.hireCourt(hire);
        } catch (Exception e) {
            e.printStackTrace();
            return Response.serverError().build();
        }
        return Response.ok().build();
    }

}