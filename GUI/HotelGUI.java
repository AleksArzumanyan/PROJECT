package GUI;
import javax.swing.*;
import java.awt.*;

public class HotelGUI {
    private JFrame mainFrame;
    private CardLayout cardLayout;
    private JPanel cardPanel;

    public HotelGUI() {
        initialize();
    }

    public JFrame getMainFrame() {
        return mainFrame;
    }

    private void initialize() {
        mainFrame = new JFrame("Hotel Management System");
        mainFrame.setSize(1000, 750);
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);



        cardLayout = new CardLayout();
        cardPanel = new JPanel(cardLayout) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                Color color1 = new Color(9, 32, 63);
                Color color2 = new Color(32, 58, 67);
                GradientPaint gp = new GradientPaint(0, 0, color1, getWidth(), getHeight(), color2);
                g2d.setPaint(gp);
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };

        
        cardPanel.add(new MainMenuPanel(this), "MainMenu");
        cardPanel.add(new RoomBookingPanel(this), "RoomBooking");
        cardPanel.add(new GymBookingPanel(this), "GymBooking");
        cardPanel.add(new RestaurantPanel(this), "RestaurantBooking");
        cardPanel.add(new EventReservationPanel(this), "EventBooking");
        cardPanel.add(new FeedbackPanel(this), "FeedbackPanel");
        cardPanel.add(new FeedbackSummaryGUI(this), "FeedbackSummary");

        mainFrame.add(cardPanel);
        mainFrame.setLocationRelativeTo(null);
        mainFrame.setVisible(true);
    }

    public void showPanel(String panelName) {
        cardLayout.show(cardPanel, panelName);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                new HotelGUI();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
}
