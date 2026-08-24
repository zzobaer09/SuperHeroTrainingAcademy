package data;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

import academy.Academy;
import academy.Hero;
import academy.Power;

public class DataManager {

    public void saveHeroes(ArrayList<Hero> heroes, double balance) {

        try {

            FileWriter myWriter = new FileWriter("heroes.txt");

            myWriter.write("BALANCE," + balance + "\n");

            for (Hero hero : heroes) {
                myWriter.write(heroToLine(hero) + "\n");
            }

            myWriter.close();

        } catch (IOException e) {

            System.out.println("Error saving heroes.");

        }
    }

    public void loadHeroes(Academy academy) {

        try {

            File myObj = new File("heroes.txt");
            Scanner myReader = new Scanner(myObj);

            while (myReader.hasNextLine()) {

                String line = myReader.nextLine();

                if (line.trim().isEmpty()) {
                    continue;
                }

                String[] data = line.split(",");

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

            myReader.close();

        } catch (IOException e) {

            System.out.println("No saved heroes found.");

        }
    }

    private static String heroToLine(Hero hero) {

        String line = hero.getId() + "," + hero.getName() + "," + hero.getLevel();

        for (Power power : hero.getPowers()) {
            line = line + "," + power.getType();
        }

        return line;
    }
}