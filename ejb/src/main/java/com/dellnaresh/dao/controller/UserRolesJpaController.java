/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dellnaresh.dao.controller;

import com.dellnaresh.common.remote.entities.UserRoles;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import com.dellnaresh.common.remote.entities.Users;
import com.dellnaresh.dao.controller.exceptions.NonexistentEntityException;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author nareshm
 */
public class UserRolesJpaController implements Serializable {

    public UserRolesJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(UserRoles userRoles) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Users userid = userRoles.getUserid();
            if (userid != null) {
                userid = em.getReference(userid.getClass(), userid.getLogin());
                userRoles.setUserid(userid);
            }
            em.persist(userRoles);
            if (userid != null) {
                userid.getUserRolesCollection().add(userRoles);
                userid = em.merge(userid);
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(UserRoles userRoles) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            UserRoles persistentUserRoles = em.find(UserRoles.class, userRoles.getRoleid());
            Users useridOld = persistentUserRoles.getUserid();
            Users useridNew = userRoles.getUserid();
            if (useridNew != null) {
                useridNew = em.getReference(useridNew.getClass(), useridNew.getLogin());
                userRoles.setUserid(useridNew);
            }
            userRoles = em.merge(userRoles);
            if (useridOld != null && !useridOld.equals(useridNew)) {
                useridOld.getUserRolesCollection().remove(userRoles);
                useridOld = em.merge(useridOld);
            }
            if (useridNew != null && !useridNew.equals(useridOld)) {
                useridNew.getUserRolesCollection().add(userRoles);
                useridNew = em.merge(useridNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Long id = userRoles.getRoleid();
                if (findUserRoles(id) == null) {
                    throw new NonexistentEntityException("The userRoles with id " + id + " no longer exists.");
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
            UserRoles userRoles;
            try {
                userRoles = em.getReference(UserRoles.class, id);
                userRoles.getRoleid();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The userRoles with id " + id + " no longer exists.", enfe);
            }
            Users userid = userRoles.getUserid();
            if (userid != null) {
                userid.getUserRolesCollection().remove(userRoles);
                userid = em.merge(userid);
            }
            em.remove(userRoles);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<UserRoles> findUserRolesEntities() {
        return findUserRolesEntities(true, -1, -1);
    }

    public List<UserRoles> findUserRolesEntities(int maxResults, int firstResult) {
        return findUserRolesEntities(false, maxResults, firstResult);
    }

    private List<UserRoles> findUserRolesEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(UserRoles.class));
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

    public UserRoles findUserRoles(Long id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(UserRoles.class, id);
        } finally {
            em.close();
        }
    }

    public int getUserRolesCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<UserRoles> rt = cq.from(UserRoles.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
