import java.util.Scanner;
import java.util.InputMismatchException;

/**
 * Enum representing different types of tables.
 */
enum TableType {
    SMALL_TABLE,
    MEDIUM_TABLE,
    LARGE_TABLE;
}

/**
 * Table class for restaurant table reservation in a hotel.
 */
public class TableReservation{
    private int tableNumber;
    private boolean isAvailable = true;
    private TableType tableType;
    private static TableReservation[] tables = new TableReservation[30]; // Total of 30 tables

    public TableReservation(int tableNumber, TableType tableType) {
        this.tableNumber = tableNumber;
        this.tableType = tableType;
        this.isAvailable = true;
    }

    public static void startReservationSystem() {
        Scanner scanner = new Scanner(System.in);
        initializeTables();

        while (true) {
            System.out.println("\n--- Restaurant Table Reservation Menu ---");
            System.out.println("1. Book a Table");
            System.out.println("2. Unbook a Table");
            System.out.println("3. View Booked Tables");
            System.out.println("4. Go Back");
            System.out.print("Enter your choice (1–4): ");

            int choice;
            try {
                choice = scanner.nextInt();
            } catch (InputMismatchException e) {
                System.out.println("Invalid input.");
                scanner.nextLine();
                continue;
            }

            switch (choice) {
                case 1 -> bookTable(scanner);
                case 2 -> unbookTable(scanner);
                case 3 -> showBookedTables();
                case 4 -> {
                    return;
                }
                default -> System.out.println("Invalid option. Try again.");
            }
        }
    }

    private static void initializeTables() {
        for (int i = 0; i < tables.length; i++) {
            TableType type;
            if (i < 10) type = TableType.SMALL_TABLE;      // 0–9
            else if (i < 20) type = TableType.MEDIUM_TABLE; // 10–19
            else type = TableType.LARGE_TABLE;              // 20–29

            tables[i] = new TableReservation(i + 1, type);
        }
    }

    private static void bookTable(Scanner scanner) {
        while (true) {
            System.out.println("\nChoose Table Type:");
            System.out.println("1. Small Table (1–10)");
            System.out.println("2. Medium Table (11–20)");
            System.out.println("3. Large Table (21–30)");
            System.out.println("4. Go Back");
            System.out.print("Enter choice (1–4): ");

            int choice;
            try {
                choice = scanner.nextInt();
            } catch (InputMismatchException e) {
                System.out.println("Invalid input.");
                scanner.nextLine();
                continue;
            }

            if (choice == 4) return;

            TableType selectedType;
            if (choice == 1) selectedType = TableType.SMALL_TABLE;
            else if (choice == 2) selectedType = TableType.MEDIUM_TABLE;
            else if (choice == 3) selectedType = TableType.LARGE_TABLE;
            else {
                System.out.println("Invalid type.");
                continue;
            }

            System.out.println("Available " + selectedType + "s:");
            boolean found = false;
            for (TableReservation table : tables) {
                if (table.tableType == selectedType && table.isAvailable) {
                    System.out.print(table.tableNumber + " ");
                    found = true;
                }
            }
            System.out.println();

            if (!found) {
                System.out.println("No available tables of this type.");
                return;
            }

            System.out.print("Enter table number to book: ");
            int tableNum = scanner.nextInt();
            boolean booked = false;

            for (TableReservation table : tables) {
                if (table.tableNumber == tableNum && table.tableType == selectedType) {
                    if (table.isAvailable) {
                        table.isAvailable = false;
                        System.out.println("Table " + tableNum + " is now booked.");
                        booked = true;
                        break;
                    } else {
                        System.out.println("Table already booked.");
                        booked = true;
                        break;
                    }
                }
            }

            if (!booked) {
                System.out.println("Invalid table number for selected type.");
            }

            return;
        }
    }

    private static void unbookTable(Scanner scanner) {
        System.out.println("\n--- Booked Tables ---");
        for (TableReservation table : tables) {
            if (!table.isAvailable) {
                System.out.println("Table " + table.tableNumber + " (" + table.tableType + ")");
            }
        }

        System.out.print("Enter table number to unbook (or -1 to cancel): ");
        int number;
        try {
            number = scanner.nextInt();
        } catch (InputMismatchException e) {
            System.out.println("Invalid input.");
            scanner.nextLine();
            return;
        }

        if (number == -1) return;

        for (TableReservation table : tables) {
            if (table.tableNumber == number) {
                if (!table.isAvailable) {
                    table.isAvailable = true;
                    System.out.println("Table " + number + " has been unbooked.");
                    return;
                } else {
                    System.out.println("That table is already available.");
                    return;
                }
            }
        }

        System.out.println("Table not found.");
    }

    private static void showBookedTables() {
        System.out.println("\n--- Booked Tables ---");
        boolean anyBooked = false;
        for (TableReservation table : tables) {
            if (!table.isAvailable) {
                System.out.println("Table " + table.tableNumber + " (" + table.tableType + ") - BOOKED");
                anyBooked = true;
            }
        }
        if (!anyBooked) {
            System.out.println("No tables booked yet.");
        }
    }
    public static void main(String[] args) {
        startReservationSystem();
    }
}
