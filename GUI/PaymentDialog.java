package GUI;
import javax.swing.*;
import java.awt.*;

public class PaymentDialog extends JDialog {
    private boolean paymentSuccessful = false;
    private String selectedMethod = "Credit Card";  // Must be declared early

    public PaymentDialog(Frame parent, double amount) {
        super(parent, "Payment", true);
        setLayout(new BorderLayout(10, 10));
        setSize(300, 200);

        JPanel centerPanel = new JPanel(new GridLayout(4, 1, 5, 5));
        centerPanel.add(new JLabel("Amount: $" + String.format("%.2f", amount)));

        ButtonGroup group = new ButtonGroup();
        JRadioButton creditCard = new JRadioButton("Credit Card");
        JRadioButton cash = new JRadioButton("Cash");
        JRadioButton online = new JRadioButton("Online");

        group.add(creditCard);
        group.add(cash);
        group.add(online);
        creditCard.setSelected(true);


        creditCard.addActionListener(e -> selectedMethod = "Credit Card");
        cash.addActionListener(e -> selectedMethod = "Cash");
        online.addActionListener(e -> selectedMethod = "Online");

        centerPanel.add(creditCard);
        centerPanel.add(cash);
        centerPanel.add(online);

        JPanel buttonPanel = new JPanel();
        JButton payButton = new JButton("Confirm Payment");
        payButton.addActionListener(e -> {
            paymentSuccessful = true;
            dispose();
        });

        JButton cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(e -> dispose());

        buttonPanel.add(payButton);
        buttonPanel.add(cancelButton);

        add(centerPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
        setLocationRelativeTo(parent);
    }

    public boolean isPaymentSuccessful() {
        return paymentSuccessful;
    }

    public String getPaymentMethod() {
        return selectedMethod;
    }
}
