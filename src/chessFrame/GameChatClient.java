package chessFrame;

import hallFrame.HallPlayerPanel;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;

import javax.swing.ComboBoxEditor;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ScrollPaneConstants;

import server.HallServer;
import server.Server;
/**
 * 用于实现游戏中的聊天功能
 * @author ZhuYingtao
 *
 */

@SuppressWarnings("serial")
public class GameChatClient extends JPanel implements ActionListener {
    /**
     * 用于输出聊天的内容
     */
	JTextArea ta = new JTextArea("欢迎来到黑白棋游戏！" + "\n");
	/**
	 * 游戏中的常用语句
	 */
	String[] info = { "大家好,很高兴见到各位", "快点啊,都等的我花都谢了", "我们交个朋友吧", "下次再玩吧,我要走了",
			"再见了，我会想念大家的", "又断线了?网络怎么这么差" };
	/**
	 * 用于输入聊天的内容
	 */
	JComboBox jcb = new JComboBox(info);
	/**
	 * 加入滚动条
	 */
	JScrollPane scr = new JScrollPane(ta);
	/**
	 * 游戏中的聊天内容
	 */
	String str;
	private ComboBoxEditor cbe = jcb.getEditor();
	private Voice v = new Voice();
	private DataOutputStream dos = null;
	private DataInputStream dis = null;
	private Game game = null;
    
    /**
     * 构造聊天窗口
     * @param game 游戏类的引用
     */
	public GameChatClient(Game game) {
		this.game = game;
		this.add(scr);
		this.add(jcb);
		jcb.setEditable(true);
		jcb.configureEditor(cbe, "");
		jcb.setFont(new Font("宋体", Font.PLAIN, 14));// 设置字体
		jcb.setPreferredSize(new Dimension(195, 30));// 设置大小
		ta.setLineWrap(true);
		ta.setEditable(false);
		ta.setBackground(Color.CYAN);
		scr.setPreferredSize(new Dimension(195, 250));
		scr.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		scr.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		/*game.addWindowListener(new WindowAdapter() { // 设置光标位置在JCombBox
					public void windowOpened(WindowEvent evt) {
						jcb.requestFocusInWindow();
					}
				});*/
		jcb.addActionListener(this);
		this.setBounds(572, 215, 200, 290);
	}
    /**
     * 连接服务器
     */
	public void connect() {
		Socket s = null;
		try {
			s = new Socket(Server.IP, HallServer.game_TCP_PORT + game.tableNo
					+ 100);
			dos = new DataOutputStream(s.getOutputStream());
			dos.writeUTF("enter");
			dos.writeUTF(HallPlayerPanel.nickname + "进入了!");
			dis = new DataInputStream(s.getInputStream());
			new Thread(new Client()).start();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
    
	class Client implements Runnable {
		String info = null;

		public void run() {
			try {
				while (true) {
					info = dis.readUTF();
					System.out.println(info);
					if (info.equals("enter")) {
						String str = dis.readUTF();
						ta.setText(ta.getText() + str + "\n");
					}
					if (info.equals("talking")) {
						String name = dis.readUTF();
						String str = dis.readUTF();
						String sex = dis.readUTF();
						if (sex.equals("男")) {
							if (str.equals("大家好,很高兴见到各位")) {
								v.play("1001");
							}
							if (str.equals("快点啊,都等的我花都谢了")) {
								v.play("1002");
							}
							if (str.equals("我们交个朋友吧")) {
								v.play("1003");
							}
							if (str.equals("下次再玩吧,我要走了")) {
								v.play("1004");
							}
							if (str.equals("再见了，我会想念大家的")) {
								v.play("1005");
							}
							if (str.equals("又断线了?网络怎么这么差")) {
								v.play("1006");
							}
						}
						if (sex.equals("女")) {
							if (str.equals("大家好,很高兴见到各位")) {
								v.play("2001");
							}
							if (str.equals("快点啊,都等的我花都谢了")) {
								v.play("2002");
							}
							if (str.equals("我们交个朋友吧")) {
								v.play("2003");
							}
							if (str.equals("下次再玩吧,我要走了")) {
								v.play("2004");
							}
							if (str.equals("再见了，我会想念大家的")) {
								v.play("2005");
							}
							if (str.equals("又断线了?网络怎么这么差")) {
								v.play("2006");
							}

						}
						if (str != null && str != "") {
							ta.setText(ta.getText() + name + ":" + str + "\n");
						}
					}
				}
			} catch (SocketException e) {
				System.out.println("聊天对象已退出！！");
			} catch (IOException e1) {
				e1.printStackTrace();
			}

		}

	}
    /**
     * 输入文字后，实现对文字的发送和显示功能
     */
	public void actionPerformed(ActionEvent e) {
		String str = (String) jcb.getSelectedItem();
		jcb.setSelectedItem("");
		System.out.println(str+"aa");
		if (str != null && str != "") {
			try {
				dos.writeUTF("talking");
				dos.writeUTF(HallPlayerPanel.nickname);
				dos.writeUTF(str);
				dos.writeUTF(HallPlayerPanel.sex);
			} catch (IOException e1) {
				System.out.println("尚未连接");
			}
		}

	}

	/*public void keyPressed(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_ENTER) {
			if (e.getSource() == jcb) {
				String str = (String) jcb.getSelectedItem();
				jcb.setSelectedItem("");
				System.out.println(str);
				try {
					dos.writeUTF("talking");
					dos.writeUTF(HallPlayerPanel.nickname);
					dos.writeUTF(str);
				} catch (IOException e1) {
					System.out.println("尚未连接");
				}
			}
		}
	}*/
}
