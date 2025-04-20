package all.Utils;
import all.Game.Game;

import java.io.*;
import java.util.Scanner;

public class SaveManager {
    static int[] gold = new int[3];
    static int[] turns = new int[3];
    private static final Scanner scanner = new Scanner(System.in);
    private static int saveSlot;


    public static void saveGame(Game game) {
        try {
            File logDir = new File("saves");
            if (!logDir.exists()) {
                logDir.mkdir();
            }
            FileOutputStream outputStream = new FileOutputStream("saves\\save" + saveSlot + ".bin");
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);

            objectOutputStream.writeObject(game);
            objectOutputStream.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static Game loadGame(int number) {
        try {
            FileInputStream fileInputStream = new FileInputStream("saves\\save" + number + ".bin");
            ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
            return (Game) objectInputStream.readObject();
        } catch (FileNotFoundException e) {
            return null;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static Game selectSaveSlot() {
        for (int i = 0; i < 3; i++) {
            Game game = loadGame(i);
            if (game != null) {
                gold[i] = game.getPlayerCastle().getGold();
                turns[i] = game.getTurnNumber();
            } else {
                turns[i] = -1;
            }

        }
        System.out.println("Select save slot:");
        for (int i = 0; i < 3; i++) {
            if (turns[i] != -1) {
                System.out.println("slot #" + (i + 1) + ": " + "turn: " + turns[i] + ", gold: " + gold[i]);
            } else {
                System.out.println("slot #" + (i + 1) + ": empty");
            }
        }

        while (true) {
            try {
                int input = Integer.parseInt(scanner.nextLine().toLowerCase());
                if (input >= 1 && input <= 3) {
                    saveSlot = input - 1;
                    return loadGame(saveSlot);
                } else {
                    System.out.println("Invalid input!");
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input!");
            }
        }

    }
}