package gui;

import java.util.ArrayList;
import javax.swing.table.DefaultTableModel;

import academy.Hero;
import academy.Power;

public class GUIUtils {

    // Helper table model that prevents user from editing cells directly
    static class ReadOnlyTableModel extends DefaultTableModel {

        public ReadOnlyTableModel(String[] columnNames) {
            super(columnNames, 0);
        }

        @Override
        public boolean isCellEditable(int row, int column) {
            return false;
        }
    }

    public static String formatPowers(Hero hero) {
        ArrayList<Power> powers = hero.getPowers();
        String result = "";

        for (int i = 0; i < powers.size(); i++) {
            result += powers.get(i).getType();
            if (i < powers.size() - 1) {
                result += ", ";
            }
        }

        return result;
    }

    public static DefaultTableModel createReadOnlyTableModel(String[] columnNames) {
        return new ReadOnlyTableModel(columnNames);
    }
}

