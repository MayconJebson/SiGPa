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
    
    private static final int[] pesoCPF = {11, 10, 9, 8, 7, 6, 5, 4, 3, 2};
    
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
            String cpf = pessoa.getCpf().substring(0,3) + pessoa.getCpf().substring(4,7) 
                       + pessoa.getCpf().substring(8,11) + pessoa.getCpf().substring(12,14);
            if (isValidCPF(cpf)){
                pessoa.setId(null);
                daoPessoa.create(pessoa);
                pessoa = new Pessoa();
                context.addMessage("formPessoa", new FacesMessage("Pessoa foi inserida com sucesso!"));
            }
            else{
                pessoa.setCpf("");
                context.addMessage("formPessoa", new FacesMessage("CPF Inválido, Insira outro CPF!"));
            }
        } catch (Exception ex) {
            context.addMessage("formPessoa", new FacesMessage("Pessoa não pode ser inserida"));
            Logger.getLogger(PessoaBean.class.getName()).log(Level.SEVERE, null, ex);
        }
        pesquisarPessoas();
    }
    
    public void alterar(){
        FacesContext context = FacesContext.getCurrentInstance();
        try {
            String cpf = pessoa.getCpf().substring(0,3) + pessoa.getCpf().substring(4,7) 
                       + pessoa.getCpf().substring(8,11) + pessoa.getCpf().substring(12,14);
            if (isValidCPF(cpf)){
                daoPessoa.edit(pessoa);
                pessoa = new Pessoa();
                context.addMessage("formPessoa", new FacesMessage("Pessoa foi alterada com sucesso!"));
            }
            else{
                pessoa.setCpf("");
                context.addMessage("formPessoa", new FacesMessage("CPF Inválido, Insira outro CPF!"));
            }
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

    private static int calcularDigito(String str, int[] peso) {
        int soma = 0;
        for (int indice=str.length()-1, digito; indice >= 0; indice-- ) {
            digito = Integer.parseInt(str.substring(indice,indice+1));
            soma += digito*peso[peso.length-str.length()+indice];
        }
        soma = 11 - soma % 11;
        return soma > 9 ? 0 : soma;
    }

    public boolean isValidCPF(String cpf) {
        
        boolean equals = true;
        
        for (int i = 1; i < cpf.length(); i++) {
            if (!cpf.substring(i,i+1).equals(cpf.substring(0,1))){
                equals = false;
            }
        }
        
        if ((cpf==null) || (cpf.length()!=11) || equals) return false;

        Integer digito1 = calcularDigito(cpf.substring(0,9), pesoCPF);
        Integer digito2 = calcularDigito(cpf.substring(0,9) + digito1, pesoCPF);
        return cpf.equals(cpf.substring(0,9) + digito1.toString() + digito2.toString());
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
    
    public List<Pessoa> mostrarPessoas(){
        return daoPessoa.findPessoaEntities();
    }
    
    public void limpar(){
        pessoa = new Pessoa();
    }
}
