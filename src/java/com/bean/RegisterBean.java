package com.bean;

import com.dao.RegisterDAO;
import java.util.regex.Pattern;
import model.UserAddress;
import model.UserDb;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;
import org.primefaces.PrimeFaces;

@ManagedBean
@RequestScoped
public class RegisterBean {

    private UserDb user = new UserDb();
    private RegisterDAO userDAO = new RegisterDAO();
    private String confirmPassword;
    
    // New fields for address information
    private String address;
    private String city;
    private String postalCode;

     private static final String EMAIL_PATTERN = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";

    // Method to register new user
    public String register() {
        FacesContext context = FacesContext.getCurrentInstance();

 if (user.getNamaUser() == null || user.getNamaUser().trim().isEmpty() ||
    user.getUsername() == null || user.getUsername().trim().isEmpty() ||
    user.getEmail() == null || user.getEmail().trim().isEmpty() ||
    user.getPassword() == null || user.getPassword().trim().isEmpty() ||
    confirmPassword == null || confirmPassword.trim().isEmpty() ||
    address == null || address.trim().isEmpty() ||
    city == null || city.trim().isEmpty() ||
    postalCode == null || postalCode.trim().isEmpty()) {
    PrimeFaces.current().executeScript("PF('emptyFieldsDialog').show();");
    return null;
}

        // Check if email format is valid
        if (!isValidEmailFormat(user.getEmail())) {
            PrimeFaces.current().executeScript("PF('invalidEmailFormatDialog').show();");
            return null;
        }

        // Check if username is taken
        if (userDAO.isUsernameTaken(user.getUsername())) {
            PrimeFaces.current().executeScript("PF('usernameTakenDialog').show();");
            return null;
        }

        // Check if email is taken
        if (userDAO.isEmailTaken(user.getEmail())) {
            PrimeFaces.current().executeScript("PF('emailTakenDialog').show();");
            return null;
        }

        // Check if passwords match
        if (!user.getPassword().equals(confirmPassword)) {
            PrimeFaces.current().executeScript("PF('passwordMismatchDialog').show();");
            return null;
        }

        UserAddress userAddress = new UserAddress(user, address, city, postalCode);
userDAO.registerUser(user, userAddress);

// Show success dialog and redirect after a delay
PrimeFaces.current().executeScript("PF('successDialog').show(); setTimeout(function() { window.location.href = 'login.xhtml'; }, 3000);");

// Return null to stay on the same page after showing the dialog
return null;
    }

    

    // Method to validate email format
    private boolean isValidEmailFormat(String email) {
        return Pattern.compile(EMAIL_PATTERN).matcher(email).matches();
    }

    // Getters and Setters
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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }
}
