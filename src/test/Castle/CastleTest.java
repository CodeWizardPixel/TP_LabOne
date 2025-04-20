package test.Castle;

import all.Map.Castle;
import all.Map.Hero;
import all.Units.*;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class CastleTest {
    private Castle castle;
    private Hero hero;

    @Before
    public void setUp() {
        castle = new Castle('@', false, 0, 0);
        hero = new Hero('A', 50, false, 0, 0, 5);
    }

    @Test
    public void testBuildingPurchase() {
        assertEquals(1000, castle.getGold());

        assertTrue(castle.purchaseBuilding("Guard Post"));
        assertEquals(800, castle.getGold());

        assertFalse(castle.purchaseBuilding("Guard Post"));
        assertEquals(800, castle.getGold());

        castle.setGold(100);
        assertFalse(castle.purchaseBuilding("Cathedral"));
    }

    @Test
    public void testUnitRecruitment() {
        castle.purchaseBuilding("Guard Post");

        castle.setGold(1000);
        Unit spearmen = castle.recruitUnit("spearman", 2, 0, 0, false);
        assertNotNull(spearmen);
        assertEquals(2, spearmen.getAmount());
        assertEquals(800, castle.getGold());

        castle.setGold(50);
        assertNull(castle.recruitUnit("spearman", 1, 0, 0, false));

        castle.setGold(1000);
        assertNull(castle.recruitUnit("paladin", 1, 0, 0, false));
    }

    @Test
    public void testUnitTransfer() {
        castle.purchaseBuilding("Guard Post");
        castle.recruitUnit("spearman", 2, 0, 0, false);

        Unit[] initialHeroUnits = hero.getHeroUnits();
        boolean hasInitialUnit = false;
        for (Unit unit : initialHeroUnits) {
            if (unit != null) {
                hasInitialUnit = true;
                break;
            }
        }
        assertTrue(hasInitialUnit);

        castle.retrieveUnits(hero);

        Unit[] heroUnits = hero.getHeroUnits();
        boolean hasSpearmen = false;
        for (Unit unit : heroUnits) {
            if (unit instanceof Spearman) {
                hasSpearmen = true;
                assertEquals(3, unit.getAmount());
                break;
            }
        }
        assertTrue(hasSpearmen);
    }

    @Test
    public void testTransferOnlyInCastle() {

        castle.purchaseBuilding("Guard Post");
        castle.recruitUnit("spearman", 2, 0, 0, false);
        
        int initialUnitsCount = 0;
        for (Unit unit : hero.getHeroUnits()) {
            if (unit != null) initialUnitsCount++;
        }
        assertEquals(1, initialUnitsCount);

        hero.setPosX(5);
        hero.setPosY(5);

        castle.retrieveUnits(hero);
        
        Unit[] heroUnits = hero.getHeroUnits();
        int unitsCount = 0;
        for (Unit unit : heroUnits) {
            if (unit != null) unitsCount++;
        }
        assertEquals(1, unitsCount);

        hero.setPosX(castle.getPosX());
        hero.setPosY(castle.getPosY());

        castle.retrieveUnits(hero);


        heroUnits = hero.getHeroUnits();
        unitsCount = 0;
        for (Unit unit : heroUnits) {
            if (unit != null) unitsCount++;
        }
        assertTrue(unitsCount > initialUnitsCount);
    }
}
