package com.dao;

import model.UserDb;
import model.AdminDb;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.Query;
import org.hibernate.service.ServiceRegistry;

public class LoginDAO {

   private SessionFactory sessionFactory;

    // Constructor: Initialize Hibernate SessionFactory using configuration
    public LoginDAO() {
        Configuration configuration = new Configuration().configure(); // Reads hibernate.cfg.xml
        ServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder()
                .applySettings(configuration.getProperties())
                .build();
        sessionFactory = configuration.buildSessionFactory(serviceRegistry);
    }

    // Method to check if the user is an Admin or a regular User based on login credentials using HQL
    public Object loginUserOrAdmin(String username, String password) {
        Session session = sessionFactory.openSession();
        Object result = null;

        try {
            Transaction tx = session.beginTransaction();

            // First, check AdminDb using HQL
            String adminHQL = "FROM AdminDb WHERE username = :username AND password = :password";
            Query adminQuery = session.createQuery(adminHQL);
            adminQuery.setParameter("username", username);
            adminQuery.setParameter("password", password);
            AdminDb admin = (AdminDb) adminQuery.uniqueResult();

            if (admin != null) {
                result = admin;
            } else {
                // If no match in AdminDb, check UserDb using HQL
                String userHQL = "FROM UserDb WHERE username = :username AND password = :password";
                Query userQuery = session.createQuery(userHQL);
                userQuery.setParameter("username", username);
                userQuery.setParameter("password", password);
                UserDb user = (UserDb) userQuery.uniqueResult();

                if (user != null) {
                    result = user;
                }
            }

            tx.commit();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            session.close();
        }

        return result;  // Return either an AdminDb or UserDb object, or null if no match
    }

    // Method to close the session factory (optional, to clean resources)
    public void close() {
        if (sessionFactory != null) {
            sessionFactory.close();
        }
    }
}
