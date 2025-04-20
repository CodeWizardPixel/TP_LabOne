package all.Units;

public class Crossbowman extends Unit {
    public Crossbowman(int amount, int posX, int posY, boolean isOpponent) {
        super(20, 15, amount, 'C', posX, posY, 3.0, isOpponent);
    }
    
    public void shoot(all.Map.Field[][] battleGrid) {
        Unit optimalTarget = null;
        double highestDamage = -1;
        for (int i = 0; i < battleGrid.length; i++) {
            for (int j = 0; j < battleGrid[0].length; j++) {
                Unit enemy = battleGrid[i][j].getUnit();
                if (enemy != null && enemy.getIsOpponent() != this.getIsOpponent()) {
                    double enemyDamage = enemy.getDamage() * enemy.getAmount();
                    if (enemyDamage > highestDamage) {
                        highestDamage = enemyDamage;
                        optimalTarget = enemy;
                    }
                }
            }
        }
        if (optimalTarget != null) {
            Unit.attack(this, optimalTarget);
        }
    }
}