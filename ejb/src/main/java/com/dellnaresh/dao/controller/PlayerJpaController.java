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
import com.dellnaresh.common.remote.entities.BadmintonAccount;
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
        if (player.getBadmintonAccountCollection() == null) {
            player.setBadmintonAccountCollection(new ArrayList<BadmintonAccount>());
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
            Collection<BadmintonAccount> attachedBadmintonAccountCollection = new ArrayList<BadmintonAccount>();
            for (BadmintonAccount badmintonaccountCollectionBadmintonAccountToAttach : player.getBadmintonAccountCollection()) {
                badmintonaccountCollectionBadmintonAccountToAttach = em.getReference(badmintonaccountCollectionBadmintonAccountToAttach.getClass(), badmintonaccountCollectionBadmintonAccountToAttach.getId());
                attachedBadmintonAccountCollection.add(badmintonaccountCollectionBadmintonAccountToAttach);
            }
            player.setBadmintonAccountCollection(attachedBadmintonAccountCollection);
            Collection<BadmintonHire> attachedBadmintonHireCollection = new ArrayList<BadmintonHire>();
            for (BadmintonHire BadmintonHireCollectionBadmintonHireToAttach : player.getBadmintonHireCollection()) {
                BadmintonHireCollectionBadmintonHireToAttach = em.getReference(BadmintonHireCollectionBadmintonHireToAttach.getClass(), BadmintonHireCollectionBadmintonHireToAttach.getId());
                attachedBadmintonHireCollection.add(BadmintonHireCollectionBadmintonHireToAttach);
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
            for (BadmintonAccount badmintonaccountCollectionBadmintonAccount : player.getBadmintonAccountCollection()) {
                Player oldPlayerIdOfBadmintonAccountCollectionBadmintonAccount = badmintonaccountCollectionBadmintonAccount.getPlayerId();
                badmintonaccountCollectionBadmintonAccount.setPlayerId(player);
                badmintonaccountCollectionBadmintonAccount = em.merge(badmintonaccountCollectionBadmintonAccount);
                if (oldPlayerIdOfBadmintonAccountCollectionBadmintonAccount != null) {
                    oldPlayerIdOfBadmintonAccountCollectionBadmintonAccount.getBadmintonAccountCollection().remove(badmintonaccountCollectionBadmintonAccount);
                    oldPlayerIdOfBadmintonAccountCollectionBadmintonAccount = em.merge(oldPlayerIdOfBadmintonAccountCollectionBadmintonAccount);
                }
            }
            for (BadmintonHire BadmintonHireCollectionBadmintonHire : player.getBadmintonHireCollection()) {
                Player oldPayerOfBadmintonHireCollectionBadmintonHire = BadmintonHireCollectionBadmintonHire.getPayer();
                BadmintonHireCollectionBadmintonHire.setPayer(player);
                BadmintonHireCollectionBadmintonHire = em.merge(BadmintonHireCollectionBadmintonHire);
                if (oldPayerOfBadmintonHireCollectionBadmintonHire != null) {
                    oldPayerOfBadmintonHireCollectionBadmintonHire.getBadmintonHireCollection().remove(BadmintonHireCollectionBadmintonHire);
                    oldPayerOfBadmintonHireCollectionBadmintonHire = em.merge(oldPayerOfBadmintonHireCollectionBadmintonHire);
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
            Collection<BadmintonAccount> badmintonAccountCollectionOld = persistentPlayer.getBadmintonAccountCollection();
            Collection<BadmintonAccount> badmintonAccountCollectionNew = player.getBadmintonAccountCollection();
            Collection<BadmintonHire> badmintonHireCollectionOld = persistentPlayer.getBadmintonHireCollection();
            Collection<BadmintonHire> badmintonHireCollectionNew = player.getBadmintonHireCollection();
            List<String> illegalOrphanMessages = null;
            if (usersOld != null && !usersOld.equals(usersNew)) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("You must retain Users " + usersOld + " since its player field is not nullable.");
            }
            for (BadmintonAccount badmintonaccountCollectionOldBadmintonAccount : badmintonAccountCollectionOld) {
                if (!badmintonAccountCollectionNew.contains(badmintonaccountCollectionOldBadmintonAccount)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain BadmintonAccount " + badmintonaccountCollectionOldBadmintonAccount + " since its playerId field is not nullable.");
                }
            }
            for (BadmintonHire BadmintonHireCollectionOldBadmintonHire : badmintonHireCollectionOld) {
                if (!badmintonHireCollectionNew.contains(BadmintonHireCollectionOldBadmintonHire)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain BadmintonHire " + BadmintonHireCollectionOldBadmintonHire + " since its payer field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (usersNew != null) {
                usersNew = em.getReference(usersNew.getClass(), usersNew.getLogin());
                player.setUsers(usersNew);
            }
            Collection<BadmintonAccount> attachedBadmintonAccountCollectionNew = new ArrayList<BadmintonAccount>();
            for (BadmintonAccount badmintonaccountCollectionNewBadmintonAccountToAttach : badmintonAccountCollectionNew) {
                badmintonaccountCollectionNewBadmintonAccountToAttach = em.getReference(badmintonaccountCollectionNewBadmintonAccountToAttach.getClass(), badmintonaccountCollectionNewBadmintonAccountToAttach.getId());
                attachedBadmintonAccountCollectionNew.add(badmintonaccountCollectionNewBadmintonAccountToAttach);
            }
            badmintonAccountCollectionNew = attachedBadmintonAccountCollectionNew;
            player.setBadmintonAccountCollection(badmintonAccountCollectionNew);
            Collection<BadmintonHire> attachedBadmintonHireCollectionNew = new ArrayList<BadmintonHire>();
            for (BadmintonHire BadmintonHireCollectionNewBadmintonHireToAttach : badmintonHireCollectionNew) {
                BadmintonHireCollectionNewBadmintonHireToAttach = em.getReference(BadmintonHireCollectionNewBadmintonHireToAttach.getClass(), BadmintonHireCollectionNewBadmintonHireToAttach.getId());
                attachedBadmintonHireCollectionNew.add(BadmintonHireCollectionNewBadmintonHireToAttach);
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
            for (BadmintonAccount badmintonaccountCollectionNewBadmintonAccount : badmintonAccountCollectionNew) {
                if (!badmintonAccountCollectionOld.contains(badmintonaccountCollectionNewBadmintonAccount)) {
                    Player oldPlayerIdOfBadmintonAccountCollectionNewBadmintonAccount = badmintonaccountCollectionNewBadmintonAccount.getPlayerId();
                    badmintonaccountCollectionNewBadmintonAccount.setPlayerId(player);
                    badmintonaccountCollectionNewBadmintonAccount = em.merge(badmintonaccountCollectionNewBadmintonAccount);
                    if (oldPlayerIdOfBadmintonAccountCollectionNewBadmintonAccount != null && !oldPlayerIdOfBadmintonAccountCollectionNewBadmintonAccount.equals(player)) {
                        oldPlayerIdOfBadmintonAccountCollectionNewBadmintonAccount.getBadmintonAccountCollection().remove(badmintonaccountCollectionNewBadmintonAccount);
                        oldPlayerIdOfBadmintonAccountCollectionNewBadmintonAccount = em.merge(oldPlayerIdOfBadmintonAccountCollectionNewBadmintonAccount);
                    }
                }
            }
            for (BadmintonHire BadmintonHireCollectionNewBadmintonHire : badmintonHireCollectionNew) {
                if (!badmintonHireCollectionOld.contains(BadmintonHireCollectionNewBadmintonHire)) {
                    Player oldPayerOfBadmintonHireCollectionNewBadmintonHire = BadmintonHireCollectionNewBadmintonHire.getPayer();
                    BadmintonHireCollectionNewBadmintonHire.setPayer(player);
                    BadmintonHireCollectionNewBadmintonHire = em.merge(BadmintonHireCollectionNewBadmintonHire);
                    if (oldPayerOfBadmintonHireCollectionNewBadmintonHire != null && !oldPayerOfBadmintonHireCollectionNewBadmintonHire.equals(player)) {
                        oldPayerOfBadmintonHireCollectionNewBadmintonHire.getBadmintonHireCollection().remove(BadmintonHireCollectionNewBadmintonHire);
                        oldPayerOfBadmintonHireCollectionNewBadmintonHire = em.merge(oldPayerOfBadmintonHireCollectionNewBadmintonHire);
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
            Collection<BadmintonAccount> badmintonAccountCollectionOrphanCheck = player.getBadmintonAccountCollection();
            for (BadmintonAccount badmintonaccountCollectionOrphanCheckBadmintonAccount : badmintonAccountCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Player (" + player + ") cannot be destroyed since the BadmintonAccount " + badmintonaccountCollectionOrphanCheckBadmintonAccount + " in its BadmintonAccountCollection field has a non-nullable playerId field.");
            }
            Collection<BadmintonHire> badmintonHireCollectionOrphanCheck = player.getBadmintonHireCollection();
            for (BadmintonHire BadmintonHireCollectionOrphanCheckBadmintonHire : badmintonHireCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Player (" + player + ") cannot be destroyed since the BadmintonHire " + BadmintonHireCollectionOrphanCheckBadmintonHire + " in its BadmintonHireCollection field has a non-nullable payer field.");
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
