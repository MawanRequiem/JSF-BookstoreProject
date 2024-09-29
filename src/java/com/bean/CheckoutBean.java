package com.bean;

import com.dao.CheckoutDAO;
import com.dao.BukuDAO;
import com.dao.PaymentMethodDAO;
import com.dao.UserAddressDAO;
import java.io.Serializable;
import java.util.Base64;
import model.Buku;
import model.Orders;
import model.UserDb;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import java.util.Date;
import java.util.List;
import model.PaymentMethod;
import model.UserAddress;

@ManagedBean(name = "checkoutBean")
@ViewScoped
public class CheckoutBean implements Serializable {

    private CheckoutDAO checkoutDAO;
    private BukuDAO bukuDAO;
    private UserAddressDAO userAddressDAO;
    private Buku selectedBook;
    private UserDb currentUser;
    private UserAddress userAddress;

    private int idBukuS;

    // Quantity and total price management
    private int quantity = 1;  // Default quantity is 1
    private double totalPrice;  // Total price based on quantity

    // Payment method properties
    private PaymentMethod selectedPaymentMethod;
    private List<PaymentMethod> paymentMethods;
    private PaymentMethodDAO paymentMethodDAO;
    private int selectedPaymentMethodId;  // Selected payment method ID from the dropdown

    @PostConstruct
    public void init() {
        checkoutDAO = new CheckoutDAO();
        bukuDAO = new BukuDAO();
        userAddressDAO = new UserAddressDAO();
        paymentMethodDAO = new PaymentMethodDAO();

        // Get current user from session
        currentUser = (UserDb) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("user");

        // Get selected book from request parameters
        String idBukuParam = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("idBuku");
        if (currentUser != null) {
            userAddress = userAddressDAO.getAddressByUserId(currentUser.getIdUser());
        }

        if (idBukuParam != null) {
            try {
                idBukuS = Integer.parseInt(idBukuParam);
                loadBuku(idBukuS);  // Load the book by its ID
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        }

        // Load available payment methods
        paymentMethods = checkoutDAO.getAllPaymentMethods();

        // Initialize the total price based on the default quantity
        calculateTotalPrice();
    }

    // Load the book based on its ID
    public void loadBuku(int idBuku) {
        selectedBook = bukuDAO.getBookById(idBuku);
        if (selectedBook != null) {
            calculateTotalPrice();  // Recalculate total price based on loaded book
        } else {
            FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Book not found."));
        }
    }

    // Calculate total price based on quantity
    public void calculateTotalPrice() {
        if (selectedBook != null) {
            totalPrice = selectedBook.getHargaBuku() * quantity;
        }
    }

    // Method to handle checkout
    public String checkout(int idBuku) {
        selectedBook = bukuDAO.getBookById(idBuku);
        selectedPaymentMethod = getSelectedPaymentMethod();

        if (selectedBook == null) {
            FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Book not found."));
            return null;
        }

        if (currentUser == null) {
            FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "You must be logged in to checkout."));
            return "login.xhtml?faces-redirect=true";
        }

        // Check stock availability
        if (selectedBook.getStockBuku() < quantity) {
            FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Insufficient stock."));
            return null;
        }

        // Check if payment method is selected
        if (selectedPaymentMethod == null) {
            FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Please select a payment method."));
            return null;
        }

        // Generate a payment code
        String paymentCode = generatePaymentCode();
        System.out.println(paymentCode);

        // Create a new order
        Orders order = new Orders();
        order.setBuku(selectedBook);
        order.setPaymentMethod(selectedPaymentMethod);
        order.setUserDb(currentUser);
        order.setPaymentCode(paymentCode);
        order.setDate(new Date());
        order.setKuantitas(quantity);  // Set the selected quantity
        order.setTotalHarga(totalPrice);  // Set the total price for the order

        // Save the order to the database
        boolean isSuccess = checkoutDAO.saveOrder(order);

        if (isSuccess) {
            // Reduce book stock
            selectedBook.setStockBuku(selectedBook.getStockBuku() - quantity);
            bukuDAO.updateBookStock(selectedBook);

            return "index.xhtml?faces-redirect=true";  // Redirect to success page
        } else {
            FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Checkout failed."));
            return null;
        }
    }

    // Generate payment code (overloaded for convenience)
    private String generatePaymentCode() {
        int randomDigits = (int) (Math.random() * 1_000_000_0000L);  // Generate a 10-digit random number
        return String.format("%010d", randomDigits);
    }

    // Getter and setter methods for quantity
    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
    if (quantity > selectedBook.getStockBuku()) {
        // Show an error message
        FacesContext.getCurrentInstance().addMessage(null,
            new FacesMessage(FacesMessage.SEVERITY_ERROR, "Checkout error", 
            "Stock available: " + selectedBook.getStockBuku() + " items"));
        
        // Reset the quantity to the maximum available
        this.quantity = selectedBook.getStockBuku();
    } else {
        this.quantity = quantity;
    }
    calculateTotalPrice();  // Recalculate total price when quantity changes
}

    // Getter for totalPrice
    public double getTotalPrice() {
        return totalPrice;
    }

    // Helper methods for payment selection
    public PaymentMethod getSelectedPaymentMethod() {
        for (PaymentMethod pm : paymentMethods) {
            if (pm.getIdPayment() == selectedPaymentMethodId) {
                return pm;
            }
        }
        return null;
    }

    public int getSelectedPaymentMethodId() {
        return selectedPaymentMethodId;
    }

    public void setSelectedPaymentMethodId(int selectedPaymentMethodId) {
        this.selectedPaymentMethodId = selectedPaymentMethodId;
    }

    public Buku getSelectedBook() {
        return selectedBook;
    }

    public UserDb getCurrentUser() {
        return currentUser;
    }

    public List<PaymentMethod> getPaymentMethods() {
        return paymentMethods;
    }

    public void setPaymentMethods(List<PaymentMethod> paymentMethods) {
        this.paymentMethods = paymentMethods;
    }

    public UserAddress getUserAddress() {
        return userAddress;
    }

    public void setUserAddress(UserAddress userAddress) {
        this.userAddress = userAddress;
    }

    // Helper method to convert image to Base64
    public String convertToBase64(byte[] image) {
        return Base64.getEncoder().encodeToString(image);
    }
}
