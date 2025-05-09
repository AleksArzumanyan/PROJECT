package GUI;

import core.*;
import javax.swing.*;
import javax.swing.event.*;
import java.awt.*;

public class RoomBookingPanel extends JPanel {
    private JComboBox<RoomType> roomTypeCombo;
    private JList<Integer> roomsList;
    private DefaultListModel<Integer> roomListModel;
    private HotelGUI gui;
    private JScrollPane scrollPane;
    private JButton unbookBackButton;
    private ListSelectionListener unbookSelectionListener;

    private final Color MAIN_BG = new Color(9, 32, 63);
    private final Color PANEL_BG = new Color(32, 58, 67);
    private final Color BUTTON_BG = new Color(76, 175, 80);
    private final Color TEXT_COLOR = Color.WHITE;

    private JPanel topPanel;
    private JPanel bottomPanel;

    public RoomBookingPanel(HotelGUI gui) {
        this.gui = gui;
        initializeComponents();
        setupLayout();
        Room.initializeRooms();
        showAvailableRooms();
    }

    private void initializeComponents() {
        roomTypeCombo = new JComboBox<>(RoomType.values());
        roomListModel = new DefaultListModel<>();
        roomsList = new JList<>(roomListModel);
        scrollPane = new JScrollPane(roomsList);
        unbookBackButton = new JButton("Back to Available Rooms");

        styleComponents();
        setupEventHandlers();
    }

    private void setupLayout() {
        setLayout(new BorderLayout(15, 15));
        setBackground(MAIN_BG);
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Top Panel
        topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 10));
        topPanel.setBackground(PANEL_BG);
        topPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel typeLabel = new JLabel("Room Type:");
        typeLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        typeLabel.setForeground(TEXT_COLOR);
        topPanel.add(typeLabel);
        topPanel.add(roomTypeCombo);

        // Bottom Panel
        bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 10));
        bottomPanel.setBackground(PANEL_BG);
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JButton backButton = new JButton("Back to Main Menu");
        JButton bookButton = new JButton("Book Selected Room");
        JButton unbookButton = new JButton("Unbook Room");

        styleButton(backButton);
        styleButton(bookButton);
        styleButton(unbookButton);
        styleButton(unbookBackButton);

        backButton.addActionListener(e -> gui.showPanel("MainMenu"));
        bookButton.addActionListener(e -> bookRoom());
        unbookButton.addActionListener(e -> unbookRoom());
        unbookBackButton.addActionListener(e -> showAvailableRooms());

        bottomPanel.add(backButton);
        bottomPanel.add(bookButton);
        bottomPanel.add(unbookButton);

        add(topPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);
    }

    private void styleComponents() {
        styleComboBox(roomTypeCombo);
        styleRoomList(roomsList);
        styleScrollPane();
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
        list.setBackground(new Color(240, 248, 255));
        list.setForeground(Color.BLACK);
        list.setSelectionBackground(BUTTON_BG);
        list.setSelectionForeground(TEXT_COLOR);
        list.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        list.setFixedCellHeight(25);
        list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    }

    private void styleScrollPane() {
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

    private void setupEventHandlers() {
        roomTypeCombo.addActionListener(e -> updateRoomsList(false));

        unbookSelectionListener = new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()) {
                    Integer selectedRoom = roomsList.getSelectedValue();
                    if (selectedRoom != null) {
                        confirmUnbooking(selectedRoom);
                    }
                }
            }
        };
    }

    private void updateRoomsList(boolean showBooked) {
        roomListModel.clear();
        RoomType selectedType = (RoomType) roomTypeCombo.getSelectedItem();

        for (Room room : Room.getRooms()) {
            if (room != null && room.roomType == selectedType) {
                if (showBooked ? !room.isAvailable : room.isAvailable) {
                    roomListModel.addElement(room.getRoomNumber());
                }
            }
        }
    }

    private void showAvailableRooms() {
        roomsList.removeListSelectionListener(unbookSelectionListener);
        updateRoomsList(false);
        scrollPane.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(Color.WHITE),
                "Available Rooms",
                javax.swing.border.TitledBorder.LEFT,
                javax.swing.border.TitledBorder.TOP,
                new Font("Segoe UI", Font.BOLD, 12),
                TEXT_COLOR
        ));

        // Remove back button if visible
        topPanel.remove(unbookBackButton);
        topPanel.revalidate();
        topPanel.repaint();
    }

    private void showBookedRooms() {
        updateRoomsList(true);
        scrollPane.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(Color.WHITE),
                "Booked Rooms - Select a room to unbook",
                javax.swing.border.TitledBorder.LEFT,
                javax.swing.border.TitledBorder.TOP,
                new Font("Segoe UI", Font.BOLD, 12),
                TEXT_COLOR
        ));

        // Add back button if not present
        if (unbookBackButton.getParent() == null) {
            topPanel.add(unbookBackButton);
        }

        roomsList.addListSelectionListener(unbookSelectionListener);
        topPanel.revalidate();
        topPanel.repaint();
    }

    private void bookRoom() {
        Integer selectedRoom = roomsList.getSelectedValue();
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

        boolean isReturning = Room.loyalCustomers.stream()
                .anyMatch(lc -> lc.getPassportNumber().equals(customer.getPassportNumber()));

        if (isReturning) {
            JOptionPane.showMessageDialog(this,
                    "<html><div style='font-size:14px;'>⭐ Loyal Customer Detected ⭐<br><br>" +
                            "Welcome back! You get 10% discount!</div></html>",
                    "Loyal Customer Discount",
                    JOptionPane.INFORMATION_MESSAGE);
        } else {
            Room.loyalCustomers.add(customer);
        }

        double price = Room.getPriceForType((RoomType) roomTypeCombo.getSelectedItem());
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
                    (RoomType) roomTypeCombo.getSelectedItem(),
                    finalPrice,
                    paymentDialog.getPaymentMethod(),
                    customer);

            JOptionPane.showMessageDialog(this,
                    "<html><div style='font-size:14px;'>Room <b>" + selectedRoom + "</b> booked successfully!<br>" +
                            "Type: " + roomTypeCombo.getSelectedItem() + "<br>" +
                            "Price: <b>$" + String.format("%.2f", finalPrice) + "</b>" +
                            (isReturning ? "<br>(Including 10% loyal customer discount)" : "") +
                            "</div></html>",
                    "Booking Confirmed",
                    JOptionPane.INFORMATION_MESSAGE);

            showAvailableRooms();
        } else {
            JOptionPane.showMessageDialog(this,
                    "Booking was cancelled",
                    "Cancelled",
                    JOptionPane.WARNING_MESSAGE);
        }
    }

    private void unbookRoom() {
        showBookedRooms();
    }

    private void confirmUnbooking(int roomNumber) {
        int confirm = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to unbook Room " + roomNumber + "?",
                "Confirm Unbooking",
                JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            for (Room room : Room.getRooms()) {
                if (room.getRoomNumber() == roomNumber) {
                    room.setAvailable(true);
                    room.customer = null;
                    JOptionPane.showMessageDialog(this,
                            "Room " + roomNumber + " has been successfully unbooked.",
                            "Unbooking Complete",
                            JOptionPane.INFORMATION_MESSAGE);
                    showAvailableRooms();
                    break;
                }
            }
        }
    }
}
