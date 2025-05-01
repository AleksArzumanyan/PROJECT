import java.util.ArrayList;
import java.util.Random;

public class Customer {
    private String guestID;
    private String name;
    private String email;
    private String phoneNumber;
    private static ArrayList<String> existingGuestIDs = new ArrayList<>();
    private static int counter=1;

    public Customer(String name, String email, String phoneNumber) {
        if (name.length() < 2) {
            throw new IllegalArgumentException("Name must have at least 2 characters.");
        }

        this.guestID = generateUniqueGuestID(name);
        this.name = name;
        this.email = email;
        this.phoneNumber = phoneNumber;
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



    private String generateRandomDigits() {
        Random rand = new Random();
        String digits = "";
        for (int i = 0; i < 8; i++) {
            digits += rand.nextInt(10);
        }
        return digits;
    }


    public String getGuestID() {
        return guestID;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    // Setters (if you want the ability to change customer info later)
    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String toString() {
        return "GuestID: " + guestID + ", Name: " + name + ", Email: " + email + ", Phone: " + phoneNumber;
    }
}
