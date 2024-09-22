package com.dao;

import model.UserDb;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.Query;
import util.HibernateUtil;

public class RegisterDAO {

    // Simpan user ke database
    public void registerUser(UserDb user) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();
            session.save(user);  // Menyimpan user ke database
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
        } finally {
            session.close();
        }
    }

    // Periksa apakah username sudah digunakan
    public boolean isUsernameTaken(String username) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            String hql = "SELECT COUNT(*) FROM UserDb WHERE username = :username";
            Query query = session.createQuery(hql);
            query.setParameter("username", username);
            Long count = (Long) query.uniqueResult();
            return count != null && count > 0;
        } finally {
            session.close();
        }
    }

    // Periksa apakah email sudah digunakan
    public boolean isEmailTaken(String email) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            String hql = "SELECT COUNT(*) FROM UserDb WHERE email = :email";
            Query query = session.createQuery(hql);
            query.setParameter("email", email);
            Long count = (Long) query.uniqueResult();
            return count != null && count > 0;
        } finally {
            session.close();
        }
    }
}
