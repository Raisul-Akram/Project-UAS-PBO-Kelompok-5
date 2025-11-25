package cli;

import model.Account;
import model.Admin;
import model.Booking;
import model.Movie;
import model.Showtime;
import service.BookingService;
import service.MovieService;
import repository.BookingRepository;
import repository.UserRepository;
import util.FileManager;
import util.InvalidInputException;

import java.util.List;
import java.util.Scanner;

/**
 * Menu CLI untuk Admin (Versi PRO + Fitur Batal).
 * @author Kelompok 5
 */
public class AdminMenu {
    private MovieService movieService;
    private BookingService bookingService;
    private BookingRepository bookingRepo;
    private UserRepository userRepo;
    private Scanner scanner;

    // ANSI Colors
    private final String RESET = "\u001B[0m";
    private final String GOLD = "\u001B[33m";
    private final String CYAN = "\u001B[36m";
    private final String GREEN = "\u001B[32m";
    private final String RED = "\u001B[31m";

    public AdminMenu() {
        this.movieService = new MovieService();
        this.bookingService = new BookingService();
        this.bookingRepo = new BookingRepository();
        this.userRepo = new UserRepository();
        this.scanner = new Scanner(System.in);
    }

    public void display() {
        while (true) {
            printHeader();
            printDashboard(); 

            System.out.println("\n" + GOLD + "=== MAIN NAVIGATION ===" + RESET);
            System.out.printf("%-30s %-30s\n", "1. [+ Film] Tambah Film", "5. [Jadwal] Atur Tayangan");
            System.out.printf("%-30s %-30s\n", "2. [Edit] Edit Film", "6. [User] Lihat User");
            System.out.printf("%-30s %-30s\n", "3. [Hapus] Hapus Film", "7. [Trans] Riwayat Transaksi");
            System.out.printf("%-30s %-30s\n", "4. [List] Lihat Daftar Film", "8. [Lapor] Generate Laporan");
            System.out.println("9. [Keluar] Logout");
            System.out.println("------------------------------------------------------------");
            System.out.print("Pilih Menu > ");

            boolean validInput = false;
            while (!validInput) {
                try {
                    String inputRaw = scanner.nextLine();
                    if (inputRaw.isEmpty()) continue;
                    int choice = Integer.parseInt(inputRaw);

                    switch (choice) {
                        case 1 -> { addMovie(); validInput = true; }
                        case 2 -> { editMovie(); validInput = true; }
                        case 3 -> { deleteMovie(); validInput = true; }
                        case 4 -> { viewMovies(); validInput = true; }
                        case 5 -> { manageShowtimes(); validInput = true; }
                        case 6 -> { viewUsers(); validInput = true; }
                        case 7 -> { viewTransactions(); validInput = true; }
                        case 8 -> { generateReport(); validInput = true; }
                        case 9 -> { return; }
                        default -> throw new InvalidInputException("Pilihan tidak valid.");
                    }
                } catch (NumberFormatException e) {
                    System.out.println(RED + "Input harus angka." + RESET);
                    System.out.print("Pilih Menu > ");
                } catch (InvalidInputException e) {
                    System.out.println(RED + "Error: " + e.getMessage() + RESET);
                    System.out.print("Pilih Menu > ");
                }
            }
            System.out.println("\nTekan Enter untuk kembali ke menu...");
            scanner.nextLine();
        }
    }

    // ================= HELPER UI =================

    private void printHeader() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
        
        System.out.println(GOLD + "============================================================");
        System.out.println("   █▀▄▀█ █▀█ █░█ █ █▀▀   ▄▀█ █▀▄ █▀▄▀█ █ █▄░█");
        System.out.println("   █░▀░█ █▄█ ▀▄▀ █ ██▄   █▀█ █▄▀ █░▀░█ █ █░▀█");
        System.out.println("           ADMINISTRATOR CONTROL PANEL");
        System.out.println("============================================================" + RESET);
    }

    private void printDashboard() {
        int totalMovies = movieService.getAllMovies().size();
        int totalUsers = userRepo.findAll().size();
        List<Booking> bookings = bookingRepo.findAll();
        double totalRevenue = bookings.stream().mapToDouble(Booking::getTotalPrice).sum();

        System.out.println(CYAN + " [ DASHBOARD STATISTIK ]" + RESET);
        System.out.println(" +---------------------+---------------------+---------------------+");
        System.out.printf(" | %-19s | %-19s | %-19s |\n", "TOTAL FILM", "TOTAL USER", "PENDAPATAN");
        System.out.println(" +---------------------+---------------------+---------------------+");
        System.out.printf(" | %-19s | %-19s | Rp %-16s |\n", 
                center(String.valueOf(totalMovies), 19), 
                center(String.valueOf(totalUsers), 19), 
                (long)totalRevenue);
        System.out.println(" +---------------------+---------------------+---------------------+");
    }
    
    private String center(String s, int size) {
        return String.format("%" + (size + s.length()) / 2 + "s", s);
    }

    // ================= FITUR FILM =================

    private void addMovie() {
        System.out.println("\n" + GOLD + "--- Tambah Film Baru ---" + RESET);
        System.out.println("(Ketik '0' untuk batal)");
        
        String id = "";
        while (true) {
            System.out.print("ID Film : ");
            id = scanner.nextLine();
            
            if (id.equals("0")) { System.out.println("Proses dibatalkan."); return; } // FITUR BATAL

            if (movieService.findById(id) != null) {
                System.out.println(RED + "Error: ID Film '" + id + "' sudah ada! Gunakan ID lain." + RESET);
            } else {
                break; 
            }
        }

        System.out.print("Judul   : ");
        String title = scanner.nextLine();
        System.out.print("Genre   : ");
        String genre = scanner.nextLine();
        System.out.print("Durasi (menit): "); 
        int duration = 0;
        try {
             duration = Integer.parseInt(scanner.nextLine());
        } catch(Exception e) { duration = 0; }

        boolean success = movieService.addMovie(new Movie(id, title, genre, duration));
        
        if (success) {
            System.out.println(GREEN + "✔ Film berhasil ditambahkan ke database." + RESET);
        } else {
            System.out.println(RED + "✘ Gagal menyimpan film." + RESET);
        }
    }

    private void editMovie() {
        System.out.println("\n" + GOLD + "--- Edit Film ---" + RESET);
        viewMovies(); 
        System.out.println("(Ketik '0' untuk batal)");

        System.out.print("\nMasukkan ID film yang akan diedit: ");
        String id = scanner.nextLine();
        
        if (id.equals("0")) { System.out.println("Proses dibatalkan."); return; } // FITUR BATAL

        Movie movie = movieService.findById(id);
        if (movie == null) {
            System.out.println(RED + "✘ Film tidak ditemukan." + RESET);
            return;
        }

        System.out.println("Data saat ini: " + CYAN + movie.getTitle() + " (" + movie.getGenre() + ")" + RESET);
        System.out.print("Judul baru (Enter jika tetap): ");
        String newTitle = scanner.nextLine();
        if (!newTitle.isBlank()) movie.setTitle(newTitle);

        System.out.print("Genre baru (Enter jika tetap): ");
        String newGenre = scanner.nextLine();
        if (!newGenre.isBlank()) movie.setGenre(newGenre);

        movieService.updateMovie(movie);
        System.out.println(GREEN + "✔ Data film berhasil diperbarui." + RESET);
    }

    private void deleteMovie() {
        System.out.println("\n" + GOLD + "--- Hapus Film ---" + RESET);
        System.out.println("(Ketik '0' untuk batal)");
        
        System.out.print("Masukkan ID film yang akan dihapus: ");
        String id = scanner.nextLine();

        if (id.equals("0")) { System.out.println("Proses dibatalkan."); return; } // FITUR BATAL

        boolean success = movieService.deleteMovieById(id);
        if (success) {
            System.out.println(GREEN + "✔ Film berhasil dihapus permanen." + RESET);
        } else {
            System.out.println(RED + "✘ Film tidak ditemukan." + RESET);
        }
    }

    private void viewMovies() {
        List<Movie> movies = movieService.sortMoviesByPopularity();
        if (movies.isEmpty()) {
            System.out.println(RED + "Belum ada data film." + RESET);
            return;
        }

        System.out.println("\n" + CYAN + "=== DAFTAR FILM ===" + RESET);
        System.out.printf(GOLD + "%-6s | %-25s | %-15s | %-10s%n" + RESET, "ID", "JUDUL", "GENRE", "DURASI");
        System.out.println("----------------------------------------------------------------");
        for (Movie m : movies) {
            System.out.printf("%-6s | %-25s | %-15s | %-10s%n", 
                m.getId(), 
                (m.getTitle().length() > 25 ? m.getTitle().substring(0,22)+"..." : m.getTitle()), 
                m.getGenre(),
                m.getDuration() + " mnt"
            );
        }
        System.out.println("----------------------------------------------------------------");
    }

    // ================= FITUR JADWAL =================

    private void manageShowtimes() {
        System.out.println("\n" + GOLD + "=== MANAJEMEN JADWAL ===" + RESET);
        System.out.println("1. Lihat Jadwal Tayang");
        System.out.println("2. Tambah Jadwal Baru");
        System.out.println("3. Kembali");
        System.out.print("Pilih > ");

        try {
            String input = scanner.nextLine();
            if(input.isEmpty()) return;
            int choice = Integer.parseInt(input);
            switch (choice) {
                case 1 -> viewShowtimes();
                case 2 -> addShowtime();
                case 3 -> { return; }
                default -> System.out.println("Pilihan tidak valid.");
            }
        } catch (NumberFormatException e) {
            System.out.println("Input harus angka.");
        }
    }

    private void viewShowtimes() {
        List<Showtime> showtimes = movieService.getAllShowtimes();
        if (showtimes.isEmpty()) {
            System.out.println("Belum ada jadwal tayang.");
            return;
        }

        System.out.println("\n" + CYAN + "=== JADWAL TAYANG ===" + RESET);
        System.out.printf(GOLD + "%-6s | %-6s | %-10s | %-12s%n" + RESET, "ID", "FILM", "WAKTU", "HARGA");
        System.out.println("------------------------------------------");
        for (Showtime st : showtimes) {
            System.out.printf("%-6s | %-6s | %-10s | Rp %-10s%n",
                    st.getId(), st.getMovieId(), st.getTime(), (long)st.getPrice());
        }
        System.out.println("------------------------------------------");
    }

    private void addShowtime() {
        System.out.println("\n" + GOLD + "--- Tambah Jadwal ---" + RESET);
        System.out.println("(Ketik '0' untuk batal)");
        viewMovies(); 
        
        System.out.print("ID Showtime (Unik)   : ");
        String id = scanner.nextLine();
        if (id.equals("0")) return; // BATAL

        System.out.print("ID Film              : ");
        String movieId = scanner.nextLine();
        if (movieId.equals("0")) return; // BATAL
        
        System.out.print("Jam Tayang (HH:mm)   : ");
        String time = scanner.nextLine();
        
        System.out.print("Harga Tiket          : ");
        double price;
        try {
            String p = scanner.nextLine();
            if (p.equals("0")) return; // BATAL
            price = Double.parseDouble(p);
        } catch (NumberFormatException e) {
            System.out.println(RED + "Harga tidak valid." + RESET);
            return;
        }

        Showtime showtime = new Showtime(id, movieId, time, "-", price); 
        movieService.addShowtime(showtime);
        System.out.println(GREEN + "✔ Jadwal tayang berhasil ditambahkan." + RESET);
    }

    // ================= FITUR USER & LAPORAN =================

    private void viewUsers() {
        List<Account> accounts = userRepo.findAll();
        
        if (accounts.isEmpty()) {
            System.out.println("Belum ada data user.");
            return;
        }

        System.out.println("\n" + CYAN + "=== DATABASE USER ===" + RESET);
        System.out.printf(GOLD + "%-10s | %-20s%n" + RESET, "ROLE", "USERNAME");
        System.out.println("------------------------------");
        for (Account acc : accounts) {
            String role = (acc instanceof Admin) ? GOLD + "ADMIN" + RESET : "USER";
            System.out.printf("%-20s | %-20s%n", role, acc.getUsername());
        }
        System.out.println("------------------------------");
    }

    private void viewTransactions() {
        List<Booking> bookings = bookingRepo.findAll();
        if (bookings.isEmpty()) {
            System.out.println("Belum ada transaksi.");
            return;
        }

        System.out.println("\n" + CYAN + "=== RIWAYAT TRANSAKSI ===" + RESET);
        System.out.printf(GOLD + "%-15s | %-10s | %-15s | %-10s%n" + RESET, "KODE", "USER", "SHOWTIME", "TOTAL");
        System.out.println("----------------------------------------------------------");
        for (Booking b : bookings) {
            System.out.printf("%-15s | %-10s | %-15s | Rp %-10s%n",
                    b.getBookingCode(), b.getUserId(), b.getShowtimeId(), (long)b.getTotalPrice());
        }
        System.out.println("----------------------------------------------------------");
    }

    private void generateReport() {
        List<Booking> all = bookingRepo.findAll();
        int totalTiket = 0;
        double totalPendapatan = 0;

        for(Booking b : all) {
            totalTiket += b.getSeats().size();
            totalPendapatan += b.getTotalPrice();
        }

        System.out.println("\n" + GREEN + "=== FINANCIAL REPORT ===" + RESET);
        System.out.println("------------------------");
        System.out.printf("%-20s : %d%n", "Total Tiket Terjual", totalTiket);
        System.out.printf("%-20s : Rp %d%n", "Total Pendapatan", (long)totalPendapatan);
        System.out.println("------------------------");
        
        FileManager.getInstance().writeFile("data/report.txt",
                List.of("--- LAPORAN KEUANGAN BIOSKOP ---",
                        "Tanggal Generate: " + java.time.LocalDate.now(),
                        "Total Tiket: " + totalTiket, 
                        "Pendapatan: " + (long)totalPendapatan));
        
        System.out.println(CYAN + "✔ Laporan tercetak di data/report.txt" + RESET);
    }
}
