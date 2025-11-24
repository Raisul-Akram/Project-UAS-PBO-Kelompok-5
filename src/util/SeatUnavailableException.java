package util;

/**
 * Exception untuk kursi tidak tersedia.
 * @author Kelompok 5
 */
public class SeatUnavailableException extends Exception {
    public SeatUnavailableException(String message) {
        super(message);
    }
}
