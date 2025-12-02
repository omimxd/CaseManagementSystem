import javax.swing.*;
import java.awt.*;

public class CreateCasePanel extends JPanel {

    private MainMenu mainMenu;

    private JTextField caseTitleField;
    private String statusField = "Ongoing";
    private JTextField defendantNameField;    
    private JTextField defendantAgeField;
    private JTextField defendantDOBField;
    private JComboBox<String> caseTypeBox;
    private JTextField hearingDateField;

    JPanel formPanel;

    public CreateCasePanel(MainMenu mainMenu) {

        this.mainMenu = mainMenu;
        setLayout(new BorderLayout());

        // ===== Form Panel =====
        formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        int row = 0;

        // 1. Title
        gbc.gridx = 0; gbc.gridy = row;
        formPanel.add(new JLabel("Case Title:"), gbc);
        gbc.gridx = 1;
        caseTitleField = new JTextField(20);
        formPanel.add(caseTitleField, gbc);
        row++;

        // // Status
        // gbc.gridx = 0; gbc.gridy = row;
        // formPanel.add(new JLabel("Status:"), gbc);
        // gbc.gridx = 1;
        // statusField = new JTextField(20);
        // formPanel.add(statusField, gbc);
        // row++;

        // 3. Defendant Name
        gbc.gridx = 0; gbc.gridy = row;
        formPanel.add(new JLabel("Defendant Name:"), gbc);
        gbc.gridx = 1;
        defendantNameField = new JTextField(20);
        formPanel.add(defendantNameField, gbc);
        row++;

        // 4. Defendant Age
        gbc.gridx = 0; gbc.gridy = row;
        formPanel.add(new JLabel("Defendant Age:"), gbc);
        gbc.gridx = 1;
        defendantAgeField = new JTextField(20);
        formPanel.add(defendantAgeField, gbc);
        row++;

        // 5. Date of Birth
        gbc.gridx = 0; gbc.gridy = row;
        formPanel.add(new JLabel("Date of Birth (YYYY-MM-DD):"), gbc);
        gbc.gridx = 1;
        defendantDOBField = new JTextField(20);
        formPanel.add(defendantDOBField, gbc);
        row++;

        // 6. Case Type
        gbc.gridx = 0; gbc.gridy = row;
        formPanel.add(new JLabel("Case Type:"), gbc);
        gbc.gridx = 1;
        String[] caseTypes = {"Criminal", "Civil", "Family", "Juvenile"};
        caseTypeBox = new JComboBox<>(caseTypes);
        formPanel.add(caseTypeBox, gbc);
        row++;

        // 7. Hearing Date
        gbc.gridx = 0; gbc.gridy = row;
        formPanel.add(new JLabel("Hearing Date (YYYY-MM-DD):"), gbc);
        gbc.gridx = 1;
        hearingDateField = new JTextField(20);
        formPanel.add(hearingDateField, gbc);
        row++;

        // ===== Buttons Panel =====
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));

        JButton backButton = new JButton("Back");
        backButton.setPreferredSize(new Dimension(120, 40));
        backButton.setFont(new Font("SansSerif", Font.BOLD, 14));

        JButton submitButton = new JButton("Create Case");
        submitButton.setPreferredSize(new Dimension(120, 40));
        submitButton.setFont(new Font("SansSerif", Font.BOLD, 14));
        

        // ===== Button Listeners =====
        submitButton.addActionListener(e -> {
            // System.out.println("Submit button clicked");
            createCaseRecord();
        });

        backButton.addActionListener(e -> {
            // System.out.println("Back button clicked");
            mainMenu.switchToMainMenu();
        });

        buttonPanel.add(backButton);
        buttonPanel.add(submitButton);
        

        // ===== Add to main panel =====
        add(formPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    
    // ===== Methods =====
    private void createCaseRecord() {

        // 1. Collecting form data
        String caseTitle = caseTitleField.getText().trim();
        // String status = "Ongoing";
        String status = statusField;
        String defendantName = defendantNameField.getText().trim();
        String defendantAge = defendantAgeField.getText().trim();
        String defendantDOB = defendantDOBField.getText().trim();
        String caseType = (String) caseTypeBox.getSelectedItem();
        String hearingDate = hearingDateField.getText().trim();

        // 2. Validating fields
        if (caseTitle.isEmpty() || status.isEmpty() || defendantName.isEmpty() || defendantAge.isEmpty() 
            || defendantDOB.isEmpty() || caseType.isEmpty() || hearingDate.isEmpty()) {
            JOptionPane.showMessageDialog(this, "All fields are required.");
            return;
        }

        // 3. Create the Case object
        Case newCase = new Case(
            caseTitle,
            defendantName,
            defendantAge,
            defendantDOB,
            status,
            caseType,
            hearingDate
        );

        // 4. Insert into database using DatabaseHelper
        // DatabaseHelper.createDatabaseAndTables();
        DatabaseHelper.insertCase(newCase);
        JOptionPane.showMessageDialog(formPanel, "Case Recorded Successfully!", "Case Record", JOptionPane.INFORMATION_MESSAGE);
        mainMenu.refreshTable();
        clearForm();
    }


    private void clearForm() {
        caseTitleField.setText("");
        defendantNameField.setText("");
        defendantAgeField.setText("");
        defendantDOBField.setText("");
        caseTypeBox.setSelectedIndex(0);
        hearingDateField.setText("");
       
    }
}

