package cn.ncbh.ncw.ncwjavafx;

import cn.ncbh.ncw.ncwjavafx.base.JarResourceCopier;
import cn.ncbh.ncw.ncwjavafx.base.PyRandom;
import cn.ncbh.ncw.ncwjavafx.base.Settings;
import cn.ncbh.ncw.ncwjavafx.base.hash.FileCheckMethod;
import cn.ncbh.ncw.ncwjavafx.log.LEVEL;
import cn.ncbh.ncw.ncwjavafx.log.ThreadLogger;
import cn.ncbh.ncw.ncwjavafx.midiplayer.StreamAudioPlayer;
import cn.ncbh.ncw.ncwjavafx.midiplayer.musicTest.MusicThreadPool;
import cn.ncbh.ncw.ncwjavafx.network.WriteDownMessage;
import cn.ncbh.ncw.ncwjavafx.network.server.IPv6Server;
import cn.ncbh.ncw.ncwjavafx.pyex.os;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.jetbrains.annotations.NotNull;

import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import static cn.ncbh.ncw.ncwjavafx.base.CommonVariable.*;

public class NCWInitialize {

    public static final String work_path = os.getcwd(); //获取工作目录

    private static final String image_1 = "/resources/images/custom/default.png";
    private static final String image_2 = "/resources/images/custom/default2.png";
    private static final String image_3 = "/resources/images/custom/default3.png";
    private static final String icon_default = "/resources/icons/default_icon.png";

    //设置默认目录变量
    private static final Path resources_path = Paths.get(work_path + "\\resources");
    private static final Path images_path = Paths.get(work_path + "\\resources\\images\\custom");
    private static final Path icons_path = Paths.get(work_path + "\\resources\\icons");
    private static final Path default_music_path = Paths.get(work_path + "\\resources\\sounds\\music\\default");
    private static final Path sounds_sf2_path = Paths.get(work_path + "\\resources\\sounds\\sf2");

    //-------------------------------------

    static {

        EASTER_EGG = false;

        player = new StreamAudioPlayer();

        LIST_OPERATE_WORD = Arrays.asList("+", "-", "x", "/");

        LIST_RESOURCES_FILE = Arrays.asList("images", "icons", "sounds");

        LIST_DAY = Arrays.asList("day1.png", "day2.png", "day3.png", "day4.png", "day5.png");

        LIST_NIGHT = Arrays.asList("night1.png", "night2.png", "night3.png", "night4.png", "night5.png");

        LIST_MUSIC = Arrays.asList("music1.wav", "music2.wav", "music3.wav", "music4.wav", "music5.wav");

        IMAGE = "";

        EASTER_EGG_PROBABILITY = 20;
    }


    public static void Initialize() {

        logger.log(LEVEL.INFO, "initialize", "Data Initialize...");
        threadLogger.log(ThreadLogger.LogLevel.INFO, "Data Initialize...", "initialize", null);

        Thread thread = new Thread(NCWInitialize::createPath);
        try {
            thread.start();
            thread.join();
        } catch (InterruptedException ignored) {

        }

        Thread thread_0 = new Thread(NCWInitialize::createSettingsFile);
        try {
            thread_0.start();
            thread_0.join();
        } catch (InterruptedException ignored) {

        }

        Thread thread_1 = new Thread(NCWInitialize::firstLoadSettings);
        try {
            thread_1.start();
            thread_1.join();
        } catch (InterruptedException ignored) {

        }

        Thread thread_2 = new Thread(NCWInitialize::launchTimesCount);
        thread_2.start();



        Thread thread_4 = new Thread(NCWInitialize::copyTimeBackgroundPictures);
        thread_4.start();

        Thread thread_5 = new Thread(NCWInitialize::copyDefaultBackgroundPictures);
        try {
            thread_5.start();
            thread_5.join();
        } catch (InterruptedException ignored) {

        }

        JarResourceCopier.copyResource("/resources/sounds/music/default/music1.wav", default_music_path);

        Thread thread_6 = new Thread(NCWInitialize::copyDefaultMusic);
        thread_6.start();

        Thread thread_7 = new Thread(NCWInitialize::copyInfo);
        thread_7.start();

        Thread thread_8 = new Thread(NCWInitialize::copySF2);
        thread_8.start();

        Thread thread_9 = new Thread(NCWInitialize::firstFileCheck);
        try {
            thread_9.start();
            thread_9.join();
        } catch (InterruptedException ignored) {

        }

        Thread thread_10 = new Thread(NCWInitialize::threadFileCheck);
        thread_10.start();

        easterEgg();

        launchServer();

        logger.log(LEVEL.INFO, "initialize", "NCW Initialize Successfully!");
        threadLogger.log(ThreadLogger.LogLevel.INFO, "NCW Initialize Successfully!", "initialize", null);

    }

    //---------------------------------------------

    private static void launchTimesCount() {
        try {
            //启动次数计算
            String NEW_LAUNCH_TIMES = String.valueOf(LAUNCH_TIMES + 1);
            Path path = Paths.get(os.getcwd() + "\\resources\\options.ini");
            String content = Files.readString(path, StandardCharsets.UTF_8);
            String target = "  \"launch_times\": " + LAUNCH_TIMES;
            String replacement = "  \"launch_times\": " + NEW_LAUNCH_TIMES;
            String newContent = content.replace(target, replacement);
            Files.writeString(
                    path,
                    newContent,
                    StandardCharsets.UTF_8,
                    StandardOpenOption.TRUNCATE_EXISTING
            );
        } catch (IOException ignored) {

        }
    }

    private static void createPath() {

        //生成必要目录
        if (!Files.isDirectory(Path.of(work_path + "\\resources"))) {
            try {
                for (String s : LIST_RESOURCES_FILE) {
                    os.mkdir(work_path + "\\resources\\" + s);
                }
            } catch (Exception ignored) {

            }
        } else {
            for (String s : LIST_RESOURCES_FILE) {
                if (!Files.isDirectory(Path.of(work_path + "\\resources\\" + s))) {
                    os.mkdir(work_path + "\\resources\\" + s);
                }
            }
        }

        //生成音乐资源目录
        if (!Files.isDirectory(Path.of(work_path + "\\resources\\sounds\\music"))) {
            os.mkdir(work_path + "\\resources\\sounds\\music");
        }

        //生成默认音乐资源目录
        if (!Files.isDirectory(Path.of(work_path + "\\resources\\sounds\\music\\default"))) {
            os.mkdir(work_path + "\\resources\\sounds\\music\\default");
        }

        //生成自定义音乐资源目录
        if (!Files.isDirectory(Path.of(work_path + "\\resources\\sounds\\music\\custom"))) {
            os.mkdir(work_path + "\\resources\\sounds\\music\\custom");
        }

        //生成自定义背景图片资源目录
        if (!Files.isDirectory(Path.of(work_path + "\\resources\\images\\custom"))) {
            os.mkdir(work_path + "\\resources\\images\\custom");
        }

        //生成时间背景图片资源目录
        if (!Files.isDirectory(Path.of(work_path + "\\resources\\images\\time\\day"))) {
            os.mkdir(work_path + "\\resources\\images\\time\\day");
        }
        if (!Files.isDirectory(Path.of(work_path + "\\resources\\images\\time\\night"))) {
            os.mkdir(work_path + "\\resources\\images\\time\\night");
        }

        //生成RSA密钥对文件目录
        if (!Files.isDirectory(Path.of(work_path + "\\resources\\network\\key"))) {
            os.mkdir(work_path + "\\resources\\network\\key");
        }
    }

    private static void createSettingsFile() {
        //生成设置文件
        if (Files.notExists(Paths.get(os.getcwd() + "\\resources\\settings.json"))) {
            JarResourceCopier.copyResource("/resources/settings.json", Path.of(os.getcwd() + "\\resources"));
        }
    }

    public static void loadSettings() {

        ObjectMapper objectMapper = new ObjectMapper();

        try {
            SETTINGS = objectMapper.readValue(new File(resources_path + "\\settings.json"), Settings.class);
        } catch (Exception ignored) {

        }

        DEBUG = SETTINGS.isDebug();
        FILE_CHECK = SETTINGS.isFile_check();
        DEFAULT_FLOAT_PART_LENGTH = SETTINGS.getDefault_float_length();
        BACKGROUND_MODE = SETTINGS.getBackground_mode();
        MUSIC_IS_PLAYING = SETTINGS.isMusic_is_playing();
        CONTINUE_PLAY = SETTINGS.isContinue_playing();
        MUSIC_PLAY_MODE = SETTINGS.getMusic_play_mode();
        LOGIN = SETTINGS.isLogin();
        LAUNCH_TIMES = SETTINGS.getLaunch_times();
        DEFAULT_SF2 = SETTINGS.getDefault_sf2();
        SERVER_PORT = SETTINGS.getDefault_server_port();
        OPERATE_MODE = SETTINGS.getOperate_mode();

    }

    private static void firstLoadSettings() {

        ObjectMapper objectMapper = new ObjectMapper();

        try {
            SETTINGS = objectMapper.readValue(new File(resources_path + "\\settings.json"), Settings.class);
        } catch (Exception ignored) {

        }

        DEBUG = SETTINGS.isDebug();
        FILE_CHECK = SETTINGS.isFile_check();
        DEFAULT_FLOAT_PART_LENGTH = SETTINGS.getDefault_float_length();
        BACKGROUND_MODE = SETTINGS.getBackground_mode();
        MUSIC_IS_PLAYING = SETTINGS.isMusic_is_playing();
        CONTINUE_PLAY = SETTINGS.isContinue_playing();
        MUSIC_PLAY_MODE = SETTINGS.getMusic_play_mode();
        LOGIN = SETTINGS.isLogin();
        LAUNCH_TIMES = SETTINGS.getLaunch_times();
        DEFAULT_SF2 = SETTINGS.getDefault_sf2();
        SERVER_PORT = SETTINGS.getDefault_server_port();
        OPERATE_MODE = SETTINGS.getOperate_mode();

    }

    private static void copyTimeBackgroundPictures(){
        //从应用程序中拷贝时间背景图片资源到外部
        for (int i = 0; i < LIST_DAY.size(); i++) {
            JarResourceCopier.copyResource("/resources/images/time/day/" + LIST_DAY.get(i), Path.of(os.getcwd() + "\\resources\\images\\time\\day"));
            JarResourceCopier.copyResource("/resources/images/time/night/" + LIST_NIGHT.get(i), Path.of(os.getcwd() + "\\resources\\images\\time\\night"));
        }
    }

    private static void copyDefaultBackgroundPictures() {
        //从应用程序中拷贝默认背景图片资源到外部
        if (!Files.exists(Path.of(images_path + "\\custom\\default.png"))) {
            JarResourceCopier.copyResource(image_1, images_path);
        }
        if (!Files.exists(Path.of(images_path + "\\custom\\default2.png"))) {
            JarResourceCopier.copyResource(image_2, images_path);
        }
        if (!Files.exists(Path.of(images_path + "\\custom\\default3.png"))) {
            JarResourceCopier.copyResource(image_3, images_path);
        }
        if (!Files.exists(Path.of(icons_path + "\\default_icon.png"))) {
            JarResourceCopier.copyResource(icon_default, icons_path);
        }
    }

    private static void copyDefaultMusic() {
        //从应用程序中拷贝默认音乐资源到外部
        for (int i = 2; i < LIST_MUSIC.size() + 1; i++) {
            if (!Files.exists(Path.of(default_music_path + "\\music" + i + ".wav"))) {
                JarResourceCopier.copyResource("/resources/sounds/music/default/music" + i + ".wav", default_music_path);
            }
        }
    }

    private static void copyInfo() {
        //从应用程序中拷贝默认音乐信息文件到外部
        if (!Files.exists(Path.of(default_music_path + "\\music_info.json"))) {
            JarResourceCopier.copyResource("/resources/sounds/music/default/music_info.json", default_music_path);
        }

        //从应用程序中拷贝应用信息文件到外部
        if (!Files.exists(Path.of(resources_path + "\\ncw.json"))) {
            JarResourceCopier.copyResource("/resources/ncw.json", resources_path);
        }
    }

    private static void copySF2() {
        //从应用程序中拷贝.sf2音色库文件到外部
        if (!Files.exists(Paths.get(os.getcwd() + "\\resources\\sounds\\sf2\\Reality_GMGS_falcomod.sf2"))) {
            JarResourceCopier.copyResource("/resources/sounds/sf2/Reality_GMGS_falcomod.sf2", Path.of(os.getcwd() + "\\resources\\sounds\\sf2"));
        }
        if (!Files.exists(Paths.get(os.getcwd() + "\\resources\\sounds\\sf2\\SGM-V2.01.sf2"))) {
            JarResourceCopier.copyResource("/resources/sounds/sf2/SGM-V2.01.sf2", Path.of(os.getcwd() + "\\resources\\sounds\\sf2"));
        }
    }

    private static void launchServer() {
        logger.log(LEVEL.INFO, "initialize", "IPv6 Server is launching...");
        threadLogger.log(ThreadLogger.LogLevel.INFO, "IPv6 Server is launching...", "initialize", null);
        Thread server_thread = new Thread(NCWInitialize::serverContinue);
        server_thread.setDaemon(true);
        server_thread.start();
        Thread write_thread = new WriteDownMessage();
        write_thread.setDaemon(true);
        write_thread.start();
    }

    private static void easterEgg() {
        int a = PyRandom.randint(1, 100);
        if (a <= EASTER_EGG_PROBABILITY) {

            logger.log(LEVEL.INFO, "initialize", "Easter egg!");
            threadLogger.log(ThreadLogger.LogLevel.INFO, "Easter egg!", "initialize", null);
            Thread thread = new Thread(MusicThreadPool::start);
            thread.setDaemon(true);
            thread.start();
            EASTER_EGG = true;

        } else {
            if (MUSIC_IS_PLAYING) {
                logger.log(LEVEL.INFO, "initialize", "Play Music...");
                threadLogger.log(ThreadLogger.LogLevel.INFO, "Play Music...", "initialize", null);
                new Thread(NCWInitialize::continue_play).start();
            }
        }
    }

    private static void serverContinue() {
        while (true) {
            try {
                Thread thread = new IPv6Server(SERVER_PORT);
                thread.start();
                thread.join();
            } catch (IOException | InterruptedException e) {
                logger.log(LEVEL.ERROR, "server", "Error!" + e);
                threadLogger.log(ThreadLogger.LogLevel.ERROR, "Error!", "server", e);
            }
        }
    }

    private static void continue_play() {

        try {
            TimeUnit.MILLISECONDS.sleep(1650);
        } catch (InterruptedException ignored) {

        }

        if (Objects.equals(MUSIC_PLAY_MODE, "CUSTOM")) {

            List<String> customMusicList =os.listdir(work_path + "\\resources\\sounds\\music\\custom");
            if (CONTINUE_PLAY) {
                Thread thread_custom = getMusicThreadCustom(customMusicList);
                thread_custom.start();
            }
        } else {
            if (CONTINUE_PLAY) {
                Thread thread_default = getMusicThreadDefault();
                thread_default.start();
            }
        }
    }

    @NotNull
    private static Thread getMusicThreadDefault() {
        Thread thread_default = new Thread(() -> {
           while (true) {
               for (int i = 1; i < LIST_MUSIC.size() + 1; i++) {
                   try {
                       player.play(os.getcwd() + "\\resources\\sounds\\music\\default\\music" + i + ".wav");
                   } catch (UnsupportedAudioFileException | IOException | LineUnavailableException | InterruptedException ignored) {

                   }
               }
           }
        });
        thread_default.setDaemon(true);
        return thread_default;
    }

    @NotNull
    private static Thread getMusicThreadCustom(List<String> customMusicList) {
        Thread thread_custom = new Thread(() -> {
            while (true) {
                for (String path : customMusicList) {
                    try {
                        player.play(path);
                    } catch (UnsupportedAudioFileException | IOException | LineUnavailableException | InterruptedException ignored) {

                    }
                }
            }
        });
        thread_custom.setDaemon(true);
        return thread_custom;
    }

    private static void firstFileCheck() {
        if (!DEBUG) {
            if (FILE_CHECK) {
                try {
                    if (!Objects.equals(FileCheckMethod.check_md5_with_sha(default_music_path + "\\music1.wav"), MD5_SHA256_CHECK.get("music1.wav"))) {
                        logger.log(LEVEL.WARN, "initialize", "File music1.wav do not pass.");
                        threadLogger.log(ThreadLogger.LogLevel.WARN, "File music1.wav do not pass.", "initialize", null);
                        logger.log(LEVEL.INFO, "initialize", "File music1.wav is overwriting.");
                        threadLogger.log(ThreadLogger.LogLevel.INFO, "File music1.wav is overwriting.", "initialize", null);
                        JarResourceCopier.copyResource("/resources/sounds/music/default/music1.wav", default_music_path);
                    }
                } catch (NoSuchAlgorithmException | IOException ignored) {

                }
            }
        }
    }

    private static void threadFileCheck() {
        //文件校验
        if (!DEBUG) {
            if (FILE_CHECK) {
                logger.log(LEVEL.INFO, "initialize", "File check...");
                threadLogger.log(ThreadLogger.LogLevel.INFO, "File check...", "initialize", null);
                try {
                    if (!Objects.equals(FileCheckMethod.check_md5_with_sha(default_music_path + "\\music_info.json"), MD5_SHA256_CHECK.get("music_info.json"))) {
                        logger.log(LEVEL.WARN, "initialize", "File music_info.json do not pass.");
                        threadLogger.log(ThreadLogger.LogLevel.WARN, "File music_info.json do not pass.", "initialize", null);
                        logger.log(LEVEL.INFO, "initialize", "File music_info.json is overwriting.");
                        threadLogger.log(ThreadLogger.LogLevel.INFO, "File music_info.json is overwriting.", "initialize", null);
                        JarResourceCopier.copyResource("/resources/sounds/music/default/music_info.json", default_music_path);
                    }
                } catch (NoSuchAlgorithmException | IOException ignored) {

                }

                try {
                    if (!Objects.equals(FileCheckMethod.check_md5_with_sha(resources_path + "\\ncw.json"), MD5_SHA256_CHECK.get("ncw.json"))) {
                        logger.log(LEVEL.WARN, "initialize", "File ncw.json do not pass.");
                        threadLogger.log(ThreadLogger.LogLevel.WARN, "File ncw.json do not pass.", "initialize", null);
                        logger.log(LEVEL.INFO, "initialize", "File ncw.json is overwriting.");
                        threadLogger.log(ThreadLogger.LogLevel.INFO, "File ncw.json is overwriting.", "initialize", null);
                        JarResourceCopier.copyResource("/resources/ncw.json", resources_path);
                    }
                } catch (NoSuchAlgorithmException | IOException ignored) {

                }

                try {
                    if (!Objects.equals(FileCheckMethod.check_md5_with_sha(sounds_sf2_path + "\\Reality_GMGS_falcomod.sf2"), MD5_SHA256_CHECK.get("Reality_GMGS_falcomod.sf2"))) {
                        logger.log(LEVEL.WARN, "initialize", "File Reality_GMGS_falcomod.sf2 do not pass.");
                        threadLogger.log(ThreadLogger.LogLevel.WARN, "File Reality_GMGS_falcomod.sf2 do not pass.", "initialize", null);
                        logger.log(LEVEL.INFO, "initialize", "File Reality_GMGS_falcomod.sf2 is overwriting.");
                        threadLogger.log(ThreadLogger.LogLevel.INFO, "File Reality_GMGS_falcomod.sf2 is overwriting.", "initialize", null);
                        JarResourceCopier.copyResource("/resources/sounds/sf2/Reality_GMGS_falcomod.sf2", sounds_sf2_path);
                    }
                } catch (NoSuchAlgorithmException | IOException ignored) {

                }

                try {
                    if (!Objects.equals(FileCheckMethod.check_md5_with_sha(sounds_sf2_path + "\\SGM-V2.01.sf2"), MD5_SHA256_CHECK.get("SGM-V2.01.sf2"))) {
                        logger.log(LEVEL.WARN, "initialize", "File SGM-V2.01.sf2 do not pass.");
                        threadLogger.log(ThreadLogger.LogLevel.WARN, "File SGM-V2.01.sf2 do not pass.", "initialize", null);
                        logger.log(LEVEL.INFO, "initialize", "File SGM-V2.01.sf2 is overwriting.");
                        threadLogger.log(ThreadLogger.LogLevel.INFO, "File SGM-V2.01.sf2 is overwriting.", "initialize", null);
                        JarResourceCopier.copyResource("/resources/sounds/sf2/SGM-V2.01.sf2", sounds_sf2_path);
                    }
                } catch (NoSuchAlgorithmException | IOException ignored) {

                }

                for (int i = 2; i < LIST_MUSIC.size() + 1; i++) {
                    try {
                        if (!Objects.equals(FileCheckMethod.check_md5_with_sha(default_music_path + "\\music" + i + ".wav"), MD5_SHA256_CHECK.get("music" + i + ".wav"))) {
                            logger.log(LEVEL.WARN, "initialize", "File music" + i + ".wav do not pass.");
                            threadLogger.log(ThreadLogger.LogLevel.WARN, "File music" + i + ".wav do not pass.", "initialize", null);
                            logger.log(LEVEL.INFO, "initialize", "File music" + i + ".wav is overwriting.");
                            threadLogger.log(ThreadLogger.LogLevel.INFO, "File music" + i + ".wav is overwriting.", "initialize", null);
                            JarResourceCopier.copyResource("/resources/sounds/music/default/music" + i + ".wav", default_music_path);
                        }
                    } catch (NoSuchAlgorithmException | IOException ignored) {

                    }
                }
            }
        }
    }

}
