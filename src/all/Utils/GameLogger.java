package all.Utils;

import java.io.File;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class GameLogger {
    private static final Logger logger = Logger.getLogger(GameLogger.class.getName());
    private static FileHandler fileHandler;

//    public static void init() throws Exception {
//        File logDir = new File("logs");
//        if (!logDir.exists()) {
//            logDir.mkdir();
//        }
//        fileHandler = new FileHandler("logs/game.log");
//        fileHandler.setEncoding("UTF-8");
//        fileHandler.setFormatter(new SimpleFormatter());
//        logger.addHandler(fileHandler);
//        logger.setLevel(Level.ALL);
//    }

    static {
        try {
            File logDir = new File("logs");
            if (!logDir.exists()) {
                logDir.mkdir();
            }
            fileHandler = new FileHandler("logs/game.log");
            fileHandler.setEncoding("UTF-8");
            fileHandler.setFormatter(new SimpleFormatter());
            logger.addHandler(fileHandler);
            logger.setLevel(Level.ALL);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    public static Logger getLogger() {
        return logger;
    }
}
