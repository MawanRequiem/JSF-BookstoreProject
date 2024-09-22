package com.bean;

import com.dao.BukuDAO;
import model.Buku;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import java.util.List;
import java.util.Base64;

@ManagedBean(name = "bukuBean")
@RequestScoped
public class BukuBean {

    private List<Buku> bukuList;
    private BukuDAO bukuDAO;
    private Buku selectedBook;  // Add this to store the selected book details

    @PostConstruct
    public void init() {
        bukuDAO = new BukuDAO();
        bukuList = bukuDAO.getAllBooks();  // Fetch all books from DAO
    }

    public List<Buku> getBukuList() {
        return bukuList;
    }

    public void setBukuList(List<Buku> bukuList) {
        this.bukuList = bukuList;
    }

    public Buku getSelectedBook() {
        return selectedBook;
    }

    public void setSelectedBook(Buku selectedBook) {
        this.selectedBook = selectedBook;
    }

    // This method will be called when user clicks on "View Details" and it sets the selected book
    public void loadBuku(int idBuku) {
        selectedBook = bukuDAO.getBookById(idBuku);  // Fetch book by its ID
    }

    // Convert image byte array to Base64 string
public String convertToBase64(byte[] gambarBuku) {
    if (gambarBuku != null && gambarBuku.length > 0) {
        String base64Image = Base64.getEncoder().encodeToString(gambarBuku);
        System.out.println("Base64 Image: " + base64Image);  // Debugging
        return base64Image;
    } else {
        System.out.println("No image found, returning placeholder.");
        return "image/1.png";
    }
}

}
