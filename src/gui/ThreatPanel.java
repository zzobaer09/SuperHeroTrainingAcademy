package gui;

import java.awt.*;
import java.util.ArrayList;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import academy.Academy;
import academy.Hero;
import academy.Power;
import data.DataManager;
import exceptions.HeroNotEligibleException;
import finance.FinanceManager;
import threat.Threat;

public class ThreatPanel extends JPanel {

    private final MainFrame mainFrame;
    private final Academy academy;
    private final DataManager dataManager;
    private final FinanceManager financeManager;

    private CardLayout cardLayout;
    private JPanel mainContainer;

    // Idle View components
    private JLabel idleStatusLabel;

    // Threat View components
    private Threat currentThreat;
    private JLabel threatTypeLabel;
    private JLabel threatReqLevelLabel;
    private JLabel threatReqPowerLabel;
    private JLabel threatDescLabel;
    private JTextField dispatchHeroIdField;
    private DefaultTableModel rosterTableModel;

    public ThreatPanel(MainFrame mainFrame, Academy academy, DataManager dataManager, FinanceManager financeManager) {
        this.mainFrame = mainFrame;
        this.academy = academy;
        this.dataManager = dataManager;
        this.financeManager = financeManager;

        initComponents();
    }

    private void initComponents() {
        setLayout(new BorderLayout());
        cardLayout = new CardLayout();
        mainContainer = new JPanel(cardLayout);

        mainContainer.add(createIdlePanel(), "IDLE");
        mainContainer.add(createThreatActivePanel(), "THREAT");

        add(mainContainer, BorderLayout.CENTER);
        cardLayout.show(mainContainer, "IDLE");
    }

    private JPanel createIdlePanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.CENTER;

        JLabel titleLabel = new JLabel("🚨 Threat Scanner & Dispatch Radar");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        gbc.gridy = 0;
        panel.add(titleLabel, gbc);

        idleStatusLabel = new JLabel("System Ready. Click below to scan the city for threats.");
        idleStatusLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        idleStatusLabel.setForeground(new Color(71, 85, 105));
        gbc.gridy = 1;
        panel.add(idleStatusLabel, gbc);

        JButton scanButton = new JButton("🚨 Scan for Threat");
        scanButton.setFont(new Font("Segoe UI", Font.BOLD, 16));
        scanButton.setBackground(new Color(220, 38, 38)); // Bright Red
        scanButton.setForeground(Color.WHITE);
        scanButton.setFocusPainted(false);
        scanButton.setPreferredSize(new Dimension(240, 50));

        scanButton.addActionListener(e -> handleThreatScan());

        gbc.gridy = 2;
        panel.add(scanButton, gbc);

        return panel;
    }

    private JPanel createThreatActivePanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        // Threat Details Card (North)
        JPanel threatCard = new JPanel(new GridLayout(4, 1, 6, 6));
        threatCard.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(220, 38, 38), 2),
                "⚠️ ACTIVE THREAT DETECTED"
        ));
        threatCard.setBackground(new Color(254, 242, 242)); // Light red background

        threatTypeLabel = new JLabel();
        threatTypeLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        threatTypeLabel.setForeground(new Color(153, 27, 27));

        threatReqLevelLabel = new JLabel();
        threatReqLevelLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));

        threatReqPowerLabel = new JLabel();
        threatReqPowerLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));

        threatDescLabel = new JLabel();
        threatDescLabel.setFont(new Font("Segoe UI", Font.ITALIC, 13));

        threatCard.add(threatTypeLabel);
        threatCard.add(threatReqLevelLabel);
        threatCard.add(threatReqPowerLabel);
        threatCard.add(threatDescLabel);

        panel.add(threatCard, BorderLayout.NORTH);

        // Center: Available Heroes Reference Table
        JPanel centerPanel = new JPanel(new BorderLayout(5, 5));
        centerPanel.setBorder(BorderFactory.createTitledBorder("Available Heroes Roster (Check eligibility before dispatching)"));

        String[] cols = {"ID", "Name", "Level", "Powers"};
        rosterTableModel = new DefaultTableModel(cols, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };

        JTable rosterTable = new JTable(rosterTableModel);
        rosterTable.setRowHeight(24);
        rosterTable.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        rosterTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
        rosterTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        rosterTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int row = rosterTable.getSelectedRow();
                if (row != -1) {
                    dispatchHeroIdField.setText(String.valueOf(rosterTableModel.getValueAt(row, 0)));
                }
            }
        });

        JScrollPane scrollPane = new JScrollPane(rosterTable);
        centerPanel.add(scrollPane, BorderLayout.CENTER);
        panel.add(centerPanel, BorderLayout.CENTER);

        // South: Dispatch Controls
        JPanel dispatchControlPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        dispatchControlPanel.setBorder(BorderFactory.createTitledBorder("Hero Dispatch"));

        dispatchControlPanel.add(new JLabel("Enter Hero ID:"));
        dispatchHeroIdField = new JTextField(8);
        dispatchHeroIdField.setFont(new Font("Segoe UI", Font.BOLD, 13));
        dispatchControlPanel.add(dispatchHeroIdField);

        JButton dispatchBtn = new JButton("🚀 Dispatch Hero");
        dispatchBtn.setFont(new Font("Segoe UI", Font.BOLD, 12));
        dispatchBtn.setBackground(new Color(22, 163, 74)); // Green
        dispatchBtn.setFocusPainted(false);
        dispatchBtn.addActionListener(e -> handleDispatch());
        dispatchControlPanel.add(dispatchBtn);

        JButton declineBtn = new JButton("❌ Decline");
        declineBtn.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        declineBtn.addActionListener(e -> handleDecline());
        dispatchControlPanel.add(declineBtn);

        panel.add(dispatchControlPanel, BorderLayout.SOUTH);

        return panel;
    }

    private void handleThreatScan() {
        Threat threat = academy.threatCheck();

        if (threat == null) {
            idleStatusLabel.setText("Radar Clear! City is safe.");
            idleStatusLabel.setForeground(new Color(22, 163, 74)); // Green
            cardLayout.show(mainContainer, "IDLE");
        } else {
            this.currentThreat = threat;
            displayThreat(threat);
            updateRosterTable();
            dispatchHeroIdField.setText("");
            cardLayout.show(mainContainer, "THREAT");
        }
    }

    private void displayThreat(Threat threat) {
        threatTypeLabel.setText("Threat Type: " + threat.getType());
        threatReqLevelLabel.setText("Required Level: " + threat.getRequiredLevel());
        threatReqPowerLabel.setText("Required Power: " + threat.getRequiredPower());
        threatDescLabel.setText("Description: " + threat.getDescription());
    }

    private void updateRosterTable() {
        rosterTableModel.setRowCount(0);
        for (Hero hero : academy.getHeroes()) {
            StringBuilder powersStr = new StringBuilder();
            ArrayList<Power> powers = hero.getPowers();
            for (int i = 0; i < powers.size(); i++) {
                powersStr.append(powers.get(i).getType());
                if (i < powers.size() - 1) powersStr.append(", ");
            }
            rosterTableModel.addRow(new Object[]{
                    hero.getId(),
                    hero.getName(),
                    hero.getLevel(),
                    powersStr.toString()
            });
        }
    }

    private void handleDispatch() {
        String idText = dispatchHeroIdField.getText().trim();
        if (idText.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter or select a Hero ID to dispatch.", "Input Required", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int heroId;
        try {
            heroId = Integer.parseInt(idText);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Please enter a valid numeric Hero ID.", "Invalid Input", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            Hero hero = academy.findHero(heroId);
            double reward = academy.dispatchHero(heroId, currentThreat);

            dataManager.saveHeroes(academy.getHeroes(), academy.getBalance());
            mainFrame.refreshAll();

            String heroName = (hero != null) ? hero.getName() : ("ID " + heroId);
            JOptionPane.showMessageDialog(
                    this,
                    "Threat Successfully Neutralized!\n\nHero: " + heroName +
                            "\nThreat: " + currentThreat.getType() +
                            "\nReward Earned: $" + String.format("%.2f", reward) +
                            "\nTreasury Balance: $" + String.format("%.2f", academy.getBalance()),
                    "Mission Complete",
                    JOptionPane.INFORMATION_MESSAGE
            );

            // Reset back to idle state
            currentThreat = null;
            idleStatusLabel.setText("Mission successful! City is safe.");
            idleStatusLabel.setForeground(new Color(22, 163, 74));
            cardLayout.show(mainContainer, "IDLE");

        } catch (HeroNotEligibleException e) {
            JOptionPane.showMessageDialog(
                    this,
                    e.getMessage() + "\nPlease choose another hero or decline.",
                    "Dispatch Ineligible",
                    JOptionPane.WARNING_MESSAGE
            );
            dispatchHeroIdField.requestFocus();
        }
    }

    private void handleDecline() {
        currentThreat = null;
        idleStatusLabel.setText("Threat was declined. Radar standing by.");
        idleStatusLabel.setForeground(new Color(202, 138, 4)); // Yellow-amber
        cardLayout.show(mainContainer, "IDLE");
    }

    public void refresh() {
        updateRosterTable();
    }
}
