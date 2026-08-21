package gui;

import java.awt.*;
import java.util.ArrayList;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import academy.Academy;
import academy.Hero;
import data.DataManager;
import finance.FinanceManager;

public class FinancePanel extends JPanel {

    private final MainFrame mainFrame;
    private final Academy academy;
    private final DataManager dataManager;
    private final FinanceManager financeManager;

    private JLabel countValueLabel;
    private JLabel balanceValueLabel;
    private JLabel grossValueLabel;
    private JLabel netValueLabel;
    private JLabel totalTrainCostValueLabel;

    private JTable financeTable;
    private DefaultTableModel tableModel;

    public FinancePanel(MainFrame mainFrame, Academy academy, DataManager dataManager, FinanceManager financeManager) {
        this.mainFrame = mainFrame;
        this.academy = academy;
        this.dataManager = dataManager;
        this.financeManager = financeManager;

        initComponents();
    }

    private void initComponents() {
        setLayout(new BorderLayout(10, 15));
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        // Top Summary Dashboard
        JPanel summaryPanel = new JPanel(new GridLayout(2, 3, 12, 12));
        summaryPanel.setBorder(BorderFactory.createTitledBorder("Financial Overview & Treasury Metrics"));

        countValueLabel = new JLabel("0");
        balanceValueLabel = new JLabel("$0.00");
        grossValueLabel = new JLabel("$0.00");
        netValueLabel = new JLabel("$0.00");
        totalTrainCostValueLabel = new JLabel("$0.00");

        summaryPanel.add(createMetricCard("🦸 Total Heroes", countValueLabel, new Color(59, 130, 246)));
        summaryPanel.add(createMetricCard("💰 Treasury Balance", balanceValueLabel, new Color(16, 185, 129)));
        summaryPanel.add(createMetricCard("📅 Gross Monthly Allowances", grossValueLabel, new Color(245, 158, 11)));
        summaryPanel.add(createMetricCard("📉 Net Allowances (after " + (int)(financeManager.getTaxRate() * 100) + "% tax)", netValueLabel, new Color(139, 92, 246)));
        summaryPanel.add(createMetricCard("⚡ Cost to Train All Heroes", totalTrainCostValueLabel, new Color(239, 68, 68)));

        // Empty card for alignment or refresh button
        JPanel extraPanel = new JPanel(new GridBagLayout());
        extraPanel.setBackground(new Color(248, 250, 252));
        extraPanel.setBorder(BorderFactory.createLineBorder(new Color(226, 232, 240)));
        JButton refreshBtn = new JButton("🔄 Recalculate Finances");
        refreshBtn.setFont(new Font("Segoe UI", Font.BOLD, 12));
        refreshBtn.addActionListener(e -> mainFrame.refreshAll());
        extraPanel.add(refreshBtn);
        summaryPanel.add(extraPanel);

        add(summaryPanel, BorderLayout.NORTH);

        // Bottom Table: Per-Hero Breakdown
        JPanel tableContainer = new JPanel(new BorderLayout(5, 5));
        tableContainer.setBorder(BorderFactory.createTitledBorder("Individual Hero Financial Breakdown"));

        String[] columns = {"Hero ID", "Name", "Level", "Powers Count", "Training Cost", "Training Time", "Monthly Allowance"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        financeTable = new JTable(tableModel);
        financeTable.setRowHeight(24);
        financeTable.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        financeTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));

        JScrollPane scrollPane = new JScrollPane(financeTable);
        tableContainer.add(scrollPane, BorderLayout.CENTER);

        add(tableContainer, BorderLayout.CENTER);
    }

    private JPanel createMetricCard(String title, JLabel valueLabel, Color accentColor) {
        JPanel card = new JPanel(new BorderLayout(5, 5));
        card.setBackground(new Color(248, 250, 252));
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(226, 232, 240), 1),
                BorderFactory.createEmptyBorder(10, 14, 10, 14)
        ));

        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        titleLabel.setForeground(new Color(100, 116, 139));
        card.add(titleLabel, BorderLayout.NORTH);

        valueLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        valueLabel.setForeground(accentColor);
        card.add(valueLabel, BorderLayout.CENTER);

        return card;
    }

    public void refresh() {
        ArrayList<Hero> heroes = academy.getHeroes();
        int count = heroes.size();
        double treasury = academy.getBalance();
        double gross = financeManager.getMonthlyAllowance(heroes);
        double net = gross * (1 - financeManager.getTaxRate());
        double trainCost = financeManager.getTotalTrainingCost(heroes);

        countValueLabel.setText(String.valueOf(count));
        balanceValueLabel.setText(String.format("$%.2f", treasury));
        grossValueLabel.setText(String.format("$%.2f", gross));
        netValueLabel.setText(String.format("$%.2f", net));
        totalTrainCostValueLabel.setText(String.format("$%.2f", trainCost));

        tableModel.setRowCount(0);
        for (Hero hero : heroes) {
            tableModel.addRow(new Object[]{
                    hero.getId(),
                    hero.getName(),
                    hero.getLevel(),
                    hero.getPowers().size() + " powers",
                    String.format("$%.2f", hero.getTrainingCost()),
                    hero.getTrainingTime() + " mins",
                    String.format("$%.2f", hero.getMonthlyAllowance())
            });
        }
    }
}
