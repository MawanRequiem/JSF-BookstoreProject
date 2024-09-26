/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bean;

import com.dao.BukuDAO;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import model.Buku;

/**
 *
 * @author Personal
 */
@ManagedBean
@ViewScoped
public class ViewBookBean {
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
    
        public void loadBuku(int idBuku) {
        selectedBook = bukuDAO.getBookById(idBuku);
        if (selectedBook != null) {
            System.out.println("Book loaded: " + selectedBook.getNamaBuku());
        } else {
            System.out.println("No book found with idBuku: " + idBuku);
        }
    }
    
}