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
 * 登陆游戏的界面
 * 
 * @author ZhuYingtao
 * 
 */
@SuppressWarnings("serial")
public class LoginFrame extends JFrame implements ActionListener {
	// 获取屏幕大小
	private Toolkit tk = Toolkit.getDefaultToolkit();
	/**
	 * 屏幕的宽度
	 */
	int width = tk.getScreenSize().width;
	/**
	 * 屏幕的高度
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
	 * 构造游戏界面
	 */
	public LoginFrame() {
		this.setSize(270, 200);
		this.setTitle("网络黑白棋");
		this.setLocation(width / 2 - 135, height / 2 - 100);
		this.setResizable(false);

		this.setLayout(new GridLayout(5, 1));// 设置布局管理器
		ImageIcon ico = new ImageIcon("picture\\Login\\008.jpg");// 添加图片
		JLabel img = new JLabel(ico);
		this.add(img);

		JPanel name = new JPanel();
		name.setLayout(new FlowLayout());
		JLabel j1 = new JLabel("帐号:");
		jtf = new JTextField(15);
		name.add(j1);
		name.add(jtf);
		this.add(name);

		JPanel password = new JPanel();
		password.setLayout(new FlowLayout());
		JLabel j2 = new JLabel("密码:");
		jpf = new JPasswordField(15);
		jpf.setEchoChar('*');
		password.add(j2);
		password.add(jpf);
		this.add(password);

		JPanel button = new JPanel();
		button.setLayout(new FlowLayout());
		JButton login = new JButton("登录");
		JButton register = new JButton("注册");
		JButton cancel = new JButton("取消");
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
	 * 按钮的事件处理
	 */
	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand().equals("取消")) {
			this.setVisible(false);
			dispose();
		}
		if (e.getActionCommand().equals("注册")) {
			reg = new Register();

		}
		if (e.getActionCommand().equals("登录")) {
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
					System.out.println("登录成功");
					this.setVisible(false);
					dispose();
					new GameHall();
				} else {
					JOptionPane.showMessageDialog(this, "帐号或密码错误!");
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
	 * 运行登录界面
	 * @param args
	 */
	public static void main(String[] args) {
		new LoginFrame();
	}

}
