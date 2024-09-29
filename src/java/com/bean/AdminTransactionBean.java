package com.bean;

import java.io.Serializable;
import java.util.Date;

public class AdminTransactionBean implements Serializable {

    private Integer orderId;
    private Date orderDate;
    private Integer userId;
    private String userName;
    private Integer bookId;
    private String bookName;

    // Constructor untuk HQL
    public AdminTransactionBean(Integer orderId, Date orderDate,  
                                Integer userId, String userName, 
                                Integer bookId, String bookName) {
        this.orderId = orderId;
        
        this.orderDate = orderDate;
        
        this.userId = userId;
        this.userName = userName;
        this.bookId = bookId;
        this.bookName = bookName;
        
    }

    // Getter dan Setter
    public Integer getOrderId() {
        return orderId;
    }

    public void setOrderId(Integer orderId) {
        this.orderId = orderId;
    }

    

    public Date getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(Date orderDate) {
        this.orderDate = orderDate;
    }

    

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Integer getBookId() {
        return bookId;
    }

    public void setBookId(Integer bookId) {
        this.bookId = bookId;
    }

    public String getBookName() {
        return bookName;
    }

    public void setBookName(String bookName) {
        this.bookName = bookName;
    }

   
}
