/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dellnaresh.dao.controller;

import com.dellnaresh.common.remote.entities.BadmintonHire;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import com.dellnaresh.common.remote.entities.Player;
import com.dellnaresh.common.remote.entities.BadmintonAccount;
import com.dellnaresh.dao.controller.exceptions.NonexistentEntityException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author nareshm
 */
public class BadimintonHireJpaController implements Serializable {

    public BadimintonHireJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(BadmintonHire badmintonHire) {
        if (badmintonHire.getBadmintonAccountCollection() == null) {
            badmintonHire.setBadmintonAccountCollection(new ArrayList<BadmintonAccount>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Player payer = badmintonHire.getPayer();
            if (payer != null) {
                payer = em.getReference(payer.getClass(), payer.getId());
                badmintonHire.setPayer(payer);
            }
            Collection<BadmintonAccount> attachedBadmintonAccountCollection = new ArrayList<BadmintonAccount>();
            for (BadmintonAccount badmintonaccountCollectionBadmintonAccountToAttach : badmintonHire.getBadmintonAccountCollection()) {
                badmintonaccountCollectionBadmintonAccountToAttach = em.getReference(badmintonaccountCollectionBadmintonAccountToAttach.getClass(), badmintonaccountCollectionBadmintonAccountToAttach.getId());
                attachedBadmintonAccountCollection.add(badmintonaccountCollectionBadmintonAccountToAttach);
            }
            badmintonHire.setBadmintonAccountCollection(attachedBadmintonAccountCollection);
            em.persist(badmintonHire);
            if (payer != null) {
                payer.getBadmintonHireCollection().add(badmintonHire);
                payer = em.merge(payer);
            }
            for (BadmintonAccount badmintonaccountCollectionBadmintonAccount : badmintonHire.getBadmintonAccountCollection()) {
                BadmintonHire oldHireIdOfBadmintonAccountCollectionBadmintonAccount = badmintonaccountCollectionBadmintonAccount.getHireId();
                badmintonaccountCollectionBadmintonAccount.setHireId(badmintonHire);
                badmintonaccountCollectionBadmintonAccount = em.merge(badmintonaccountCollectionBadmintonAccount);
                if (oldHireIdOfBadmintonAccountCollectionBadmintonAccount != null) {
                    oldHireIdOfBadmintonAccountCollectionBadmintonAccount.getBadmintonAccountCollection().remove(badmintonaccountCollectionBadmintonAccount);
                    oldHireIdOfBadmintonAccountCollectionBadmintonAccount = em.merge(oldHireIdOfBadmintonAccountCollectionBadmintonAccount);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(BadmintonHire badmintonHire) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            BadmintonHire persistentBadmintonHire = em.find(BadmintonHire.class, badmintonHire.getId());
            Player payerOld = persistentBadmintonHire.getPayer();
            Player payerNew = badmintonHire.getPayer();
            Collection<BadmintonAccount> badmintonAccountCollectionOld = persistentBadmintonHire.getBadmintonAccountCollection();
            Collection<BadmintonAccount> badmintonAccountCollectionNew = badmintonHire.getBadmintonAccountCollection();
            if (payerNew != null) {
                payerNew = em.getReference(payerNew.getClass(), payerNew.getId());
                badmintonHire.setPayer(payerNew);
            }
            Collection<BadmintonAccount> attachedBadmintonAccountCollectionNew = new ArrayList<BadmintonAccount>();
            for (BadmintonAccount badmintonaccountCollectionNewBadmintonAccountToAttach : badmintonAccountCollectionNew) {
                badmintonaccountCollectionNewBadmintonAccountToAttach = em.getReference(badmintonaccountCollectionNewBadmintonAccountToAttach.getClass(), badmintonaccountCollectionNewBadmintonAccountToAttach.getId());
                attachedBadmintonAccountCollectionNew.add(badmintonaccountCollectionNewBadmintonAccountToAttach);
            }
            badmintonAccountCollectionNew = attachedBadmintonAccountCollectionNew;
            badmintonHire.setBadmintonAccountCollection(badmintonAccountCollectionNew);
            badmintonHire = em.merge(badmintonHire);
            if (payerOld != null && !payerOld.equals(payerNew)) {
                payerOld.getBadmintonHireCollection().remove(badmintonHire);
                payerOld = em.merge(payerOld);
            }
            if (payerNew != null && !payerNew.equals(payerOld)) {
                payerNew.getBadmintonHireCollection().add(badmintonHire);
                payerNew = em.merge(payerNew);
            }
            for (BadmintonAccount badmintonaccountCollectionOldBadmintonAccount : badmintonAccountCollectionOld) {
                if (!badmintonAccountCollectionNew.contains(badmintonaccountCollectionOldBadmintonAccount)) {
                    badmintonaccountCollectionOldBadmintonAccount.setHireId(null);
                    badmintonaccountCollectionOldBadmintonAccount = em.merge(badmintonaccountCollectionOldBadmintonAccount);
                }
            }
            for (BadmintonAccount badmintonaccountCollectionNewBadmintonAccount : badmintonAccountCollectionNew) {
                if (!badmintonAccountCollectionOld.contains(badmintonaccountCollectionNewBadmintonAccount)) {
                    BadmintonHire oldHireIdOfBadmintonAccountCollectionNewBadmintonAccount = badmintonaccountCollectionNewBadmintonAccount.getHireId();
                    badmintonaccountCollectionNewBadmintonAccount.setHireId(badmintonHire);
                    badmintonaccountCollectionNewBadmintonAccount = em.merge(badmintonaccountCollectionNewBadmintonAccount);
                    if (oldHireIdOfBadmintonAccountCollectionNewBadmintonAccount != null && !oldHireIdOfBadmintonAccountCollectionNewBadmintonAccount.equals(badmintonHire)) {
                        oldHireIdOfBadmintonAccountCollectionNewBadmintonAccount.getBadmintonAccountCollection().remove(badmintonaccountCollectionNewBadmintonAccount);
                        oldHireIdOfBadmintonAccountCollectionNewBadmintonAccount = em.merge(oldHireIdOfBadmintonAccountCollectionNewBadmintonAccount);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Long id = badmintonHire.getId();
                if (findBadimintionHire(id) == null) {
                    throw new NonexistentEntityException("The badimintionHire with id " + id + " no longer exists.");
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
            BadmintonHire badmintonHire;
            try {
                badmintonHire = em.getReference(BadmintonHire.class, id);
                badmintonHire.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The badimintionHire with id " + id + " no longer exists.", enfe);
            }
            Player payer = badmintonHire.getPayer();
            if (payer != null) {
                payer.getBadmintonHireCollection().remove(badmintonHire);
                payer = em.merge(payer);
            }
            Collection<BadmintonAccount> badmintonAccountCollection = badmintonHire.getBadmintonAccountCollection();
            for (BadmintonAccount badmintonaccountCollectionBadmintonAccount : badmintonAccountCollection) {
                badmintonaccountCollectionBadmintonAccount.setHireId(null);
                badmintonaccountCollectionBadmintonAccount = em.merge(badmintonaccountCollectionBadmintonAccount);
            }
            em.remove(badmintonHire);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<BadmintonHire> findBadimintionHireEntities() {
        return findBadimintionHireEntities(true, -1, -1);
    }

    public List<BadmintonHire> findBadimintionHireEntities(int maxResults, int firstResult) {
        return findBadimintionHireEntities(false, maxResults, firstResult);
    }

    private List<BadmintonHire> findBadimintionHireEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(BadmintonHire.class));
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

    public BadmintonHire findBadimintionHire(Long id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(BadmintonHire.class, id);
        } finally {
            em.close();
        }
    }

    public int getBadimintionHireCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<BadmintonHire> rt = cq.from(BadmintonHire.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
