package labtrack.users;

public class Technician extends User {
    public Technician(String id, String name) {
        super(id, name, "123456","Technician");
    }

    @Override
    public void showMenu() {
        System.out.println("1. Add Inventory Item\n2. View Inventory\n0. Logout");
    }
}