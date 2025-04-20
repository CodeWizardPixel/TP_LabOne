package test.Game;

import all.Game.Game;
import all.Map.Hero;
import all.Map.Castle;
import all.Map.Field;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class GameTest {
    private Game game;

    @Before
    public void setUp() {
        game = new Game();
    }

    @Test
    public void testGameInitialization() {
        assertNotNull(game);
        assertNotNull(game.getPlayerHero());
        assertNotNull(game.getOpponentHero());
        assertNotNull(game.getPlayerCastle());
        assertNotNull(game.getOpponentCastle());
        assertNotNull(game.getGameGrid());
    }

    @Test
    public void testCastlePositions() {
        Castle playerCastle = game.getPlayerCastle();
        Castle opponentCastle = game.getOpponentCastle();

        assertEquals(0, playerCastle.getPosX());
        assertEquals(0, playerCastle.getPosY());

        assertEquals(19, opponentCastle.getPosX());
        assertEquals(19, opponentCastle.getPosY());
    }

    @Test
    public void testHeroInitialPositions() {
        Hero playerHero = game.getPlayerHero();
        Hero opponentHero = game.getOpponentHero();

        assertEquals(1, playerHero.getPosX());
        assertEquals(1, playerHero.getPosY());

        assertEquals(18, opponentHero.getPosX());
        assertEquals(18, opponentHero.getPosY());
    }

    @Test
    public void testGridSize() {
        Field[][] gameGrid = game.getGameGrid();
        assertEquals(20, gameGrid.length);
        assertEquals(20, gameGrid[0].length);
    }

    @Test
    public void testInitialGoldValues() {
        assertEquals(1000, game.getPlayerCastle().getGold());
        assertEquals(1000, game.getOpponentCastle().getGold());
    }

    @Test
    public void testHeroUnitSlots() {
        Hero playerHero = game.getPlayerHero();
        Hero opponentHero = game.getOpponentHero();

        assertEquals(5, playerHero.getHeroUnits().length);
        assertEquals(5, opponentHero.getHeroUnits().length);
    }

    @Test
    public void testVictoryOutput() throws Exception {  
        Castle oppCastle = game.getOpponentCastle();
        Hero playerHero = game.getPlayerHero();
        playerHero.setPosX(oppCastle.getPosX());
        playerHero.setPosY(oppCastle.getPosY());
        
        game.setOpponentHero(null);
        game.checkVictoryCondition();

        java.lang.reflect.Field endFlag = Game.class.getDeclaredField("endFlag");
        endFlag.setAccessible(true);

        boolean actualEndFlag = (boolean) endFlag.get(game);
        assertEquals(false, actualEndFlag);
    }

    @Test
    public void testDefeatOutput() throws Exception {  
        Castle playerCastle = game.getPlayerCastle();
        Hero opponentHero = game.getOpponentHero();
        opponentHero.setPosX(playerCastle.getPosX());
        opponentHero.setPosY(playerCastle.getPosY());
        
        game.setPlayerHero(null);
        game.checkDefeatCondition();

        java.lang.reflect.Field endFlag = Game.class.getDeclaredField("endFlag");
        endFlag.setAccessible(true);

        boolean actualEndFlag = (boolean) endFlag.get(game);
        assertEquals(false, actualEndFlag);
    }
}
