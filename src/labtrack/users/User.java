package labtrack.users;

import java.util.Scanner;

/**
 * Abstract base class for all user roles in the LABTRACK system.
 * Defines the core structure for dashboards and command handling.
 */
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
        boolean matches = password.equals(inputPassword);
        return matches;
    }

    public void logout() {
        System.out.println("Logged out successfully.");
    }

    public String getUsername() {
        return username;
    }

    /**
     * Displays the role-specific command menu.
     */
    public abstract void showMenu();

    /**
     * Handles the user's menu selection.
     */
    public abstract void handleChoice(int choice, Scanner sc);
}
