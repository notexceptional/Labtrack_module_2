package labtrack.users;

import java.util.Scanner;

public abstract class User {
    protected String userID;
    protected String username;
    protected String password;
    protected String role;

    public User(String userID, String username, String role) {
        this.userID = userID;
        this.username = username;
        this.role = role;
    }

    public boolean login(String inputPassword) {
        return password.equals(inputPassword);
    }

    public void logout() {
        System.out.println("Logged out successfully.");
    }

    public abstract void showMenu();
    public void handleChoice(int choice, Scanner sc) {
    }
}
