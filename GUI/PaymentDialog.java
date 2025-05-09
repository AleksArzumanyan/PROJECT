package GUI;
import javax.swing.*;
import java.awt.*;

public class PaymentDialog extends JDialog {
    private boolean paymentSuccessful = false;
    private String selectedMethod = "Credit Card";

    private final Color MAIN_BG = new Color(9, 32, 63);
    private final Color PANEL_BG = new Color(32, 58, 67);
    private final Color BUTTON_BG = new Color(76, 175, 80);
    private final Color TEXT_COLOR = Color.WHITE;
    private final Color RADIO_BG = new Color(240, 248, 255); // AliceBlue

    public PaymentDialog(Frame parent, double amount) {
        super(parent, "Payment", true);
        setLayout(new BorderLayout(15, 15));
        setSize(500, 350);
        getContentPane().setBackground(MAIN_BG);
        setLocationRelativeTo(parent);


        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        mainPanel.setBackground(PANEL_BG);


        JLabel amountLabel = new JLabel("Amount to Pay: $" + String.format("%.2f", amount));
        amountLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        amountLabel.setForeground(TEXT_COLOR);
        amountLabel.setHorizontalAlignment(SwingConstants.CENTER);
        amountLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 15, 0));
        mainPanel.add(amountLabel, BorderLayout.NORTH);


        JPanel methodPanel = new JPanel(new GridLayout(3, 1, 5, 5));
        methodPanel.setBackground(PANEL_BG);
        methodPanel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(Color.WHITE),
                "Select Payment Method",
                javax.swing.border.TitledBorder.LEFT,
                javax.swing.border.TitledBorder.TOP,
                new Font("Segoe UI", Font.BOLD, 12),
                TEXT_COLOR
        ));

        ButtonGroup group = new ButtonGroup();
        JRadioButton creditCard = createRadioButton("Credit Card");
        JRadioButton cash = createRadioButton("Cash");
        JRadioButton online = createRadioButton("Online Payment");

        group.add(creditCard);
        group.add(cash);
        group.add(online);
        creditCard.setSelected(true);

        creditCard.addActionListener(e -> selectedMethod = "Credit Card");
        cash.addActionListener(e -> selectedMethod = "Cash");
        online.addActionListener(e -> selectedMethod = "Online Payment");

        methodPanel.add(creditCard);
        methodPanel.add(cash);
        methodPanel.add(online);

        mainPanel.add(methodPanel, BorderLayout.CENTER);


        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        buttonPanel.setBackground(PANEL_BG);
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));

        JButton payButton = new JButton("Confirm Payment");
        styleButton(payButton);
        payButton.addActionListener(e -> {
            paymentSuccessful = true;
            dispose();
        });

        JButton cancelButton = new JButton("Cancel");
        styleButton(cancelButton);
        cancelButton.addActionListener(e -> dispose());

        buttonPanel.add(payButton);
        buttonPanel.add(cancelButton);

        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
        add(mainPanel);
    }

    private JRadioButton createRadioButton(String text) {
        JRadioButton radio = new JRadioButton(text);
        radio.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        radio.setBackground(RADIO_BG);
        radio.setForeground(Color.BLACK);
        radio.setFocusPainted(false);
        radio.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 5));
        return radio;
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

    public boolean isPaymentSuccessful() {
        return paymentSuccessful;
    }

    public String getPaymentMethod() {
        return selectedMethod;
    }
}
