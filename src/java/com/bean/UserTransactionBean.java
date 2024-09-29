package com.bean;

import java.io.Serializable;
import java.util.Date;

public class UserTransactionBean implements Serializable {
    private Integer orderId;
    private String bookName;
    private Integer quantity;
    private Double totalPrice;
    private String paymentMethod;
    private Date orderDate;

    // Constructor untuk memudahkan mapping
    public UserTransactionBean(Integer orderId, String bookName, Integer quantity, 
                               Double totalPrice, String paymentMethod, Date orderDate) {
        this.orderId = orderId;
        this.bookName = bookName;
        this.quantity = quantity;
        this.totalPrice = totalPrice;
        this.paymentMethod = paymentMethod;
        this.orderDate = orderDate;
    }

    // Getter dan Setter
    public Integer getOrderId() {
        return orderId;
    }

    public void setOrderId(Integer orderId) {
        this.orderId = orderId;
    }

    public String getBookName() {
        return bookName;
    }

    public void setBookName(String bookName) {
        this.bookName = bookName;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(Double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public Date getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(Date orderDate) {
        this.orderDate = orderDate;
    }
}
