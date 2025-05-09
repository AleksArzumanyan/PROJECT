package GUI;
import core.*;
import javax.swing.*;
import java.awt.*;

public class RoomBookingPanel extends JPanel {
    private JComboBox<RoomType> roomTypeCombo;
    private JList<Integer> availableRoomsList;
    private DefaultListModel<Integer> roomListModel;
    private HotelGUI gui;


    private final Color MAIN_BG = new Color(9, 32, 63);
    private final Color PANEL_BG = new Color(32, 58, 67);
    private final Color BUTTON_BG = new Color(76, 175, 80);
    private final Color TEXT_COLOR = Color.WHITE;

    public RoomBookingPanel(HotelGUI gui) {
        this.gui = gui;

        setLayout(new BorderLayout(15, 15));
        setBackground(MAIN_BG);
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        Room.initializeRooms();


        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 10));
        topPanel.setBackground(PANEL_BG);
        topPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel typeLabel = new JLabel("Room Type:");
        typeLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        typeLabel.setForeground(TEXT_COLOR);
        topPanel.add(typeLabel);

        roomTypeCombo = new JComboBox<>(RoomType.values());
        styleComboBox(roomTypeCombo);
        roomTypeCombo.addActionListener(e -> updateAvailableRooms());
        topPanel.add(roomTypeCombo);


        roomListModel = new DefaultListModel<>();
        availableRoomsList = new JList<>(roomListModel);
        availableRoomsList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        styleRoomList(availableRoomsList);

        JScrollPane scrollPane = new JScrollPane(availableRoomsList);
        scrollPane.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(Color.WHITE),
                "Available Rooms",
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

        JButton bookButton = new JButton("Book Selected Room");
        styleInvisibleButton(bookButton);
        bookButton.addActionListener(e -> bookRoom());

        bottomPanel.add(backButton);
        bottomPanel.add(bookButton);

        add(topPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);

        updateAvailableRooms();
    }

    private void styleComboBox(JComboBox<?> comboBox) {
        comboBox.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        comboBox.setBackground(Color.WHITE);
        comboBox.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.WHITE, 1),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)
        ));
    }

    private void styleRoomList(JList<?> list) {
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

    private void updateAvailableRooms() {
        roomListModel.clear();
        RoomType selectedType = (RoomType)roomTypeCombo.getSelectedItem();

        for (Room room : Room.getRooms()) {
            if (room != null && room.roomType == selectedType && room.isAvailable) {
                roomListModel.addElement(room.getRoomNumber());
            }
        }
    }

    private void bookRoom() {
        Integer selectedRoom = availableRoomsList.getSelectedValue();
        if (selectedRoom == null) {
            JOptionPane.showMessageDialog(this,
                    "Please select a room first",
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

        double price = Room.getPriceForType((RoomType)roomTypeCombo.getSelectedItem());
        double finalPrice = isReturning ? price * 0.9 : price;

        PaymentDialog paymentDialog = new PaymentDialog(gui.getMainFrame(), finalPrice);
        paymentDialog.setVisible(true);

        if (paymentDialog.isPaymentSuccessful()) {
            for (Room room : Room.getRooms()) {
                if (room.getRoomNumber() == selectedRoom) {
                    room.setAvailable(false);
                    room.customer = customer;
                    break;
                }
            }

            Receipt.generateReceipt(selectedRoom,
                    (RoomType)roomTypeCombo.getSelectedItem(),
                    finalPrice,
                    paymentDialog.getPaymentMethod(),
                    customer);

            JOptionPane.showMessageDialog(this,
                    "<html><div style='font-size:14px;'>" +
                            "Room <b>" + selectedRoom + "</b> booked successfully!<br>" +
                            "Type: " + roomTypeCombo.getSelectedItem() + "<br>" +
                            "Price: <b>$" + String.format("%.2f", finalPrice) + "</b>" +
                            "</div></html>",
                    "Booking Confirmed",
                    JOptionPane.INFORMATION_MESSAGE);

            updateAvailableRooms();
        } else {
            JOptionPane.showMessageDialog(this,
                    "Booking was cancelled",
                    "Cancelled",
                    JOptionPane.WARNING_MESSAGE);
        }
    }
}
