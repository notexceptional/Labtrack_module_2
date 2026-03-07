# LabTrack 🔬

**A comprehensive Laboratory Management System built with Java**

![LabTrack](images/Gemini_Generated_Image_t33l3ut33l3ut33l.png)

LabTrack is a role-based research laboratory management system designed to streamline lab operations including user management, inventory tracking, experiment documentation, lab room reservations, and equipment borrowing. The system supports multiple user roles with tailored functionalities for each.

---

## Features

### User Management (Admin)
- Create, delete, and view users
- Update user passwords
- Role-based access control (Researcher, Technician, Lab Manager)

### Experiment Management (Researcher)
- Add, view, modify, and delete experiments
- **Version Control**: Track experiment history with snapshots
- Restore previous versions of experiments
- View detailed change logs with diff comparisons

### Inventory Management (Technician)
- Add inventory items (categorized as "necessary" or "other")
- Update item quantities
- Mark items as out of stock
- View complete inventory and out-of-stock items
- Process borrow requests from researchers

### Lab Room Reservations
- **Researchers**: Request lab room reservations
- **Lab Managers**: Approve/reject reservation requests
- View all booked rooms and pending reservations

### Item Borrowing System
- Researchers can request to borrow items
- Technicians review and approve borrow requests
- Track currently borrowed items

### Reports (Lab Manager)
- Generate system summary reports
- View statistics on experiments, inventory, and bookings
- Manage item requests

---

## Project Structure

```
Labtrack/
├── src/labtrack/
│   ├── auth/           # Authentication services
│   ├── booking/        # Lab room reservation system
│   ├── experiments/    # Experiment management & version control
│   ├── inventory/      # Inventory & item services
│   ├── labroom/        # Lab room data models
│   ├── main/           # Application entry point
│   ├── protocol/       # Protocol definitions
│   ├── reports/        # Reporting services
│   ├── sample/         # Sample data models
│   ├── users/          # User roles (Admin, Researcher, Technician, LabManager)
│   ├── util/           # File management & input helpers
│   └── version/        # Version control system
├── bin/                # Compiled output
├── lib/                # Dependencies
└── *.csv               # Data storage files
```

---

## Prerequisites

- **Java JDK 17** or higher
- **VS Code** with Java Extension Pack (recommended) or any Java IDE

---

## Installation & Running

### Using VS Code
1. Open the project folder in VS Code
2. Ensure the Java Extension Pack is installed
3. Run `Main.java` from `src/labtrack/main/`

### Using Command Line
```bash
# Navigate to project directory
cd Labtrack

# Compile
javac -d bin src/labtrack/**/*.java

# Run
java -cp bin labtrack.main.Main
```

---

## User Roles & Permissions

| Role | Key Permissions |
|------|-----------------|
| **Admin** | Create/delete users, manage passwords, view all users |
| **Researcher** | Manage experiments, version control, borrow items, book lab rooms |
| **Technician** | Manage inventory, process borrow requests, track items |
| **Lab Manager** | Approve reservations, generate reports, approve item requests |

---

## Default Login

On first run, only **Admin** login is available:
- **Role**: `admin`
- **Password**: `123456`

After initial setup, the Admin can create users with other roles.

---

## Data Storage

Data is persisted in CSV files:

| File | Description |
|------|-------------|
| `users.csv` | User accounts and credentials |
| `experiments.csv` | Experiment records |
| `experiment_versions.csv` | Version history for experiments |
| `inventory.csv` | Inventory items |
| `borrow_requests.csv` | Pending borrow requests |
| `borrowed_items.csv` | Currently borrowed items |
| `bookings_pending.csv` | Pending room reservations |
| `bookings_approved.csv` | Approved reservations |
| `lab_rooms.csv` | Lab room allocations |

---

## Usage Examples

### Login Flow
```
**************************************************
*           Welcome to LABTRACK                  *
**************************************************

+----------------------------------------------+
|                    LOGIN                     |
+----------------------------------------------+
Enter username: john
Enter role: researcher
Enter password: ****

  >>> Welcome, john! <<<
```

### Researcher Dashboard
```
+----------------------------------------------+
|           RESEARCHER DASHBOARD               |
+----------------------------------------------+

  ~~~ Experiments ~~~
  [1] Add Experiment
  [2] View Experiments
  [3] Modify Experiment

  ~~~ Version Control ~~~
  [6] View Version History
  [7] Restore Version

  ~~~ Items ~~~
  [10] View Available Items
  [11] Borrow Item
  [12] Request New Item

  ~~~ Reservations ~~~
  [20] Delete Experiment
  [21] Make Reservation
  [22] View Booked Rooms
```

---

## Key Highlights

- **Role-Based Access Control**: Each user type has a customized dashboard
- **Version Control for Experiments**: Track changes with timestamps and restore previous versions
- **File-Based Persistence**: No external database required
- **Modular Architecture**: Clean separation of concerns with service classes
- **Input Validation**: Secure input handling with validation rules

---

## Contributing

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/new-feature`)
3. Commit changes (`git commit -am 'Add new feature'`)
4. Push to branch (`git push origin feature/new-feature`)
5. Create a Pull Request

---

## License

This project is available for educational and academic purposes.

---

## Authors

Developed as a Laboratory Management Solution for efficient lab operations tracking.
