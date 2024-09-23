package com.bean;

import com.dao.BukuDAO;
import com.dao.CartDAO;
import model.Keranjang;
import model.Buku;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import java.io.Serializable;
import java.util.List;

@ManagedBean
@SessionScoped
public class CartBean implements Serializable {

    private List<Keranjang> cartItems;
    private int totalPrice;
    private boolean selectAll;

    @PostConstruct
    public void init() {
        Integer currentUserId = (Integer) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("userId");
        if (currentUserId != null) {
            cartItems = CartDAO.getCartItemsForUser(currentUserId);
            calculateTotalPrice();
        }
    }

    public List<Keranjang> getCartItems() {
        return cartItems;
    }

    public int getTotalPrice() {
        return totalPrice;
    }

    public boolean isSelectAll() {
        return selectAll;
    }

    public void setSelectAll(boolean selectAll) {
        this.selectAll = selectAll;
    }

    public void addToCart(int idBuku, int quantity) {
        Integer currentUserId = (Integer) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("userId");
        if (currentUserId != null) {
            Buku buku = BukuDAO.getBukuById(idBuku); // Retrieve book by id
            CartDAO.addBookToCart(buku, currentUserId, quantity);
            cartItems = CartDAO.getCartItemsForUser(currentUserId); // Refresh cart
            calculateTotalPrice();
        }
    }

    public void removeItem(Keranjang item) {
        CartDAO.removeFromCart(item);
        cartItems.remove(item);
        calculateTotalPrice();
    }

    public void changeQuantity(Keranjang item, int newQuantity) {
        if (newQuantity > 0) {
            CartDAO.updateQuantity(item, newQuantity);
            item.setTotalBeli(newQuantity);
            calculateTotalPrice();
        }
    }

    public void calculateTotalPrice() {
        totalPrice = (int) cartItems.stream()
                .mapToDouble(item -> item.getTotalBeli() * item.getBuku().getHargaBuku()) // Menggunakan mapToDouble
                .sum();  // Hasil sum() dalam double, lalu cast ke int
    }

    public void checkout() {
        // Proses checkout (bisa dikembangkan sesuai kebutuhan)
    }
}
