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
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

@ManagedBean(name = "bukuBean")
@ViewScoped
public class BukuBean {

    private List<Buku> bukuList;
    private BukuDAO bukuDAO;
    private Buku selectedBook;
    private int idBuku;  // ID buku yang akan diambil dari URL
    @PostConstruct
    public void init() {
        bukuDAO = new BukuDAO();
        bukuList = bukuDAO.getAllBooks();  // Fetch all books from DAO
    
        
        String idBukuParam = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("idBuku");

        if (idBukuParam != null) {
            try {
                idBuku = Integer.parseInt(idBukuParam);
                loadBuku(idBuku);  // Load book by ID
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        }
    }
    
        // Metode untuk memuat data buku berdasarkan ID
    public void loadBuku(int idBuku) {
        selectedBook = bukuDAO.getBookById(idBuku);
        if (selectedBook != null) {
            System.out.println("Book loaded: " + selectedBook.getNamaBuku());
        } else {
            System.out.println("No book found with idBuku: " + idBuku);
        }
    }

    public Buku getSelectedBook() {
        return selectedBook;
    }

    public void setSelectedBook(Buku selectedBook) {
        this.selectedBook = selectedBook;
    }
    

    public List<Buku> getBukuList() {
        return bukuList;
    }

    public void setBukuList(List<Buku> bukuList) {
        this.bukuList = bukuList;
    }


    // Group books by genre
    public List<Buku> getBooksByGenre(String genre) {
    return bukuList.stream()
        .filter(buku -> buku.getGenreBuku().equalsIgnoreCase(genre))
        .collect(Collectors.toList());
}

    // Group books by series
    public Map<String, List<Buku>> groupBooksBySeries() {
        return bukuList.stream().collect(Collectors.groupingBy(Buku::getSeriesBuku));
    }

    // Sets the selected book and stores it in the session
    // This method stores the selected book in the session and redirects to the edit page
    public String editBuku(int idBuku) {
        // Just pass the ID to the edit page, as the data will be loaded in the page's f:viewAction
        return "editBuku?faces-redirect=true&idBuku=" + idBuku;
    }

    // Method to save or update the selected book
    public String saveOrUpdateBook(int idBuku) {
        Buku bookToUpdate = bukuDAO.getBookById(idBuku);  // Fetch the book by ID
        if (bookToUpdate == null) {
            System.out.println("No book found with ID: " + idBuku);
            return "adminDashboard?faces-redirect=true";  // Redirect to the dashboard if no book is found
        }

        try {
            // Update the book details
            bookToUpdate.setNamaBuku(selectedBook.getNamaBuku());
            bookToUpdate.setHargaBuku(selectedBook.getHargaBuku());
            bookToUpdate.setGenreBuku(selectedBook.getGenreBuku());
            bookToUpdate.setSeriesBuku(selectedBook.getSeriesBuku());
            bookToUpdate.setDeskripsiBuku(selectedBook.getDeskripsiBuku());
            bookToUpdate.setStockBuku(selectedBook.getStockBuku());

            // Save the changes
            bukuDAO.saveOrUpdateBook(bookToUpdate);
            System.out.println("Book updated with ID: " + idBuku);
            return "adminDashboard?faces-redirect=true";  // Redirect to the dashboard after saving
        } catch (Exception e) {
            e.printStackTrace();
            return null;  // Stay on the same page if an error occurs
        }
    }

    // Convert image byte array to Base64 string
    public String convertToBase64(byte[] gambarBuku) {
        if (gambarBuku != null && gambarBuku.length > 0) {
            return Base64.getEncoder().encodeToString(gambarBuku);
        } else {
            return "https://via.placeholder.com/300x200.png?text=No+Image"; // Placeholder image when no image is available
        }
    }

    // In BukuBean class
    public String viewBookDetails(int idBuku) {
        selectedBook = bukuDAO.getBookById(idBuku);  // Fetch book by its ID
        return "bookDetails?faces-redirect=true";    // Redirect to the new details page
    }
public List<Buku> getBooksBySeries(String series) {
    return bukuList.stream()
        .filter(b -> b.getSeriesBuku().equalsIgnoreCase(series))
        .collect(Collectors.toList());
}
}
