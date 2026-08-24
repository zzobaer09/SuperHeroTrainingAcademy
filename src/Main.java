import java.util.Scanner;

import academy.Academy;
import academy.Hero;
import data.DataManager;
import exceptions.HeroNotEligibleException;
import finance.FinanceManager;
import threat.Threat;

public class Main {

    public static void main(String[] args) {

        Scanner input = new Scanner(System.in);

        Academy academy = new Academy();
        DataManager dataManager = new DataManager();
        FinanceManager finance = new FinanceManager();

        // Load saved heroes and balance first
        dataManager.loadHeroes(academy);

        int choice;

        do {

            System.out.println("\n========================================");
            System.out.println("     SUPERHERO TRAINING ACADEMY");
            System.out.println("========================================");
            System.out.println();
            System.out.println("1. Add Hero");
            System.out.println("2. Train Hero");
            System.out.println("3. Update Hero");
            System.out.println("4. Delete Hero");
            System.out.println("5. Show Heroes");
            System.out.println("6. Threat Check");
            System.out.println("7. Finance Summary");
            System.out.println("8. Exit");
            System.out.println();
            System.out.print("Enter choice: ");

            choice = readInt(input);

            switch (choice) {

                case 1: {

                    System.out.print("Enter Hero Name: ");
                    String name = input.nextLine();

                    academy.addHero(name);

                    Hero newlyAddedHero =
                            academy.getHeroes().get(academy.getHeroes().size() - 1);

                    System.out.println("\n========================================");
                    System.out.println("          HERO CREATED!");
                    System.out.println("========================================");
                    System.out.println(newlyAddedHero);
                    System.out.println("========================================");

                    dataManager.saveHeroes(academy.getHeroes(), academy.getBalance());

                    System.out.println("Hero saved automatically.");

                    pressEnterToContinue(input);

                    break;
                }

                case 2: {

                    if (academy.getHeroes().isEmpty()) {

                        System.out.println("\nNo heroes available to train.\n");

                        pressEnterToContinue(input);

                        break;
                    }

                    System.out.println("\n-------- AVAILABLE HEROES --------");
                    academy.showHeroes();
                    System.out.println("----------------------------------");

                    System.out.println("\nEnter Hero ID to train: ");
                    int trainId = readInt(input);

                    Hero hero = academy.findHero(trainId);

                    if (hero == null) {

                        System.out.println("\nHero with ID " + trainId + " does not exist.\n");

                    } else {

                        double cost = hero.getTrainingCost();
                        int time = hero.getTrainingTime();

                        academy.trainHero(trainId);

                        System.out.println("\n========================================");
                        System.out.println("           HERO TRAINED!");
                        System.out.println("========================================");
                        System.out.println(hero);
                        System.out.println("----------------------------------------");
                        System.out.println("Cost required : $" + String.format("%.2f", cost));
                        System.out.println("Training time : " + time + " minutes");
                        System.out.println("========================================");

                        dataManager.saveHeroes(academy.getHeroes(), academy.getBalance());

                        System.out.println("\nUpdated hero data saved automatically.\n");
                    }

                    pressEnterToContinue(input);

                    break;
                }

                case 3: {

                    if (academy.getHeroes().isEmpty()) {

                        System.out.println("\nNo heroes available to update.\n");

                        pressEnterToContinue(input);

                        break;
                    }

                    System.out.println("\n-------- AVAILABLE HEROES --------");
                    academy.showHeroes();
                    System.out.println("----------------------------------");

                    System.out.println("\nEnter Hero ID to update: ");
                    int updateId = readInt(input);

                    if (academy.findHero(updateId) == null) {

                        System.out.println("\nHero with ID " + updateId + " does not exist.\n");

                    } else {

                        System.out.print("Enter new name: ");
                        String newName = input.nextLine();

                        if (newName.trim().isEmpty()) {

                            System.out.println("\nName cannot be empty.\n");

                        } else {

                            academy.updateHero(updateId, newName);

                            dataManager.saveHeroes(academy.getHeroes(), academy.getBalance());

                            System.out.println("\nHero updated and saved automatically.\n");
                        }
                    }

                    pressEnterToContinue(input);

                    break;
                }

                case 4: {

                    if (academy.getHeroes().isEmpty()) {

                        System.out.println("\nNo heroes available to delete.\n");

                        pressEnterToContinue(input);

                        break;
                    }

                    System.out.println("\n-------- AVAILABLE HEROES --------");
                    academy.showHeroes();
                    System.out.println("----------------------------------");

                    System.out.println("\nEnter Hero ID to delete: ");
                    int deleteId = readInt(input);

                    if (academy.deleteHero(deleteId)) {

                        dataManager.saveHeroes(academy.getHeroes(), academy.getBalance());

                        System.out.println("\nHero with ID " + deleteId + " deleted.\n");

                    } else {

                        System.out.println("\nHero with ID " + deleteId + " does not exist.\n");
                    }

                    pressEnterToContinue(input);

                    break;
                }

                case 5:

                    System.out.println("\n========================================");
                    System.out.println("             ALL HEROES");
                    System.out.println("========================================");
                    academy.showHeroes();
                    System.out.println("========================================");

                    pressEnterToContinue(input);

                    break;

                case 6: {

                    Threat threat = academy.threatCheck();

                    if (threat == null) {

                        System.out.println("\nNo threats detected.\n");

                    } else {

                        System.out.println("\n========== THREAT DETECTED ==========");
                        System.out.println(threat);
                        System.out.println("=====================================\n");

                        while (true) {

                            System.out.println("-------- AVAILABLE HEROES --------");
                            academy.showHeroes();
                            System.out.println("----------------------------------");

                            System.out.print("Enter Hero ID to dispatch (0 to decline): ");
                            int heroId = readInt(input);

                            if (heroId == 0) {

                                System.out.println("\nThreat declined.\n");
                                break;
                            }

                            try {

                                double reward = academy.dispatchHero(heroId, threat);

                                System.out.println("\n========================================");
                                System.out.println("        THREAT HANDLED!");
                                System.out.println("========================================");
                                System.out.println("Hero " + academy.findHero(heroId).getName()
                                        + " handled the " + threat.getType() + " threat.");
                                System.out.println("Reward earned: $" + String.format("%.2f", reward));
                                System.out.println("Treasury balance: $" + String.format("%.2f", academy.getBalance()));
                                System.out.println("========================================");

                                dataManager.saveHeroes(academy.getHeroes(), academy.getBalance());

                                System.out.println("\nData saved automatically.\n");

                                break;

                            } catch (HeroNotEligibleException e) {

                                System.out.println("\n" + e.getMessage());
                                System.out.println("\nPlease choose another hero.\n");
                            }
                        }
                    }

                    pressEnterToContinue(input);

                    break;
                }

                case 7: {

                    int count = academy.getHeroes().size();
                    double gross = finance.getMonthlyAllowance(academy.getHeroes());
                    double net = gross * (1 - finance.getTaxRate());
                    double trainCost = finance.getTotalTrainingCost(academy.getHeroes());

                    System.out.println("\n========================================");
                    System.out.println("          FINANCE SUMMARY");
                    System.out.println("========================================");
                    System.out.println("Heroes in academy        : " + count);
                    System.out.println("Treasury balance (earned): $" + String.format("%.2f", academy.getBalance()));
                    System.out.println("Gross monthly allowances: $" + String.format("%.2f", gross));
                    System.out.println("Allowances after " + (int) (finance.getTaxRate() * 100) + "% tax: $"
                            + String.format("%.2f", net));
                    System.out.println("Cost to train all heroes : $" + String.format("%.2f", trainCost));

                    if (count > 0) {

                        System.out.println("\nPer hero details:");
                        System.out.println("----------------------------------------");

                        for (Hero hero : academy.getHeroes()) {

                            System.out.println("ID " + hero.getId() + " - " + hero.getName()
                                    + " (Level " + hero.getLevel() + ", " + hero.getPowers().size() + " powers)");
                            System.out.println("   Training cost  : $" + String.format("%.2f", hero.getTrainingCost()));
                            System.out.println("   Training time  : " + hero.getTrainingTime() + " minutes");
                            System.out.println("   Monthly allowance: $" + String.format("%.2f", hero.getMonthlyAllowance()));
                        }
                    }

                    System.out.println("========================================");

                    pressEnterToContinue(input);

                    break;
                }

                case 8:

                    System.out.println("\n\nThank you for using Superhero Training Academy!\n");
                    System.out.println("Program terminated.\n");

                    break;
            }

        } while (choice != 8);
    }

    public static int readInt(Scanner input) {

        while (true) {

            String line = input.nextLine().trim();

            try {

                return Integer.parseInt(line);

            } catch (NumberFormatException e) {

                System.out.println("Invalid input. Please enter a valid number.");
            }
        }
    }

    public static void pressEnterToContinue(Scanner input) {

        System.out.println();
        System.out.print("Press ENTER to return to the main menu...");

        input.nextLine();
    }
}
