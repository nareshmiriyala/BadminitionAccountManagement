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
import com.dellnaresh.common.remote.entities.BadmintonAccount;
import com.dellnaresh.common.remote.entities.Player;
import com.dellnaresh.dao.controller.exceptions.NonexistentEntityException;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author nareshm
 */
public class BadmintonAccountJpaController implements Serializable {

    public BadmintonAccountJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(BadmintonAccount BadmintonAccount) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            BadmintonHire hireId = BadmintonAccount.getHireId();
            if (hireId != null) {
                hireId = em.getReference(hireId.getClass(), hireId.getId());
                BadmintonAccount.setHireId(hireId);
            }
            Player playerId = BadmintonAccount.getPlayerId();
            if (playerId != null) {
                playerId = em.getReference(playerId.getClass(), playerId.getId());
                BadmintonAccount.setPlayerId(playerId);
            }
            em.persist(BadmintonAccount);
            if (hireId != null) {
                hireId.getBadmintonAccountCollection().add(BadmintonAccount);
                hireId = em.merge(hireId);
            }
            if (playerId != null) {
                playerId.getBadmintonAccountCollection().add(BadmintonAccount);
                playerId = em.merge(playerId);
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(BadmintonAccount BadmintonAccount) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            BadmintonAccount persistentBadmintonAccount = em.find(BadmintonAccount.class, BadmintonAccount.getId());
            BadmintonHire hireIdOld = persistentBadmintonAccount.getHireId();
            BadmintonHire hireIdNew = BadmintonAccount.getHireId();
            Player playerIdOld = persistentBadmintonAccount.getPlayerId();
            Player playerIdNew = BadmintonAccount.getPlayerId();
            if (hireIdNew != null) {
                hireIdNew = em.getReference(hireIdNew.getClass(), hireIdNew.getId());
                BadmintonAccount.setHireId(hireIdNew);
            }
            if (playerIdNew != null) {
                playerIdNew = em.getReference(playerIdNew.getClass(), playerIdNew.getId());
                BadmintonAccount.setPlayerId(playerIdNew);
            }
            BadmintonAccount = em.merge(BadmintonAccount);
            if (hireIdOld != null && !hireIdOld.equals(hireIdNew)) {
                hireIdOld.getBadmintonAccountCollection().remove(BadmintonAccount);
                hireIdOld = em.merge(hireIdOld);
            }
            if (hireIdNew != null && !hireIdNew.equals(hireIdOld)) {
                hireIdNew.getBadmintonAccountCollection().add(BadmintonAccount);
                hireIdNew = em.merge(hireIdNew);
            }
            if (playerIdOld != null && !playerIdOld.equals(playerIdNew)) {
                playerIdOld.getBadmintonAccountCollection().remove(BadmintonAccount);
                playerIdOld = em.merge(playerIdOld);
            }
            if (playerIdNew != null && !playerIdNew.equals(playerIdOld)) {
                playerIdNew.getBadmintonAccountCollection().add(BadmintonAccount);
                playerIdNew = em.merge(playerIdNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Long id = BadmintonAccount.getId();
                if (findBadmintonAccount(id) == null) {
                    throw new NonexistentEntityException("The BadmintonAccount with id " + id + " no longer exists.");
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
            BadmintonAccount BadmintonAccount;
            try {
                BadmintonAccount = em.getReference(BadmintonAccount.class, id);
                BadmintonAccount.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The BadmintonAccount with id " + id + " no longer exists.", enfe);
            }
            BadmintonHire hireId = BadmintonAccount.getHireId();
            if (hireId != null) {
                hireId.getBadmintonAccountCollection().remove(BadmintonAccount);
                hireId = em.merge(hireId);
            }
            Player playerId = BadmintonAccount.getPlayerId();
            if (playerId != null) {
                playerId.getBadmintonAccountCollection().remove(BadmintonAccount);
                playerId = em.merge(playerId);
            }
            em.remove(BadmintonAccount);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<BadmintonAccount> findBadmintonAccountEntities() {
        return findBadmintonAccountEntities(true, -1, -1);
    }

    public List<BadmintonAccount> findBadmintonAccountEntities(int maxResults, int firstResult) {
        return findBadmintonAccountEntities(false, maxResults, firstResult);
    }

    private List<BadmintonAccount> findBadmintonAccountEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(BadmintonAccount.class));
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

    public BadmintonAccount findBadmintonAccount(Long id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(BadmintonAccount.class, id);
        } finally {
            em.close();
        }
    }

    public int getBadmintonAccountCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<BadmintonAccount> rt = cq.from(BadmintonAccount.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
