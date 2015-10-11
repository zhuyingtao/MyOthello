package hallFrame;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

import server.HallServer;
import server.Server;
/**
 * 此类用于接受从大厅服务器接收到的数据，以实现一些功能
 * @author  ZhuYingtao
 *
 */
public class HallClient implements Runnable {
	
	private Socket s = null;
	/**
	 * 发送信息到服务器
	 */
	public DataOutputStream dos = null;
	/**
	 * 读取从服务器接收到的信息
	 */
	public DataInputStream dis = null;
	private HallNamePanel hnp = null;
	private Room room = null;
    /**
     * 实现与大厅服务器的连接
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
     * 得到HallNamePanel类的对象
     * @param hnp
     */
	public void getHallNamePanel(HallNamePanel hnp) {
		this.hnp = hnp;
	}
    /**
     * 得到Room类的对象
     * @param room
     */
	public void getRoom(Room room) {
		this.room = room;
	}
    /**
     * 设定一个线程，不断从服务器端接收信息
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
