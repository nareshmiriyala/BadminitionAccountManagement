package com.dellnaresh.controller.jaxrs;

import com.dellnaresh.common.remote.RemotePlayerService;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;

import static org.junit.Assert.*;

/**
 * Created by nareshm on 10/05/2015.
 */
@RunWith(Arquillian.class)
public class CourtHireServiceTest {
    private static WebTarget target;
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
    return ShrinkWrap.create(WebArchive.class,"test.war")
//            .addPackages(true,"com.dellnaresh")
            .addClasses(CourtHireService.class)
            .addAsResource("test-persistence.xml", "META-INF/persistence.xml")
            .addAsWebInfResource("jbossas-ds.xml")
            .addAsManifestResource(EmptyAsset.INSTANCE, "beans.xml");
}
    @ArquillianResource
    private URL base;

    @Before
    public void setupClass() throws MalformedURLException {
        Client client = ClientBuilder.newClient();
        target = client.target(URI.create(new URL(base, "web/hirecourt").toExternalForm()));
    }
    /**
     * Test of POST method, of class MyResource.
     */
    @Test
    public void test1Post() {
        String json="{ 'CourtName':'Endeavour Hills',\n" +
                "                'DateHired': '10/05/2015',\n" +
                "                'StartTime': '08:30',\n" +
                "                'EndTime':'10:30',\n" +
                "                'MoneyPaid':'35.50',\n" +
                "                'Player':'Naresh'}";
        target.request().post(Entity.json(json));
        String r = target.request().get(String.class);
        assertEquals("[apple]", r);
    }


}