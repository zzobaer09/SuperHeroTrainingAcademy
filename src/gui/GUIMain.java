package gui;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;

public class GUIMain {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception ignored) {
                // Fall back to default Swing look and feel
            }

            MainFrame frame = new MainFrame();
            frame.setVisible(true);
        });
    }
}
