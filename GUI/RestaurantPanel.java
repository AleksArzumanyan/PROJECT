package GUI;
import core.*;
import javax.swing.*;
import java.awt.*;

public class RestaurantPanel extends JPanel {
    private JComboBox<TableType> tableTypeCombo;
    private JList<Integer> availableTablesList;
    private DefaultListModel<Integer> tableListModel;
    private HotelGUI gui;


    private final Color MAIN_BG = new Color(9, 32, 63);
    private final Color PANEL_BG = new Color(32, 58, 67);
    private final Color BUTTON_BG = new Color(76, 175, 80);
    private final Color TEXT_COLOR = Color.WHITE;

    public RestaurantPanel(HotelGUI gui) {
        this.gui = gui;
        // Main panel styling
        setLayout(new BorderLayout(15, 15));
        setBackground(MAIN_BG);
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        Restaurant.initializeTables();


        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 10));
        topPanel.setBackground(PANEL_BG);
        topPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel typeLabel = new JLabel("Table Type:");
        typeLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        typeLabel.setForeground(TEXT_COLOR);
        topPanel.add(typeLabel);

        tableTypeCombo = new JComboBox<>(TableType.values());
        styleComboBox(tableTypeCombo);
        tableTypeCombo.addActionListener(e -> updateAvailableTables());
        topPanel.add(tableTypeCombo);


        tableListModel = new DefaultListModel<>();
        availableTablesList = new JList<>(tableListModel);
        availableTablesList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        styleTableList(availableTablesList);

        JScrollPane scrollPane = new JScrollPane(availableTablesList);
        scrollPane.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(Color.WHITE),
                "Available Tables",
                javax.swing.border.TitledBorder.LEFT,
                javax.swing.border.TitledBorder.TOP,
                new Font("Segoe UI", Font.BOLD, 12),
                TEXT_COLOR
        ));
        scrollPane.setBackground(PANEL_BG);
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);


        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 10));
        bottomPanel.setBackground(PANEL_BG);
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JButton backButton = new JButton("Back to Main Menu");
        styleInvisibleButton(backButton);
        backButton.addActionListener(e -> gui.showPanel("MainMenu"));

        JButton bookButton = new JButton("Book Selected Table");
        styleInvisibleButton(bookButton);
        bookButton.addActionListener(e -> bookTable());

        bottomPanel.add(backButton);
        bottomPanel.add(bookButton);

        add(topPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);

        updateAvailableTables();
    }

    private void styleComboBox(JComboBox<?> comboBox) {
        comboBox.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        comboBox.setBackground(Color.WHITE);
        comboBox.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.WHITE, 1),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)
        ));
    }

    private void styleTableList(JList<?> list) {
        list.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        list.setBackground(new Color(240, 248, 255)); // AliceBlue
        list.setForeground(Color.BLACK);
        list.setSelectionBackground(BUTTON_BG);
        list.setSelectionForeground(TEXT_COLOR);
        list.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        list.setFixedCellHeight(25);
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

    private void updateAvailableTables() {
        tableListModel.clear();
        TableType selectedType = (TableType)tableTypeCombo.getSelectedItem();

        for (Restaurant table : Restaurant.tables) {
            if (table != null && table.tableType == selectedType && table.isAvailable) {
                tableListModel.addElement(table.tableNumber);
            }
        }
    }

    private void bookTable() {
        Integer selectedTable = availableTablesList.getSelectedValue();
        if (selectedTable == null) {
            JOptionPane.showMessageDialog(this,
                    "Please select a table first",
                    "Selection Required",
                    JOptionPane.WARNING_MESSAGE);
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

        double price = getPriceForTableType((TableType)tableTypeCombo.getSelectedItem());
        double finalPrice = isReturning ? price * 0.9 : price;

        PaymentDialog paymentDialog = new PaymentDialog(gui.getMainFrame(), finalPrice);
        paymentDialog.setVisible(true);

        if (paymentDialog.isPaymentSuccessful()) {
            for (Restaurant table : Restaurant.tables) {
                if (table.tableNumber == selectedTable) {
                    table.isAvailable = false;
                    break;
                }
            }

            JOptionPane.showMessageDialog(this,
                    "<html><div style='font-size:14px;'>" +
                            "Table <b>" + selectedTable + "</b> booked successfully!<br>" +
                            "Type: " + tableTypeCombo.getSelectedItem() + "<br>" +
                            "Price: <b>$" + String.format("%.2f", finalPrice) + "</b>" +
                            "</div></html>",
                    "Booking Confirmed",
                    JOptionPane.INFORMATION_MESSAGE);

            updateAvailableTables();
        } else {
            JOptionPane.showMessageDialog(this,
                    "Booking was cancelled",
                    "Cancelled",
                    JOptionPane.WARNING_MESSAGE);
        }
    }

    private double getPriceForTableType(TableType type) {
        switch (type) {
            case SMALL_TABLE: return 50;
            case MEDIUM_TABLE: return 100;
            case LARGE_TABLE: return 200;
            default: return 0;
        }
    }
}
