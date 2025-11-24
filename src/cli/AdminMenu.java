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
