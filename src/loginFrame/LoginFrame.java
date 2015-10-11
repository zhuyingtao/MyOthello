package loginFrame;

import hallFrame.GameHall;

import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import server.LoginServer;
import server.Server;

/**
 * ��½��Ϸ�Ľ���
 * 
 * @author ZhuYingtao
 * 
 */
@SuppressWarnings("serial")
public class LoginFrame extends JFrame implements ActionListener {
	// ��ȡ��Ļ��С
	private Toolkit tk = Toolkit.getDefaultToolkit();
	/**
	 * ��Ļ�Ŀ��
	 */
	int width = tk.getScreenSize().width;
	/**
	 * ��Ļ�ĸ߶�
	 */
	int height = tk.getScreenSize().height;

	@SuppressWarnings("unused")
	private Register reg = null;

	private JTextField jtf = null;
	private JPasswordField jpf = null;

	private Socket s = null;
	private DataOutputStream dos = null;
	private DataInputStream dis = null;

	/**
	 * ������Ϸ����
	 */
	public LoginFrame() {
		this.setSize(270, 200);
		this.setTitle("����ڰ���");
		this.setLocation(width / 2 - 135, height / 2 - 100);
		this.setResizable(false);

		this.setLayout(new GridLayout(5, 1));// ���ò��ֹ�����
		ImageIcon ico = new ImageIcon("picture\\Login\\008.jpg");// ���ͼƬ
		JLabel img = new JLabel(ico);
		this.add(img);

		JPanel name = new JPanel();
		name.setLayout(new FlowLayout());
		JLabel j1 = new JLabel("�ʺ�:");
		jtf = new JTextField(15);
		name.add(j1);
		name.add(jtf);
		this.add(name);

		JPanel password = new JPanel();
		password.setLayout(new FlowLayout());
		JLabel j2 = new JLabel("����:");
		jpf = new JPasswordField(15);
		jpf.setEchoChar('*');
		password.add(j2);
		password.add(jpf);
		this.add(password);

		JPanel button = new JPanel();
		button.setLayout(new FlowLayout());
		JButton login = new JButton("��¼");
		JButton register = new JButton("ע��");
		JButton cancel = new JButton("ȡ��");
		button.add(login);
		button.add(register);
		button.add(cancel);
		login.addActionListener(this);
		register.addActionListener(this);
		cancel.addActionListener(this);
		this.add(button);

		this.setVisible(true);
	}

	@SuppressWarnings("deprecation")
	/**
	 * ��ť���¼�����
	 */
	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand().equals("ȡ��")) {
			this.setVisible(false);
			dispose();
		}
		if (e.getActionCommand().equals("ע��")) {
			reg = new Register();

		}
		if (e.getActionCommand().equals("��¼")) {
			String ID = jtf.getText();
			String password = jpf.getText();
			try {
				s = new Socket(Server.IP, LoginServer.TCP_PORT);
				dos = new DataOutputStream(s.getOutputStream());
				dis = new DataInputStream(s.getInputStream());
				dos.writeUTF("checkPlayer");
				dos.writeUTF(ID);
				dos.writeUTF(password);

				boolean hasID = dis.readBoolean();
				if (hasID == true) {
					System.out.println("��¼�ɹ�");
					this.setVisible(false);
					dispose();
					new GameHall();
				} else {
					JOptionPane.showMessageDialog(this, "�ʺŻ��������!");
					jtf.setText("");
					jpf.setText("");
				}

			} catch (UnknownHostException e1) {
				e1.printStackTrace();
			} catch (IOException e1) {
				e1.printStackTrace();
			} finally {
				try {
					dis.close();
					dos.close();
					s.close();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		}
	}

	/**
	 * ���е�¼����
	 * @param args
	 */
	public static void main(String[] args) {
		new LoginFrame();
	}

}
