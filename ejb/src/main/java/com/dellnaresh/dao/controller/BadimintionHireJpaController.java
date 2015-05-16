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
import com.dellnaresh.common.remote.entities.Badmintonaccount;
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

    public void create(BadmintonHire badmintonHire) {
        if (badmintonHire.getBadmintonaccountCollection() == null) {
            badmintonHire.setBadmintonaccountCollection(new ArrayList<Badmintonaccount>());
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
            Collection<Badmintonaccount> attachedBadmintonaccountCollection = new ArrayList<Badmintonaccount>();
            for (Badmintonaccount BadmintonaccountCollectionBadmintonaccountToAttach : badmintonHire.getBadmintonaccountCollection()) {
                BadmintonaccountCollectionBadmintonaccountToAttach = em.getReference(BadmintonaccountCollectionBadmintonaccountToAttach.getClass(), BadmintonaccountCollectionBadmintonaccountToAttach.getId());
                attachedBadmintonaccountCollection.add(BadmintonaccountCollectionBadmintonaccountToAttach);
            }
            badmintonHire.setBadmintonaccountCollection(attachedBadmintonaccountCollection);
            em.persist(badmintonHire);
            if (payer != null) {
                payer.getBadmintonHireCollection().add(badmintonHire);
                payer = em.merge(payer);
            }
            for (Badmintonaccount BadmintonaccountCollectionBadmintonaccount : badmintonHire.getBadmintonaccountCollection()) {
                BadmintonHire oldHireIdOfBadmintonaccountCollectionBadmintonaccount = BadmintonaccountCollectionBadmintonaccount.getHireId();
                BadmintonaccountCollectionBadmintonaccount.setHireId(badmintonHire);
                BadmintonaccountCollectionBadmintonaccount = em.merge(BadmintonaccountCollectionBadmintonaccount);
                if (oldHireIdOfBadmintonaccountCollectionBadmintonaccount != null) {
                    oldHireIdOfBadmintonaccountCollectionBadmintonaccount.getBadmintonaccountCollection().remove(BadmintonaccountCollectionBadmintonaccount);
                    oldHireIdOfBadmintonaccountCollectionBadmintonaccount = em.merge(oldHireIdOfBadmintonaccountCollectionBadmintonaccount);
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
            Collection<Badmintonaccount> BadmintonaccountCollectionOld = persistentBadmintonHire.getBadmintonaccountCollection();
            Collection<Badmintonaccount> BadmintonaccountCollectionNew = badmintonHire.getBadmintonaccountCollection();
            if (payerNew != null) {
                payerNew = em.getReference(payerNew.getClass(), payerNew.getId());
                badmintonHire.setPayer(payerNew);
            }
            Collection<Badmintonaccount> attachedBadmintonaccountCollectionNew = new ArrayList<Badmintonaccount>();
            for (Badmintonaccount BadmintonaccountCollectionNewBadmintonaccountToAttach : BadmintonaccountCollectionNew) {
                BadmintonaccountCollectionNewBadmintonaccountToAttach = em.getReference(BadmintonaccountCollectionNewBadmintonaccountToAttach.getClass(), BadmintonaccountCollectionNewBadmintonaccountToAttach.getId());
                attachedBadmintonaccountCollectionNew.add(BadmintonaccountCollectionNewBadmintonaccountToAttach);
            }
            BadmintonaccountCollectionNew = attachedBadmintonaccountCollectionNew;
            badmintonHire.setBadmintonaccountCollection(BadmintonaccountCollectionNew);
            badmintonHire = em.merge(badmintonHire);
            if (payerOld != null && !payerOld.equals(payerNew)) {
                payerOld.getBadmintonHireCollection().remove(badmintonHire);
                payerOld = em.merge(payerOld);
            }
            if (payerNew != null && !payerNew.equals(payerOld)) {
                payerNew.getBadmintonHireCollection().add(badmintonHire);
                payerNew = em.merge(payerNew);
            }
            for (Badmintonaccount BadmintonaccountCollectionOldBadmintonaccount : BadmintonaccountCollectionOld) {
                if (!BadmintonaccountCollectionNew.contains(BadmintonaccountCollectionOldBadmintonaccount)) {
                    BadmintonaccountCollectionOldBadmintonaccount.setHireId(null);
                    BadmintonaccountCollectionOldBadmintonaccount = em.merge(BadmintonaccountCollectionOldBadmintonaccount);
                }
            }
            for (Badmintonaccount BadmintonaccountCollectionNewBadmintonaccount : BadmintonaccountCollectionNew) {
                if (!BadmintonaccountCollectionOld.contains(BadmintonaccountCollectionNewBadmintonaccount)) {
                    BadmintonHire oldHireIdOfBadmintonaccountCollectionNewBadmintonaccount = BadmintonaccountCollectionNewBadmintonaccount.getHireId();
                    BadmintonaccountCollectionNewBadmintonaccount.setHireId(badmintonHire);
                    BadmintonaccountCollectionNewBadmintonaccount = em.merge(BadmintonaccountCollectionNewBadmintonaccount);
                    if (oldHireIdOfBadmintonaccountCollectionNewBadmintonaccount != null && !oldHireIdOfBadmintonaccountCollectionNewBadmintonaccount.equals(badmintonHire)) {
                        oldHireIdOfBadmintonaccountCollectionNewBadmintonaccount.getBadmintonaccountCollection().remove(BadmintonaccountCollectionNewBadmintonaccount);
                        oldHireIdOfBadmintonaccountCollectionNewBadmintonaccount = em.merge(oldHireIdOfBadmintonaccountCollectionNewBadmintonaccount);
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
            Collection<Badmintonaccount> BadmintonaccountCollection = badmintonHire.getBadmintonaccountCollection();
            for (Badmintonaccount BadmintonaccountCollectionBadmintonaccount : BadmintonaccountCollection) {
                BadmintonaccountCollectionBadmintonaccount.setHireId(null);
                BadmintonaccountCollectionBadmintonaccount = em.merge(BadmintonaccountCollectionBadmintonaccount);
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
