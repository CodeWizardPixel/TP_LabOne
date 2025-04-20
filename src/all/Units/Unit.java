
package all.Units;

import all.Utils.Dijkstra;
import all.Map.Field;

import java.io.Serializable;

public class Unit implements Serializable {
    private int health;
    private int damage;
    private int amount;
    private boolean isSelected = false;
    private String color;
    private char symbol;
    private boolean isOpponent;
    private double stamina = 2.0;
    private double turnStamina;
    private int posX = 0;
    private int posY;

    private int[][] DIRECTIONS = {
            {-1, 0}, {1, 0}, {0, -1}, {0, 1},
            {-1, -1}, {-1, 1}, {1, -1}, {1, 1}
    };

    public Unit (int health, int damage, int amount, char symbol, int posX, int posY, double stamina, boolean isOpponent) {
        this.health = health;
        this.damage = damage;
        this.amount = amount;
        this.symbol = symbol;
        this.posX = posX;
        this.posY = posY;
        this.stamina = stamina;
        turnStamina = stamina;
        this.isOpponent = isOpponent;
        this.color = isOpponent ? "\u001B[31m" : "\u001B[34m";
    }

    public static void attack(Unit attackUnit, Unit defendUnit) {
        int newHealth = defendUnit.getAmount() * defendUnit.getHealth() - attackUnit.getAmount() * attackUnit.getDamage();
        int amount = newHealth / defendUnit.getHealth();
        if (amount > 0) {
            defendUnit.setAmount(amount);
        }
        else {
            defendUnit.setAmount(0);
        }

    }

    public int getHealth() {
        return health;
    }

    public int getDamage() {
        return damage;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public void setIsSelected(boolean state) {
        isSelected = state;
    }

    public boolean getIsSelected() {
        return isSelected;
    }

    public boolean getIsOpponent() {
        return isOpponent;
    }

    public String getColor() {
        return color;
    }

    public char getSymbol() {
        return symbol;
    }

    public void move(int posX, int posY, Field[][] gameGrid) {
        double[][] grid = Dijkstra.convertForDijkstra(gameGrid);
        int[] start = {this.posY, this.posX};
        int[] goal = {posY, posX};
        Dijkstra.PathStep[] path = Dijkstra.dijkstra(grid, start, goal);

        for (Dijkstra.PathStep step : path) {
            if (turnStamina - step.stepCost >= 0 && gameGrid[step.y][step.x].getHero() == null && gameGrid[step.y][step.x].getUnit() == null) {
                turnStamina -= step.stepCost;
                this.posX = step.x;
                this.posY = step.y;
            } else {
                break;
            }
        }
    }

    public void preview(int posX, int posY, Field[][] gameGrid) {
        double[][] grid = Dijkstra.convertForDijkstra(gameGrid);
        int[] start = {this.posY, this.posX};
        int[] goal = {posY, posX};
        Dijkstra.PathStep[] path = Dijkstra.dijkstra(grid, start, goal);
        double turnStaminaTemp = turnStamina;
        String color = "\u001b[32m";
        for (Dijkstra.PathStep step : path) {
            if (turnStaminaTemp - step.stepCost < 0) {
                color = "\u001b[31m";
            }
            turnStaminaTemp -= step.stepCost;
            gameGrid[step.y][step.x].setColor(color);
        }
    }


    public boolean checkUnitPresence(Unit unit, Field[][] gameGrid) {
        for (int[] dir : DIRECTIONS) {
            int y = posY + dir[1];
            int x = posX + dir[0];
            if (y >= 0 && y < gameGrid.length && x >= 0 && x < gameGrid[0].length) {
                if (gameGrid[y][x].getUnit() == unit) {
                    return true;
                }
            }
        }
        return false;
    }

    public Unit[] findUnits(Field[][] gameGrid) {
        Unit[] units = new Unit[DIRECTIONS.length];
        int i = 0;
        for (int[] dir : DIRECTIONS) {
            int y = posY + dir[1];
            int x = posX + dir[0];
            if (y >= 0 && y < gameGrid.length && x >= 0 && x < gameGrid[0].length) {
                if (gameGrid[y][x].getUnit() != null && gameGrid[y][x].getUnit().getIsOpponent() != isOpponent) {
                    units[i] = gameGrid[y][x].getUnit();
                    i++;
                }
            }
        }
        return units;
    }

    public int getPosX() {
        return posX;
    }

    public int getPosY() {
        return posY;
    }

    public void setPosition(int posX, int posY) {
        this.posX = posX;
        this.posY = posY;
    }

    public void resetStamina() {
        turnStamina = stamina;
    }
}
