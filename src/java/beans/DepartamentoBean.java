/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package beans;

import dao.DepartamentoJpaController;
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
import modelos.Pessoa;
import util.JPAUtil;

/**
 *
 * @author maycon
 */
@ManagedBean
@RequestScoped
public class DepartamentoBean {
    
    private Departamento departamento;
    private Pessoa chefe;
    DepartamentoJpaController daoDepartamento;
    private List<Departamento> departamentos;
    
    public DepartamentoBean(){
        departamento = new Departamento();
        chefe = new Pessoa();
        daoDepartamento = new DepartamentoJpaController(JPAUtil.factory);
        departamentos = new ArrayList<Departamento>();
        pesquisarDepartamentos();
    }
    
    public void inserir(){
        FacesContext context = FacesContext.getCurrentInstance();
        try {
            departamento.setId(null);
            departamento.setChefe(chefe);
            daoDepartamento.create(departamento);
            chefe = new Pessoa();
            departamento = new Departamento();
            context.addMessage("formDepartamento", new FacesMessage("Departamento foi inserido com sucesso!"));
        } catch (Exception ex) {
            context.addMessage("formDepartamento", new FacesMessage("Departamento não pode ser inserido"));
            Logger.getLogger(DepartamentoBean.class.getName()).log(Level.SEVERE, null, ex);
        }
        pesquisarDepartamentos();
    }
    
    public void alterar(){
        FacesContext context = FacesContext.getCurrentInstance();
        try {
            departamento.setChefe(chefe);
            daoDepartamento.edit(departamento);
            chefe = new Pessoa();
            departamento = new Departamento();
            context.addMessage("formDepartamento", new FacesMessage("Departamento foi alterado com sucesso!"));
        } catch (NonexistentEntityException ex) {
            context.addMessage("formDepartamento", new FacesMessage("Error"));
            Logger.getLogger(DepartamentoBean.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            context.addMessage("formDepartamento", new FacesMessage("Departamento não pode ser alterado"));
            Logger.getLogger(DepartamentoBean.class.getName()).log(Level.SEVERE, null, ex);
        }
        pesquisarDepartamentos();
    }
    
    public void excluir(){
        FacesContext context = FacesContext.getCurrentInstance();
        try {
            daoDepartamento.destroy(departamento.getId());
            chefe = new Pessoa();
            departamento = new Departamento();
            context.addMessage("formDepartamento", new FacesMessage("Departamento foi excluído com sucesso!"));
        } catch (NonexistentEntityException ex) {
            context.addMessage("formDepartamento", new FacesMessage("Departamento não pode ser excluído"));
            Logger.getLogger(DepartamentoBean.class.getName()).log(Level.SEVERE, null, ex);
        }
        pesquisarDepartamentos();
    }
    
    public void pesquisarDepartamentos(){
        setDepartamentos(daoDepartamento.findDepartamentoEntities());
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
        chefe = departamento.getChefe();
    }

    /**
     * @return the departamentos
     */
    public List<Departamento> getDepartamentos() {
        return departamentos;
    }

    /**
     * @param departamentos the departamentos to set
     */
    public void setDepartamentos(List<Departamento> departamentos) {
        this.departamentos = departamentos;
    }

    /**
     * @return the chefe
     */
    public Pessoa getChefe() {
        return chefe;
    }

    /**
     * @param chefe the chefe to set
     */
    public void setChefe(Pessoa chefe) {
        this.chefe = chefe;
    }
    
    public void limpar(){
        chefe = new Pessoa();
        departamento = new Departamento();
    }
    
    public List<Departamento> mostrarDepartamentos(){
        return daoDepartamento.findDepartamentoEntities();
    }
}
