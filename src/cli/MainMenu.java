package cli;

import auth.AuthService;
import auth.LoginFailedException;
import model.Account;
import util.InvalidInputException;

import java.util.Scanner;

/**
 * Main menu CLI untuk login.
 * @author Kelompok 5
 */
public class MainMenu {
    private AuthService authService;
    private Scanner scanner;

    public MainMenu() {
        this.authService = new AuthService();
        this.scanner = new Scanner(System.in);
    }
public void display() {
        // Banner ASCII
        System.out.println("\033[34m" + // Warna biru
                "██████╗ ██╗ ██████╗ ███████╗██╗  ██╗ ██████╗ ██████╗\n" +
                "██╔══██╗██║██╔═══██╗██╔════╝██║ ██╔╝██╔═══██╗██╔══██╗\n" +
                "██████╔╝██║██║   ██║███████╗█████╔╝ ██║   ██║██████╔╝\n" +
                "██╔══██╗██║██║   ██║╚════██║██╔═██╗ ██║   ██║██╔═══╝ \n" +
                "██████╔╝██║╚██████╔╝███████║██║  ██╗╚██████╔╝██║     \n" +
                "╚═════╝ ╚═╝ ╚═════╝ ╚══════╝╚═╝  ╚═╝ ╚═════╝ ╚═╝     \n" +
                "Sistem Pemesanan Tiket Bioskop - UAS PBO C KLP 5\n" + "\033[0m");

        while (true) {
            System.out.println("\n=== Main Menu ===");
            System.out.println("1. Login");
            System.out.println("2. Exit");
            System.out.print("Pilih: ");
            boolean validInput = false;
            while (!validInput) {
                try {
                    int choice = Integer.parseInt(scanner.nextLine());
                    switch (choice) {
                        case 1:
                            login();
                            validInput = true;
                            break;
                        case 2:
                            System.out.println("Terima kasih!");
                            validInput = true;
                            return;
                        default:
                            throw new InvalidInputException("Pilihan tidak valid.");
                    }
                } catch (NumberFormatException e) {
                    System.out.println("Input tidak valid. Silakan masukkan angka yang valid.");
                    System.out.print("Pilih: ");
                } catch (InvalidInputException e) {
                    System.out.println("Error: " + e.getMessage());
                    System.out.print("Pilih: ");
                }
            }
        }
    }
    private void login() {
        System.out.print("Username: ");
        String username = scanner.nextLine();
        System.out.print("Password: ");
        String password = scanner.nextLine();
            try {
                Account account = authService.login(username, password);
                if (account instanceof model.Admin) {
                    new AdminMenu().display();
                } else {
                    new UserMenu(account.getUsername()).display();
                }
            } catch (LoginFailedException e) {
                System.out.println("Error: " + e.getMessage());
            }
    }
}
