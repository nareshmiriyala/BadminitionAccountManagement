package com.dellnaresh.controller.jaxrs;

import com.dellnaresh.common.remote.PlayerService;
import com.dellnaresh.common.remote.RemotePlayerService;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by nareshm on 10/05/2015.
 */
@ApplicationPath("app")
public class BadmintonApp extends Application {
    private Set<Object> singletons = new HashSet<Object>();
    private Set<Class<?>> classes = new HashSet<Class<?>>();
    public BadmintonApp(){
        classes.add(CourtHireService.class);
    }
    @Override
    public Set<Class<?>> getClasses() {
        return classes;
    }

    @Override
    public Set<Object> getSingletons() {
        return singletons;
    }
}
