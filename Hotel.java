import java.util.InputMismatchException;
import java.util.Scanner;

public class Hotel {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        boolean running = true;

        while (running) {
            System.out.println("\nWelcome to the Hotel Booking System!");
            System.out.println("What would you like to do?");
            System.out.println("1. Book a Room");
            System.out.println("2. Book the Gym");
            System.out.println("3. Book the Restaurant");
            System.out.println("4. Book an Event Hall");
            System.out.println("5. Leave Feedback");
            System.out.println("6. Show Rating Summary");
            System.out.println("7. Exit");

            System.out.print("Enter your choice (1-7): ");

            try {
                int choice = scanner.nextInt();
                scanner.nextLine();

                switch (choice) {
                    case 1:
                        Room.startBookingSystem();
                        break;

                    case 2:
                        Gym.startGymBooking();
                        break;

                    case 3:
                        TableReservation.startReservationSystem();
                        break;

                    case 4:
                        EventReservation.startEventReservationSystem();
                        break;

                    case 5:
                        Feedback.leaveFeedback(scanner);
                        break;

                    case 6:
                        Feedback.showRatingSummary();
                        break;

                    case 7:
                        running = false;
                        System.out.println("Thank you for using our hotel system!");
                        break;

                    default:
                        System.out.println("Invalid choice. Please enter a number between 1 and 7.");
                }
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter a number between 1 and 7.");
                scanner.nextLine(); 
            } catch (Exception e) {
                System.out.println("An unexpected error occurred: " + e.getMessage());
            }
        }
        scanner.close();
    }
}
