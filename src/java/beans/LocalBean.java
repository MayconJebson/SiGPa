/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package beans;

import dao.LocalJpaController;
import dao.exceptions.NonexistentEntityException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;
import modelos.Departamento;
import modelos.Local;
import modelos.Pessoa;
import util.JPAUtil;

/**
 *
 * @author maycon
 */
@ManagedBean
@RequestScoped
public class LocalBean {
    
    private Local local;
    LocalJpaController daoLocal;
    private List<Local> locais;
    private Pessoa responsavel;
    private Departamento departamento;
    
    public LocalBean(){
        local = new Local();
        daoLocal = new LocalJpaController(JPAUtil.factory);
        locais = new ArrayList<Local>();
        responsavel = new Pessoa();
        departamento = new Departamento();
        pesquisarLocais();
    }
    
    public void inserir(){
        FacesContext context = FacesContext.getCurrentInstance();
        try {
            local.setId(null);
            local.setDepartamento(departamento);
            local.setResponsavel(responsavel);
            daoLocal.create(local);
            local = new Local();
            departamento = new Departamento();
            responsavel = new Pessoa();
            context.addMessage("formLocal", new FacesMessage("Local foi inserido com sucesso!"));
        } catch (Exception ex) {
            context.addMessage("formLocal", new FacesMessage("Local não pode ser inserido"));
            Logger.getLogger(LocalBean.class.getName()).log(Level.SEVERE, null, ex);
        }
        pesquisarLocais();
    }
    
    public void alterar(){
        FacesContext context = FacesContext.getCurrentInstance();
        try {
            local.setDepartamento(departamento);
            local.setResponsavel(responsavel);
            daoLocal.edit(local);
            local = new Local();
            departamento = new Departamento();
            responsavel = new Pessoa();
            context.addMessage("formLocal", new FacesMessage("Local foi alterado com sucesso!"));
        } catch (NonexistentEntityException ex) {
            context.addMessage("formLocal", new FacesMessage("Error"));
            Logger.getLogger(LocalBean.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            context.addMessage("formLocal", new FacesMessage("Local não pode ser alterado"));
            Logger.getLogger(LocalBean.class.getName()).log(Level.SEVERE, null, ex);
        }
        pesquisarLocais();
    }
    
    public void excluir(){
        FacesContext context = FacesContext.getCurrentInstance();
        try {
            daoLocal.destroy(local.getId());
            local = new Local();
            departamento = new Departamento();
            responsavel = new Pessoa();
            context.addMessage("formLocal", new FacesMessage("Local foi excluído com sucesso!"));
        } catch (NonexistentEntityException ex) {
            context.addMessage("formLocal", new FacesMessage("Local não pode ser excluído"));
            Logger.getLogger(DepartamentoBean.class.getName()).log(Level.SEVERE, null, ex);
        }
        pesquisarLocais();
    }
    
    /**
     * @return the local
     */
    public Local getLocal() {
        return local;
    }

    /**
     * @param local the local to set
     */
    public void setLocal(Local local) {
        this.local = local;
        responsavel = local.getResponsavel();
        departamento = local.getDepartamento();
    }

    /**
     * @return the locais
     */
    public List<Local> getLocais() {
        return locais;
    }

    /**
     * @param locais the locais to set
     */
    public void setLocais(List<Local> locais) {
        this.locais = locais;
    }
    
    public void pesquisarLocais(){
        locais = daoLocal.findLocalEntities();
    }

    /**
     * @return the responsavel
     */
    public Pessoa getResponsavel() {
        return responsavel;
    }

    /**
     * @param responsavel the responsavel to set
     */
    public void setResponsavel(Pessoa responsavel) {
        this.responsavel = responsavel;
    }

    /**
     * @return the departamento
     */
    public Departamento getDepartamento() {
        return departamento;
    }

    /**
     * @param departamento the departamento to set
     */
    public void setDepartamento(Departamento departamento) {
        this.departamento = departamento;
    }
    
    public void limpar(){
        local = new Local();
        responsavel = new Pessoa();
        departamento = new Departamento();
    }
    
    public List<Local> mostrarLocais(){
        return daoLocal.findLocalEntities();
    }
}
