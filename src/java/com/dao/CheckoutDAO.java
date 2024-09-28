package com.dao;

import java.util.ArrayList;
import java.util.List;
import model.Orders;
import model.PaymentMethod;
import org.hibernate.Session;
import org.hibernate.Transaction;
import util.HibernateUtil;

public class CheckoutDAO {

    // Method to save the order in the database
    public boolean saveOrder(Orders order) {
        Session session = null;
        Transaction tx = null;

        try {
            session = HibernateUtil.getSessionFactory().openSession();  // Obtain session from HibernateUtil
            tx = session.beginTransaction();
            session.save(order);  // Save the order
            tx.commit();
            System.out.println("Order saved successfully for book ID: " + order.getBuku().getIdBuku());
            return true;
        } catch (Exception e) {
            if (tx != null) {
                tx.rollback();
            }
            e.printStackTrace();
            System.out.println("Error saving order: " + e.getMessage());
            return false;
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }
    
    public List<PaymentMethod> getAllPaymentMethods() {
        Session session = null;
        List<PaymentMethod> paymentMethods = new ArrayList<>();
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            paymentMethods = session.createQuery("from PaymentMethod").list();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (session != null) {
                session.close();
            }
        }
        return paymentMethods;
    }
}
