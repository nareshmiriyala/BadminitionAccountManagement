package com.dellnaresh.interfaces;

import com.dellnaresh.common.remote.entities.BadmintonHire;
import com.dellnaresh.common.remote.entities.Player;

import java.util.List;

/**
 * Created by nareshm on 2/05/2015.
 */

public interface PlayerDAO {
    public void create(Player player) throws Exception;
    public Player get(int id) throws Exception;
    public void update(Player player) throws Exception;
    public void delete(int id) throws Exception;
    public void hireCourt(BadmintonHire hire);
    public Player get(String username) throws Exception ;
    public List<BadmintonHire> getCourts(int payerId) throws Exception;
}
