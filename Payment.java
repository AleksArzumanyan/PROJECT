import java.util.InputMismatchException;
import java.util.Scanner;

/**
 * The Payment class handles the payment process for room bookings.
 * It allows users to confirm their payment method and finalize the booking.
 */
public class Payment {
    
    private int roomNumber;
    private double amount;
    private String paymentMethod;

    public Payment(int roomNumber, double amount, String paymentMethod) {
        this.roomNumber = roomNumber;
        this.amount = amount;
        this.paymentMethod = paymentMethod;
    }

    public boolean processPayment() {
        Scanner scanner = new Scanner(System.in);

        System.out.println("\n--- Payment Details ---");
        System.out.println("Room Number: " + roomNumber);
        System.out.println("Amount to be paid: " + amount + " USD");
        System.out.println("Payment Method: " + paymentMethod);

        String confirmation = "";
        boolean validInput = false;

        while (!validInput) {
            System.out.print("Has the payment been confirmed? (yes/no): ");
            try {
                confirmation = scanner.next().trim().toLowerCase();
                if (!confirmation.equals("yes") && !confirmation.equals("no")) {
                    throw new IllegalArgumentException("Invalid input. Please enter 'yes' or 'no'.");
                }
                validInput = true;
            } catch (IllegalArgumentException e) {
                System.out.println("Error: " + e.getMessage());
            } catch (InputMismatchException e) {
                System.out.println("Error: Invalid input type. Please enter 'yes' or 'no'.");
                scanner.nextLine();
            } catch (Exception e) {
                System.out.println("Unexpected error: " + e.getMessage());
            }
        }

        if (confirmation.equals("yes")) {
            System.out.println("Payment confirmed. Booking completed successfully.");
            return true;
        } else {
            System.out.println("Payment not confirmed. Booking canceled.");
            cancelBooking(roomNumber);
            return false;
        }
    }


    private void cancelBooking(int roomNumber) {
        for (Room room : Room.getRooms()) {
            if (room.getRoomNumber() == roomNumber) {
                room.setAvailable(true);
                break;
            }
        }
    }
}

