package model;

/**
 * Class untuk menyimpan data jadwal tayang.
 * @author Kelompok 5
 */
public class Showtime {
    private String id;
    private String movieId;
    private String time;
    private String date;
    private double price; // harga per kursi

    public Showtime(String id, String movieId, String time, String date, double price) {
        this.id = id;
        this.movieId = movieId;
        this.time = time;
        this.date = date;
        this.price = price;
    }

    // Getters
    public String getId() { return id; }
    public String getMovieId() { return movieId; }
    public String getTime() { return time; }
    public String getDate() { return date; }
    public double getPrice() { return price; }

    // Setters (supaya AdminMenu bisa edit data)
    public void setMovieId(String movieId) { 
        this.movieId = movieId; 
    }

    public void setTime(String time) { 
        this.time = time; 
    }

    public void setDate(String date) { 
        this.date = date; 
    }

    public void setPrice(double price) { 
        this.price = price; 
    }

    @Override
    public String toString() {
        return "Showtime ID: " + id +
                " | Movie ID: " + movieId +
                " | Date: " + date +
                " | Time: " + time +
                " | Price: " + price;
    }
}
