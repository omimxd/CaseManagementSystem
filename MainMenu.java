import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.security.PublicKey;
import java.util.ArrayList;

public class MainMenu extends JFrame {

    private CardLayout cardLayout;
    private JPanel containerPanel;

    
    private SearchMenuPanel searchMenuPanel;
    private CreateCasePanel createCasePanel;
    private ViewCasePanel viewCasePanel;
    private ResolveCasePanel resolveCasePanel;
    private SummariesPanel summariesPanel;
    private ArchivePanel archivePanel;

    private DefaultTableModel model;  // <-- make it a class field
    private JTable table;
    private ArrayList<Case> casesList;

    

    public MainMenu() {
        

        setTitle("Case Management System");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // ===== MAIN CARDLAYOUT CONTAINER  =====
        cardLayout = new CardLayout();
        containerPanel = new JPanel(cardLayout);

        // Main Menu panel
        JPanel mainMenuPanel = createMainMenuPanel();

        // Other panels
        searchMenuPanel = new SearchMenuPanel(this);
        createCasePanel = new CreateCasePanel(this);
        viewCasePanel = new ViewCasePanel(this);
        resolveCasePanel = new ResolveCasePanel(this);
        summariesPanel = new SummariesPanel(this);
        archivePanel = new ArchivePanel(this);

        // Adding panels to container
        containerPanel.add(mainMenuPanel, "MainMenu");
        containerPanel.add(createCasePanel, "CreateCase");
        containerPanel.add(searchMenuPanel, "SearchMenu");
        containerPanel.add(viewCasePanel, "ViewCase");
        containerPanel.add(resolveCasePanel, "ResolveCase");
        containerPanel.add(summariesPanel, "Summaries");
        
        containerPanel.add(archivePanel, "Archive");

        add(containerPanel);
        setVisible(true);
    }

    // ===== Menu Switching Methods =====
    public void switchToMainMenu() {
        cardLayout.show(containerPanel, "MainMenu");
    }

    public void switchToSearchMenu() {
        cardLayout.show(containerPanel, "SearchMenu");
    }

    public void switchToArchiveMenu() {
        cardLayout.show(archivePanel, "Archive");
    }

    // 1. ===== Create Main Menu Panel =====
    private JPanel createMainMenuPanel() {
        JPanel panel = new JPanel(new BorderLayout());

        DatabaseHelper.createDatabaseAndTables();
        // === TABLE SECTION (Top 50%) ===
        String[] columnNames = {"Case ID", "Case Title", "Status", "Defendant", "Date Created", "ACTION"};

        model = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 5;   // Only Action column is editable
            }
        };

        table = new JTable(model);

        // Header styling
        table.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 16));
        table.getTableHeader().setPreferredSize(new Dimension(0, 35));

        // Add View button column
        table.getColumn("ACTION").setCellRenderer(new ButtonRenderer());
        table.getColumn("ACTION").setCellEditor(new ButtonEditor(new JCheckBox(), this));

        JScrollPane scrollPane = new JScrollPane(table);

        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.add(scrollPane, BorderLayout.CENTER);
        tablePanel.setPreferredSize(new Dimension(800, 300));
        panel.add(tablePanel, BorderLayout.NORTH);

        // === BUTTON SECTION ===
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 20));

        JButton createBtn = new JButton("Create Case");
        JButton searchBtn = new JButton("Search");
        JButton exitBtn = new JButton("Exit");
        JButton archiveBtn = new JButton("Archive");

        Dimension btnSize = new Dimension(180, 50);
        Font btnFont = new Font("SansSerif", Font.BOLD, 16);
        createBtn.setPreferredSize(btnSize); createBtn.setFont(btnFont);
        searchBtn.setPreferredSize(btnSize); searchBtn.setFont(btnFont);
        exitBtn.setPreferredSize(btnSize); exitBtn.setFont(btnFont);

        Dimension archBtnSize = new Dimension(180, 30);
        archiveBtn.setPreferredSize(archBtnSize); archiveBtn.setFont(btnFont);

        // Button actions
        createBtn.addActionListener(e -> cardLayout.show(containerPanel, "CreateCase"));
        searchBtn.addActionListener(e -> cardLayout.show(containerPanel, "SearchMenu"));
        exitBtn.addActionListener(e -> System.exit(0));
        archiveBtn.addActionListener(e -> {
            cardLayout.show(containerPanel, "Archive");
            ArchivePanel.refresh();
        });

        buttonPanel.add(exitBtn);
        buttonPanel.add(searchBtn);
        buttonPanel.add(createBtn);
        buttonPanel.add(archiveBtn);
        

        panel.add(buttonPanel, BorderLayout.CENTER);

        loadCasesToTable();

        return panel;
    }

    //2. ===== Load cases to Main Menu Table =====
    public void loadCasesToTable() {
        // ArrayList<Case> casesList = DatabaseHelper.getAllCases();
        casesList = DatabaseHelper.getAllCases();

        // Clears existing rows
        model.setRowCount(0);

        for (Case c : casesList) {
            model.addRow(new Object[]{
                    c.getCaseId(),
                    c.getCaseTitle(),
                    c.getStatus(),
                    c.getDefendantName(),
                    c.getDateCreated(), 
                    "View"               // Action column
            });
        }
    }

    // 3. ===== Refresh Table =====
    public void refreshTable() {
        loadCasesToTable();
    }

    // 4. ===== View Case Panel =====
    public void showViewCasePanel(int caseId) {
        viewCasePanel.setCaseDetails(caseId);
        cardLayout.show(containerPanel, "ViewCase");
    }

    // Show Search Panel
    public void showSearchPanel() {
        cardLayout.show(containerPanel, "SearchMenu");
    }

    public void showResolveCasePanel(int caseId) {
        if (resolveCasePanel == null) {
            resolveCasePanel = new ResolveCasePanel(this);
            containerPanel.add(resolveCasePanel, "ResolveCase");
        }

        resolveCasePanel.setCaseId(caseId); 
        cardLayout.show(containerPanel, "ResolveCase");
    }

    public void showSummariesPanel(int caseId){
        if (summariesPanel == null) {
            summariesPanel = new SummariesPanel(this);
            containerPanel.add(summariesPanel, "ResolveCase");
        }
        summariesPanel.setCaseId(caseId);
        summariesPanel.refresh();
        cardLayout.show(containerPanel, "Summaries");
    }

    public void showPanel(String name) {
        cardLayout.show(containerPanel, name);
    }

    
    
    // -------------------- MAIN --------------------
    public static void main(String[] args) {
        new MainMenu();
    }
}