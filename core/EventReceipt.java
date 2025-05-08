package core;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class EventReceipt {
    private static final String FILE_NAME = "event_receipts.txt";

    public static void generateEventReceipt(EventReservation er, double price) {
        String timestamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        String content =
                "-------------------------\n" +
                        "  EVENT BOOKING RECEIPT  \n" +
                        "  " + timestamp + "\n" +
                        "-------------------------\n" +
                        "Event ID    : " + er.getEventId() + "\n" +
                        "Package     : " + er.getPackageType() + "\n" +
                        String.format("Price       : %.2f USD\n", price) +
                        "Date & Time : " + new SimpleDateFormat("yyyy-MM-dd").format(er.getEventDate())
                        + " " + er.getEventTime() + "\n" +
                        "Customer    : " + er.getCustomer().getName() + " " + er.getCustomer().getSurname() + "\n" +
                        "-------------------------\n\n";
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(FILE_NAME, true))) {
            bw.write(content);
        } catch (IOException e) {
            System.out.println("Error writing event receipt: " + e.getMessage());
        }
    }

}