package cn.ncw.javafx.ncwjavafx;

import cn.ncw.javafx.ncwjavafx.pyex.os;
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

import static cn.ncw.javafx.ncwjavafx.base.CommonVariable.logger;
import static cn.ncw.music.midi.MidiPlayer.convertToMidi;
import static cn.ncw.music.midi.MidiPlayer.playNote;

public class NCWTerminal extends Application {

    public static String test = "video";


    @Override
    public void start(Stage stage) throws IOException {

        logger.info("Resources Initialize...", "start");

        FXMLLoader loader = new FXMLLoader(Objects.requireNonNull(getClass().getResource("/resources/ncw-main-view.fxml")));

        Parent root = loader.load();

        Scene scene = new Scene(root);

        MainController mainController = loader.getController();

        if (Objects.equals(test, "image")) {
            // 图片模式下的设置保持不变
            mainController.MainImageView.fitHeightProperty().bind(stage.heightProperty());
            mainController.MainImageView.fitWidthProperty().bind(stage.widthProperty());
            mainController.MainImageView.setPreserveRatio(true);
        } else if (Objects.equals(test, "video")) {
            // 视频模式下的设置
            // 已经在MainController中完成了MediaView的绑定
        }

        logger.info("Set Title...", "start");

        stage.setTitle("NCW Terminal");
        stage.setResizable(false);

        logger.info("Set Icon...", "start");

        setWindowIcon(stage, os.getcwd() + "\\resources\\icons\\default_icon.png");
        stage.setScene(scene);

        logger.info("Stage Show...", "start");

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
                    logger.error("Icon file do not exist!", "setIcon");
                }
            } catch (Exception e) {
                logger.error(e.toString(), "setIcon", e);
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
                logger.error(e.toString(), "loadIcon", e);
            }
        }
    }
}
