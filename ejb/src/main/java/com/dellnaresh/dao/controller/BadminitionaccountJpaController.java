/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dellnaresh.dao.controller;

import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import com.dellnaresh.common.remote.entities.BadimintionHire;
import com.dellnaresh.common.remote.entities.Badminitionaccount;
import com.dellnaresh.common.remote.entities.Player;
import com.dellnaresh.dao.controller.exceptions.NonexistentEntityException;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author nareshm
 */
public class BadminitionaccountJpaController implements Serializable {

    public BadminitionaccountJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Badminitionaccount badminitionaccount) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            BadimintionHire hireId = badminitionaccount.getHireId();
            if (hireId != null) {
                hireId = em.getReference(hireId.getClass(), hireId.getId());
                badminitionaccount.setHireId(hireId);
            }
            Player playerId = badminitionaccount.getPlayerId();
            if (playerId != null) {
                playerId = em.getReference(playerId.getClass(), playerId.getId());
                badminitionaccount.setPlayerId(playerId);
            }
            em.persist(badminitionaccount);
            if (hireId != null) {
                hireId.getBadminitionaccountCollection().add(badminitionaccount);
                hireId = em.merge(hireId);
            }
            if (playerId != null) {
                playerId.getBadminitionaccountCollection().add(badminitionaccount);
                playerId = em.merge(playerId);
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Badminitionaccount badminitionaccount) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Badminitionaccount persistentBadminitionaccount = em.find(Badminitionaccount.class, badminitionaccount.getId());
            BadimintionHire hireIdOld = persistentBadminitionaccount.getHireId();
            BadimintionHire hireIdNew = badminitionaccount.getHireId();
            Player playerIdOld = persistentBadminitionaccount.getPlayerId();
            Player playerIdNew = badminitionaccount.getPlayerId();
            if (hireIdNew != null) {
                hireIdNew = em.getReference(hireIdNew.getClass(), hireIdNew.getId());
                badminitionaccount.setHireId(hireIdNew);
            }
            if (playerIdNew != null) {
                playerIdNew = em.getReference(playerIdNew.getClass(), playerIdNew.getId());
                badminitionaccount.setPlayerId(playerIdNew);
            }
            badminitionaccount = em.merge(badminitionaccount);
            if (hireIdOld != null && !hireIdOld.equals(hireIdNew)) {
                hireIdOld.getBadminitionaccountCollection().remove(badminitionaccount);
                hireIdOld = em.merge(hireIdOld);
            }
            if (hireIdNew != null && !hireIdNew.equals(hireIdOld)) {
                hireIdNew.getBadminitionaccountCollection().add(badminitionaccount);
                hireIdNew = em.merge(hireIdNew);
            }
            if (playerIdOld != null && !playerIdOld.equals(playerIdNew)) {
                playerIdOld.getBadminitionaccountCollection().remove(badminitionaccount);
                playerIdOld = em.merge(playerIdOld);
            }
            if (playerIdNew != null && !playerIdNew.equals(playerIdOld)) {
                playerIdNew.getBadminitionaccountCollection().add(badminitionaccount);
                playerIdNew = em.merge(playerIdNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Long id = badminitionaccount.getId();
                if (findBadminitionaccount(id) == null) {
                    throw new NonexistentEntityException("The badminitionaccount with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Long id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Badminitionaccount badminitionaccount;
            try {
                badminitionaccount = em.getReference(Badminitionaccount.class, id);
                badminitionaccount.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The badminitionaccount with id " + id + " no longer exists.", enfe);
            }
            BadimintionHire hireId = badminitionaccount.getHireId();
            if (hireId != null) {
                hireId.getBadminitionaccountCollection().remove(badminitionaccount);
                hireId = em.merge(hireId);
            }
            Player playerId = badminitionaccount.getPlayerId();
            if (playerId != null) {
                playerId.getBadminitionaccountCollection().remove(badminitionaccount);
                playerId = em.merge(playerId);
            }
            em.remove(badminitionaccount);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Badminitionaccount> findBadminitionaccountEntities() {
        return findBadminitionaccountEntities(true, -1, -1);
    }

    public List<Badminitionaccount> findBadminitionaccountEntities(int maxResults, int firstResult) {
        return findBadminitionaccountEntities(false, maxResults, firstResult);
    }

    private List<Badminitionaccount> findBadminitionaccountEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Badminitionaccount.class));
            Query q = em.createQuery(cq);
            if (!all) {
                q.setMaxResults(maxResults);
                q.setFirstResult(firstResult);
            }
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    public Badminitionaccount findBadminitionaccount(Long id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Badminitionaccount.class, id);
        } finally {
            em.close();
        }
    }

    public int getBadminitionaccountCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Badminitionaccount> rt = cq.from(Badminitionaccount.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
