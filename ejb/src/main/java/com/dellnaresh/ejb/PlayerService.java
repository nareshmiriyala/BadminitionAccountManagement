package com.dellnaresh.ejb;

import com.dellnaresh.common.remote.RemotePlayerService;
import com.dellnaresh.common.remote.entities.Player;
import com.dellnaresh.interfaces.LocalPlayerService;
import com.dellnaresh.interfaces.PlayerDAO;

import javax.ejb.EJB;
import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.inject.Inject;
import java.io.Serializable;

/**
 * Created by nareshm on 2/05/2015.
 */
@Stateless(name = "PlayerServiceEJB")
@Remote
public class PlayerService implements RemotePlayerService, Serializable {
    @Inject
    PlayerDAO playerDAO;

    public PlayerService() {
    }

    public void createPlayer(Player player) throws Exception {
        playerDAO.create(player);
    }
}
