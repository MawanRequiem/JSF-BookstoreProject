package com.dao;

import model.Buku;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;

import java.util.List;

public class BukuDAO {

    private SessionFactory sessionFactory;

    // Constructor: Inisialisasi Hibernate SessionFactory dengan konfigurasi Hibernate 4.3
    public BukuDAO() {
        Configuration configuration = new Configuration().configure(); // Membaca konfigurasi dari hibernate.cfg.xml
        ServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder()
                                            .applySettings(configuration.getProperties())
                                            .build();
        sessionFactory = configuration.buildSessionFactory(serviceRegistry); // Membuat sessionFactory menggunakan ServiceRegistry
    }

    // Metode untuk mengambil semua buku dari database
    @SuppressWarnings("unchecked")  // Untuk menekan warning unchecked cast
    public List<Buku> getAllBooks() {
        Session session = sessionFactory.openSession();
        List<Buku> books = null;
        try {
            // HQL query, tetapi tidak dapat langsung meng-cast ke Buku.class
            books = (List<Buku>) session.createQuery("from Buku").list(); // Casting secara manual
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            session.close();
        }
        return books;
    }

    // Metode untuk mengambil buku berdasarkan ID
    public Buku getBookById(int idBuku) {
        Session session = sessionFactory.openSession();
        Buku book = null;
        try {
            // Mengambil buku berdasarkan ID, dengan casting secara manual ke Buku
            book = (Buku) session.get(Buku.class, idBuku); // Cast dari Object ke Buku
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            session.close();
        }
        return book;
    }

    // Menyimpan atau memperbarui data buku
    public void saveOrUpdateBook(Buku buku) {
        Session session = sessionFactory.openSession();
    Transaction tx = null;
    try {
        tx = session.beginTransaction();
        session.saveOrUpdate(buku);  // Simpan atau update buku
        System.out.println("Saving or updating book: " + buku.getIdBuku()); // Log ID buku
        tx.commit();
    } catch (Exception e) {
        if (tx != null) {
            tx.rollback();
        }
        e.printStackTrace();
        System.out.println("Error during saveOrUpdate: " + e.getMessage()); // Log error
    } finally {
        session.close();
    }
    }

    // Menghapus buku berdasarkan ID
    public void deleteBook(int idBuku) {
        Session session = sessionFactory.openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            Buku book = (Buku) session.get(Buku.class, idBuku); // Mengambil buku dengan casting manual
            if (book != null) {
                session.delete(book);  // Menghapus buku jika ada
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
