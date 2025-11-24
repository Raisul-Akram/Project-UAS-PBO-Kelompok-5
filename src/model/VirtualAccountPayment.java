package model;

public class VirtualAccountPayment implements Payment {
    @Override
    public boolean processPayment(double amount) {
        System.out.println("Pembayaran Virtual Account berhasil.");
        return true;
    }
