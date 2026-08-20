package finance;

import java.util.ArrayList;

import academy.Hero;
import academy.Trainable;
import threat.Threat;

public class FinanceManager {

    private double taxRate;

    public FinanceManager() {
        this(0.10);
    }

    public FinanceManager(double taxRate) {
        this.taxRate = taxRate;
    }

    // Monthly allowance of a single hero (overload 1)
    public double getMonthlyAllowance(Trainable hero) {
        return hero.getMonthlyAllowance();
    }

    // Total monthly allowance of all heroes (overload 2)
    public double getMonthlyAllowance(ArrayList<Hero> heroes) {

        double total = 0;

        for (Hero hero : heroes) {
            total += hero.getMonthlyAllowance();
        }

        return total;
    }

    // Total cost of training every hero once
    public double getTotalTrainingCost(ArrayList<Hero> heroes) {

        double total = 0;

        for (Hero hero : heroes) {
            total += hero.getTrainingCost();
        }

        return total;
    }

    // Income earned by a hero for handling a threat
    public double getThreatIncome(Hero hero, Threat threat) {
        return hero.getThreatReward(threat);
    }

    public double getTaxRate() {
        return taxRate;
    }

}