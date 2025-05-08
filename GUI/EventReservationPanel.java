package GUI;
import core.*;

import javax.swing.*;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Date;


public class EventReservationPanel extends JPanel {
    private JComboBox<EventReservation.EventPackageType> packageCombo;
    private JTextField dateField;
    private JTextField timeField;
    private HotelGUI gui;

    public EventReservationPanel(HotelGUI gui) {
        this.gui = gui;
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.WEST;

        gbc.gridx = 0;
        gbc.gridy = 0;
        add(new JLabel("Event Package:"), gbc);

        packageCombo = new JComboBox<>(EventReservation.EventPackageType.values());
        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        add(packageCombo, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        add(new JLabel("Date (yyyy-MM-dd):"), gbc);

        dateField = new JTextField(10);
        gbc.gridx = 1;
        add(dateField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        add(new JLabel("Time (HH:mm):"), gbc);

        timeField = new JTextField(5);
        gbc.gridx = 1;
        add(timeField, gbc);

        JButton bookButton = new JButton("Book Event");
        bookButton.addActionListener(e -> bookEvent());
        JButton backButton = new JButton("Back to Main Menu");
        backButton.addActionListener(e -> gui.showPanel("MainMenu"));

        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.CENTER;
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(backButton);
        buttonPanel.add(bookButton);
        add(buttonPanel, gbc);
    }

    private void bookEvent() {

        Date eventDate;
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            sdf.setLenient(false);
            eventDate = sdf.parse(dateField.getText());
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Invalid date format. Please use yyyy-MM-dd");
            return;
        }

        // Validate time
        if (!timeField.getText().matches("^(?:[01]\\d|2[0-3]):(?:[0-5]\\d)$")) {
            JOptionPane.showMessageDialog(this, "Invalid time format. Please use HH:mm");
            return;
        }

        GuestInfoDialog guestDialog = new GuestInfoDialog(gui.getMainFrame(), "Guest Information");
        guestDialog.setVisible(true);

        if (!guestDialog.isSubmitted()) return;

        LoyalCustomer customer = guestDialog.getCustomer();
        if (customer == null) return;

        boolean isReturning = Room.getLoyalCustomers().stream()
                .anyMatch(lc -> lc.getPassportNumber().equals(customer.getPassportNumber()));

        if (!isReturning) {
            Room.getLoyalCustomers().add(customer);
        }

        // Get package and calculate price
        EventReservation.EventPackageType packageType =
                (EventReservation.EventPackageType)packageCombo.getSelectedItem();
        double basePrice = packageType.getPrice();
        double finalPrice = isReturning ? basePrice * 0.9 : basePrice;

        PaymentDialog paymentDialog = new PaymentDialog(gui.getMainFrame(), finalPrice);
        paymentDialog.setVisible(true);

        if (paymentDialog.isPaymentSuccessful()) {
            EventReservation event = new EventReservation();
            event.packageType = packageType;
            event.eventDate = eventDate;
            event.eventTime = timeField.getText();
            event.customer = customer;
            event.isBooked = true;
            EventReservation.reservations.add(event);
            EventReceipt.generateEventReceipt(event, finalPrice);

            JOptionPane.showMessageDialog(this,
                    "Event booked successfully!\n" +
                            "Package: " + packageType + "\n" +
                            "Date: " + new SimpleDateFormat("yyyy-MM-dd").format(eventDate) + "\n" +
                            "Time: " + timeField.getText() + "\n" +
                            "Price: $" + finalPrice + "\n" +
                            "Event ID: " + event.getEventId()+ "\n" +
                            "Receipt was successfully generated",
                    "Booking Confirmed",
                    JOptionPane.INFORMATION_MESSAGE);

            dateField.setText("");
            timeField.setText("");
        } else {
            JOptionPane.showMessageDialog(this,
                    "Booking cancelled",
                    "Cancelled",
                    JOptionPane.WARNING_MESSAGE);
        }
    }
}