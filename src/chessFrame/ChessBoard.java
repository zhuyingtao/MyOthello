package chessFrame;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import javax.swing.JOptionPane;
import javax.swing.JPanel;

/**
 * 实现棋盘的显示功能
 * 
 * @author ZhuYingtao
 * 
 */
@SuppressWarnings("serial")
public class ChessBoard extends JPanel implements MouseListener {
	private Toolkit tk = Toolkit.getDefaultToolkit();
	/**
	 * 获取屏幕的宽度
	 */
	int width = tk.getScreenSize().width;
	/**
	 * 获取屏幕的高度
	 */
	int height = tk.getScreenSize().height;
	/**
	 * 存储游戏中所需要的图像
	 */
	Image[] images = null;

	private int x, y;// 标识坐标
	/**
	 * 表示所点的点在数组中所在的行数
	 */
	int m;
	/**
	 * 标识所点的点在数组中所在的列数
	 */
	int n;

	private final int BLACK = 1;

	private final int WHITE = 2;
	/**
	 * 显示游戏的信息
	 */
	String message = "黑方先行";
	/**
	 * 标识是否与电脑下棋
	 */
	boolean computerPlay = false;
	/**
	 * 标识进入的人是否是观战者
	 */
	boolean isWatcher = false;
	/**
	 * 标识是否停步
	 */
	boolean isStop = false;
	/**
	 * 标识玩家1是否已下
	 */
	boolean isPlayer1Played = false;
	/**
	 * 标识是否轮到黑方下
	 */
	boolean isBlack = true;
	/**
	 * 标识能否继续游戏
	 */
	boolean canPlay = false;
	/**
	 * 标识是否改变字体颜色
	 */
	boolean changeColor = false;
	/**
	 * 标识是否由对方下棋
	 */
	boolean isOpposite = false;
	/**
	 * 存储已下的棋子
	 */
	int[][] allChess = new int[8][8];
	/**
	 * 悔棋时记录上一步所走的棋子
	 */
	int[][] regChess = new int[8][8];
	/**
	 * 记录黑方赢的盘数
	 */
	int blackWin = 0;
	/**
	 * 记录白方赢的盘数
	 */
	int whiteWin = 0;
	/**
	 * 记录黑方单局的分数
	 */
	int blackScore = 0;
	/**
	 * 记录白方单局的分数
	 */
	int whiteScore = 0;
	/**
	 * 缓冲图片
	 */
	Image bi = null;
	/**
	 * Chess类的引用
	 */
	Chess ch = null;
	/**
	 * PlayerPanel类的引用
	 */
	PlayerPanel pp = null;
	/**
	 * ChessClient类的引用
	 */
	ChessClient cc = null;
	/**
	 * GameOption类的引用
	 */
	GameOption go = null;
	/**
	 * ScoreUpdate类的引用
	 */
	ScoreUpdate su = null;
	private Voice voice = new Voice();

	/**
	 * 构造棋盘界面
	 * 
	 * @param game
	 *            Game类的引用
	 */
	public ChessBoard(Game game) {
		this.isWatcher = game.isWatcher;
		ch = new Chess(this);
		pp = new PlayerPanel(this);
		cc = game.cc;
		go = game.go;
		su = new ScoreUpdate(this);
		cc.getChessBoard(this);
		go.getChessBoard(this);

		
		this.addMouseListener(this);

		// 引入图片
		images = new Image[] { tk.getImage("picture/chess/BackGround.png"),
				tk.getImage("picture/chess/BlackChess.jpg"),
				tk.getImage("picture/chess/WhiteChess.jpg"),
				tk.getImage("picture/chess/Border.jpg"),
				tk.getImage("picture/chess/Smile.jpg"),
				tk.getImage("picture/playerPanel/man.jpg"),
				tk.getImage("picture/chess/BlackChess2.jpg"),
				tk.getImage("picture/chess/WhiteChess2.jpg"),
				tk.getImage("picture/chess/middle.jpg") };
		// 初始化最初四个棋子
		allChess[4][4] = WHITE;
		allChess[3][3] = WHITE;
		allChess[3][4] = BLACK;
		allChess[4][3] = BLACK;

		this.setSize(570, 530);

	}

	/**
	 * 画出棋盘及相关组件
	 */
	public void paint(Graphics g) {
		// 双缓冲技术,防止屏幕闪烁
		bi = this.createImage(570, 530);
		Graphics g2 = bi.getGraphics();
		// 绘制背景
		g2.drawImage(images[0], 10, 55, this);// 绘制棋盘
		g2.drawImage(images[4], 25, 15, this);// 绘制表情
		g2.drawImage(images[6], 70, 430, this);// 绘制计算数目黑棋
		g2.drawImage(images[7], 270, 430, this);// 绘制计算数目白棋

		if (GameOption.drawBorder == true) {
			ch.drawBorder();
		}

		g.drawImage(bi, 0, 0, this);
		ch.draw();
		pp.draw();
		if (cc.isPlayer1 == true && cc.isPlayer2 == false
				&& pp.player1Name != "") { // 第一个玩家进入
			pp.drawPlayer1();
		}
		if (cc.isPlayer2 == true && cc.isPlayer1 == false
				&& pp.player2Name != "") { // 第二个玩家进入
			pp.drawPlayer2();
		}
		if (cc.player1Enter == true && pp.player1Name != "") {// 当第一个玩家进入时，第二个玩家显示
			pp.drawPlayer1();
		}
		if (cc.player2Enter == true && pp.player2Name != "") {// 第二个玩家进入时，第一个玩家显示
			pp.drawPlayer2();
		}
		if (cc.isPlayer1 == false && cc.isPlayer2 == false
				&& pp.player1Name != "" && pp.player2Name != "") { // 观战者
			pp.drawPlayer1();
			pp.drawPlayer2();
		}
		this.drawInfo(g2);

		g.drawImage(bi, 0, 0, null);
	}

	/**
	 * 画出相关的游戏信息
	 * 
	 * @param g
	 *            画笔
	 */
	public void drawInfo(Graphics g) {
		g.setFont(new Font("黑体", Font.BOLD, 20));// 绘制游戏信息
		g.setColor(Color.BLACK);
		g.drawString("游戏信息:", 50, 35);
		if (changeColor == true) {
			g.setColor(Color.RED);
			g.drawString(message, 154, 35);
			changeColor = false;
		} else {
			g.drawString(message, 154, 35);

		}

		g.setFont(new Font("黑体", Font.BOLD, 30));// 绘制棋子数目
		g.setColor(Color.RED);
		g.drawString("" + ch.countBlack, 140, 460);
		g.drawString(":", 180, 455);
		g.drawString("" + ch.countWhite, 220, 460);
		g.setFont(new Font("黑体", Font.BOLD, 17));
		g.setColor(Color.WHITE);// 绘制时间信息
		g.drawString(go.blackMessage, 490, 203);
		g.drawString(go.whiteMessage, 490, 309);
		g.drawString("" + go.blackOneTime, 500, 223);
		g.drawString("" + go.whiteOneTime, 500, 329);
		g.drawString("" + this.blackWin, 500, 245);
		g.drawString("" + this.whiteWin, 500, 350);

	}

	/**
	 * 实现鼠标点击的功能
	 */
	public void mousePressed(MouseEvent e) {
		x = e.getX();
		y = e.getY();
		// 确定鼠标点击的位点
		if (x >= 30 && x <= 350 && y >= 75 && y <= 395) {
			for (int i = 0; i < 8; i++) {
				if (x >= 30 + i * 40 && x <= 30 + (i + 1) * 40) {
					x = 30 + i * 40;
				}
				if (y >= 75 + i * 40 && y <= 75 + (i + 1) * 40) {
					y = 75 + i * 40;
				}
			}
			m = (x - 30) / 40;
			n = (y - 75) / 40;
			// 和电脑对战
			if (computerPlay == true) {
				if (allChess[m][n] != 0) {// 下到已经有棋子的地方，重新落子
					voice.play("wrong");
					JOptionPane.showMessageDialog(this, "当前位置已经有棋子，请重新落子。");
					return;
				}
				if(ch.isWhiteStop==true){
					ch.isWhiteStop=false;
				}
				ch.gameRule(m, n);
			}
			// 和玩家对战
			if (computerPlay == false) {
				if (isWatcher == true) { // 如果是观战者，无法下棋
					JOptionPane.showMessageDialog(this, "您是观战者，无法下棋！");
					return;
				}
				if (canPlay == true && isOpposite == false) {
					if (allChess[m][n] != 0) {// 下到已经有棋子的地方，重新落子
						voice.play("wrong");
						JOptionPane.showMessageDialog(this, "当前位置已经有棋子，请重新落子。");
						return;
					}
					if (ch.isRight(m, n) == false) {// 下到不符合规定的地方，重新落子
						return;
					}
					if (cc.isPlayer1 == true && cc.isPlayer2 == false
							&& isPlayer1Played == false) {// 玩家1先下棋
						isPlayer1Played = true;
						try {
							cc.dos.writeUTF("player1played");
							isPlayer1Played = true;
						} catch (IOException e1) {
							e1.printStackTrace();
						}
					}
					if (isPlayer1Played == false) {// 判断玩家1是否已经下子
						return;
					}
					ch.gameRule(m, n);
					this.send(cc.dos);

				}
			}
		}

	}

	/**
	 * 发送游戏数据到服务器
	 * 
	 * @param dos
	 */
	public void send(DataOutputStream dos) {// 发送数据
		try {
			dos.writeUTF("chess");// 发送标记数据
			for (int i = 0; i < 8; i++) {
				for (int j = 0; j < 8; j++) {
					dos.writeInt(allChess[i][j]);
				}
			}
			dos.writeBoolean(isBlack);
			dos.writeBoolean(canPlay);
			dos.writeBoolean(ch.isStop);
			System.out.println(ch.isStop);
			dos.writeUTF(message);
			dos.writeInt(ch.countBlack);
			dos.writeInt(ch.countWhite);
			if (ch.isStop == false) {
				this.isOpposite = true;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 读取服务器发送来的信息
	 * 
	 * @param dis
	 */
	@SuppressWarnings("deprecation")
	public void read(DataInputStream dis) {// 接收数据
		try {
			for (int i = 0; i < 8; i++) {
				for (int j = 0; j < 8; j++) {
					allChess[i][j] = dis.readInt();

				}
			}
			isBlack = dis.readBoolean();
			canPlay = dis.readBoolean();
			isStop = dis.readBoolean();
			message = dis.readUTF();
			ch.countBlack = dis.readInt();
			ch.countWhite = dis.readInt();
			if (canPlay == false) {
				go.t.suspend();
			}
			this.repaint();
			if (isStop == false) {
				this.isOpposite = false;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	/**
	 * 实现鼠标松开时，电脑下棋的功能
	 */
	public void mouseReleased(MouseEvent e) {
		if (computerPlay == true&&ch.isWhiteStop==false) {
			ch.playWithCom();
		}
	}

	public void mouseClicked(MouseEvent e) {
	}

	public void mouseEntered(MouseEvent arg0) {
	}

	public void mouseExited(MouseEvent arg0) {
	}

	@SuppressWarnings("deprecation")
	/**
	 * 初始化游戏
	 */
	public void initChessBoard() {
		for (int i = 0; i < 8; i++) {
			for (int j = 0; j < 8; j++) {
				allChess[i][j] = 0;
			}
		}
		allChess[4][4] = WHITE;
		allChess[3][3] = WHITE;
		allChess[3][4] = BLACK;
		allChess[4][3] = BLACK;
		this.message = "黑方先行";
		this.isBlack = true;
		this.canPlay = true;
		this.changeColor=false;
		this.blackScore = 0;
		this.whiteScore = 0;
		ch.countBlack=2;
		ch.countWhite=2;
		go.blacktime = go.maxtime;
		go.whitetime = go.maxtime;
		go.blackMessage=go.initTime(go.blacktime);
		go.whiteMessage=go.initTime(go.whitetime);
		go.blackOneTime = go.maxOneStepTime;
		go.whiteOneTime = go.maxOneStepTime;
		if (computerPlay == false) {
			go.t.resume();
		}
		new Voice().play("start");
		this.repaint();
	}
}
