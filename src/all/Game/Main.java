package all.Game;

import static all.Utils.SaveManager.saveGame;
import static all.Utils.SaveManager.selectSaveSlot;

public class Main {
    public static void main(String[] args) {

        Game game = selectSaveSlot();
        if (game == null) {
            game = new Game();
            game.fillGrid();
        }

        while(game.getEndFlag()) {
            saveGame(game);
            game.playerTurn();
            game.opponentTurn();
        }
    }
}