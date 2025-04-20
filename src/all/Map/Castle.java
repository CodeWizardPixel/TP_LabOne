package all.Map;

import all.Units.*;
import all.Utils.GameLogger;

import java.io.Serializable;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.logging.Logger;

public class Castle implements Serializable {
    private static final Logger logger = GameLogger.getLogger();
    private char symbol;
    private boolean isOpponent;
    private int posX;
    private int posY;
    private int gold = 1000;
    private static final Scanner scanner = new Scanner(System.in);
    private List<Unit> recruitedUnits = new ArrayList<>();

    String[] buildingNames = {
            "Tavern",
            "Stable",
            "Guard Post",
            "The Tower of Crossbowmen",
            "Armory",
            "Arena",
            "Cathedral"
    };
    Boolean[] buildings = new Boolean[buildingNames.length];;

    public Castle(char symbol, boolean isOpponent, int posX, int posY) {
        this.isOpponent = isOpponent;
        this.symbol = symbol;
        this.posX = posX;
        this.posY = posY;
        buildings[0] = true;

        if(isOpponent) {
            for (int i = 0; i < buildingNames.length; i++) {
                buildings[i] = true;
            }
        }
    }

    public boolean getIsOpponent() {
        return isOpponent;
    }
    public char getSymbol() {
        return symbol;
    }

    public int getPosX() {
        return posX;
    }

    public void setPosX(int posX) {
        this.posX = posX;
    }

    public int getPosY() {
        return posY;
    }

    public void setPosY(int posY) {
        this.posY = posY;
    }

    public Hero showCastleUI(Hero hero) {
        while(true) {
            System.out.println("Castle Interface Options:");
            System.out.println("0) Buy Hero");
            System.out.println("1) Purchase Buildings");
            System.out.println("2) Recruit Units");
            System.out.println("3) Retrieve Units");
            System.out.println("4) View Stored Units");
            System.out.println("(q) Exit");
            String input = scanner.nextLine().toLowerCase();
            if(input.equals("0")) {
                if(hero == null) {
                    if(gold >= 2000) {
                        gold -= 2000;
                        hero = new Hero('A', 50, false, posX, posY, 5);
                        System.out.println("New hero purchased!");
                    } else {
                        System.out.println("Not enough gold to buy a hero");
                    }
                } else {
                    logger.severe("Hero already exists. Cannot buy another one.");
                }
            } else if(input.equals("1")) {
                purchaseBuildings();
            } else if(input.equals("2")) {
                recruitUnits();
            } else if(input.equals("3")) {
                if(hero == null) {
                    System.out.println("No hero available to retrieve units. Please buy a hero first");
                } else {
                    if(recruitedUnits.size() > 0) {
                        retrieveUnits(hero);
                    } else {
                        System.out.println("No units to retrieve");
                    }
                }
            } else if(input.equals("4")) {
                viewRecruitedUnits();
            } else if(input.equals("q")) {
                break;
            } else {
                System.out.println("Invalid input. Please try again");
            }
        }
        return hero;
    }

    private int getBuildingCost(String buildingName) {
        switch (buildingName.toLowerCase()) {
            case "tavern": return 300;
            case "stable": return 500;
            case "guard post": return 200;
            case "tower of crossbowmen": 
            case "the tower of crossbowmen": return 300;
            case "armory": return 400;
            case "arena": return 500;
            case "cathedral": return 600;
            default: return 0;
        }
    }

    private void purchaseBuildings() {
        System.out.println("Current gold: " + gold);
        System.out.println("Available buildings to purchase:");
        for (int i = 0; i < buildingNames.length; i++) {
            if (buildings[i] != null && buildings[i]) {
                System.out.println((i + 1) + ") " + "\u001B[33m" + buildingNames[i] + " (Built)" + "\u001B[0m");
            } else {
                int price = getBuildingCost(buildingNames[i]);
                String color = (gold >= price) ? "\u001B[32m" : "\u001B[31m";
                System.out.println((i + 1) + ") " + color + buildingNames[i] + " (Cost: " + price + " gold)" + "\u001B[0m");
            }
        }
        System.out.println("Enter the number of the building to purchase or 'q' to go back:");
        String input = scanner.nextLine();
        if (input.toLowerCase().equals("q"))
            return;
        try {
            int choice = Integer.parseInt(input);
            if (choice > 0 && choice <= buildingNames.length) {
                String building = buildingNames[choice - 1];
                purchaseBuilding(building);
            } else {
                System.out.println("Invalid choice");
            }
        } catch (NumberFormatException e) {
            System.out.println("Invalid input");
        }
    }

    private void recruitUnits() {
        System.out.println("Current gold: " + gold);
        System.out.println("Choose unit to recruit:");
        
        String opt1Color = (buildings[2] != null && buildings[2] && gold >= 100) ? "\u001B[32m" : "\u001B[31m";
        String opt2Color = (buildings[3] != null && buildings[3] && gold >= 150) ? "\u001B[32m" : "\u001B[31m";
        String opt3Color = (buildings[4] != null && buildings[4] && gold >= 200) ? "\u001B[32m" : "\u001B[31m";
        String opt4Color = (buildings[5] != null && buildings[5] && gold >= 250) ? "\u001B[32m" : "\u001B[31m";
        String opt5Color = (buildings[6] != null && buildings[6] && gold >= 300) ? "\u001B[32m" : "\u001B[31m";
        
        System.out.println("1) " + opt1Color + "Spearman (100 gold/unit)" + "\u001B[0m");
        System.out.println("2) " + opt2Color + "Crossbowman (150 gold/unit)" + "\u001B[0m");
        System.out.println("3) " + opt3Color + "Swordsman (200 gold/unit)" + "\u001B[0m");
        System.out.println("4) " + opt4Color + "Cavalier (250 gold/unit)" + "\u001B[0m");
        System.out.println("5) " + opt5Color + "Paladin (300 gold/unit)" + "\u001B[0m");
        
        System.out.println("Enter the number corresponding to the unit or 'q' to go back:");
        String choice = scanner.nextLine();
        if (choice.toLowerCase().equals("q")) {
            return;
        }
        String unitType = "";
        switch(choice) {
            case "1": unitType = "spearman"; break;
            case "2": unitType = "crossbowman"; break;
            case "3": unitType = "swordsman"; break;
            case "4": unitType = "cavalier"; break;
            case "5": unitType = "paladin"; break;
            default:
                logger.severe("Invalid choice");
                return;
        }
        System.out.println("Enter amount:");
        String amountInput = scanner.nextLine();
        int amount;
        try {
            amount = Integer.parseInt(amountInput);
        } catch (NumberFormatException e) {
            logger.severe("Invalid amount");
            return;
        }
        Unit recruited = recruitUnit(unitType, amount, posX, posY, isOpponent);
        if (recruited != null) {
            System.out.println("Recruited " + amount + " " + unitType + "(s) successfully");
        }
    }

    public int getGold() {
        return gold;
    }

    public void setGold(int gold) {
        this.gold = gold;
    }

    public void addGold(int gold) {
        this.gold += gold;
    }

    public boolean purchaseBuilding(String buildingName) {
        int cost = 0;
        switch (buildingName.toLowerCase()) {
            case "tavern": cost = 300; break;
            case "stable": cost = 500; break;
            case "guard post": cost = 200; break;
            case "tower of crossbowmen": 
            case "the tower of crossbowmen": cost = 300; break;
            case "armory": cost = 400; break;
            case "arena": cost = 500; break;
            case "cathedral": cost = 600; break;
            default:
                System.out.println("Unknown building");
                return false;
        }

        for (int i = 0; i < buildingNames.length; i++) {
            if (buildingNames[i].equalsIgnoreCase(buildingName)) {
                if (buildings[i] != null && buildings[i]) {
                    System.out.println(buildingName + " already purchased");
                    return false;
                }
                break;
            }
        }
        if (gold >= cost) {
            gold -= cost;

            for (int i = 0; i < buildingNames.length; i++) {
                if (buildingNames[i].equalsIgnoreCase(buildingName)) {
                    buildings[i] = true;
                    break;
                }
            }
            System.out.println(buildingName + " purchased for " + cost + " gold");
            return true;
        } else {
            logger.severe("Not enough gold to purchase " + buildingName);
            return false;
        }
    }
    
    public Unit recruitUnit(String unitType, int amount, int posX, int posY, boolean isOpponent) {
        int costPerUnit = 0;
        Unit recruited = null;
        switch (unitType.toLowerCase()) {
            case "spearman":
                if (!(buildings[2] != null && buildings[2])) {
                    logger.severe("Guard Post is not built. Cannot recruit Spearmen");
                    return null;
                }
                costPerUnit = 100;
                recruited = new Spearman(amount, posX, posY, isOpponent);
                break;
            case "crossbowman":
                if (!(buildings[3] != null && buildings[3])) {
                    logger.severe("Tower of Crossbowmen is not built. Cannot recruit Crossbowmen");
                    return null;
                }
                costPerUnit = 150;
                recruited = new Crossbowman(amount, posX, posY, isOpponent);
                break;
            case "swordsman":
                if (!(buildings[4] != null && buildings[4])) {
                    logger.severe("Armory is not built. Cannot recruit Swordsmen");
                    return null;
                }
                costPerUnit = 200;
                recruited = new Swordsman(amount, posX, posY, isOpponent);
                break;
            case "cavalier":
                if (!(buildings[5] != null && buildings[5])) {
                    logger.severe("Arena is not built. Cannot recruit Cavaliers");
                    return null;
                }
                costPerUnit = 250;
                recruited = new Cavalier(amount, posX, posY, isOpponent);
                break;
            case "paladin":
                if (!(buildings[6] != null && buildings[6])) {
                    logger.severe("Cathedral is not built. Cannot recruit Paladins");
                    return null;
                }
                costPerUnit = 300;
                recruited = new Paladin(amount, posX, posY, isOpponent);
                break;
            default:
                logger.severe("Unknown unit type: " + unitType);
                return null;
        }
        int totalCost = costPerUnit * amount;
        if (gold >= totalCost) {
            gold -= totalCost;
            System.out.println(unitType + " recruited (" + amount + ") for " + totalCost + " gold");
            recruitedUnits.add(recruited);
            return recruited;
        } else {
            logger.severe("Not enough gold to recruit " + unitType);
            return null;
        }
    }

    public void retrieveUnits(Hero hero) {
        if (hero.getPosX() != this.posX || hero.getPosY() != this.posY) {
            logger.severe("Player not in the castle to retrieve units");
            return;
        }
        Unit[] heroUnits = hero.getHeroUnits();
        for (Unit unit : recruitedUnits) {
            boolean added = false;
            for (int i = 0; i < heroUnits.length; i++) {
                if (heroUnits[i] == null) {
                    heroUnits[i] = unit;
                    System.out.println("Retrieved unit " + unit.getSymbol() + " into slot " + i);
                    added = true;
                    break;
                }
            }
            if (!added) {
                System.out.println("No free slot available for " + unit.getSymbol() + " unit");
            }
        }
        recruitedUnits.clear();
    }

    private void viewRecruitedUnits() {
        if (recruitedUnits.isEmpty()) {
            System.out.println("No units stored in the castle");
        } else {
            System.out.println("Units stored in the castle:");
            for (Unit unit : recruitedUnits) {
                System.out.println("- " + unit.getClass().getSimpleName() + " (Amount: " + unit.getAmount() + ")");
            }
        }
    }

    public void purchaseRandomUnit() {
        if (!isOpponent) return;
        String[] unitTypes = {"spearman", "crossbowman", "swordsman", "cavalier", "paladin"};
        Random rand = new Random();
        String unitType = unitTypes[rand.nextInt(unitTypes.length)];
        
        int costPerUnit = 0;
        switch (unitType.toLowerCase()) {
            case "spearman": costPerUnit = 100; break;
            case "crossbowman": costPerUnit = 150; break;
            case "swordsman": costPerUnit = 200; break;
            case "cavalier": costPerUnit = 250; break;
            case "paladin": costPerUnit = 300; break;
        }
        
        int availableGold = getGold();
        if (availableGold < costPerUnit) return;
        int amount = availableGold / costPerUnit;
        Unit recruited = recruitUnit(unitType, amount, posX, posY, true);
        if (recruited != null) {
            logger.info("Opponent recruited " + amount + " " + unitType +
                               "(s) for " + (amount * costPerUnit) + " gold");
        }
    }
}
