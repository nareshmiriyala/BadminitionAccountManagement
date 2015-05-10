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
import com.dellnaresh.common.remote.entities.Users;
import com.dellnaresh.common.remote.entities.Badminitionaccount;
import java.util.ArrayList;
import java.util.Collection;
import com.dellnaresh.common.remote.entities.BadimintionHire;
import com.dellnaresh.common.remote.entities.Player;
import com.dellnaresh.dao.controller.exceptions.IllegalOrphanException;
import com.dellnaresh.dao.controller.exceptions.NonexistentEntityException;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author nareshm
 */
public class PlayerJpaController implements Serializable {

    public PlayerJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Player player) {
        if (player.getBadminitionaccountCollection() == null) {
            player.setBadminitionaccountCollection(new ArrayList<Badminitionaccount>());
        }
        if (player.getBadimintionHireCollection() == null) {
            player.setBadimintionHireCollection(new ArrayList<BadimintionHire>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Users users = player.getUsers();
            if (users != null) {
                users = em.getReference(users.getClass(), users.getLogin());
                player.setUsers(users);
            }
            Collection<Badminitionaccount> attachedBadminitionaccountCollection = new ArrayList<Badminitionaccount>();
            for (Badminitionaccount badminitionaccountCollectionBadminitionaccountToAttach : player.getBadminitionaccountCollection()) {
                badminitionaccountCollectionBadminitionaccountToAttach = em.getReference(badminitionaccountCollectionBadminitionaccountToAttach.getClass(), badminitionaccountCollectionBadminitionaccountToAttach.getId());
                attachedBadminitionaccountCollection.add(badminitionaccountCollectionBadminitionaccountToAttach);
            }
            player.setBadminitionaccountCollection(attachedBadminitionaccountCollection);
            Collection<BadimintionHire> attachedBadimintionHireCollection = new ArrayList<BadimintionHire>();
            for (BadimintionHire badimintionHireCollectionBadimintionHireToAttach : player.getBadimintionHireCollection()) {
                badimintionHireCollectionBadimintionHireToAttach = em.getReference(badimintionHireCollectionBadimintionHireToAttach.getClass(), badimintionHireCollectionBadimintionHireToAttach.getId());
                attachedBadimintionHireCollection.add(badimintionHireCollectionBadimintionHireToAttach);
            }
            player.setBadimintionHireCollection(attachedBadimintionHireCollection);
            em.persist(player);
            if (users != null) {
                Player oldPlayerOfUsers = users.getPlayer();
                if (oldPlayerOfUsers != null) {
                    oldPlayerOfUsers.setUsers(null);
                    oldPlayerOfUsers = em.merge(oldPlayerOfUsers);
                }
                users.setPlayer(player);
                users = em.merge(users);
            }
            for (Badminitionaccount badminitionaccountCollectionBadminitionaccount : player.getBadminitionaccountCollection()) {
                Player oldPlayerIdOfBadminitionaccountCollectionBadminitionaccount = badminitionaccountCollectionBadminitionaccount.getPlayerId();
                badminitionaccountCollectionBadminitionaccount.setPlayerId(player);
                badminitionaccountCollectionBadminitionaccount = em.merge(badminitionaccountCollectionBadminitionaccount);
                if (oldPlayerIdOfBadminitionaccountCollectionBadminitionaccount != null) {
                    oldPlayerIdOfBadminitionaccountCollectionBadminitionaccount.getBadminitionaccountCollection().remove(badminitionaccountCollectionBadminitionaccount);
                    oldPlayerIdOfBadminitionaccountCollectionBadminitionaccount = em.merge(oldPlayerIdOfBadminitionaccountCollectionBadminitionaccount);
                }
            }
            for (BadimintionHire badimintionHireCollectionBadimintionHire : player.getBadimintionHireCollection()) {
                Player oldPayerOfBadimintionHireCollectionBadimintionHire = badimintionHireCollectionBadimintionHire.getPayer();
                badimintionHireCollectionBadimintionHire.setPayer(player);
                badimintionHireCollectionBadimintionHire = em.merge(badimintionHireCollectionBadimintionHire);
                if (oldPayerOfBadimintionHireCollectionBadimintionHire != null) {
                    oldPayerOfBadimintionHireCollectionBadimintionHire.getBadimintionHireCollection().remove(badimintionHireCollectionBadimintionHire);
                    oldPayerOfBadimintionHireCollectionBadimintionHire = em.merge(oldPayerOfBadimintionHireCollectionBadimintionHire);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Player player) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Player persistentPlayer = em.find(Player.class, player.getId());
            Users usersOld = persistentPlayer.getUsers();
            Users usersNew = player.getUsers();
            Collection<Badminitionaccount> badminitionaccountCollectionOld = persistentPlayer.getBadminitionaccountCollection();
            Collection<Badminitionaccount> badminitionaccountCollectionNew = player.getBadminitionaccountCollection();
            Collection<BadimintionHire> badimintionHireCollectionOld = persistentPlayer.getBadimintionHireCollection();
            Collection<BadimintionHire> badimintionHireCollectionNew = player.getBadimintionHireCollection();
            List<String> illegalOrphanMessages = null;
            if (usersOld != null && !usersOld.equals(usersNew)) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("You must retain Users " + usersOld + " since its player field is not nullable.");
            }
            for (Badminitionaccount badminitionaccountCollectionOldBadminitionaccount : badminitionaccountCollectionOld) {
                if (!badminitionaccountCollectionNew.contains(badminitionaccountCollectionOldBadminitionaccount)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Badminitionaccount " + badminitionaccountCollectionOldBadminitionaccount + " since its playerId field is not nullable.");
                }
            }
            for (BadimintionHire badimintionHireCollectionOldBadimintionHire : badimintionHireCollectionOld) {
                if (!badimintionHireCollectionNew.contains(badimintionHireCollectionOldBadimintionHire)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain BadimintionHire " + badimintionHireCollectionOldBadimintionHire + " since its payer field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (usersNew != null) {
                usersNew = em.getReference(usersNew.getClass(), usersNew.getLogin());
                player.setUsers(usersNew);
            }
            Collection<Badminitionaccount> attachedBadminitionaccountCollectionNew = new ArrayList<Badminitionaccount>();
            for (Badminitionaccount badminitionaccountCollectionNewBadminitionaccountToAttach : badminitionaccountCollectionNew) {
                badminitionaccountCollectionNewBadminitionaccountToAttach = em.getReference(badminitionaccountCollectionNewBadminitionaccountToAttach.getClass(), badminitionaccountCollectionNewBadminitionaccountToAttach.getId());
                attachedBadminitionaccountCollectionNew.add(badminitionaccountCollectionNewBadminitionaccountToAttach);
            }
            badminitionaccountCollectionNew = attachedBadminitionaccountCollectionNew;
            player.setBadminitionaccountCollection(badminitionaccountCollectionNew);
            Collection<BadimintionHire> attachedBadimintionHireCollectionNew = new ArrayList<BadimintionHire>();
            for (BadimintionHire badimintionHireCollectionNewBadimintionHireToAttach : badimintionHireCollectionNew) {
                badimintionHireCollectionNewBadimintionHireToAttach = em.getReference(badimintionHireCollectionNewBadimintionHireToAttach.getClass(), badimintionHireCollectionNewBadimintionHireToAttach.getId());
                attachedBadimintionHireCollectionNew.add(badimintionHireCollectionNewBadimintionHireToAttach);
            }
            badimintionHireCollectionNew = attachedBadimintionHireCollectionNew;
            player.setBadimintionHireCollection(badimintionHireCollectionNew);
            player = em.merge(player);
            if (usersNew != null && !usersNew.equals(usersOld)) {
                Player oldPlayerOfUsers = usersNew.getPlayer();
                if (oldPlayerOfUsers != null) {
                    oldPlayerOfUsers.setUsers(null);
                    oldPlayerOfUsers = em.merge(oldPlayerOfUsers);
                }
                usersNew.setPlayer(player);
                usersNew = em.merge(usersNew);
            }
            for (Badminitionaccount badminitionaccountCollectionNewBadminitionaccount : badminitionaccountCollectionNew) {
                if (!badminitionaccountCollectionOld.contains(badminitionaccountCollectionNewBadminitionaccount)) {
                    Player oldPlayerIdOfBadminitionaccountCollectionNewBadminitionaccount = badminitionaccountCollectionNewBadminitionaccount.getPlayerId();
                    badminitionaccountCollectionNewBadminitionaccount.setPlayerId(player);
                    badminitionaccountCollectionNewBadminitionaccount = em.merge(badminitionaccountCollectionNewBadminitionaccount);
                    if (oldPlayerIdOfBadminitionaccountCollectionNewBadminitionaccount != null && !oldPlayerIdOfBadminitionaccountCollectionNewBadminitionaccount.equals(player)) {
                        oldPlayerIdOfBadminitionaccountCollectionNewBadminitionaccount.getBadminitionaccountCollection().remove(badminitionaccountCollectionNewBadminitionaccount);
                        oldPlayerIdOfBadminitionaccountCollectionNewBadminitionaccount = em.merge(oldPlayerIdOfBadminitionaccountCollectionNewBadminitionaccount);
                    }
                }
            }
            for (BadimintionHire badimintionHireCollectionNewBadimintionHire : badimintionHireCollectionNew) {
                if (!badimintionHireCollectionOld.contains(badimintionHireCollectionNewBadimintionHire)) {
                    Player oldPayerOfBadimintionHireCollectionNewBadimintionHire = badimintionHireCollectionNewBadimintionHire.getPayer();
                    badimintionHireCollectionNewBadimintionHire.setPayer(player);
                    badimintionHireCollectionNewBadimintionHire = em.merge(badimintionHireCollectionNewBadimintionHire);
                    if (oldPayerOfBadimintionHireCollectionNewBadimintionHire != null && !oldPayerOfBadimintionHireCollectionNewBadimintionHire.equals(player)) {
                        oldPayerOfBadimintionHireCollectionNewBadimintionHire.getBadimintionHireCollection().remove(badimintionHireCollectionNewBadimintionHire);
                        oldPayerOfBadimintionHireCollectionNewBadimintionHire = em.merge(oldPayerOfBadimintionHireCollectionNewBadimintionHire);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = player.getId();
                if (findPlayer(id) == null) {
                    throw new NonexistentEntityException("The player with id " + id + " no longer exists.");
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
            Player player;
            try {
                player = em.getReference(Player.class, id);
                player.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The player with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            Users usersOrphanCheck = player.getUsers();
            if (usersOrphanCheck != null) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Player (" + player + ") cannot be destroyed since the Users " + usersOrphanCheck + " in its users field has a non-nullable player field.");
            }
            Collection<Badminitionaccount> badminitionaccountCollectionOrphanCheck = player.getBadminitionaccountCollection();
            for (Badminitionaccount badminitionaccountCollectionOrphanCheckBadminitionaccount : badminitionaccountCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Player (" + player + ") cannot be destroyed since the Badminitionaccount " + badminitionaccountCollectionOrphanCheckBadminitionaccount + " in its badminitionaccountCollection field has a non-nullable playerId field.");
            }
            Collection<BadimintionHire> badimintionHireCollectionOrphanCheck = player.getBadimintionHireCollection();
            for (BadimintionHire badimintionHireCollectionOrphanCheckBadimintionHire : badimintionHireCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Player (" + player + ") cannot be destroyed since the BadimintionHire " + badimintionHireCollectionOrphanCheckBadimintionHire + " in its badimintionHireCollection field has a non-nullable payer field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(player);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Player> findPlayerEntities() {
        return findPlayerEntities(true, -1, -1);
    }

    public List<Player> findPlayerEntities(int maxResults, int firstResult) {
        return findPlayerEntities(false, maxResults, firstResult);
    }

    private List<Player> findPlayerEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Player.class));
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

    public Player findPlayer(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Player.class, id);
        } finally {
            em.close();
        }
    }

    public int getPlayerCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Player> rt = cq.from(Player.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
