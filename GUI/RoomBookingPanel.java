package GUI;
import core.*;
import javax.swing.*;
import java.awt.*;
public class RoomBookingPanel extends JPanel {
    private JComboBox<RoomType> roomTypeCombo;
    private JList<Integer> availableRoomsList;
    private DefaultListModel<Integer> roomListModel;
    private HotelGUI gui;

    public RoomBookingPanel(HotelGUI gui) {
        this.gui = gui;
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));


        Room.initializeRooms();


        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        topPanel.add(new JLabel("Room Type:"));
        roomTypeCombo = new JComboBox<>(RoomType.values());
        roomTypeCombo.addActionListener(e -> updateAvailableRooms());
        topPanel.add(roomTypeCombo);


        roomListModel = new DefaultListModel<>();
        availableRoomsList = new JList<>(roomListModel);
        availableRoomsList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane scrollPane = new JScrollPane(availableRoomsList);


        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        JButton bookButton = new JButton("Book Selected Room");
        bookButton.addActionListener(e -> bookRoom());
        JButton backButton = new JButton("Back to Main Menu");
        backButton.addActionListener(e -> gui.showPanel("MainMenu"));

        bottomPanel.add(backButton);
        bottomPanel.add(bookButton);

        add(topPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);

        updateAvailableRooms();
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
            JOptionPane.showMessageDialog(this, "Please select a room first");
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
                    "Room " + selectedRoom + " booked successfully!\n" +
                            "Type: " + roomTypeCombo.getSelectedItem() + "\n" +
                            "Price: $" + finalPrice,
                    "Booking Confirmed",
                    JOptionPane.INFORMATION_MESSAGE);

            updateAvailableRooms();
        } else {
            JOptionPane.showMessageDialog(this,
                    "Booking cancelled",
                    "Cancelled",
                    JOptionPane.WARNING_MESSAGE);
        }
    }
}