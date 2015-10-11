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
 * ����������ʾ������Ϸ����
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
	 * ������Ϸ������
	 */
	public int tableNo = 0;
	/**
	 * ������Ϸ��λ��
	 */
	public int chairNo = 0;
	/**
	 * ����Ƿ��ǹ�ս��
	 */
	boolean isWatcher = false;
    /**
     * ������Ϸ����
     * @param hc HallClient������
     * @param tableNo ������Ϸ������
     * @param chairNo ������Ϸ��λ��
     * @param isWatcher �Ƿ��ǹ�ս��
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

		this.setTitle("�ڰ���");
		this.setResizable(false);// ���ô����С���ɱ䡣
		this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);// ���ô���رշ�ʽ
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

		JMenu game = new JMenu("��Ϸ");
		JMenuItem restart=new JMenuItem("���¿�ʼ");
		JMenuItem regret = new JMenuItem("����");
		JMenuItem giveUp = new JMenuItem("����");
		JMenuItem draw = new JMenuItem("����");
		game.add(restart);
		game.add(regret);
		game.add(giveUp);
		game.add(draw);
		go.setGame(restart, regret, giveUp, draw);

		JMenu option = new JMenu("ѡ��");
		JMenuItem cancelsug = new JMenuItem("ȡ����ʾ");
		JMenuItem addsug = new JMenuItem("������ʾ");
		JMenuItem wholeTime = new JMenuItem("��������ʱ");
		JMenuItem onestepTime = new JMenuItem("���õ���ʱ");
		option.add(wholeTime);
		option.add(onestepTime);
		option.add(cancelsug);
		option.add(addsug);
		go.setOption(cancelsug, addsug, wholeTime, onestepTime);

		JMenu doc = new JMenu("�ļ�");
		JMenuItem save = new JMenuItem("�洢��Ϸ");
		JMenuItem open = new JMenuItem("��ȡ��Ϸ");
		doc.add(save);
		doc.add(open);
		go.getGameClient(this);
		go.getChessBoard(cb);
		go.setDoc(save, open);

		JMenu help = new JMenu("����");
		JMenuItem gameInstructor = new JMenuItem("��Ϸ˵��");
		JMenuItem about=new JMenuItem("����");
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
					JOptionPane.showMessageDialog(cb, "��Ϸ���ڽ����У������˳�����");
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
