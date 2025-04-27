import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;


public class Receipt {
    /**
     * Generates a receipt for the room booking and appends it to a single receipts file.
     */
    public static void generateReceipt(int roomNumber, RoomType type, double price, String method) {
        String filename = "receipts.txt";
        DateFormat data= new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date();
        String data1 = data.format(date);
        String content =
                "-------------------------\n" +
                        "  HOTEL BOOKING RECEIPT \n" +
                        "   " + data1 + "\n" +
                        "-------------------------\n" +
                        "Room Number : " + roomNumber + "\n" +
                        "Room Type   : " + type + "\n" +
                        "Price       : " + price + " USD\n" +
                        "Payment     : " + method + "\n" +
                        "Status      : Confirmed\n" +
                        "-------------------------\n" +
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
