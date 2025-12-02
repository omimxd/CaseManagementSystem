import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;



public class ArchivePanel extends JPanel{

    private MainMenu mainMenu;

    private JTable resultsTable;
    private static DefaultTableModel tableModel;

    public ArchivePanel(MainMenu mainMenu){
        this.mainMenu = mainMenu;
        
        setLayout(new BorderLayout());

        // ====== ARCHIVE FORM PANEL ======
        JPanel searchPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10,10,10,10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // // Person attached to case
        // gbc.gridx = 0; gbc.gridy = 0;
        // searchPanel.add(new JLabel("Person Attached:"), gbc);

        // gbc.gridx = 1;
        // defendantField = new JTextField(15);
        // searchPanel.add(defendantField, gbc);

        // // Case Type dropdown
        // gbc.gridx = 0; gbc.gridy = 1;
        // searchPanel.add(new JLabel("Case Type:"), gbc);

        // gbc.gridx = 1;
        // String[] caseTypes = {"All", "Criminal", "Civil", "Family", "Juvenile"};
        // caseTypeBox = new JComboBox<>(caseTypes);
        // searchPanel.add(caseTypeBox, gbc);

        // // Date of Birth
        // gbc.gridx = 0; gbc.gridy = 2;
        // searchPanel.add(new JLabel("Date of Birth (YYYY-MM-DD):"), gbc);

        // gbc.gridx = 1;
        // dobField = new JTextField(15);
        // searchPanel.add(dobField, gbc);

        // // Hearing Date
        // gbc.gridx = 0; gbc.gridy = 3;
        // searchPanel.add(new JLabel("Hearing Date (YYYY-MM-DD):"), gbc);

        // gbc.gridx = 1;
        // hearingDateField = new JTextField(15);
        // searchPanel.add(hearingDateField, gbc);

        // // Search Button
        // JButton searchButton = new JButton("Search");
        // searchButton.setPreferredSize(new Dimension(180,50));
        // searchButton.setFont(new Font("SansSerif", Font.BOLD, 16));
        // gbc.gridx = 0; gbc.gridy = 4; gbc.gridwidth = 2; gbc.anchor = GridBagConstraints.CENTER;
        // searchPanel.add(searchButton, gbc);

        // Back Button
        JButton backButton = new JButton("Back");
        backButton.setPreferredSize(new Dimension(180,30));
        backButton.setFont(new Font("SansSerif", Font.BOLD, 16));
        gbc.gridx = 0; gbc.gridy = 5; gbc.gridwidth = 2; gbc.anchor = GridBagConstraints.CENTER;
        searchPanel.add(backButton, gbc);

        add(searchPanel, BorderLayout.NORTH);

        // ====== RESULTS TABLE ======
        // tableModel = new DefaultTableModel(
        //         new String[]{"Case ID", "Defendant", "Case Type", "Status", "DOB", "Hearing Date", "ACTION"},
        //         0
        // );
        
        

        String[] columns = {
                "Case ID", "Status", "Defendant", "Case Type",  "DOB", "Hearing Date", "ACTION"
        };

        // tableModel = new DefaultTableModel(columns, 0);
        
        tableModel = new DefaultTableModel(columns, 0);
        resultsTable = new JTable(tableModel);
        // tableModel = new DefaultTableModel(columns, 0) {
        //     @Override
        //     public boolean isCellEditable(int row, int column) {
        //         return column == 5;   // Only Action column is editable
        //     }
        // };
        

        // resultsTable = new JTable(tableModel);

        
        // Style table header
        resultsTable.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 16));
        resultsTable.getTableHeader().setPreferredSize(new Dimension(0,35));

        // Add View button column
        resultsTable.getColumn("ACTION").setCellRenderer(new ButtonRenderer());
        resultsTable.getColumn("ACTION").setCellEditor(new ButtonEditor(new JCheckBox(), mainMenu));

        

        add(new JScrollPane(resultsTable), BorderLayout.CENTER);
        // refresh();
        getAllArchives();

        // ====== BUTTON ACTIONS ======
        // searchButton.addActionListener(e -> performSearch());
        backButton.addActionListener(e -> mainMenu.switchToMainMenu());

    }

    public static void getAllArchives() {
        // String query = searchField.getText().trim();

        
        // String defendant = defendantField.getText().trim();
        // String caseType = caseTypeBox.getSelectedItem().toString();
        // String dob = dobField.getText().trim();
        // String hearingDate = hearingDateField.getText().trim();

        // Get results from DB
        // ArrayList<Case> results = DatabaseHelper.searchCases(query);
        ArrayList<Case> results = DatabaseHelper.getArchives();

        // Clear table
        tableModel.setRowCount(0);

        // Fill table
        for (Case c : results) {
            tableModel.addRow(new Object[]{
                    c.getCaseId(),
                    c.getStatus(),
                    c.getDefendantName(),
                    c.getCaseType(),
                    c.getDefendantDOB(),
                    c.getHearingDate(),
                    "view"
            });
        }
    }

    public static void refresh(){
        getAllArchives();
    }
    
}
