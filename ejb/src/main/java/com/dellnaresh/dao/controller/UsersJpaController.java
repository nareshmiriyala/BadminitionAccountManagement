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
import com.dellnaresh.common.remote.entities.Player;
import com.dellnaresh.common.remote.entities.UserRoles;
import com.dellnaresh.common.remote.entities.Users;
import com.dellnaresh.dao.controller.exceptions.IllegalOrphanException;
import com.dellnaresh.dao.controller.exceptions.NonexistentEntityException;
import com.dellnaresh.dao.controller.exceptions.PreexistingEntityException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author nareshm
 */
public class UsersJpaController implements Serializable {

    public UsersJpaController(EntityManager emf) {
        this.em = em;
    }
    private EntityManager em = null;

    public EntityManager getEntityManager() {
        return em;
    }

    public void create(Users users) throws IllegalOrphanException, PreexistingEntityException, Exception {
        if (users.getUserRolesCollection() == null) {
            users.setUserRolesCollection(new ArrayList<UserRoles>());
        }
        List<String> illegalOrphanMessages = null;
        Player playerOrphanCheck = users.getPlayer();
        if (playerOrphanCheck != null) {
            Users oldUsersOfPlayer = playerOrphanCheck.getUsers();
            if (oldUsersOfPlayer != null) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("The Player " + playerOrphanCheck + " already has an item of type Users whose player column cannot be null. Please make another selection for the player field.");
            }
        }
        if (illegalOrphanMessages != null) {
            throw new IllegalOrphanException(illegalOrphanMessages);
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Player player = users.getPlayer();
            if (player != null) {
                player = em.getReference(player.getClass(), player.getId());
                users.setPlayer(player);
            }
            Collection<UserRoles> attachedUserRolesCollection = new ArrayList<UserRoles>();
            for (UserRoles userRolesCollectionUserRolesToAttach : users.getUserRolesCollection()) {
                userRolesCollectionUserRolesToAttach = em.getReference(userRolesCollectionUserRolesToAttach.getClass(), userRolesCollectionUserRolesToAttach.getRoleid());
                attachedUserRolesCollection.add(userRolesCollectionUserRolesToAttach);
            }
            users.setUserRolesCollection(attachedUserRolesCollection);
            em.persist(users);
            if (player != null) {
                player.setUsers(users);
                player = em.merge(player);
            }
            for (UserRoles userRolesCollectionUserRoles : users.getUserRolesCollection()) {
                Users oldUseridOfUserRolesCollectionUserRoles = userRolesCollectionUserRoles.getUserid();
                userRolesCollectionUserRoles.setUserid(users);
                userRolesCollectionUserRoles = em.merge(userRolesCollectionUserRoles);
                if (oldUseridOfUserRolesCollectionUserRoles != null) {
                    oldUseridOfUserRolesCollectionUserRoles.getUserRolesCollection().remove(userRolesCollectionUserRoles);
                    oldUseridOfUserRolesCollectionUserRoles = em.merge(oldUseridOfUserRolesCollectionUserRoles);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findUsers(users.getLogin()) != null) {
                throw new PreexistingEntityException("Users " + users + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Users users) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Users persistentUsers = em.find(Users.class, users.getLogin());
            Player playerOld = persistentUsers.getPlayer();
            Player playerNew = users.getPlayer();
            Collection<UserRoles> userRolesCollectionOld = persistentUsers.getUserRolesCollection();
            Collection<UserRoles> userRolesCollectionNew = users.getUserRolesCollection();
            List<String> illegalOrphanMessages = null;
            if (playerNew != null && !playerNew.equals(playerOld)) {
                Users oldUsersOfPlayer = playerNew.getUsers();
                if (oldUsersOfPlayer != null) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("The Player " + playerNew + " already has an item of type Users whose player column cannot be null. Please make another selection for the player field.");
                }
            }
            for (UserRoles userRolesCollectionOldUserRoles : userRolesCollectionOld) {
                if (!userRolesCollectionNew.contains(userRolesCollectionOldUserRoles)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain UserRoles " + userRolesCollectionOldUserRoles + " since its userid field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (playerNew != null) {
                playerNew = em.getReference(playerNew.getClass(), playerNew.getId());
                users.setPlayer(playerNew);
            }
            Collection<UserRoles> attachedUserRolesCollectionNew = new ArrayList<UserRoles>();
            for (UserRoles userRolesCollectionNewUserRolesToAttach : userRolesCollectionNew) {
                userRolesCollectionNewUserRolesToAttach = em.getReference(userRolesCollectionNewUserRolesToAttach.getClass(), userRolesCollectionNewUserRolesToAttach.getRoleid());
                attachedUserRolesCollectionNew.add(userRolesCollectionNewUserRolesToAttach);
            }
            userRolesCollectionNew = attachedUserRolesCollectionNew;
            users.setUserRolesCollection(userRolesCollectionNew);
            users = em.merge(users);
            if (playerOld != null && !playerOld.equals(playerNew)) {
                playerOld.setUsers(null);
                playerOld = em.merge(playerOld);
            }
            if (playerNew != null && !playerNew.equals(playerOld)) {
                playerNew.setUsers(users);
                playerNew = em.merge(playerNew);
            }
            for (UserRoles userRolesCollectionNewUserRoles : userRolesCollectionNew) {
                if (!userRolesCollectionOld.contains(userRolesCollectionNewUserRoles)) {
                    Users oldUseridOfUserRolesCollectionNewUserRoles = userRolesCollectionNewUserRoles.getUserid();
                    userRolesCollectionNewUserRoles.setUserid(users);
                    userRolesCollectionNewUserRoles = em.merge(userRolesCollectionNewUserRoles);
                    if (oldUseridOfUserRolesCollectionNewUserRoles != null && !oldUseridOfUserRolesCollectionNewUserRoles.equals(users)) {
                        oldUseridOfUserRolesCollectionNewUserRoles.getUserRolesCollection().remove(userRolesCollectionNewUserRoles);
                        oldUseridOfUserRolesCollectionNewUserRoles = em.merge(oldUseridOfUserRolesCollectionNewUserRoles);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = users.getLogin();
                if (findUsers(id) == null) {
                    throw new NonexistentEntityException("The users with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Integer id) throws IllegalOrphanException, NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Users users;
            try {
                users = em.getReference(Users.class, id);
                users.getLogin();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The users with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            Collection<UserRoles> userRolesCollectionOrphanCheck = users.getUserRolesCollection();
            for (UserRoles userRolesCollectionOrphanCheckUserRoles : userRolesCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Users (" + users + ") cannot be destroyed since the UserRoles " + userRolesCollectionOrphanCheckUserRoles + " in its userRolesCollection field has a non-nullable userid field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Player player = users.getPlayer();
            if (player != null) {
                player.setUsers(null);
                player = em.merge(player);
            }
            em.remove(users);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Users> findUsersEntities() {
        return findUsersEntities(true, -1, -1);
    }

    public List<Users> findUsersEntities(int maxResults, int firstResult) {
        return findUsersEntities(false, maxResults, firstResult);
    }

    private List<Users> findUsersEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Users.class));
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

    public Users findUsers(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Users.class, id);
        } finally {
            em.close();
        }
    }

    public int getUsersCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Users> rt = cq.from(Users.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
