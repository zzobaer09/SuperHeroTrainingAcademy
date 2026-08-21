package data;

import java.io.*;
import java.util.ArrayList;
import academy.Hero;
import academy.Power;
import academy.Academy;

public class DataManager {

    private static final String BALANCE_PREFIX = "BALANCE,";

    public void saveHeroes(ArrayList<Hero> heroes) {
        saveHeroes(heroes, readBalanceFromFile());
    }

    public void saveHeroes(ArrayList<Hero> heroes, double balance) {

        try {

            PrintWriter writer =
                    new PrintWriter(new FileWriter("heroes.txt"));

            writer.println(BALANCE_PREFIX + balance);

            for (Hero hero : heroes) {
                writer.println(heroToLine(hero));
            }

            writer.close();

        } catch (IOException e) {

            System.out.println("Error saving heroes.");

        }
    }

    public void loadHeroes(Academy academy) {

        try {

            BufferedReader reader =
                    new BufferedReader(new FileReader("heroes.txt"));

            String line;

            while ((line = reader.readLine()) != null) {

                String[] data = splitLine(line);

                if (data.length == 0) {
                    continue;
                }

                // Restore the academy treasury balance
                if (data[0].equals("BALANCE")) {

                    try {

                        academy.addFunds(Double.parseDouble(data[1]));

                    } catch (NumberFormatException e) {

                        System.out.println("Skipping corrupted balance: " + line);
                    }

                    continue;
                }

                if (data.length < 3) {
                    continue;
                }

                try {

                    int id = Integer.parseInt(data[0]);
                    String name = data[1];
                    int level = Integer.parseInt(data[2]);

                    ArrayList<Power> powers = new ArrayList<>();

                    for (int i = 3; i < data.length; i++) {

                        powers.add(new Power(data[i]));

                    }

                    Hero hero = new Hero(id, name, level, powers);

                    academy.addExistingHero(hero);

                } catch (NumberFormatException e) {

                    System.out.println("Skipping corrupted hero data: " + line);

                }
            }

            reader.close();

        } catch (IOException e) {

            System.out.println("No saved heroes found.");

        }
    }

    public void deleteSavedHero(int heroId) {

        try {

            File file = new File("heroes.txt");

            if (!file.exists()) {
                return;
            }

            BufferedReader reader =
                    new BufferedReader(new FileReader(file));

            ArrayList<String> lines = new ArrayList<>();

            String line;

            while ((line = reader.readLine()) != null) {
                lines.add(line);
            }

            reader.close();

            PrintWriter writer =
                    new PrintWriter(new FileWriter(file));

            for (String saved : lines) {

                String[] data = splitLine(saved);

                if (data.length >= 3 && data[0].equals(String.valueOf(heroId))) {
                    continue;
                }

                writer.println(saved);
            }

            writer.close();

        } catch (IOException e) {

            System.out.println("Error deleting hero.");

        }
    }

    public void showSavedHeroes() {

        try {

            BufferedReader reader =
                    new BufferedReader(new FileReader("heroes.txt"));

            String line;

            System.out.println();
            System.out.println("========================================");
            System.out.println("           SAVED HEROES");
            System.out.println("========================================");

            boolean found = false;

            while ((line = reader.readLine()) != null) {

                String[] data = splitLine(line);

                if (data.length >= 3) {

                    System.out.println();
                    System.out.println("Hero ID : " + data[0]);
                    System.out.println("Name    : " + data[1]);
                    System.out.println("Level   : " + data[2]);

                    System.out.println("Powers:");

                    if (data.length > 3) {

                        for (int i = 3; i < data.length; i++) {

                            System.out.println("  - " + data[i]);

                        }

                    } else {

                        System.out.println("  No powers saved.");

                    }

                    found = true;
                }
            }

            reader.close();

            if (!found) {
                System.out.println();
                System.out.println("No saved heroes found.");
            }

            System.out.println();
            System.out.println("========================================");

        } catch (IOException e) {

            System.out.println();
            System.out.println("No saved heroes found.");

        }
    }

    private double readBalanceFromFile() {

        try {

            BufferedReader reader =
                    new BufferedReader(new FileReader("heroes.txt"));

            String line;

            while ((line = reader.readLine()) != null) {

                String[] data = splitLine(line);

                if (data.length >= 2 && data[0].equals("BALANCE")) {

                    try {

                        return Double.parseDouble(data[1]);

                    } catch (NumberFormatException ignored) {
                        return 0;
                    } finally {

                        reader.close();
                    }
                }
            }

            reader.close();

        } catch (IOException ignored) {
            // No file yet - balance stays 0
        }

        return 0;
    }

    private static String heroToLine(Hero hero) {

        StringBuilder heroData = new StringBuilder();

        heroData.append(hero.getId()).append(",");
        heroData.append(escapeName(hero.getName())).append(",");
        heroData.append(hero.getLevel());

        for (Power power : hero.getPowers()) {

            heroData.append(",");
            heroData.append(power.getType());

        }

        return heroData.toString();
    }

    private static String escapeName(String name) {
        return name.replace("\\", "\\\\").replace(",", "\\,");
    }

    private static String[] splitLine(String line) {

        ArrayList<String> fields = new ArrayList<>();
        StringBuilder current = new StringBuilder();

        for (int i = 0; i < line.length(); i++) {

            char c = line.charAt(i);

            if (c == '\\' && i + 1 < line.length()) {

                char next = line.charAt(i + 1);

                if (next == ',' || next == '\\') {

                    current.append(next);
                    i++;
                    continue;
                }
            }

            if (c == ',') {

                fields.add(current.toString());
                current.setLength(0);

            } else {

                current.append(c);
            }
        }

        fields.add(current.toString());

        return fields.toArray(new String[0]);
    }
}