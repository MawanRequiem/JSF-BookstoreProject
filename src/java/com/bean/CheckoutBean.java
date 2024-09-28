package com.bean;

import com.dao.CheckoutDAO;
import com.dao.BukuDAO;
import com.dao.PaymentMethodDAO;
import com.dao.UserAddressDAO;
import java.io.Serializable;
import java.util.Base64;
import model.Buku;
import model.Orders;
import model.UserDb;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import java.util.Date;
import java.util.List;
import javax.faces.bean.ViewScoped;
import model.PaymentMethod;
import model.UserAddress;

@ManagedBean(name = "checkoutBean")
@ViewScoped
public class CheckoutBean implements Serializable {

    // Properti yang sudah ada
    private CheckoutDAO checkoutDAO;
    private BukuDAO bukuDAO;
    private UserAddressDAO userAddressDAO;
    private Buku selectedBook;
    private UserDb currentUser;

    private UserAddress userAddress;
    private int idBukuS;

    // Properti baru untuk metode pembayaran
    private PaymentMethod selectedPaymentMethod;
    private List<PaymentMethod> paymentMethods;
    private PaymentMethodDAO paymentMethodDAO;
      private int selectedPaymentMethodId;  // ID yang dipilih dari dropdown

    @PostConstruct
    public void init() {
        checkoutDAO = new CheckoutDAO();
        bukuDAO = new BukuDAO();
        userAddressDAO = new UserAddressDAO();
        paymentMethodDAO = new PaymentMethodDAO();

        // Ambil current user dari session
        currentUser = (UserDb) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("user");
        String idBukuParam = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("idBuku");
        if (currentUser != null) {
            userAddress = userAddressDAO.getAddressByUserId(currentUser.getIdUser());
            System.out.println("ALAMAT SI USER" + userAddress);
            System.out.println("ID SI USER" + currentUser.getIdUser());
        }

        if (idBukuParam != null) {
            try {
                idBukuS = Integer.parseInt(idBukuParam);
                loadBuku(idBukuS);  // Load book by ID
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        }

        // Ambil semua metode pembayaran
        paymentMethods = checkoutDAO.getAllPaymentMethods();
    }

    public void loadBuku(int idBuku) {
        selectedBook = bukuDAO.getBookById(idBuku);
        if (selectedBook != null) {
            System.out.println("Book loaded: " + selectedBook.getNamaBuku());
        } else {
            System.out.println("No book found with idBuku: " + idBuku);
        }
    }
    
public PaymentMethod getSelectedPaymentMethod() {
    for (PaymentMethod pm : paymentMethods) {
        if (pm.getIdPayment() == selectedPaymentMethodId) {
            return pm;  // Kembalikan objek PaymentMethod yang sesuai dengan ID
        }
    }
    return null;  // Return null jika tidak ditemukan
}

        public int getSelectedPaymentMethodId() {
        return selectedPaymentMethodId;
    }

    public void setSelectedPaymentMethodId(int selectedPaymentMethodId) {
        this.selectedPaymentMethodId = selectedPaymentMethodId;
    }
    
    // Getter for selectedBook
    public Buku getSelectedBook() {
        return selectedBook;
    }

    // Getter for currentUser
    public UserDb getCurrentUser() {
        return currentUser;
    }

    // Getter dan setter untuk paymentMethods dan selectedPaymentMethod
    public List<PaymentMethod> getPaymentMethods() {
        return paymentMethods;
    }

    public void setPaymentMethods(List<PaymentMethod> paymentMethods) {
        this.paymentMethods = paymentMethods;
    }


public void setSelectedPaymentMethod(PaymentMethod selectedPaymentMethod) {
        this.selectedPaymentMethod = selectedPaymentMethod;
        System.out.println("Selected Payment Method Set: " + selectedPaymentMethod.getPayment());
    }

    public UserAddressDAO getUserAddressDAO() {
        return userAddressDAO;
    }

    public void setUserAddressDAO(UserAddressDAO userAddressDAO) {
        this.userAddressDAO = userAddressDAO;
    }

    // Method to handle checkout
public String checkout(int idBuku) {
    // Fetch the selected book from the database using its ID
    selectedBook = bukuDAO.getBookById(idBuku);
    
    // Langsung gunakan field selectedPaymentMethod
    selectedPaymentMethod = getSelectedPaymentMethod();
    
    System.out.println("Checkout method called for book: " + selectedBook.getNamaBuku());
    System.out.println("Selected Payment Method: " + selectedPaymentMethod);

    // Cek apakah buku tersedia
    if (selectedBook == null) {
        FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Book not found."));
        return null;
    }

    // Cek apakah user sudah login
    if (currentUser == null) {
        FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "You must be logged in to checkout."));
        return "login.xhtml?faces-redirect=true";  // Redirect to login page
    }

    // Cek apakah stok buku tersedia
    if (selectedBook.getStockBuku() <= 0) {
        FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Insufficient stock."));
        return null;
    }

    // Cek apakah metode pembayaran dipilih
    if (selectedPaymentMethod == null) {
        FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Please select a payment method."));
        return null;
    }

    // Auto-generate kode pembayaran
    String paymentCode = generatePaymentCode();
    System.out.println(paymentCode);

    // Buat order baru dan simpan ke database
    Orders order = new Orders();
    order.setBuku(selectedBook);
    order.setPaymentMethod(selectedPaymentMethod);  // Set metode pembayaran
    order.setUserDb(currentUser);
    order.setPaymentCode(paymentCode);  // Set kode pembayaran
    order.setDate(new Date());  // Tanggal pesanan

    // Simpan pesanan ke database
    boolean isSuccess = checkoutDAO.saveOrder(order);

    if (isSuccess) {
        // Kurangi stok buku setelah pesanan berhasil
        selectedBook.setStockBuku(selectedBook.getStockBuku() - 1);
        bukuDAO.updateBookStock(selectedBook);  // Update stok buku di database

        // Redirect ke halaman sukses
        return "index.xhtml?faces-redirect=true";
    } else {
        // Gagal menyimpan pesanan, tampilkan pesan error
        FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Checkout failed."));
        return null;
    }
}



    // Fungsi untuk menghasilkan kode pembayaran otomatis
    private String generatePaymentCode(UserAddress userAddress) {
        String base = userAddress.getPostalCode();
        int randomDigits = (int) (Math.random() * 10000);
        return base + String.format("%04d", randomDigits);
    }

    // Optional: Helper method to convert image to Base64 (if required)
    public String convertToBase64(byte[] image) {
        return Base64.getEncoder().encodeToString(image);
    }

    public String forwardToCheckout(int idBuku) {
        selectedBook = bukuDAO.getBookById(idBuku);

        // Error checking: Ensure selectedBook exists
        if (selectedBook == null) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Book not found."));
            return null;
        }

        return "checkout.xhtml?faces-redirect=true";  // Redirect to checkout page
    }

    private String generatePaymentCode() {
        int randomDigits = (int) (Math.random() * 1_000_000_0000L); // 10 digit random number
        return String.format("%010d", randomDigits);
    }

    public UserAddress getUserAddress() {
        return userAddress;
    }

    public void setUserAddress(UserAddress userAddress) {
        this.userAddress = userAddress;
    }
}
