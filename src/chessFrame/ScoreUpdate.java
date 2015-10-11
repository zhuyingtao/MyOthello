package chessFrame;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

import server.RegServer;
import server.Server;
/**
 * �������ڸ�����ҵ���Ϣ������֣�ʤ��������
 * @author ZhuYingtao
 *
 */
public class ScoreUpdate {
	@SuppressWarnings("unused")
	private ChessBoard cb = null;
	private Socket s=null;
	private DataOutputStream dos=null;
	@SuppressWarnings("unused")
	private DataInputStream dis=null;
  
	public ScoreUpdate(ChessBoard cb) {
		this.cb = cb;
		this.connect();
	}
	/**
	 * �������ݿ�
	 */
	public void connect(){
		try {
			s = new Socket(Server.IP, RegServer.TCP_PORT);
			dos = new DataOutputStream(s.getOutputStream());
			dis = new DataInputStream(s.getInputStream());
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	/**
	 * �����ݿⷢ�͸��º����Ϸ��Ϣ
	 * @param winOrLose ʤ��
	 * @param score  �÷�
	 * @param name   �ǳ�
	 */
	public void update(int winOrLose, int score,String name){
        try {
			dos.writeUTF("update");
			dos.writeUTF(name);
		    dos.writeInt(score);
		    dos.writeInt(winOrLose);
		} catch (IOException e) {
			e.printStackTrace();
		}
        
	}
}
