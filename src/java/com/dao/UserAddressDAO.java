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
}
