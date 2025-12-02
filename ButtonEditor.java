import javax.swing.*;
import java.awt.*;


public class ButtonEditor extends DefaultCellEditor {
    private JButton button;
    private boolean clicked;
    private MainMenu mainMenu;
    private int caseId;

    public ButtonEditor(JCheckBox checkBox, MainMenu mainMenu) {
        super(checkBox);
        this.mainMenu = mainMenu;
        button = new JButton("View");
        button.setOpaque(true);
        button.addActionListener(e -> fireEditingStopped());
    }

    @Override
    public Component getTableCellEditorComponent(JTable table, Object value,
                                                 boolean isSelected, int row, int column) {
        caseId = Integer.parseInt(table.getValueAt(row, 0).toString()); // 0 = Case ID column
        clicked = true;
        return button;
    }

    @Override
    public Object getCellEditorValue() {
        if (clicked) {
            mainMenu.showViewCasePanel(caseId);  // switch to ViewCasePanel
        }
        clicked = false;
        return "View";
    }
}


