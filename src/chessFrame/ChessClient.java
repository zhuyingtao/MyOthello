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
 * 此类用来接收下棋服务器发送来的各种数据并处理
 * @author ZhuYingtao
 *
 */
public class ChessClient {
	private Socket s = null;
	/**
	 * 用于向服务器端发送数据
	 */
	DataOutputStream dos = null;
	/**
	 * 用于读取接收到的数据
	 */
	DataInputStream dis = null;
    /**
     * 标记是否是玩家1
     */
	boolean isPlayer1 = false;
	/**
	 * 标记是否是玩家2
	 */
	boolean isPlayer2 = false;
	/**
	 * 标记玩家1是否进入，以在游戏中显示
	 */
	boolean player1Enter = false;
	/**
	 * 标记玩家2是否进入，以在游戏中显示
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
     * 得到Game类的实例化对象
     * @param game
     */
	public ChessClient(Game game) {
		this.game = game;
	}
    /**
     * 得到ChessBoard类的对象
     * @param cb
     */
	public void getChessBoard(ChessBoard cb) {
		this.cb = cb;
		this.ch = cb.ch;
		this.go = cb.go;
		this.pp=cb.pp;
	}
    /**
     * 得到NamePanel类的对象
     * @param np
     */
	public void getNamePanel(NamePanel np) {
		this.np = np;
	}
	/**
	 * 得到ReadyPanel类的对象
	 * @param rp
	 */
	public void getReadyPanel(ReadyPanel rp){
		this.rp=rp;
	}
    /**
     * 连接服务器
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
		boolean firstStart;//定义是否第一次启动计时线程
        
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
							JOptionPane.showMessageDialog(cb, "玩家已退出，观战结束!");
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
							JOptionPane.showMessageDialog(cb, "玩家已退出，观战结束!");
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
							cb.initChessBoard();// 开始时初始化游戏
							cb.ch.oneCheck=true;
						    if(firstStart==true){
						    	go.t.start();
						    }  //启动计时线程
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
								"对方要求悔棋，是否同意?");
						dos.writeUTF("answer");
						dos.writeBoolean(isPlayer1);
						dos.writeBoolean(isPlayer2);
						dos.writeUTF("regret");
						dos.writeInt(answer);
					}
					if (info.equals("draw")) {
						int answer = JOptionPane.showConfirmDialog(cb,
								"对方求和，是否同意?");
						if(answer==0){
							rp.ready.setText("准备");
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
								"对方要求继续上次游戏，是否同意?");
						dos.writeUTF("answer");
						dos.writeBoolean(isPlayer1);
						dos.writeBoolean(isPlayer2);
						dos.writeUTF("open");
						dos.writeInt(answer);
					}
					if(info.equals("restart")){
						int answer = JOptionPane.showConfirmDialog(cb,
						"对方要求重新开始游戏，是否同意?");
						dos.writeUTF("answer");
						dos.writeBoolean(isPlayer1);
						dos.writeBoolean(isPlayer2);
						dos.writeUTF("restart");
						dos.writeInt(answer);
					}
					if(info.equals("time")){
						String time=dis.readUTF();
						int answer = JOptionPane.showConfirmDialog(cb,
						"对方要求设置总游戏时间为"+time+"分钟，是否同意?");
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
						"对方要求设置单步游戏时间为"+oneTime+"秒，是否同意?");
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
