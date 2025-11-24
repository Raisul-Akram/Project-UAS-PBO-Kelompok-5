package model;

public class CashPayment implements Payment {
    @Override
    public boolean processPayment(double amount) {
        System.out.println("Pembayaran tunai berhasil.");
        return true;
    }
