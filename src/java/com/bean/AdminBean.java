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
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.bean.SessionScoped;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.Part;
import model.Buku;

/**
 *
 * @author Raka
 */

@ManagedBean
@SessionScoped
public class AdminBean implements Serializable {
    private AdminDAO adminDAO = new AdminDAO();
    private List<Buku> bukuList;
    private Buku selectedBuku;
        private Buku newBuku = new Buku(); // Instance baru untuk penambahan buku
    private byte[] oldImage; // To store the old image
    private Part uploadedFile; // For file upload

    public Part getUploadedFile() {
        return uploadedFile;
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
        if (bukuList == null) {
            bukuList = adminDAO.getAllBuku();
        }
        return bukuList;
    }

    public Buku getSelectedBuku() {
        return selectedBuku;
    }

    public void setSelectedBuku(Buku selectedBuku) {
        this.selectedBuku = selectedBuku;
        this.oldImage = selectedBuku.getGambarBuku(); // Store the old image for later use
    }
    
    public String navigateBackToDashboard() {
    selectedBuku = null;  // Reset selectedBuku sebelum kembali
    return "adminDashboard.xhtml?faces-redirect=true";  // Kembali ke dashboard dengan redirect
}

    public String editBuku(Buku buku) {
        this.selectedBuku = buku;
        this.oldImage = buku.getGambarBuku(); // Store the old image when editing
        return "editBuku.xhtml?faces-redirect=true";
    }

    public void updateBuku() throws IOException {
        // Convert uploaded file to byte[] if a new file is uploaded
        if (uploadedFile != null && uploadedFile.getSize() > 0) {
            selectedBuku.setGambarBuku(convertPartToBytes(uploadedFile));
        } else {
            selectedBuku.setGambarBuku(oldImage); // Use the old image if no new image is uploaded
        }

        adminDAO.updateBuku(selectedBuku);
        FacesContext.getCurrentInstance().getExternalContext().redirect("adminDashboard.xhtml");
    }

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