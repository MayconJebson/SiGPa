/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package beans;

import com.lowagie.text.BadElementException;
import com.lowagie.text.DocumentException;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Document;
import com.lowagie.text.Element;
import com.lowagie.text.Phrase;
import dao.DepartamentoJpaController;
import dao.LocalJpaController;
import dao.PatrimonioJpaController;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import modelos.Departamento;
import modelos.Local;
import modelos.Patrimonio;
import util.JPAUtil;

/**
 *
 * @author maycon
 */
@ManagedBean
@SessionScoped
public class RelatoriosBean implements Serializable{
    
    private Departamento departamento;
    private Local local;
    PatrimonioJpaController daoPatrimonio;
    LocalJpaController daoLocal;
    DepartamentoJpaController daoDepartamento;
    private List<Patrimonio> patrimonios;
    
    public RelatoriosBean(){
        departamento = new Departamento();
        local = new Local();
        daoPatrimonio = new PatrimonioJpaController(JPAUtil.factory);
        daoLocal = new LocalJpaController(JPAUtil.factory);
        daoDepartamento = new DepartamentoJpaController(JPAUtil.factory);
        patrimonios = new ArrayList<Patrimonio>();
    }
    
    public void pesquisarPatrimoniosDepartamento(){
        patrimonios = new ArrayList<Patrimonio>();
        departamento = daoDepartamento.findDepartamento(departamento.getId());
        for(Patrimonio p : daoPatrimonio.findPatrimonioEntities()){
            for(Local l : daoLocal.findLocalEntities()){
                if(p.getLocal().getId() == l.getId() && l.getDepartamento().getId() == departamento.getId()){
                    patrimonios.add(p);
                }
            }
        }
    }
    
    public void pesquisarPatrimoniosLocal(){
        patrimonios = new ArrayList<Patrimonio>();
        local = daoLocal.findLocal(local.getId());
        for(Patrimonio p : daoPatrimonio.findPatrimonioEntities()){
            if (p.getLocal().getId() == local.getId()){
                patrimonios.add(p);
            }
        }
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
    
    public void redirecionarIndex() throws IOException{
        departamento = new Departamento();
        local = new Local();
        patrimonios = new ArrayList<Patrimonio>();
        FacesContext.getCurrentInstance().getExternalContext().redirect("index.html");
    }
    
    public void redirecionarLogin() throws IOException{
        departamento = new Departamento();
        local = new Local();
        patrimonios = new ArrayList<Patrimonio>();
        FacesContext.getCurrentInstance().getExternalContext().redirect("login.xhtml");
    }
    
    public void limpar(){
        departamento = new Departamento();
        local = new Local();
        patrimonios = new ArrayList<Patrimonio>();
    }
    
    public void preProcessPDFDepartamento(Object document) throws IOException, BadElementException, DocumentException {  
        //cria o documento  
        Document pdf = (Document) document;

        //seta a margin e página, precisa estar antes da abertura do documento, ou seja da linha: pdf.open()  
        //pdf.setMargins(200f, 200f, 200f, 200f);
        pdf.setPageSize(PageSize.A4);

        pdf.open();
        
        Paragraph p = new Paragraph(); 
        p.add(new Phrase("Bens do " + departamento.getSigla()
                + " - " + departamento.getNome() + "\n\n"));
        p.setAlignment(Element.ALIGN_CENTER); 
        p.setIndentationLeft(18); 
        p.setFirstLineIndent(-18);
        
        pdf.add(p);
    }
    
    public void preProcessPDFLocal(Object document) throws IOException, BadElementException, DocumentException {  
        //cria o documento  
        Document pdf = (Document) document;

        //seta a margin e página, precisa estar antes da abertura do documento, ou seja da linha: pdf.open()  
        //pdf.setMargins(200f, 200f, 200f, 200f);
        pdf.setPageSize(PageSize.A4); 

        pdf.open();
        
        Paragraph p = new Paragraph(); 
        p.add(new Phrase("Bens do Local - " + local.getNome() + "\n\n"));
        p.setAlignment(Element.ALIGN_CENTER); 
        p.setIndentationLeft(18); 
        p.setFirstLineIndent(-18);
        
        pdf.add(p);
    }
}
