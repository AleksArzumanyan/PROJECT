package core;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * The Feedback class represents a feedback entry left by a customer
 */
public class Feedback {
    private static ArrayList<Feedback> allFeedback = new ArrayList<>();
    public static void leaveFeedbackDirect(Feedback feedback) {
        allFeedback.add(feedback);
    }
    public static ArrayList<Feedback> getAllFeedback() {
        return allFeedback;
    }

    private Customer customer;
    private int rating;
    private String comment;

    public Feedback(Customer customer, int rating, String comment) {
        this.customer = customer;
        this.rating = rating;
        this.comment = comment;
    }

    public Customer getCustomer() {
        return customer;
    }

    public int getRating() {
        return rating;
    }

    public String getComment() {
        return comment;
    }

    @Override
    public String toString() {
        return "Feedback from " + customer.getName() +
                " (Phone: " + customer.getPhoneNumber() + ") - " +
                "Rating: " + rating + "/5, Comment: \"" + comment + "\"";
    }

    public static void leaveFeedback(Scanner scanner) {
        System.out.print("\nEnter customer's passport number:");
        String passport = scanner.nextLine();

        // Find the customer with the phone number
        Customer foundCustomer = null;
        for (Customer c : Room.loyalCustomers) {
            if (c.getPassportNumber().equals(passport)) {
                foundCustomer = c;
                break;
            }
        }

        if (foundCustomer == null) {
            System.out.println("Customer not found. Feedback can only be left for existing customers.");
            return;
        }

        if (foundCustomer instanceof LoyalCustomer) {
            System.out.println("Loyal Customer identified! Thank you for your continued support.");
        } else {
            System.out.println("Thank you for staying with us!");
        }

        int rating = 0;
        while (true) {
            System.out.print("Enter feedback rating (1 to 5): ");
            try {
                rating = Integer.parseInt(scanner.nextLine());
                if (rating >= 1 && rating <= 5) break;
                System.out.println("Rating must be between 1 and 5.");
            } catch (Exception e) {
                System.out.println("Invalid number. Try again.");
            }
        }

        System.out.print("Enter feedback comment (optional): ");
        String comment = scanner.nextLine();

        Feedback feedback = new Feedback(foundCustomer, rating, comment);
        allFeedback.add(feedback);

        System.out.println("\nFeedback saved:");
        System.out.println(feedback);
    }

    public static void showRatingSummary() {
        if (allFeedback.isEmpty()) {
            System.out.println("No feedbacks available yet.");
            return;
        }

        int[] ratingsCount = new int[5];
        int totalRatings = 0;
        double totalScore = 0;

        for (Feedback feedback : allFeedback) {
            int rating = feedback.getRating();
            if (rating >= 1 && rating <= 5) {
                ratingsCount[rating - 1]++;
                totalRatings++;
                totalScore += rating;
            }
        }

        double averageRating = totalRatings > 0 ? totalScore / totalRatings : 0;

        System.out.println("\n--- Hotel Rating Summary ---");
        System.out.println("Total Feedbacks: " + totalRatings);
        System.out.println("1 star: " + ratingsCount[0]);
        System.out.println("2 stars: " + ratingsCount[1]);
        System.out.println("3 stars: " + ratingsCount[2]);
        System.out.println("4 stars: " + ratingsCount[3]);
        System.out.println("5 stars: " + ratingsCount[4]);
        System.out.println("Average Rating: " + String.format("%.2f", averageRating));
    }
}
