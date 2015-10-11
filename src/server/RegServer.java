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
 * 关于游戏中注册的服务器
 * 
 * @author ZhuYingtao
 * 
 */
public class RegServer implements Runnable {
	/**
	 * 服务器的端口号
	 */
	public static final int TCP_PORT = 5002;
	private static final String DBDRIVER = "com.mysql.jdbc.Driver";
	private static final String DBURL = "jdbc:mysql://" + Server.IP
			+ ":3306/game";
	private static final String DBUSER = "root";
	private static final String DBPASSWORD = "zyt";

	/**
	 * 服务器线程，加载数据库并实现连接,接收客户端发送的信息，并进行处理
	 */
	public void run() {
		Connection conn = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;

		ServerSocket ss = null;
		DataInputStream dis = null;
		DataOutputStream dos = null;
		String info = null;// 定义标记信息
		String sql = "INSERT INTO user(id,name,password,sex) VALUES (?,?,?,?)";
		String sql2 = "SELECT id FROM user WHERE id LIKE ? ";
		String sql3 = "SELECT win,lose,draw,score FROM user WHERE name LIKE ? ";
		String sql4 = "UPDATE user SET win=?,score=? WHERE name LIKE ? ";
		String sql5 = "UPDATE user SET lose=?,score=? WHERE name LIKE ? ";
		String sql6 = "UPDATE user SET draw=? WHERE name LIKE ? ";
		try {
			Class.forName(DBDRIVER); // 加载数据库驱动
			// 连接数据库

			conn = DriverManager.getConnection(DBURL, DBUSER, DBPASSWORD);

			ss = new ServerSocket(TCP_PORT);
			System.out.println("注册服务器已启动，等待连接,端口号：" + TCP_PORT);
			while (true) {
				Socket s = ss.accept();
				dis = new DataInputStream(s.getInputStream());
				dos = new DataOutputStream(s.getOutputStream());
				info = dis.readUTF();
				System.out.println(info);
				if (info.equals("register")) {// 把帐号信息写入数据库
					pstm = conn.prepareStatement(sql);// 实例化对象
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
				if (info.equals("check")) {// 检测帐号是否存在
					pstm = conn.prepareStatement(sql2);// 实例化对象
					String ID = dis.readUTF();
					pstm.setString(1, ID);
					rs = pstm.executeQuery();
					boolean hasID = rs.next();
					dos.writeBoolean(hasID);
                }
				if (info.equals("update")) {// 更新数据库
					pstm = conn.prepareStatement(sql3);// 实例化对象
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
			System.out.println("驱动加载失败！！");
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("数据库连接失败");
		} catch (IOException e) {
			System.out.println("端口使用中，请重新再试。");
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
				System.out.println("出错了");
			}
		}

	}
}