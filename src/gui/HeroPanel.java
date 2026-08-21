package gui;

import java.awt.*;
import java.util.ArrayList;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import academy.Academy;
import academy.Hero;
import academy.Power;
import data.DataManager;
import finance.FinanceManager;

public class HeroPanel extends JPanel {

    private final MainFrame mainFrame;
    private final Academy academy;
    private final DataManager dataManager;
    private final FinanceManager financeManager;

    private JTable heroTable;
    private DefaultTableModel tableModel;
    private JTextField addNameField;
    private JTextField editNameField;

    public HeroPanel(MainFrame mainFrame, Academy academy, DataManager dataManager, FinanceManager financeManager) {
        this.mainFrame = mainFrame;
        this.academy = academy;
        this.dataManager = dataManager;
        this.financeManager = financeManager;

        initComponents();
    }

    private void initComponents() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        // Table setup
        String[] columnNames = {"ID", "Name", "Level", "Powers", "Training Cost", "Monthly Allowance"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // read-only table
            }
        };

        heroTable = new JTable(tableModel);
        heroTable.setRowHeight(26);
        heroTable.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        heroTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));
        heroTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        // Adjust column widths
        heroTable.getColumnModel().getColumn(0).setPreferredWidth(50);
        heroTable.getColumnModel().getColumn(1).setPreferredWidth(140);
        heroTable.getColumnModel().getColumn(2).setPreferredWidth(60);
        heroTable.getColumnModel().getColumn(3).setPreferredWidth(260);
        heroTable.getColumnModel().getColumn(4).setPreferredWidth(110);
        heroTable.getColumnModel().getColumn(5).setPreferredWidth(130);

        // Listen for table selection to auto-populate edit text field
        heroTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int selectedRow = heroTable.getSelectedRow();
                if (selectedRow != -1) {
                    String name = (String) tableModel.getValueAt(selectedRow, 1);
                    editNameField.setText(name);
                }
            }
        });

        JScrollPane scrollPane = new JScrollPane(heroTable);
        add(scrollPane, BorderLayout.CENTER);

        // Action Panel at bottom
        JPanel actionsContainer = new JPanel(new GridLayout(2, 1, 10, 10));
        actionsContainer.setBorder(BorderFactory.createTitledBorder("Hero Management Actions"));

        // Row 1: Add Hero
        JPanel addRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        addRow.add(new JLabel("New Hero Name:"));
        addNameField = new JTextField(18);
        addRow.add(addNameField);

        JButton addButton = new JButton("➕ Add Hero");
        addButton.setFont(new Font("Segoe UI", Font.BOLD, 12));
        addButton.addActionListener(e -> handleAddHero());
        addRow.add(addButton);
        actionsContainer.add(addRow);

        // Row 2: Edit Name & Delete Hero
        JPanel editDeleteRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        editDeleteRow.add(new JLabel("Selected Hero Name:"));
        editNameField = new JTextField(15);
        editDeleteRow.add(editNameField);

        JButton updateButton = new JButton("✏️ Edit Name");
        updateButton.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        updateButton.addActionListener(e -> handleUpdateHero());
        editDeleteRow.add(updateButton);

        editDeleteRow.add(Box.createHorizontalStrut(20));

        JButton deleteButton = new JButton("🗑️ Delete Selected Hero");
        deleteButton.setFont(new Font("Segoe UI", Font.BOLD, 12));
        deleteButton.setForeground(new Color(180, 40, 40));
        deleteButton.addActionListener(e -> handleDeleteHero());
        editDeleteRow.add(deleteButton);

        actionsContainer.add(editDeleteRow);

        add(actionsContainer, BorderLayout.SOUTH);
    }

    private void handleAddHero() {
        String name = addNameField.getText().trim();
        if (name.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter a hero name.", "Input Required", JOptionPane.WARNING_MESSAGE);
            return;
        }

        academy.addHero(name);
        dataManager.saveHeroes(academy.getHeroes(), academy.getBalance());
        addNameField.setText("");

        mainFrame.refreshAll();
        JOptionPane.showMessageDialog(this, "Hero \"" + name + "\" added successfully!", "Hero Added", JOptionPane.INFORMATION_MESSAGE);
    }

    private void handleUpdateHero() {
        int selectedRow = heroTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a hero from the table to edit.", "Selection Required", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String newName = editNameField.getText().trim();
        if (newName.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Hero name cannot be empty.", "Input Required", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int heroId = (int) tableModel.getValueAt(selectedRow, 0);
        boolean updated = academy.updateHero(heroId, newName);

        if (updated) {
            dataManager.saveHeroes(academy.getHeroes(), academy.getBalance());
            mainFrame.refreshAll();
            JOptionPane.showMessageDialog(this, "Hero ID " + heroId + " updated to \"" + newName + "\".", "Hero Updated", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(this, "Failed to update hero.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void handleDeleteHero() {
        int selectedRow = heroTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a hero from the table to delete.", "Selection Required", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int heroId = (int) tableModel.getValueAt(selectedRow, 0);
        String heroName = (String) tableModel.getValueAt(selectedRow, 1);

        int confirm = JOptionPane.showConfirmDialog(
                this,
                "Are you sure you want to delete Hero " + heroName + " (ID: " + heroId + ")?",
                "Confirm Hero Deletion",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE
        );

        if (confirm == JOptionPane.YES_OPTION) {
            if (academy.deleteHero(heroId)) {
                dataManager.deleteSavedHero(heroId);
                dataManager.saveHeroes(academy.getHeroes(), academy.getBalance());
                editNameField.setText("");
                mainFrame.refreshAll();
                JOptionPane.showMessageDialog(this, "Hero " + heroName + " deleted successfully.", "Deleted", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "Could not find hero to delete.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    public void refresh() {
        tableModel.setRowCount(0);
        ArrayList<Hero> heroes = academy.getHeroes();

        for (Hero hero : heroes) {
            StringBuilder powersStr = new StringBuilder();
            ArrayList<Power> powers = hero.getPowers();
            for (int i = 0; i < powers.size(); i++) {
                powersStr.append(powers.get(i).getType());
                if (i < powers.size() - 1) {
                    powersStr.append(", ");
                }
            }

            tableModel.addRow(new Object[]{
                    hero.getId(),
                    hero.getName(),
                    hero.getLevel(),
                    powersStr.toString(),
                    String.format("$%.2f", hero.getTrainingCost()),
                    String.format("$%.2f", hero.getMonthlyAllowance())
            });
        }
    }
}
