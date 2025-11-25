package cli;

import model.Booking;
import model.Movie;
import model.Payment;
import model.CashPayment;
import model.QRISPayment;
import model.Showtime;
import model.VirtualAccountPayment;
import service.BookingService;
import service.MovieService;
import service.PaymentService;
import util.InvalidInputException;
import util.SeatUnavailableException;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Menu CLI untuk User (Versi Ultimate - MOVIE TIX).
 * @author Kelompok 5
 */
public class UserMenu {
    private String userId;
    private MovieService movieService;
    private BookingService bookingService;
    private PaymentService paymentService;
    private Scanner scanner;

    // ANSI Colors (Tema: CYAN & WHITE)
    private final String RESET = "\u001B[0m";
    private final String CYAN = "\u001B[36m";
    private final String GREEN = "\u001B[32m";
    private final String RED = "\u001B[31m";
    private final String YELLOW = "\u001B[33m";
    private final String BOLD = "\u001B[1m";

    public UserMenu(String userId) {
        this.userId = userId;
        this.movieService = new MovieService();
        this.bookingService = new BookingService();
        this.paymentService = new PaymentService();
        this.scanner = new Scanner(System.in);
    }

    public void display() {
        while (true) {
            printHeader();
            
            System.out.println(CYAN + "   [ MAIN MENU ]" + RESET);
            System.out.println("   +---------------------------------------+");
            System.out.println("   | 1. Lihat Daftar Film                  |");
            System.out.println("   | 2. Cari Film                          |");
            System.out.println("   | 3. Pesan Tiket                        |");
            System.out.println("   | 4. Riwayat Pemesanan                  |");
            System.out.println("   | 5. Logout                             |");
            System.out.println("   +---------------------------------------+");
            System.out.print("   Pilih Menu > ");

            boolean validInput = false;
            while (!validInput) {
                try {
                    String input = scanner.nextLine();
                    if (input.isEmpty()) continue;
                    int choice = Integer.parseInt(input);

                    switch (choice) {
                        case 1 -> { viewMovies(); validInput = true; }
                        case 2 -> { searchMovies(); validInput = true; }
                        case 3 -> { bookTicket(); validInput = true; }
                        case 4 -> { viewHistory(); validInput = true; }
                        case 5 -> { return; }
                        default -> throw new InvalidInputException("Pilihan tidak valid.");
                    }
                } catch (NumberFormatException | InvalidInputException e) {
                    System.out.println(RED + "   Error: " + e.getMessage() + RESET);
                    System.out.print("   Pilih Menu > ");
                }
            }
            
            System.out.println("\n   Tekan Enter untuk kembali...");
            scanner.nextLine();
        }
    }

    // ================= HELPER UI =================

    private void printHeader() {
        System.out.print("\033[H\033[2J"); // Clear Screen
        System.out.flush();

        System.out.println(CYAN + "=========================================");
        // ASCII ART: MOVIE TIX
        System.out.println(" █▀▄▀█ █▀█ █░█ █ █▀▀   ▀█▀ █ ▀▄▀");
        System.out.println(" █░▀░█ █▄█ ▀▄▀ █ ██▄   ░█░ █ █░█");
        System.out.println("          SELF SERVICE SYSTEM");
        System.out.println("=========================================" + RESET);
        System.out.println("   Selamat datang, " + BOLD + userId + RESET);
        System.out.println(); 
    }

    // ================= FITUR FILM =================

    private void viewMovies() {
        List<Movie> movies = movieService.sortMoviesByPopularity();
        if (movies.isEmpty()) {
            System.out.println(RED + "   Belum ada data film saat ini." + RESET);
            return;
        }
        
        System.out.println("\n" + CYAN + "   === DAFTAR FILM SEDANG TAYANG ===" + RESET);
        System.out.printf("   %-6s | %-25s | %-10s | %-8s%n", "ID", "JUDUL", "GENRE", "DURASI");
        System.out.println("   ---------------------------------------------------------");
        
        for (Movie m : movies) {
            System.out.printf("   %-6s | %-25s | %-10s | %d mnt%n", 
                m.getId(), 
                (m.getTitle().length() > 25 ? m.getTitle().substring(0,22)+"..." : m.getTitle()), 
                m.getGenre(), 
                m.getDuration());
        }
        System.out.println("   ---------------------------------------------------------");
    }

    private void searchMovies() {
        System.out.println("\n" + CYAN + "   --- Cari Film ---" + RESET);
        System.out.print("   Masukkan judul film: ");
        String title = scanner.nextLine();
        
        List<Movie> results = movieService.searchMovies(title);
        if (results.isEmpty()) {
            System.out.println(RED + "   Film tidak ditemukan." + RESET);
            return;
        }
        System.out.println("\n   === Hasil Pencarian ===");
        for (Movie m : results) {
             System.out.printf("   Found: [%s] %s (%s)\n", m.getId(), m.getTitle(), m.getGenre());
        }
    }

    // ================= PROSES PEMESANAN (CORE) =================

    private void bookTicket() {
        System.out.println("\n" + CYAN + "   === PESAN TIKET ===" + RESET);
        System.out.println(YELLOW + "   (Ketik '0' untuk batal)" + RESET);

        // 1. Input ID Film
        System.out.print("   Masukkan ID Film: ");
        String movieId = scanner.nextLine();
        if (movieId.equals("0")) return; 

        // 2. Input ID Showtime
        System.out.print("   Masukkan ID Showtime (Cth: S001): ");
        String showtimeId = scanner.nextLine();
        if (showtimeId.equals("0")) return;

        Showtime showtime = movieService.findShowtimeById(showtimeId);
        if (showtime == null) {
            System.out.println(RED + "   ✘ Showtime tidak ditemukan." + RESET);
            return;
        }

        // 3. Tampilkan Peta Kursi
        displaySeatMap(showtimeId);

        // 4. Validasi Input Kursi
        List<String> validSeats = new ArrayList<>();
        while (true) {
            System.out.print("   Pilih Kursi (Pisah ';', Cth: A1;A2): ");
            String input = scanner.nextLine();
            
            if (input.equals("0")) return;
            if (input.trim().isEmpty()) {
                System.out.println(RED + "   Mohon pilih minimal satu kursi." + RESET);
                continue;
            }

            String[] seatsArr = input.split(";");
            boolean allValid = true;
            validSeats.clear();

            for (String s : seatsArr) {
                String seat = s.trim().toUpperCase();
                if (!seat.matches("[A-E][1-6]")) {
                    System.out.println(RED + "   Kursi '" + seat + "' TIDAK VALID! (Hanya A1-E6)" + RESET);
                    allValid = false;
                    break;
                }
                validSeats.add(seat);
            }

            if (allValid) break; 
        }

        // 5. Cek Ketersediaan
        if (!bookingService.isSeatsAvailable(showtimeId, validSeats)) {
            System.out.println(RED + "\n   [GAGAL] Kursi pilihan Anda SUDAH TERISI." + RESET);
            System.out.println("   Silakan pilih kursi HIJAU [Nomor] di peta.");
            return; 
        }

        // 6. Konfirmasi & Bayar
        double pricePerSeat = showtime.getPrice();
        double price = pricePerSeat * validSeats.size();
        
        System.out.println("\n   ---------------------------------");
        System.out.println("   Kursi       : " + String.join(", ", validSeats));
        System.out.println(CYAN + "   TOTAL BAYAR : Rp " + (long)price + RESET);
        System.out.println("   ---------------------------------");

        boolean paymentSuccess = false;
        while (!paymentSuccess) {
            System.out.println("\n   Metode Pembayaran:");
            System.out.println("   1. Tunai");
            System.out.println("   2. QRIS");
            System.out.println("   3. Virtual Account");
            System.out.println("   4. Batal");
            System.out.print("   Pilih > ");

            int payChoice = -1;
            try {
                String input = scanner.nextLine();
                payChoice = Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.println("   Input angka saja.");
                continue; 
            }

            if (payChoice == 4) {
                System.out.println(YELLOW + "   Transaksi dibatalkan." + RESET);
                return; 
            }

            if (payChoice < 1 || payChoice > 4) {
                System.out.println(RED + "   Pilihan tidak valid." + RESET);
                continue;
            }

            Payment payment = getPaymentMethod(payChoice);
            
            if (paymentService.process(payment, price)) {
                paymentSuccess = true;
                
                String bookingCode = "BK" + System.currentTimeMillis();
                Booking booking = new Booking(bookingCode, userId, showtimeId, validSeats, price);
                try {
                    bookingService.createBooking(booking);
                    System.out.println(GREEN + "\n   ✔ TRANSAKSI SUKSES! Kode: " + bookingCode + RESET);
                    System.out.println("   ========================================");
                } catch (SeatUnavailableException e) {
                    System.out.println(RED + "   Error Sistem: " + e.getMessage() + RESET);
                }
            } else {
                System.out.println(YELLOW + "\n   Pembayaran Belum Selesai." + RESET);
                System.out.print("   Coba lagi? (y/n): ");
                if (!scanner.nextLine().equalsIgnoreCase("y")) return;
            }
        }
    }

    private void displaySeatMap(String showtimeId) {
        System.out.println("\n   ================ LAYAR ================");
        List<String> bookedSeats = bookingService.getBookedSeats(showtimeId);
        char[] rows = {'A', 'B', 'C', 'D', 'E'};
        int cols = 6;

        for (char row : rows) {
            System.out.print("   Row " + row + " | ");
            for (int i = 1; i <= cols; i++) {
                String seatNum = row + String.valueOf(i);
                
                if (bookedSeats.contains(seatNum)) {
                    System.out.print(RED + "[XX] " + RESET); 
                } else {
                    System.out.print(GREEN + "[" + seatNum + "] " + RESET);
                }
                if (i == 3) System.out.print("   "); 
            }
            System.out.println();
        }
        System.out.println("\n   Ket: " + RED + "[XX]" + RESET + " Terisi, " + GREEN + "[A1]" + RESET + " Kosong");
        System.out.println("   =======================================");
    }

    private Payment getPaymentMethod(int choice) {
        return switch (choice) {
            case 1 -> new CashPayment();
            case 2 -> new QRISPayment();
            case 3 -> new VirtualAccountPayment();
            default -> new CashPayment();
        };
    }

    private void viewHistory() {
        List<Booking> bookings = bookingService.getBookingsByUser(userId);
        if (bookings.isEmpty()) {
            System.out.println(YELLOW + "   Belum ada riwayat pemesanan." + RESET);
            return;
        }
        System.out.println("\n" + CYAN + "   === RIWAYAT PEMESANAN ===" + RESET);
        for (Booking b : bookings) {
            System.out.printf("   Kode: %-14s | Kursi: %-8s | Rp %d\n",
                    b.getBookingCode(), String.join(",", b.getSeats()), (long)b.getTotalPrice());
        }
    }
}
