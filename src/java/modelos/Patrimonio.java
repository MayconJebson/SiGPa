/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelos;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;


/**
 *
 * @author maycon
 */
@Entity
public class Patrimonio implements Serializable {
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    
    private String numPatrimonio;
    
    private String nome;
    
    private String descricao;
    
    @OneToOne
    private Local local;
    
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
     * @return the numPatrimonio
     */
    public String getNumPatrimonio() {
        return numPatrimonio;
    }

    /**
     * @param numPatrimonio the numPatrimonio to set
     */
    public void setNumPatrimonio(String numPatrimonio) {
        this.numPatrimonio = numPatrimonio;
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
     * @return the descricao
     */
    public String getDescricao() {
        return descricao;
    }

    /**
     * @param descricao the descricao to set
     */
    public void setDescricao(String descricao) {
        this.descricao = descricao;
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
}
