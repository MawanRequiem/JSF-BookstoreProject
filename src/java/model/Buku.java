package model;
// Generated Sep 29, 2024 3:45:29 PM by Hibernate Tools 4.3.1


import java.util.HashSet;
import java.util.Set;

/**
 * Buku generated by hbm2java
 */
public class Buku  implements java.io.Serializable {


     private Integer idBuku;
     private byte[] gambarBuku;
     private String namaBuku;
     private Double hargaBuku;
     private String genreBuku;
     private String seriesBuku;
     private String deskripsiBuku;
     private Integer stockBuku;
     private Set orderses = new HashSet(0);

    public Buku() {
    }

    public Buku(byte[] gambarBuku, String namaBuku, Double hargaBuku, String genreBuku, String seriesBuku, String deskripsiBuku, Integer stockBuku, Set orderses) {
       this.gambarBuku = gambarBuku;
       this.namaBuku = namaBuku;
       this.hargaBuku = hargaBuku;
       this.genreBuku = genreBuku;
       this.seriesBuku = seriesBuku;
       this.deskripsiBuku = deskripsiBuku;
       this.stockBuku = stockBuku;
       this.orderses = orderses;
    }
   
    public Integer getIdBuku() {
        return this.idBuku;
    }
    
    public void setIdBuku(Integer idBuku) {
        this.idBuku = idBuku;
    }
    public byte[] getGambarBuku() {
        return this.gambarBuku;
    }
    
    public void setGambarBuku(byte[] gambarBuku) {
        this.gambarBuku = gambarBuku;
    }
    public String getNamaBuku() {
        return this.namaBuku;
    }
    
    public void setNamaBuku(String namaBuku) {
        this.namaBuku = namaBuku;
    }
    public Double getHargaBuku() {
        return this.hargaBuku;
    }
    
    public void setHargaBuku(Double hargaBuku) {
        this.hargaBuku = hargaBuku;
    }
    public String getGenreBuku() {
        return this.genreBuku;
    }
    
    public void setGenreBuku(String genreBuku) {
        this.genreBuku = genreBuku;
    }
    public String getSeriesBuku() {
        return this.seriesBuku;
    }
    
    public void setSeriesBuku(String seriesBuku) {
        this.seriesBuku = seriesBuku;
    }
    public String getDeskripsiBuku() {
        return this.deskripsiBuku;
    }
    
    public void setDeskripsiBuku(String deskripsiBuku) {
        this.deskripsiBuku = deskripsiBuku;
    }
    public Integer getStockBuku() {
        return this.stockBuku;
    }
    
    public void setStockBuku(Integer stockBuku) {
        this.stockBuku = stockBuku;
    }
    public Set getOrderses() {
        return this.orderses;
    }
    
    public void setOrderses(Set orderses) {
        this.orderses = orderses;
    }
}