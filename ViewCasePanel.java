import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;


public class ViewCasePanel extends JPanel {

    private MainMenu mainMenu;

    private JTextField idField;
    private JTextField titleField;
    private JTextField statusField;
    private JComboBox<String> caseTypeBox;
    private JTextField defendantNameField;
    private JTextField defendantAgeField;
    private JTextField caseTypeField;
    private JTextField defendantDOBField;
    private JTextField hearingDateField;
    private JTextField dateCreatedField;

    private int caseId;

    private JPanel formPanel;



    public ViewCasePanel(MainMenu mainMenu) {
        this.mainMenu = mainMenu;

        setLayout(new BorderLayout());
        setBorder(BorderFactory.createEmptyBorder(25, 25, 25, 25));

        JLabel header = new JLabel("View / Edit Case Record", SwingConstants.CENTER);
        header.setFont(new Font("Arial", Font.BOLD, 22));
        add(header, BorderLayout.NORTH);

        // Case View and Form panel
        formPanel = new JPanel(new GridLayout(10, 2, 12, 12));
        formPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.GRAY, 1),
                BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));

        // ID Fields
        idField = new JTextField();
        idField.setEditable(false);

        // Date Created
        dateCreatedField = new JTextField();
        dateCreatedField.setEditable(false);
        
        // Title
        titleField = new JTextField();

        // Status 
        statusField = new JTextField();
        statusField.setEditable(false);
        
        // Defendant Name 
        defendantNameField = new JTextField();

        // Defendant Age
        defendantAgeField = new JTextField();

        // Case Type
        caseTypeField = new JTextField();
        caseTypeBox = new JComboBox<>(new String[]{
                "Criminal",
                "Civil",
                "Family",
                "Traffic",
                "Juvenile"
        });

        // Date of Birth
        defendantDOBField = new JTextField();

        // Hearing Dtae
        hearingDateField = new JTextField();

        // ===== Adding Fields to form =====
        formPanel.add(new JLabel("Case ID:"));
        formPanel.add(idField);

        formPanel.add(new JLabel("Title:"));
        formPanel.add(titleField);

        formPanel.add(new JLabel("Status:"));
        formPanel.add(statusField);

        formPanel.add(new JLabel("Name:"));
        formPanel.add(defendantNameField);

        formPanel.add(new JLabel("Age:"));
        formPanel.add(defendantAgeField);

        formPanel.add(new JLabel("Case Type:"));
        formPanel.add(caseTypeBox);

        formPanel.add(new JLabel("Date of Birth:"));
        formPanel.add(defendantDOBField);

        formPanel.add(new JLabel("Hearing Date:"));
        formPanel.add(hearingDateField);

        formPanel.add(new JLabel("Date Created:"));
        formPanel.add(dateCreatedField);

        add(formPanel, BorderLayout.CENTER);

        // Buttons panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 15));

        JButton backBtn = new JButton("Back");
        backBtn.addActionListener(e -> mainMenu.switchToSearchMenu());

        JButton removeBtn = new JButton("Remove");
        removeBtn.addActionListener(e -> {
            Case c = DatabaseHelper.getCaseById(caseId);
            if (c.getStatus().equals("Completed")){
                int choice = JOptionPane.showConfirmDialog(
                    null,
                    "Are you sure you want to delete this case?",
                    "Confirm Deletion",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.WARNING_MESSAGE
                );

                if (choice == JOptionPane.YES_OPTION) {
                
                    JOptionPane.showMessageDialog(this, "Case Deleted.", "Remove", JOptionPane.INFORMATION_MESSAGE);
                    DatabaseHelper.removeFromArchives(c);
                    mainMenu.refreshTable();
                    mainMenu.showPanel("MainMenu");
                }

            }else{
                JOptionPane.showMessageDialog(this, "Can only remove when case is resolved.", "Remove", JOptionPane.INFORMATION_MESSAGE);
            }
        });

        JButton summaryBtn = new JButton("Summaries");
        summaryBtn.addActionListener(e -> {
                // JOptionPane.showMessageDialog(this, "Summaries window coming soon!", "Summary", JOptionPane.INFORMATION_MESSAGE)
                
                mainMenu.showSummariesPanel(caseId);
        });

        JButton saveBtn = new JButton("Save Changes");
        
        saveBtn.addActionListener(e -> { 
            Case c = DatabaseHelper.getCaseById(caseId);
            if (c.getStatus().equals("Completed")){    
                JOptionPane.showMessageDialog(this, "Case was Resolve Already (Cannot Save)", "Resolve Case", JOptionPane.INFORMATION_MESSAGE);
            }else{
                saveChanges();
            }

        });

        JButton resolveBtn = new JButton("Resolve");
        resolveBtn.addActionListener(e -> {
            Case c = DatabaseHelper.getCaseById(caseId);
            if (c.getStatus().equals("Completed")){    
                JOptionPane.showMessageDialog(this, "Case was Resolve Already", "Resolve Case", JOptionPane.INFORMATION_MESSAGE);
            }else{
                mainMenu.showResolveCasePanel(caseId);
            }
                
                
        });

        buttonPanel.add(backBtn);
        buttonPanel.add(removeBtn);
        buttonPanel.add(summaryBtn);
        buttonPanel.add(resolveBtn);
        buttonPanel.add(saveBtn);

        add(buttonPanel, BorderLayout.SOUTH);
    }

   
    public void setCaseDetails(int caseId) {
        this.caseId = caseId;

        Case c = DatabaseHelper.getCaseById(caseId);

        if (c == null) {
            JOptionPane.showMessageDialog(this, "Case not found!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Populate fields
        idField.setText(String.valueOf(c.getCaseId()));
        titleField.setText(c.getCaseTitle());
        statusField.setText(c.getStatus());
        defendantNameField.setText(c.getDefendantName());
        defendantAgeField.setText(c.getDefendantAge());
        // caseTypeBox.setText(c.getCaseType());
        caseTypeBox.setSelectedItem(c.getCaseType());
        defendantDOBField.setText(c.getDefendantDOB());
        hearingDateField.setText(c.getHearingDate());
        dateCreatedField.setText(c.getDateCreated());
    }

   
    private void saveChanges() {

        // 1. Collecting form data
        String caseTitle = titleField.getText().trim();
        String status = "Ongoing";
        String defendantName = defendantNameField.getText().trim();
        String defendantAge = defendantAgeField.getText().trim();
        String defendantDOB = defendantDOBField.getText().trim();
        String caseType = (String) caseTypeBox.getSelectedItem();
        String hearingDate = hearingDateField.getText().trim();
        String dateCreated = dateCreatedField.getText().trim();

        // 2. Validating fields
        if (caseTitle.isEmpty() || status.isEmpty()|| defendantName.isEmpty() || defendantAge.isEmpty() 
            || defendantDOB.isEmpty() || caseType.isEmpty() || hearingDate.isEmpty()) {
            JOptionPane.showMessageDialog(this, "All fields are required.");
            return;
        }

        // 3. Create the Case object
        Case newCase = new Case(
                caseId,
                caseTitle,
                defendantName,
                defendantAge,
                defendantDOB,
                status,
                caseType,
                hearingDate,
                dateCreated
        );

        DatabaseHelper.updateCase(newCase);
        JOptionPane.showMessageDialog(formPanel, "Case Updated!", "Update Case Record", JOptionPane.INFORMATION_MESSAGE);
        refreshViewCase();
        mainMenu.refreshTable();

    }

    public void refreshViewCase(){
        setCaseDetails(caseId);
    }
}





