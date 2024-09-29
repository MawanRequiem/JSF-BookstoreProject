package com.bean;

import com.dao.OrdersDAO;
import model.Orders;
import java.io.Serializable;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;

@ManagedBean(name = "ordersBean")
@SessionScoped
public class OrdersBean implements Serializable {

    private List<AdminTransactionBean> transactions;
    private List<UserTransactionBean> transhistory;
    private List<Orders> ordersList;
    private OrdersDAO ordersDAO = new OrdersDAO(); // Assuming you have OrdersDAO implemented

    public OrdersBean() {
        // Initialize ordersList by fetching from DAO
        this.ordersList = ordersDAO.getAllOrders();
        
        // Inisialisasi transaksi user ketika objek dibuat
        loadUserTransactions();

        // Inisialisasi transaksi user
        this.transhistory = ordersDAO.getUserTransactions();
    }

    private Integer getLoggedInUserId() {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        HttpSession session = (HttpSession) facesContext.getExternalContext().getSession(false);
        return (Integer) session.getAttribute("userId");
    }

    // Method untuk memuat transaksi berdasarkan user yang sedang login
    public void loadUserTransactions() {
        Integer userId = getLoggedInUserId();
        if (userId != null) {
            this.transhistory = ordersDAO.getUserTransactions();
        } else {
            this.transhistory = null;  // Jika userId tidak ada
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