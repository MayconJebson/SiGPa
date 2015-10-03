/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package beans;

import dao.PatrimonioJpaController;
import dao.exceptions.NonexistentEntityException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;
import modelos.Local;
import modelos.Patrimonio;
import util.JPAUtil;

/**
 *
 * @author maycon
 */
@ManagedBean
@RequestScoped
public class PatrimonioBean {
    
    private Patrimonio patrimonio;
    PatrimonioJpaController daoPatrimonio;
    private List<Patrimonio> patrimonios;
    private Local local;
    
    public PatrimonioBean(){
        patrimonio = new Patrimonio();
        daoPatrimonio = new PatrimonioJpaController(JPAUtil.factory);
        patrimonios = new ArrayList<Patrimonio>();
        local = new Local();
        pesquisarPatrimonios();
    }

    /**
     * @return the patrimonio
     */
    public Patrimonio getPatrimonio() {
        return patrimonio;
    }

    /**
     * @param patrimonio the patrimonio to set
     */
    public void setPatrimonio(Patrimonio patrimonio) {
        this.patrimonio = patrimonio;
        local = patrimonio.getLocal();
    }
    
    public void inserir(){
        FacesContext context = FacesContext.getCurrentInstance();
        try {
            patrimonio.setId(null);
            patrimonio.setLocal(local);
            daoPatrimonio.create(patrimonio);
            patrimonio = new Patrimonio();
            local = new Local();
            context.addMessage("formPatrimonio", new FacesMessage("Patrimonio foi inserido com sucesso!"));
        } catch (Exception ex) {
            context.addMessage("formPatrimonio", new FacesMessage("Patrimonio não pode ser inserido"));
            Logger.getLogger(PatrimonioBean.class.getName()).log(Level.SEVERE, null, ex);
        }
        pesquisarPatrimonios();
    }
    
    public void alterar(){
        FacesContext context = FacesContext.getCurrentInstance();
        try {
            patrimonio.setLocal(local);
            daoPatrimonio.edit(patrimonio);
            patrimonio = new Patrimonio();
            local = new Local();
            context.addMessage("formPatrimonio", new FacesMessage("Patrimônio foi alterado com sucesso!"));
        } catch (NonexistentEntityException ex) {
            context.addMessage("formPatrimonio", new FacesMessage("Error"));
            Logger.getLogger(PessoaBean.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            context.addMessage("formPatrimonio", new FacesMessage("Patrimonio não pode ser alterado"));
            Logger.getLogger(PessoaBean.class.getName()).log(Level.SEVERE, null, ex);
        }
        pesquisarPatrimonios();
    }
    
    public void excluir(){
        FacesContext context = FacesContext.getCurrentInstance();
        try {
            daoPatrimonio.destroy(patrimonio.getId());
            patrimonio = new Patrimonio();
            local = new Local();
            context.addMessage("formPatrimonio", new FacesMessage("Patrimônio foi excluído com sucesso!"));
        } catch (NonexistentEntityException ex) {
            context.addMessage("formPatrimonio", new FacesMessage("Patrimônio não pode ser excluído"));
            Logger.getLogger(PessoaBean.class.getName()).log(Level.SEVERE, null, ex);
        }
        pesquisarPatrimonios();
    }

    /**
     * @return the patrimonios
     */
    public List<Patrimonio> getPatrimonios() {
        return patrimonios;
    }

    /**
     * @param patrimonios the patrimonios to set
     */
    public void setPatrimonios(List<Patrimonio> patrimonios) {
        this.patrimonios = patrimonios;
    }
    
    public void pesquisarPatrimonios(){
        patrimonios = daoPatrimonio.findPatrimonioEntities();
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
    }
    
    public void limpar(){
        local = new Local();
        patrimonio = new Patrimonio();
    }
}
