package academy;

import java.util.ArrayList;
import java.util.Random;

import threat.Threat;

public class Hero implements Trainable {

    // Shared counter across all heroes: increments by 1 each time a hero is created
    private static int nextId = 1;

    private int id;
    private String name;
    private int level;
    private ArrayList<Power> powers;

    // Constructor for creating a new hero (starts at Level 1 with random powers)
    public Hero(String name) {

        this.id = nextId++;
        this.name = name;
        this.level = 1;
        this.powers = new ArrayList<>();

        generateRandomPowers();
    }
    
    public Hero(int id, String name, int level, ArrayList<Power> powers) {

        this.id = id;
        this.name = name;
        this.level = level;
        this.powers = powers;

        if (id >= nextId) {
            nextId = id + 1;
        }
    }

    // Copy Constructor (Deep Copy)
    public Hero(Hero other) {
        this.id = nextId++;
        this.name = other.name + " (Copy)";
        this.level = other.level;
        this.powers = new ArrayList<>();

        for (Power power : other.powers) {
            this.powers.add(new Power(power));
        }
    }

    private void generateRandomPowers() {

        Random random = new Random();

        String[] allPowers = {
                "Fire",
                "Strength",
                "Speed",
                "Tech",
                "Telepathy",
                "Water",
                "Ice",
                "Lightning"
        };

        int numberOfPowers = random.nextInt(3) + 3;

        while (powers.size() < numberOfPowers) {

            String randomPower =
                    allPowers[random.nextInt(allPowers.length)];

            boolean exists = false;

            for (Power power : powers) {

                if (power.getType().equalsIgnoreCase(randomPower)) {
                    exists = true;
                    break;
                }
            }

            if (!exists) {

                powers.add(new Power(randomPower));
            }
        }
    }

    public void train() {
        level++;
    }

    public void updateName(String name) {
        this.name = name;
    }

    public boolean isEligible(Threat threat) {

        if (level < threat.getRequiredLevel()) {
            return false;
        }

        for (Power power : powers) {

            if (power.getType().equalsIgnoreCase(
                    threat.getRequiredPower())) {

                return true;
            }
        }

        return false;
    }

    @Override
    public double getTrainingCost() {
        return 100 + 25 * powers.size() + 10 * level;
    }

    @Override
    public int getTrainingTime() {
        return 30 + 15 * powers.size() + 5 * level;
    }

    @Override
    public double getMonthlyAllowance() {
        return 500 + 100 * level + 25 * powers.size();
    }

    @Override
    public double getThreatReward(Threat threat) {
        return 250 * threat.getRequiredLevel() + 50 * level;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getLevel() {
        return level;
    }

    public ArrayList<Power> getPowers() {
        return powers;
    }

    @Override
    public String toString() {

        String result = "";

        result += "Hero ID: " + id + "\n";
        result += "Name: " + name + "\n";
        result += "Level: " + level + "\n";
        result += "Powers:\n";

        for (Power power : powers) {
            result += "- " + power + "\n";
        }

        return result;
    }
}
