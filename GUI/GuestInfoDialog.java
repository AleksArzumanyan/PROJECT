package GUI;
import core.*;
import javax.swing.*;
import java.awt.*;

public class GuestInfoDialog extends JDialog {
    private JTextField nameField, surnameField, emailField, phoneField, passportField;
    private boolean submitted = false;

    private final Color MAIN_BG = new Color(9, 32, 63);
    private final Color PANEL_BG = new Color(32, 58, 67);
    private final Color BUTTON_BG = new Color(76, 175, 80);
    private final Color TEXT_COLOR = Color.WHITE;

    public GuestInfoDialog(Frame parent, String title) {
        super(parent, title, true);
        setSize(450, 450);
        setLocationRelativeTo(parent);

        // Main container
        JPanel mainPanel = new JPanel(new BorderLayout(15, 15));
        mainPanel.setBackground(MAIN_BG);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Form panel
        JPanel formPanel = new JPanel(new GridLayout(5, 2, 10, 15));
        formPanel.setBackground(PANEL_BG);
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));


        addFormField(formPanel, "Name:", nameField = new JTextField());
        addFormField(formPanel, "Surname:", surnameField = new JTextField());
        addFormField(formPanel, "Email:", emailField = new JTextField());
        addFormField(formPanel, "Phone (+country):", phoneField = new JTextField());
        addFormField(formPanel, "Passport:", passportField = new JTextField());

        mainPanel.add(formPanel, BorderLayout.CENTER);


        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 10));
        buttonPanel.setBackground(PANEL_BG);
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JButton cancelButton = new JButton("Cancel");
        styleInvisibleButton(cancelButton);
        cancelButton.addActionListener(e -> dispose());
        buttonPanel.add(cancelButton);

        JButton submitButton = new JButton("Submit");
        styleInvisibleButton(submitButton);
        submitButton.addActionListener(e -> {
            if (validateInputs()) {
                submitted = true;
                dispose();
            }
        });
        buttonPanel.add(submitButton);

        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        add(mainPanel);
    }

    private void addFormField(JPanel panel, String labelText, JTextField textField) {
        JLabel label = new JLabel(labelText);
        label.setFont(new Font("Segoe UI", Font.BOLD, 14));
        label.setForeground(TEXT_COLOR);
        panel.add(label);

        textField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        textField.setBackground(Color.WHITE);
        textField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.WHITE, 1),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)
        ));
        panel.add(textField);
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

    public boolean isSubmitted() {
        return submitted;
    }

    public LoyalCustomer getCustomer() {
        if (!submitted) return null;
        return new LoyalCustomer(
                nameField.getText(),
                surnameField.getText(),
                emailField.getText(),
                phoneField.getText(),
                passportField.getText()
        );
    }

    private boolean validateInputs() {
        String name = nameField.getText().trim();
        String surname = surnameField.getText().trim();
        String email = emailField.getText().trim();
        String phone = phoneField.getText().trim();
        String passport = passportField.getText().trim();

        if (name.length() < 2) {
            showValidationError("Name must be at least 2 characters long.");
            return false;
        }

        if (surname.length() < 2) {
            showValidationError("Surname must be at least 2 characters long.");
            return false;
        }

        if (!email.contains("@") || email.length() < 5) {
            showValidationError("Please enter a valid email address (must include '@' and be at least 5 characters).");
            return false;
        }

        if (!phone.matches("\\+?\\d{8,15}")) {
            showValidationError("Phone number must be 8 to 15 digits, optionally starting with '+'.");
            return false;
        }

        if (passport.length() < 5 ||
                !passport.matches(".*[A-Z].*") ||
                !passport.matches(".*\\d.*")) {
            showValidationError("Passport must be at least 5 characters and include both an uppercase letter and a digit.");
            return false;
        }

        return true;
    }

    private void showValidationError(String message) {
        JOptionPane.showMessageDialog(this,
                "<html><div style='font-size:14px;'>" + message + "</div></html>",
                "Validation Error",
                JOptionPane.ERROR_MESSAGE);
    }
}
