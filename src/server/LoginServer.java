package server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
/**
 * ������Ϸ�е�¼�ķ�����
 * @author ZhuYingtao
 *
 */

public class LoginServer implements Runnable {
	/**
	 * �������Ķ˿ں�
	 */
	public static final int TCP_PORT = 5001;
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
		String sql = "SELECT id,password,name ,win,lose,draw,score,sex FROM user "
				+ "WHERE id LIKE ? AND password LIKE ?";
		boolean hasID = false;// �����Ƿ��д��ʺ�
		ServerSocket ss = null;
		DataInputStream dis = null;
		DataOutputStream dos = null;

		String info = null;// �����Ϣ

		String nickname = null;// ����ǳ�
		String sex=null;
		int win=0;
		int lose=0;
		int draw=0;
		int score=0;

		try {
			Class.forName(DBDRIVER); // �������ݿ�����
			// �������ݿ�
			conn = DriverManager.getConnection(DBURL, DBUSER, DBPASSWORD);
			pstm = conn.prepareStatement(sql);

			ss = new ServerSocket(TCP_PORT);
			System.out.println("�����������������ȴ�����,�˿ںţ�" + TCP_PORT);
			while (true) {
				Socket s = ss.accept();
				dis = new DataInputStream(s.getInputStream());
				dos = new DataOutputStream(s.getOutputStream());
				info = dis.readUTF();
				if (info.equals("checkPlayer")) {
					String ID = dis.readUTF();
					String password = dis.readUTF();
					pstm.setString(1, ID);
					pstm.setString(2, password);
					rs = pstm.executeQuery();
					hasID = rs.next();
					dos.writeBoolean(hasID);
                    while (hasID) {
						nickname = rs.getString(3);
						win=rs.getInt(4);
						lose=rs.getInt(5);
						draw=rs.getInt(6);
						score=rs.getInt(7);
						sex=rs.getString(8);
						hasID=false;
					}// �õ������Ϣ
				}
				if (info.equals("getPlayer")) {
					dos.writeUTF(nickname);// ���������Ϣ����Ϸ����
					dos.writeUTF(sex);
					dos.writeInt(win);
					dos.writeInt(lose);
					dos.writeInt(draw);
					dos.writeInt(score);
				}
			}
		} catch (ClassNotFoundException e) {
			System.out.println("��������ʧ�ܣ���");

		} catch (SQLException e) {
			System.out.println("���ݿ�����ʧ��");
			e.printStackTrace();
		} catch (IOException e) {
			System.out.println("�˿�ʹ���У����������ԡ�");
			System.exit(0);
		} finally {
			try {
				if (dis != null) {
					dis.close();
				}
				if (dos != null) {
					dos.close();
				}
				ss.close();
				rs.close();
				pstm.close();
				conn.close();
			} catch (Exception e) {
				System.out.println("������");
			}
		}
	}
}
