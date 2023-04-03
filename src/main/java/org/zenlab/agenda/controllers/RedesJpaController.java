/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.zenlab.agenda.controllers;

import java.io.Serializable;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import org.zenlab.agenda.controllers.exceptions.NonexistentEntityException;
import org.zenlab.agenda.entities.Contacto;
import org.zenlab.agenda.entities.Redes;

/**
 *
 * @author zenlaboratory
 */
public class RedesJpaController implements Serializable {

    public RedesJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Redes redes) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Contacto contactoId = redes.getContactoId();
            if (contactoId != null) {
                contactoId = em.getReference(contactoId.getClass(), contactoId.getId());
                redes.setContactoId(contactoId);
            }
            em.persist(redes);
            if (contactoId != null) {
                contactoId.getRedesCollection().add(redes);
                contactoId = em.merge(contactoId);
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Redes redes) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Redes persistentRedes = em.find(Redes.class, redes.getId());
            Contacto contactoIdOld = persistentRedes.getContactoId();
            Contacto contactoIdNew = redes.getContactoId();
            if (contactoIdNew != null) {
                contactoIdNew = em.getReference(contactoIdNew.getClass(), contactoIdNew.getId());
                redes.setContactoId(contactoIdNew);
            }
            redes = em.merge(redes);
            if (contactoIdOld != null && !contactoIdOld.equals(contactoIdNew)) {
                contactoIdOld.getRedesCollection().remove(redes);
                contactoIdOld = em.merge(contactoIdOld);
            }
            if (contactoIdNew != null && !contactoIdNew.equals(contactoIdOld)) {
                contactoIdNew.getRedesCollection().add(redes);
                contactoIdNew = em.merge(contactoIdNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = redes.getId();
                if (findRedes(id) == null) {
                    throw new NonexistentEntityException("The redes with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Integer id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Redes redes;
            try {
                redes = em.getReference(Redes.class, id);
                redes.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The redes with id " + id + " no longer exists.", enfe);
            }
            Contacto contactoId = redes.getContactoId();
            if (contactoId != null) {
                contactoId.getRedesCollection().remove(redes);
                contactoId = em.merge(contactoId);
            }
            em.remove(redes);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Redes> findRedesEntities() {
        return findRedesEntities(true, -1, -1);
    }

    public List<Redes> findRedesEntities(int maxResults, int firstResult) {
        return findRedesEntities(false, maxResults, firstResult);
    }

    private List<Redes> findRedesEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Redes.class));
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

    public Redes findRedes(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Redes.class, id);
        } finally {
            em.close();
        }
    }

    public int getRedesCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Redes> rt = cq.from(Redes.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
