package com.dao;

import model.UserDb;
import org.hibernate.Session;
import org.hibernate.Transaction;
import util.HibernateUtil;

public class UserDbDAO {

    // Function to get user by ID
    public UserDb getUserById(int userId) {
        Session session = null;
        UserDb user = null;

        try {
            session = HibernateUtil.getSessionFactory().openSession();
            user = (UserDb) session.createQuery("FROM UserDb WHERE idUser = :userId")
                                   .setParameter("userId", userId)
                                   .uniqueResult();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (session != null) {
                session.close();
            }
        }

        return user;
    }

    // Function to get user by username and password (for login)
    public UserDb getUserByUsernameAndPassword(String username, String password) {
        Session session = null;
        UserDb user = null;

        try {
            session = HibernateUtil.getSessionFactory().openSession();
            user = (UserDb) session.createQuery("FROM UserDb WHERE username = :username AND password = :password")
                                   .setParameter("username", username)
                                   .setParameter("password", password)
                                   .uniqueResult();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (session != null) {
                session.close();
            }
        }

        return user;
    }

    // Function to update user details
    public void updateUser(UserDb user) {
        Transaction transaction = null;
        Session session = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            transaction = session.beginTransaction();
            session.update(user);
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
    
    // Method to delete user by ID
    public void deleteUser(int userId) {
        Transaction transaction = null;
        Session session = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            transaction = session.beginTransaction();

            // Get the user entity
            UserDb user = (UserDb) session.get(UserDb.class, userId);
            if (user != null) {
                session.delete(user);  // Delete the user entity
            }

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
