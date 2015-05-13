package com.dellnaresh.dao;

import com.dellnaresh.common.remote.entities.BadimintionHire;
import com.dellnaresh.common.remote.entities.Player;
import com.dellnaresh.common.remote.entities.Users;
import com.dellnaresh.interfaces.PlayerDAO;

import javax.enterprise.context.Dependent;
import javax.enterprise.inject.Default;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.List;

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
    public Player get(String username) throws Exception {
        TypedQuery<Users> query =
                entityManager.createNamedQuery("Users.findByUsername", Users.class);
        query.setParameter("username",username);
        List<Users> results = query.getResultList();
        Users users = results.get(0);
        return entityManager.find(Player.class, users.getLogin());
    }

    @Override
    public List<BadimintionHire> getCourts(int payerId) throws Exception {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery cq = criteriaBuilder.createQuery();
        Root from = cq.from(BadimintionHire.class);
        cq.select(from);
        Predicate predicate1 = criteriaBuilder.equal(from.get("payer"), new Player(payerId));
        cq.where(predicate1);
        return entityManager.createQuery(cq).getResultList();
    }

    @Override
    public void update(Player player) throws Exception {

    }

    @Override
    public void delete(int id) throws Exception {

    }

    @Override
    public void hireCourt(BadimintionHire hire) {
        entityManager.persist(hire);
    }
}
