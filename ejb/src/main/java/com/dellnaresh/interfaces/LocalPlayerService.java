package com.dellnaresh.interfaces;

import com.dellnaresh.common.remote.entities.Player;

import javax.ejb.Local;

/**
 * Created by nareshm on 2/05/2015.
 */
@Local
public interface LocalPlayerService {
    public void createPlayer(Player player) throws Exception;
}
