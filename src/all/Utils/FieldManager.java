package all.Utils;

import all.Map.*;
import all.Units.Unit;

public class FieldManager {
    public static void resetGridColor(Field[][] grid) {
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[0].length; j++) {
                grid[i][j].setColor("\u001B[0m");
            }
        }
    }
    
    public static void fillGrid(Field[][] grid, int m, int n, Castle playerCastle, Castle opponentCastle, Hero playerHero, Hero opponentHero) {
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                grid[i][j] = new Field(Field.NEUTRAL_ZONE, 1.0);
            }
        }

        int zoneSize = n / 3;
        for (int i = 0; i < zoneSize; i++) {
            for (int j = 0; j < zoneSize; j++) {
                grid[i][j] = new Field(Field.PLAYER_ZONE, 1.0);
            }
        }

        for (int i = n - zoneSize; i < n; i++) {
            for (int j = m - zoneSize; j < m; j++) {
                grid[i][j] = new Field(Field.ENEMY_ZONE, 1.0);
            }
        }

        int x = 0, y = 0;
        while (x < m - 1 || y < n - 1) {
            grid[y][x] = new Field('0', 0.5);
            if (x == m - 1) {
                y++;
            } else if (y == n - 1) {
                x++;
            } else if (Math.random() < 0.5) {
                x++;
            } else {
                y++;
            }
        }

        grid[playerCastle.getPosY()][playerCastle.getPosX()].setCastle(playerCastle);
        grid[opponentCastle.getPosY()][opponentCastle.getPosX()].setCastle(opponentCastle);
        grid[playerHero.getPosY()][playerHero.getPosX()].setHero(playerHero);
        grid[opponentHero.getPosY()][opponentHero.getPosX()].setHero(opponentHero);
    }
    
    public static void updateGameGrid(Field[][] grid, int m, int n, Hero playerHero, Hero opponentHero, Cursor cursor) {
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                grid[i][j].setHasCursor(false);
                grid[i][j].setHero(null);
            }
        }
        if (playerHero != null) {
            grid[playerHero.getPosY()][playerHero.getPosX()].setHero(playerHero);
        }
        if (opponentHero != null) {
            grid[opponentHero.getPosY()][opponentHero.getPosX()].setHero(opponentHero);
        }
        grid[cursor.getPosY()][cursor.getPosX()].setHasCursor(true);
    }
    
    public static void updateBattleGrid(Field[][] battleGrid, int nB, int mB, Hero playerHero, Hero opponentHero, Cursor battleCursor) {
        for (int i = 0; i < nB; i++) {
            for (int j = 0; j < mB; j++) {
                battleGrid[i][j].setHasCursor(false);
                battleGrid[i][j].setUnit(null);
            }
        }
        for (Unit unit : playerHero.getHeroUnits()) {
            if (unit != null) {
                battleGrid[unit.getPosY()][unit.getPosX()].setUnit(unit);
            }
        }
        for (Unit unit : opponentHero.getHeroUnits()) {
            if (unit != null) {
                battleGrid[unit.getPosY()][unit.getPosX()].setUnit(unit);
            }
        }
        battleGrid[battleCursor.getPosY()][battleCursor.getPosX()].setHasCursor(true);
    }

    public static void fillBattleGrid(Field[][] battleGrid, Hero playerHero, Hero opponentHero, int nB, int mB) {
        for (int i = 0; i < nB; i++) {
            for (int j = 0; j < mB; j++) {
                battleGrid[i][j] = new Field('*', 1.0);
            }
        }
        Unit[] playerUnits = playerHero.getHeroUnits();
        int newY = 0;
        for (int i = 0; i < playerUnits.length; i++) {
            if (playerUnits[i] != null) {
                playerUnits[i].setPosition(0, newY);
                newY += 2;
            }
        }
        Unit[] opponentUnits = opponentHero.getHeroUnits();
        newY = 0;
        for (int i = 0; i < opponentUnits.length; i++) {
            if (opponentUnits[i] != null) {
                opponentUnits[i].setPosition(mB - 1, newY);
                newY += 2;
            }
        }
        for (Unit unit : playerHero.getHeroUnits()) {
            if (unit != null) {
                battleGrid[unit.getPosY()][unit.getPosX()].setUnit(unit);
            }
        }
        for (Unit unit : opponentHero.getHeroUnits()) {
            if (unit != null) {
                battleGrid[unit.getPosY()][unit.getPosX()].setUnit(unit);
            }
        }
    }

    public static void fillIllusionBattleGrid(Field[][] illusionBattleGrid, Hero playerHero, Hero opponentHero, int nI, int mI) {
        for (int i = 0; i < nI; i++) {
            for (int j = 0; j < mI; j++) {
                illusionBattleGrid[i][j] = new Field('*', 1.0);
            }
        }
        
        Unit[] playerUnits = playerHero.getHeroUnits();
        int newY = nI / 4;
        for (int i = 0; i < playerUnits.length; i++) {
            if (playerUnits[i] != null) {
                playerUnits[i].setPosition(0, newY);
                newY += 2;
            }
        }
        
        Unit[] opponentUnits = opponentHero.getHeroUnits();
        newY = nI / 4;
        for (int i = 0; i < opponentUnits.length; i++) {
            if (opponentUnits[i] != null) {
                opponentUnits[i].setPosition(mI - 1, newY);
                newY += 2;
            }
        }
        
        for (Unit unit : playerHero.getHeroUnits()) {
            if (unit != null) {
                illusionBattleGrid[unit.getPosY()][unit.getPosX()].setUnit(unit);
            }
        }
        for (Unit unit : opponentHero.getHeroUnits()) {
            if (unit != null) {
                illusionBattleGrid[unit.getPosY()][unit.getPosX()].setUnit(unit);
            }
        }
    }
}
