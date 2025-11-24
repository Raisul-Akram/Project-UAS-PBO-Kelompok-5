package repository;

import model.Movie;
import util.FileManager;

import java.util.ArrayList;
import java.util.List;

/**
 * Repository untuk Movie (CRUD lengkap + persistence).
 * Versi Sempurna.
 * @author 
 */
public class MovieRepository {
    private List<Movie> movies = new ArrayList<>();
    private final String FILE_PATH = "data/movies.txt";

    public MovieRepository() {
        loadFromFile();
    }

    // =====================
    // CREATE
    // =====================
    public boolean save(Movie movie) {
        if (findById(movie.getId()) != null) {
            return false; // ID sudah ada
        }
        movies.add(movie);
        saveToFile();
        return true;
    }

    // =====================
    // READ
    // =====================
    public List<Movie> findAll() {
        return new ArrayList<>(movies); // kembalikan copy agar aman
    }

    public Movie findById(String id) {
        for (Movie m : movies) {
            if (m.getId().equalsIgnoreCase(id)) {
                return m;
            }
        }
        return null;
    }

    // =====================
    // UPDATE
    // =====================
    public boolean update(Movie updatedMovie) {
        for (int i = 0; i < movies.size(); i++) {
            if (movies.get(i).getId().equals(updatedMovie.getId())) {
                movies.set(i, updatedMovie);
                saveToFile();
                return true;
            }
        }
        return false;
    }

    // =====================
    // DELETE
    // =====================
    public boolean deleteById(String id) {
        Movie target = findById(id);
        if (target != null) {
            movies.remove(target);
            saveToFile();
            return true;
        }
        return false;
    }


    

