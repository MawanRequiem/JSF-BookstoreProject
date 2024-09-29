package com.bean;

import com.dao.UserAddressDAO;
import com.dao.UserDbDAO;
import model.UserDb;
import model.UserAddress;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;

@ManagedBean(name = "accountSettingsBean")
@SessionScoped
public class AccountSettingsBean {
    private UserDb user;
    private UserAddress userAddress;
    private Integer userId; // Using Integer to avoid NullPointerException

    private UserDbDAO userDbDAO = new UserDbDAO();
    private UserAddressDAO userAddressDAO = new UserAddressDAO();

    @PostConstruct
    public void init() {
        // Load userId from session
        ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
        HttpSession session = (HttpSession) externalContext.getSession(true);

        if (session.getAttribute("userId") != null) {
            userId = (Integer) session.getAttribute("userId");
            
            // Load user data
            user = userDbDAO.getUserById(userId); 
            
            // Load address data
            userAddress = userAddressDAO.getAddressByUserId(userId);
            
            // Initialize empty UserAddress if not found
            if (userAddress == null) {
                userAddress = new UserAddress();
            }
            
            // Debugging output to verify data loading
            System.out.println("User loaded: " + user);
            System.out.println("UserAddress loaded: " + userAddress);
        } else {
            // Handle case when userId is not in session, like redirecting to login page
            try {
                externalContext.redirect("login.xhtml");
            } catch (Exception e) {
                e.printStackTrace();
            }
            return;  // Exit if no userId found in session
        }
    }

    // Method to update both user and address
    public void updateAccount() {
        // Update user details
        userDbDAO.updateUser(user);
        
        // Update address details
        userAddressDAO.updateAddress(userAddress);
    }
    
    // Method to delete user account
    public String deleteAccount() {
            System.out.println("Delete account called for user ID: " + userId);

        if (userId != null) {
            // Delete the associated address first
            userAddressDAO.deleteAddressByUserId(userId);

            // Then delete the user
            userDbDAO.deleteUser(userId);

            // Invalidate session and redirect to login page
            ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
            HttpSession session = (HttpSession) externalContext.getSession(false);
            if (session != null) {
                session.invalidate();
            }
            try {
                externalContext.redirect("login.xhtml");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    // Getters and setters for UserDb and UserAddress
    public UserDb getUser() {
        return user;
    }

    public void setUser(UserDb user) {
        this.user = user;
    }

    public UserAddress getUserAddress() {
        return userAddress;
    }

    public void setUserAddress(UserAddress userAddress) {
        this.userAddress = userAddress;
    }
    
    
}
