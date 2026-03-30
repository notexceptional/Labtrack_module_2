# LABTRACK: Research Laboratory Management System 🔬

**A robust, role-based terminal application engineered in Java for efficient laboratory operations.**

[![Language](https://img.shields.io/badge/language-Java-orange?style=for-the-badge&logo=openjdk&logoColor=white)](https://www.oracle.com/java/)
[![Version](https://img.shields.io/badge/Version-1.1-blue?style=for-the-badge&color=blue)](https://github.com/)
[![License](https://img.shields.io/badge/License-Academic-green?style=for-the-badge)](LICENSE)

---

## 📝 Project Overview

**LABTRACK** is a comprehensive Research Laboratory Management System (RLMS) designed to digitize and streamline the daily operations of a modern research facility. Built with a focus on **Object-Oriented Programming (OOP)** principles, the system provides a secure, role-based environment for managing researchers, inventory, experiments, and facility resources.

### Core Objectives
- **Centralized Management**: Unified platform for equipment, personnel, and data tracking.
- **Data Integrity**: Integrated Version Control System (VCS) for research experiments.
- **Optimized Workflow**: Automated approval queues for borrowing and reservations.
- **Professional UX**: A high-fidelity, colored ANSI terminal interface with structured tabular data presentation.

---

## 🏗️ Technical Architecture

The system is built on a modular architecture that emphasizes scalability and maintainability.

### 🧩 OOP Principles in Action
- **Inheritance**: A hierarchical `User` model defines the base identity, which is extended by specialized roles: `Admin`, `Researcher`, `Technician`, and `LabManager`.
- **Polymorphism**: The application utilizes dynamic method dispatch to render role-specific dashboards and handle commands through a unified interface.
- **Encapsulation**: Strict data hiding in model classes (`Experiment`, `InventoryItem`, `Booking`) ensures that state transitions are managed exclusively through validated service layers.
- **Abstraction**: Utility classes like `TablePrinter` and `Colors` abstract complex terminal rendering logic from the business services.

### 💾 Persistence & Infrastructure
- **CSV Data Layer**: High-performance, lightweight persistence using flat-file CSV storage with custom serialization logic.
- **Custom VCS**: A specialized Version Control System for experiments that captures multi-property snapshots and supports state restoration.

---

## 🚀 Getting Started

### Prerequisites
- **Java Development Kit (JDK) 17** or higher.
- A terminal emulator supporting **ANSI escape codes** (Windows Terminal, iTerm2, VS Code Integrated Terminal).

### Installation & Execution
```bash
# 1. Clone or extract the project
# 2. Navigate to the project directory
cd Labtrack

# 3. Compile the source code
javac -d bin src/labtrack/**/*.java

# 4. Launch the application
java -cp bin labtrack.main.Main
```

---

## 👥 User Roles & Access Control

| Role | Key Responsibilities | Technical Access |
|------|----------------------|------------------|
| **Admin** | System Governance | User CRUD, Password Overrides, Audit Views |
| **Researcher** | Scientific Operations | Experiment VCS, Resource Borrowing, Room Booking |
| **Technician** | Logistics & Inventory | Stock Management, Borrow Request Approval |
| **Lab Manager** | Strategic Oversight | System Reporting, Resource Approval, Item Procurement |

---

## 🛠️ Key Technical Features

### 🏢 Refined Terminal UI
Instead of a standard CLI, LABTRACK features a **Premium ANSI Terminal Interface**:
- **Standardized Tables**: All data is presented in clean, auto-scaling ASCII tables.
- **Color-Coded Feedback**: Success, Warning, and Error states are uniquely styled for immediate user recognition.
- **Clean Navigation**: Elimination of redundant outputs ensures a professional, single-source-of-truth interaction model.

### 🔄 Experiment Versioning
Researchers can track the evolution of their work through a custom **Version Control System**:
- **Snapshots**: Capture the entire state of an experiment at any point in time.
- **Rollback**: Instant restoration to any previous version with detailed change logging.

---

## 📂 Project Structure

```bash
Labtrack/
├── src/labtrack/
│   ├── main/           # Application entry point & core loop
│   ├── auth/           # RBAC & Authentication logic
│   ├── users/          # Hierarchical role models (Inheritance/Polymorphism)
│   ├── experiments/    # Research management & VCS integration
│   ├── inventory/      # Logistics & Item lifecycle services
│   ├── booking/        # Resource reservation system
│   ├── util/           # I/O, Styling (Colors), & Data Presentation (TablePrinter)
│   └── version/        # Core VCS data structures
├── bin/                # Compiled bytecode
└── *.csv               # CSV persistence layer
```

---

## 🎓 Authors

- **230042105** - Tiran Abdullah
- **230042121** - Md Rufayed Ul Alam
- **230042142** - Shudipto Sarwar Mamun

Developed as a professional solution for **Research Laboratory Management**.
