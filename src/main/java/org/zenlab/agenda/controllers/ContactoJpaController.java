/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.zenlab.agenda.controllers;

import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import org.zenlab.agenda.entities.Direccion;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import org.zenlab.agenda.controllers.exceptions.IllegalOrphanException;
import org.zenlab.agenda.controllers.exceptions.NonexistentEntityException;
import org.zenlab.agenda.entities.Contacto;
import org.zenlab.agenda.entities.Redes;

/**
 *
 * @author zenlaboratory
 */
public class ContactoJpaController implements Serializable {

    public ContactoJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Contacto contacto) {
        if (contacto.getDireccionCollection() == null) {
            contacto.setDireccionCollection(new ArrayList<Direccion>());
        }
        if (contacto.getRedesCollection() == null) {
            contacto.setRedesCollection(new ArrayList<Redes>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Collection<Direccion> attachedDireccionCollection = new ArrayList<Direccion>();
            for (Direccion direccionCollectionDireccionToAttach : contacto.getDireccionCollection()) {
                direccionCollectionDireccionToAttach = em.getReference(direccionCollectionDireccionToAttach.getClass(), direccionCollectionDireccionToAttach.getId());
                attachedDireccionCollection.add(direccionCollectionDireccionToAttach);
            }
            contacto.setDireccionCollection(attachedDireccionCollection);
            Collection<Redes> attachedRedesCollection = new ArrayList<Redes>();
            for (Redes redesCollectionRedesToAttach : contacto.getRedesCollection()) {
                redesCollectionRedesToAttach = em.getReference(redesCollectionRedesToAttach.getClass(), redesCollectionRedesToAttach.getId());
                attachedRedesCollection.add(redesCollectionRedesToAttach);
            }
            contacto.setRedesCollection(attachedRedesCollection);
            em.persist(contacto);
            for (Direccion direccionCollectionDireccion : contacto.getDireccionCollection()) {
                Contacto oldContactoIdOfDireccionCollectionDireccion = direccionCollectionDireccion.getContactoId();
                direccionCollectionDireccion.setContactoId(contacto);
                direccionCollectionDireccion = em.merge(direccionCollectionDireccion);
                if (oldContactoIdOfDireccionCollectionDireccion != null) {
                    oldContactoIdOfDireccionCollectionDireccion.getDireccionCollection().remove(direccionCollectionDireccion);
                    oldContactoIdOfDireccionCollectionDireccion = em.merge(oldContactoIdOfDireccionCollectionDireccion);
                }
            }
            for (Redes redesCollectionRedes : contacto.getRedesCollection()) {
                Contacto oldContactoIdOfRedesCollectionRedes = redesCollectionRedes.getContactoId();
                redesCollectionRedes.setContactoId(contacto);
                redesCollectionRedes = em.merge(redesCollectionRedes);
                if (oldContactoIdOfRedesCollectionRedes != null) {
                    oldContactoIdOfRedesCollectionRedes.getRedesCollection().remove(redesCollectionRedes);
                    oldContactoIdOfRedesCollectionRedes = em.merge(oldContactoIdOfRedesCollectionRedes);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Contacto contacto) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Contacto persistentContacto = em.find(Contacto.class, contacto.getId());
            Collection<Direccion> direccionCollectionOld = persistentContacto.getDireccionCollection();
            Collection<Direccion> direccionCollectionNew = contacto.getDireccionCollection();
            Collection<Redes> redesCollectionOld = persistentContacto.getRedesCollection();
            Collection<Redes> redesCollectionNew = contacto.getRedesCollection();
            List<String> illegalOrphanMessages = null;
            for (Direccion direccionCollectionOldDireccion : direccionCollectionOld) {
                if (!direccionCollectionNew.contains(direccionCollectionOldDireccion)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Direccion " + direccionCollectionOldDireccion + " since its contactoId field is not nullable.");
                }
            }
            for (Redes redesCollectionOldRedes : redesCollectionOld) {
                if (!redesCollectionNew.contains(redesCollectionOldRedes)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Redes " + redesCollectionOldRedes + " since its contactoId field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Collection<Direccion> attachedDireccionCollectionNew = new ArrayList<Direccion>();
            for (Direccion direccionCollectionNewDireccionToAttach : direccionCollectionNew) {
                direccionCollectionNewDireccionToAttach = em.getReference(direccionCollectionNewDireccionToAttach.getClass(), direccionCollectionNewDireccionToAttach.getId());
                attachedDireccionCollectionNew.add(direccionCollectionNewDireccionToAttach);
            }
            direccionCollectionNew = attachedDireccionCollectionNew;
            contacto.setDireccionCollection(direccionCollectionNew);
            Collection<Redes> attachedRedesCollectionNew = new ArrayList<Redes>();
            for (Redes redesCollectionNewRedesToAttach : redesCollectionNew) {
                redesCollectionNewRedesToAttach = em.getReference(redesCollectionNewRedesToAttach.getClass(), redesCollectionNewRedesToAttach.getId());
                attachedRedesCollectionNew.add(redesCollectionNewRedesToAttach);
            }
            redesCollectionNew = attachedRedesCollectionNew;
            contacto.setRedesCollection(redesCollectionNew);
            contacto = em.merge(contacto);
            for (Direccion direccionCollectionNewDireccion : direccionCollectionNew) {
                if (!direccionCollectionOld.contains(direccionCollectionNewDireccion)) {
                    Contacto oldContactoIdOfDireccionCollectionNewDireccion = direccionCollectionNewDireccion.getContactoId();
                    direccionCollectionNewDireccion.setContactoId(contacto);
                    direccionCollectionNewDireccion = em.merge(direccionCollectionNewDireccion);
                    if (oldContactoIdOfDireccionCollectionNewDireccion != null && !oldContactoIdOfDireccionCollectionNewDireccion.equals(contacto)) {
                        oldContactoIdOfDireccionCollectionNewDireccion.getDireccionCollection().remove(direccionCollectionNewDireccion);
                        oldContactoIdOfDireccionCollectionNewDireccion = em.merge(oldContactoIdOfDireccionCollectionNewDireccion);
                    }
                }
            }
            for (Redes redesCollectionNewRedes : redesCollectionNew) {
                if (!redesCollectionOld.contains(redesCollectionNewRedes)) {
                    Contacto oldContactoIdOfRedesCollectionNewRedes = redesCollectionNewRedes.getContactoId();
                    redesCollectionNewRedes.setContactoId(contacto);
                    redesCollectionNewRedes = em.merge(redesCollectionNewRedes);
                    if (oldContactoIdOfRedesCollectionNewRedes != null && !oldContactoIdOfRedesCollectionNewRedes.equals(contacto)) {
                        oldContactoIdOfRedesCollectionNewRedes.getRedesCollection().remove(redesCollectionNewRedes);
                        oldContactoIdOfRedesCollectionNewRedes = em.merge(oldContactoIdOfRedesCollectionNewRedes);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = contacto.getId();
                if (findContacto(id) == null) {
                    throw new NonexistentEntityException("The contacto with id " + id + " no longer exists.");
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
            Contacto contacto;
            try {
                contacto = em.getReference(Contacto.class, id);
                contacto.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The contacto with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            Collection<Direccion> direccionCollectionOrphanCheck = contacto.getDireccionCollection();
            for (Direccion direccionCollectionOrphanCheckDireccion : direccionCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Contacto (" + contacto + ") cannot be destroyed since the Direccion " + direccionCollectionOrphanCheckDireccion + " in its direccionCollection field has a non-nullable contactoId field.");
            }
            Collection<Redes> redesCollectionOrphanCheck = contacto.getRedesCollection();
            for (Redes redesCollectionOrphanCheckRedes : redesCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Contacto (" + contacto + ") cannot be destroyed since the Redes " + redesCollectionOrphanCheckRedes + " in its redesCollection field has a non-nullable contactoId field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(contacto);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Contacto> findContactoEntities() {
        return findContactoEntities(true, -1, -1);
    }

    public List<Contacto> findContactoEntities(int maxResults, int firstResult) {
        return findContactoEntities(false, maxResults, firstResult);
    }

    private List<Contacto> findContactoEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Contacto.class));
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

    public Contacto findContacto(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Contacto.class, id);
        } finally {
            em.close();
        }
    }

    public int getContactoCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Contacto> rt = cq.from(Contacto.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
