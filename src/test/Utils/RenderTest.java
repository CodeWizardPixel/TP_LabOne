package test.Utils;

import all.Map.Castle;
import all.Map.Cursor;
import all.Map.Field;
import all.Map.Hero;
import all.Utils.OutputHandler;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

public class RenderTest {
    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;
    private OutputHandler outputHandler;

    @Before
    public void setUpStreams() {
        System.setOut(new PrintStream(outContent));
        outputHandler = new OutputHandler();
    }

    @After
    public void restoreStreams() {
        System.setOut(originalOut);
    }

    @Test
    public void testPrintGameWithNullHero() {
        Field[][] gameGrid = new Field[1][1];
        gameGrid[0][0] = new Field('*', 1.0);
        Castle playerCastle = new Castle('@', false, 0, 0);
        Castle opponentCastle = new Castle('@', true, 0, 0);
        Cursor cursor = new Cursor();
        
        outputHandler.printGame(gameGrid, null, playerCastle, opponentCastle, cursor);
        assertTrue(outContent.toString().contains("No hero available!"));
    }

    @Test
    public void testPrintGameWithHero() {
        Field[][] gameGrid = new Field[1][1];
        gameGrid[0][0] = new Field('*', 1.0);
        Hero hero = new Hero('H', 50, false, 0, 0, 5);
        Castle playerCastle = new Castle('@', false, 0, 0);
        Castle opponentCastle = new Castle('@', true, 0, 0);
        Cursor cursor = new Cursor();
        
        outputHandler.printGame(gameGrid, hero, playerCastle, opponentCastle, cursor);
        assertTrue(outContent.toString().contains("Hero stamina: " + hero.getTurnStamina()));
    }

    @Test
    public void testInfoPrint() {
        Castle playerCastle = new Castle('@', false, 0, 0);
        Castle opponentCastle = new Castle('@', true, 0, 0);
        
        outputHandler.infoPrint("Test info", playerCastle, opponentCastle);
        String expected = "player gold: 1000 | opponent gold: 1000 Test info";
        assertTrue(outContent.toString().trim().contains(expected));
    }

    @Test
    public void testPrintBattleBasicGrid() {
        Field[][] battleGrid = new Field[2][2];
        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 2; j++) {
                battleGrid[i][j] = new Field('*', 1.0);
            }
        }
        Hero playerHero = new Hero('H', 50, false, 0, 0, 5);
        Hero opponentHero = new Hero('E', 50, true, 1, 1, 5);
        Cursor battleCursor = new Cursor();
        
        outputHandler.printBattle(battleGrid, playerHero, opponentHero, battleCursor);
        assertFalse(outContent.toString().isEmpty());
    }

    @Test
    public void testGameMapSize() {
        int rows = 20;
        int cols = 20;
        Field[][] gameGrid = new Field[rows][cols];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                gameGrid[i][j] = new Field('*', 1.0);
            }
        }
        Hero hero = new Hero('H', 50, false, 0, 0, 5);
        Castle playerCastle = new Castle('@', false, 0, 0);
        Castle opponentCastle = new Castle('@', true, rows-1, cols-1);
        Cursor cursor = new Cursor();

        outputHandler.printGame(gameGrid, hero, playerCastle, opponentCastle, cursor);
        
        String output = outContent.toString();
        String[] lines = output.split("\n");
        
        // First line is hero stamina info
        assertEquals(rows + 1, lines.length);
        // Each line should have cols*3 characters (symbol + 2 spaces)
        assertTrue(lines[1].trim().length() >= cols * 3);
    }

    @Test
    public void testBattleMapSize() {
        int unitSlots = 5;
        int rows = unitSlots * 2 + 1;
        int cols = unitSlots * 2 + 1;
        Field[][] battleGrid = new Field[rows][cols];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                battleGrid[i][j] = new Field('*', 1.0);
            }
        }
        Hero playerHero = new Hero('H', 50, false, 0, 0, unitSlots);
        Hero opponentHero = new Hero('E', 50, true, cols-1, rows-1, unitSlots);
        Cursor battleCursor = new Cursor();

        outputHandler.printBattle(battleGrid, playerHero, opponentHero, battleCursor);
        
        String output = outContent.toString();
        String[] lines = output.split("\n");
        
        assertEquals(rows, lines.length);
        // Each line should contain both main symbols and amount numbers
        assertTrue(lines[0].trim().length() >= cols * 6);
    }
}
