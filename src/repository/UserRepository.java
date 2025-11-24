package repository;

import model.User;
import model.Admin;
import model.Account;
import java.util.ArrayList;
import java.util.List;

public class UserRepository {

    private List<Account> users = new ArrayList<>();

    public UserRepository() {
        loadDummyData();
    }

    private void loadDummyData() {
        // USER BIASA
        users.add(new User("U001", "user1", "pass123"));

        // ADMIN SEHARUSNYA OBJECT ADMIN, bukan USER
        users.add(new Admin("A001", "admin", "admin123"));
    }

    public List<Account> findAll() {
        return users;
    }

    public Account findByUsername(String username) {
        for (Account u : users) {
            if (u.getUsername().equals(username)) {
                return u;
            }
        }
        return null;
    }
}
