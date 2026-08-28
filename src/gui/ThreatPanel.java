package gui;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
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
import exceptions.HeroNotEligibleException;
import threat.Threat;

public class ThreatPanel extends JPanel {

    private final MainFrame mainFrame;
    private final Academy academy;
    private final DataManager dataManager;

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

    public ThreatPanel(MainFrame mainFrame, Academy academy, DataManager dataManager) {
        this.mainFrame = mainFrame;
        this.academy = academy;
        this.dataManager = dataManager;

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
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(40, 20, 40, 20));

        JLabel titleLabel = new JLabel("🚨 Threat Scanner & Dispatch Radar");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(titleLabel);

        panel.add(Box.createVerticalStrut(15));

        idleStatusLabel = new JLabel("System Ready. Click below to scan the city for threats.");
        idleStatusLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        idleStatusLabel.setForeground(new Color(71, 85, 105));
        idleStatusLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(idleStatusLabel);

        panel.add(Box.createVerticalStrut(25));

        JButton scanButton = new JButton("🚨 Scan for Threat");
        scanButton.setFont(new Font("Segoe UI", Font.BOLD, 16));
        scanButton.setBackground(new Color(220, 38, 38)); // Bright Red
        scanButton.setForeground(Color.WHITE);
        scanButton.setFocusPainted(false);
        scanButton.setMaximumSize(new Dimension(240, 50));
        scanButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        scanButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleThreatScan();
            }
        });

        panel.add(scanButton);

        return panel;
    }

    private JPanel createThreatActivePanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        // Threat Details Card (North)
        JPanel threatCard = new JPanel(new GridLayout(5, 1, 4, 4));
        threatCard.setBorder(BorderFactory.createEmptyBorder(10, 14, 10, 14));
        threatCard.setBackground(new Color(254, 242, 242)); // Light red background

        JLabel threatHeaderLabel = new JLabel("⚠️ ACTIVE THREAT DETECTED");
        threatHeaderLabel.setFont(new Font("Segoe UI", Font.BOLD, 13));
        threatHeaderLabel.setForeground(new Color(185, 28, 28));

        threatTypeLabel = new JLabel();
        threatTypeLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        threatTypeLabel.setForeground(new Color(153, 27, 27));

        threatReqLevelLabel = new JLabel();
        threatReqLevelLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));

        threatReqPowerLabel = new JLabel();
        threatReqPowerLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));

        threatDescLabel = new JLabel();
        threatDescLabel.setFont(new Font("Segoe UI", Font.ITALIC, 13));

        threatCard.add(threatHeaderLabel);
        threatCard.add(threatTypeLabel);
        threatCard.add(threatReqLevelLabel);
        threatCard.add(threatReqPowerLabel);
        threatCard.add(threatDescLabel);

        panel.add(threatCard, BorderLayout.NORTH);

        // Center: Available Heroes Reference Table
        JPanel centerPanel = new JPanel(new BorderLayout(5, 5));
        centerPanel.setBorder(BorderFactory.createEmptyBorder(5, 0, 5, 0));

        String[] cols = {"ID", "Name", "Level", "Powers"};
        rosterTableModel = GUIUtils.createReadOnlyTableModel(cols);

        JTable rosterTable = new JTable(rosterTableModel);
        rosterTable.setRowHeight(24);
        rosterTable.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        rosterTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
        rosterTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        JScrollPane scrollPane = new JScrollPane(rosterTable);
        centerPanel.add(scrollPane, BorderLayout.CENTER);
        panel.add(centerPanel, BorderLayout.CENTER);

        // South: Dispatch Controls
        JPanel dispatchControlPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        dispatchControlPanel.setBorder(BorderFactory.createEmptyBorder(5, 0, 5, 0));

        dispatchControlPanel.add(new JLabel("Enter Hero ID:"));
        dispatchHeroIdField = new JTextField(8);
        dispatchHeroIdField.setFont(new Font("Segoe UI", Font.BOLD, 13));
        dispatchControlPanel.add(dispatchHeroIdField);

        JButton dispatchBtn = new JButton("🚀 Dispatch Hero");
        dispatchBtn.setFont(new Font("Segoe UI", Font.BOLD, 12));
        dispatchBtn.setBackground(new Color(22, 163, 74)); // Green
        dispatchBtn.setFocusPainted(false);
        dispatchBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleDispatch();
            }
        });
        dispatchControlPanel.add(dispatchBtn);

        JButton declineBtn = new JButton("❌ Decline");
        declineBtn.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        declineBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleDecline();
            }
        });
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
            rosterTableModel.addRow(new Object[]{
                    hero.getId(),
                    hero.getName(),
                    hero.getLevel(),
                    GUIUtils.formatPowers(hero)
            });
        }
    }

    private void handleDispatch() {
        String idText = dispatchHeroIdField.getText().trim();
        if (idText.isEmpty()) {
            return;
        }

        int heroId;
        try {
            heroId = Integer.parseInt(idText);
        } catch (NumberFormatException e) {
            return;
        }

        try {
            academy.dispatchHero(heroId, currentThreat);

            dataManager.saveHeroes(academy.getHeroes(), academy.getBalance());
            mainFrame.refreshAll();

            // Reset back to idle state
            currentThreat = null;
            idleStatusLabel.setText("Mission successful! City is safe.");
            idleStatusLabel.setForeground(new Color(22, 163, 74));
            cardLayout.show(mainContainer, "IDLE");

        } catch (HeroNotEligibleException e) {
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
