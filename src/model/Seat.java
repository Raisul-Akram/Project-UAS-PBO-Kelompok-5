package model;

/**
 * Class untuk menyimpan data kursi.
 * @author Kelompok 5
 */
public class Seat {
    private String id;
    private boolean isAvailable;

    public Seat(String id) {
        this.id = id;
        this.isAvailable = true;
    }

