package service;

import model.Movie;
import model.Showtime;
import repository.MovieRepository;
import repository.ShowtimeRepository;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Service untuk mengelola film dan jadwal tayang.
 * Menghubungkan layer CLI (AdminMenu/UserMenu) dengan repository.
 * @author Kelompok 5
 */
public class MovieService {

    private MovieRepository movieRepo;
    private ShowtimeRepository showtimeRepo;

    public MovieService() {
        this.movieRepo = new MovieRepository();
        this.showtimeRepo = new ShowtimeRepository();
    }

    // ========= BAGIAN FILM =========

    /** 
     * Tambah film baru.
     * MENGEMBALIKAN BOOLEAN agar AdminMenu tahu sukses/gagal.
     */
    public boolean addMovie(Movie movie) {
        return movieRepo.save(movie);
    }

    /** Cari film berdasarkan judul (contains, case-insensitive) */
    public List<Movie> searchMovies(String title) {
        return movieRepo.findAll().stream()
                .filter(m -> m.getTitle().toLowerCase().contains(title.toLowerCase()))
                .collect(Collectors.toList());
    }

    /** 
     * Urutkan film berdasarkan JUDUL (A-Z).
     */
    public List<Movie> sortMoviesByPopularity() {
        List<Movie> allMovies = movieRepo.findAll();
        allMovies.sort((m1, m2) -> m1.getTitle().compareToIgnoreCase(m2.getTitle()));
        return allMovies;
    }

    /** Ambil semua film */
    public List<Movie> getAllMovies() {
        return movieRepo.findAll();
    }
 
    /** Cari film berdasarkan ID (untuk edit/hapus) */
    public Movie findById(String id) {
        return movieRepo.findById(id);   
    }

    /** Hapus film berdasarkan ID, kembalikan true kalau berhasil */
    public boolean deleteMovieById(String id) {
        return movieRepo.deleteById(id); 
    }

    /** Simpan perubahan film ke file */
    public void updateMovie(Movie movie) {
        movieRepo.update(movie);         
    }

    // ========= BAGIAN SHOWTIME =========

    /** Cari showtime berdasarkan ID (dipakai UserMenu.bookTicket) */
    public Showtime findShowtimeById(String showtimeId) {
        return showtimeRepo.findById(showtimeId);
    }

    /** Ambil semua showtime (bisa dipakai AdminMenu untuk list jadwal) */
    public List<Showtime> getAllShowtimes() {
        return showtimeRepo.findAll();
    }

    /** Tambah showtime baru */
    public void addShowtime(Showtime showtime) {
        showtimeRepo.save(showtime);
    }
}
