package cn.ncw.javafx.ncwjavafx.network;

import cn.ncw.javafx.ncwjavafx.pyex.os;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static cn.ncw.javafx.ncwjavafx.base.CommonVariable.*;

public class WriteDownMessage extends Thread {

    @Override
    public void run() {
        int var = 1;
        while (true) {
            if (!LIST_MESSAGE.isEmpty()) {
                if (LIST_MESSAGE.size() == var) {
                    try {
                        os.mkdir(os.getcwd() + "\\network\\message");
                        Files.write(Path.of(os.getcwd() + "\\network\\message\\" + CURRENT_TIME + ".txt"), LIST_MESSAGE.getLast().getBytes());
                        var++;
                    } catch (IOException e) {
                        logger.error("Error!" + e, "WriteMessage");
                        break;
                    }
                }
            }
        }
    }
}
