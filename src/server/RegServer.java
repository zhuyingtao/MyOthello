package server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.PreparedStatement;

/**
 * ������Ϸ��ע��ķ�����
 * 
 * @author ZhuYingtao
 * 
 */
public class RegServer implements Runnable {
	/**
	 * �������Ķ˿ں�
	 */
	public static final int TCP_PORT = 5002;
	private static final String DBDRIVER = "com.mysql.jdbc.Driver";
	private static final String DBURL = "jdbc:mysql://" + Server.IP
			+ ":3306/game";
	private static final String DBUSER = "root";
	private static final String DBPASSWORD = "zyt";

	/**
	 * �������̣߳��������ݿⲢʵ������,���տͻ��˷��͵���Ϣ�������д���
	 */
	public void run() {
		Connection conn = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;

		ServerSocket ss = null;
		DataInputStream dis = null;
		DataOutputStream dos = null;
		String info = null;// ��������Ϣ
		String sql = "INSERT INTO user(id,name,password,sex) VALUES (?,?,?,?)";
		String sql2 = "SELECT id FROM user WHERE id LIKE ? ";
		String sql3 = "SELECT win,lose,draw,score FROM user WHERE name LIKE ? ";
		String sql4 = "UPDATE user SET win=?,score=? WHERE name LIKE ? ";
		String sql5 = "UPDATE user SET lose=?,score=? WHERE name LIKE ? ";
		String sql6 = "UPDATE user SET draw=? WHERE name LIKE ? ";
		try {
			Class.forName(DBDRIVER); // �������ݿ�����
			// �������ݿ�

			conn = DriverManager.getConnection(DBURL, DBUSER, DBPASSWORD);

			ss = new ServerSocket(TCP_PORT);
			System.out.println("ע����������������ȴ�����,�˿ںţ�" + TCP_PORT);
			while (true) {
				Socket s = ss.accept();
				dis = new DataInputStream(s.getInputStream());
				dos = new DataOutputStream(s.getOutputStream());
				info = dis.readUTF();
				System.out.println(info);
				if (info.equals("register")) {// ���ʺ���Ϣд�����ݿ�
					pstm = conn.prepareStatement(sql);// ʵ��������
					String ID = dis.readUTF();
					String nickname = dis.readUTF();
					String password = dis.readUTF();
					String sex = dis.readUTF();

					pstm.setString(1, ID);
					pstm.setString(2, nickname);
					pstm.setString(3, password);
					pstm.setString(4, sex);
					pstm.executeUpdate();
				}
				if (info.equals("check")) {// ����ʺ��Ƿ����
					pstm = conn.prepareStatement(sql2);// ʵ��������
					String ID = dis.readUTF();
					pstm.setString(1, ID);
					rs = pstm.executeQuery();
					boolean hasID = rs.next();
					dos.writeBoolean(hasID);
                }
				if (info.equals("update")) {// �������ݿ�
					pstm = conn.prepareStatement(sql3);// ʵ��������
					String name = dis.readUTF();
					int score = dis.readInt();
					int winOrLose = dis.readInt();
					pstm.setString(1, name);
					rs = pstm.executeQuery();

					while (rs.next()) {
						int win = rs.getInt(1);
						int lose = rs.getInt(2);
						int draw = rs.getInt(3);
						int totalScore = rs.getInt(4);
						if (winOrLose > 0) {
							win += 1;
							totalScore = totalScore + score;
							pstm = conn.prepareStatement(sql4);
							pstm.setInt(1, win);
							pstm.setInt(2, totalScore);
							pstm.setString(3, name);
						}
						if (winOrLose < 0) {
							lose += 1;
							totalScore = totalScore + score;
							pstm = conn.prepareStatement(sql5);
							pstm.setInt(1, lose);
							pstm.setInt(2, totalScore);
							pstm.setString(3, name);
						}
						if (winOrLose == 0) {
							draw += 1;
							pstm = conn.prepareStatement(sql6);
							pstm.setInt(1, draw);
							pstm.setString(2, name);
						}

					}
					System.out.println(name + " " + score + " " + winOrLose);
					pstm.executeUpdate();
				}
			}
		} catch (ClassNotFoundException e) {
			System.out.println("��������ʧ�ܣ���");
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("���ݿ�����ʧ��");
		} catch (IOException e) {
			System.out.println("�˿�ʹ���У����������ԡ�");
			System.exit(0);
		} finally {
			try {
				if (dis != null) {
					dis.close();
				}
				ss.close();
				pstm.close();
				conn.close();
			} catch (Exception e) {
				System.out.println("������");
			}
		}

	}
}