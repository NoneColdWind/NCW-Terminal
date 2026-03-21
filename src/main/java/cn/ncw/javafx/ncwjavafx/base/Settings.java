package cn.ncw.javafx.ncwjavafx.base;

public class Settings {

    private boolean debug;

    private boolean file_check;

    private int default_float_length;

    private String background_mode;

    private boolean music_is_playing;

    private boolean continue_playing;

    private String music_pref;

    private String music_play_mode;

    private boolean login;

    private int launch_times;

    private String default_sf2;

    private int default_server_port;

    private String operate_mode;

    public Settings() {

    }

    public Settings(boolean _debug, boolean _file_check, int _default_float_length, String _background_mode, boolean _music_is_playing, boolean _continue_playing, String _music_pref, String _music_play_mode, boolean _login, int _launch_times, String _default_sf2, int _default_server_port, String _operate_mode) {

        this.debug = _debug;
        this.file_check = _file_check;
        this.background_mode = _background_mode;
        this.music_is_playing = _music_is_playing;
        this.continue_playing = _continue_playing;
        this.music_pref = _music_pref;
        this.music_play_mode = _music_play_mode;
        this.login = _login;
        this.launch_times = _launch_times;
        this.default_sf2 = _default_sf2;
        this.default_server_port = _default_server_port;
        this.operate_mode = _operate_mode;

    }

    public boolean isDebug() {
        return this.debug;
    }

    public boolean isFile_check() {
        boolean ret = this.file_check;
        if (this.debug) {
            ret = false;
        }
        return ret;
    }

    public int getDefault_float_length() {
        return this.default_float_length;
    }

    public String getBackground_mode() {
        return this.background_mode;
    }

    public boolean isMusic_is_playing() {
        return this.music_is_playing;
    }

    public boolean isContinue_playing() {
        return this.continue_playing;
    }

    public String getMusic_pref() {
        return this.music_pref;
    }

    public String getMusic_play_mode() {
        return this.music_play_mode;
    }

    public boolean isLogin() {
        return this.login;
    }

    public int getLaunch_times() {
        return this.launch_times;
    }

    public String getDefault_sf2() {
        return this.default_sf2;
    }

    public int getDefault_server_port() {
        return this.default_server_port;
    }

    public String getOperate_mode() {
        return this.operate_mode;
    }



    public void setDebug(boolean value) {
        this.debug = value;
    }

    public void setFile_check(boolean value) {
        this.file_check = value;
    }

    public void setDefault_float_length(int value) {
        this.default_float_length = value;
    }

    public void setBackground_mode(String value) {
        this.background_mode = value;
    }

    public void setMusic_is_playing(boolean value) {
        this.music_is_playing = value;
    }

    public void setContinue_playing(boolean value) {
        this.continue_playing = value;
    }

    public void setMusic_pref(String value) {
        this.music_pref = value;
    }

    public void setMusic_play_mode(String value) {
        this.music_play_mode = value;
    }

    public void setLogin(boolean value) {
        this.login = value;
    }

    public void setLaunch_times(int value) {
        this.launch_times = value;
    }

    public void setDefault_sf2(String value) {
        this.default_sf2 = value;
    }

    public void setDefault_server_port(int value) {
        this.default_server_port = value;
    }

    public void setOperate_mode(String value) {
        this.operate_mode = value;
    }

}
