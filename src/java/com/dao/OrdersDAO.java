package com.dao;

import com.bean.AdminTransactionBean;
import model.Orders;
import org.hibernate.Session;
import org.hibernate.Transaction;
import util.HibernateUtil;
import java.util.List;
import org.hibernate.Query;

public class OrdersDAO {
    
    // Fetch all orders from the database
    public List<AdminTransactionBean> getAdminTransactions() {
        Session session = HibernateUtil.getSessionFactory().openSession();
        List<AdminTransactionBean> transactions = null;

        try {
            session.beginTransaction();

            // HQL untuk join tabel Orders, UserDb, Orderitems, Buku
            String hql = "SELECT new model.AdminTransactionBean("
                    + "o.orderId, o.totalPrice, o.orderDate, o.status, "
                    + "u.idUser, u.namaUser, "
                    + "i.buku.idBuku, i.buku.namaBuku, i.kuantitas) "
                    + "FROM Orders o "
                    + "JOIN o.userDb u "
                    + "JOIN o.orderitemses i";

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
    
    public List<Orders> getAllOrders() {
        Transaction transaction = null;
        List<Orders> ordersList = null;
        
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            // Start transaction
            transaction = session.beginTransaction();

            // Use HQL to fetch Orders with UserDb and OrderItems (with Buku entity in OrderItems)
            String hql = "FROM Orders o LEFT JOIN FETCH o.userDb u LEFT JOIN FETCH o.orderitemses oi LEFT JOIN FETCH oi.buku";
            Query query = session.createQuery(hql);  // Hibernate 4.3 tidak memiliki tipe generik di Query

            ordersList = (List<Orders>) query.list();  // Casting hasil query ke List<Orders>

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



    // Update order in the database
//    public void updateOrder(Orders order) {
//        Transaction transaction = null;
//        
//        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
//            // Start transaction
//            transaction = session.beginTransaction();
//            
//            // Update the order
//            session.update(order);
//            
//            // Commit transaction
//            transaction.commit();
//        } catch (Exception e) {
//            if (transaction != null) {
//                transaction.rollback();
//            }
//            e.printStackTrace();
//        }
//    }

