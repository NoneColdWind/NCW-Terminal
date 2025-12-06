package cn.ncbh.ncw.ncwjavafx.midiplayer;

import cn.ncbh.ncw.ncwjavafx.log.LEVEL;
import cn.ncbh.ncw.ncwjavafx.log.ThreadLogger;

import javax.sound.midi.*;
import java.io.File;
import java.io.IOException;

import static cn.ncbh.ncw.ncwjavafx.base.CommonVariable.*;

public class SF2Player {

    // 自定义音色库路径（在类加载时自动初始化）
    private static final String CUSTOM_SOUNDFONT_PATH = DEFAULT_SF2;
    private static Synthesizer synth;
    private static Soundbank loadedSoundbank;
    private static MidiChannel[] channels;

    // 常用音色常量
    public static final int PIANO = 0;
    public static final int MARIMBA = 12;
    public static final int ORGAN = 16;
    public static final int GUITAR = 25;
    public static final int BASS = 33;
    public static final int VIOLIN = 40;
    public static final int TRUMPET = 56;
    public static final int FLUTE = 73;

    // 静态初始化块：在类加载时初始化音色库
    static {
        try {
            // 1. 创建MIDI合成器
            synth = MidiSystem.getSynthesizer();
            synth.open();

            // 2. 加载自定义音色库
            loadedSoundbank = MidiSystem.getSoundbank(new File(CUSTOM_SOUNDFONT_PATH));

            // 3. 卸载默认音色并加载自定义音色
            if (synth.isSoundbankSupported(loadedSoundbank)) {
                synth.unloadAllInstruments(synth.getDefaultSoundbank());
                synth.loadAllInstruments(loadedSoundbank);
                logger.log(LEVEL.INFO, "SF2Player", "Music Base loaded successfully! The number of instruments: " + loadedSoundbank.getInstruments().length);
                threadLogger.log(ThreadLogger.LogLevel.INFO, "Music Base loaded successfully! The number of instruments: " + loadedSoundbank.getInstruments().length, "SF2Player", null);
            } else {
                logger.log(LEVEL.ERROR, "SF2Player", "Music Base failed to load! ");
                threadLogger.log(ThreadLogger.LogLevel.ERROR, "Music Base failed to load! ", "SF2Player", null);
            }

            // 4. 获取MIDI通道
            channels = synth.getChannels();

            // 5. 注册JVM关闭钩子释放资源
            Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                if (synth != null && synth.isOpen()) {
                    // 停止所有音符
                    for (MidiChannel channel : channels) {
                        if (channel != null) {
                            channel.allNotesOff();
                            channel.allSoundOff();
                        }
                    }
                    synth.close();
                }
            }));
        } catch (MidiUnavailableException | InvalidMidiDataException | IOException e) {
            e.printStackTrace();
            // 尝试回退到默认音色库
            if (synth != null && synth.isOpen()) {
                try {
                    synth.loadAllInstruments(synth.getDefaultSoundbank());
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }
    }

    /**
     * 获取音色库信息
     * @return 音色库描述字符串
     */
    public static String getSoundbankInfo() {
        if (loadedSoundbank == null) return "使用默认音色库";

        return String.format("自定义音色库: %s (%d 种乐器)",
                loadedSoundbank.getName(),
                loadedSoundbank.getInstruments().length);
    }

    /**
     * 播放和弦
     * @param notes 音符数组（MIDI编号）
     * @param velocity 力度(0-127)
     * @param duration 持续时间（毫秒）
     * @param instrument 音色编号（0-127）
     */
    public static void playChord(int[] notes, int velocity, int duration, int instrument) {
        playChord(notes, velocity, duration, instrument, 0);
    }

    /**
     * 播放和弦（指定通道）
     * @param notes 音符数组
     * @param velocity 力度
     * @param duration 持续时间
     * @param instrument 音色编号
     * @param channel 通道编号（0-15）
     */
    public static void playChord(int[] notes, int velocity, int duration, int instrument, int channel) {
        if (notes == null || notes.length == 0 || channels == null) return;

        try {
            // 设置通道音色
            channels[channel].programChange(instrument);

            // 同时播放所有音符
            for (int note : notes) {
                channels[channel].noteOn(note, velocity);
            }

            // 持续播放
            Thread.sleep(duration);

            // 停止所有音符
            for (int note : notes) {
                channels[channel].noteOff(note);
            }
        } catch (Exception ignored) {
        }
    }

    /**
     * 播放单音
     * @param note MIDI音符编号
     * @param velocity 力度(0-127)
     * @param duration 持续时间(毫秒)
     * @param instrument 音色编号
     */
    public static void playNote(int note, int velocity, int duration, int instrument) {
        playNote(note, velocity, duration, instrument, 0);
    }

    /**
     * 播放单音(指定通道)
     * @param note MIDI音符编号
     * @param velocity 力度
     * @param duration 持续时间
     * @param instrument 音色编号
     * @param channel 通道编号(0-15)
     */
    public static void playNote(int note, int velocity, int duration, int instrument, int channel) {
        if (channels == null) return;

        try {
            channels[channel].programChange(instrument);
            channels[channel].noteOn(note, velocity);
            Thread.sleep(duration);
            channels[channel].noteOff(note);
        } catch (Exception ignored) {
        }
    }

    /**
     * 转换音名到MIDI编号
     * @param noteName 音名字符串（如 "C4", "A#5", "Gb3"）
     * @return MIDI音符编号
     */
    public static int convertToMidi(String noteName) {
        // 音符映射 C=0, C#=1, D=2, D#=3, E=4, F=5, F#=6, G=7, G#=8, A=9, A#=10, B=11
        String[] notes = {"C", "C#", "D", "D#", "E", "F", "F#", "G", "G#", "A", "A#", "B"};

        // 解析八度
        int octave = Integer.parseInt(noteName.replaceAll("\\D", ""));
        String baseNote = noteName.replaceAll("\\d", "");

        // 查找基础音高
        int noteValue = -1;
        for (int i = 0; i < notes.length; i++) {
            if (notes[i].equalsIgnoreCase(baseNote)) {
                noteValue = i;
                break;
            }
        }

        // 计算MIDI编号 (C4 = 60)
        return 12 * (octave + 1) + noteValue;
    }
}