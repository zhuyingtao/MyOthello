package chessFrame;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import sun.audio.AudioPlayer;
import sun.audio.AudioStream;

/**
 * ������Ϸ�еĸ�������
 * @author ZhuYingtao
 *
 */
public class Voice {
    /**
     * ��ø���������ʵ��������
     * @param name �������ļ���
     * @return ������ʵ��������
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
     * ��������
     * @param name �������ļ���
     */
	public void play(String name) {
		AudioStream as = getAudioStream(name);
		AudioPlayer.player.start(as);
	}
}
