package all.Game;

import all.Map.Castle;
import all.Map.Cursor;
import all.Map.Field;
import all.Map.Hero;
import all.Utils.FieldManager;
import all.Utils.GameLogger;
import java.io.Serializable;
import java.util.logging.Logger;
import java.util.Scanner;

import static all.Game.Battle.*;
import static all.Utils.OutputHandler.*;

public class Game implements Serializable {
    private static final Logger logger = GameLogger.getLogger();
    private  boolean endFlag;
    private final int n, m, unitSlots;
    private int turnNumber;
    private static final Scanner scanner = new Scanner(System.in);
    private static final Cursor cursor = new Cursor();
    private Hero playerHero, opponentHero;
    private final Castle playerCastle, opponentCastle;
    private Field[][] gameGrid;

    public Game() {
        logger.info("Initializing new game");
        endFlag = true;
        n = 20;
        m = 20;
        unitSlots = 5;
        turnNumber = 0;
        playerHero = new Hero('A', 50, false, 1, 1, unitSlots);
        opponentHero = new Hero('I', 3, true, m - 2, n - 2, unitSlots);

        opponentHero.setIsIllusionist(true);

        playerCastle = new Castle('@', false, 0, 0);
        opponentCastle = new Castle('@', true, n - 1, m - 1);
        gameGrid = new Field[n][m];
        fillGrid();
        logger.info("Game initialized successfully");
    }

    public void resetGridColor(Field[][] grid) {
        FieldManager.resetGridColor(grid);
    }

    public void fillGrid() {
        FieldManager.fillGrid(gameGrid, m, n, playerCastle, opponentCastle, playerHero, opponentHero);
    }

    public void updateGameGrid() {
        FieldManager.updateGameGrid(gameGrid, m, n, playerHero, opponentHero, cursor);
    }

    public void playerTurn() {
        checkVictoryCondition();
        checkDefeatCondition();
        turnNumber++;
        playerCastle.addGold(500);
        
        if (playerHero != null) {
            playerHero.resetStamina();
        }
        printGame(gameGrid, playerHero, playerCastle, opponentCastle, cursor);
        infoPrint("turn number: " + turnNumber, playerCastle, opponentCastle);
        while (true) {
            String input = scanner.nextLine().toLowerCase();
            gameGrid[cursor.getPosY()][cursor.getPosX()].setHasCursor(false);
            if (processPlayerInput(input)) {
                gameGrid[cursor.getPosY()][cursor.getPosX()].setHasCursor(true);
                updateGameGrid();
                return;
            }
            gameGrid[cursor.getPosY()][cursor.getPosX()].setHasCursor(true);
            printGame(gameGrid, playerHero, playerCastle, opponentCastle, cursor);
            infoPrint("turn number: " + turnNumber, playerCastle, opponentCastle);
            resetGridColor(gameGrid);
        }
    }

    private boolean processPlayerInput(String input) {
        switch (input) {
            case "w":
                if (cursor.getPosY() > 0) {
                    cursor.setPosY(cursor.getPosY() - 1);
                }
                break;
            case "s":
                if (cursor.getPosY() < n - 1) {
                    cursor.setPosY(cursor.getPosY() + 1);
                }
                break;
            case "a":
                if (cursor.getPosX() > 0) {
                    cursor.setPosX(cursor.getPosX() - 1);
                }
                break;
            case "d":
                if (cursor.getPosX() < m - 1) {
                    cursor.setPosX(cursor.getPosX() + 1);
                }
                break;
            case "m":
                if (playerHero != null) {
                    playerHero.move(cursor.getPosX(), cursor.getPosY(), gameGrid);
                    updateGameGrid();
                }
                break;
            case "p":
                if (playerHero != null) {
                    playerHero.preview(cursor.getPosX(), cursor.getPosY(), gameGrid);
                }
                break;
            case "c":
                playerHero = playerCastle.showCastleUI(playerHero);
                if (playerHero != null) {
                    gameGrid[playerHero.getPosY()][playerHero.getPosX()].setHero(playerHero);
                    logger.info("Player purchased a new hero");
                }
                break;
            case "n":
                gameGrid[cursor.getPosY()][cursor.getPosX()].setHasCursor(true);
                return true;
            default:
                logger.severe("Invalid input: " + input);
                break;
        }
        return false;
    }

    public void opponentTurn() {
        checkVictoryCondition();
        checkDefeatCondition();
        opponentCastle.addGold(500);
        if (opponentHero != null) {
            opponentCastle.purchaseRandomUnit();
            if (opponentHero.getPosX() == opponentCastle.getPosX() &&
                    opponentHero.getPosY() == opponentCastle.getPosY() && !opponentHero.getIsIllusionist()) {
                opponentCastle.retrieveUnits(opponentHero);
            }
            opponentHero.resetStamina();
            int[] enemyHeroCords = opponentHero.checkHeroPresence(gameGrid);
            if (playerHero != null) {
                opponentHero.move(playerHero.getPosX(), playerHero.getPosY(), gameGrid);

                if (enemyHeroCords[0] != -1) {
                    if (battle(gameGrid[enemyHeroCords[1]][enemyHeroCords[0]].getHero(),
                            opponentHero, playerCastle, opponentCastle) == opponentHero) {
                        playerHero = null;
                    } else {
                        opponentHero = null;
                    }
                }

            } else {
                opponentHero.move(0, 0, gameGrid);
            }
        }

        if (opponentHero == null) {
            if (opponentCastle.getGold() >= 2000) {
                opponentCastle.addGold(-2000);
                if (Math.random() < 0.3) {
                    opponentHero = new Hero('I', 3, true, opponentCastle.getPosX(), opponentCastle.getPosY(), unitSlots);
                    opponentHero.setIsIllusionist(true);
                    logger.info("Opponent bought a new hero");
                } else {
                    opponentHero = new Hero('A', 3, true, opponentCastle.getPosX(), opponentCastle.getPosY(), unitSlots);

                    logger.info("Opponent bought a new hero");
                }
            } else {
                logger.warning("Opponent lacks funds, current gold: " + opponentCastle.getGold());
            }
        }
        updateGameGrid();
    }
    public void checkVictoryCondition() {
        if (playerHero != null && opponentHero == null &&
                playerHero.getPosX() == opponentCastle.getPosX() &&
                playerHero.getPosY() == opponentCastle.getPosY()) {
            logger.warning("Player has conquered the opponent's castle");
            endFlag = false;
        }
    }

    public void checkDefeatCondition() {
        if (opponentHero != null && playerHero == null &&
                opponentHero.getPosX() == playerCastle.getPosX() &&
                opponentHero.getPosY() == playerCastle.getPosY()) {
            logger.warning("The opponent has conquered the player's castle");
            endFlag = false;
        }
    }

    public Hero getPlayerHero() {
        return playerHero;
    }

    public Hero getOpponentHero() {
        return opponentHero;
    }

    public void setPlayerHero(Hero hero) {
        this.playerHero = hero;
    }

    public void setOpponentHero(Hero hero) {
        this.opponentHero = hero;
    }

    public Castle getPlayerCastle() {
        return playerCastle;
    }

    public Castle getOpponentCastle() {
        return opponentCastle;
    }

    public Field[][] getGameGrid() {
        return gameGrid;
    }

    public Boolean getEndFlag() {
        return endFlag;
    }

    public int getTurnNumber() {
        return turnNumber;
    }
}