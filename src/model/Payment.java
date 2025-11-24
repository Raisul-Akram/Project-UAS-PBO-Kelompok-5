package model;

/**
 * Interface untuk pembayaran.
 * @author Kelompok 5
 */
public interface Payment {
    boolean processPayment(double amount);
    String getPaymentType();
}
