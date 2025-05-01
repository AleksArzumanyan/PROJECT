import java.util.ArrayList;
import java.util.Random;

public class Customer {
    private String guestID;
    private String name;
    private String email;
    private String phoneNumber;
    private static ArrayList<String> existingGuestIDs = new ArrayList<>();

    /**
     * Constructs a Customer with a unique guestID, name, email, and phone number.
     */
    public Customer(String name, String email, String phoneNumber) {
        if (name.length() < 2) {
            throw new IllegalArgumentException("Name must have at least 2 characters.");
        }

        this.guestID = generateUniqueGuestID(name);  // Generate unique guestID
        this.name = name;
        this.email = email;
        this.phoneNumber = phoneNumber;
    }

    /**
     * Generates a unique guestID based on the name (first two uppercase letters) and 8 random digits.
     */
    private String generateUniqueGuestID(String name) {
        String baseID = name.substring(0, 2).toUpperCase();  // Get first two uppercase letters of name
        String randomDigits = generateRandomDigits();         // Generate 8 random digits
        String newGuestID = baseID + randomDigits;             // Combine to form the guestID

        // Ensure the ID is unique by checking against existing IDs
        while (existingGuestIDs.contains(newGuestID)) {
            randomDigits = generateRandomDigits();  // Generate new random digits
            newGuestID = baseID + randomDigits;     // Form new guestID
        }

        // Add the new unique guestID to the list of existing IDs
        existingGuestIDs.add(newGuestID);

        return newGuestID;
    }


    private String generateRandomDigits() {
        Random rand = new Random();
        String digits = "";
        for (int i = 0; i < 8; i++) {
            digits += rand.nextInt(10); // Append random digit (0-9)
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
