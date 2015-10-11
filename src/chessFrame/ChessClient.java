package chessFrame;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import javax.swing.JOptionPane;

import server.HallServer;
import server.Server;
import hallFrame.HallPlayerPanel;
import hallFrame.Room;
/**
 * ����������������������������ĸ������ݲ�����
 * @author ZhuYingtao
 *
 */
public class ChessClient {
	private Socket s = null;
	/**
	 * ������������˷�������
	 */
	DataOutputStream dos = null;
	/**
	 * ���ڶ�ȡ���յ�������
	 */
	DataInputStream dis = null;
    /**
     * ����Ƿ������1
     */
	boolean isPlayer1 = false;
	/**
	 * ����Ƿ������2
	 */
	boolean isPlayer2 = false;
	/**
	 * ������1�Ƿ���룬������Ϸ����ʾ
	 */
	boolean player1Enter = false;
	/**
	 * ������2�Ƿ���룬������Ϸ����ʾ
	 */
	boolean player2Enter = false;
	
	private ChessBoard cb = null;
	@SuppressWarnings("unused")
	private Chess ch = null;
	private GameOption go = null;
	private Game game = null;
	private NamePanel np = null;
	private PlayerPanel pp=null;
	private ReadyPanel rp=null;
    /**
     * �õ�Game���ʵ��������
     * @param game
     */
	public ChessClient(Game game) {
		this.game = game;
	}
    /**
     * �õ�ChessBoard��Ķ���
     * @param cb
     */
	public void getChessBoard(ChessBoard cb) {
		this.cb = cb;
		this.ch = cb.ch;
		this.go = cb.go;
		this.pp=cb.pp;
	}
    /**
     * �õ�NamePanel��Ķ���
     * @param np
     */
	public void getNamePanel(NamePanel np) {
		this.np = np;
	}
	/**
	 * �õ�ReadyPanel��Ķ���
	 * @param rp
	 */
	public void getReadyPanel(ReadyPanel rp){
		this.rp=rp;
	}
    /**
     * ���ӷ�����
     */
	public void connect() {
		try {
			s = new Socket(Server.IP, HallServer.game_TCP_PORT + game.tableNo);
			dos = new DataOutputStream(s.getOutputStream());
			dis = new DataInputStream(s.getInputStream());
			dos.writeUTF("allPlayerName");
			dos.writeUTF(HallPlayerPanel.nickname);
			dos.writeInt(HallPlayerPanel.score);
			dos.writeUTF(HallPlayerPanel.level);
			new Thread(new Client()).start();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
    
	class Client implements Runnable {
		String info;
		boolean firstStart;//�����Ƿ��һ��������ʱ�߳�
        
		public void run() {
			try {
				while (true) {

					info = dis.readUTF();
					System.out.println(info);
					if (info.equals("isPlayer1")) {
						isPlayer1 = true;
						isPlayer2 = false;
						dos.writeUTF("player1Name");
						dos.writeUTF(HallPlayerPanel.nickname);
						dos.writeUTF(HallPlayerPanel.sex);
					}
					if (info.equals("isPlayer2")) {
						isPlayer2 = true;
						isPlayer1 = false;
						dos.writeUTF("player2Name");
						dos.writeUTF(HallPlayerPanel.nickname);
						dos.writeUTF(HallPlayerPanel.sex);
					}
					if (info.equals("player1Enter")) {
						player1Enter = true;

					}
					if (info.equals("player2Enter")) {
						player2Enter = true;
					}
					if(info.equals("player1played")){
						cb.isPlayer1Played=true;
					}
					if (info.equals("watcher")) {
						isPlayer1 = false;
						isPlayer2 = false;
						dos.writeUTF("getPlayersName");
					}
					if (info.equals("name")) {
						pp.player1Name = dis.readUTF();
						pp.player2Name = dis.readUTF();
						pp.player1Sex=dis.readUTF();
						pp.player2Sex=dis.readUTF();
					}
					if (info.equals("allPlayerName")) {
						np.read(dis);
					}
					if (info.equals("player1Exit")) {
						player1Enter = false;
						cb.canPlay = dis.readBoolean();
						if (cb.isWatcher == true) {
							dos.writeUTF("watcherEnd");
							JOptionPane.showMessageDialog(cb, "������˳�����ս����!");
							Room.canEnter = true;
							game.setVisible(false);
							game.dispose();
						}
					}
					if (info.equals("player2Exit")) {
						player2Enter = false;
						cb.canPlay = dis.readBoolean();
						if (cb.isWatcher == true) {
							dos.writeUTF("watcherEnd");
							JOptionPane.showMessageDialog(cb, "������˳�����ս����!");
							Room.canEnter = true;
							game.setVisible(false);
							game.dispose();
						}
					}
					if (info.equals("ready")) {
						System.out.println("ready received");
						cb.canPlay = dis.readBoolean();
						firstStart=dis.readBoolean();
						if (cb.canPlay == true) {
							cb.initChessBoard();// ��ʼʱ��ʼ����Ϸ
							cb.ch.oneCheck=true;
						    if(firstStart==true){
						    	go.t.start();
						    }  //������ʱ�߳�
						}
					}
					if (info.equals("chess")) {
						cb.read(dis);
						boolean iswin=cb.ch.isWin();
						if(iswin==true){
						   dos.writeUTF("win");
						}
					}
					if(info.equals("giveUp")){
					    go.giveUpAnswer(dis);
					}
					if (info.equals("regret")) {
						int answer = JOptionPane.showConfirmDialog(cb,
								"�Է�Ҫ����壬�Ƿ�ͬ��?");
						dos.writeUTF("answer");
						dos.writeBoolean(isPlayer1);
						dos.writeBoolean(isPlayer2);
						dos.writeUTF("regret");
						dos.writeInt(answer);
					}
					if (info.equals("draw")) {
						int answer = JOptionPane.showConfirmDialog(cb,
								"�Է���ͣ��Ƿ�ͬ��?");
						if(answer==0){
							rp.ready.setText("׼��");
							cb.su.update(0, 0, HallPlayerPanel.nickname);
						}
						dos.writeUTF("answer");
						dos.writeBoolean(isPlayer1);
						dos.writeBoolean(isPlayer2);
						dos.writeUTF("draw");
						dos.writeInt(answer);
					}
					if (info.equals("open")) {
						int answer = JOptionPane.showConfirmDialog(cb,
								"�Է�Ҫ������ϴ���Ϸ���Ƿ�ͬ��?");
						dos.writeUTF("answer");
						dos.writeBoolean(isPlayer1);
						dos.writeBoolean(isPlayer2);
						dos.writeUTF("open");
						dos.writeInt(answer);
					}
					if(info.equals("restart")){
						int answer = JOptionPane.showConfirmDialog(cb,
						"�Է�Ҫ�����¿�ʼ��Ϸ���Ƿ�ͬ��?");
						dos.writeUTF("answer");
						dos.writeBoolean(isPlayer1);
						dos.writeBoolean(isPlayer2);
						dos.writeUTF("restart");
						dos.writeInt(answer);
					}
					if(info.equals("time")){
						String time=dis.readUTF();
						int answer = JOptionPane.showConfirmDialog(cb,
						"�Է�Ҫ����������Ϸʱ��Ϊ"+time+"���ӣ��Ƿ�ͬ��?");
						if(answer==0){
							go.maxtime=Integer.parseInt(time)*60;
							go.blacktime=go.maxtime;
							go.whitetime=go.maxtime;
							go.blackMessage=go.initTime(go.blacktime);
							go.whiteMessage=go.initTime(go.whitetime);
							cb.repaint();
						}
						dos.writeUTF("answer");
						dos.writeBoolean(isPlayer1);
						dos.writeBoolean(isPlayer2);
						dos.writeUTF("time");
						dos.writeInt(answer);
					}
					if(info.equals("oneTime")){
						String oneTime=dis.readUTF();
						int answer = JOptionPane.showConfirmDialog(cb,
						"�Է�Ҫ�����õ�����Ϸʱ��Ϊ"+oneTime+"�룬�Ƿ�ͬ��?");
						if(answer==0){
							go.maxOneStepTime=Integer.parseInt(oneTime);
							go.blackOneTime=go.maxOneStepTime;
							go.whiteOneTime=go.maxOneStepTime;
							cb.repaint();
						}
						dos.writeUTF("answer");
						dos.writeBoolean(isPlayer1);
						dos.writeBoolean(isPlayer2);
						dos.writeUTF("oneTime");
						dos.writeInt(answer);
					}
					if (info.equals("answer")) {
						String info2 = dis.readUTF();
						if (info2.equals("regret")) {
							go.read(dis);
							go.regretAnswer();
							cb.send(dos);
							cb.isOpposite=false;
						}
						if (info2.equals("draw")) {
							go.read(dis);
							go.drawAnswer();
							cb.send(dos);
						}
						if(info2.equals("open")){
							go.read(dis);
							go.openAnswer();
							cb.send(dos);
						}
						if(info2.equals("restart")){
							cb.initChessBoard();
							cb.send(dos);
						}
						if(info2.equals("time")){
							go.read(dis);
							go.timeAnswer();
						}
						if(info2.equals("oneTime")){
							go.read(dis);
							go.oneTimeAnswer();
						}
					}
				}

			} catch (SocketException e) {
				e.printStackTrace();
			} catch (IOException e1) {
				e1.printStackTrace();
			}

		}

	}
	public void update(int score){
		try {
			dos.writeUTF("update");
			dos.writeBoolean(isPlayer1);
			dos.writeBoolean(isPlayer2);
			dos.writeInt(score);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
