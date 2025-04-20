package all.Map;

import all.Units.Unit;

import java.io.Serializable;

public class Field implements Serializable {
    private char symbol;
    private String color = "\u001B[0m";
    private double cost;
    private boolean hasCursor = false;
    private Hero hero;
    private Castle castle;
    private Unit unit;

    public static final char PLAYER_ZONE = '&';
    public static final char NEUTRAL_ZONE = '*';
    public static final char ENEMY_ZONE = '?';
    
    public static final double PLAYER_IN_PLAYER_ZONE = 1.0;
    public static final double PLAYER_IN_NEUTRAL_ZONE = 1.0;
    public static final double PLAYER_IN_ENEMY_ZONE = 2.0;
    public static final double ENEMY_IN_PLAYER_ZONE = 2.0;
    public static final double ENEMY_IN_NEUTRAL_ZONE = 1.0;
    public static final double ENEMY_IN_ENEMY_ZONE = 1.0;

    public Field(char symbol, double cost) {
        this.symbol = symbol;
        this.cost = cost;
    }

    public double getCost() {
        return cost;
    }

    public char getSymbol() {
        return symbol;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
         this.color = color;
    }

    public void setHasCursor(boolean hasCursor) {
        this.hasCursor = hasCursor;
    }

    public boolean getHasCursor() {
        return hasCursor;
    }

    public void setHero(Hero hero) {
        this.hero = hero;
    }

    public Hero getHero() {
        return hero;
    }

    public void setCastle(Castle castle) {
        this.castle = castle;
    }

    public Castle getCastle() {
        return castle;
    }

    public void setUnit(Unit unit) {
        this.unit = unit;
    }

    public Unit getUnit() {
        return unit;
    }

    public void setCost(double cost) {
        this.cost = cost;
    }

    public double getZoneCost(boolean isOpponent) {
        if (isOpponent) {
            if (symbol == PLAYER_ZONE) return ENEMY_IN_PLAYER_ZONE;
            if (symbol == ENEMY_ZONE) return ENEMY_IN_ENEMY_ZONE;
            return ENEMY_IN_NEUTRAL_ZONE;
        } else {
            if (symbol == PLAYER_ZONE) return PLAYER_IN_PLAYER_ZONE;
            if (symbol == ENEMY_ZONE) return PLAYER_IN_ENEMY_ZONE;
            return PLAYER_IN_NEUTRAL_ZONE;
        }
    }
}
