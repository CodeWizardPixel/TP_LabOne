package all.Units;

public class Ghost extends Unit {
    public Ghost(int amount, int posX, int posY, boolean isOpponent) {
        super(60, 25, amount, 'G', posX, posY, 4.0, isOpponent);
    }
}
