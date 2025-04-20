package all.Game;

import all.Map.Castle;
import all.Map.Cursor;
import all.Map.Field;
import all.Map.Hero;
import all.Units.Ghost;
import all.Units.Unit;
import all.Utils.FieldManager;
import all.Utils.GameLogger;

import java.util.Scanner;
import java.util.logging.Logger;

import static all.Utils.FieldManager.*;
import static all.Utils.OutputHandler.infoPrint;
import static all.Utils.OutputHandler.printBattle;

public class Battle {
    private static Cursor battleCursor = new Cursor();
    private static Scanner scanner = new Scanner(System.in);
    private static final Logger logger = GameLogger.getLogger();
    private static int killGold = 200;
    private static int unitSlots = 5;
    private static int nB = (unitSlots * 2) + 1, mB = (unitSlots * 2) + 1;
    private static int nI = 15, mI = 15;
    private static Field[][] battleGrid = new Field[nB][mB];
    private static Field[][] illusionBattleGrid = new Field[nI][mI];

    public static void playerBattleTurn(Unit unit, Field[][] grid, Hero playerHero, Hero opponentHero, Castle playerCastle, Castle opponentCastle) {
        unit.setIsSelected(true);
        unit.resetStamina();
        printBattle(grid, playerHero, opponentHero, battleCursor);
        infoPrint("", playerCastle, opponentCastle);
        while (true) {
            String input = scanner.nextLine().toLowerCase();
            grid[battleCursor.getPosY()][battleCursor.getPosX()].setHasCursor(false);
            if (processBattleInput(input, unit, grid, playerHero, opponentHero, playerCastle)) {
                grid[battleCursor.getPosY()][battleCursor.getPosX()].setHasCursor(true);
                manageUpdate(grid, playerHero, opponentHero);
                return;
            }
            grid[battleCursor.getPosY()][battleCursor.getPosX()].setHasCursor(true);
            printBattle(grid, playerHero, opponentHero, battleCursor);
            infoPrint("", playerCastle, opponentCastle);
            resetGridColor(grid);
        }
    }

    private static void opponentBattleTurn(Unit unit, Field[][] grid, Hero playerHero, Hero opponentHero, Castle opponentCastle) {
        unit.resetStamina();
        int maxDamageSlot = getMaxDamageSlot(playerHero.getHeroUnits());
        if (maxDamageSlot == -1) {
            return;
        }
        Unit[] units = playerHero.getHeroUnits();
        unit.move(units[maxDamageSlot].getPosX(), units[maxDamageSlot].getPosY(), grid);
        for (Unit opponentUnit : unit.findUnits(grid)) {
            if (opponentUnit == units[maxDamageSlot] && opponentUnit != null) {
                int before = units[maxDamageSlot].getAmount();
                Unit.attack(unit, units[maxDamageSlot]);
                opponentCastle.addGold((before - units[maxDamageSlot].getAmount()) * killGold);
                logger.info("Opponent killed " + (before - units[maxDamageSlot].getAmount()) + " units");
                logger.info("Opponent attacked player unit, gold gained: " + (before - units[maxDamageSlot].getAmount()) * killGold);
                if (units[maxDamageSlot].getAmount() <= 0) {
                    units[maxDamageSlot] = null;
                }
            }
        }
        manageUpdate(grid, playerHero, opponentHero);
    }

    public static boolean processBattleInput(String input, Unit unit, Field[][] grid, Hero playerHero, Hero opponentHero, Castle playerCastle) {
        switch (input) {
            case "w":
                if (battleCursor.getPosY() > 0) {
                    battleCursor.setPosY(battleCursor.getPosY() - 1);
                }
                break;
            case "s":
                if (battleCursor.getPosY() < grid.length - 1) {
                    battleCursor.setPosY(battleCursor.getPosY() + 1);
                }
                break;
            case "a":
                if (battleCursor.getPosX() > 0) {
                    battleCursor.setPosX(battleCursor.getPosX() - 1);
                }
                break;
            case "d":
                if (battleCursor.getPosX() < grid[0].length - 1) {
                    battleCursor.setPosX(battleCursor.getPosX() + 1);
                }
                break;
            case "m":
                unit.move(battleCursor.getPosX(), battleCursor.getPosY(), grid);
                manageUpdate(grid, playerHero, opponentHero);
                break;
            case "p":
                unit.preview(battleCursor.getPosX(), battleCursor.getPosY(), grid);
                break;
            case "n":
                unit.setIsSelected(false);
                if (unit instanceof all.Units.Crossbowman) {
                    ((all.Units.Crossbowman) unit).shoot(grid);
                    Unit[] units = opponentHero.getHeroUnits();
                    for (int i = 0; i < units.length; i++) {
                        if (units[i] != null && units[i].getAmount() <= 0) {
                            units[i] = null;
                        }
                    }
                } else {
                    Unit[] nearUnits = unit.findUnits(grid);
                    Unit[] units = opponentHero.getHeroUnits();
                    int maxDamageSlot = getMaxDamageSlot(nearUnits);
                    if (maxDamageSlot != -1) {
                        for (int i = 0; i < units.length; i++) {
                            if (units[i] == nearUnits[maxDamageSlot]) {
                                int before = units[i].getAmount();
                                Unit.attack(unit, units[i]);
                                playerCastle.addGold((before - units[i].getAmount()) * killGold);
                                logger.info("Player killed " + (before - units[i].getAmount()) + " units");
                                logger.info("Player attacked opponent unit, gold gained: " + (before - units[i].getAmount()) * killGold);
                                System.out.println((before - units[i].getAmount()) * killGold);
                                if (units[i].getAmount() <= 0) {
                                    units[i] = null;
                                }
                            }
                        }
                    }
                }
                return true;
            default:
                logger.severe("Invalid input: " + input);
                break;
        }
        return false;
    }

    public static Hero battle(Hero playerHero, Hero opponentHero, Castle playerCastle, Castle opponentCastle) {
        if (opponentHero.getIsIllusionist()) {
            logger.info("Player encountered the Illusionist");

            Unit[] ghostUnits = new Unit[unitSlots];
            int ghostAmount = 5;
            for (int i = 0; i < ghostAmount && i < unitSlots; i++) {
                ghostUnits[i] = new Ghost(2, 0, 0, true);
            }
            opponentHero.setHeroUnits(ghostUnits);

            int turnsLeft = 10;
            FieldManager.fillIllusionBattleGrid(illusionBattleGrid, playerHero, opponentHero, nI, mI);
            return conductBattle(playerHero, opponentHero, illusionBattleGrid, turnsLeft, playerCastle, opponentCastle);
        }
        FieldManager.fillBattleGrid(battleGrid, playerHero, opponentHero, battleGrid.length, battleGrid[0].length);
        return conductBattle(playerHero, opponentHero, battleGrid, -1, playerCastle, opponentCastle);
    }

    private static Hero conductBattle(Hero playerHero, Hero opponentHero, Field[][] grid, int turnsLeft, Castle playerCastle, Castle opponentCastle) {
        while (true) {
            if (turnsLeft > 0) {
                infoPrint("Turns remaining: " + turnsLeft, playerCastle, opponentCastle);
            }

            for (Unit unit : playerHero.getHeroUnits()) {
                if (unit != null) {
                    if (checkWinConditions(opponentHero)) {
                        return playerHero;
                    }
                    playerBattleTurn(unit, grid, playerHero, opponentHero, playerCastle, opponentCastle);
                }
            }

            for (Unit unit : opponentHero.getHeroUnits()) {
                if (unit != null) {
                    if (checkWinConditions(playerHero)) {
                        return opponentHero;
                    }
                    opponentBattleTurn(unit, grid, playerHero, opponentHero, opponentCastle);
                }
            }

            if (turnsLeft > 0) {
                turnsLeft--;
                if (turnsLeft <= 0) {
                    logger.warning("Player hero has been trapped in the illusion");
                    return opponentHero;
                }
            }
        }
    }

    public static boolean checkWinConditions(Hero hero) {
        int cnt = 0;
        for (Unit unit : hero.getHeroUnits()) {
            if (unit == null) {
                cnt++;
                if (cnt == unitSlots) {
                    return true;
                }
            }
        }
        return false;
    }

    private static int getMaxDamageSlot(Unit[] units) {
        int maxDamage = -1;
        int slot = -1;
        for (int i = 0; i < units.length; i++) {
            if (units[i] != null && maxDamage < units[i].getDamage() * units[i].getAmount()) {
                maxDamage = units[i].getDamage() * units[i].getAmount();
                slot = i;
            }
        }
        return slot;
    }

    private static void manageUpdate(Field[][] grid, Hero playerHero, Hero opponentHero) {
        if (grid == battleGrid) {
            FieldManager.updateBattleGrid(battleGrid, nB, mB, playerHero, opponentHero, battleCursor);
        } else if (grid == illusionBattleGrid) {
            FieldManager.updateBattleGrid(illusionBattleGrid, grid.length, grid[0].length, playerHero, opponentHero, battleCursor);
        }
    }

}
