package org.example;

public class Main {
    public static void main(String[] args) {
        System.out.println("SmartBank System Başlatılıyor...");

        try {
            // Veritabanı bağlantısını test et
            System.out.println("Veritabanı bağlantısı testi yapılıyor...");
            if (DatabaseConnection.getConnection() != null) {
                System.out.println("Veritabanı bağlantısı başarılı!");
            }

            // Kullanıcı Servisi
            UserService userService = new UserService();

            // Kullanıcı kaydı (varsa hata verir)
            try {
                System.out.println("Yeni kullanıcı kaydı ekleniyor...");
                userService.registerUser("Ahmet Yılmaz", "12345678901", "password123");
                System.out.println("Kullanıcı başarıyla kaydedildi.");
            } catch (Exception e) {
                System.err.println("Kullanıcı zaten mevcut: " + e.getMessage());
            }

            // Kullanıcı giriş işlemi
            System.out.println("Kullanıcı giriş yapıyor...");
            boolean isLoggedIn = userService.loginUser("12345678901", "password123");
            if (isLoggedIn) {
                System.out.println("Giriş başarılı!");
            } else {
                System.out.println("Giriş başarısız! TCKN veya şifre hatalı.");
            }

            // Hesap Servisi
            AccountService accountService = new AccountService();

            // Hesap oluşturma
            System.out.println("Yeni hesap oluşturuluyor...");
            accountService.createAccount("Ahmet Yılmaz", 5000.00);
            System.out.println("Hesap başarıyla oluşturuldu.");

            // Hesap bakiyesi kontrolü
            System.out.println("Hesap bakiyesi kontrol ediliyor...");
            double balance = accountService.getAccountBalance(1); // 1. hesap ID'si
            System.out.println("Hesap Bakiyesi: " + balance);

            // Para yatırma işlemi
            System.out.println("Hesaba para yatırılıyor...");
            accountService.depositMoney(1, 1000.00);
            System.out.println("Yeni Hesap Bakiyesi: " + accountService.getAccountBalance(1));

            // Para çekme işlemi
            System.out.println("Hesaptan para çekiliyor...");
            boolean isWithdrawn = accountService.withdrawMoney(1, 500.00);
            if (isWithdrawn) {
                System.out.println("Para çekme işlemi başarılı.");
                System.out.println("Güncellenen Hesap Bakiyesi: " + accountService.getAccountBalance(1));
            } else {
                System.out.println("Yetersiz bakiye. Para çekme işlemi başarısız.");
            }

            // İşlem Servisi
            TransactionService transactionService = new TransactionService();

            // Para transferi işlemi
            System.out.println("Para transferi yapılıyor...");
            boolean isTransferred = transactionService.transferMoney(1, 2, 1000.00); // 1'den 2'ye transfer
            if (isTransferred) {
                System.out.println("Para transferi başarılı.");
            } else {
                System.out.println("Para transferi başarısız.");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        System.out.println("SmartBank System kapatılıyor...");
    }
}
