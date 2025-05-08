package GUI;
import core.*;
import javax.swing.*;
import java.awt.*;


public class GymBookingPanel extends JPanel {
    private ButtonGroup group;
    private JRadioButton withPoolBtn, withoutPoolBtn;
    private JComboBox<String> durationCombo;
    private HotelGUI gui;

    public GymBookingPanel(HotelGUI gui) {
        this.gui = gui;
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.WEST;


        gbc.gridx = 0;
        gbc.gridy = 0;
        add(new JLabel("Gym Options:"), gbc);

        withPoolBtn = new JRadioButton("With Pool", true);
        withoutPoolBtn = new JRadioButton("Without Pool");
        group = new ButtonGroup();
        group.add(withPoolBtn);
        group.add(withoutPoolBtn);

        gbc.gridy = 1;
        add(withPoolBtn, gbc);
        gbc.gridy = 2;
        add(withoutPoolBtn, gbc);


        gbc.gridy = 3;
        add(new JLabel("Duration:"), gbc);

        String[] durations = {"1 Day", "1 Month", "1 Year"};
        durationCombo = new JComboBox<>(durations);
        gbc.gridy = 4;
        add(durationCombo, gbc);


        JButton submitBtn = new JButton("Book Now");
        submitBtn.addActionListener(e -> handleGymBooking());
        JButton backBtn = new JButton("Back");
        backBtn.addActionListener(e -> gui.showPanel("MainMenu"));

        gbc.gridy = 5;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridwidth = 1;
        add(backBtn, gbc);
        gbc.gridx = 1;
        add(submitBtn, gbc);
    }

    private void handleGymBooking() {
        GuestInfoDialog guestDialog = new GuestInfoDialog(gui.getMainFrame(), "Gym Booking Information");
        guestDialog.setVisible(true);

        if (!guestDialog.isSubmitted()) return;

        LoyalCustomer customer = guestDialog.getCustomer();
        if (customer == null) return;


        boolean isReturning = Room.getLoyalCustomers().stream()
                .anyMatch(lc -> lc.getPassportNumber().equals(customer.getPassportNumber()));

        if (!isReturning) {
            Room.getLoyalCustomers().add(customer);
        }


        int durationIndex = durationCombo.getSelectedIndex();
        double basePrice = withPoolBtn.isSelected() ?
                Gym.prices[0][durationIndex] : Gym.prices[1][durationIndex];
        double finalPrice = isReturning ? basePrice * 0.9 : basePrice;

        PaymentDialog paymentDialog = new PaymentDialog(gui.getMainFrame(), finalPrice);
        paymentDialog.setVisible(true);

        if (paymentDialog.isPaymentSuccessful()) {
            GymReceipt.generateGymReceipt(finalPrice, customer);

            JOptionPane.showMessageDialog(this,
                    "Gym booking successful!\n" +
                            "Duration: " + durationCombo.getSelectedItem() + "\n" +
                            (withPoolBtn.isSelected() ? "With Pool" : "Without Pool") + "\n" +
                            "Price: $" + finalPrice,
                    "Booking Confirmed",
                    JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(this,
                    "Gym booking cancelled",
                    "Cancelled",
                    JOptionPane.WARNING_MESSAGE);
        }
    }
}