import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

public class Gym {
    private static final String[][] plans = {
            {"1 Day", "1 Month", "1 Year"},
            {"1 Day", "1 Month", "1 Year"}
    };

    private static final double[][] prices = {
            {10.0, 100.0, 1000.0}, // With Pool
            {8.0, 80.0, 800.0}     // Without Pool
    };

    // Store loyal customers based on passport number
    private static List<LoyalCustomer> loyalCustomers = new ArrayList<>();

    public static void startGymBooking() {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            try {
                System.out.println("\n--- Gym Appointment Menu ---");
                System.out.println("1. Book With Pool");
                System.out.println("2. Book Without Pool");
                System.out.println("3. Return to Main Menu");
                System.out.println("4. Exit");
                System.out.print("Enter your choice (1–4): ");

                int option = scanner.nextInt();
                scanner.nextLine(); // clear newline

                if (option == 1 || option == 2) {
                    handleBooking(scanner, option - 1);
                } else if (option == 3) {
                    break;
                }
                else if (option == 4) {
                    System.out.println("Goodbye!");
                    System.exit(0);
                }
                else {
                    System.out.println("Invalid choice.");
                }

            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter a number.");
                scanner.nextLine();
            }
        }
    }

    private static void handleBooking(Scanner scanner, int planType) {
        System.out.println("\nChoose Duration:");
        for (int i = 0; i < plans[planType].length; i++) {
            System.out.println((i + 1) + ". " + plans[planType][i] + " — $" + prices[planType][i]);
        }

        System.out.print("Enter your choice (1–3): ");
        int duration;
        try {
            duration = scanner.nextInt();
            scanner.nextLine(); // clear newline

            if (duration < 1 || duration > 3) {
                System.out.println("Invalid duration choice.");
                return;
            }

            // Get customer details
            String name;
            while (true) {
                System.out.print("Enter name: ");
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
                System.out.print("Enter surname: ");
                surname = scanner.nextLine();

                if (surname.length() <= 1) {
                    System.out.println("Surname must be longer than 1 character.");
                    continue;
                }
                if (!surname.matches("[A-Za-z ]+")) {
                    System.out.println("Invalid surname. Only letters and spaces are allowed.");
                    continue;
                }
                break;
            }

            String email;
            while (true) {
                System.out.print("Enter email: ");
                email = scanner.nextLine();

                if (email.length() <= 5) {
                    System.out.println("Email must be longer than 5 characters.");
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
                System.out.print("Enter phone number: +");
                phoneNumber = scanner.nextLine();
                if (!phoneNumber.matches("\\d{8,15}")) {
                    System.out.println("Invalid phone number. It should be 8 to 15 digits.");
                    continue;
                }
                break;
            }

            String passportNumber;
            while (true) {
                System.out.print("Enter passport number: ");
                passportNumber = scanner.nextLine();
                if (passportNumber.length() < 5 || !passportNumber.matches("[A-Z0-9]+")) {
                    System.out.println("Invalid passport number. Must be 5+ letters/digits.");
                    continue;
                }
                break;
            }

            // Price logic
            double basePrice = prices[planType][duration - 1];
            double finalPrice;
            LoyalCustomer customer = findLoyalCustomerByPassport(passportNumber);

            if (customer != null) {
                finalPrice = basePrice * 0.9; // 10% discount for returning customer
                System.out.println("✅ Returning loyal customer — 10% discount applied!");
            } else {
                customer = new LoyalCustomer(name, surname, email, phoneNumber, passportNumber);
                loyalCustomers.add(customer); // Add as loyal customer (without points)
                finalPrice = basePrice; // no discount for new customer
                System.out.println("🎉 First time customer — no discount, but now enrolled in loyalty program!");
            }

            GymReceipt.generateGymReceipt(finalPrice, customer);

        } catch (InputMismatchException e) {
            System.out.println("Invalid input.");
            scanner.nextLine();
        }
    }

    private static LoyalCustomer findLoyalCustomerByPassport(String passport) {
        for (LoyalCustomer lc : loyalCustomers) {
            if (lc.getPassportNumber().equalsIgnoreCase(passport)) {
                return lc;
            }
        }
        return null;
    }

    public static void main(String[] args) {
        startGymBooking();
    }
}
