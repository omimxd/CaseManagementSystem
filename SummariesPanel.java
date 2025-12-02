import javax.swing.*;
import java.awt.*;
import java.lang.reflect.Array;
import java.util.ArrayList;

public class SummariesPanel extends JPanel {

    private MainMenu mainMenu;
    private int caseId;

    private JPanel summariesListPanel; 
    private JTextArea summaryInput;

    public SummariesPanel(MainMenu mainMenu) {
        this.mainMenu = mainMenu;

        setLayout(new BorderLayout(15, 15));
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        // ===== TITLE =====
        JLabel titleLabel = new JLabel("Case Summaries", SwingConstants.CENTER);
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 22));
        add(titleLabel, BorderLayout.NORTH);

        // ===== SUMMARIES LIST (with scroll) =====
        summariesListPanel = new JPanel();
        summariesListPanel.setLayout(new BoxLayout(summariesListPanel, BoxLayout.Y_AXIS));

        JScrollPane scrollPane = new JScrollPane(summariesListPanel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        add(scrollPane, BorderLayout.CENTER);

        // ===== INPUT AREA + BUTTONS =====
        JPanel inputPanel = new JPanel(new BorderLayout(10, 10));

        summaryInput = new JTextArea(4, 40);
        summaryInput.setLineWrap(true);
        summaryInput.setWrapStyleWord(true);

        JScrollPane inputScrollPane = new JScrollPane(summaryInput);
        inputPanel.add(inputScrollPane, BorderLayout.CENTER);

        JButton submitBtn = new JButton("Add Summary");
        submitBtn.setPreferredSize(new Dimension(150, 40));
        submitBtn.setFont(new Font("SansSerif", Font.BOLD, 14));

        JButton backBtn = new JButton("Back");
        backBtn.setPreferredSize(new Dimension(120, 40));
        backBtn.setFont(new Font("SansSerif", Font.BOLD, 14));

        // Buttons layout
        JPanel buttonRow = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 5));
        buttonRow.add(backBtn);
        buttonRow.add(submitBtn);

        inputPanel.add(buttonRow, BorderLayout.SOUTH);

        add(inputPanel, BorderLayout.SOUTH);

        // ===== BUTTON ACTIONS =====
        submitBtn.addActionListener(e -> {
            Case c = DatabaseHelper.getCaseById(caseId);
            if (c.getStatus().equals("Completed")){ 
                JOptionPane.showMessageDialog(this, "Case Already Resolved.", "Summary", JOptionPane.INFORMATION_MESSAGE);
            }else{
                saveNewSummary();
            }
                
        });
        backBtn.addActionListener(e -> mainMenu.showPanel("ViewCase"));
    }

    // Called when switching into this panel
    public void loadSummaries(int caseId) {
        this.caseId = caseId;

        summariesListPanel.removeAll();

        // Get summaries from DB
        ArrayList<Summary> summaries = DatabaseHelper.getSummariesForCase(caseId);

        if (summaries.isEmpty()) {
            JLabel noData = new JLabel("No summaries yet.", SwingConstants.CENTER);
            noData.setFont(new Font("SansSerif", Font.ITALIC, 16));
            summariesListPanel.add(noData);
        } else {
            for (Summary s : summaries) {
                JPanel item = createSummaryItem(s);
                System.out.println(s.getCaseId());
                summariesListPanel.add(item);
                summariesListPanel.add(Box.createVerticalStrut(10));
            }
        }

        summariesListPanel.revalidate();
        summariesListPanel.repaint();
    }

    // Creates a panel for a single summary
    private JPanel createSummaryItem(Summary summary) {
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.GRAY, 1),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));

        JLabel dateLabel = new JLabel("Date: " + summary.getDateCreated());
        dateLabel.setFont(new Font("SansSerif", Font.BOLD, 14));

        JTextArea textArea = new JTextArea(summary.getText());
        textArea.setWrapStyleWord(true);
        textArea.setLineWrap(true);
        textArea.setEditable(false);
        textArea.setFont(new Font("SansSerif", Font.PLAIN, 14));
        textArea.setBackground(panel.getBackground());

        panel.add(dateLabel, BorderLayout.NORTH);
        panel.add(textArea, BorderLayout.CENTER);

        return panel;
    }

    // Save new summary
    private void saveNewSummary() {

        String text = summaryInput.getText().trim();

        if (text.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Summary cannot be empty.",
                    "Validation Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Insert into DB
        DatabaseHelper.insertSummary(caseId, text);

        summaryInput.setText("");

        // Reload list
        loadSummaries(caseId);
    }

    public void refresh(){
        loadSummaries(caseId);
    }

    public void setCaseId(int caseId) {
        this.caseId = caseId;
        // summaryArea.setText("");
    }
}
