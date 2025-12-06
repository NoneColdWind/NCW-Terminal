package cn.ncbh.ncw.ncwjavafx.midiplayer;

import javax.sound.midi.MidiChannel;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.Soundbank;
import javax.sound.midi.Synthesizer;
import java.io.File;
import java.util.concurrent.TimeUnit;

public class SoundFontLoader {
    public static void main(String[] args) throws Exception {
        // 1. 获取 MIDI 合成器
        Synthesizer synth = MidiSystem.getSynthesizer();
        synth.open();

        // 2. 加载 .sf2 文件
        Soundbank soundbank = MidiSystem.getSoundbank(new File("check\\Reality_GMGS_falcomod.sf2"));

        if (synth.isSoundbankSupported(soundbank)) {
            // 3. 将音色库加载到合成器
            synth.loadAllInstruments(soundbank);
            System.out.println("SoundFont loaded!");

            // 4. 播放示例（例如通道0，音高60，力度93）
            for (int i = 0; i < 128; i++) {
                MidiChannel channel = synth.getChannels()[0];

                channel.noteOn(60, 93); // 播放音符 C4
                Thread.sleep(1000);     // 持续1秒
                channel.noteOff(60);
                TimeUnit.SECONDS.sleep(1);
            }
        } else {
            System.err.println("SoundBank not supported!");
        }

        synth.close();
    }
}
