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
 * 关于游戏中登录的服务器
 * @author ZhuYingtao
 *
 */

public class LoginServer implements Runnable {
	/**
	 * 服务器的端口号
	 */
	public static final int TCP_PORT = 5001;
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
		String sql = "SELECT id,password,name ,win,lose,draw,score,sex FROM user "
				+ "WHERE id LIKE ? AND password LIKE ?";
		boolean hasID = false;// 定义是否有此帐号
		ServerSocket ss = null;
		DataInputStream dis = null;
		DataOutputStream dos = null;

		String info = null;// 标记信息

		String nickname = null;// 玩家昵称
		String sex=null;
		int win=0;
		int lose=0;
		int draw=0;
		int score=0;

		try {
			Class.forName(DBDRIVER); // 加载数据库驱动
			// 连接数据库
			conn = DriverManager.getConnection(DBURL, DBUSER, DBPASSWORD);
			pstm = conn.prepareStatement(sql);

			ss = new ServerSocket(TCP_PORT);
			System.out.println("检测服务器已启动，等待连接,端口号：" + TCP_PORT);
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
					}// 得到玩家信息
				}
				if (info.equals("getPlayer")) {
					dos.writeUTF(nickname);// 发送玩家信息到游戏大厅
					dos.writeUTF(sex);
					dos.writeInt(win);
					dos.writeInt(lose);
					dos.writeInt(draw);
					dos.writeInt(score);
				}
			}
		} catch (ClassNotFoundException e) {
			System.out.println("驱动加载失败！！");

		} catch (SQLException e) {
			System.out.println("数据库连接失败");
			e.printStackTrace();
		} catch (IOException e) {
			System.out.println("端口使用中，请重新再试。");
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
				System.out.println("出错了");
			}
		}
	}
}
