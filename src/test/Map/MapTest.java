package test.Map;

import all.Map.Field;
import all.Utils.Dijkstra;
import all.Utils.Dijkstra.PathStep;
import org.junit.Test;
import static org.junit.Assert.*;

public class MapTest {
    
    @Test
    public void testMovementCost() {
        Field[][] gameGrid = new Field[3][3];
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                gameGrid[i][j] = new Field('*', 1.0);
            }
        }
        
        double[][] grid = Dijkstra.convertForDijkstra(gameGrid);
        PathStep[] path = Dijkstra.dijkstra(grid, new int[]{0, 0}, new int[]{0, 2});
        
        assertEquals("Path should have 2 steps", 2, path.length);
        assertEquals("Each step should cost 1.0", 1.0, path[0].stepCost, 0.001);
    }

    @Test
    public void testZoneCosts() {
        Field playerZone = new Field(Field.PLAYER_ZONE, 1.0);
        Field neutralZone = new Field(Field.NEUTRAL_ZONE, 1.0);
        Field enemyZone = new Field(Field.ENEMY_ZONE, 1.0);
        
        assertEquals("Player in player zone should cost 1.0", 1.0, playerZone.getZoneCost(false), 0.001);
        assertEquals("Player in neutral zone should cost 1.0", 1.0, neutralZone.getZoneCost(false), 0.001);
        assertEquals("Player in enemy zone should cost 2.0", 2.0, enemyZone.getZoneCost(false), 0.001);
        
        assertEquals("Enemy in player zone should cost 2.0", 2.0, playerZone.getZoneCost(true), 0.001);
        assertEquals("Enemy in neutral zone should cost 1.0", 1.0, neutralZone.getZoneCost(true), 0.001);
        assertEquals("Enemy in enemy zone should cost 1.0", 1.0, enemyZone.getZoneCost(true), 0.001);
    }

}
