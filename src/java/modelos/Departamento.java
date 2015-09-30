/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelos;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

/**
 *
 * @author maycon
 */

@Entity
public class Departamento implements Serializable {
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    
    @OneToOne
    private Pessoa chefe;
    
    @OneToMany
    private List<Pessoa> pessoas;
    
    private String nome, sigla;
    
    @OneToMany
    private List<Local> listLocais;

    /**
     * @return the id
     */
    public Long getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(Long id) {
        this.id = id;
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

    /**
     * @return the nome
     */
    public String getNome() {
        return nome;
    }

    /**
     * @param nome the nome to set
     */
    public void setNome(String nome) {
        this.nome = nome;
    }

    /**
     * @return the sigla
     */
    public String getSigla() {
        return sigla;
    }

    /**
     * @param sigla the sigla to set
     */
    public void setSigla(String sigla) {
        this.sigla = sigla;
    }

    /**
     * @return the listLocais
     */
    public List<Local> getListLocais() {
        return listLocais;
    }

    /**
     * @param listLocais the listLocais to set
     */
    public void setListLocais(List<Local> listLocais) {
        this.listLocais = listLocais;
    }
}
