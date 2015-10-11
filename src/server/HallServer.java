package server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

/**
 * ���ڴ����ķ�����
 * 
 * @author ZhuYingtao
 * 
 */
public class HallServer implements Runnable {
	/**
	 * �����������Ķ˿ں�
	 */
	public static final int TCP_PORT = 7001;
	/**
	 * ��Ϸ�������ĳ�ʼ�˿ںţ� �ټ��ϴ���������λ���뼴�ǿ�����Ϸ��������ʵ�ʶ˿ں�
	 */
	public static final int game_TCP_PORT = 6000;
	private List<Client> clients = new ArrayList<Client>();
	private List<String> names = new ArrayList<String>();// �洢����ҵ��ǳ�
	private List<String> scores = new ArrayList<String>();
	private List<String> levels = new ArrayList<String>();
	private int[][] chairStates = new int[50][2]; // �洢����λ�ӵ�״̬
	private boolean[] firstEnter = new boolean[50];
	private String info = null;

	/**
	 * �������������̣߳��������̼߳��ɽ��շ���������������
	 */
	public void run() {
		ServerSocket ss = null;
		for (int i = 0; i < firstEnter.length; i++) {
			firstEnter[i] = true;
		}
		try {
			ss = new ServerSocket(TCP_PORT);
			System.out.println("�������������������ȴ�����,�˿ںţ�" + TCP_PORT);
			while (true) {
				Socket s = ss.accept();
				Client c = new Client(s);
				clients.add(c);
				new Thread(c).start();
			}
		} catch (IOException e) {
			System.out.println("�˿�ʹ���У����������ԡ�");
		} finally {
			try {
				ss.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	class Client implements Runnable {
		Socket s = null;

		private DataInputStream dis = null;

		private DataOutputStream dos = null;
		String nickname = null;
		String score = null;
		String level = null;

		public Client(Socket s) {
			this.s = s;
			try {
				dis = new DataInputStream(s.getInputStream());
				dos = new DataOutputStream(s.getOutputStream());
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		public void run() {
			try {
				while (true) {
					info = dis.readUTF();
					if (info.equals("firstEnter")) {
						int tableNo = dis.readInt();
						if (firstEnter[tableNo] == true) {
							new ChessServer(game_TCP_PORT + tableNo);
							new GameChatServer(game_TCP_PORT + tableNo + 100);
							firstEnter[tableNo] = false;
						}
					}
					if (info.equals("nickname")) {
						nickname = dis.readUTF();
						score = dis.readUTF();
						level = dis.readUTF();

						names.add(nickname);
						scores.add(score + "");
						levels.add(level);
						for (int i = 0; i < clients.size(); i++) {
							Client c = clients.get(i);
							c.send();
						}
					}
					if (info.equals("sit")) {
						int tableNo = dis.readInt();
						int chairNo = dis.readInt();
						chairStates[tableNo][chairNo] = 1;
						for (int i = 0; i < clients.size(); i++) {
							Client c = clients.get(i);
							c.send();
						}
					}
					if (info.equals("enterRoom")) {
						for (int i = 0; i < clients.size(); i++) {
							Client c = clients.get(i);
							c.send();
						}
					}
					if (info.equals("exitRoom")) {
						int tableNo = dis.readInt();
						int chairNo = dis.readInt();
						chairStates[tableNo][chairNo] = 0;
						for (int i = 0; i < clients.size(); i++) {
							Client c = clients.get(i);
							c.send();
						}
					}
					if (info.equals("exit")) {
						names.remove(this.nickname);
						scores.remove(this.score);
						levels.remove(this.level);
						clients.remove(this);
						info = "nickname";
						for (int i = 0; i < clients.size(); i++) {
							Client c = clients.get(i);
							c.send();
						}
					}
				}
			} catch (IOException e) {
				System.out.println("HallServer:�ͻ����˳��ˣ���");
				names.remove(this.nickname);
				scores.remove(this.score);
				levels.remove(this.level);
				clients.remove(this);
				info = "nickname";
				for (int i = 0; i < clients.size(); i++) {
					Client c = clients.get(i);
					c.send();
				}
			}
		}

		public void send() {
			try {
				if (info.equals("nickname")) {
					dos.writeUTF("nickname");
					dos.writeInt(names.size());
					for (int i = 0; i < names.size(); i++) {
						dos.writeUTF(names.get(i));
						dos.writeInt(Integer.parseInt(scores.get(i)));
						if (levels.size() != 0) {
							dos.writeUTF(levels.get(i));
						}
					}
				}
				if (info.equals("sit") || info.equals("enterRoom")
						|| info.equals("exitRoom")) {
					dos.writeUTF("sit");
					for (int i = 0; i < 50; i++) {
						for (int j = 0; j < 2; j++) {
							dos.writeInt(chairStates[i][j]);
						}
					}
				}

			} catch (IOException e) {
				e.printStackTrace();
				System.out.println("ϵͳ��������������");

			}
		}
	}
}
