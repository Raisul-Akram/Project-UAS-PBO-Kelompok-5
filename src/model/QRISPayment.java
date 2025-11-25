package model;

import java.util.Scanner;
import java.text.SimpleDateFormat;
import java.util.Date;

public class QRISPayment implements Payment {

    @Override
    public boolean processPayment(double amount) {
        Scanner scanner = new Scanner(System.in);
        
        // --- TAMBAHAN BIAYA ADMIN 0.7% (MDR Standar) ---
        double adminFee = amount * 0.007;
        double totalBayar = amount + adminFee;
        // ------------------------------------------------

        System.out.println("\n--- Pembayaran QRIS ---");
        
        // 1. Tampilkan Barcode QRIS (Hanya Hiasan Visual/ASCII Art)
        System.out.println("       SCAN QRIS      ");
        System.out.println("▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄");
        System.out.println("█ ▄▄▄▄▄ █ ▄ █ ▄▄▄▄▄ █"); // Bagian Pola QR
        System.out.println("█ █   █ █  ▀█ █   █ █");
        System.out.println("█ █▄▄▄█ █ ▀ █ █▄▄▄█ █");
        System.out.println("█▄▄▄▄▄▄▄█▄█▄█▄▄▄▄▄▄▄█");
        System.out.println("█ ▀▀ ▄▄▄▀▄▀▀▀   ▄ ▄ █");
        System.out.println("█▄█▀▄▄▄█ █▀▀█▀▀▀█ ▀ █");
        System.out.println("█ ▄▄▄▄▄ █▄▀ ▀ ▀▀█ ▀▄█");
        System.out.println("█ █   █ █ █▀█ ▄ █ █ █");
        System.out.println("█ █▄▄▄█ █ ▀▄▀▄▀ █▀▄ █");
        System.out.println("█▄▄▄▄▄▄▄█▄▄▄█▄▄▄█▄▄▄█");
        System.out.println("▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀");
        System.out.println("   NAMA MERCHANT:     ");
        System.out.println("   BIOSKOP KEL. 5     ");
        System.out.println("======================");
        
        // Update Tampilan Tagihan dengan Admin Fee
        System.out.println("Harga Tiket   : Rp " + (long)amount);
        System.out.println("Biaya Layanan : Rp " + (long)adminFee + " (0.7%)");
        System.out.println("TOTAL BAYAR   : Rp " + (long)totalBayar);
        
        System.out.println("Masukkan nominal transfer sesuai TOTAL BAYAR)");
        System.out.println("(Ketik '0' jika ingin membatalkan)");

        // 2. Loop Input Transfer (Simulasi transfer via HP)
        while (true) {
            System.out.print("Masukkan nominal transfer: Rp ");
            try {
                double transfer = Double.parseDouble(scanner.nextLine());

                // 3. Fitur Batal
                if (transfer == 0) {
                    System.out.println("Pembayaran QRIS dibatalkan user.");
                    return false; 
                }

                // 4. Validasi Nominal Transfer (Cek terhadap TOTAL BAYAR)
                if (transfer >= totalBayar) {
                    System.out.println("Verifikasi pembayaran... SUKSES!");
                    
                    // 5. Cetak Struk Akhir
                    printReceipt("QRIS (NMD)", amount, adminFee, totalBayar, transfer);
                    return true;
                } else {
                    System.out.println("Gagal! Nominal transfer kurang dari Rp " + (long)totalBayar);
                    System.out.print("Coba lagi? (y/n): ");
                    if (!scanner.nextLine().equalsIgnoreCase("y")) return false;
                }
            } catch (NumberFormatException e) {
                System.out.println("Input salah.");
            }
        }
    }

    @Override
    public String getPaymentType() {
        return "QRIS";
    }
    
    // Helper cetak struk QRIS
    private void printReceipt(String method, double ticketPrice, double adminFee, double total, double paid) {
        String date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        System.out.println("\n=========================================");
        System.out.println("          BUKTI PEMBAYARAN LUNAS        ");
        System.out.println("===========================================");
        System.out.printf("%-15s: %s\n", "Tanggal", date);
        System.out.printf("%-15s: %s\n", "Metode", method);
        System.out.printf("%-15s: %s\n", "Status", "SUCCESS");
        System.out.println("------------------------------------------");
        System.out.printf("%-15s: Rp %d\n", "Harga Tiket", (long)ticketPrice);
        System.out.printf("%-15s: Rp %d\n", "Biaya Admin", (long)adminFee);
        System.out.println("-------------------------------------------");
        System.out.printf("%-15s: Rp %d\n", "TOTAL TAGIHAN", (long)total);
        System.out.printf("%-15s: Rp %d\n", "Total Bayar", (long)paid);
        System.out.println("===========================================");
        System.out.println("        Terima Kasih atas Kunjungan Anda");
        
        // 6. Barcode Tiket (Sesuai requestmu)
        System.out.println("\n           TIKET VALIDATED");
        System.out.println("║█║▌║█║▌│║▌█║▌║│█║▌║█║▌║║█║▌║█║▌│║▌█║▌║│█║▌");
        System.out.println("║█║▌║█║▌│║▌█║▌║│█║▌║█║▌║║█║▌║█║▌│║▌█║▌║│█║▌");
        System.out.println("===========================================\n");
    }
}
