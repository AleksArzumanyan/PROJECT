/**
 * The Feedback class represents a feedback entry left by a customer.
 */
public class Feedback {
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
}
