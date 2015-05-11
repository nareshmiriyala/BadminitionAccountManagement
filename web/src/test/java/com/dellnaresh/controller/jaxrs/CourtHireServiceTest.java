package com.dellnaresh.controller.jaxrs;

import com.dellnaresh.common.remote.RemotePlayerService;
import com.dellnaresh.common.remote.entities.BadimintionHire;
import com.dellnaresh.dao.DefaultPlayerDAO;
import com.dellnaresh.ejb.BadminitionPlayerService;
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
import javax.xml.ws.Response;
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
    //    @Deployment
//    public static Archive createDeployment()
//    {
//        final GenericArchive webResources = ShrinkWrap.create(GenericArchive.class)
//                .as(ExplodedImporter.class)
//                .importDirectory("src/main/webapp")
//                .as(GenericArchive.class);
//        final File[] seleniumApi = Maven.resolver()
//                .loadPomFromFile("pom.xml")
//                .resolve("org.seleniumhq.selenium:selenium-api:2.35.0")
//                .withTransitivity()
//                .asFile();
//        return ShrinkWrap.create(WebArchive.class, CourtHireServiceTest.class.getSimpleName() + ".war")
//                .addClasses(CourtHireService.class, RemotePlayerService.class)
//                .addAsResource("META-INF/persistence.xml")
//                .addAsWebInfResource("enforce-beans.xml", "jboss-all.xml")
//                .addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml")
//                .addAsLibraries(seleniumApi)
//                .merge(webResources);
//    }
@Deployment
public static Archive<?> createDeployment() {
    return ShrinkWrap.create(WebArchive.class, "test.war")
//            .addPackages(true,"com.dellnaresh")
            .addClasses(CourtHireService.class, BadmintionApp.class)
            .addPackages(true, BadimintionHire.class.getPackage())
            .addPackages(true, BadminitionPlayerService.class.getPackage(), PlayerDAO.class.getPackage(), DefaultPlayerDAO.class.getPackage(), RemotePlayerService.class.getPackage())
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
        target = client.target(URI.create(new URL(base, "web/hirecourt").toExternalForm()));
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
        assertEquals("Status",post.getStatus(),200);

    }


}