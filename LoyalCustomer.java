/**
 * The LoyalCustomer class extends Customer and tracks loyalty points.
 */
class LoyalCustomer extends Customer {
    private int loyaltyPoints;

    public LoyalCustomer(String name, String email, String phoneNumber) {
        super(name, email, phoneNumber);
        this.loyaltyPoints = 0;
    }

    public void addPoints(int points) {
        this.loyaltyPoints += points;
    }

    public int getPoints() {
        return loyaltyPoints;
    }
}
