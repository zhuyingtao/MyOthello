package hallFrame;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;

import server.HallChatServer;
import server.Server;

/**
 * ����ʵ�ִ����е����칦��
 * 
 * @author ZhuYingtao
 * 
 */
@SuppressWarnings("serial")
public class HallChatClient extends JPanel implements ActionListener {

	private JTextArea ta = new JTextArea("��ӭ�����ڰ�����Ϸ������" + "\n");
	private JTextField tf = new JTextField(17);
	private JScrollPane scr = new JScrollPane(ta);
	/**
	 * �����������������������
	 */
	public DataOutputStream dos = null;
	/**
	 * ���ڶ�ȡ������������������Ϣ
	 */
	public DataInputStream dis = null;

	@SuppressWarnings("unused")
	private GameHall gh = null;

	/**
	 * ��������������
	 * 
	 * @param gh
	 */
	public HallChatClient(GameHall gh) {
		this.gh = gh;

		this.add(scr);
		this.add(tf);
		tf.setBackground(Color.WHITE);
		ta.setLineWrap(true);
		ta.setEditable(false);
		ta.setBackground(Color.CYAN);
		scr.setPreferredSize(new Dimension(200, 250));
		scr.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		scr.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		tf.addActionListener(this);
		/*
		 * gh.addWindowListener(new WindowAdapter() { // ���ù��λ����TextField public
		 * void windowOpened(WindowEvent evt) { tf.requestFocusInWindow(); } });
		 */
		this.setBounds(752, 290, 200, 500);
		this.connect(Server.IP, HallChatServer.TCP_PORT);

	}

	/**
	 * ���Ӵ���������
	 * 
	 * @param IP
	 *            ��������IP��ַ
	 * @param TCP_PORT
	 *            �������Ķ˿ں�
	 */
	public void connect(String IP, int TCP_PORT) {
		Socket s = null;
		try {
			s = new Socket(IP, TCP_PORT);
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
					if (info.equals("enter")) {
						String str = dis.readUTF();
						ta.setText(ta.getText() + str + "\n");
					}
					if (info.equals("talking")) {
						String name = dis.readUTF();
						String str = dis.readUTF();
						ta.setText(ta.getText() + name + ":" + str + "\n");
					}
				}
			} catch (SocketException e) {
				System.out.println("����������˳�����");
				// System.exit(0);
			} catch (IOException e1) {
				e1.printStackTrace();
			}

		}
	}

	/**
	 * �������ֺ�ʵ�ֶ����ֵķ��ͺ���ʾ����
	 */
	public void actionPerformed(ActionEvent e) {
		String str = tf.getText();
		tf.setText("");
		try {
			dos.writeUTF("talking");
			dos.writeUTF(HallPlayerPanel.nickname);
			dos.writeUTF(str);
		} catch (IOException e1) {
			System.out.println("��δ����");
		}

	}

}
