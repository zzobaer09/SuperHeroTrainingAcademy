package academy;

import java.util.ArrayList;
import java.util.Random;

//import threat.Threat;
import id.IdGenerator;

public class Hero {

    private IdGenerator id;
    private String name;
    private int level;
    private ArrayList<Power> powers;

    public Hero(String name) {

        this.id = new IdGenerator();
        this.name = name;
        this.level = 1;
        this.powers = new ArrayList<>();

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

    public void train(int amount) {
        level += amount;
    }

    public void updateName(String name) {
        this.name = name;
    }

    // public boolean isEligible(Threat threat) {

    //     if (level < threat.getRequiredLevel()) {
    //         return false;
    //     }

    //     for (Power power : powers) {

    //         if (power.getType().equalsIgnoreCase(
    //                 threat.getRequiredPower())) {

    //             return true;
    //         }
    //     }

    //     return false;
    // }

    // @Override
    // public double getTrainingCost() {
    //     return 100 + 25 * powers.size() + 10 * level;
    // }

    // @Override
    // public int getTrainingTime() {
    //     return 30 + 15 * powers.size() + 5 * level;
    // }

    // @Override
    // public double getMonthlyAllowance() {
    //     return 500 + 100 * level + 25 * powers.size();
    // }

    // @Override
    // public double getThreatReward(Threat threat) {
    //     return 250 * threat.getRequiredLevel() + 50 * level;
    // }

    public String getId() {
        return id.getId();
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