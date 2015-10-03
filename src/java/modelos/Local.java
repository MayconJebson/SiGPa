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
public class Local implements Serializable {
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    
    private String nome;
    
    @OneToOne
    private Pessoa responsavel;
    
    @OneToMany
    private List<Patrimonio> listPatrimonios;
    
    @OneToOne
    private Departamento departamento;

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
     * @return the listPatrimonios
     */
    public List<Patrimonio> getListPatrimonios() {
        return listPatrimonios;
    }

    /**
     * @param listPatrimonios the listPatrimonios to set
     */
    public void setListPatrimonios(List<Patrimonio> listPatrimonios) {
        this.listPatrimonios = listPatrimonios;
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
}
