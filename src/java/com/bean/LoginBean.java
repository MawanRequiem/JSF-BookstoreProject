package com.bean;

import com.dao.LoginDAO;
import model.AdminDb;
import model.UserDb;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@ManagedBean(name = "loginBean")
@RequestScoped
public class LoginBean {

    private String username;
    private String password;
    private LoginDAO loginDAO;

    // Constructor
    public LoginBean() {
        loginDAO = new LoginDAO();
    }

    // Getter and Setter for username and password
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    // Login method to handle user and admin login
    public String login() {
        Object result = loginDAO.loginUserOrAdmin(username, password);

        if (result instanceof AdminDb) {
            // If AdminDb object is returned, it's an admin login
            AdminDb admin = (AdminDb) result;
            setSession("user", admin);  // Store the entire admin object
            setSession("userType", "admin");
            return redirect("adminDashboard.xhtml");
        } else if (result instanceof UserDb) {
            // If UserDb object is returned, it's a regular user login
            UserDb user = (UserDb) result;
            setSession("user", user);  // Store the entire user object
            setSession("userType", "user");
            return redirect("index.xhtml");
        } else {
            // Invalid credentials
            FacesContext.getCurrentInstance().addMessage(null,
                    new javax.faces.application.FacesMessage("Invalid username or password"));
            return null;
        }
    }

    // Helper method to redirect
    private String redirect(String page) {
        try {
            FacesContext.getCurrentInstance().getExternalContext().redirect(page);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    // Helper method to set session attributes
    private void setSession(String key, Object value) {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        HttpSession session = (HttpSession) facesContext.getExternalContext().getSession(true);
        session.setAttribute(key, value);
    }

    // Logout method
    public String logout() {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        HttpSession session = (HttpSession) facesContext.getExternalContext().getSession(false);
        if (session != null) {
            session.invalidate(); // Invalidate session
        }
        return redirect("login.xhtml"); // Redirect to login page
    }
}
