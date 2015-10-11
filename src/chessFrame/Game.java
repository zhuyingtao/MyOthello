package chessFrame;

import java.awt.Color;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;

import hallFrame.HallClient;
import hallFrame.Room;
/**
 * 此类用于显示整个游戏窗口
 * @author ZhuYingtao
 *
 */
@SuppressWarnings("serial")
public class Game extends JFrame {
	
	ChessBoard cb = null;
	GameOption go = null;
	ChessClient cc = null;
	private GameChatClient chat = null;
	private ReadyPanel rp = null;
	private NamePanel np = null;
	@SuppressWarnings("unused")
	private HallClient hc = null;
	/**
	 * 进入游戏的桌号
	 */
	public int tableNo = 0;
	/**
	 * 进入游戏的位号
	 */
	public int chairNo = 0;
	/**
	 * 标记是否是观战者
	 */
	boolean isWatcher = false;
    /**
     * 构造游戏界面
     * @param hc HallClient的引用
     * @param tableNo 进入游戏的桌号
     * @param chairNo 进入游戏的位号
     * @param isWatcher 是否是观战者
     */
	public Game(final HallClient hc, final int tableNo, final int chairNo,
			final boolean isWatcher) {
		this.tableNo = tableNo;
		this.chairNo = chairNo;
		this.isWatcher = isWatcher;
		chat = new GameChatClient(this);
		go = new GameOption();
		cc = new ChessClient(this);
		cb = new ChessBoard(this);
		rp = new ReadyPanel(this);
		np = new NamePanel(cc);
		cc.getNamePanel(np);
		cc.getReadyPanel(rp);
		
		this.hc = hc;

		this.setTitle("黑白棋");
		this.setResizable(false);// 设置窗体大小不可变。
		this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);// 设置窗体关闭方式
		this.setSize(780, 570);
		this.setLocation(cb.width / 2 - 390, cb.height / 2 - 285);
		this.setLayout(null);
		this.setBackground(Color.GREEN);
		this.add(chat);
		this.add(rp);
		this.add(np);
		this.add(cb);
		go.getReadyPanel(rp);
		cb.ch.getReadyPanel(rp);

		JMenu game = new JMenu("游戏");
		JMenuItem restart=new JMenuItem("重新开始");
		JMenuItem regret = new JMenuItem("悔棋");
		JMenuItem giveUp = new JMenuItem("认输");
		JMenuItem draw = new JMenuItem("和棋");
		game.add(restart);
		game.add(regret);
		game.add(giveUp);
		game.add(draw);
		go.setGame(restart, regret, giveUp, draw);

		JMenu option = new JMenu("选项");
		JMenuItem cancelsug = new JMenuItem("取消提示");
		JMenuItem addsug = new JMenuItem("增加提示");
		JMenuItem wholeTime = new JMenuItem("设置总用时");
		JMenuItem onestepTime = new JMenuItem("设置倒计时");
		option.add(wholeTime);
		option.add(onestepTime);
		option.add(cancelsug);
		option.add(addsug);
		go.setOption(cancelsug, addsug, wholeTime, onestepTime);

		JMenu doc = new JMenu("文件");
		JMenuItem save = new JMenuItem("存储游戏");
		JMenuItem open = new JMenuItem("读取游戏");
		doc.add(save);
		doc.add(open);
		go.getGameClient(this);
		go.getChessBoard(cb);
		go.setDoc(save, open);

		JMenu help = new JMenu("帮助");
		JMenuItem gameInstructor = new JMenuItem("游戏说明");
		JMenuItem about=new JMenuItem("关于");
		help.add(gameInstructor);
		help.add(about);
		go.setHelp(gameInstructor);

		JMenuBar menu = new JMenuBar();
		menu.add(game);
		menu.add(option);
		menu.add(doc);
		menu.add(help);
		this.setJMenuBar(menu);
		chat.connect();
		cc.connect();
		this.setVisible(true);
		this.addWindowListener(new WindowAdapter() {
			@SuppressWarnings("deprecation")
			public void windowClosing(WindowEvent e) {

				if (cb.canPlay == true && isWatcher == false&&cb.computerPlay==false) {
					JOptionPane.showMessageDialog(cb, "游戏正在进行中，请勿退出！！");
				} else {
					try {
						if (cc.isPlayer1 == true || cc.isPlayer2 == true) {
							hc.dos.writeUTF("exitRoom");
							hc.dos.writeInt(tableNo);
							hc.dos.writeInt(chairNo);
							Room.canEnter = true;

							cc.dos.writeUTF("exit");
							cc.dos.writeBoolean(cc.isPlayer1);
							cc.dos.writeBoolean(cc.isPlayer2);
							cb.blackWin=0;
							cb.whiteWin=0;
						}
						if (cc.isPlayer1 == false && cc.isPlayer2 == false) {
							cc.dos.writeUTF("watcherEnd");
							Room.canEnter = true;
							go.t.suspend();
						}

						setVisible(false);
						dispose();

					} catch (IOException e1) {
						e1.printStackTrace();
					}

				}
			}
		});
	}
}
