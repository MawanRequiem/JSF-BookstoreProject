/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dao;

import org.hibernate.Session;
import org.hibernate.Transaction;
import java.util.List;
import model.Buku;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import util.HibernateUtil;

public class AdminDAO {

    public List<Buku> getAllBuku() {
    Session session = HibernateUtil.getSessionFactory().openSession();
    List<Buku> bukuList = session.createQuery("from Buku").list(); // No need to specify class type in Hibernate 4.3
    session.close();
    return bukuList;
}

    public void updateBuku(Buku buku) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction transaction = session.beginTransaction();
        session.update(buku);
        transaction.commit();
        session.close();
    }

   public void deleteBuku(Buku buku) {
    Session session = HibernateUtil.getSessionFactory().openSession();
    Transaction transaction = null;
    try {
        transaction = session.beginTransaction();
        session.delete(buku);
        transaction.commit();
    } catch (Exception e) {
        if (transaction != null) {
            transaction.rollback();
        }
        e.printStackTrace();
    } finally {
        session.close();
    }
}
   
   public void addBuku(Buku buku) {
    Session session = HibernateUtil.getSessionFactory().openSession();
    Transaction transaction = null;
    try {
        transaction = session.beginTransaction();
        session.save(buku);
        transaction.commit();
        System.out.println("Buku berhasil ditambahkan: " + buku.getNamaBuku());
    } catch (Exception e) {
        if (transaction != null) {
            transaction.rollback();
        }
        e.printStackTrace();
        System.out.println("Error menambahkan buku: " + e.getMessage());
    } finally {
        session.close();
    }
}
    
}