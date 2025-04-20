package test.Battle;

import all.Game.Game;
import all.Units.*;
import all.Map.Hero;
import all.Game.Battle;
import org.junit.Test;

import static all.Game.Battle.checkWinConditions;
import static org.junit.Assert.*;
import java.lang.reflect.Field;

public class BattleTest {
    
    @Test
    public void testHeroInitialUnit() {
        Hero hero = new Hero('A', 50, false, 1, 1, 5);
        int unitCount = 0;
        for (Unit unit : hero.getHeroUnits()) {
            if (unit != null) unitCount++;
        }
        assertEquals(1, unitCount);
    }

    @Test
    public void testCheckWinConditions() {
        Game game = new Game();
        Hero hero = new Hero('A', 50, false, 1, 1, 5);
        
        hero.getHeroUnits()[0] = null;
        assertTrue(checkWinConditions(hero));
        
        hero.getHeroUnits()[0] = new Paladin(5, 0, 0, false);
        assertFalse(checkWinConditions(hero));
        
        Hero deadHero = new Hero('B', 50, false, 1, 1, 5);
        for (int i = 0; i < 5; i++) {
            deadHero.getHeroUnits()[i] = null;
        }
        assertTrue(checkWinConditions(deadHero));
    }
    
    @Test
    public void testHeroStamina() throws Exception {
        Hero hero = new Hero('A', 50, false, 1, 1, 5);
        
        Field staminaField = Hero.class.getDeclaredField("stamina");
        staminaField.setAccessible(true);
        
        double actualStamina = (double) staminaField.get(hero);
        assertEquals( 50.0, actualStamina, 0.001);
    }
}
