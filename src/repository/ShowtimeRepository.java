package repository;

import model.Showtime;
import util.FileManager;

import java.util.ArrayList;
import java.util.List;

public class ShowtimeRepository {
    private List<Showtime> showtimes = new ArrayList<>();

    public ShowtimeRepository() {
        loadFromFile();
    }

    /** CREATE */
    public void save(Showtime showtime) {
        showtimes.add(showtime);
        saveToFile();
    }

    /** READ ALL */
    public List<Showtime> findAll() {
        return showtimes;
    }

    /** READ BY ID */
    public Showtime findById(String id) {
        for (Showtime st : showtimes) {
            if (st.getId().equals(id)) {
                return st;
            }
        }
        return null;
    }

    /** UPDATE */
    public boolean update(Showtime updated) {
        for (int i = 0; i < showtimes.size(); i++) {
            if (showtimes.get(i).getId().equals(updated.getId())) {
                showtimes.set(i, updated);
                saveToFile();
                return true;
            }
        }
        return false; // jika ID tidak ditemukan
    }

    /** DELETE */
    public boolean delete(String id) {
        for (int i = 0; i < showtimes.size(); i++) {
            if (showtimes.get(i).getId().equals(id)) {
                showtimes.remove(i);
                saveToFile();
                return true;
            }
        }
        return false; // jika ID tidak ditemukan
    }

    /** LOAD DATA DARI FILE */
    private void loadFromFile() {
        List<String> lines = FileManager.getInstance().readFile("data/showtimes.txt");

        for (String line : lines) {
            String[] parts = line.split(",");
            // id, movieId, time, date, price
            showtimes.add(new Showtime(
                    parts[0],
                    parts[1],
                    parts[2],
                    parts[3],
                    Double.parseDouble(parts[4])
            ));
        }
    }

    /** SAVE DATA KE FILE */
    private void saveToFile() {
        List<String> lines = new ArrayList<>();

        for (Showtime st : showtimes) {
            lines.add(
                    st.getId() + "," +
                    st.getMovieId() + "," +
                    st.getTime() + "," +
                    st.getDate() + "," +
                    st.getPrice()
            );
        }

        FileManager.getInstance().writeFile("data/showtimes.txt", lines);
    }
}
