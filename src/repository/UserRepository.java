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
