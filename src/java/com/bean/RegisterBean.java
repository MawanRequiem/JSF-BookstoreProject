package com.bean;

import com.dao.RegisterDAO;
import model.UserDb;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;

@ManagedBean
@RequestScoped
public class RegisterBean {

    private UserDb user = new UserDb();
    private RegisterDAO userDAO = new RegisterDAO();
    private String confirmPassword;

    // Method untuk register user baru
    public String register() {
        FacesContext context = FacesContext.getCurrentInstance();

        // Validasi apakah username atau email sudah digunakan
        if (userDAO.isUsernameTaken(user.getUsername())) {
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Username sudah digunakan!", null));
            return null;
        }

        if (userDAO.isEmailTaken(user.getEmail())) {
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Email sudah digunakan!", null));
            return null;
        }

        // Validasi password
        if (!user.getPassword().equals(confirmPassword)) {
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Password dan konfirmasi password tidak cocok!", null));
            return null;
        }

        // Simpan user ke database
        userDAO.registerUser(user);
        context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Registrasi berhasil!", null));
        return "login.xhtml?faces-redirect=true";  // Redirect ke halaman login
    }

    // Getter dan Setter
    public UserDb getUser() {
        return user;
    }

    public void setUser(UserDb user) {
        this.user = user;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }
}
