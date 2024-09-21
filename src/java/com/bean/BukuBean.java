package com.bean;

import com.dao.BukuDAO;
import model.Buku;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import java.util.List;
import java.util.Base64;


import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;

@ManagedBean(name = "bukuBean")
@RequestScoped
public class BukuBean {

    private List<Buku> bukuList;
    private BukuDAO bukuDAO;

    @PostConstruct
    public void init() {
        bukuDAO = new BukuDAO();
        bukuList = bukuDAO.getAllBooks(); // Ambil semua data buku dari DAO
    }

    public List<Buku> getBukuList() {
        return bukuList;
    }

    public void setBukuList(List<Buku> bukuList) {
        this.bukuList = bukuList;
        
    }
    
    public String convertToBase64(byte[] gambarBuku) {
    return Base64.getEncoder().encodeToString(gambarBuku);
}
}
