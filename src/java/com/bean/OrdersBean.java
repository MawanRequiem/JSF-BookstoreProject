package com.bean;

import com.dao.OrdersDAO;
import model.Orders;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import java.util.List;

@ManagedBean(name = "ordersBean")
@RequestScoped
public class OrdersBean {

    private List<Orders> ordersList;
    private OrdersDAO ordersDAO;
    private Orders selectedOrder;  // To store the selected order details

    @PostConstruct
    public void init() {
        ordersDAO = new OrdersDAO();
        ordersList = ordersDAO.getAllOrders();  // Fetch all orders from DAO
    }

    public List<Orders> getOrdersList() {
        return ordersList;
    }

    public void setOrdersList(List<Orders> ordersList) {
        this.ordersList = ordersList;
    }

    public Orders getSelectedOrder() {
        return selectedOrder;
    }

    public void setSelectedOrder(Orders selectedOrder) {
        this.selectedOrder = selectedOrder;
    }

    // This method will be called when user clicks on "View Details" to set the selected order
    public void loadOrder(int orderId) {
        selectedOrder = ordersDAO.getOrderById(orderId);  // Fetch order by its ID
    }
}