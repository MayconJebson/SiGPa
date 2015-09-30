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
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import modelos.Departamento;
import modelos.Local;

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
        if (departamento.getPessoas() == null) {
            departamento.setPessoas(new ArrayList<Pessoa>());
        }
        if (departamento.getListLocais() == null) {
            departamento.setListLocais(new ArrayList<Local>());
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
            List<Pessoa> attachedPessoas = new ArrayList<Pessoa>();
            for (Pessoa pessoasPessoaToAttach : departamento.getPessoas()) {
                pessoasPessoaToAttach = em.getReference(pessoasPessoaToAttach.getClass(), pessoasPessoaToAttach.getId());
                attachedPessoas.add(pessoasPessoaToAttach);
            }
            departamento.setPessoas(attachedPessoas);
            List<Local> attachedListLocais = new ArrayList<Local>();
            for (Local listLocaisLocalToAttach : departamento.getListLocais()) {
                listLocaisLocalToAttach = em.getReference(listLocaisLocalToAttach.getClass(), listLocaisLocalToAttach.getId());
                attachedListLocais.add(listLocaisLocalToAttach);
            }
            departamento.setListLocais(attachedListLocais);
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
            for (Pessoa pessoasPessoa : departamento.getPessoas()) {
                Departamento oldDepartamentoOfPessoasPessoa = pessoasPessoa.getDepartamento();
                pessoasPessoa.setDepartamento(departamento);
                pessoasPessoa = em.merge(pessoasPessoa);
                if (oldDepartamentoOfPessoasPessoa != null) {
                    oldDepartamentoOfPessoasPessoa.getPessoas().remove(pessoasPessoa);
                    oldDepartamentoOfPessoasPessoa = em.merge(oldDepartamentoOfPessoasPessoa);
                }
            }
            for (Local listLocaisLocal : departamento.getListLocais()) {
                Departamento oldDepartamentoOfListLocaisLocal = listLocaisLocal.getDepartamento();
                listLocaisLocal.setDepartamento(departamento);
                listLocaisLocal = em.merge(listLocaisLocal);
                if (oldDepartamentoOfListLocaisLocal != null) {
                    oldDepartamentoOfListLocaisLocal.getListLocais().remove(listLocaisLocal);
                    oldDepartamentoOfListLocaisLocal = em.merge(oldDepartamentoOfListLocaisLocal);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Departamento departamento) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Departamento persistentDepartamento = em.find(Departamento.class, departamento.getId());
            Pessoa chefeOld = persistentDepartamento.getChefe();
            Pessoa chefeNew = departamento.getChefe();
            List<Pessoa> pessoasOld = persistentDepartamento.getPessoas();
            List<Pessoa> pessoasNew = departamento.getPessoas();
            List<Local> listLocaisOld = persistentDepartamento.getListLocais();
            List<Local> listLocaisNew = departamento.getListLocais();
            if (chefeNew != null) {
                chefeNew = em.getReference(chefeNew.getClass(), chefeNew.getId());
                departamento.setChefe(chefeNew);
            }
            List<Pessoa> attachedPessoasNew = new ArrayList<Pessoa>();
            for (Pessoa pessoasNewPessoaToAttach : pessoasNew) {
                pessoasNewPessoaToAttach = em.getReference(pessoasNewPessoaToAttach.getClass(), pessoasNewPessoaToAttach.getId());
                attachedPessoasNew.add(pessoasNewPessoaToAttach);
            }
            pessoasNew = attachedPessoasNew;
            departamento.setPessoas(pessoasNew);
            List<Local> attachedListLocaisNew = new ArrayList<Local>();
            for (Local listLocaisNewLocalToAttach : listLocaisNew) {
                listLocaisNewLocalToAttach = em.getReference(listLocaisNewLocalToAttach.getClass(), listLocaisNewLocalToAttach.getId());
                attachedListLocaisNew.add(listLocaisNewLocalToAttach);
            }
            listLocaisNew = attachedListLocaisNew;
            departamento.setListLocais(listLocaisNew);
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
            for (Pessoa pessoasOldPessoa : pessoasOld) {
                if (!pessoasNew.contains(pessoasOldPessoa)) {
                    pessoasOldPessoa.setDepartamento(null);
                    pessoasOldPessoa = em.merge(pessoasOldPessoa);
                }
            }
            for (Pessoa pessoasNewPessoa : pessoasNew) {
                if (!pessoasOld.contains(pessoasNewPessoa)) {
                    Departamento oldDepartamentoOfPessoasNewPessoa = pessoasNewPessoa.getDepartamento();
                    pessoasNewPessoa.setDepartamento(departamento);
                    pessoasNewPessoa = em.merge(pessoasNewPessoa);
                    if (oldDepartamentoOfPessoasNewPessoa != null && !oldDepartamentoOfPessoasNewPessoa.equals(departamento)) {
                        oldDepartamentoOfPessoasNewPessoa.getPessoas().remove(pessoasNewPessoa);
                        oldDepartamentoOfPessoasNewPessoa = em.merge(oldDepartamentoOfPessoasNewPessoa);
                    }
                }
            }
            for (Local listLocaisOldLocal : listLocaisOld) {
                if (!listLocaisNew.contains(listLocaisOldLocal)) {
                    listLocaisOldLocal.setDepartamento(null);
                    listLocaisOldLocal = em.merge(listLocaisOldLocal);
                }
            }
            for (Local listLocaisNewLocal : listLocaisNew) {
                if (!listLocaisOld.contains(listLocaisNewLocal)) {
                    Departamento oldDepartamentoOfListLocaisNewLocal = listLocaisNewLocal.getDepartamento();
                    listLocaisNewLocal.setDepartamento(departamento);
                    listLocaisNewLocal = em.merge(listLocaisNewLocal);
                    if (oldDepartamentoOfListLocaisNewLocal != null && !oldDepartamentoOfListLocaisNewLocal.equals(departamento)) {
                        oldDepartamentoOfListLocaisNewLocal.getListLocais().remove(listLocaisNewLocal);
                        oldDepartamentoOfListLocaisNewLocal = em.merge(oldDepartamentoOfListLocaisNewLocal);
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
            List<Pessoa> pessoas = departamento.getPessoas();
            for (Pessoa pessoasPessoa : pessoas) {
                pessoasPessoa.setDepartamento(null);
                pessoasPessoa = em.merge(pessoasPessoa);
            }
            List<Local> listLocais = departamento.getListLocais();
            for (Local listLocaisLocal : listLocais) {
                listLocaisLocal.setDepartamento(null);
                listLocaisLocal = em.merge(listLocaisLocal);
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
