package com.bean;

import com.dao.OrdersDAO;
import model.Orders;
import java.io.Serializable;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;
import model.UserDb;

@ManagedBean(name = "ordersBean")
@SessionScoped
public class OrdersBean implements Serializable {

    private List<AdminTransactionBean> transactions;
    private List<UserTransactionBean> transhistory;
    private List<Orders> ordersList;
    private UserDb currentUser;
    private OrdersDAO ordersDAO = new OrdersDAO(); // Assuming you have OrdersDAO implemented
    private int idUser;

    public OrdersBean() {
        // Initialize ordersList by fetching from DAO
        this.ordersList = ordersDAO.getAllOrders();

        // Inisialisasi transaksi user ketika objek dibuat
        loadUserTransactions();

        currentUser = (UserDb) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("user");
        idUser = currentUser.getIdUser();
        System.out.println("ID USER ITU" + idUser);
        // Inisialisasi transaksi user

        this.transhistory = ordersDAO.getUserTransactions(idUser);
    }

    private Integer getLoggedInUserId() {
        int idUser;
        currentUser = (UserDb) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("user");
        idUser = currentUser.getIdUser();
        System.out.println("ID USER ITU" + idUser);
        return idUser;
    }

    // Method untuk memuat transaksi berdasarkan user yang sedang login
    public void loadUserTransactions() {
        Integer userId = getLoggedInUserId();
        if (userId != null) {
            this.transhistory = ordersDAO.getUserTransactions(userId);  // Pass the user ID to the DAO
        } else {
            this.transhistory = null;  // No transactions if the user ID is missing
        }
    }

    // Method untuk mengecek apakah ada transaksi
    public boolean hasTransactions() {
        return transactions != null && !transactions.isEmpty();
    }

    public List<UserTransactionBean> getTranshistory() {
        return transhistory;
    }

    public void setTranshistory(List<UserTransactionBean> transhistory) {
        this.transhistory = transhistory;
    }

    public List<Orders> getOrdersList() {
        return ordersList;
    }

    public void setOrdersList(List<Orders> ordersList) {
        this.ordersList = ordersList;
    }
}
