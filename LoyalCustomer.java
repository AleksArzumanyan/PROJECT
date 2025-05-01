
/**
 * The LoyalCustomer class extends Customer and tracks loyalty points for discount eligibility.
 */
class LoyalCustomer extends Customer {
    private int loyaltyPoints;

    public LoyalCustomer(String name, String surname, String email, String phoneNumber, String passportNumber) {
        super(name, surname, email, phoneNumber, passportNumber);
        this.loyaltyPoints = 0;
    }


    public void addPoints(int points) {
        this.loyaltyPoints += points;
    }


    public int getLoyaltyPoints() {
        return loyaltyPoints;
    }


    public boolean isEligibleForDiscount() {
        return loyaltyPoints > 0;
    }


    public double calculateFinalPrice(double roomPrice) {
        double finalPrice = roomPrice;


        if (isEligibleForDiscount()) {
            finalPrice *= 0.9;
        }

        return finalPrice;
    }



    public boolean equals(Object obj) {
        if (obj == null || getClass() != obj.getClass()) return false;
        LoyalCustomer that = (LoyalCustomer) obj;
        return getPassportNumber().equals(that.getPassportNumber());
    }
    public String toString() {
        return "GuestID: " + guestID + ", Name: " + name + ", Surname: " + surname + ", Email: " + email + ", Phone: " + phoneNumber + ", Passport: " + passportNumber + ", LoyalityPoints: " + loyaltyPoints;
    }
}
