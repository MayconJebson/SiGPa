/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package beans;

import dao.LoginJpaController;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import modelos.Login;
import util.JPAUtil;

/**
 *
 * @author maycon
 */
@ManagedBean
@SessionScoped
public class LoginBean {
    
    private Login login;
    LoginJpaController daoLogin;
    boolean logado;
    
    public LoginBean(){
        login = new Login();
        daoLogin = new LoginJpaController(JPAUtil.factory);
        logado = false;
    }

    public void logar() throws IOException{
        FacesContext context = FacesContext.getCurrentInstance();
        logado = daoLogin.findLogin(login.getLogin(), login.getSenha());
        if(logado){
            FacesContext.getCurrentInstance().getExternalContext().redirect("index.html");
        }else{
            context.addMessage("loginform", new FacesMessage("Login e/ou Senha estão inválidos"));
            Logger.getLogger(LoginBean.class.getName()).log(Level.SEVERE, null);
            login = new Login();
            //FacesContext.getCurrentInstance().getExternalContext().redirect("login.xhtml");
        }
    }
    
    public void logout(){
        login = new Login();
        logado = false;
    }
    
    public void validarLogin() throws IOException{
        if (!logado){
            FacesContext.getCurrentInstance().getExternalContext().redirect("login.xhtml");
        }
    }
    
    public void redirecionarIndex() throws IOException{
        FacesContext.getCurrentInstance().getExternalContext().redirect("index.html");
    }
    
    public void redirecionarLogin() throws IOException{
        FacesContext.getCurrentInstance().getExternalContext().redirect("login.xhtml");
    }

    /**
     * @return the login
     */
    public Login getLogin() {
        return login;
    }

    /**
     * @param login the login to set
     */
    public void setLogin(Login login) {
        this.login = login;
    }
}
