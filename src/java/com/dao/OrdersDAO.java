package com.dao;

import com.bean.AdminTransactionBean;
import com.bean.UserTransactionBean;
import model.Orders;
import org.hibernate.Session;
import org.hibernate.Transaction;
import util.HibernateUtil;
import java.util.List;
import javax.persistence.criteria.Order;
import org.hibernate.Query;
import org.hibernate.SessionFactory;

public class OrdersDAO {
    
    private SessionFactory sessionFactory;

    public OrdersDAO() {
        this.sessionFactory = HibernateUtil.getSessionFactory();
    }
    
    // Fetch all orders from the database
    public List<AdminTransactionBean> getAdminTransactions() {
        Session session = HibernateUtil.getSessionFactory().openSession();
        List<AdminTransactionBean> transactions = null;

        try {
            session.beginTransaction();

            // HQL untuk join tabel Orders, UserDb, Orderitems, Buku
            String hql = "SELECT new model.AdminTransactionBean("
                    + "o.orderId, o.orderDate, o.kuantitas, o.totalHarga, "
                    + "u.idUser, u.namaUser, "
                    + "pm.payment"
                    + "b.idBuku, b.namaBuku) "
                    + "FROM Orders o "
                    + "JOIN o.userDb u "
                    + "JOIN o.buku b"
                    + "JOIN o.paymentMethod pm";

            

            // Buat query menggunakan Hibernate versi 4.3
            Query query = session.createQuery(hql);
            transactions = (List<AdminTransactionBean>) query.list();

            session.getTransaction().commit();
        } catch (Exception e) {
            if (session.getTransaction() != null) session.getTransaction().rollback();
            e.printStackTrace();
        } finally {
            session.close();
        }

        return transactions;
    }
    
     // Method baru untuk mengambil UserTransactionBean
public List<UserTransactionBean> getUserTransactions(Integer userId) {
    Session session = HibernateUtil.getSessionFactory().openSession();
    List<UserTransactionBean> transhistory = null;

    try {
        session.beginTransaction();

        // Modified HQL to filter by logged-in user ID and sort by orderId (ascending order)
        String hql = "SELECT new com.bean.UserTransactionBean(" +
                "o.idOrder, b.namaBuku, o.kuantitas, o.totalHarga, " +
                "pm.payment, o.date) " +
                "FROM Orders o " +
                "JOIN o.buku b " +
                "JOIN o.paymentMethod pm " +
                "JOIN o.userDb u " +
                "WHERE u.idUser = :userId " +  // Filter orders by the user ID
                "ORDER BY o.idOrder ASC";  // Sort by orderId in ascending order

        Query query = session.createQuery(hql);
        query.setParameter("userId", userId);  // Set the userId parameter

        transhistory = (List<UserTransactionBean>) query.list();

        session.getTransaction().commit();
    } catch (Exception e) {
        if (session.getTransaction() != null) session.getTransaction().rollback();
        e.printStackTrace();
    } finally {
        session.close();
    }

    return transhistory;
}


    public List<UserTransactionBean> getUserTransactions() {
        Session session = HibernateUtil.getSessionFactory().openSession();
        List<UserTransactionBean> transhistory = null;

        try {
            session.beginTransaction();

            // HQL untuk join tabel Orders, Buku, PaymentMethod, dan mengambil data yang relevan
            String hql = "SELECT new com.bean.UserTransactionBean(" +
                    "o.idOrder, b.namaBuku, o.kuantitas, o.totalHarga, " +
                    "pm.payment, o.date) " +
                    "FROM Orders o " +
                    "JOIN o.buku b " +
                    "JOIN o.paymentMethod pm " +
                    "JOIN o.userDb u";

            Query query = session.createQuery(hql);
            transhistory = (List<UserTransactionBean>) query.list();

            session.getTransaction().commit();
        } catch (Exception e) {
            if (session.getTransaction() != null) session.getTransaction().rollback();
            e.printStackTrace();
        } finally {
            session.close();
        }

        return transhistory;
    }
    
    // Method untuk mengambil pesanan berdasarkan userId
public List<Orders> getOrdersByUserId(Long userId) {
    Session session = null;
    Transaction transaction = null;
    List<Orders> orders = null;

    try {
        session = sessionFactory.openSession();
        transaction = session.beginTransaction();

        String hql = "FROM Orders o WHERE o.userDb.idUser = :userId";
        Query query = session.createQuery(hql);
        query.setParameter("userId", userId);

        orders = (List<Orders>) query.list();
        transaction.commit();

    } catch (Exception e) {
        if (transaction != null) {
            transaction.rollback();
        }
        e.printStackTrace();
    } finally {
        if (session != null) {
            session.close();
        }
    }
    return orders;
}

    
    public List<Orders> getAllOrders() {
        Transaction transaction = null;
    List<Orders> ordersList = null;

    Session session = HibernateUtil.getSessionFactory().openSession();
    try {
        // Start transaction
        transaction = session.beginTransaction();

        // Fetch all orders, users, and payment methods
        String hql = "FROM Orders o LEFT JOIN FETCH o.userDb u LEFT JOIN FETCH o.buku b LEFT JOIN FETCH o.paymentMethod pm";
        Query query = session.createQuery(hql); 

        ordersList = (List<Orders>) query.list();  // Casting the result to List<Orders>

        // Commit the transaction
        transaction.commit();
    } catch (Exception e) {
        if (transaction != null) {
            transaction.rollback();
        }
        e.printStackTrace();
    } finally {
        session.close();
    }
    return ordersList;
}
}