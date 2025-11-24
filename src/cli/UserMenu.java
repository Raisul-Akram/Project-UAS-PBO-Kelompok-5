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
import util.QRGenerator;
import util.SeatUnavailableException;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Menu CLI untuk User.
 * @author Kelompok 5
 */
public class UserMenu {
    private String userId;
    private MovieService movieService;
    private BookingService bookingService;
    private PaymentService paymentService;
    private Scanner scanner;

    public UserMenu(String userId) {
        this.userId = userId;
        this.movieService = new MovieService();
        this.bookingService = new BookingService();
        this.paymentService = new PaymentService();
        this.scanner = new Scanner(System.in);
    }

    public void display() {
        while (true) {
            System.out.println("\n\033[36m=== User Menu ===\033[0m"); // Warna cyan
            System.out.println("1. Lihat Daftar Film");
            System.out.println("2. Cari Film");
            System.out.println("3. Pesan Tiket");
            System.out.println("4. Lihat Riwayat Pemesanan");
            System.out.println("5. Logout");
            System.out.print("Pilih: ");

            boolean validInput = false;
            while (!validInput) {
                try {
                    int choice = Integer.parseInt(scanner.nextLine());
                    switch (choice) {
                        case 1 -> {
                            viewMovies();
                            validInput = true;
                        }
                        case 2 -> {
                            searchMovies();
                            validInput = true;
                        }
                        case 3 -> {
                            bookTicket();
                            validInput = true;
                        }
                        case 4 -> {
                            viewHistory();
                            validInput = true;
                        }
                        case 5 -> {
                            validInput = true;
                            return;
                        }
                        default -> throw new InvalidInputException("Pilihan tidak valid.");
                    }
                } catch (NumberFormatException | InvalidInputException e) {
                    System.out.println("Error: " + e.getMessage() + ". Silakan masukkan angka yang valid.");
                    System.out.print("Pilih: ");
                }
            }
        }
    }

    private void viewMovies() {
        List<Movie> movies = movieService.sortMoviesByPopularity();
        if (movies.isEmpty()) {
            System.out.println("Belum ada data film.");
            return;
        }

        System.out.println("\n=== Daftar Film ===");
        for (Movie m : movies) {
            System.out.println(m.getId() + " - " + m.getTitle() + " (" + m.getGenre() + ")");
        }
    }

    private void searchMovies() {
        System.out.print("Cari judul: ");
        String title = scanner.nextLine();
        List<Movie> results = movieService.searchMovies(title);

        if (results.isEmpty()) {
            System.out.println("Film tidak ditemukan.");
            return;
        }

        System.out.println("\n=== Hasil Pencarian ===");
        for (Movie m : results) {
            System.out.println(m.getId() + " - " + m.getTitle() + " (" + m.getGenre() + ")");
        }
    }

    private void bookTicket() {
        System.out.print("ID Film: ");
        String movieId = scanner.nextLine();

        System.out.print("ID Showtime: ");
        String showtimeId = scanner.nextLine();

        // Ambil showtime untuk dapat harga per kursi
        Showtime showtime = movieService.findShowtimeById(showtimeId);
        if (showtime == null) {
            System.out.println("Showtime tidak ditemukan.");
            return;
        }

        System.out.print("Kursi (pisah koma, e.g., A1,A2): ");
        String[] seatsArr = scanner.nextLine().split(",");
        List<String> seats = new ArrayList<>();
        for (String s : seatsArr) {
            seats.add(s.trim());
        }

        double pricePerSeat = showtime.getPrice();
        double price = pricePerSeat * seats.size();
        System.out.println("Harga per kursi : " + pricePerSeat);
        System.out.println("Total harga      : " + price);

        // Simulasi pembayaran
        System.out.println("Pilih metode pembayaran:");
        System.out.println("1. Tunai");
        System.out.println("2. QRIS");
        System.out.println("3. Virtual Account");
        System.out.print("Pilih: ");

        int payChoice = -1;
        boolean validPayChoice = false;
        while (!validPayChoice) {
            try {
                payChoice = Integer.parseInt(scanner.nextLine());
                if (payChoice < 1 || payChoice > 3) {
                    System.out.println("Pilihan tidak valid, silakan coba lagi.");
                    System.out.print("Pilih: ");
                } else {
                    validPayChoice = true;
                }
            } catch (NumberFormatException e) {
                System.out.println("Input tidak valid. Silakan masukkan angka 1, 2, atau 3.");
                System.out.print("Pilih: ");
            }
        }

        Payment payment = getPaymentMethod(payChoice);
        if (paymentService.process(payment, price)) {
            String bookingCode = "BK" + System.currentTimeMillis(); // Kode booking otomatis
            Booking booking = new Booking(bookingCode, userId, showtimeId, seats, price);
            try {
                bookingService.createBooking(booking);
                System.out.println("Pemesanan berhasil! Kode Booking: " + bookingCode);
                QRGenerator.generateQR(bookingCode); // Bonus QR ASCII
            } catch (SeatUnavailableException e) {
                System.out.println("Error: " + e.getMessage());
            }
        } else {
            System.out.println("Pembayaran gagal.");
        }
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
            System.out.println("Belum ada riwayat pemesanan.");
            return;
        }

        System.out.println("\n=== Riwayat Pemesanan ===");
        for (Booking b : bookings) {
            System.out.println(
                    "Kode: " + b.getBookingCode() +
                    " | Showtime: " + b.getShowtimeId() +
                    " | Kursi: " + String.join(",", b.getSeats()) +
                    " | Total: " + b.getTotalPrice()
            );
        }
    }
}
