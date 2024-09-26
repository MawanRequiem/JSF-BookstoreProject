package com.bean;

import com.dao.CheckoutDAO;
import com.dao.BukuDAO;
import model.Buku;
import model.Orders;
import model.UserDb;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import java.util.Date;

@ManagedBean(name = "checkoutBean")
@SessionScoped
public class CheckoutBean {

    private CheckoutDAO checkoutDAO;
    private BukuDAO bukuDAO;
    private Buku selectedBook;
    private UserDb currentUser;

    @PostConstruct
    public void init() {
        checkoutDAO = new CheckoutDAO();
        bukuDAO = new BukuDAO();
        // Retrieve current user from session as UserDb object
        currentUser = (UserDb) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("user");
    }

    // Method to handle checkout
    public String checkout(int idBuku) {
        selectedBook = bukuDAO.getBookById(idBuku);

        // Error checking: Ensure selectedBook exists
        if (selectedBook == null) {
            FacesContext.getCurrentInstance().addMessage(null, 
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Book not found."));
            return null;
        }

        // Error checking: Ensure user is logged in
        if (currentUser == null) {
            FacesContext.getCurrentInstance().addMessage(null, 
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "You must be logged in to checkout."));
            return "login.xhtml?faces-redirect=true";  // Redirect to login if not logged in
        }

        // Error checking: Ensure sufficient stock is available
        if (selectedBook.getStockBuku() <= 0) {
            FacesContext.getCurrentInstance().addMessage(null, 
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Insufficient stock."));
            return null;  // Stay on the same page
        }

        // Proceed with the checkout if all checks pass
        Orders order = new Orders(selectedBook, currentUser, new Date());

        // Save the order in the database
        boolean isSuccess = checkoutDAO.saveOrder(order);

        if (isSuccess) {
            // Update stock in the database after successful order
            selectedBook.setStockBuku(selectedBook.getStockBuku() - 1);
            bukuDAO.updateBookStock(selectedBook);

            return "orderSuccess.xhtml?faces-redirect=true";  // Redirect to success page
        } else {
            FacesContext.getCurrentInstance().addMessage(null, 
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Checkout failed."));
            return null;  // Stay on the same page if an error occurs
        }
    }
}
