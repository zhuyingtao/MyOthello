package server;

/**
 * 启动服务器
 * @author ZhuYingtao
 *
 */
public class Server {
	/**
	 * 服务器的IP地址
	 */
	public static final String IP = "127.0.0.1";
   /**
    * 启动各个分服务器 
    * @param args
    */
	public Server(){
		new Thread(new LoginServer()).start();
		new Thread(new RegServer()).start();
		new Thread(new HallServer()).start();
		new Thread(new HallChatServer()).start();
	}
	public static void main(String[] args) {
		new Server();
	}
}
