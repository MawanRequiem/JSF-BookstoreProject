package com.bean;

import com.dao.BukuDAO;
import java.io.IOException;
import model.Buku;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import java.util.List;
import java.util.Base64;
import java.util.Map;
import java.util.stream.Collectors;
import javax.faces.context.FacesContext;

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
    
    // Group books by genre
    public Map<String, List<Buku>> groupBooksByGenre() {
        return bukuList.stream().collect(Collectors.groupingBy(Buku::getGenreBuku));
    }

    // Group books by series
    public Map<String, List<Buku>> groupBooksBySeries() {
        return bukuList.stream().collect(Collectors.groupingBy(Buku::getSeriesBuku));
    }

        // Sets the selected book and stores it in the session
    public String editBuku(Buku buku) {
        // Set the selected book
        this.selectedBook = buku;
        
        // Store the selected book in the session
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("selectedBook", selectedBook);

        // Redirect to editBuku.xhtml
        return "editBuku?faces-redirect=true";
    }

    // Retrieves the selected book from the session when loading the edit page
    public void loadSelectedBook() throws IOException {
        selectedBook = (Buku) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("selectedBook");
    if (selectedBook == null) {
        // Redirect to another page or show an error
        System.out.println("Selected book is null, redirecting back or showing error.");
        FacesContext.getCurrentInstance().getExternalContext().redirect("adminDashboard.xhtml");
    } else {
        System.out.println("Selected book loaded: " + selectedBook.getIdBuku());
    }
    }
    
    public String saveOrUpdateBook() {
        if (selectedBook == null) {
        System.out.println("No book selected for update.");
        return null;  // Stay on the same page if no book is selected
    }
    
    try {
        System.out.println("Updating book: " + selectedBook.getIdBuku());
        bukuDAO.saveOrUpdateBook(selectedBook);  // Simpan atau update buku
        return "adminDashboard?faces-redirect=true";  // Redirect to admin dashboard
    } catch (Exception e) {
        e.printStackTrace();
        return null;  // Tetap di halaman yang sama jika terjadi error
    }
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
