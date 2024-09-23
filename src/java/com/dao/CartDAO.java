package com.dao;

import model.Buku;
import model.Keranjang;
import model.UserDb;
import org.hibernate.Session;
import org.hibernate.Transaction;
import util.HibernateUtil;

import java.util.List;

public class CartDAO {

    /**
     * Menambahkan buku ke keranjang untuk pengguna tertentu.
     * 
     * @param buku     Buku yang ditambahkan
     * @param userId   ID pengguna
     * @param quantity Jumlah buku yang ditambahkan ke keranjang
     */
    public static void addBookToCart(Buku buku, int userId, int quantity) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = null;

        try {
            tx = session.beginTransaction();

            // Membuat objek Keranjang baru untuk disimpan
            Keranjang keranjang = new Keranjang();
            keranjang.setBuku(buku);
            keranjang.setUserDb(new UserDb(userId)); // Set hanya ID user
            keranjang.setTotalBeli(quantity);

            // Menyimpan keranjang baru
            session.save(keranjang);
            tx.commit();
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            e.printStackTrace();
        } finally {
            session.close();
        }
    }

    /**
     * Mengambil semua item keranjang untuk user tertentu.
     * 
     * @param userId ID user yang digunakan untuk mengambil item keranjang
     * @return List item keranjang untuk user tertentu
     */
    public static List<Keranjang> getCartItemsForUser(int userId) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        List<Keranjang> cartItems = null;

        try {
            // Query berdasarkan user ID
            cartItems = session.createQuery("FROM Keranjang WHERE userDb.id = :userId")
                               .setParameter("userId", userId)
                               .list();
        } finally {
            session.close();
        }

        return cartItems;
    }

    /**
     * Menghapus item dari keranjang.
     * 
     * @param item Item Keranjang yang akan dihapus
     */
    public static void removeFromCart(Keranjang item) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = null;

        try {
            tx = session.beginTransaction();
            session.delete(item);  // Menghapus item dari keranjang
            tx.commit();
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            e.printStackTrace();
        } finally {
            session.close();
        }
    }

    /**
     * Mengupdate jumlah (quantity) buku dalam keranjang.
     * 
     * @param item     Item keranjang yang akan diupdate
     * @param quantity Jumlah baru untuk item tersebut
     */
    public static void updateQuantity(Keranjang item, int quantity) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = null;

        try {
            tx = session.beginTransaction();
            item.setTotalBeli(quantity);  // Mengupdate total pembelian
            session.update(item);  // Menyimpan perubahan ke database
            tx.commit();
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            e.printStackTrace();
        } finally {
            session.close();
        }
    }
}
