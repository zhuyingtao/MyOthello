package server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
/**
 * ������Ϸ������ķ�����
 * @author ZhuYingtao
 *
 */
public class GameChatServer implements Runnable {
	/**
	 * �������Ķ˿ں�
	 */
	int TCP_PORT;

	private List<Client> clients = new ArrayList<Client>();
    /**
     * �õ��˿ںţ��������������߳�
     * @param tcp_port
     */
	public GameChatServer(int tcp_port) {
		this.TCP_PORT = tcp_port;
		System.out.println(TCP_PORT);
		new Thread(this).start();
	}
	 /**
     * �������̣߳����տͻ��˵����Ӳ�����
     */
	public void run() {
		ServerSocket ss = null;
		try {
			ss = new ServerSocket(TCP_PORT);
			System.out.println("������������������ȴ�����,�˿ںţ�" + TCP_PORT);
			while (true) {
				Socket s = ss.accept();
				Client c = new Client(s);
				clients.add(c);
				new Thread(c).start();
				System.out.println("a gamechatclient connect");
			}
		} catch (IOException e) {
			System.out.println("�˿�ʹ���У����������ԡ�");
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
				System.out.println("ϵͳ��������������");

			}
		}

		public void run() {
			try {
				dis = new DataInputStream(s.getInputStream());
				while (true) {
					info = dis.readUTF();
					System.out.println(info);
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
						String sex = dis.readUTF();
						if (str != null && str != "") {
							for (int i = 0; i < clients.size(); i++) {
								Client c = clients.get(i);
								c.send("talking");
								c.send(name);
								c.send(str);
								c.send(sex);
							}
						}
					}
				}
			} catch (IOException e) {
				clients.remove(this);
				System.out.println("�ͻ����˳��ˣ���");
			}
		}
	}
}
