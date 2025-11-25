package model;

public class Movie {
    private String id;
    private String title;
    private String genre;
    private int duration; // Tambahan baru

    // Update Constructor: Terima 4 parameter
    public Movie(String id, String title, String genre, int duration) {
        this.id = id;
        this.title = title;
        this.genre = genre;
        this.duration = duration;
    }

    // Constructor lama (untuk jaga-jaga kompatibilitas, optional)
    public Movie(String id, String title, String genre) {
        this(id, title, genre, 0);
    }

    // Getters
    public String getId() { return id; }
    public String getTitle() { return title; }
    public String getGenre() { return genre; }
    public int getDuration() { return duration; } // Tambahan baru
    
    // Setters (jika perlu)
    public void setTitle(String title) { this.title = title; }
    public void setGenre(String genre) { this.genre = genre; }
    public void setDuration(int duration) { this.duration = duration; }
}
