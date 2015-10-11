package hallFrame;

import java.awt.Color;
import java.awt.Font;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.border.LineBorder;

import server.LoginServer;
import server.Server;
/**
 * 此类用于在大厅显示玩家的个人信息
 * @author ZhuYingtao
 *
 */
@SuppressWarnings("serial")
public class HallPlayerPanel extends JPanel {
	private JTextArea ta = new JTextArea();
    private Socket s = null;
	private DataOutputStream dos = null;
	private DataInputStream dis = null;
	/**
	 * 玩家的昵称 
	 */
	public static String nickname=null;
	/**
	 * 玩家的性别
	 */
	public static String sex=null;
	/**
	 * 玩家的胜局数
	 */
	public static int win=0;
	/**
	 * 玩家的负局数
	 */
	public static int lose=0;
	/**
	 * 玩家的平局数
	 */
	public static int draw=0;
	/**
	 * 玩家的游戏积分
	 */
	public static int score=0;
	/**
	 * 玩家的游戏等级
	 */
	public static String level=null;
    /**
     * 构造显示界面
     */
	public HallPlayerPanel() {
		this.setBorder(new LineBorder(Color.GRAY, 1));
		this.setBackground(Color.GREEN);
		this.setLayout(null);
		
		JLabel string=new JLabel("  ◎◎◎◎玩家信息◎◎◎◎");
		string.setForeground(Color.RED);
		string.setFont(new Font("宋体",Font.PLAIN,14));
		string.setBounds(5, 0, 200, 35);
		this.add(string);
		
		ImageIcon img=new ImageIcon("picture\\GameHall\\face2.jpg");
		JLabel face=new JLabel(img);
		face.setBounds(5, 50, 49, 49);
		this.add(face);
		
		ta.setEditable(false);
		ta.setBackground(Color.GREEN);
		ta.setBounds(68, 37, 120,90 );
		this.add(ta);
		
		this.setBounds(751, 1, 200, 144);
		this.getPlayerInfo();
	}
    /**
     * 连接登录服务器，以得到玩家的具体信息
     */
	public void getPlayerInfo() {
		try {
			s = new Socket(Server.IP, LoginServer.TCP_PORT);
			dos = new DataOutputStream(s.getOutputStream());
			dis = new DataInputStream(s.getInputStream());
			dos.writeUTF("getPlayer");// 发送标记信息

			nickname = dis.readUTF();
			sex=dis.readUTF();
			win=dis.readInt();
			lose=dis.readInt();
			draw=dis.readInt();
			score=dis.readInt();
			if(score>=0&&score<100){
				level="初出茅庐";
			}
			if(score>=100&&score<200){
				level="江湖小虾";
			}
			if(score>=200&&score<300){
				level="明日之星";
			}
			if(score>=300&&score<400){
				level="绝世奇才";
			}
			if(score>=400&&score<500){
				level="盖世棋王";
			}
			if(score>=500&&score<1000){
				level="你无敌了";
			}
			if(score>=-100&&score<0){
				level="1级";
			}
			if(score>=-200&&score<-100){
				level="2级";
			}
			if(score>=-300&&score<-200){
				level="3级";
			}
			
			ta.setText(ta.getText() + "昵称：   "+nickname+ "\n"+
					"胜："+win+"    "+"负："+lose+"    "+"平："+draw+"    "+"\n"
					+"分数：   "+score+"    "+"\n"+"级别：   "+level);
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (dis != null) {
					dis.close();
				}
				if (dos != null) {
					dos.close();
				}
				s.close();
			} catch (IOException e) {
				e.printStackTrace();
			}

		}
	}

}
