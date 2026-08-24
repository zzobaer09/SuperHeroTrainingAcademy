package finance;

import java.util.ArrayList;

import academy.Hero;

public class FinanceManager {

    private double taxRate;

    public FinanceManager() {
        this.taxRate = 0.10;
    }

    // Total monthly allowance of all heroes
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

    public double getTaxRate() {
        return taxRate;
    }

}