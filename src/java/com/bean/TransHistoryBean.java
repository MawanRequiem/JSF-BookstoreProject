package com.bean;

import com.dao.OrdersDAO;
import model.Orders;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;
import java.io.Serializable;
import java.util.List;

@ManagedBean(name = "transHistoryBean")
@SessionScoped
public class TransHistoryBean implements Serializable {
    
    private List<Orders> ordersList;
    
    // Inject OrderDAO
    private OrdersDAO ordersDAO = new OrdersDAO();

    // Method untuk menginisialisasi transaksi history berdasarkan user ID
    public void init() {
        // Mendapatkan user ID dari session
        FacesContext facesContext = FacesContext.getCurrentInstance();
        HttpSession session = (HttpSession) facesContext.getExternalContext().getSession(false);
        Integer userId = (Integer) session.getAttribute("userId");

        if (userId != null) {
            // Panggil DAO untuk mengambil daftar pesanan berdasarkan user ID
            this.ordersList = ordersDAO.getOrdersByUserId(userId.longValue()); // pastikan userId sesuai tipe Long
        }
    }

    // Getter untuk ordersList
    public List<Orders> getOrdersList() {
        return ordersList;
    }

    public void setOrdersList(List<Orders> ordersList) {
        this.ordersList = ordersList;
    }
}
