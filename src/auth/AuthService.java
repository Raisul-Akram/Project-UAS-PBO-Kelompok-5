package auth;

import model.Account;
import model.Admin;
import model.User;
import repository.UserRepository;

public class AuthService {

    private UserRepository userRepo;

    public AuthService() {
        this.userRepo = new UserRepository();
    }
