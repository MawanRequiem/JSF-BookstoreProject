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
    private Buku selectedBook;

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

    // This method will be called when user clicks on "View Details"
    public void loadBuku(int idBuku) {
        selectedBook = bukuDAO.getBookById(idBuku);  // Fetch book by its ID
    }

    // Convert image byte array to Base64 string
    public String convertToBase64(byte[] gambarBuku) {
        if (gambarBuku != null && gambarBuku.length > 0) {
            return Base64.getEncoder().encodeToString(gambarBuku);
        } else {
            return "image/placeholder.png"; // Placeholder image when no image is available
        }
    }
}
