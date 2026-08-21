package gui;

import java.awt.*;
import java.util.ArrayList;
import javax.swing.*;
import academy.Academy;
import academy.Hero;
import academy.Power;
import data.DataManager;
import finance.FinanceManager;

public class TrainingPanel extends JPanel {

    private final MainFrame mainFrame;
    private final Academy academy;
    private final DataManager dataManager;
    private final FinanceManager financeManager;

    private JPanel cardsContainer;

    public TrainingPanel(MainFrame mainFrame, Academy academy, DataManager dataManager, FinanceManager financeManager) {
        this.mainFrame = mainFrame;
        this.academy = academy;
        this.dataManager = dataManager;
        this.financeManager = financeManager;

        initComponents();
    }

    private void initComponents() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        // Header instruction
        JLabel headerLabel = new JLabel("⚡ Select any hero below to level up and improve their combat abilities:");
        headerLabel.setFont(new Font("Segoe UI", Font.BOLD, 13));
        add(headerLabel, BorderLayout.NORTH);

        cardsContainer = new JPanel();
        cardsContainer.setLayout(new BoxLayout(cardsContainer, BoxLayout.Y_AXIS));

        JScrollPane scrollPane = new JScrollPane(cardsContainer);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        add(scrollPane, BorderLayout.CENTER);
    }

    public void refresh() {
        cardsContainer.removeAll();
        ArrayList<Hero> heroes = academy.getHeroes();

        if (heroes.isEmpty()) {
            JPanel emptyPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 40));
            JLabel emptyLabel = new JLabel("No heroes available to train. Add heroes in the Hero Roster tab first.");
            emptyLabel.setFont(new Font("Segoe UI", Font.ITALIC, 14));
            emptyLabel.setForeground(Color.GRAY);
            emptyPanel.add(emptyLabel);
            cardsContainer.add(emptyPanel);
        } else {
            for (Hero hero : heroes) {
                cardsContainer.add(createHeroCard(hero));
                cardsContainer.add(Box.createVerticalStrut(12));
            }
        }

        cardsContainer.revalidate();
        cardsContainer.repaint();
    }

    private JPanel createHeroCard(Hero hero) {
        JPanel card = new JPanel(new BorderLayout(15, 10));
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(203, 213, 225), 1),
                BorderFactory.createEmptyBorder(12, 16, 12, 16)
        ));
        card.setBackground(new Color(248, 250, 252));
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 110));

        // Left info panel
        JPanel infoPanel = new JPanel(new GridLayout(3, 1, 4, 4));
        infoPanel.setOpaque(false);

        // Line 1: Name and Level progression
        JLabel titleLabel = new JLabel(String.format("🦸 %s (ID: %d)   |   Level %d  ➔  Next: Level %d",
                hero.getName(), hero.getId(), hero.getLevel(), hero.getLevel() + 1));
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        infoPanel.add(titleLabel);

        // Line 2: Powers
        StringBuilder powersStr = new StringBuilder();
        ArrayList<Power> powers = hero.getPowers();
        for (int i = 0; i < powers.size(); i++) {
            powersStr.append(powers.get(i).getType());
            if (i < powers.size() - 1) {
                powersStr.append(", ");
            }
        }
        JLabel powersLabel = new JLabel("Active Powers: " + powersStr);
        powersLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        powersLabel.setForeground(new Color(51, 65, 85));
        infoPanel.add(powersLabel);

        // Line 3: Cost and Time
        JLabel metaLabel = new JLabel(String.format("Required Training Cost: $%.2f   |   Training Time: %d mins",
                hero.getTrainingCost(), hero.getTrainingTime()));
        metaLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        metaLabel.setForeground(new Color(71, 85, 105));
        infoPanel.add(metaLabel);

        card.add(infoPanel, BorderLayout.CENTER);

        // Right button panel
        JPanel buttonPanel = new JPanel(new GridBagLayout());
        buttonPanel.setOpaque(false);

        JButton trainBtn = new JButton("⚡ TRAIN HERO (+1 LEVEL)");
        trainBtn.setFont(new Font("Segoe UI", Font.BOLD, 12));
        trainBtn.setBackground(new Color(234, 179, 8)); // Amber/Yellow
        trainBtn.setFocusPainted(false);

        trainBtn.addActionListener(e -> {
            int prevLevel = hero.getLevel();
            academy.trainHero(hero.getId());
            dataManager.saveHeroes(academy.getHeroes(), academy.getBalance());
            mainFrame.refreshAll();
            JOptionPane.showMessageDialog(
                    this,
                    hero.getName() + " trained successfully!\nLevel: " + prevLevel + " ➔ " + hero.getLevel(),
                    "Training Complete",
                    JOptionPane.INFORMATION_MESSAGE
            );
        });

        buttonPanel.add(trainBtn);
        card.add(buttonPanel, BorderLayout.EAST);

        return card;
    }
}
