package chessFrame;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

import server.RegServer;
import server.Server;
/**
 * 此类用于更新玩家的信息，如积分，胜负盘数等
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
	 * 连接数据库
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
	 * 向数据库发送更新后的游戏信息
	 * @param winOrLose 胜负
	 * @param score  得分
	 * @param name   昵称
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
