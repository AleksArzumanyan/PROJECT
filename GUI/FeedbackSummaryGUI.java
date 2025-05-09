package GUI;
import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.io.*;

public class FeedbackSummaryGUI extends JPanel {
    private JTextArea summaryArea;

    private final Color MAIN_BG = new Color(9, 32, 63);
    private final Color PANEL_BG = new Color(32, 58, 67);
    private final Color BUTTON_BG = new Color(76, 175, 80);
    private final Color TEXT_COLOR = Color.WHITE;

    public FeedbackSummaryGUI(HotelGUI gui) {
        setLayout(new BorderLayout(15, 15));
        setBackground(MAIN_BG);
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));


        TitledBorder border = BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(Color.WHITE),
                "All Feedback",
                TitledBorder.LEFT,
                TitledBorder.TOP,
                new Font("Segoe UI", Font.BOLD, 14),
                TEXT_COLOR);

        JPanel contentPanel = new JPanel(new BorderLayout());
        contentPanel.setBackground(PANEL_BG);
        contentPanel.setBorder(border);

        summaryArea = new JTextArea();
        summaryArea.setEditable(false);
        summaryArea.setFont(new Font("Monospaced", Font.PLAIN, 14));
        summaryArea.setForeground(Color.BLACK);
        summaryArea.setBackground(new Color(240, 248, 255)); // AliceBlue
        summaryArea.setMargin(new Insets(10, 10, 10, 10));

        JScrollPane scrollPane = new JScrollPane(summaryArea);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.setBackground(PANEL_BG);
        contentPanel.add(scrollPane, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(PANEL_BG);
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 10));

        JButton refreshButton = new JButton("Refresh");
        styleInvisibleButton(refreshButton);
        refreshButton.addActionListener(e -> loadFeedback());

        JButton backButton = new JButton("Back to Main Menu");
        styleInvisibleButton(backButton);
        backButton.addActionListener(e -> gui.showPanel("MainMenu"));

        buttonPanel.add(refreshButton);
        buttonPanel.add(backButton);

        add(contentPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        loadFeedback();
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

    private void loadFeedback() {
        File file = new File("feedback.txt");

        if (!file.exists()) {
            summaryArea.setText("No feedback found.");
            return;
        }

        StringBuilder content = new StringBuilder();

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                content.append(line).append("\n");
            }
            summaryArea.setText(content.toString());
        } catch (IOException e) {
            summaryArea.setText("Error loading feedback.");
        }
    }
}
