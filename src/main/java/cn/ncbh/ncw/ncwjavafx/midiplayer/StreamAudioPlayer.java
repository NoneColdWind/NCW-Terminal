package cn.ncbh.ncw.ncwjavafx.midiplayer;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;

/**
 * 支持暂停/继续功能的流式音频播放器
 */
public class StreamAudioPlayer {

    // 播放状态常量
    public static final int STATE_PLAYING = 0;
    public static final int STATE_PAUSED = 1;
    public static final int STATE_STOPPED = 2;

    // 成员变量
    private SourceDataLine sourceDataLine;
    private Thread playbackThread;
    private AudioInputStream audioStream;
    private volatile boolean playing;
    private volatile boolean paused;
    private volatile int playbackState;
    private final Object playLock = new Object();

    /**
     * 播放指定的WAV文件
     *
     * @param filePath WAV文件路径
     * @throws UnsupportedAudioFileException 如果文件格式不支持
     * @throws IOException 如果文件读取错误
     * @throws LineUnavailableException 如果音频设备不可用
     */
    public void play(String filePath)
            throws UnsupportedAudioFileException, IOException, LineUnavailableException, InterruptedException {

        if (!(playbackThread == null)) {
            if (playbackThread.isAlive()) {
                playbackThread.join();
            }
        }

        // 准备音频流
        File audioFile = new File(filePath);
        audioStream = AudioSystem.getAudioInputStream(audioFile);
        AudioFormat format = audioStream.getFormat();

        // 如果不支持此格式，则转换为标准PCM格式
        if (!isFormatSupported(format)) {
            format = new AudioFormat(
                    AudioFormat.Encoding.PCM_SIGNED,
                    format.getSampleRate(),
                    16,
                    format.getChannels(),
                    format.getChannels() * 2,
                    format.getSampleRate(),
                    false);
            audioStream = AudioSystem.getAudioInputStream(format, audioStream);
        }

        // 打开数据行
        DataLine.Info info = new DataLine.Info(SourceDataLine.class, format);
        sourceDataLine = (SourceDataLine) AudioSystem.getLine(info);
        sourceDataLine.open(format);

        // 设置初始状态
        playing = true;
        paused = false;
        playbackState = STATE_PLAYING;

        // 启动播放线程
        playbackThread = new Thread(this::streamPlayback);
        playbackThread.setDaemon(true);
        playbackThread.start();
    }

    /**
     * 暂停播放
     */
    public void pause() {
        if (sourceDataLine != null && sourceDataLine.isRunning()) {
            synchronized (playLock) {
                sourceDataLine.stop();
                paused = true;
                playbackState = STATE_PAUSED;
            }
        }
    }

    /**
     * 继续播放
     */
    public void resume() {
        if (sourceDataLine != null && !sourceDataLine.isRunning() && paused) {
            synchronized (playLock) {
                sourceDataLine.start();
                paused = false;
                playbackState = STATE_PLAYING;
                playLock.notifyAll(); // 唤醒播放线程
            }
        }
    }

    /**
     * 停止播放并释放资源
     */
    public void stop() {
        if (sourceDataLine != null) {
            playing = false;
            paused = false;

            // 唤醒可能处于等待状态的播放线程
            synchronized (playLock) {
                playLock.notifyAll();
            }

            // 等待播放线程结束
            try {
                if (playbackThread != null && playbackThread.isAlive()) {
                    playbackThread.join(500);
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }

            // 关闭资源
            closeResources();
            playbackState = STATE_STOPPED;
        }
    }

    /**
     * 获取当前播放状态
     *
     * @return 播放状态（STATE_PLAYING/STATE_PAUSED/STATE_STOPPED）
     */
    public int getPlaybackState() {
        return playbackState;
    }

    /**
     * 获取音频流的格式信息（播放前不可用）
     *
     * @return 音频格式对象，如果没有音频流则返回null
     */
    public AudioFormat getAudioFormat() {
        return sourceDataLine != null ? sourceDataLine.getFormat() : null;
    }

    // 流式播放核心逻辑
    private void streamPlayback() {
        try {
            sourceDataLine.start();
            int bufferSize = 4096; // 4KB缓冲区
            byte[] buffer = new byte[bufferSize];
            int bytesRead;

            // 播放循环
            while (playing) {
                // 当暂停时，等待
                synchronized (playLock) {
                    while (paused) {
                        try {
                            playLock.wait();
                        } catch (InterruptedException e) {
                            Thread.currentThread().interrupt();
                            return;
                        }
                    }
                }

                // 读取数据
                bytesRead = audioStream.read(buffer, 0, buffer.length);

                if (bytesRead == -1) {
                    break; // 文件结束
                }

                // 写入数据行
                if (bytesRead > 0) {
                    sourceDataLine.write(buffer, 0, bytesRead);
                }
            }

            // 播放完成，释放资源
            sourceDataLine.drain();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (playing) {
                closeResources();
            }
            playbackState = STATE_STOPPED;
        }
    }

    // 关闭音频资源
    private void closeResources() {
        try {
            if (sourceDataLine != null) {
                sourceDataLine.stop();
                sourceDataLine.close();
                sourceDataLine = null;
            }
            if (audioStream != null) {
                audioStream.close();
                audioStream = null;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // 检查格式是否支持
    private boolean isFormatSupported(AudioFormat format) {
        DataLine.Info info = new DataLine.Info(SourceDataLine.class, format);
        return AudioSystem.isLineSupported(info);
    }
}