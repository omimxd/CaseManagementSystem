import javax.swing.*;
import java.awt.*;

public class ResolveCasePanel extends JPanel {

    private MainMenu mainMenu;
    private int caseId;

    private JTextArea summaryArea;

    public ResolveCasePanel(MainMenu mainMenu) {
        this.mainMenu = mainMenu;

        setLayout(new BorderLayout(15, 15));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel title = new JLabel("Resolve Case");
        title.setFont(new Font("Arial", Font.BOLD, 22));
        title.setHorizontalAlignment(SwingConstants.CENTER);
        add(title, BorderLayout.NORTH);

        JPanel center = new JPanel(new BorderLayout(10, 10));

        center.add(new JLabel("Resolution Summary:"), BorderLayout.NORTH);

        summaryArea = new JTextArea(8, 40);
        summaryArea.setLineWrap(true);
        summaryArea.setWrapStyleWord(true);

        JScrollPane scrollPane = new JScrollPane(summaryArea);

        center.add(scrollPane, BorderLayout.CENTER);

        add(center, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));

        JButton backButton = new JButton("Back");
        JButton resolveButton = new JButton("Resolve Case");

        buttonPanel.add(backButton);
        buttonPanel.add(resolveButton);

        add(buttonPanel, BorderLayout.SOUTH);

       
        backButton.addActionListener(e -> {
            mainMenu.showPanel("MainMenu");
            
        });

        // Resolve Action
        resolveButton.addActionListener(e -> {
            String summary = summaryArea.getText().trim();

            if (summary.isEmpty()) {
                JOptionPane.showMessageDialog(
                        this,
                        "Please enter a summary before resolving.",
                        "Missing Summary",
                        JOptionPane.WARNING_MESSAGE
                );
                return;
            }

            // Store resolution in DB
            DatabaseHelper.resolveCase(caseId,summary);
            Case c = DatabaseHelper.getCaseById(caseId);
            DatabaseHelper.insertCaseIntoArchives(c);
            mainMenu.refreshTable();
            
            JOptionPane.showMessageDialog(
                    this,
                    "Case " + caseId + " has been resolved.",
                    "Case Resolved",
                    JOptionPane.INFORMATION_MESSAGE
            );

            mainMenu.showSearchPanel();
        });
    }

    public void setCaseId(int caseId) {
        this.caseId = caseId;
        summaryArea.setText("");
    }


}


