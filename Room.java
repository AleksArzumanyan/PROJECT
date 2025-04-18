import java.util.Scanner;

/**
 * Enum representing different types of rooms.
 */
enum RoomType {
    SINGLE_ROOM,
    DOUBLE_ROOM,
    VIP_ROOM;
}

/**
 * The Room class represents a hotel room with information about availability, price,
 * room number, and room type. It also includes a simple console-based room booking system.
 */
public class Room {
    /** Indicates if the room is available for booking. */
    private boolean isAvailable = true;

    /** The number assigned to the room. */
    private int roomNumber;

    /** The price of the room. */
    private double price;

    /** The type of the room (Single, Double, or VIP). */
    private RoomType roomType;

    /** Array to hold all room instances. */
    private static Room[] rooms = new Room[120];

    /**
     * Constructs a Room object with a room number, price, and type.
     *
     * @param roomNumber the number assigned to the room
     * @param price      the price of the room
     * @param roomType   the type of the room
     */
    public Room(int roomNumber, double price, RoomType roomType) {
        this.roomNumber = roomNumber;
        this.price = price;
        this.roomType = roomType;
    }

    /**
     * Starts the interactive booking system for users to book and view rooms.
     */
    public static void startBookingSystem() {
        Scanner scanner = new Scanner(System.in);
        initializeRooms();

        while (true) {
            System.out.println("\n--- Room Booking Menu ---");
            System.out.println("1. Book a Room");
            System.out.println("2. View All Booked Rooms");
            System.out.println("3. Exit");
            System.out.print("Enter your choice (1–3): ");
            int option;
            try {
                option = scanner.nextInt();
            } catch (Exception e) {
                System.out.println("Invalid input. Please enter a number between 1 and 3.");
                scanner.nextLine(); // Clear input buffer
                continue;
            }

            if (option == 1) {
                handleBooking(scanner);
            } else if (option == 2) {
                showBookedRooms();
            } else if (option == 3) {
                System.out.println("Goodbye!");
                break;
            } else {
                System.out.println("Invalid choice.");
            }
        }
    }

    /**
     * Handles the process of selecting a room type and booking an available room.
     */
    private static void handleBooking(Scanner scanner) {
        System.out.println("\nChoose Room Type:");
        System.out.println("1. Single Room (1–49)");
        System.out.println("2. Double Room (50–99)");
        System.out.println("3. VIP Room (100–119)");
        System.out.print("Enter choice (1–3): ");
        int choice;
        try {
            choice = scanner.nextInt();
        } catch (Exception e) {
            System.out.println("Invalid input. Please enter a number between 1 and 3.");
            scanner.nextLine();
            return;
        }

        RoomType selectedType;
        if (choice == 1) selectedType = RoomType.SINGLE_ROOM;
        else if (choice == 2) selectedType = RoomType.DOUBLE_ROOM;
        else if (choice == 3) selectedType = RoomType.VIP_ROOM;
        else {
            System.out.println("Invalid room type.");
            return;
        }

        // Show available rooms
        boolean available = false;
        System.out.println("Available " + selectedType + "s:");
        for (Room room : rooms) {
            if (room.roomType == selectedType && room.isAvailable) {
                System.out.print(room.roomNumber + " ");
                available = true;
            }
        }
        System.out.println();

        if (!available) {
            System.out.println("\nNo available rooms in this type.");
            return;
        }

        int selectedNumber = 0;
        boolean bookingComplete = false;

        // Booking loop
        while (!bookingComplete) {
            System.out.print("\nEnter room number to book: ");
            try {
                selectedNumber = scanner.nextInt();
            } catch (Exception e) {
                System.out.println("Invalid input. Please enter a valid room number.");
                scanner.nextLine(); // clear invalid input
                continue;
            }

            boolean roomFound = false;

            for (Room room : rooms) {
                if (room.roomNumber == selectedNumber && room.roomType == selectedType) {
                    roomFound = true;

                    if (room.isAvailable) {
                        room.isAvailable = false;
                        System.out.println("Room " + selectedNumber + " is now booked.");
                        bookingComplete = true;
                        break;
                    } else {
                        System.out.println("Room " + selectedNumber + " is already booked.");
                        System.out.println("Please choose another available room:");

                        boolean availableRoomsExist = false;
                        for (Room r : rooms) {
                            if (r.roomType == selectedType && r.isAvailable) {
                                System.out.print(r.roomNumber + " ");
                                availableRoomsExist = true;
                            }
                        }

                        if (!availableRoomsExist) {
                            System.out.println("\nNo available rooms for the selected type.");
                            bookingComplete = true;
                        }

                        System.out.println();
                        break;
                    }
                }
            }

            if (!roomFound) {
                System.out.println("Invalid room number for selected type. Please try again.");
            }
        }
    }

    /**
     * Displays a list of all rooms that have been booked.
     */
    private static void showBookedRooms() {
        System.out.println("\n--- Booked Rooms ---");
        boolean anyBooked = false;
        for (Room room : rooms) {
            if (!room.isAvailable) {
                System.out.println("Room " + room.roomNumber + " (" + room.roomType + ") - BOOKED");
                anyBooked = true;
            }
        }
        if (!anyBooked) {
            System.out.println("No rooms booked yet.");
        }
    }

    /**
     * Initializes the rooms array with predefined room types and prices.
     */
    private static void initializeRooms() {
        for (int i = 0; i < rooms.length; i++) {
            int roomNum = i + 1;
            RoomType type;
            if (roomNum < 50) type = RoomType.SINGLE_ROOM;
            else if (roomNum < 100) type = RoomType.DOUBLE_ROOM;
            else type = RoomType.VIP_ROOM;

            rooms[i] = new Room(roomNum, getPriceForType(type), type);
        }
    }

    /**
     * Returns the price based on the room type.
     * @param type the RoomType enum
     * @return the price of the room
     */
    private static double getPriceForType(RoomType type) {
        switch (type) {
            case SINGLE_ROOM:
                return 60.0;
            case DOUBLE_ROOM:
                return 100.0;
            case VIP_ROOM:
                return 200.0;
            default:
                return 0.0;
        }
    }

    /**
     * The main method that starts the booking system.
     */
    public static void main(String[] args) {
        Room.startBookingSystem();
    }
}










