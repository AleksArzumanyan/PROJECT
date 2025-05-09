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

    private final Color MAIN_BG = new Color(9, 32, 63);
    private final Color PANEL_BG = new Color(32, 58, 67);
    private final Color BUTTON_BG = new Color(76, 175, 80);
    private final Color TEXT_COLOR = Color.WHITE;
    private final Color INPUT_BG = new Color(240, 248, 255);

    public FeedbackPanel(HotelGUI gui) {
        setLayout(new BorderLayout(15, 15));
        setBackground(MAIN_BG);
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));


        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(PANEL_BG);
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;


        gbc.gridx = 0;
        gbc.gridy = 0;
        addStyledLabel(formPanel, "Passport Number:", gbc);
        gbc.gridx = 1;
        passportField = createStyledTextField(20);
        passportField.addActionListener(e -> verifyLoyalCustomer());
        formPanel.add(passportField, gbc);


        gbc.gridx = 0;
        gbc.gridy = 1;
        addStyledLabel(formPanel, "Rating (1-5):", gbc);
        gbc.gridx = 1;
        ratingCombo = new JComboBox<>(new Integer[]{1, 2, 3, 4, 5});
        styleComboBox(ratingCombo);
        ratingCombo.setEnabled(false);
        formPanel.add(ratingCombo, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        addStyledLabel(formPanel, "Comments:", gbc);
        gbc.gridy = 3;
        commentsArea = new JTextArea(5, 25);
        styleTextArea(commentsArea);
        commentsArea.setEnabled(false);
        JScrollPane scrollPane = new JScrollPane(commentsArea);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        formPanel.add(scrollPane, gbc);


        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        buttonPanel.setBackground(PANEL_BG);
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        submitButton = new JButton("Submit Feedback");
        styleInvisibleButton(submitButton);
        submitButton.setEnabled(false);
        submitButton.addActionListener(e -> saveFeedback());
        buttonPanel.add(submitButton);

        JButton backButton = new JButton("Back to Main Menu");
        styleInvisibleButton(backButton);
        backButton.addActionListener(e -> gui.showPanel("MainMenu"));
        buttonPanel.add(backButton);

        add(formPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    private void addStyledLabel(JPanel panel, String text, GridBagConstraints gbc) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Segoe UI", Font.BOLD, 14));
        label.setForeground(TEXT_COLOR);
        panel.add(label, gbc);
    }

    private JTextField createStyledTextField(int columns) {
        JTextField field = new JTextField(columns);
        field.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        field.setBackground(INPUT_BG);
        field.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.WHITE, 1),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)
        ));
        return field;
    }

    private void styleComboBox(JComboBox<?> comboBox) {
        comboBox.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        comboBox.setBackground(INPUT_BG);
        comboBox.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.WHITE, 1),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)
        ));
    }

    private void styleTextArea(JTextArea textArea) {
        textArea.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        textArea.setBackground(INPUT_BG);
        textArea.setForeground(Color.BLACK);
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
        textArea.setBorder(BorderFactory.createCompoundBorder(
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
            showStyledMessage("Verified loyal customer. Thank you for your feedback!", "Success");
        } else {
            ratingCombo.setEnabled(false);
            commentsArea.setEnabled(false);
            submitButton.setEnabled(false);
            showStyledMessage(
                    "Only loyal customers can submit feedback.\n" +
                            "Please book a service first to become a loyal customer.",
                    "Access Denied",
                    JOptionPane.WARNING_MESSAGE);
            clearFields();
        }
    }

    private void showStyledMessage(String message, String title) {
        showStyledMessage(message, title, JOptionPane.INFORMATION_MESSAGE);
    }

    private void showStyledMessage(String message, String title, int messageType) {
        JOptionPane.showMessageDialog(this,
                "<html><div style='font-size:14px;'>" + message + "</div></html>",
                title,
                messageType);
    }

    private void saveFeedback() {
        String passport = passportField.getText().trim();
        int rating = (int) ratingCombo.getSelectedItem();
        String comments = commentsArea.getText().trim();

        if (comments.isEmpty()) {
            showStyledMessage("Please provide your feedback comments", "Validation Error");
            return;
        }

        try (FileWriter writer = new FileWriter("feedback.txt", true)) {
            writer.write("Loyal Customer Passport: " + passport + "\n");
            writer.write("Rating: " + rating + "/5\n");
            writer.write("Comments: " + comments + "\n");
            writer.write("------\n");

            showStyledMessage("Thank you for your feedback!", "Success");
            clearFields();
        } catch (IOException ex) {
            showStyledMessage("Failed to save feedback: " + ex.getMessage(), "Error");
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
