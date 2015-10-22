/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import dao.exceptions.NonexistentEntityException;
import java.io.Serializable;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import modelos.Local;
import modelos.Patrimonio;

/**
 *
 * @author maycon
 */
public class PatrimonioJpaController implements Serializable {

    public PatrimonioJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Patrimonio patrimonio) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Local local = patrimonio.getLocal();
            if (local != null) {
                local = em.getReference(local.getClass(), local.getId());
                patrimonio.setLocal(local);
            }
            em.persist(patrimonio);
            if (local != null) {
                local.getListPatrimonios().add(patrimonio);
                local = em.merge(local);
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Patrimonio patrimonio) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Patrimonio persistentPatrimonio = em.find(Patrimonio.class, patrimonio.getId());
            Local localOld = persistentPatrimonio.getLocal();
            Local localNew = patrimonio.getLocal();
            if (localNew != null) {
                localNew = em.getReference(localNew.getClass(), localNew.getId());
                patrimonio.setLocal(localNew);
            }
            patrimonio = em.merge(patrimonio);
            if (localOld != null && !localOld.equals(localNew)) {
                localOld.getListPatrimonios().remove(patrimonio);
                localOld = em.merge(localOld);
            }
            if (localNew != null && !localNew.equals(localOld)) {
                localNew.getListPatrimonios().add(patrimonio);
                localNew = em.merge(localNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Long id = patrimonio.getId();
                if (findPatrimonio(id) == null) {
                    throw new NonexistentEntityException("The patrimonio with id " + id + " no longer exists.");
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
            Patrimonio patrimonio;
            try {
                patrimonio = em.getReference(Patrimonio.class, id);
                patrimonio.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The patrimonio with id " + id + " no longer exists.", enfe);
            }
            Local local = patrimonio.getLocal();
            if (local != null) {
                local.getListPatrimonios().remove(patrimonio);
                local = em.merge(local);
            }
            em.remove(patrimonio);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Patrimonio> findPatrimonioEntities() {
        return findPatrimonioEntities(true, -1, -1);
    }

    public List<Patrimonio> findPatrimonioEntities(int maxResults, int firstResult) {
        return findPatrimonioEntities(false, maxResults, firstResult);
    }

    private List<Patrimonio> findPatrimonioEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Patrimonio.class));
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

    public Patrimonio findPatrimonio(Long id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Patrimonio.class, id);
        } finally {
            em.close();
        }
    }

    public int getPatrimonioCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Patrimonio> rt = cq.from(Patrimonio.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
    public List<Patrimonio> pesquisarDepartamento(Long id_dep){
        EntityManager em = getEntityManager();
        TypedQuery<Patrimonio> query;
        query = em.createQuery("select * from Patrimonio p where p.local_id=:id_dep)",
                               Patrimonio.class);
        query.setParameter("id_dep", id_dep);
        try{
            return query.getResultList();
        }catch(NoResultException e){
            return null;
        }
    }
}