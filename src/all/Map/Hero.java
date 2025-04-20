package all.Map;

import all.Units.Spearman;
import all.Units.Unit;
import all.Utils.Dijkstra;

import java.io.Serializable;

public class Hero implements Serializable {
    private char symbol;
    private double stamina;
    private double turnStamina;
    private boolean isOpponent;
    private int posX;
    private int posY;
    private Unit[] heroUnits;
    private String color;
    private boolean isIllusionist = false;

    public Hero(char symbol, double stamina, boolean isOpponent, int posX, int posY, int unitSlots) {
        this.symbol = symbol;
        this.stamina = stamina;
        turnStamina = stamina;
        this.isOpponent = isOpponent;
        this.color = isOpponent ? "\u001B[31m" : "\u001B[34m";

        this.posX = posX;
        this.posY = posY;

        Unit[] heroUnits = new Unit[unitSlots];

        if (isOpponent) {
            heroUnits[0] = new Spearman(3, 0, 0, true);
        } else {
            heroUnits[0] = new Spearman(3, 0,0, false);
        }

        this.heroUnits = heroUnits;
    }

    public void move(int posX, int posY, Field[][] gameGrid) {
        double[][] grid = new double[gameGrid.length][gameGrid[0].length];
        for (int i = 0; i < gameGrid.length; i++) {
            for (int j = 0; j < gameGrid[0].length; j++) {
                if (gameGrid[i][j].getHero() != null || gameGrid[i][j].getUnit() != null) {
                    grid[i][j] = Double.POSITIVE_INFINITY;
                } else {
                    grid[i][j] = gameGrid[i][j].getCost() * gameGrid[i][j].getZoneCost(isOpponent);
                }
            }
        }

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
        double[][] grid = new double[gameGrid.length][gameGrid[0].length];
        for (int i = 0; i < gameGrid.length; i++) {
            for (int j = 0; j < gameGrid[0].length; j++) {
                if (gameGrid[i][j].getHero() != null || gameGrid[i][j].getUnit() != null) {
                    grid[i][j] = Double.POSITIVE_INFINITY;
                } else {
                    grid[i][j] = gameGrid[i][j].getCost() * gameGrid[i][j].getZoneCost(isOpponent);
                }
            }
        }
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

    public int[] checkHeroPresence(Field[][] gameGrid) {
        int[] coords = {-1, -1};
        int radius = (int) Math.round(stamina);

        for (int dy = -radius; dy <= radius; dy++) {
            int horizontalRange = radius - Math.abs(dy);
            for (int dx = -horizontalRange; dx <= horizontalRange; dx++) {
                int x = posX + dx;
                int y = posY + dy;

                if (x >= 0 && x < gameGrid[0].length &&
                        y >= 0 && y < gameGrid.length) {

                    Field cell = gameGrid[y][x];
                    Hero hero = cell.getHero();

                    if (hero != null && !hero.isOpponent) {
                        coords[0] = x;
                        coords[1] = y;
                    }
                }
            }
        }
        return coords;
    }

    public void resetStamina() {
        turnStamina = stamina;
    }

    public double getTurnStamina() {
        return turnStamina;
    }

    public boolean getIsOpponent () {
        return isOpponent;
    }

    public int getPosX() {
        return posX;
    }

    public int getPosY() {
        return posY;
    }
    
    public void setPosX(int posX) {
        this.posX = posX;
    }
    
    public void setPosY(int posY) {
        this.posY = posY;
    }

    public char getSymbol() {
        return symbol;
    }

    public Unit[] getHeroUnits() {
        return heroUnits;
    }

    public void setHeroUnits(Unit[] heroUnits) {
        this.heroUnits = heroUnits;
    }

    public String getColor() {
        return color;
    }

    public void applyStableBonus(double bonus) {
        this.stamina += bonus;
        this.turnStamina = this.stamina;
    }

    public boolean getIsIllusionist() {
        return isIllusionist;
    }

    public void setIsIllusionist(boolean isIllusionist) {
        this.isIllusionist = isIllusionist;
    }
}
