package cli;

import model.Account; // Tambahan import ini penting
import model.Admin;   // Tambahan import ini penting
import model.Booking;
import model.Movie;
import model.Showtime;
import model.User;
import service.BookingService;
import service.MovieService;
import repository.BookingRepository;
import repository.UserRepository;
import util.FileManager;
import util.InvalidInputException;

import java.util.List;
import java.util.Scanner;

/**
 * Menu CLI untuk Admin.
 * Mengelola film, jadwal tayang, user, transaksi, dan laporan.
 * @author Kelompok 5
 */

public class AdminMenu {
    private MovieService movieService;
    private BookingService bookingService;
    private BookingRepository bookingRepo;
    private UserRepository userRepo;
    private Scanner scanner;

public AdminMenu() {
        this.movieService = new MovieService();
        this.bookingService = new BookingService();
        this.bookingRepo = new BookingRepository();
        this.userRepo = new UserRepository();
        this.scanner = new Scanner(System.in);
    }

public void display() {
        while (true) {
            System.out.println("\n\033[33m=== Admin Menu ===\033[0m");
            System.out.println("1. Tambah Film");
            System.out.println("2. Edit Film");
            System.out.println("3. Hapus Film");
            System.out.println("4. Lihat Daftar Film");
            System.out.println("5. Atur Jadwal Tayang");
            System.out.println("6. Lihat Daftar User");
            System.out.println("7. Lihat Daftar Transaksi");
            System.out.println("8. Generate Laporan");
            System.out.println("9. Logout");
            System.out.print("Pilih: ");

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
                    System.out.println("Input harus angka.");
                    System.out.print("Pilih: ");
                } catch (InvalidInputException e) {
                    System.out.println("Error: " + e.getMessage());
                    System.out.print("Pilih: ");
                }
            }
        }
    }
