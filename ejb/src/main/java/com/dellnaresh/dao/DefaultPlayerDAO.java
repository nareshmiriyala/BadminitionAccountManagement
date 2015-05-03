package com.dellnaresh.dao;

import com.dellnaresh.common.remote.entities.Player;
import com.dellnaresh.interfaces.PlayerDAO;

import javax.enterprise.context.Dependent;
import javax.enterprise.inject.Default;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 * Created by nareshm on 2/05/2015.
 */
@Dependent
public class DefaultPlayerDAO implements PlayerDAO {
    @PersistenceContext(unitName = "BadmintionAccountPU")
    private EntityManager entityManager;
    @Override
    public void create(Player player) throws Exception {
       entityManager.persist(player);
    }

    @Override
    public Player get(int id) throws Exception {
        return null;
    }

    @Override
    public void update(Player player) throws Exception {

    }

    @Override
    public void delete(int id) throws Exception {

    }
}
