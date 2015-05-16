package com.dellnaresh.controller.jaxrs;

import com.dellnaresh.common.remote.PlayerService;
import com.dellnaresh.common.remote.RemotePlayerService;
import com.dellnaresh.common.remote.entities.BadmintonHire;
import org.slf4j.Logger;

import javax.ejb.EJB;
import javax.ws.rs.*;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.Response;
import java.util.List;

/**
 * Created by nareshm on 10/05/2015.
 */
// The Java class will be hosted at the URI path "/courthire"
@Path("hirecourt")
public class CourtHireService {
    Logger logger = org.slf4j.LoggerFactory.getLogger(CourtHireService.class);

    @EJB(beanInterface = RemotePlayerService.class)
    PlayerService remotePlayerService;
    // The Java method will process HTTP GET requests
    @GET
    @Produces("application/json")
    public Response getHiredCourts() {
        // Return some cliched textual content
        List<BadmintonHire> hiredCourts=null;
        try {
            hiredCourts = remotePlayerService.getHiredCourts(1);
        } catch (Exception e) {
            e.printStackTrace();
        }
        logger.info("Returned Hired Courts List",hiredCourts);
        GenericEntity<List<BadmintonHire>> ge = new GenericEntity<List<BadmintonHire>>(hiredCourts){};

        return Response.ok(ge).build();
    }

    @POST
    @Consumes("application/json")
    public Response hireCourt(BadmintonHire hire){
        try {
            logger.info("Input value "+hire);
            remotePlayerService.hireCourt(hire);
        } catch (Exception e) {
            logger.error("Exception:",e);
            return Response.serverError().build();
        }
        return Response.ok().build();
    }

}
