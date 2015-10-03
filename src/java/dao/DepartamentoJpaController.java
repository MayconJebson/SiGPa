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
import modelos.Pessoa;
import modelos.Local;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import modelos.Departamento;

/**
 *
 * @author maycon
 */
public class DepartamentoJpaController implements Serializable {

    public DepartamentoJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Departamento departamento) {
        if (departamento.getLocais() == null) {
            departamento.setLocais(new ArrayList<Local>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Pessoa chefe = departamento.getChefe();
            if (chefe != null) {
                chefe = em.getReference(chefe.getClass(), chefe.getId());
                departamento.setChefe(chefe);
            }
            List<Local> attachedLocais = new ArrayList<Local>();
            for (Local locaisLocalToAttach : departamento.getLocais()) {
                locaisLocalToAttach = em.getReference(locaisLocalToAttach.getClass(), locaisLocalToAttach.getId());
                attachedLocais.add(locaisLocalToAttach);
            }
            departamento.setLocais(attachedLocais);
            em.persist(departamento);
            if (chefe != null) {
                Departamento oldDepartamentoOfChefe = chefe.getDepartamento();
                if (oldDepartamentoOfChefe != null) {
                    oldDepartamentoOfChefe.setChefe(null);
                    oldDepartamentoOfChefe = em.merge(oldDepartamentoOfChefe);
                }
                chefe.setDepartamento(departamento);
                chefe = em.merge(chefe);
            }
            for (Local locaisLocal : departamento.getLocais()) {
                Departamento oldDepartamentoOfLocaisLocal = locaisLocal.getDepartamento();
                locaisLocal.setDepartamento(departamento);
                locaisLocal = em.merge(locaisLocal);
                if (oldDepartamentoOfLocaisLocal != null) {
                    oldDepartamentoOfLocaisLocal.getLocais().remove(locaisLocal);
                    oldDepartamentoOfLocaisLocal = em.merge(oldDepartamentoOfLocaisLocal);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    /*public void edit(Departamento departamento) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Departamento persistentDepartamento = em.find(Departamento.class, departamento.getId());
            Pessoa chefeOld = persistentDepartamento.getChefe();
            Pessoa chefeNew = departamento.getChefe();
            List<Local> locaisOld = persistentDepartamento.getLocais();
            List<Local> locaisNew = departamento.getLocais();
            if (chefeNew != null) {
                chefeNew = em.getReference(chefeNew.getClass(), chefeNew.getId());
                departamento.setChefe(chefeNew);
            }
            List<Local> attachedLocaisNew = new ArrayList<Local>();
            for (Local locaisNewLocalToAttach : locaisNew) {
                locaisNewLocalToAttach = em.getReference(locaisNewLocalToAttach.getClass(), locaisNewLocalToAttach.getId());
                attachedLocaisNew.add(locaisNewLocalToAttach);
            }
            locaisNew = attachedLocaisNew;
            departamento.setLocais(locaisNew);
            departamento = em.merge(departamento);
            if (chefeOld != null && !chefeOld.equals(chefeNew)) {
                chefeOld.setDepartamento(null);
                chefeOld = em.merge(chefeOld);
            }
            if (chefeNew != null && !chefeNew.equals(chefeOld)) {
                Departamento oldDepartamentoOfChefe = chefeNew.getDepartamento();
                if (oldDepartamentoOfChefe != null) {
                    oldDepartamentoOfChefe.setChefe(null);
                    oldDepartamentoOfChefe = em.merge(oldDepartamentoOfChefe);
                }
                chefeNew.setDepartamento(departamento);
                chefeNew = em.merge(chefeNew);
            }
            for (Local locaisOldLocal : locaisOld) {
                if (!locaisNew.contains(locaisOldLocal)) {
                    locaisOldLocal.setDepartamento(null);
                    locaisOldLocal = em.merge(locaisOldLocal);
                }
            }
            for (Local locaisNewLocal : locaisNew) {
                if (!locaisOld.contains(locaisNewLocal)) {
                    Departamento oldDepartamentoOfLocaisNewLocal = locaisNewLocal.getDepartamento();
                    locaisNewLocal.setDepartamento(departamento);
                    locaisNewLocal = em.merge(locaisNewLocal);
                    if (oldDepartamentoOfLocaisNewLocal != null && !oldDepartamentoOfLocaisNewLocal.equals(departamento)) {
                        oldDepartamentoOfLocaisNewLocal.getLocais().remove(locaisNewLocal);
                        oldDepartamentoOfLocaisNewLocal = em.merge(oldDepartamentoOfLocaisNewLocal);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Long id = departamento.getId();
                if (findDepartamento(id) == null) {
                    throw new NonexistentEntityException("The departamento with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }*/
    
    public void edit(Departamento departamento) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            departamento = em.merge(departamento);
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Long id = departamento.getId();
                if (findDepartamento(id) == null) {
                    throw new NonexistentEntityException("The departamento with id " + id + " no longer exists.");
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
            Departamento departamento;
            try {
                departamento = em.getReference(Departamento.class, id);
                departamento.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The departamento with id " + id + " no longer exists.", enfe);
            }
            Pessoa chefe = departamento.getChefe();
            if (chefe != null) {
                chefe.setDepartamento(null);
                chefe = em.merge(chefe);
            }
            List<Local> locais = departamento.getLocais();
            for (Local locaisLocal : locais) {
                locaisLocal.setDepartamento(null);
                locaisLocal = em.merge(locaisLocal);
            }
            em.remove(departamento);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Departamento> findDepartamentoEntities() {
        return findDepartamentoEntities(true, -1, -1);
    }

    public List<Departamento> findDepartamentoEntities(int maxResults, int firstResult) {
        return findDepartamentoEntities(false, maxResults, firstResult);
    }

    private List<Departamento> findDepartamentoEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Departamento.class));
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

    public Departamento findDepartamento(Long id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Departamento.class, id);
        } finally {
            em.close();
        }
    }

    public int getDepartamentoCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Departamento> rt = cq.from(Departamento.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
