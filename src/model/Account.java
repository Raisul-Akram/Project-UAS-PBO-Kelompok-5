package model;

public abstract class Account {
    protected String id;
    protected String username;
    protected String password;

    public Account(String id, String username, String password) {
        this.id = id;
        this.username = username;
        this.password = password;
    }

    public String getId() { return id; }
    public String getUsername() { return username; }
    public String getPassword() { return password; }

    public abstract void showMenu();
}
