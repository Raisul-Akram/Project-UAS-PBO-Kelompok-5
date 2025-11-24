package model;

public class User extends Account {
    public User(String id, String username, String password) {
        super(id, username, password);
    }
    
    @Override
    public void showMenu() {
        System.out.println("User Menu");
    }
}
