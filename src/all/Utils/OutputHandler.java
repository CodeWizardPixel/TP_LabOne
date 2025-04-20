package all.Utils;

import all.Map.Castle;
import all.Map.Cursor;
import all.Map.Field;
import all.Map.Hero;

public class OutputHandler {
    public static void printGame(Field[][] gameGrid, Hero playerHero, Castle playerCastle, Castle opponentCastle, Cursor cursor) {
        if(playerHero == null) {
            System.out.println("No hero available!");
        } else {
            System.out.println("Hero stamina: " + playerHero.getTurnStamina());
        }
        for (int i = 0; i < gameGrid.length; i++) {
            for (int j = 0; j < gameGrid[0].length; j++) {
                if (gameGrid[i][j].getHasCursor()) {
                    System.out.print(cursor.getColor() + cursor.getSymbol() + "  ");
                } else if (gameGrid[i][j].getHero() != null) {
                    System.out.print(gameGrid[i][j].getHero().getColor() + gameGrid[i][j].getHero().getSymbol() + "  ");
                } else if (gameGrid[i][j].getCastle() != null) {
                    System.out.print(gameGrid[i][j].getColor() + gameGrid[i][j].getCastle().getSymbol() + "  ");
                } else {
                    System.out.print(gameGrid[i][j].getColor() + gameGrid[i][j].getSymbol() + "  ");
                }
            }
            System.out.println();
        }
    }
    
    public static void printBattle(Field[][] battleGrid, Hero playerHero, Hero opponentHero, Cursor battleCursor) {
        for (int i = 0; i < battleGrid.length; i++) {
            StringBuilder mainLine = new StringBuilder();
            StringBuilder amountLine = new StringBuilder();
            for (int j = 0; j < battleGrid[0].length; j++) {
                if (battleGrid[i][j].getHasCursor()) {
                    mainLine.append(battleCursor.getColor()).append(battleCursor.getSymbol()).append("  ");
                } else if (battleGrid[i][j].getUnit() != null) {
                    String color = battleGrid[i][j].getUnit().getIsSelected() ? "\u001B[32m" : battleGrid[i][j].getUnit().getColor();
                    mainLine.append(color).append(battleGrid[i][j].getUnit().getSymbol()).append("  ");
                } else {
                    mainLine.append(battleGrid[i][j].getColor()).append(battleGrid[i][j].getSymbol()).append("  ");
                }
                
                if (battleGrid[i][j].getUnit() != null) {
                    String color = battleGrid[i][j].getUnit().getIsSelected() ? "\u001B[32m" : battleGrid[i][j].getUnit().getColor();
                    int amount = battleGrid[i][j].getUnit().getAmount();
                    String formattedAmount = (amount < 10) ? amount + " " : String.valueOf(amount);
                    amountLine.append(color).append(formattedAmount).append(" ");
                } else {
                    amountLine.append(battleGrid[i][j].getColor()).append(battleGrid[i][j].getSymbol()).append("  ");
                }
            }
            System.out.println(mainLine.toString() + "    " + amountLine.toString());
        }
    }
    
    public static void infoPrint(String info, Castle playerCastle, Castle opponentCastle) {
        System.out.println("player gold: " + playerCastle.getGold() + " | opponent gold: " + opponentCastle.getGold() + " | " + info);
    }

}
