package chessFrame;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import sun.audio.AudioPlayer;
import sun.audio.AudioStream;

/**
 * 加载游戏中的各种声音
 * @author ZhuYingtao
 *
 */
public class Voice {
    /**
     * 获得各个声音的实例化对象
     * @param name 声音的文件名
     * @return 声音的实例化对象
     */
	public AudioStream getAudioStream(String name) {
		AudioStream as = null;
		try {
			as = new AudioStream(new FileInputStream("voice/" + name + ".wav"));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return as;
	}
    /**
     * 播放声音
     * @param name 声音的文件名
     */
	public void play(String name) {
		AudioStream as = getAudioStream(name);
		AudioPlayer.player.start(as);
	}
}
