package core;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;


public class Receipt {
    /**
     * Generates a receipt for the room booking and appends it to a single receipts file.
     */
    public static void generateReceipt(int roomNumber, RoomType type, double price, String method,Customer customer) {
        String filename = "receipts.txt";
        String timestamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        String content =
                "-------------------------\n" +
                        "  HOTEL BOOKING RECEIPT \n" +
                        "   " + timestamp + "\n" +
                        "-------------------------\n" +
                        "Room Number : " + roomNumber + "\n" +
                        "Room Type   : " + type + "\n" +
                        "Price       : " + price + " USD\n" +
                        "Payment     : " + method + "\n" +
                        "Status      : Confirmed\n" +
                        "-------------------------\n" +
                        "Customer Info : " + "\n" + customer + "\n" +
                        "Thank you for staying with us!\n\n";

        try (FileWriter fileWriter = new FileWriter(filename, true);
             BufferedWriter bufferedWriter = new BufferedWriter(fileWriter)) {
            bufferedWriter.write(content);
            System.out.println("Receipt added to: " + filename);
        } catch (IOException e) {
            System.out.println("Error generating receipt: " + e.getMessage());
        }
    }

}