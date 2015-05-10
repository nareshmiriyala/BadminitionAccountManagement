/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dellnaresh.dao.controller;

import com.dellnaresh.common.remote.entities.BadimintionHire;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import com.dellnaresh.common.remote.entities.Player;
import com.dellnaresh.common.remote.entities.Badminitionaccount;
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
public class BadimintionHireJpaController implements Serializable {

    public BadimintionHireJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(BadimintionHire badimintionHire) {
        if (badimintionHire.getBadminitionaccountCollection() == null) {
            badimintionHire.setBadminitionaccountCollection(new ArrayList<Badminitionaccount>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Player payer = badimintionHire.getPayer();
            if (payer != null) {
                payer = em.getReference(payer.getClass(), payer.getId());
                badimintionHire.setPayer(payer);
            }
            Collection<Badminitionaccount> attachedBadminitionaccountCollection = new ArrayList<Badminitionaccount>();
            for (Badminitionaccount badminitionaccountCollectionBadminitionaccountToAttach : badimintionHire.getBadminitionaccountCollection()) {
                badminitionaccountCollectionBadminitionaccountToAttach = em.getReference(badminitionaccountCollectionBadminitionaccountToAttach.getClass(), badminitionaccountCollectionBadminitionaccountToAttach.getId());
                attachedBadminitionaccountCollection.add(badminitionaccountCollectionBadminitionaccountToAttach);
            }
            badimintionHire.setBadminitionaccountCollection(attachedBadminitionaccountCollection);
            em.persist(badimintionHire);
            if (payer != null) {
                payer.getBadimintionHireCollection().add(badimintionHire);
                payer = em.merge(payer);
            }
            for (Badminitionaccount badminitionaccountCollectionBadminitionaccount : badimintionHire.getBadminitionaccountCollection()) {
                BadimintionHire oldHireIdOfBadminitionaccountCollectionBadminitionaccount = badminitionaccountCollectionBadminitionaccount.getHireId();
                badminitionaccountCollectionBadminitionaccount.setHireId(badimintionHire);
                badminitionaccountCollectionBadminitionaccount = em.merge(badminitionaccountCollectionBadminitionaccount);
                if (oldHireIdOfBadminitionaccountCollectionBadminitionaccount != null) {
                    oldHireIdOfBadminitionaccountCollectionBadminitionaccount.getBadminitionaccountCollection().remove(badminitionaccountCollectionBadminitionaccount);
                    oldHireIdOfBadminitionaccountCollectionBadminitionaccount = em.merge(oldHireIdOfBadminitionaccountCollectionBadminitionaccount);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(BadimintionHire badimintionHire) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            BadimintionHire persistentBadimintionHire = em.find(BadimintionHire.class, badimintionHire.getId());
            Player payerOld = persistentBadimintionHire.getPayer();
            Player payerNew = badimintionHire.getPayer();
            Collection<Badminitionaccount> badminitionaccountCollectionOld = persistentBadimintionHire.getBadminitionaccountCollection();
            Collection<Badminitionaccount> badminitionaccountCollectionNew = badimintionHire.getBadminitionaccountCollection();
            if (payerNew != null) {
                payerNew = em.getReference(payerNew.getClass(), payerNew.getId());
                badimintionHire.setPayer(payerNew);
            }
            Collection<Badminitionaccount> attachedBadminitionaccountCollectionNew = new ArrayList<Badminitionaccount>();
            for (Badminitionaccount badminitionaccountCollectionNewBadminitionaccountToAttach : badminitionaccountCollectionNew) {
                badminitionaccountCollectionNewBadminitionaccountToAttach = em.getReference(badminitionaccountCollectionNewBadminitionaccountToAttach.getClass(), badminitionaccountCollectionNewBadminitionaccountToAttach.getId());
                attachedBadminitionaccountCollectionNew.add(badminitionaccountCollectionNewBadminitionaccountToAttach);
            }
            badminitionaccountCollectionNew = attachedBadminitionaccountCollectionNew;
            badimintionHire.setBadminitionaccountCollection(badminitionaccountCollectionNew);
            badimintionHire = em.merge(badimintionHire);
            if (payerOld != null && !payerOld.equals(payerNew)) {
                payerOld.getBadimintionHireCollection().remove(badimintionHire);
                payerOld = em.merge(payerOld);
            }
            if (payerNew != null && !payerNew.equals(payerOld)) {
                payerNew.getBadimintionHireCollection().add(badimintionHire);
                payerNew = em.merge(payerNew);
            }
            for (Badminitionaccount badminitionaccountCollectionOldBadminitionaccount : badminitionaccountCollectionOld) {
                if (!badminitionaccountCollectionNew.contains(badminitionaccountCollectionOldBadminitionaccount)) {
                    badminitionaccountCollectionOldBadminitionaccount.setHireId(null);
                    badminitionaccountCollectionOldBadminitionaccount = em.merge(badminitionaccountCollectionOldBadminitionaccount);
                }
            }
            for (Badminitionaccount badminitionaccountCollectionNewBadminitionaccount : badminitionaccountCollectionNew) {
                if (!badminitionaccountCollectionOld.contains(badminitionaccountCollectionNewBadminitionaccount)) {
                    BadimintionHire oldHireIdOfBadminitionaccountCollectionNewBadminitionaccount = badminitionaccountCollectionNewBadminitionaccount.getHireId();
                    badminitionaccountCollectionNewBadminitionaccount.setHireId(badimintionHire);
                    badminitionaccountCollectionNewBadminitionaccount = em.merge(badminitionaccountCollectionNewBadminitionaccount);
                    if (oldHireIdOfBadminitionaccountCollectionNewBadminitionaccount != null && !oldHireIdOfBadminitionaccountCollectionNewBadminitionaccount.equals(badimintionHire)) {
                        oldHireIdOfBadminitionaccountCollectionNewBadminitionaccount.getBadminitionaccountCollection().remove(badminitionaccountCollectionNewBadminitionaccount);
                        oldHireIdOfBadminitionaccountCollectionNewBadminitionaccount = em.merge(oldHireIdOfBadminitionaccountCollectionNewBadminitionaccount);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Long id = badimintionHire.getId();
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
            BadimintionHire badimintionHire;
            try {
                badimintionHire = em.getReference(BadimintionHire.class, id);
                badimintionHire.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The badimintionHire with id " + id + " no longer exists.", enfe);
            }
            Player payer = badimintionHire.getPayer();
            if (payer != null) {
                payer.getBadimintionHireCollection().remove(badimintionHire);
                payer = em.merge(payer);
            }
            Collection<Badminitionaccount> badminitionaccountCollection = badimintionHire.getBadminitionaccountCollection();
            for (Badminitionaccount badminitionaccountCollectionBadminitionaccount : badminitionaccountCollection) {
                badminitionaccountCollectionBadminitionaccount.setHireId(null);
                badminitionaccountCollectionBadminitionaccount = em.merge(badminitionaccountCollectionBadminitionaccount);
            }
            em.remove(badimintionHire);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<BadimintionHire> findBadimintionHireEntities() {
        return findBadimintionHireEntities(true, -1, -1);
    }

    public List<BadimintionHire> findBadimintionHireEntities(int maxResults, int firstResult) {
        return findBadimintionHireEntities(false, maxResults, firstResult);
    }

    private List<BadimintionHire> findBadimintionHireEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(BadimintionHire.class));
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

    public BadimintionHire findBadimintionHire(Long id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(BadimintionHire.class, id);
        } finally {
            em.close();
        }
    }

    public int getBadimintionHireCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<BadimintionHire> rt = cq.from(BadimintionHire.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
