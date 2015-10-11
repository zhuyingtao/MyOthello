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
 * ����ʵ����Ϸ�е����칦��
 * @author ZhuYingtao
 *
 */

@SuppressWarnings("serial")
public class GameChatClient extends JPanel implements ActionListener {
    /**
     * ����������������
     */
	JTextArea ta = new JTextArea("��ӭ�����ڰ�����Ϸ��" + "\n");
	/**
	 * ��Ϸ�еĳ������
	 */
	String[] info = { "��Һ�,�ܸ��˼�����λ", "��㰡,���ȵ��һ���л��", "���ǽ������Ѱ�", "�´������,��Ҫ����",
			"�ټ��ˣ��һ������ҵ�", "�ֶ�����?������ô��ô��" };
	/**
	 * �����������������
	 */
	JComboBox jcb = new JComboBox(info);
	/**
	 * ���������
	 */
	JScrollPane scr = new JScrollPane(ta);
	/**
	 * ��Ϸ�е���������
	 */
	String str;
	private ComboBoxEditor cbe = jcb.getEditor();
	private Voice v = new Voice();
	private DataOutputStream dos = null;
	private DataInputStream dis = null;
	private Game game = null;
    
    /**
     * �������촰��
     * @param game ��Ϸ�������
     */
	public GameChatClient(Game game) {
		this.game = game;
		this.add(scr);
		this.add(jcb);
		jcb.setEditable(true);
		jcb.configureEditor(cbe, "");
		jcb.setFont(new Font("����", Font.PLAIN, 14));// ��������
		jcb.setPreferredSize(new Dimension(195, 30));// ���ô�С
		ta.setLineWrap(true);
		ta.setEditable(false);
		ta.setBackground(Color.CYAN);
		scr.setPreferredSize(new Dimension(195, 250));
		scr.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		scr.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		/*game.addWindowListener(new WindowAdapter() { // ���ù��λ����JCombBox
					public void windowOpened(WindowEvent evt) {
						jcb.requestFocusInWindow();
					}
				});*/
		jcb.addActionListener(this);
		this.setBounds(572, 215, 200, 290);
	}
    /**
     * ���ӷ�����
     */
	public void connect() {
		Socket s = null;
		try {
			s = new Socket(Server.IP, HallServer.game_TCP_PORT + game.tableNo
					+ 100);
			dos = new DataOutputStream(s.getOutputStream());
			dos.writeUTF("enter");
			dos.writeUTF(HallPlayerPanel.nickname + "������!");
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
						if (sex.equals("��")) {
							if (str.equals("��Һ�,�ܸ��˼�����λ")) {
								v.play("1001");
							}
							if (str.equals("��㰡,���ȵ��һ���л��")) {
								v.play("1002");
							}
							if (str.equals("���ǽ������Ѱ�")) {
								v.play("1003");
							}
							if (str.equals("�´������,��Ҫ����")) {
								v.play("1004");
							}
							if (str.equals("�ټ��ˣ��һ������ҵ�")) {
								v.play("1005");
							}
							if (str.equals("�ֶ�����?������ô��ô��")) {
								v.play("1006");
							}
						}
						if (sex.equals("Ů")) {
							if (str.equals("��Һ�,�ܸ��˼�����λ")) {
								v.play("2001");
							}
							if (str.equals("��㰡,���ȵ��һ���л��")) {
								v.play("2002");
							}
							if (str.equals("���ǽ������Ѱ�")) {
								v.play("2003");
							}
							if (str.equals("�´������,��Ҫ����")) {
								v.play("2004");
							}
							if (str.equals("�ټ��ˣ��һ������ҵ�")) {
								v.play("2005");
							}
							if (str.equals("�ֶ�����?������ô��ô��")) {
								v.play("2006");
							}

						}
						if (str != null && str != "") {
							ta.setText(ta.getText() + name + ":" + str + "\n");
						}
					}
				}
			} catch (SocketException e) {
				System.out.println("����������˳�����");
			} catch (IOException e1) {
				e1.printStackTrace();
			}

		}

	}
    /**
     * �������ֺ�ʵ�ֶ����ֵķ��ͺ���ʾ����
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
				System.out.println("��δ����");
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
					System.out.println("��δ����");
				}
			}
		}
	}*/
}
