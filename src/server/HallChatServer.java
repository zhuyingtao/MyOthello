package server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
/**
 * 关于游戏中聊天的服务器
 * @author ZhuYingtao
 *
 */
public class HallChatServer implements Runnable {
	/**
	 * 服务器的端口号
	 */
	public static final int TCP_PORT = 7777;

	private List<Client> clients = new ArrayList<Client>();
	 /**
     * 服务器线程，接收客户端的连接并处理
     */
	public void run() {
		ServerSocket ss = null;
		try {
			ss = new ServerSocket(TCP_PORT);
			System.out.println("大厅聊天服务器已启动，等待连接,端口号：" + TCP_PORT);
			while (true) {
				Socket s = ss.accept();
				Client c = new Client(s);
				clients.add(c);
				new Thread(c).start();
			}
		} catch (IOException e) {
			System.out.println("端口使用中，请重新再试。");
			System.exit(0);
		} finally {
			try {
				ss.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	class Client implements Runnable {
		private Socket s = null;

		private DataInputStream dis = null;

		private DataOutputStream dos = null;
		String info = null;

		public Client(Socket s) {
			this.s = s;
		}

		public void send(String str) {
			try {
				dos = new DataOutputStream(s.getOutputStream());
				dos.writeUTF(str);
			} catch (IOException e) {
				System.out.println("系统错误，请重新启动");

			}
		}

		public void run() {
			try {
				dis = new DataInputStream(s.getInputStream());
				while (true) {
					info = dis.readUTF();
					if (info.equals("enter")) {
						String str = dis.readUTF();
						for (int i = 0; i < clients.size(); i++) {
							Client c = clients.get(i);
							c.send("enter");
							c.send(str);
						}
					}
					if (info.equals("talking")) {
						String name = dis.readUTF();
						String str = dis.readUTF();
						for (int i = 0; i < clients.size(); i++) {
							Client c = clients.get(i);
							c.send("talking");
							c.send(name);
							c.send(str);
						}
					}
				}
			} catch (IOException e) {
				clients.remove(this);
				System.out.println("客户端退出了！！");
			}
		}
	}
}
