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
import com.dellnaresh.common.remote.entities.Badmintonaccount;
import java.util.ArrayList;
import java.util.Collection;
import com.dellnaresh.common.remote.entities.BadmintonHire;
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
        if (player.getBadmintonaccountCollection() == null) {
            player.setBadmintonaccountCollection(new ArrayList<Badmintonaccount>());
        }
        if (player.getBadmintonHireCollection() == null) {
            player.setBadmintonHireCollection(new ArrayList<BadmintonHire>());
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
            Collection<Badmintonaccount> attachedBadmintonaccountCollection = new ArrayList<Badmintonaccount>();
            for (Badmintonaccount BadmintonaccountCollectionBadmintonaccountToAttach : player.getBadmintonaccountCollection()) {
                BadmintonaccountCollectionBadmintonaccountToAttach = em.getReference(BadmintonaccountCollectionBadmintonaccountToAttach.getClass(), BadmintonaccountCollectionBadmintonaccountToAttach.getId());
                attachedBadmintonaccountCollection.add(BadmintonaccountCollectionBadmintonaccountToAttach);
            }
            player.setBadmintonaccountCollection(attachedBadmintonaccountCollection);
            Collection<BadmintonHire> attachedBadmintonHireCollection = new ArrayList<BadmintonHire>();
            for (BadmintonHire badimintionHireCollectionBadmintonHireToAttach : player.getBadmintonHireCollection()) {
                badimintionHireCollectionBadmintonHireToAttach = em.getReference(badimintionHireCollectionBadmintonHireToAttach.getClass(), badimintionHireCollectionBadmintonHireToAttach.getId());
                attachedBadmintonHireCollection.add(badimintionHireCollectionBadmintonHireToAttach);
            }
            player.setBadmintonHireCollection(attachedBadmintonHireCollection);
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
            for (Badmintonaccount BadmintonaccountCollectionBadmintonaccount : player.getBadmintonaccountCollection()) {
                Player oldPlayerIdOfBadmintonaccountCollectionBadmintonaccount = BadmintonaccountCollectionBadmintonaccount.getPlayerId();
                BadmintonaccountCollectionBadmintonaccount.setPlayerId(player);
                BadmintonaccountCollectionBadmintonaccount = em.merge(BadmintonaccountCollectionBadmintonaccount);
                if (oldPlayerIdOfBadmintonaccountCollectionBadmintonaccount != null) {
                    oldPlayerIdOfBadmintonaccountCollectionBadmintonaccount.getBadmintonaccountCollection().remove(BadmintonaccountCollectionBadmintonaccount);
                    oldPlayerIdOfBadmintonaccountCollectionBadmintonaccount = em.merge(oldPlayerIdOfBadmintonaccountCollectionBadmintonaccount);
                }
            }
            for (BadmintonHire badimintionHireCollectionBadmintonHire : player.getBadmintonHireCollection()) {
                Player oldPayerOfBadimintionHireCollectionBadimintionHire = badimintionHireCollectionBadmintonHire.getPayer();
                badimintionHireCollectionBadmintonHire.setPayer(player);
                badimintionHireCollectionBadmintonHire = em.merge(badimintionHireCollectionBadmintonHire);
                if (oldPayerOfBadimintionHireCollectionBadimintionHire != null) {
                    oldPayerOfBadimintionHireCollectionBadimintionHire.getBadmintonHireCollection().remove(badimintionHireCollectionBadmintonHire);
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
            Collection<Badmintonaccount> BadmintonaccountCollectionOld = persistentPlayer.getBadmintonaccountCollection();
            Collection<Badmintonaccount> BadmintonaccountCollectionNew = player.getBadmintonaccountCollection();
            Collection<BadmintonHire> badmintonHireCollectionOld = persistentPlayer.getBadmintonHireCollection();
            Collection<BadmintonHire> badmintonHireCollectionNew = player.getBadmintonHireCollection();
            List<String> illegalOrphanMessages = null;
            if (usersOld != null && !usersOld.equals(usersNew)) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("You must retain Users " + usersOld + " since its player field is not nullable.");
            }
            for (Badmintonaccount BadmintonaccountCollectionOldBadmintonaccount : BadmintonaccountCollectionOld) {
                if (!BadmintonaccountCollectionNew.contains(BadmintonaccountCollectionOldBadmintonaccount)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Badmintonaccount " + BadmintonaccountCollectionOldBadmintonaccount + " since its playerId field is not nullable.");
                }
            }
            for (BadmintonHire badimintionHireCollectionOldBadmintonHire : badmintonHireCollectionOld) {
                if (!badmintonHireCollectionNew.contains(badimintionHireCollectionOldBadmintonHire)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain BadimintionHire " + badimintionHireCollectionOldBadmintonHire + " since its payer field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (usersNew != null) {
                usersNew = em.getReference(usersNew.getClass(), usersNew.getLogin());
                player.setUsers(usersNew);
            }
            Collection<Badmintonaccount> attachedBadmintonaccountCollectionNew = new ArrayList<Badmintonaccount>();
            for (Badmintonaccount BadmintonaccountCollectionNewBadmintonaccountToAttach : BadmintonaccountCollectionNew) {
                BadmintonaccountCollectionNewBadmintonaccountToAttach = em.getReference(BadmintonaccountCollectionNewBadmintonaccountToAttach.getClass(), BadmintonaccountCollectionNewBadmintonaccountToAttach.getId());
                attachedBadmintonaccountCollectionNew.add(BadmintonaccountCollectionNewBadmintonaccountToAttach);
            }
            BadmintonaccountCollectionNew = attachedBadmintonaccountCollectionNew;
            player.setBadmintonaccountCollection(BadmintonaccountCollectionNew);
            Collection<BadmintonHire> attachedBadmintonHireCollectionNew = new ArrayList<BadmintonHire>();
            for (BadmintonHire badimintionHireCollectionNewBadmintonHireToAttach : badmintonHireCollectionNew) {
                badimintionHireCollectionNewBadmintonHireToAttach = em.getReference(badimintionHireCollectionNewBadmintonHireToAttach.getClass(), badimintionHireCollectionNewBadmintonHireToAttach.getId());
                attachedBadmintonHireCollectionNew.add(badimintionHireCollectionNewBadmintonHireToAttach);
            }
            badmintonHireCollectionNew = attachedBadmintonHireCollectionNew;
            player.setBadmintonHireCollection(badmintonHireCollectionNew);
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
            for (Badmintonaccount BadmintonaccountCollectionNewBadmintonaccount : BadmintonaccountCollectionNew) {
                if (!BadmintonaccountCollectionOld.contains(BadmintonaccountCollectionNewBadmintonaccount)) {
                    Player oldPlayerIdOfBadmintonaccountCollectionNewBadmintonaccount = BadmintonaccountCollectionNewBadmintonaccount.getPlayerId();
                    BadmintonaccountCollectionNewBadmintonaccount.setPlayerId(player);
                    BadmintonaccountCollectionNewBadmintonaccount = em.merge(BadmintonaccountCollectionNewBadmintonaccount);
                    if (oldPlayerIdOfBadmintonaccountCollectionNewBadmintonaccount != null && !oldPlayerIdOfBadmintonaccountCollectionNewBadmintonaccount.equals(player)) {
                        oldPlayerIdOfBadmintonaccountCollectionNewBadmintonaccount.getBadmintonaccountCollection().remove(BadmintonaccountCollectionNewBadmintonaccount);
                        oldPlayerIdOfBadmintonaccountCollectionNewBadmintonaccount = em.merge(oldPlayerIdOfBadmintonaccountCollectionNewBadmintonaccount);
                    }
                }
            }
            for (BadmintonHire badimintionHireCollectionNewBadmintonHire : badmintonHireCollectionNew) {
                if (!badmintonHireCollectionOld.contains(badimintionHireCollectionNewBadmintonHire)) {
                    Player oldPayerOfBadimintionHireCollectionNewBadimintionHire = badimintionHireCollectionNewBadmintonHire.getPayer();
                    badimintionHireCollectionNewBadmintonHire.setPayer(player);
                    badimintionHireCollectionNewBadmintonHire = em.merge(badimintionHireCollectionNewBadmintonHire);
                    if (oldPayerOfBadimintionHireCollectionNewBadimintionHire != null && !oldPayerOfBadimintionHireCollectionNewBadimintionHire.equals(player)) {
                        oldPayerOfBadimintionHireCollectionNewBadimintionHire.getBadmintonHireCollection().remove(badimintionHireCollectionNewBadmintonHire);
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
            Collection<Badmintonaccount> BadmintonaccountCollectionOrphanCheck = player.getBadmintonaccountCollection();
            for (Badmintonaccount BadmintonaccountCollectionOrphanCheckBadmintonaccount : BadmintonaccountCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Player (" + player + ") cannot be destroyed since the Badmintonaccount " + BadmintonaccountCollectionOrphanCheckBadmintonaccount + " in its BadmintonaccountCollection field has a non-nullable playerId field.");
            }
            Collection<BadmintonHire> badmintonHireCollectionOrphanCheck = player.getBadmintonHireCollection();
            for (BadmintonHire badimintionHireCollectionOrphanCheckBadmintonHire : badmintonHireCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Player (" + player + ") cannot be destroyed since the BadimintionHire " + badimintionHireCollectionOrphanCheckBadmintonHire + " in its badimintionHireCollection field has a non-nullable payer field.");
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
