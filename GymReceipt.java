import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class GymReceipt {
    /**
     * Generates and appends a receipt for a gym booking.
     */
    public static void generateGymReceipt(double price, LoyalCustomer customer) {
        String filename = "gym_receipts.txt";
        String timestamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());

        String content =
                "-------------------------\n" +
                        "     GYM BOOKING RECEIPT\n" +
                        "     " + timestamp + "\n" +
                        "-------------------------\n" +
                        String.format("Price       : %.2f USD\n", price) +
                        "Status      : Confirmed\n" +
                        "-------------------------\n" +
                        "Customer Info:\n" +
                        "Name        : " + customer.getName() + " " + customer.getSurname() + "\n" +
                        "Email       : " + customer.getEmail() + "\n" +
                        "Phone       : +" + customer.getPhoneNumber() + "\n" +
                        "Passport    : " + customer.getPassportNumber() + "\n" +
                        "Thank you for choosing our gym!\n\n";

        try (FileWriter writer = new FileWriter(filename, true);
             BufferedWriter bufferedWriter = new BufferedWriter(writer)) {
            bufferedWriter.write(content);
            System.out.println("Receipt added to: " + filename);
        } catch (IOException e) {
            System.out.println("Error generating gym receipt: " + e.getMessage());
        }
    }
}
