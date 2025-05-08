package GUI;
import core.*;
import javax.swing.*;
import java.awt.*;


public class GuestInfoDialog extends JDialog {
    private JTextField nameField, surnameField, emailField, phoneField, passportField;
    private boolean submitted = false;

    public GuestInfoDialog(Frame parent, String title) {
        super(parent, title, true);
        setSize(400, 400);
        setLayout(new GridLayout(7, 2, 5, 5));

        add(new JLabel("Name:"));
        nameField = new JTextField();
        add(nameField);

        add(new JLabel("Surname:"));
        surnameField = new JTextField();
        add(surnameField);

        add(new JLabel("Email:"));
        emailField = new JTextField();
        add(emailField);

        add(new JLabel("Phone (+country):"));
        phoneField = new JTextField();
        add(phoneField);

        add(new JLabel("Passport:"));
        passportField = new JTextField();
        add(passportField);

        JButton cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(e -> dispose());
        add(cancelButton);

        JButton submitButton = new JButton("Submit");
        submitButton.addActionListener(e -> {
            if (validateInputs()) {
                submitted = true;
                dispose();
            }
        });
        add(submitButton);

        setLocationRelativeTo(parent);
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
            JOptionPane.showMessageDialog(this, "Name must be at least 2 characters long.");
            return false;
        }


        if (surname.length() < 2) {
            JOptionPane.showMessageDialog(this, "Surname must be at least 2 characters long.");
            return false;
        }


        if (!email.contains("@") || email.length() < 5) {
            JOptionPane.showMessageDialog(this, "Please enter a valid email address (must include '@' and be at least 5 characters).");
            return false;
        }


        if (!phone.matches("\\+?\\d{8,15}")) {
            JOptionPane.showMessageDialog(this, "Phone number must be 8 to 15 digits, optionally starting with '+'.");
            return false;
        }


        if (passport.length() < 5 ||
                !passport.matches(".*[A-Z].*") ||
                !passport.matches(".*\\d.*")) {
            JOptionPane.showMessageDialog(this, "Passport must be at least 5 characters and include both an uppercase letter and a digit.");
            return false;
        }

        return true;
    }

}