package com.dao;

import model.Orders;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;

import java.util.List;

public class OrdersDAO {

    private SessionFactory sessionFactory;

    // Constructor: Initialize Hibernate SessionFactory
    public OrdersDAO() {
        Configuration configuration = new Configuration().configure(); // Read config from hibernate.cfg.xml
        ServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder()
                                            .applySettings(configuration.getProperties())
                                            .build();
        sessionFactory = configuration.buildSessionFactory(serviceRegistry); // Create sessionFactory
    }

    // Method to get all orders from the database
    @SuppressWarnings("unchecked")  // Suppress unchecked cast warning
    public List<Orders> getAllOrders() {
        Session session = sessionFactory.openSession();
        List<Orders> orders = null;
        try {
            orders = (List<Orders>) session.createQuery("from Orders").list(); // Manual casting
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            session.close();
        }
        return orders;
    }

    // Method to get an order by ID
    public Orders getOrderById(int orderId) {
        Session session = sessionFactory.openSession();
        Orders order = null;
        try {
            order = (Orders) session.get(Orders.class, orderId); // Manual casting
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            session.close();
        }
        return order;
    }

    // Method to save or update an order
    public void saveOrUpdateOrder(Orders order) {
        Session session = sessionFactory.openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            session.saveOrUpdate(order);  // Save or update the order
            tx.commit();
        } catch (Exception e) {
            if (tx != null) {
                tx.rollback();
            }
            e.printStackTrace();
        } finally {
            session.close();
        }
    }

    // Method to delete an order by ID
    public void deleteOrder(int orderId) {
        Session session = sessionFactory.openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            Orders order = (Orders) session.get(Orders.class, orderId); // Manual casting
            if (order != null) {
                session.delete(order);  // Delete order if it exists
                tx.commit();
            }
        } catch (Exception e) {
            if (tx != null) {
                tx.rollback();
            }
            e.printStackTrace();
        } finally {
            session.close();
        }
    }
}