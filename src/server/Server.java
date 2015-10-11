package server;

/**
 * ����������
 * @author ZhuYingtao
 *
 */
public class Server {
	/**
	 * ��������IP��ַ
	 */
	public static final String IP = "127.0.0.1";
   /**
    * ���������ַ����� 
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
