package com.dellnaresh.ejb;

import com.dellnaresh.common.remote.PlayerService;
import com.dellnaresh.common.remote.RemotePlayerService;
import com.dellnaresh.common.remote.entities.BadimintionHire;
import com.dellnaresh.common.remote.entities.Player;
import com.dellnaresh.common.remote.LocalPlayerService;
import com.dellnaresh.interfaces.PlayerDAO;

import javax.ejb.EJB;
import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.inject.Inject;
import java.io.Serializable;

/**
 * Created by nareshm on 2/05/2015.
 */
@Stateless(name = "BadminitionPlayerService")
@EJB(name = "BadminitionPlayerService",beanInterface = RemotePlayerService.class)
public class BadminitionPlayerService implements RemotePlayerService,PlayerService,Serializable {
    @Inject
    PlayerDAO playerDAO;

    public BadminitionPlayerService() {
    }

    public void createPlayer(Player player) throws Exception {
        playerDAO.create(player);
    }

    @Override
    public void hireCourt(BadimintionHire badimintionHire) throws Exception {
        playerDAO.hireCourt(badimintionHire);
    }

    @Override
    public Player getPlayer(String username) throws Exception {
        return playerDAO.get(username);
    }

}
