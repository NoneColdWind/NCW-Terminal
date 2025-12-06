package cn.ncbh.ncw.ncwjavafx;

import cn.ncbh.ncw.ncwjavafx.log.LEVEL;
import cn.ncbh.ncw.ncwjavafx.log.ThreadLogger;
import cn.ncbh.ncw.ncwjavafx.pyex.os;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;

import static cn.ncbh.ncw.ncwjavafx.base.CommonVariable.logger;
import static cn.ncbh.ncw.ncwjavafx.base.CommonVariable.threadLogger;
import static cn.ncbh.ncw.ncwjavafx.midiplayer.MidiPlayer.convertToMidi;
import static cn.ncbh.ncw.ncwjavafx.midiplayer.MidiPlayer.playNote;

public class NCWTerminal extends Application {

    @Override
    public void start(Stage stage) throws IOException {

        logger.log(LEVEL.INFO, "start", "Resources Initialize...");
        threadLogger.log(ThreadLogger.LogLevel.INFO, "Resources Initialize...", "start", null);

        FXMLLoader loader = new FXMLLoader(Objects.requireNonNull(getClass().getResource("/resources/ncw-main-view.fxml")));

        Parent root = loader.load();

        Scene scene = new Scene(root);

        MainController mainController = loader.getController();

        mainController.MainImageView.fitHeightProperty().bind(stage.heightProperty());
        mainController.MainImageView.fitWidthProperty().bind(stage.widthProperty());
        mainController.MainImageView.setPreserveRatio(true);

        logger.log(LEVEL.INFO, "start", "Set Title...");
        threadLogger.log(ThreadLogger.LogLevel.INFO, "Set Title...", "start", null);

        stage.setTitle("NCW Terminal");
        stage.setResizable(false);

        logger.log(LEVEL.INFO, "start", "Set Icon...");
        threadLogger.log(ThreadLogger.LogLevel.INFO, "Set Icon...", "start", null);

        setWindowIcon(stage, os.getcwd() + "\\resources\\icons\\default_icon.png");
        stage.setScene(scene);

        logger.log(LEVEL.INFO, "start", "Stage Show...");
        threadLogger.log(ThreadLogger.LogLevel.INFO, "Stage Show...", "start", null);

        stage.show();
    }

    public static void main(String[] args) throws IOException {
        playNote(convertToMidi("B4"), 100, 200, 0, 0);
        playNote(convertToMidi("E5"), 100, 310, 0, 0);
        playNote(convertToMidi("F5#"), 100, 200, 0, 0);
        playNote(convertToMidi("B5"), 100, 1380, 0, 0);
        NCWInitialize.Initialize();
        launch();
        System.exit(0);
    }

    private void setWindowIcon(Stage stage, String icon_path) {
        try {
            try {
                File iconFile = new File(icon_path);
                if (iconFile.exists()) {
                    try (FileInputStream fis = new FileInputStream(iconFile)) {
                        stage.getIcons().add(new Image(fis));
                    }
                } else {
                    logger.log(LEVEL.ERROR, "setIcon", "Icon file do not exist!");
                    threadLogger.log(ThreadLogger.LogLevel.ERROR, "Icon file do not exist!", "setIcon", null);
                }
            } catch (Exception e) {
                logger.log(LEVEL.ERROR, "setIcon", e.toString());
                threadLogger.log(ThreadLogger.LogLevel.ERROR, "Error!", "setIcon", e);
            }
        } catch (Exception e) {
            // 使用系统默认图标作为后备方案
            loadDefaultIcons(stage);
        }
    }

    private void loadDefaultIcons(Stage stage) {
        // 添加不同尺寸的图标以确保在各种环境下都能显示
        for (String size : new String[]{"16", "32", "64", "128"}) {
            try {
                InputStream defaultIcon = getClass().getResourceAsStream(
                        "/icons/default_icon_" + size + ".png");
                if (defaultIcon != null) {
                    stage.getIcons().add(new Image(defaultIcon));
                }
            } catch (Exception e) {
                logger.log(LEVEL.ERROR, "loadIcon", e.toString());
                threadLogger.log(ThreadLogger.LogLevel.ERROR, "Error!", "loadIcon", e);
            }
        }
    }
}
