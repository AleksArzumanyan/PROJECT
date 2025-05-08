package GUI;
import core.*;
import javax.swing.*;
import java.awt.*;


public class RestaurantPanel extends JPanel {
    private JComboBox<TableType> tableTypeCombo;
    private JList<Integer> availableTablesList;
    private DefaultListModel<Integer> tableListModel;
    private HotelGUI gui;

    public RestaurantPanel(HotelGUI gui) {
        this.gui = gui;
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));


        Restaurant.initializeTables();


        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        topPanel.add(new JLabel("Table Type:"));
        tableTypeCombo = new JComboBox<>(TableType.values());
        tableTypeCombo.addActionListener(e -> updateAvailableTables());
        topPanel.add(tableTypeCombo);


        tableListModel = new DefaultListModel<>();
        availableTablesList = new JList<>(tableListModel);
        availableTablesList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane scrollPane = new JScrollPane(availableTablesList);


        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        JButton bookButton = new JButton("Book Selected Table");
        bookButton.addActionListener(e -> bookTable());
        JButton backButton = new JButton("Back to Main Menu");
        backButton.addActionListener(e -> gui.showPanel("MainMenu"));

        bottomPanel.add(backButton);
        bottomPanel.add(bookButton);

        add(topPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);

        updateAvailableTables();
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
            JOptionPane.showMessageDialog(this, "Please select a table first");
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
                    "Table " + selectedTable + " booked successfully!\n" +
                            "Type: " + tableTypeCombo.getSelectedItem() + "\n" +
                            "Price: $" + finalPrice,
                    "Booking Confirmed",
                    JOptionPane.INFORMATION_MESSAGE);

            updateAvailableTables();
        } else {
            JOptionPane.showMessageDialog(this,
                    "Booking cancelled",
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