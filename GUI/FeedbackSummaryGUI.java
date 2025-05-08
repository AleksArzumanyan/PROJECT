package GUI;
import javax.swing.*;
import java.awt.*;
import java.io.*;

public class FeedbackSummaryGUI extends JPanel {
    private JTextArea summaryArea;

    public FeedbackSummaryGUI(HotelGUI gui) {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createTitledBorder("All Feedback"));

        summaryArea = new JTextArea();
        summaryArea.setEditable(false);
        summaryArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        JScrollPane scrollPane = new JScrollPane(summaryArea);
        add(scrollPane, BorderLayout.CENTER);


        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 10));

        JButton refreshButton = new JButton("Refresh");
        refreshButton.addActionListener(e -> loadFeedback());

        JButton backButton = new JButton("Back to Main Menu");
        backButton.addActionListener(e -> gui.showPanel("MainMenu"));

        buttonPanel.add(refreshButton);
        buttonPanel.add(backButton);

        add(buttonPanel, BorderLayout.SOUTH);

        loadFeedback();
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
