package all.Map;

import java.io.Serializable;

public class Cursor implements Serializable {
    private char symbol = 'X';
    private String color = "\u001B[32m";
    private int posX = 0;
    private int posY = 0;

    public int getPosX() {
        return posX;
    }

    public int getPosY() {
        return posY;
    }

    public void setPosX(int posX) {
        this.posX = posX;
    }
    public void setPosY(int posY) {
        this.posY = posY;
    }

    public char getSymbol() {
        return symbol;
    }

    public String getColor() {
        return color;
    }
}