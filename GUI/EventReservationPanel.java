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

    private final Color MAIN_BG = new Color(9, 32, 63);
    private final Color PANEL_BG = new Color(32, 58, 67);
    private final Color BUTTON_BG = new Color(76, 175, 80);
    private final Color TEXT_COLOR = Color.WHITE;
    private final Color FIELD_BG = Color.WHITE;
    private final Color FIELD_FG = Color.BLACK;

    public EventReservationPanel(HotelGUI gui) {
        this.gui = gui;
        setLayout(new BorderLayout(15, 15));
        setBackground(MAIN_BG);
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));


        JPanel inputPanel = new JPanel(new GridBagLayout());
        inputPanel.setBackground(PANEL_BG);
        inputPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;


        JLabel packageLabel = new JLabel("Event Package:");
        packageLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        packageLabel.setForeground(TEXT_COLOR);
        gbc.gridx = 0;
        gbc.gridy = 0;
        inputPanel.add(packageLabel, gbc);

        packageCombo = new JComboBox<>(EventReservation.EventPackageType.values());
        styleComboBox(packageCombo);
        gbc.gridx = 1;
        inputPanel.add(packageCombo, gbc);


        JLabel dateLabel = new JLabel("Date (yyyy-MM-dd):");
        dateLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        dateLabel.setForeground(TEXT_COLOR);
        gbc.gridx = 0;
        gbc.gridy = 1;
        inputPanel.add(dateLabel, gbc);

        dateField = new JTextField(15);
        styleTextField(dateField);
        gbc.gridx = 1;
        inputPanel.add(dateField, gbc);


        JLabel timeLabel = new JLabel("Time (HH:mm):");
        timeLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        timeLabel.setForeground(TEXT_COLOR);
        gbc.gridx = 0;
        gbc.gridy = 2;
        inputPanel.add(timeLabel, gbc);

        timeField = new JTextField(5);
        styleTextField(timeField);
        gbc.gridx = 1;
        inputPanel.add(timeField, gbc);


        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 10));
        buttonPanel.setBackground(PANEL_BG);
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JButton backButton = new JButton("Back to Main Menu");
        styleButton(backButton);
        backButton.addActionListener(e -> gui.showPanel("MainMenu"));

        JButton bookButton = new JButton("Book Event");
        styleButton(bookButton);
        bookButton.addActionListener(e -> bookEvent());

        buttonPanel.add(backButton);
        buttonPanel.add(bookButton);

        add(inputPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    private void styleComboBox(JComboBox<?> comboBox) {
        comboBox.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        comboBox.setBackground(FIELD_BG);
        comboBox.setForeground(FIELD_FG);
        comboBox.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.WHITE, 1),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)
        ));
    }

    private void styleTextField(JTextField textField) {
        textField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        textField.setBackground(FIELD_BG);
        textField.setForeground(FIELD_FG);
        textField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.WHITE, 1),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)
        ));
    }

    private void styleButton(JButton button) {
        button.setFont(new Font("Segoe UI", Font.BOLD, 14));
        button.setBackground(BUTTON_BG);
        button.setForeground(TEXT_COLOR);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.WHITE, 1),
                BorderFactory.createEmptyBorder(8, 20, 8, 20)
        ));
        button.setContentAreaFilled(false);
        button.setOpaque(true);
    }

    private void bookEvent() {

        Date eventDate;
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            sdf.setLenient(false);
            eventDate = sdf.parse(dateField.getText());
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Invalid date format. Please use yyyy-MM-dd",
                    "Invalid Date",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }


        if (!timeField.getText().matches("^(?:[01]\\d|2[0-3]):(?:[0-5]\\d)$")) {
            JOptionPane.showMessageDialog(this,
                    "Invalid time format. Please use HH:mm",
                    "Invalid Time",
                    JOptionPane.ERROR_MESSAGE);
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
                    "<html><div style='font-size:14px;'>" +
                            "Event booked successfully!<br><br>" +
                            "Package: <b>" + packageType + "</b><br>" +
                            "Date: " + new SimpleDateFormat("yyyy-MM-dd").format(eventDate) + "<br>" +
                            "Time: " + timeField.getText() + "<br>" +
                            "Price: <b>$" + String.format("%.2f", finalPrice) + "</b><br>" +
                            "Event ID: " + event.getEventId() + "<br><br>" +
                            "Receipt was successfully generated" +
                            "</div></html>",
                    "Booking Confirmed",
                    JOptionPane.INFORMATION_MESSAGE);

            dateField.setText("");
            timeField.setText("");
        } else {
            JOptionPane.showMessageDialog(this,
                    "Booking was cancelled",
                    "Cancelled",
                    JOptionPane.WARNING_MESSAGE);
        }
    }
}
