package GUI;
import core.*;
import javax.swing.*;
import java.awt.*;

public class GymBookingPanel extends JPanel {
    private final Color MAIN_BG = new Color(9, 32, 63);
    private final Color PANEL_BG = new Color(32, 58, 67);
    private final Color BUTTON_BG = new Color(76, 175, 80);
    private final Color TEXT_COLOR = Color.WHITE;

    private ButtonGroup group;
    private JRadioButton withPoolBtn, withoutPoolBtn;
    private JComboBox<String> durationCombo;
    private HotelGUI gui;

    public GymBookingPanel(HotelGUI gui) {
        this.gui = gui;
        setLayout(new GridBagLayout());
        setBackground(MAIN_BG);
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.WEST;


        JLabel optionsLabel = new JLabel("Gym Options:");
        optionsLabel.setForeground(TEXT_COLOR);
        optionsLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        gbc.gridx = 0; gbc.gridy = 0;
        add(optionsLabel, gbc);

        withPoolBtn = createStyledRadioButton("With Pool", true);
        withoutPoolBtn = createStyledRadioButton("Without Pool", false);

        group = new ButtonGroup();
        group.add(withPoolBtn);
        group.add(withoutPoolBtn);

        gbc.gridy = 1; add(withPoolBtn, gbc);
        gbc.gridy = 2; add(withoutPoolBtn, gbc);


        JLabel durationLabel = new JLabel("Duration:");
        durationLabel.setForeground(TEXT_COLOR);
        durationLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        gbc.gridy = 3; add(durationLabel, gbc);

        String[] durations = {"1 Day", "1 Month", "1 Year"};
        durationCombo = new JComboBox<>(durations);
        styleComboBox(durationCombo);
        gbc.gridy = 4; add(durationCombo, gbc);


        JButton submitBtn = new JButton("Book Now");
        styleInvisibleButton(submitBtn);
        submitBtn.addActionListener(e -> handleGymBooking());

        JButton backBtn = new JButton("Back");
        styleInvisibleButton(backBtn);
        backBtn.addActionListener(e -> gui.showPanel("MainMenu"));

        gbc.gridy = 5;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridwidth = 1;
        add(backBtn, gbc);
        gbc.gridx = 1;
        add(submitBtn, gbc);
    }

    private JRadioButton createStyledRadioButton(String text, boolean selected) {
        JRadioButton button = new JRadioButton(text, selected);
        button.setBackground(PANEL_BG);
        button.setForeground(TEXT_COLOR);
        button.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        button.setOpaque(false);
        button.setFocusPainted(false);
        return button;
    }

    private void styleComboBox(JComboBox<?> comboBox) {
        comboBox.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        comboBox.setBackground(Color.WHITE);
        comboBox.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.WHITE, 1),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)
        ));
    }

    private void styleInvisibleButton(JButton button) {
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
