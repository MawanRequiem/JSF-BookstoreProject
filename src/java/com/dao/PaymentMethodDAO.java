package com.dao;

import model.PaymentMethod;
import org.hibernate.Session;
import org.hibernate.Transaction;
import util.HibernateUtil;

import java.util.List;

public class PaymentMethodDAO {

    // Method to fetch all payment methods from the database
    public List<PaymentMethod> getAllPaymentMethods() {
        Session session = HibernateUtil.getSessionFactory().openSession();
        List<PaymentMethod> methods = null;
        try {
            methods = session.createQuery("FROM PaymentMethod").list();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            session.close();
        }
        return methods;
    }
}
