package core;
public class LoyalCustomer extends Customer {

    public LoyalCustomer(String name, String surname, String email, String phoneNumber, String passportNumber) {
        super(name, surname, email, phoneNumber, passportNumber);
    }

    public double calculateFinalPrice(double roomPrice) {

        return roomPrice * 0.9;
    }

    
    public boolean equals(Object obj) {
        if (obj == null || getClass() != obj.getClass()) return false;
        LoyalCustomer that = (LoyalCustomer) obj;
        return getPassportNumber().equals(that.getPassportNumber());
    }


    public String toString() {
        return "Customer is loyal\n" + "GuestID: " + guestID + "\n" + "Name: " + name +"\n" + "Surname: " + surname + "\n" +"Email: " + email +"\n" + "Phone: +" + phoneNumber + "\n" + "Passport: " + passportNumber +"\n";
    }
}
