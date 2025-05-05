import java.util.InputMismatchException;
import java.util.Scanner;
import java.util.ArrayList;
import java.io.*;

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
    private boolean isAvailable = true;
    private static Room[] rooms = new Room[120];
    private int roomNumber;
    private double price;
    private RoomType roomType;
    private Customer customer;
    public static ArrayList<LoyalCustomer> loyalCustomers = new ArrayList<>();



    public Room(int roomNumber, double price, RoomType roomType) {
        this.roomNumber = roomNumber;
        this.price = price;
        this.roomType = roomType;
    }

    public static Room[] getRooms() {
        return rooms;
    }

    public int getRoomNumber() {
        return roomNumber;
    }

    public static ArrayList<LoyalCustomer> getLoyalCustomers() {
        return loyalCustomers;
    }

    public void setAvailable(boolean available) {
        this.isAvailable = available;
    }

    public static void startBookingSystem() {
        Scanner scanner = new Scanner(System.in);
        initializeRooms();

        while (true) {
            try {
                System.out.println("\n--- Room Booking Menu ---");
                System.out.println("1. Book a Room");
                System.out.println("2. Unbook a Room");
                System.out.println("3. View All Booked Rooms");
                System.out.println("4. Return to Main Menu");
                System.out.println("5. Exit");
                System.out.print("Enter your choice (1–4): ");

                int option = scanner.nextInt();

                if (option == 1) {
                    handleBooking(scanner);
                } else if (option == 2) {
                    handleUnbooking(scanner);
                } else if (option == 3) {
                    showBookedRooms();
                } else if (option ==4) {
                    break;
                } else if (option ==5) {
                    System.out.println("Goodbye!");
                    System.exit(0);
                }else {
                    System.out.println("Invalid choice.");
                }
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter a valid number.");
                scanner.nextLine();
            }
        }
    }

    private static void handleBooking(Scanner scanner) {
        while (true) {
            System.out.println("\nChoose Room Type:");
            System.out.println("1. Single Room (1–49)");
            System.out.println("2. Double Room (50–99)");
            System.out.println("3. VIP Room (100–119)");
            System.out.println("4. Go Back");

            System.out.print("Enter choice (1–4): ");
            int choice;
            try {
                choice = scanner.nextInt();
            } catch (Exception e) {
                System.out.println("Invalid input. Please enter a number between 1 and 4.");
                scanner.nextLine();
                continue;
            }

            if (choice == 4) return;

            RoomType selectedType;
            if (choice == 1) selectedType = RoomType.SINGLE_ROOM;
            else if (choice == 2) selectedType = RoomType.DOUBLE_ROOM;
            else if (choice == 3) selectedType = RoomType.VIP_ROOM;
            else {
                System.out.println("Invalid room type.");
                continue;
            }

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

            while (!bookingComplete) {
                System.out.print("\nEnter room number to book: ");
                try {
                    selectedNumber = scanner.nextInt();
                } catch (Exception e) {
                    System.out.println("Invalid input. Please enter a valid room number.");
                    scanner.nextLine();
                    continue;
                }

                scanner.nextLine();

                String name;
                while (true) {
                    System.out.print("Enter your name: ");
                    name = scanner.nextLine();

                    if (name.length() <= 1) {
                        System.out.println("Name must be longer than 1 character.");
                        continue;
                    }
                    if (!name.matches("[A-Za-z ]+")) {
                        System.out.println("Invalid name. Only letters and spaces are allowed.");
                        continue;
                    }
                    break;
                }
                String surname;
                while (true) {
                    System.out.print("Enter your surname: ");
                    surname = scanner.nextLine();

                    if (surname.length() <= 1) {
                        System.out.println("Surame must be longer than 1 character.");
                        continue;
                    }
                    if (!surname.matches("[A-Za-z ]+")) {
                        System.out.println("Invalid surname. Only letters and spaces are allowed.");
                        continue;
                    }
                    break;
                }

                String email;
                while(true) {
                    System.out.print("Enter your email: ");
                    email = scanner.nextLine();

                    if (email.length() <= 5) {
                        System.out.println("Email must be longer than 5 character.");
                        continue;
                    }
                    if (!email.contains("@")) {
                        System.out.println("Invalid email address. It must include @.");
                        continue;
                    }
                    break;
                }

                String phoneNumber;
                while (true) {
                    System.out.print("Enter your phone number: +");
                    phoneNumber = scanner.nextLine();
                    if (!phoneNumber.matches("\\d{8,15}")) {
                        System.out.println("Invalid phone number. It should be 8 to 15 digits.");
                        continue;
                    }
                    break;
                }

                String passportNumber;
                while (true) {
                    System.out.print("Enter your passport number: ");
                    passportNumber = scanner.nextLine();
                    if (passportNumber.length() < 5 || !passportNumber.matches("[A-Z0-9]+")) {
                        System.out.println("Invalid passport number. Must be 5+ letters/digits.");
                        continue;
                    }
                    break;
                }

                boolean isReturning = false;
                for (LoyalCustomer lc : loyalCustomers) {
                    if (lc.getPassportNumber().equals(passportNumber)) {
                        isReturning = true;
                        break;
                    }
                }

                LoyalCustomer loyalCustomer = new LoyalCustomer(name, surname, email, phoneNumber, passportNumber);

                if (!isReturning) {
                    loyalCustomers.add(loyalCustomer);
                }

                boolean roomFound = false;

                for (Room room : rooms) {
                    if (room.roomNumber == selectedNumber && room.roomType == selectedType) {
                        roomFound = true;

                        if (room.isAvailable) {
                            room.isAvailable = false;
                            room.customer = loyalCustomer;
                            System.out.println("Room " + selectedNumber + " is now booked.");

                            double finalPrice = isReturning ? room.price * 0.9 : room.price;
                            if (isReturning) {
                                System.out.println("Returning customer detected. 10% discount applied.");
                            }

                            int paymentOption;

                            while (true) {
                                System.out.println("Please select payment method:");
                                System.out.println("1. Credit Card");
                                System.out.println("2. Cash");
                                System.out.println("3. Online");
                                System.out.print("Enter your choice (1–3): ");

                                try {
                                    paymentOption = scanner.nextInt();
                                    scanner.nextLine();

                                    if (paymentOption >= 1 && paymentOption <= 3) {
                                        break;
                                    } else {
                                        System.out.println("Invalid choice. Please select 1, 2, or 3.");
                                    }
                                } catch (InputMismatchException e) {
                                    System.out.println("Invalid input. Please enter a number between 1 and 3.");
                                    scanner.nextLine();
                                }
                            }

                            String method = switch (paymentOption) {
                                case 1 -> "Credit Card";
                                case 2 -> "Cash";
                                case 3 -> "Online";
                                default -> "Unknown";
                            };

                            Payment payment = new Payment(selectedNumber, finalPrice, method);
                            if (payment.processPayment()) {
                                Receipt.generateReceipt(room.roomNumber, room.roomType, finalPrice, method, loyalCustomer);
                                if (isReturning) {
                                    System.out.println(loyalCustomer.getName() + " is a loyal customer and received a discount.");
                                }
                                bookingComplete = true;
                            } else {
                                System.out.println("Booking not completed, no receipt generated.");
                                bookingComplete = true;
                            }
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
            return;
        }
    }


    private static void handleUnbooking(Scanner scanner) {
        System.out.println("\n--- All Rooms That Are Booked ---");
        for (Room room : rooms) {
            if (!room.isAvailable) {
                System.out.println("Room " + room.roomNumber + " (" + room.roomType + ")");
            }
        }

        int roomNumber;
        while (true) {
            System.out.print("\nEnter the room number to unbook (or -1 to go back): ");
            try {
                roomNumber = scanner.nextInt();
                scanner.nextLine();
                break;
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter a valid room number.");
                scanner.nextLine();
            }
        }


        if (roomNumber == -1) return;

        boolean roomFound = false;
        for (Room room : rooms) {
            if (room.roomNumber == roomNumber) {
                roomFound = true;

                if (!room.isAvailable) {
                    room.setAvailable(true);
                    System.out.println("Room " + roomNumber + " has been unbooked successfully.");
                } else {
                    System.out.println("Room " + roomNumber + " is already available and cannot be unbooked.");
                }
                break;
            }
        }

        if (!roomFound) {
            System.out.println("Room number " + roomNumber + " not found.");
        }
    }

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

    public static double getPriceForType(RoomType type) {
        double price = 0.0;

        switch (type) {
            case SINGLE_ROOM:
                price = 60.0;
                break;
            case DOUBLE_ROOM:
                price = 100.0;
                break;
            case VIP_ROOM:
                price = 200.0;
                break;
        }
        return price;
    }
}
