package com.dellnaresh.dao;

import com.dellnaresh.common.remote.entities.BadmintonHire;
import com.dellnaresh.common.remote.entities.BadmintonHire_;
import com.dellnaresh.common.remote.entities.Player;
import com.dellnaresh.common.remote.entities.Users;
import com.dellnaresh.interfaces.PlayerDAO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.Dependent;
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
    Logger logger= LoggerFactory.getLogger(DefaultPlayerDAO.class);

    @PersistenceContext(unitName = "BadmintonAccountPU")
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
    public List<BadmintonHire> getCourts(int payerId) throws Exception {
        CriteriaQuery cq = entityManager.getCriteriaBuilder().createQuery();
        cq.select(cq.from(BadmintonHire.class));
        Query q = entityManager.createQuery(cq);
        List resultList = q.getResultList();
        System.out.println("Result List:"+resultList);
        return resultList;
    }

    @Override
    public void update(Player player) throws Exception {

    }

    @Override
    public void delete(int id) throws Exception {

    }

    @Override
    public void hireCourt(BadmintonHire hire) {
        entityManager.persist(hire);
    }
}
