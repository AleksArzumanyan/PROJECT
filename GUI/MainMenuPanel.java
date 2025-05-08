package GUI;
import javax.swing.*;
import java.awt.*;

public class MainMenuPanel extends JPanel {
    public MainMenuPanel(HotelGUI gui) {
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

        JLabel title = new JLabel("Welcome to the Hotel Booking System!");
        title.setFont(new Font("Arial", Font.BOLD, 24));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        add(title, gbc);

        String[] options = {
                "Book a Room",
                "Book the Gym",
                "Book the Restaurant",
                "Book an Event Hall",
                "Leave Feedback",
                "Show Rating Summary",
                "Exit"
        };

        for (int i = 0; i < options.length; i++) {
            JButton button = new JButton(options[i]);
            button.setPreferredSize(new Dimension(250, 50));
            button.setFont(new Font("Arial", Font.PLAIN, 16));

            final int choice = i + 1;
            button.addActionListener(e -> {
                switch(choice) {
                    case 1: gui.showPanel("RoomBooking"); break;
                    case 2: gui.showPanel("GymBooking"); break;
                    case 3: gui.showPanel("RestaurantBooking"); break;
                    case 4: gui.showPanel("EventBooking"); break;
                    case 5:
                        gui.showPanel("FeedbackPanel"); break;
                    case 6:
                        gui.showPanel("FeedbackSummary"); break;
                    case 7: System.exit(0);
                }
            });

            gbc.gridy = i + 1;
            gbc.gridwidth = 1;
            add(button, gbc);
        }
    }
}