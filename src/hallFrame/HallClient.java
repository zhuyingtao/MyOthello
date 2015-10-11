package hallFrame;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

import server.HallServer;
import server.Server;
/**
 * �������ڽ��ܴӴ������������յ������ݣ���ʵ��һЩ����
 * @author  ZhuYingtao
 *
 */
public class HallClient implements Runnable {
	
	private Socket s = null;
	/**
	 * ������Ϣ��������
	 */
	public DataOutputStream dos = null;
	/**
	 * ��ȡ�ӷ��������յ�����Ϣ
	 */
	public DataInputStream dis = null;
	private HallNamePanel hnp = null;
	private Room room = null;
    /**
     * ʵ�������������������
     */
	public HallClient() {
		try {
			s = new Socket(Server.IP, HallServer.TCP_PORT);
			dos = new DataOutputStream(s.getOutputStream());
			dis = new DataInputStream(s.getInputStream());
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
    /**
     * �õ�HallNamePanel��Ķ���
     * @param hnp
     */
	public void getHallNamePanel(HallNamePanel hnp) {
		this.hnp = hnp;
	}
    /**
     * �õ�Room��Ķ���
     * @param room
     */
	public void getRoom(Room room) {
		this.room = room;
	}
    /**
     * �趨һ���̣߳����ϴӷ������˽�����Ϣ
     */
	public void run() {
		String info = null;
		try {
			while (true) {
				info = dis.readUTF();
				if (info.equals("nickname")) {
					hnp.read(dis);
				}
				if (info.equals("sit")) {
					room.read(dis);
				}

			}
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
}
