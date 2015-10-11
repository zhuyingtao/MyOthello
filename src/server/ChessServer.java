package server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

/**
 * 关于下棋的服务器
 * 
 * @author ZhuYingtao
 * 
 */
public class ChessServer implements Runnable {
	/**
	 * 下棋服务器的端口号
	 */
	int TCP_PORT;

	private List<Client> clients = new ArrayList<Client>();
	private List<String> names = new ArrayList<String>();
	private List<String> scores = new ArrayList<String>();
	private List<String> levels = new ArrayList<String>();
	/**
	 * 玩家1是否准备
	 */
	boolean oneReady = false;
	/**
	 * 玩家2是否准备
	 */
	boolean twoReady = false;
	/**
	 * 玩家1是否进入
	 */
	boolean hasPlayer1 = false;
	/**
	 * 玩家2是否进入
	 */
	boolean hasPlayer2 = false;
	private boolean firstStart = true;

	private String player1Name = "";
	private String player2Name = "";
	private String player1Sex = "";
	private String player2Sex = "";

	private ServerSocket ss = null;

	/**
	 * 得到端口号，启动服务器线程
	 * 
	 * @param tcp_port
	 */
	public ChessServer(int tcp_port) {
		this.TCP_PORT = tcp_port;
		System.out.println(TCP_PORT);
		new Thread(this).start();
	}

	/**
	 * 服务器线程，接收客户端的连接并处理
	 */
	public void run() {

		try {
			ss = new ServerSocket(TCP_PORT);
			System.out.println("下棋服务器已启动，等待连接,端口号：" + TCP_PORT);
			while (true) {
				Socket s = ss.accept();
				Client c = new Client(s);
				clients.add(c);

				new Thread(c).start();

				DataOutputStream dos = new DataOutputStream(s.getOutputStream());
				if (hasPlayer1 == false && hasPlayer2 == false) {// 玩家1第一次进入
					hasPlayer1 = true;
					dos.writeUTF("isPlayer1");
				} else if (hasPlayer1 == true && hasPlayer2 == false) {// 玩家2第一次进入
					hasPlayer2 = true;
					dos.writeUTF("isPlayer2");
					dos.writeUTF("player1Enter");
					DataOutputStream dos2 = new DataOutputStream(clients // 得到上一个客户端的dos
							.get(clients.indexOf(c) - 1).s.getOutputStream());
					dos2.writeUTF("player2Enter");
				} else if (hasPlayer1 == true && hasPlayer2 == true) {// 观战者
					dos.writeUTF("watcher");
				} else if (hasPlayer1 == false && hasPlayer2 == true) {
					hasPlayer1 = true;
					dos.writeUTF("isPlayer1");
					dos.writeUTF("player2Enter");
					DataOutputStream dos3 = new DataOutputStream(clients // 得到上一个客户端的dos
							.get(clients.indexOf(c) - 1).s.getOutputStream());
					dos3.writeUTF("player1Enter");

				}
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

		String info = null;// 标记发送给服务器的数据类型
		String nickname = null;
		String score = null;
		String level = null;

		private DataInputStream dis = null;
		private DataOutputStream dos = null;

		public Client(Socket s) {
			this.s = s;
		}

		public void run() {
			try {
				dis = new DataInputStream(s.getInputStream());

				while (true) {
					info = dis.readUTF();
					System.out.println(info);
					if (info.equals("player1Name")) {
						player1Name = dis.readUTF();
						player1Sex = dis.readUTF();
						sendPlayerNames();
					}
					if (info.equals("player2Name")) {
						player2Name = dis.readUTF();
						player2Sex = dis.readUTF();
						sendPlayerNames();
					}
					if (info.equals("getPlayersName")) {
						sendPlayerNames();
					}
					if (info.equals("allPlayerName")) {
						nickname = dis.readUTF();
						score = ""+dis.readInt();
						level = dis.readUTF();
						names.add(nickname);
						scores.add(score);
						levels.add(level);
						sendAllNames();

					}
					if (info.equals("ready")) {
						boolean isplayer1 = dis.readBoolean();
						boolean isplayer2 = dis.readBoolean();
						boolean isReady = dis.readBoolean();
						if (isplayer1 == true && isplayer2 == false) {
							oneReady = isReady;
						}
						if (isplayer2 == true && isplayer1 == false) {
							twoReady = isReady;
						}
						if (oneReady == true && twoReady == true) {
							boolean canPlay = true;
							sendReady(canPlay);

						}
					}
					if (info.equals("player1played")) {
						for (int i = 0; i < 2; i++) {
							Client c = clients.get(i);
							DataOutputStream dos = new DataOutputStream(c.s
									.getOutputStream());
							dos.writeUTF("player1played");
						}
					}
					if (info.equals("chess")) {
						int[][] chess = new int[8][8];
						for (int i = 0; i < 8; i++) {
							for (int j = 0; j < 8; j++) {
								chess[i][j] = dis.readInt();
							}
						}
						boolean isBlack = dis.readBoolean();
						boolean canPlay = dis.readBoolean();
						boolean isStop = dis.readBoolean();
						String message = dis.readUTF();
						int countBlack = dis.readInt();
						int countWhite = dis.readInt();
						for (int n = 0; n < clients.size(); n++) {
							getDos(n);
							if (dos != null) {
								dos.writeUTF("chess");
								for (int i = 0; i < 8; i++) {
									for (int j = 0; j < 8; j++) {
										dos.writeInt(chess[i][j]);
									}
								}
								dos.writeBoolean(isBlack);
								dos.writeBoolean(canPlay);
								dos.writeBoolean(isStop);
								dos.writeUTF(message);
								dos.writeInt(countBlack);
								dos.writeInt(countWhite);
							}
						}
					}
					if (info.equals("win")) {
						oneReady = false;
						twoReady = false;
					}
					if(info.equals("restart")){
						sendOption("restart");
					}
					if (info.equals("regret")) {
						sendOption("regret");
					}
					if (info.equals("draw")) {
						sendOption("draw");
					}
					if (info.equals("open")) {
						sendOption("open");
					}
					if(info.equals("time")){
						sendOption("time");
					}
					if(info.equals("oneTime")){
						sendOption("oneTime");
					}
					if (info.equals("giveUp")) {
						boolean isplayer1 = dis.readBoolean();
						boolean isplayer2 = dis.readBoolean();
						for (int i = 0; i < clients.size(); i++) {
							Client c = clients.get(i);
							dos = new DataOutputStream(c.s.getOutputStream());
							dos.writeUTF("giveUp");
							dos.writeBoolean(isplayer1);
							dos.writeBoolean(isplayer2);
						}
						oneReady = false;
						twoReady = false;
					}
					if (info.equals("answer")) {
						Client c = null;
						boolean isplayer1 = dis.readBoolean();
						boolean isplayer2 = dis.readBoolean();
						String info2 = dis.readUTF();
						int answer = dis.readInt();
						if (info2.equals("draw") && answer == 0) {
							oneReady = false;
							twoReady = false;
						}
						if (isplayer1 == true && isplayer2 == false) {
							c = clients.get(1);
						}
						if (isplayer1 == false && isplayer2 == true) {
							c = clients.get(0);
						}
						dos = new DataOutputStream(c.s.getOutputStream());
						dos.writeUTF("answer");
						dos.writeUTF(info2);
						dos.writeInt(answer);

					}
					if (info.equals("exit")) {
						oneReady = false;
						twoReady = false;
						firstStart = true;
						boolean canPlay = false;// 客户端退出，游戏结束
						boolean isplayer1 = dis.readBoolean();
						boolean isplayer2 = dis.readBoolean();
						if (isplayer1 == true && isplayer2 == false) {
							hasPlayer1 = false;
							player1Name = "";
							names.remove(this.nickname);
						    if (scores.size() != 0) {
								scores.remove(this.score);
							}
							levels.remove(this.level);
							clients.remove(this);
							sendAllNames();
							for (int n = 0; n < clients.size(); n++) {
								getDos(n);
								if (dos != null) {
									dos.writeUTF("player1Exit");
									dos.writeBoolean(canPlay);
								}
							}
						}
						if (isplayer1 == false && isplayer2 == true) {
							hasPlayer2 = false;
							player2Name = "";
							names.remove(this.nickname);
							scores.remove(this.score);
							levels.remove(this.level);
							clients.remove(this);
							sendAllNames();
							for (int n = 0; n < clients.size(); n++) {
								getDos(n);
								if (dos != null) {
									dos.writeUTF("player2Exit");
									dos.writeBoolean(canPlay);
								}
							}
						}
					}
					if (info.equals("watcherEnd")) {
						names.remove(this.nickname);
						scores.remove(this.score);
						levels.remove(this.level);
						clients.remove(this);
						sendAllNames();
					}
					if(info.equals("update")){
						boolean isplayer1=dis.readBoolean();
						boolean isplayer2=dis.readBoolean();
						int score=dis.readInt();
						
						if(isplayer1==true&&isplayer2==false){
							int totalScore=Integer.parseInt(scores.get(0));
						    scores.set(0, totalScore+score+"");
						}
						if(isplayer2==true&&isplayer1==false){
							int totalScore=Integer.parseInt(scores.get(1));
							scores.set(1, totalScore+score+"");
						}
						sendAllNames();
						
					}
				}
			} catch (IOException e) {
				names.remove(this.nickname);
				if (scores.size() != 0) {
					scores.remove(this.score);
				}
				levels.remove(this.level);
				clients.remove(this);
				sendAllNames();
				System.out.println("ChessServer:客户端退出了！！");
			}
		}

		public void getDos(int n) {
			Client c = clients.get(n);
			if (c == this) {
				return;
			}
			try {
				dos = new DataOutputStream(c.s.getOutputStream());// 得到不同客户端的dos
			} catch (IOException e) {
				e.printStackTrace();
			}

		}

		public void sendAllNames() {
			for (int i = 0; i < clients.size(); i++) {
				Client c = clients.get(i);
				try {
					DataOutputStream dos = new DataOutputStream(c.s
							.getOutputStream());
					dos.writeUTF("allPlayerName");
					dos.writeInt(names.size());
					for (int j = 0; j < names.size(); j++) {
						dos.writeUTF(names.get(j));
						if (scores.size() != 0) {
							dos.writeInt(Integer.parseInt(scores.get(j)));
						}
						if (levels.size() != 0) {
							dos.writeUTF(levels.get(j));
						}
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		public void sendPlayerNames() {
			for (int i = 0; i < clients.size(); i++) {
				Client c = clients.get(i);// 向所有客户端发送
				try {
					DataOutputStream dos = new DataOutputStream(c.s
							.getOutputStream());
					dos.writeUTF("name");
					dos.writeUTF(player1Name);
					dos.writeUTF(player2Name);
					dos.writeUTF(player1Sex);
					dos.writeUTF(player2Sex);
				} catch (IOException e) {
					e.printStackTrace();
				}

			}

		}

		public void sendReady(boolean canPlay) {
			for (int n = 0; n < clients.size(); n++) {
				Client c = clients.get(n);// 此处要向各个客户端发送准备信息
				try {
					DataOutputStream dos = new DataOutputStream(c.s
							.getOutputStream());
					dos.writeUTF("ready");
					dos.writeBoolean(canPlay);
					dos.writeBoolean(firstStart);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			firstStart = false;

		}

		public void sendOption(String option) {
			String time=null;
			String oneTime=null;
			try {
				boolean isplayer1 = dis.readBoolean();
				boolean isplayer2 = dis.readBoolean();
				if(option.equals("time")){
					 time=dis.readUTF();
				}
				if(option.equals("oneTime")){
					 oneTime=dis.readUTF();
				}
				if (isplayer1 == true && isplayer2 == false) {
					Client c = clients.get(1);
					DataOutputStream dos = new DataOutputStream(c.s
							.getOutputStream());
					dos.writeUTF(option);
					if(option.equals("time")){
						dos.writeUTF(time);
					}
					if(option.equals("oneTime")){
						dos.writeUTF(oneTime);
					}
				}
				if (isplayer1 == false && isplayer2 == true) {
					Client c = clients.get(0);
					DataOutputStream dos = new DataOutputStream(c.s
							.getOutputStream());
					dos.writeUTF(option);
					if(option.equals("time")){
						dos.writeUTF(time);
					}
					if(option.equals("oneTime")){
						dos.writeUTF(oneTime);
					}
				}
			} catch (IOException e) {
				e.printStackTrace();
			}

		}

	}

}
