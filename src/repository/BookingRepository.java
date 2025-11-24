package repository;

import model.Booking;
import util.FileManager;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Repository untuk Booking (Repository Pattern).
 * @author Kelompok 5
 */
public class BookingRepository {
    private List<Booking> bookings = new ArrayList<>();

    public BookingRepository() {
        loadFromFile();
    }

    public void save(Booking booking) {
        bookings.add(booking);
        saveToFile();
    }

    public List<Booking> findAll() {
        return bookings;
    }

    public List<Booking> findByUser(String userId) {
        return bookings.stream()
                .filter(b -> b.getUserId().equals(userId))
                .collect(Collectors.toList());
    }

    private void loadFromFile() {
        // Path file sudah diperbaiki, relatif ke folder kerja
        List<String> lines = FileManager.getInstance().readFile("data/bookings.txt");
        for (String line : lines) {
            String[] parts = line.split(",");
            List<String> seats = List.of(parts[3].split(";")); // Asumsikan seats dipisah ";"
            bookings.add(new Booking(parts[0], parts[1], parts[2], seats, Double.parseDouble(parts[4])));
        }
    }

    private void saveToFile() {
        List<String> lines = new ArrayList<>();
        for (Booking b : bookings) {
            String seatsStr = String.join(";", b.getSeats());
            lines.add(b.getBookingCode() + "," + b.getUserId() + "," + b.getShowtimeId() + "," + seatsStr + "," + b.getTotalPrice());
        }
        // Path file sudah diperbaiki, relatif ke folder kerja
        FileManager.getInstance().writeFile("data/bookings.txt", lines);
    }
}
