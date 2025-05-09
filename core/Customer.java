package core;
import java.util.ArrayList;

public class Customer {
    protected String guestID;
    protected String name;
    protected String surname;
    protected String email;
    protected String phoneNumber;
    protected String passportNumber;
    private static ArrayList<String> existingGuestIDs = new ArrayList<>();
    private static int counter = 1;

    public Customer(String name,String surname, String email, String phoneNumber, String passportNumber) {
        this.guestID = generateUniqueGuestID(name);
        this.name = name;
        this.surname = surname;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.passportNumber = passportNumber;
    }

    private String generateUniqueGuestID(String name) {
        String baseID = name.substring(0, 2).toUpperCase();
        String numberPart;
        String newGuestID;

        do {
            numberPart = String.format("%08d", counter);
            newGuestID = baseID + numberPart;
            counter++;
        } while (existingGuestIDs.contains(newGuestID));

        existingGuestIDs.add(newGuestID);
        return newGuestID;
    }

    public String getGuestID() {
        return guestID;
    }

    public String getName() {
        return name;
    }
    public String getSurname() {
        return surname;
    }

    public String getEmail() {
        return email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getPassportNumber() {
        return passportNumber;
    }


    public void setName(String name) {
        this.name = name;
    }
    public void setSurName(String surname) {
        this.surname = surname;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String toString() {
        return "GuestID: " + guestID + "\n" + "Name: " + name +"\n" + "Surname: " + surname + "\n" +"Email: " + email +"\n" + "Phone: +" + phoneNumber + "\n" + "Passport: " + passportNumber +"\n" + "LoyalityPoints: ";
    }
}
