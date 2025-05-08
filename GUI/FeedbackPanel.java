package GUI;
import core.*;
import javax.swing.*;
import java.awt.*;
import java.io.FileWriter;
import java.io.IOException;


public class FeedbackPanel extends JPanel {
    private JTextField passportField;
    private JComboBox<Integer> ratingCombo;
    private JTextArea commentsArea;
    private JButton submitButton;
    private boolean isLoyalCustomer = false;

    public FeedbackPanel(HotelGUI gui) {
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.WEST;


        gbc.gridx = 0;
        gbc.gridy = 0;
        add(new JLabel("Passport Number:"), gbc);
        gbc.gridx = 1;
        passportField = new JTextField(20);
        passportField.addActionListener(e -> verifyLoyalCustomer());
        add(passportField, gbc);


        gbc.gridx = 0;
        gbc.gridy = 1;
        add(new JLabel("Rating (1-5):"), gbc);
        gbc.gridx = 1;
        ratingCombo = new JComboBox<>(new Integer[]{1, 2, 3, 4, 5});
        ratingCombo.setEnabled(false);
        add(ratingCombo, gbc);


        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        add(new JLabel("Comments:"), gbc);
        gbc.gridy = 3;
        commentsArea = new JTextArea(5, 25);
        commentsArea.setLineWrap(true);
        commentsArea.setWrapStyleWord(true);
        commentsArea.setEnabled(false);
        JScrollPane scrollPane = new JScrollPane(commentsArea);
        add(scrollPane, gbc);


        gbc.gridy = 4;
        gbc.gridwidth = 1;
        gbc.gridx = 0;
        submitButton = new JButton("Submit Feedback");
        submitButton.setEnabled(false);
        submitButton.addActionListener(e -> saveFeedback());
        add(submitButton, gbc);


        gbc.gridx = 1;
        JButton backButton = new JButton("Back to Main Menu");
        backButton.addActionListener(e -> gui.showPanel("MainMenu"));
        add(backButton, gbc);
    }

    private void verifyLoyalCustomer() {
        String passport = passportField.getText().trim();
        if (passport.isEmpty()) {
            return;
        }


        boolean isRoomLoyal = Room.getLoyalCustomers().stream()
                .anyMatch(c -> c.getPassportNumber().equalsIgnoreCase(passport));

        boolean isGymLoyal = Gym.loyalCustomers.stream()
                .anyMatch(c -> c.getPassportNumber().equalsIgnoreCase(passport));

        boolean isEventLoyal = EventReservation.reservations.stream()
                .anyMatch(er -> er.customer != null &&
                        er.customer.getPassportNumber().equalsIgnoreCase(passport));

        isLoyalCustomer = isRoomLoyal || isGymLoyal || isEventLoyal;

        if (isLoyalCustomer) {
            ratingCombo.setEnabled(true);
            commentsArea.setEnabled(true);
            submitButton.setEnabled(true);
            JOptionPane.showMessageDialog(this, "Verified loyal customer. Thank you for your feedback!");
        } else {
            ratingCombo.setEnabled(false);
            commentsArea.setEnabled(false);
            submitButton.setEnabled(false);
            JOptionPane.showMessageDialog(this,
                    "Only loyal customers can submit feedback.\n" +
                            "Please book a service first to become a loyal customer.",
                    "Access Denied",
                    JOptionPane.WARNING_MESSAGE);
            clearFields();
        }
    }

    private void saveFeedback() {
        String passport = passportField.getText().trim();
        int rating = (int) ratingCombo.getSelectedItem();
        String comments = commentsArea.getText().trim();

        if (comments.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please provide your feedback comments");
            return;
        }

        try (FileWriter writer = new FileWriter("feedback.txt", true)) {
            writer.write("Loyal Customer Passport: " + passport + "\n");
            writer.write("Rating: " + rating + "/5\n");
            writer.write("Comments: " + comments + "\n");
            writer.write("------\n");

            JOptionPane.showMessageDialog(this, "Thank you for your feedback!");
            clearFields();
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, "Failed to save feedback: " + ex.getMessage());
        }
    }

    private void clearFields() {
        passportField.setText("");
        ratingCombo.setSelectedIndex(4);
        commentsArea.setText("");
        ratingCombo.setEnabled(false);
        commentsArea.setEnabled(false);
        submitButton.setEnabled(false);
        isLoyalCustomer = false;
    }
}