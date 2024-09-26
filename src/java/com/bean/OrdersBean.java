package com.bean;

import com.dao.OrdersDAO;
import model.Orders;
import java.io.Serializable;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

@ManagedBean(name = "ordersBean")
@SessionScoped
public class OrdersBean implements Serializable {

    private List<AdminTransactionBean> transactions;
    private List<Orders> ordersList;
    private OrdersDAO ordersDAO = new OrdersDAO(); // Assuming you have OrdersDAO implemented

    public OrdersBean() {
        // Initialize ordersList by fetching from DAO
        this.ordersList = ordersDAO.getAllOrders();
    }

    public List<Orders> getOrdersList() {
        return ordersList;
    }

    public void setOrdersList(List<Orders> ordersList) {
        this.ordersList = ordersList;
    }
}