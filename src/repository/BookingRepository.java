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
