package gui;

import java.awt.*;
import javax.swing.*;
import academy.Academy;
import data.DataManager;
import finance.FinanceManager;

public class MainFrame extends JFrame {

    private final Academy academy;
    private final DataManager dataManager;
    private final FinanceManager financeManager;

    private JLabel balanceLabel;
    private HeroPanel heroPanel;
    private TrainingPanel trainingPanel;
    private ThreatPanel threatPanel;
    private FinancePanel financePanel;
    private JTabbedPane tabbedPane;

    public MainFrame() {
        super("Superhero Training Academy");

        this.academy = new Academy();
        this.dataManager = new DataManager();
        this.financeManager = new FinanceManager();

        // Load saved heroes and treasury balance
        dataManager.loadHeroes(academy);

        initComponents();
    }

    private void initComponents() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(980, 680);
        setMinimumSize(new Dimension(860, 560));
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Header Bar
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(30, 41, 59));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(12, 18, 12, 18));

        JLabel titleLabel = new JLabel("🚀 SUPERHERO TRAINING ACADEMY");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        titleLabel.setForeground(Color.WHITE);
        headerPanel.add(titleLabel, BorderLayout.WEST);

        JPanel rightHeaderPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 12, 0));
        rightHeaderPanel.setOpaque(false);

        balanceLabel = new JLabel();
        balanceLabel.setFont(new Font("Segoe UI", Font.BOLD, 15));
        balanceLabel.setForeground(new Color(52, 211, 153)); // Light Emerald Green
        rightHeaderPanel.add(balanceLabel);

        JButton refreshBtn = new JButton("🔄 Refresh");
        refreshBtn.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        refreshBtn.setFocusPainted(false);
        refreshBtn.addActionListener(e -> refreshAll());
        rightHeaderPanel.add(refreshBtn);

        headerPanel.add(rightHeaderPanel, BorderLayout.EAST);
        add(headerPanel, BorderLayout.NORTH);

        // Tabbed Pane
        tabbedPane = new JTabbedPane();
        tabbedPane.setFont(new Font("Segoe UI", Font.BOLD, 13));

        heroPanel = new HeroPanel(this, academy, dataManager, financeManager);
        trainingPanel = new TrainingPanel(this, academy, dataManager, financeManager);
        threatPanel = new ThreatPanel(this, academy, dataManager, financeManager);
        financePanel = new FinancePanel(this, academy, dataManager, financeManager);

        tabbedPane.addTab("👥 1. Hero Roster", heroPanel);
        tabbedPane.addTab("⚡ 2. Training Station", trainingPanel);
        tabbedPane.addTab("🚨 3. Threat Dispatch", threatPanel);
        tabbedPane.addTab("📊 4. Finance", financePanel);

        tabbedPane.addChangeListener(e -> refreshAll());

        add(tabbedPane, BorderLayout.CENTER);

        refreshAll();
    }

    public void refreshAll() {
        balanceLabel.setText(String.format("Treasury Balance: $%.2f", academy.getBalance()));
        if (heroPanel != null) heroPanel.refresh();
        if (trainingPanel != null) trainingPanel.refresh();
        if (threatPanel != null) threatPanel.refresh();
        if (financePanel != null) financePanel.refresh();
    }

    public Academy getAcademy() {
        return academy;
    }

    public DataManager getDataManager() {
        return dataManager;
    }

    public FinanceManager getFinanceManager() {
        return financeManager;
    }
}
