package model;

public class Admin extends Account {
    public Admin(String id, String username, String password) {
        super(id, username, password);
    }

    @Override
    public void showMenu() {
        System.out.println("Admin Menu");
    }
}
