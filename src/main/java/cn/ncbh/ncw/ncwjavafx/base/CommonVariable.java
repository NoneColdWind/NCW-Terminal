package cn.ncbh.ncw.ncwjavafx.base;

import cn.ncbh.ncw.ncwjavafx.log.Logger;
import cn.ncbh.ncw.ncwjavafx.log.ThreadLogger;
import cn.ncbh.ncw.ncwjavafx.midiplayer.StreamAudioPlayer;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

public class CommonVariable {

    public static final Logger logger = new Logger("NCW-Terminal");

    public static final ThreadLogger threadLogger = new ThreadLogger("NCW-Terminal");

    public static String CURRENT_TIME = String.format("%-" + 15 + "s", LocalTime.now().toString().substring(0, Math.min(LocalTime.now().toString().length(), 15))).replace(":", "").replace(".", "");

    public static StreamAudioPlayer player;

    public static Map<String, String> MD5_SHA256_CHECK = new Hashtable<>();

    static {
        MD5_SHA256_CHECK.put("music1.wav", "6f3b2a7439ef4cd0f06d406eac24a5d4e79de441572d50b41bdeaf10a61b56bb");
        MD5_SHA256_CHECK.put("music2.wav", "a9ff5e0850c3d9b4bfdeabdc9549d77c278e1aa13ca5f51eb56d55fcbc523322");
        MD5_SHA256_CHECK.put("music3.wav", "15362232c92d13a31fbf5f9edaebccbdef4540499280a1f7f4191ed93a42ef88");
        MD5_SHA256_CHECK.put("music4.wav", "15049e91bc09fa86e3f5a75a44c5ad6b34750bb7862500aa1c3a965421a8f4f7");
        MD5_SHA256_CHECK.put("music5.wav", "fd6620d9484d8f9c01ba4fb0f5179abcb66b0d94c2fa06e322c302363ded2bb0");
        MD5_SHA256_CHECK.put("ncw.json", "ace26cdc3f7966a2689db201081d2f864919a726745e6ecc30944343a117d54f");
        MD5_SHA256_CHECK.put("music_info.json", "c07e3feca4e43382b1db7b54607b396cc1485a0cdafcacf795e29ae03cb26b81");
        MD5_SHA256_CHECK.put("Reality_GMGS_falcomod.sf2", "270bcfa4b247456eeacd44e38a6c3f4c17588dab2b758859a5834552011bedf6");
        MD5_SHA256_CHECK.put("SGM-V2.01.sf2", "d7f36e0676b9b84524978b642c0cbc252c2fb557c779503047e95dd2394a859c");
    }

    public static boolean DEBUG;
    public static boolean FILE_CHECK;
    public static int DEFAULT_FLOAT_PART_LENGTH;
    public static String BACKGROUND_MODE;
    public static boolean MUSIC_IS_PLAYING;
    public static boolean CONTINUE_PLAY;
    public static String MUSIC_PLAY_MODE;
    public static boolean LOGIN;
    public static int LAUNCH_TIMES;
    public static String DEFAULT_SF2;
    public static int SERVER_PORT;
    public static String OPERATE_MODE;
    public static int EASTER_EGG_PROBABILITY;

    public static List<String> LIST_OPERATE_WORD;

    public static List<String> LIST_RESOURCES_FILE;

    public static List<String> LIST_DAY;

    public static List<String> LIST_NIGHT;

    public static List<String> LIST_MUSIC;

    public static List<String> LIST_BACKGROUND = new ArrayList<>();

    public static List<String> LIST_WAIT= new ArrayList<>();

    public static List<Object> LIST_FOR_OPERATING = new ArrayList<>();

    public static Settings SETTINGS = new Settings();

    public static String IMAGE = "";

    public static String STRING_WAIT = "";

    public static String STRING_SHOW = "";

    public static String RESULT = "";

    public static boolean EASTER_EGG;

    public static List<String> LIST_MESSAGE = new ArrayList<>();

}
