public class LoyalCustomer extends Customer {

    public LoyalCustomer(String name, String surname, String email, String phoneNumber, String passportNumber) {
        super(name, surname, email, phoneNumber, passportNumber);
    }

    public double calculateFinalPrice(double roomPrice) {
        // Apply a 10% discount for loyal customers
        return roomPrice * 0.9;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null || getClass() != obj.getClass()) return false;
        LoyalCustomer that = (LoyalCustomer) obj;
        return getPassportNumber().equals(that.getPassportNumber());
    }

    @Override
    public String toString() {
        return "GuestID: " + guestID + "\n" + "Name: " + name +"\n" + "Surname: " + surname + "\n" +"Email: " + email +"\n" + "Phone: +" + phoneNumber + "\n" + "Passport: " + passportNumber +"\n";
    }
}
