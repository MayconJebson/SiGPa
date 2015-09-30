/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import dao.exceptions.NonexistentEntityException;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import modelos.Departamento;
import modelos.Patrimonio;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import modelos.Local;

/**
 *
 * @author maycon
 */
public class LocalJpaController implements Serializable {

    public LocalJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Local local) {
        if (local.getListPatrimonios() == null) {
            local.setListPatrimonios(new ArrayList<Patrimonio>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Departamento departamento = local.getDepartamento();
            if (departamento != null) {
                departamento = em.getReference(departamento.getClass(), departamento.getId());
                local.setDepartamento(departamento);
            }
            List<Patrimonio> attachedListPatrimonios = new ArrayList<Patrimonio>();
            for (Patrimonio listPatrimoniosPatrimonioToAttach : local.getListPatrimonios()) {
                listPatrimoniosPatrimonioToAttach = em.getReference(listPatrimoniosPatrimonioToAttach.getClass(), listPatrimoniosPatrimonioToAttach.getId());
                attachedListPatrimonios.add(listPatrimoniosPatrimonioToAttach);
            }
            local.setListPatrimonios(attachedListPatrimonios);
            em.persist(local);
            if (departamento != null) {
                departamento.getListLocais().add(local);
                departamento = em.merge(departamento);
            }
            for (Patrimonio listPatrimoniosPatrimonio : local.getListPatrimonios()) {
                Local oldLocalOfListPatrimoniosPatrimonio = listPatrimoniosPatrimonio.getLocal();
                listPatrimoniosPatrimonio.setLocal(local);
                listPatrimoniosPatrimonio = em.merge(listPatrimoniosPatrimonio);
                if (oldLocalOfListPatrimoniosPatrimonio != null) {
                    oldLocalOfListPatrimoniosPatrimonio.getListPatrimonios().remove(listPatrimoniosPatrimonio);
                    oldLocalOfListPatrimoniosPatrimonio = em.merge(oldLocalOfListPatrimoniosPatrimonio);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Local local) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Local persistentLocal = em.find(Local.class, local.getId());
            Departamento departamentoOld = persistentLocal.getDepartamento();
            Departamento departamentoNew = local.getDepartamento();
            List<Patrimonio> listPatrimoniosOld = persistentLocal.getListPatrimonios();
            List<Patrimonio> listPatrimoniosNew = local.getListPatrimonios();
            if (departamentoNew != null) {
                departamentoNew = em.getReference(departamentoNew.getClass(), departamentoNew.getId());
                local.setDepartamento(departamentoNew);
            }
            List<Patrimonio> attachedListPatrimoniosNew = new ArrayList<Patrimonio>();
            for (Patrimonio listPatrimoniosNewPatrimonioToAttach : listPatrimoniosNew) {
                listPatrimoniosNewPatrimonioToAttach = em.getReference(listPatrimoniosNewPatrimonioToAttach.getClass(), listPatrimoniosNewPatrimonioToAttach.getId());
                attachedListPatrimoniosNew.add(listPatrimoniosNewPatrimonioToAttach);
            }
            listPatrimoniosNew = attachedListPatrimoniosNew;
            local.setListPatrimonios(listPatrimoniosNew);
            local = em.merge(local);
            if (departamentoOld != null && !departamentoOld.equals(departamentoNew)) {
                departamentoOld.getListLocais().remove(local);
                departamentoOld = em.merge(departamentoOld);
            }
            if (departamentoNew != null && !departamentoNew.equals(departamentoOld)) {
                departamentoNew.getListLocais().add(local);
                departamentoNew = em.merge(departamentoNew);
            }
            for (Patrimonio listPatrimoniosOldPatrimonio : listPatrimoniosOld) {
                if (!listPatrimoniosNew.contains(listPatrimoniosOldPatrimonio)) {
                    listPatrimoniosOldPatrimonio.setLocal(null);
                    listPatrimoniosOldPatrimonio = em.merge(listPatrimoniosOldPatrimonio);
                }
            }
            for (Patrimonio listPatrimoniosNewPatrimonio : listPatrimoniosNew) {
                if (!listPatrimoniosOld.contains(listPatrimoniosNewPatrimonio)) {
                    Local oldLocalOfListPatrimoniosNewPatrimonio = listPatrimoniosNewPatrimonio.getLocal();
                    listPatrimoniosNewPatrimonio.setLocal(local);
                    listPatrimoniosNewPatrimonio = em.merge(listPatrimoniosNewPatrimonio);
                    if (oldLocalOfListPatrimoniosNewPatrimonio != null && !oldLocalOfListPatrimoniosNewPatrimonio.equals(local)) {
                        oldLocalOfListPatrimoniosNewPatrimonio.getListPatrimonios().remove(listPatrimoniosNewPatrimonio);
                        oldLocalOfListPatrimoniosNewPatrimonio = em.merge(oldLocalOfListPatrimoniosNewPatrimonio);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Long id = local.getId();
                if (findLocal(id) == null) {
                    throw new NonexistentEntityException("The local with id " + id + " no longer exists.");
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
            Local local;
            try {
                local = em.getReference(Local.class, id);
                local.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The local with id " + id + " no longer exists.", enfe);
            }
            Departamento departamento = local.getDepartamento();
            if (departamento != null) {
                departamento.getListLocais().remove(local);
                departamento = em.merge(departamento);
            }
            List<Patrimonio> listPatrimonios = local.getListPatrimonios();
            for (Patrimonio listPatrimoniosPatrimonio : listPatrimonios) {
                listPatrimoniosPatrimonio.setLocal(null);
                listPatrimoniosPatrimonio = em.merge(listPatrimoniosPatrimonio);
            }
            em.remove(local);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Local> findLocalEntities() {
        return findLocalEntities(true, -1, -1);
    }

    public List<Local> findLocalEntities(int maxResults, int firstResult) {
        return findLocalEntities(false, maxResults, firstResult);
    }

    private List<Local> findLocalEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Local.class));
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

    public Local findLocal(Long id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Local.class, id);
        } finally {
            em.close();
        }
    }

    public int getLocalCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Local> rt = cq.from(Local.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
