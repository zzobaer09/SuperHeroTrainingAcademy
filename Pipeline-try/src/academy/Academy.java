package academy;

import java.util.ArrayList;
import java.util.Random;

import exceptions.HeroNotEligibleException;
import threat.FireThreat;
import threat.RobberyThreat;
import threat.Threat;
import threat.VillainThreat;

public class Academy {

    private ArrayList<Hero> heroes;
    private double balance;

    public Academy() {
        heroes = new ArrayList<>();
        balance = 0;
    }

    // Add a new hero
    public void addHero(String name) {
        heroes.add(new Hero(name));
    }
    
    public void addExistingHero(Hero hero) {

        if (findHero(hero.getId()) == null) {
            heroes.add(hero);
        }

    }

    // Update an existing hero's name
    public boolean updateHero(int heroId, String newName) {

        Hero hero = findHero(heroId);

        if (hero == null) {
            return false;
        }

        hero.updateName(newName);

        return true;
    }

    // Delete a hero by ID
    public boolean deleteHero(int heroId) {

        for (int i = 0; i < heroes.size(); i++) {

            if (heroes.get(i).getId() == heroId) {

                heroes.remove(i);

                return true;
            }
        }

        return false;
    }

    // Return all heroes
    public ArrayList<Hero> getHeroes() {
        return heroes;
    }

    // Train a hero by ID
    public boolean trainHero(int heroId) {

        for (Hero hero : heroes) {

            if (hero.getId() == heroId) {

                hero.train();

                return true;
            }
        }

        return false;
    }

    // Display all heroes
    public void showHeroes() {

        if (heroes.isEmpty()) {
            System.out.println("No heroes available.");
            return;
        }

        for (Hero hero : heroes) {
            System.out.println(hero);
        }

    }

    // Find hero by ID
    public Hero findHero(int heroId) {

        for (Hero hero : heroes) {

            if (hero.getId() == heroId) {
                return hero;
            }

        }

        return null;
    }

    // Generate a random threat
    public Threat threatCheck() {

        Random random = new Random();

        int chance = random.nextInt(4);

        switch (chance) {

            case 0:
                return null;

            case 1:
                return new FireThreat();

            case 2:
                return new RobberyThreat();

            default:
                return new VillainThreat();

        }

    }

    // Dispatch hero to a threat and pay the reward into the treasury
    public double dispatchHero(int heroId, Threat threat)
            throws HeroNotEligibleException {

        Hero hero = findHero(heroId);

        if (hero == null) {
            throw new HeroNotEligibleException("Hero not found.");
        }

        if (!hero.isEligible(threat)) {
            throw new HeroNotEligibleException("Hero not eligible!");
        }

        return payThreatReward(hero, threat);

    }

    // Calculate the reward for handling a threat and add it to the balance
    public double payThreatReward(Hero hero, Threat threat) {

        double reward = hero.getThreatReward(threat);

        balance += reward;

        return reward;
    }

    public void addFunds(double amount) {
        balance += amount;
    }

    public double getBalance() {
        return balance;
    }

}