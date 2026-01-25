package labtrack.users;

public class Admin extends User {
    public Admin(String id, String name) {
        super(id, name, "123456","Admin");
    }

    @Override
    public void showMenu() {
        System.out.println("1. Create User\n2. Delete User\n0. Logout");
    }
}