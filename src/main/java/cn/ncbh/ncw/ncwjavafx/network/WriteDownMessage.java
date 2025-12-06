package cn.ncbh.ncw.ncwjavafx.network;

import cn.ncbh.ncw.ncwjavafx.log.LEVEL;
import cn.ncbh.ncw.ncwjavafx.log.ThreadLogger;
import cn.ncbh.ncw.ncwjavafx.pyex.os;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static cn.ncbh.ncw.ncwjavafx.base.CommonVariable.*;

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
                        logger.log(LEVEL.ERROR, "WriteMessage", "Error!" + e);
                        threadLogger.log(ThreadLogger.LogLevel.ERROR, "Error!", "WriteMessage", e);
                        break;
                    }
                }
            }
        }
    }
}
