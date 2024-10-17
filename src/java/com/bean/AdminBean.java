/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bean;

import com.dao.AdminDAO;
import java.io.IOException;
import java.io.Serializable;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.bean.SessionScoped;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.Part;
import model.Buku;

@ManagedBean
@ViewScoped
public class AdminBean implements Serializable {

    private AdminDAO adminDAO = new AdminDAO();
    private List<Buku> bukuList;
    private Buku selectedBuku;
    private Buku newBuku = new Buku(); // Instance baru untuk penambahan buku
    private byte[] oldImage; // To store the old image
    private Part uploadedFile; // For file upload
    private Integer id;

    @PostConstruct
    public void init() {
        if (id != null) {
            selectedBuku = adminDAO.getBukuById(id);
            if (selectedBuku != null) {
                oldImage = selectedBuku.getGambarBuku();
            } else {
                // Handle case when no book is found with the given id
                FacesContext.getCurrentInstance().addMessage(null,
                        new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Buku tidak ditemukan."));
            }
        }
    }

    // Getter and Setter methods
    public Part getUploadedFile() {
        return uploadedFile;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setUploadedFile(Part uploadedFile) {
        this.uploadedFile = uploadedFile;
    }

    public Buku getNewBuku() {
        return newBuku;
    }

    public void setNewBuku(Buku newBuku) {
        this.newBuku = newBuku;
    }

    public List<Buku> getBukuList() {
        // Refresh bukuList every time the method is called
        bukuList = adminDAO.getAllBuku();
        return bukuList;
    }

    public Buku getSelectedBuku() {
        return selectedBuku;
    }

    public void setSelectedBuku(Buku selectedBuku) {
        this.selectedBuku = selectedBuku;
        this.oldImage = selectedBuku.getGambarBuku(); // Store the old image for later use
    }

    // Navigation to the dashboard after updating
    public String navigateBackToDashboard() {
        selectedBuku = null;  // Reset selectedBuku sebelum kembali
        return "adminDashboard.xhtml?faces-redirect=true";  // Kembali ke dashboard dengan redirect
    }

    // Edit Buku logic
    public String editBuku(Buku buku) {
        this.selectedBuku = buku;
        System.out.println("Selected Buku: " + this.selectedBuku.getNamaBuku());  // Debug log
        this.oldImage = buku.getGambarBuku();
        return "editBuku.xhtml?faces-redirect=true&id=" + buku.getIdBuku();

    }

    // Update Buku logic
    public void updateBuku() throws IOException {
        if (uploadedFile != null && uploadedFile.getSize() > 0) {
            selectedBuku.setGambarBuku(convertPartToBytes(uploadedFile));
            System.out.println("New image uploaded.");
        } else {
            selectedBuku.setGambarBuku(oldImage); // Use the old image if no new image is uploaded
            System.out.println("No new image, using old image.");
        }

        adminDAO.updateBuku(selectedBuku);
        FacesContext.getCurrentInstance().getExternalContext().redirect("adminDashboard.xhtml");
    }

    // Delete Buku logic
    public void deleteBuku() {
        if (selectedBuku != null) {
            System.out.println("Buku yang dihapus: " + selectedBuku.getNamaBuku());
            adminDAO.deleteBuku(selectedBuku);
            bukuList = adminDAO.getAllBuku(); // Refresh list after deletion
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Buku Deleted", "The book has been successfully deleted."));
        } else {
            System.out.println("Tidak ada buku yang dipilih untuk dihapus.");
        }
    }

    // Add Buku logic
    public String addBuku() throws IOException {
        if (uploadedFile != null && uploadedFile.getSize() > 0) {
            newBuku.setGambarBuku(convertPartToBytes(uploadedFile));
        }

        adminDAO.addBuku(newBuku);
        // Reset newBuku to clear the form
        newBuku = new Buku();
        return "adminDashboard.xhtml?faces-redirect=true"; // Redirect ke dashboard
    }

    public void resetSelectedBuku() {
        this.selectedBuku = null; // Reset selectedBuku
    }

    public void setSelectedBukuById(int idBuku) {
        for (Buku buku : bukuList) {
            if (buku.getIdBuku() == idBuku) {
                this.selectedBuku = buku;
                break;
            }
        }
    }

    private byte[] convertPartToBytes(Part file) throws IOException {
        byte[] bytes = new byte[(int) file.getSize()];
        file.getInputStream().read(bytes);
        return bytes;
    }

    public String convertToBase64(byte[] image) {
        return java.util.Base64.getEncoder().encodeToString(image);
    }
}
