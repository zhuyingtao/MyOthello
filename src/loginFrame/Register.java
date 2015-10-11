package loginFrame;

import java.awt.Checkbox;
import java.awt.CheckboxGroup;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
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

import server.RegServer;
import server.Server;

/**
 * 此类实现游戏的注册功能
 * 
 * @author ZhuYingtao
 * 
 */
@SuppressWarnings("serial")
public class Register extends JFrame implements ActionListener, ItemListener {
	// 获取屏幕大小
	private Toolkit tk = Toolkit.getDefaultToolkit();
	private int width = tk.getScreenSize().width;
	private int height = tk.getScreenSize().height;

	private JTextField IDField;
	private JTextField nameField;
	private JPasswordField passwordField;
	private JPasswordField password2Field;

	private Checkbox male;
	@SuppressWarnings("unused")
	private Checkbox female;
	private String sex = "男"; // 默认为男

	private Socket s = null;
	private DataInputStream dis = null;
	private DataOutputStream dos = null;

	/**
	 * 构造显示界面
	 */
	public Register() {
		this.setSize(350, 350);
		this.setTitle("用户注册");
		this.setLocation(width / 2 - 200, height / 2 - 150);
		this.setResizable(false);
		this.setLayout(new GridLayout(7, 1, 0, 0));// 设置布局管理器

		ImageIcon img = new ImageIcon("picture\\Login\\regist.jpg");
		JLabel pic = new JLabel(img);
		this.add(pic);

		JPanel ID = new JPanel();
		ID.setBackground(Color.CYAN);
		ID.setLayout(new FlowLayout(FlowLayout.LEFT));
		JLabel IDLabel = new JLabel("输入帐号:");
		IDField = new JTextField(15);
		JButton check = new JButton("帐号可用?");
		check.addActionListener(this);
		ID.add(IDLabel);
		ID.add(IDField);
		ID.add(check);
		this.add(ID);

		JPanel nickname = new JPanel();
		nickname.setBackground(Color.CYAN);
		nickname.setLayout(new FlowLayout(FlowLayout.LEFT));
		JLabel nameLabel = new JLabel("输入昵称:");
		nameField = new JTextField(15);
		JLabel warnLable3 = new JLabel("(汉字.字母或数字)");
		nickname.add(nameLabel);
		nickname.add(nameField);
		nickname.add(warnLable3);
		this.add(nickname);

		JPanel password = new JPanel();
		password.setBackground(Color.CYAN);
		password.setLayout(new FlowLayout(FlowLayout.LEFT));
		JLabel passwordLabel = new JLabel("输入密码:");
		JLabel warnLabel = new JLabel("(6-12位)");
		passwordField = new JPasswordField(15);
		passwordField.setEchoChar('*');
		password.add(passwordLabel);
		password.add(passwordField);
		password.add(warnLabel);
		this.add(password);

		JPanel password2 = new JPanel();
		password2.setBackground(Color.CYAN);
		password2.setLayout(new FlowLayout(FlowLayout.LEFT));
		JLabel password2Label = new JLabel("确认密码:");
		password2Field = new JPasswordField(15);
		password2Field.setEchoChar('*');
		JLabel warnLabel2 = new JLabel("(重复输入的密码)");
		password2.add(password2Label);
		password2.add(password2Field);
		password2.add(warnLabel2);
		this.add(password2);

		CheckboxGroup grp = new CheckboxGroup();
		Checkbox maleChoose = new Checkbox("男", true, grp);
		Checkbox femaleChoose = new Checkbox("女", false, grp);
		maleChoose.addItemListener(this);
		femaleChoose.addItemListener(this);

		JPanel sex = new JPanel();// choose sex;
		sex.setLayout(new FlowLayout(FlowLayout.LEFT));
		sex.setBackground(Color.CYAN);
		JLabel sexLabel = new JLabel("选择性别:");
		sex.add(sexLabel);
		sex.add(maleChoose);
		sex.add(femaleChoose);
		this.add(sex);

		JPanel button = new JPanel();
		button.setLayout(new FlowLayout());
		button.setBackground(Color.GREEN);
		JButton registButton = new JButton("注册");
		JButton cancel = new JButton("取消");
		registButton.addActionListener(this);
		cancel.addActionListener(this);
		button.add(registButton);
		button.add(cancel);
		this.add(button);

		this.setVisible(true);

	}

	/**
	 * 添加对注册等一系列功能的处理
	 */
	@SuppressWarnings("deprecation")
	public void actionPerformed(ActionEvent e) {

		if (e.getActionCommand().equals("注册")) {

			if ((nameField.getText().equals("") || IDField.getText().equals("")
					|| passwordField.getPassword().equals("") || password2Field
					.getPassword().equals(""))) {
				JOptionPane.showMessageDialog(this, "信息填写不完整，请继续填写！");
			} else if (passwordField.getText().equals(password2Field.getText()) == false) {
				JOptionPane.showMessageDialog(this, "密码输入不一致，请重新填写！");
				passwordField.setText("");
				password2Field.setText("");
			} else if (passwordField.getPassword().length < 6
					|| passwordField.getPassword().length > 12) {
				JOptionPane.showMessageDialog(this, "密码位数不符合要求，请重新填写！");
				passwordField.setText("");
				password2Field.setText("");
			} else {
				try { // 获取连接
					s = new Socket(Server.IP, RegServer.TCP_PORT);
					dos = new DataOutputStream(s.getOutputStream());
					dis = new DataInputStream(s.getInputStream());
				} catch (UnknownHostException e1) {
					e1.printStackTrace();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
				// 传递注册数据到服务器
				String info = "register";// 标记信息
				String ID = IDField.getText();
				String nickName = nameField.getText();
				String password = passwordField.getText();
				String sex = this.sex;

				try {
					dos.writeUTF(info);
					dos.writeUTF(ID);
					dos.writeUTF(nickName);
					dos.writeUTF(password);
					dos.writeUTF(sex);
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
				JOptionPane.showMessageDialog(this, "       恭喜您，注册成功！", "消息",
						JOptionPane.PLAIN_MESSAGE, new ImageIcon(
								"picture\\expression\\0.gif"));
				this.setVisible(false);
				dispose();

			}
		}
		if (e.getActionCommand().equals("取消")) {
			this.setVisible(false);
			dispose();
		}
		boolean hasID = false;
		if (e.getActionCommand().equals("帐号可用?")) {

			String info = "check";// 标记信息
			String ID = IDField.getText();
			if (ID.length() < 6 || ID.length() > 15) {
				JOptionPane.showMessageDialog(this, "帐号必须为6-15位!");
				IDField.setText("");
			} else {
				try { // 获取连接
					s = new Socket(Server.IP, RegServer.TCP_PORT);
					dos = new DataOutputStream(s.getOutputStream());
					dis = new DataInputStream(s.getInputStream());
				} catch (UnknownHostException e1) {
					e1.printStackTrace();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
				try {
					dos.writeUTF(info);
					dos.writeUTF(ID);
					hasID = dis.readBoolean();
				} catch (IOException e1) {
					e1.printStackTrace();
				}

				if (hasID == false) {
					JOptionPane.showMessageDialog(this, "帐号可用!");
				}
				if (hasID == true) {
					JOptionPane.showMessageDialog(this, "帐号已存在，请重新填写!");
					IDField.setText("");
				}

			}
		}
	}

	/**
	 * 添加对性别更改的处理
	 */
	public void itemStateChanged(ItemEvent e) {
		if (e.getSource() == male) {
			sex = "男";
		} else {
			sex = "女";
		}

	}

}
