package com.dao;

import model.UserAddress;
import org.hibernate.Session;
import org.hibernate.Transaction;
import util.HibernateUtil;

public class UserAddressDAO {

    // Function to get address by user ID
    public UserAddress getAddressByUserId(int userId) {
        Session session = null;
        UserAddress userAddress = null;

        try {
            session = HibernateUtil.getSessionFactory().openSession();
            userAddress = (UserAddress) session.createQuery("FROM UserAddress WHERE userDb.idUser = :userId")
                                               .setParameter("userId", userId)
                                               .uniqueResult();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (session != null) {
                session.close();
            }
        }

        return userAddress;
    }
    
    public void deleteAddressByUserId(int userId) {
        Transaction transaction = null;
        Session session = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            transaction = session.beginTransaction();

            // Delete the address associated with the user
            session.createQuery("DELETE FROM UserAddress WHERE userDb.idUser = :userId")
                   .setParameter("userId", userId)
                   .executeUpdate();

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
    }

    // Function to update address
    public void updateAddress(UserAddress userAddress) {
        Transaction transaction = null;
        Session session = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            transaction = session.beginTransaction();
            session.update(userAddress);
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
    }
}
