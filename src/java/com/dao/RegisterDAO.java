package com.dao;

import model.UserAddress;
import model.UserDb;
import org.hibernate.Session;
import org.hibernate.Transaction;
import util.HibernateUtil;

public class RegisterDAO {

    // Simpan user dan alamat ke database
    public void registerUser(UserDb user, UserAddress userAddress) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();
            session.save(user);  // Simpan user
            userAddress.setUserDb(user);  // Set user relationship in address
            session.save(userAddress);  // Simpan alamat user
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
            Long count = (Long) session.createQuery(hql)
                                       .setParameter("username", username)
                                       .uniqueResult();
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
            Long count = (Long) session.createQuery(hql)
                                       .setParameter("email", email)
                                       .uniqueResult();
            return count != null && count > 0;
        } finally {
            session.close();
        }
    }
}
