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
import com.dellnaresh.common.remote.entities.BadmintonHire;
import com.dellnaresh.common.remote.entities.Badmintonaccount;
import com.dellnaresh.common.remote.entities.Player;
import com.dellnaresh.dao.controller.exceptions.NonexistentEntityException;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author nareshm
 */
public class BadmintonaccountJpaController implements Serializable {

    public BadmintonaccountJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Badmintonaccount Badmintonaccount) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            BadmintonHire hireId = Badmintonaccount.getHireId();
            if (hireId != null) {
                hireId = em.getReference(hireId.getClass(), hireId.getId());
                Badmintonaccount.setHireId(hireId);
            }
            Player playerId = Badmintonaccount.getPlayerId();
            if (playerId != null) {
                playerId = em.getReference(playerId.getClass(), playerId.getId());
                Badmintonaccount.setPlayerId(playerId);
            }
            em.persist(Badmintonaccount);
            if (hireId != null) {
                hireId.getBadmintonaccountCollection().add(Badmintonaccount);
                hireId = em.merge(hireId);
            }
            if (playerId != null) {
                playerId.getBadmintonaccountCollection().add(Badmintonaccount);
                playerId = em.merge(playerId);
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Badmintonaccount Badmintonaccount) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Badmintonaccount persistentBadmintonaccount = em.find(Badmintonaccount.class, Badmintonaccount.getId());
            BadmintonHire hireIdOld = persistentBadmintonaccount.getHireId();
            BadmintonHire hireIdNew = Badmintonaccount.getHireId();
            Player playerIdOld = persistentBadmintonaccount.getPlayerId();
            Player playerIdNew = Badmintonaccount.getPlayerId();
            if (hireIdNew != null) {
                hireIdNew = em.getReference(hireIdNew.getClass(), hireIdNew.getId());
                Badmintonaccount.setHireId(hireIdNew);
            }
            if (playerIdNew != null) {
                playerIdNew = em.getReference(playerIdNew.getClass(), playerIdNew.getId());
                Badmintonaccount.setPlayerId(playerIdNew);
            }
            Badmintonaccount = em.merge(Badmintonaccount);
            if (hireIdOld != null && !hireIdOld.equals(hireIdNew)) {
                hireIdOld.getBadmintonaccountCollection().remove(Badmintonaccount);
                hireIdOld = em.merge(hireIdOld);
            }
            if (hireIdNew != null && !hireIdNew.equals(hireIdOld)) {
                hireIdNew.getBadmintonaccountCollection().add(Badmintonaccount);
                hireIdNew = em.merge(hireIdNew);
            }
            if (playerIdOld != null && !playerIdOld.equals(playerIdNew)) {
                playerIdOld.getBadmintonaccountCollection().remove(Badmintonaccount);
                playerIdOld = em.merge(playerIdOld);
            }
            if (playerIdNew != null && !playerIdNew.equals(playerIdOld)) {
                playerIdNew.getBadmintonaccountCollection().add(Badmintonaccount);
                playerIdNew = em.merge(playerIdNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Long id = Badmintonaccount.getId();
                if (findBadmintonaccount(id) == null) {
                    throw new NonexistentEntityException("The Badmintonaccount with id " + id + " no longer exists.");
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
            Badmintonaccount Badmintonaccount;
            try {
                Badmintonaccount = em.getReference(Badmintonaccount.class, id);
                Badmintonaccount.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The Badmintonaccount with id " + id + " no longer exists.", enfe);
            }
            BadmintonHire hireId = Badmintonaccount.getHireId();
            if (hireId != null) {
                hireId.getBadmintonaccountCollection().remove(Badmintonaccount);
                hireId = em.merge(hireId);
            }
            Player playerId = Badmintonaccount.getPlayerId();
            if (playerId != null) {
                playerId.getBadmintonaccountCollection().remove(Badmintonaccount);
                playerId = em.merge(playerId);
            }
            em.remove(Badmintonaccount);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Badmintonaccount> findBadmintonaccountEntities() {
        return findBadmintonaccountEntities(true, -1, -1);
    }

    public List<Badmintonaccount> findBadmintonaccountEntities(int maxResults, int firstResult) {
        return findBadmintonaccountEntities(false, maxResults, firstResult);
    }

    private List<Badmintonaccount> findBadmintonaccountEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Badmintonaccount.class));
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

    public Badmintonaccount findBadmintonaccount(Long id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Badmintonaccount.class, id);
        } finally {
            em.close();
        }
    }

    public int getBadmintonaccountCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Badmintonaccount> rt = cq.from(Badmintonaccount.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
