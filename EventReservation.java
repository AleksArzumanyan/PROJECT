import java.util.*;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Handles event reservations at the hotel.
 */
public class EventReservation {
    private static final List<EventReservation> reservations = new ArrayList<>();
    private static int counter = 1;

    /**
     * Types of event packages offered by the hotel.
     */

    public enum EventPackageType {
        BASIC(1000, "Up to 50 guests, basic decor"),
        SILVER(3000, "Up to 100 guests, standard decor + sound system"),
        GOLD(5000, "Up to 200 guests, premium decor + sound + lighting"),
        PLATINUM(10000, "Unlimited guests, full VIP service + catering");

        private final double price;
        private final String description;

        EventPackageType(double price, String description) {
            this.price = price;
            this.description = description;
        }

        public double getPrice() {
            return price;
        }

        public String getDescription() {
            return description;
        }

        @Override
        public String toString() {
            return name() + " (" + description + ") - $" + price;
        }
    }

    private final String eventId;
    private EventPackageType packageType;
    private Date eventDate;
    private String eventTime;
    private LoyalCustomer customer;
    private boolean isBooked;

    public EventReservation() {
        this.eventId = String.format("EVT-%04d", counter++);
        this.isBooked = false;
    }

    public void bookEvent(Scanner scanner) {
        System.out.println("Available Packages:");
        EventPackageType[] types = EventPackageType.values();
        for (int i = 0; i < types.length; i++) {
            System.out.println((i + 1) + ". " + types[i]);
        }
        System.out.print("Choose package (1-" + types.length + "): ");
        int choice;
        try {
            choice = scanner.nextInt();
            scanner.nextLine();
            if (choice < 1 || choice > types.length) {
                System.out.println("Invalid package choice.");
                return;
            }
        } catch (InputMismatchException e) {
            System.out.println("Invalid input.");
            scanner.nextLine();
            return;
        }
        this.packageType = types[choice - 1];

        System.out.print("Enter event date (yyyy-MM-dd): ");
        String dateStr = scanner.nextLine();
        System.out.print("Enter event time (HH:mm): ");
        String timeStr = scanner.nextLine();
        try {
            this.eventDate = new SimpleDateFormat("yyyy-MM-dd").parse(dateStr);
            this.eventTime = timeStr;
        } catch (Exception e) {
            System.out.println("Invalid date/time format.");
            return;
        }

        System.out.print("Enter customer passport number: ");
        String passport = scanner.nextLine().trim();
        LoyalCustomer existing = null;
        for (LoyalCustomer lc : Room.getLoyalCustomers()) {
            if (lc.getPassportNumber().equalsIgnoreCase(passport)) {
                existing = lc;
                break;
            }
        }
        if (existing == null) {
            System.out.println("New customer. Please enter full details.");
            System.out.print("Name: ");
            String name = scanner.nextLine();
            System.out.print("Surname: ");
            String surname = scanner.nextLine();
            System.out.print("Email: ");
            String email = scanner.nextLine();
            System.out.print("Phone (+countrycode...): +");
            String phone = scanner.nextLine();
            LoyalCustomer lc = new LoyalCustomer(name, surname, email, phone, passport);
            Room.getLoyalCustomers().add(lc);
            this.customer = lc;
        } else {
            this.customer = existing;
            System.out.println("Loyal customer recognized. 10% discount will apply.");
        }

        double basePrice = this.packageType.getPrice();
        double finalPrice = Room.getLoyalCustomers().contains(this.customer)
                ? basePrice * 0.9
                : basePrice;

        System.out.printf("Total price: $%.2f\n", finalPrice);
        this.isBooked = true;
        reservations.add(this);

        EventReceipt.generateEventReceipt(this, finalPrice);
        System.out.println("Event booked with ID: " + this.eventId);
    }

    public void cancelEvent(Scanner scanner) {
        System.out.print("Enter Event ID to cancel: ");
        String id = scanner.nextLine().trim();
        Iterator<EventReservation> it = reservations.iterator();
        while (it.hasNext()) {
            EventReservation er = it.next();
            if (er.eventId.equalsIgnoreCase(id) && er.isBooked) {
                it.remove();
                System.out.println("Reservation " + id + " canceled.");
                return;
            }
        }
        System.out.println("Reservation not found or already canceled.");
    }

    public void viewAllEvents() {
        if (reservations.isEmpty()) {
            System.out.println("No event reservations yet.");
            return;
        }
        System.out.println("--- All Event Reservations ---");
        for (EventReservation er : reservations) {
            System.out.printf("ID: %s | Package: %s | Date: %s %s | Customer: %s %s | Price: $%.2f\n",
                    er.eventId,
                    er.packageType,
                    new SimpleDateFormat("yyyy-MM-dd").format(er.eventDate),
                    er.eventTime,
                    er.customer.getName(),
                    er.customer.getSurname(),
                    er.packageType.getPrice());
        }
    }

    public static void startEventReservationSystem() {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println("\n--- Event Reservation Menu ---");
            System.out.println("1. Book Event");
            System.out.println("2. Cancel Event");
            System.out.println("3. View All Events");
            System.out.println("4. Exit");
            System.out.print("Enter your choice (1-4): ");
            int choice;
            try {
                choice = scanner.nextInt();
                scanner.nextLine();
            } catch (InputMismatchException e) {
                System.out.println("Invalid input.");
                scanner.nextLine();
                continue;
            }
            EventReservation er = new EventReservation();
            switch (choice) {
                case 1:
                    er.bookEvent(scanner);
                    break;
                case 2:
                    er.cancelEvent(scanner);
                    break;
                case 3:
                    er.viewAllEvents();
                    break;
                case 4:
                    System.out.println("Exiting Event Menu.");
                    return;
                default:
                    System.out.println("Invalid choice.");
            }
        }
    }

    // Getters
    public String getEventId() { return eventId; }
    public EventPackageType getPackageType() { return packageType; }
    public Date getEventDate() { return eventDate; }
    public String getEventTime() { return eventTime; }
    public LoyalCustomer getCustomer() { return customer; }
}

/**
 * Utility class to generate event receipts.
 */
