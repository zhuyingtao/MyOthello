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
 * ����ʵ����Ϸ��ע�Ṧ��
 * 
 * @author ZhuYingtao
 * 
 */
@SuppressWarnings("serial")
public class Register extends JFrame implements ActionListener, ItemListener {
	// ��ȡ��Ļ��С
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
	private String sex = "��"; // Ĭ��Ϊ��

	private Socket s = null;
	private DataInputStream dis = null;
	private DataOutputStream dos = null;

	/**
	 * ������ʾ����
	 */
	public Register() {
		this.setSize(350, 350);
		this.setTitle("�û�ע��");
		this.setLocation(width / 2 - 200, height / 2 - 150);
		this.setResizable(false);
		this.setLayout(new GridLayout(7, 1, 0, 0));// ���ò��ֹ�����

		ImageIcon img = new ImageIcon("picture\\Login\\regist.jpg");
		JLabel pic = new JLabel(img);
		this.add(pic);

		JPanel ID = new JPanel();
		ID.setBackground(Color.CYAN);
		ID.setLayout(new FlowLayout(FlowLayout.LEFT));
		JLabel IDLabel = new JLabel("�����ʺ�:");
		IDField = new JTextField(15);
		JButton check = new JButton("�ʺſ���?");
		check.addActionListener(this);
		ID.add(IDLabel);
		ID.add(IDField);
		ID.add(check);
		this.add(ID);

		JPanel nickname = new JPanel();
		nickname.setBackground(Color.CYAN);
		nickname.setLayout(new FlowLayout(FlowLayout.LEFT));
		JLabel nameLabel = new JLabel("�����ǳ�:");
		nameField = new JTextField(15);
		JLabel warnLable3 = new JLabel("(����.��ĸ������)");
		nickname.add(nameLabel);
		nickname.add(nameField);
		nickname.add(warnLable3);
		this.add(nickname);

		JPanel password = new JPanel();
		password.setBackground(Color.CYAN);
		password.setLayout(new FlowLayout(FlowLayout.LEFT));
		JLabel passwordLabel = new JLabel("��������:");
		JLabel warnLabel = new JLabel("(6-12λ)");
		passwordField = new JPasswordField(15);
		passwordField.setEchoChar('*');
		password.add(passwordLabel);
		password.add(passwordField);
		password.add(warnLabel);
		this.add(password);

		JPanel password2 = new JPanel();
		password2.setBackground(Color.CYAN);
		password2.setLayout(new FlowLayout(FlowLayout.LEFT));
		JLabel password2Label = new JLabel("ȷ������:");
		password2Field = new JPasswordField(15);
		password2Field.setEchoChar('*');
		JLabel warnLabel2 = new JLabel("(�ظ����������)");
		password2.add(password2Label);
		password2.add(password2Field);
		password2.add(warnLabel2);
		this.add(password2);

		CheckboxGroup grp = new CheckboxGroup();
		Checkbox maleChoose = new Checkbox("��", true, grp);
		Checkbox femaleChoose = new Checkbox("Ů", false, grp);
		maleChoose.addItemListener(this);
		femaleChoose.addItemListener(this);

		JPanel sex = new JPanel();// choose sex;
		sex.setLayout(new FlowLayout(FlowLayout.LEFT));
		sex.setBackground(Color.CYAN);
		JLabel sexLabel = new JLabel("ѡ���Ա�:");
		sex.add(sexLabel);
		sex.add(maleChoose);
		sex.add(femaleChoose);
		this.add(sex);

		JPanel button = new JPanel();
		button.setLayout(new FlowLayout());
		button.setBackground(Color.GREEN);
		JButton registButton = new JButton("ע��");
		JButton cancel = new JButton("ȡ��");
		registButton.addActionListener(this);
		cancel.addActionListener(this);
		button.add(registButton);
		button.add(cancel);
		this.add(button);

		this.setVisible(true);

	}

	/**
	 * ��Ӷ�ע���һϵ�й��ܵĴ���
	 */
	@SuppressWarnings("deprecation")
	public void actionPerformed(ActionEvent e) {

		if (e.getActionCommand().equals("ע��")) {

			if ((nameField.getText().equals("") || IDField.getText().equals("")
					|| passwordField.getPassword().equals("") || password2Field
					.getPassword().equals(""))) {
				JOptionPane.showMessageDialog(this, "��Ϣ��д���������������д��");
			} else if (passwordField.getText().equals(password2Field.getText()) == false) {
				JOptionPane.showMessageDialog(this, "�������벻һ�£���������д��");
				passwordField.setText("");
				password2Field.setText("");
			} else if (passwordField.getPassword().length < 6
					|| passwordField.getPassword().length > 12) {
				JOptionPane.showMessageDialog(this, "����λ��������Ҫ����������д��");
				passwordField.setText("");
				password2Field.setText("");
			} else {
				try { // ��ȡ����
					s = new Socket(Server.IP, RegServer.TCP_PORT);
					dos = new DataOutputStream(s.getOutputStream());
					dis = new DataInputStream(s.getInputStream());
				} catch (UnknownHostException e1) {
					e1.printStackTrace();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
				// ����ע�����ݵ�������
				String info = "register";// �����Ϣ
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
				JOptionPane.showMessageDialog(this, "       ��ϲ����ע��ɹ���", "��Ϣ",
						JOptionPane.PLAIN_MESSAGE, new ImageIcon(
								"picture\\expression\\0.gif"));
				this.setVisible(false);
				dispose();

			}
		}
		if (e.getActionCommand().equals("ȡ��")) {
			this.setVisible(false);
			dispose();
		}
		boolean hasID = false;
		if (e.getActionCommand().equals("�ʺſ���?")) {

			String info = "check";// �����Ϣ
			String ID = IDField.getText();
			if (ID.length() < 6 || ID.length() > 15) {
				JOptionPane.showMessageDialog(this, "�ʺű���Ϊ6-15λ!");
				IDField.setText("");
			} else {
				try { // ��ȡ����
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
					JOptionPane.showMessageDialog(this, "�ʺſ���!");
				}
				if (hasID == true) {
					JOptionPane.showMessageDialog(this, "�ʺ��Ѵ��ڣ���������д!");
					IDField.setText("");
				}

			}
		}
	}

	/**
	 * ��Ӷ��Ա���ĵĴ���
	 */
	public void itemStateChanged(ItemEvent e) {
		if (e.getSource() == male) {
			sex = "��";
		} else {
			sex = "Ů";
		}

	}

}
