package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;

import academy.Academy;
import academy.Hero;
import data.DataManager;

public class HeroPanel extends JPanel {

    private final MainFrame mainFrame;
    private final Academy academy;
    private final DataManager dataManager;

    private JTable heroTable;
    private DefaultTableModel tableModel;
    private JTextField addNameField;
    private JTextField targetHeroIdField;
    private JTextField editNameField;

    public HeroPanel(MainFrame mainFrame, Academy academy, DataManager dataManager) {
        this.mainFrame = mainFrame;
        this.academy = academy;
        this.dataManager = dataManager;

        initComponents();
    }

    private void initComponents() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        // Table setup
        String[] columnNames = {"ID", "Name", "Level", "Powers", "Training Cost", "Monthly Allowance"};
        tableModel = GUIUtils.createReadOnlyTableModel(columnNames);

        heroTable = new JTable(tableModel);
        heroTable.setRowHeight(26);
        heroTable.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        heroTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));
        heroTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        // Adjust column widths
        int[] widths = {50, 140, 60, 260, 110, 130};
        for (int i = 0; i < widths.length; i++) {
            heroTable.getColumnModel().getColumn(i).setPreferredWidth(widths[i]);
        }

        JScrollPane scrollPane = new JScrollPane(heroTable);
        add(scrollPane, BorderLayout.CENTER);

        // Action Panel at bottom
        JPanel actionsContainer = new JPanel(new GridLayout(2, 1, 10, 10));
        actionsContainer.setBorder(BorderFactory.createEmptyBorder(5, 0, 5, 0));

        // Row 1: Add Hero
        JPanel addRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        addRow.add(new JLabel("New Hero Name:"));
        addNameField = new JTextField(18);
        addRow.add(addNameField);

        JButton addButton = new JButton("➕ Add Hero");
        addButton.setFont(new Font("Segoe UI", Font.BOLD, 12));
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleAddHero();
            }
        });
        addRow.add(addButton);
        actionsContainer.add(addRow);

        // Row 2: Hero ID, New Name, Edit, Copy & Delete Hero
        JPanel editDeleteRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        editDeleteRow.add(new JLabel("Hero ID:"));
        targetHeroIdField = new JTextField(5);
        targetHeroIdField.setFont(new Font("Segoe UI", Font.BOLD, 12));
        editDeleteRow.add(targetHeroIdField);

        editDeleteRow.add(new JLabel("New Name:"));
        editNameField = new JTextField(12);
        editDeleteRow.add(editNameField);

        JButton updateButton = new JButton("✏️ Edit Name");
        updateButton.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        updateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleUpdateHero();
            }
        });
        editDeleteRow.add(updateButton);

        JButton copyButton = new JButton("📋 Copy Hero");
        copyButton.setFont(new Font("Segoe UI", Font.BOLD, 12));
        copyButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleCopyHero();
            }
        });
        editDeleteRow.add(copyButton);

        JButton deleteButton = new JButton("🗑️ Delete Hero");
        deleteButton.setFont(new Font("Segoe UI", Font.BOLD, 12));
        deleteButton.setForeground(new Color(180, 40, 40));
        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleDeleteHero();
            }
        });
        editDeleteRow.add(deleteButton);

        actionsContainer.add(editDeleteRow);

        add(actionsContainer, BorderLayout.SOUTH);
    }

    private void handleAddHero() {
        String name = addNameField.getText().trim();
        if (name.isEmpty()) {
            return;
        }

        academy.addHero(name);
        dataManager.saveHeroes(academy.getHeroes(), academy.getBalance());
        addNameField.setText("");

        mainFrame.refreshAll();
    }

    private void handleUpdateHero() {
        String idText = targetHeroIdField.getText().trim();
        String newName = editNameField.getText().trim();
        if (idText.isEmpty() || newName.isEmpty()) {
            return;
        }

        try {
            int heroId = Integer.parseInt(idText);
            boolean updated = academy.updateHero(heroId, newName);

            if (updated) {
                dataManager.saveHeroes(academy.getHeroes(), academy.getBalance());
                targetHeroIdField.setText("");
                editNameField.setText("");
                mainFrame.refreshAll();
            }
        } catch (NumberFormatException ignored) {
        }
    }

    private void handleCopyHero() {
        String idText = targetHeroIdField.getText().trim();
        if (idText.isEmpty()) {
            return;
        }

        try {
            int heroId = Integer.parseInt(idText);
            Hero copy = academy.copyHero(heroId);
            if (copy != null) {
                dataManager.saveHeroes(academy.getHeroes(), academy.getBalance());
                targetHeroIdField.setText("");
                mainFrame.refreshAll();
            }
        } catch (NumberFormatException ignored) {
        }
    }

    private void handleDeleteHero() {
        String idText = targetHeroIdField.getText().trim();
        if (idText.isEmpty()) {
            return;
        }

        try {
            int heroId = Integer.parseInt(idText);
            if (academy.deleteHero(heroId)) {
                dataManager.saveHeroes(academy.getHeroes(), academy.getBalance());
                targetHeroIdField.setText("");
                editNameField.setText("");
                mainFrame.refreshAll();
            }
        } catch (NumberFormatException ignored) {
        }
    }

    public void refresh() {
        tableModel.setRowCount(0);
        ArrayList<Hero> heroes = academy.getHeroes();

        for (Hero hero : heroes) {
            tableModel.addRow(new Object[]{
                    hero.getId(),
                    hero.getName(),
                    hero.getLevel(),
                    GUIUtils.formatPowers(hero),
                    String.format("$%.2f", hero.getTrainingCost()),
                    String.format("$%.2f", hero.getMonthlyAllowance())
            });
        }
    }
}
