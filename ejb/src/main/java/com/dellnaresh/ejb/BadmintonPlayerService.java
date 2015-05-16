package com.dellnaresh.ejb;

import com.dellnaresh.common.remote.RemotePlayerService;
import com.dellnaresh.common.remote.entities.BadmintonHire;
import com.dellnaresh.common.remote.entities.Player;
import com.dellnaresh.common.remote.LocalPlayerService;
import com.dellnaresh.interfaces.PlayerDAO;

import javax.ejb.Stateless;
import javax.inject.Inject;
import java.util.List;

/**
 * Created by nareshm on 2/05/2015.
 */
@Stateless
public class BadmintonPlayerService implements RemotePlayerService,LocalPlayerService {
    @Inject
    PlayerDAO playerDAO;

    public BadmintonPlayerService() {
    }

    public void createPlayer(Player player) throws Exception {
        playerDAO.create(player);
    }

    @Override
    public void hireCourt(BadmintonHire badmintonHire) throws Exception {
        playerDAO.hireCourt(badmintonHire);
    }

    @Override
    public Player getPlayer(String username) throws Exception {
        return playerDAO.get(username);
    }

    @Override
    public List<BadmintonHire> getHiredCourts(int payerId) throws Exception {
        return playerDAO.getCourts(payerId);
    }

}
