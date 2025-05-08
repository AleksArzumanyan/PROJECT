package GUI;
import javax.swing.*;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;

public class MainMenuPanel extends JPanel {
    public MainMenuPanel(HotelGUI gui) {
        setLayout(new GridBagLayout());
        setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(15, 15, 15, 15);
        JLabel title = new JLabel("HOTEL BOOKING SYSTEM");
        title.setFont(new Font("Segoe UI", Font.BOLD, 36));
        title.setForeground(new Color(255, 215, 0)); 
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 3;
        add(title, gbc);

        
        String[] options = {
                "ROOM BOOKING",
                "GYM BOOKING",
                "RESTAURANT BOOKING",
                "EVENT HALL BOOKING",
                "LEAVE FEEDBACK",
                "RATING SUMMARY",
                "EXIT SYSTEM"
        };

        Color[] buttonColors = {
                new Color(70, 130, 180),    
                new Color(60, 179, 113),    
                new Color(205, 92, 92),     
                new Color(186, 85, 211),    
                new Color(255, 165, 0),   
                new Color(100, 149, 237),  
                new Color(169, 169, 169)    
        };

        for (int i = 0; i < options.length; i++) {
            JButton button = createModernButton(options[i], buttonColors[i]);
            final int choice = i + 1;
            button.addActionListener(e -> {
                switch(choice) {
                    case 1: gui.showPanel("RoomBooking"); break;
                    case 2: gui.showPanel("GymBooking"); break;
                    case 3: gui.showPanel("RestaurantBooking"); break;
                    case 4: gui.showPanel("EventBooking"); break;
                    case 5: gui.showPanel("FeedbackPanel"); break;
                    case 6: gui.showPanel("FeedbackSummary"); break;
                    case 7: System.exit(0);
                }
            });

            gbc.gridy = (i / 3) + 1;
            gbc.gridx = i % 3;
            gbc.gridwidth = 1;
            add(button, gbc);
        }
    }

    private JButton createModernButton(String text, Color bgColor) {
        JButton button = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(bgColor);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 25, 25);
                g2.setColor(Color.WHITE);
                g2.setFont(new Font("Segoe UI", Font.BOLD, 14));
                FontMetrics fm = g2.getFontMetrics();
                int x = (getWidth() - fm.stringWidth(getText())) / 2;
                int y = ((getHeight() - fm.getHeight()) / 2) + fm.getAscent();
                g2.drawString(getText(), x, y);
                g2.dispose();
            }
        };
        button.setContentAreaFilled(false);
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.setPreferredSize(new Dimension(200, 80));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return button;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);


        g2d.setColor(new Color(255, 255, 255, 10));
        g2d.fillRoundRect(50, 50, getWidth()-100, getHeight()-100, 30, 30);
    }
}
