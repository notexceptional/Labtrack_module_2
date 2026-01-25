package labtrack.users;

public class LabManager extends User {
    public LabManager(String id, String name) {
        super(id, name, "123456","LabManager");
    }

    @Override
    public void showMenu() {
        System.out.println("1. Approve Reservation\n2. View Reports\n0. Logout");
    }
}