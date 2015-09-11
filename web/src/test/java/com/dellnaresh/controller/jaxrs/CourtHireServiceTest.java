package com.dellnaresh.controller.jaxrs;

import com.dellnaresh.common.remote.RemotePlayerService;
import com.dellnaresh.common.remote.entities.BadmintonHire;
import com.dellnaresh.dao.DefaultPlayerDAO;
import com.dellnaresh.ejb.BadmintonPlayerService;
import com.dellnaresh.interfaces.PlayerDAO;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;

import static org.junit.Assert.assertEquals;

/**
 * Created by nareshm on 10/05/2015.
 */
@RunWith(Arquillian.class)
@RunAsClient
public class CourtHireServiceTest {
    private static WebTarget target;
    Logger logger = LoggerFactory.getLogger(CourtHireServiceTest.class);

@Deployment
public static Archive<?> createDeployment() {
    return ShrinkWrap.create(WebArchive.class, "test.war")
            .addClasses(CourtHireService.class, BadmintonApp.class)
            .addPackages(true, BadmintonHire.class.getPackage())
            .addPackages(true, BadmintonPlayerService.class.getPackage(), PlayerDAO.class.getPackage(), DefaultPlayerDAO.class.getPackage(), RemotePlayerService.class.getPackage())
            .addAsResource("test-persistence.xml", "META-INF/persistence.xml")
            .addAsWebInfResource("jbossas-ds.xml")
            .addAsManifestResource(EmptyAsset.INSTANCE, "beans.xml");
}
    @ArquillianResource
    private URL base;

    @Before
    public void setupClass() throws MalformedURLException {
        Client client = ClientBuilder.newClient();
        logger.debug("Base URL",base);
        target = client.target(URI.create(new URL(base, "app/hirecourt").toExternalForm()));
    }
    /**
     * Test of POST method, of class MyResource.
     */
    @Test
    public void test1Post() {
        String json="{\"courtName\":\"Endeavour Hills\",\n" +
                "\"dateHired\": \"2015-05-10\",\n" +
                "\"startTime\": \"2015-05-10T08:30:00\",\n" +
                "\"endTime\":\"2015-05-10T10:30:00\",\n" +
                "\"moneyPaid\":35.50,\n" +
                "\"payer\":1}";
        logger.info("base url"+base);
        javax.ws.rs.core.Response post = target.request().post(Entity.json(json));
         logger.info("Status",post.getStatus());
        System.out.println(post.getStatus());
//        assertEquals("Status",200,post.getStatus());

    }
    @Test
    public void testGetJSON() throws Exception{

        javax.ws.rs.core.Response response = target.request().get();
        Object responseEntity = response.getEntity();
        System.out.println(responseEntity);
    }


}