/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package beans;

import dao.PessoaJpaController;
import dao.exceptions.NonexistentEntityException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;
import modelos.Pessoa;
import util.JPAUtil;

/**
 *
 * @author maycon
 */
@ManagedBean
@RequestScoped
public class PessoaBean {
    
    private Pessoa pessoa;
    PessoaJpaController daoPessoa;
    private List<Pessoa> pessoas;
    
    public PessoaBean(){
        pessoa = new Pessoa();
        daoPessoa = new PessoaJpaController(JPAUtil.factory);
        pessoas = new ArrayList<Pessoa>();
        pesquisarPessoas();
    }

    /**
     * @return the pessoa
     */
    public Pessoa getPessoa() {
        return pessoa;
    }

    /**
     * @param pessoa the pessoa to set
     */
    public void setPessoa(Pessoa pessoa) {
        this.pessoa = pessoa;
    }
    
    public void inserir(){
        FacesContext context = FacesContext.getCurrentInstance();
        try {
            pessoa.setId(null);
            daoPessoa.create(pessoa);
            context.addMessage("formPessoa", new FacesMessage("Pessoa foi inserido com sucesso!"));
            pessoa = new Pessoa();
        } catch (Exception ex) {
            context.addMessage("formPessoa", new FacesMessage("Pessoa não pode ser inserido"));
            Logger.getLogger(PessoaBean.class.getName()).log(Level.SEVERE, null, ex);
        }
        pesquisarPessoas();
    }
    
    public void alterar(){
        FacesContext context = FacesContext.getCurrentInstance();
        try {
            daoPessoa.edit(pessoa);
            pessoa = new Pessoa();
            context.addMessage("formPessoa", new FacesMessage("Pessoa foi alterada com sucesso!"));
        } catch (NonexistentEntityException ex) {
            context.addMessage("formPessoa", new FacesMessage("Error"));
            Logger.getLogger(PessoaBean.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            context.addMessage("formPessoa", new FacesMessage("Pessoa não pode ser alterada"));
            Logger.getLogger(PessoaBean.class.getName()).log(Level.SEVERE, null, ex);
        }
        pesquisarPessoas();
    }
    
    public void excluir(){
        FacesContext context = FacesContext.getCurrentInstance();
        try {
            daoPessoa.destroy(pessoa.getId());
            pessoa = new Pessoa();
            context.addMessage("formPessoa", new FacesMessage("Pessoa foi excluída com sucesso!"));
        } catch (NonexistentEntityException ex) {
            context.addMessage("formPessoa", new FacesMessage("Pessoa não pode ser excluída"));
            Logger.getLogger(PessoaBean.class.getName()).log(Level.SEVERE, null, ex);
        }
        pesquisarPessoas();
    }

    /**
     * @return the pessoas
     */
    public List<Pessoa> getPessoas() {
        return pessoas;
    }

    /**
     * @param pessoas the pessoas to set
     */
    public void setPessoas(List<Pessoa> pessoas) {
        this.pessoas = pessoas;
    }
    
    public void pesquisarPessoas(){
        pessoas = daoPessoa.findPessoaEntities();
    }
}
