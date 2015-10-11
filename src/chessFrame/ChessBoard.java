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
 * ʵ�����̵���ʾ����
 * 
 * @author ZhuYingtao
 * 
 */
@SuppressWarnings("serial")
public class ChessBoard extends JPanel implements MouseListener {
	private Toolkit tk = Toolkit.getDefaultToolkit();
	/**
	 * ��ȡ��Ļ�Ŀ��
	 */
	int width = tk.getScreenSize().width;
	/**
	 * ��ȡ��Ļ�ĸ߶�
	 */
	int height = tk.getScreenSize().height;
	/**
	 * �洢��Ϸ������Ҫ��ͼ��
	 */
	Image[] images = null;

	private int x, y;// ��ʶ����
	/**
	 * ��ʾ����ĵ������������ڵ�����
	 */
	int m;
	/**
	 * ��ʶ����ĵ������������ڵ�����
	 */
	int n;

	private final int BLACK = 1;

	private final int WHITE = 2;
	/**
	 * ��ʾ��Ϸ����Ϣ
	 */
	String message = "�ڷ�����";
	/**
	 * ��ʶ�Ƿ����������
	 */
	boolean computerPlay = false;
	/**
	 * ��ʶ��������Ƿ��ǹ�ս��
	 */
	boolean isWatcher = false;
	/**
	 * ��ʶ�Ƿ�ͣ��
	 */
	boolean isStop = false;
	/**
	 * ��ʶ���1�Ƿ�����
	 */
	boolean isPlayer1Played = false;
	/**
	 * ��ʶ�Ƿ��ֵ��ڷ���
	 */
	boolean isBlack = true;
	/**
	 * ��ʶ�ܷ������Ϸ
	 */
	boolean canPlay = false;
	/**
	 * ��ʶ�Ƿ�ı�������ɫ
	 */
	boolean changeColor = false;
	/**
	 * ��ʶ�Ƿ��ɶԷ�����
	 */
	boolean isOpposite = false;
	/**
	 * �洢���µ�����
	 */
	int[][] allChess = new int[8][8];
	/**
	 * ����ʱ��¼��һ�����ߵ�����
	 */
	int[][] regChess = new int[8][8];
	/**
	 * ��¼�ڷ�Ӯ������
	 */
	int blackWin = 0;
	/**
	 * ��¼�׷�Ӯ������
	 */
	int whiteWin = 0;
	/**
	 * ��¼�ڷ����ֵķ���
	 */
	int blackScore = 0;
	/**
	 * ��¼�׷����ֵķ���
	 */
	int whiteScore = 0;
	/**
	 * ����ͼƬ
	 */
	Image bi = null;
	/**
	 * Chess�������
	 */
	Chess ch = null;
	/**
	 * PlayerPanel�������
	 */
	PlayerPanel pp = null;
	/**
	 * ChessClient�������
	 */
	ChessClient cc = null;
	/**
	 * GameOption�������
	 */
	GameOption go = null;
	/**
	 * ScoreUpdate�������
	 */
	ScoreUpdate su = null;
	private Voice voice = new Voice();

	/**
	 * �������̽���
	 * 
	 * @param game
	 *            Game�������
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

		// ����ͼƬ
		images = new Image[] { tk.getImage("picture/chess/BackGround.png"),
				tk.getImage("picture/chess/BlackChess.jpg"),
				tk.getImage("picture/chess/WhiteChess.jpg"),
				tk.getImage("picture/chess/Border.jpg"),
				tk.getImage("picture/chess/Smile.jpg"),
				tk.getImage("picture/playerPanel/man.jpg"),
				tk.getImage("picture/chess/BlackChess2.jpg"),
				tk.getImage("picture/chess/WhiteChess2.jpg"),
				tk.getImage("picture/chess/middle.jpg") };
		// ��ʼ������ĸ�����
		allChess[4][4] = WHITE;
		allChess[3][3] = WHITE;
		allChess[3][4] = BLACK;
		allChess[4][3] = BLACK;

		this.setSize(570, 530);

	}

	/**
	 * �������̼�������
	 */
	public void paint(Graphics g) {
		// ˫���弼��,��ֹ��Ļ��˸
		bi = this.createImage(570, 530);
		Graphics g2 = bi.getGraphics();
		// ���Ʊ���
		g2.drawImage(images[0], 10, 55, this);// ��������
		g2.drawImage(images[4], 25, 15, this);// ���Ʊ���
		g2.drawImage(images[6], 70, 430, this);// ���Ƽ�����Ŀ����
		g2.drawImage(images[7], 270, 430, this);// ���Ƽ�����Ŀ����

		if (GameOption.drawBorder == true) {
			ch.drawBorder();
		}

		g.drawImage(bi, 0, 0, this);
		ch.draw();
		pp.draw();
		if (cc.isPlayer1 == true && cc.isPlayer2 == false
				&& pp.player1Name != "") { // ��һ����ҽ���
			pp.drawPlayer1();
		}
		if (cc.isPlayer2 == true && cc.isPlayer1 == false
				&& pp.player2Name != "") { // �ڶ�����ҽ���
			pp.drawPlayer2();
		}
		if (cc.player1Enter == true && pp.player1Name != "") {// ����һ����ҽ���ʱ���ڶ��������ʾ
			pp.drawPlayer1();
		}
		if (cc.player2Enter == true && pp.player2Name != "") {// �ڶ�����ҽ���ʱ����һ�������ʾ
			pp.drawPlayer2();
		}
		if (cc.isPlayer1 == false && cc.isPlayer2 == false
				&& pp.player1Name != "" && pp.player2Name != "") { // ��ս��
			pp.drawPlayer1();
			pp.drawPlayer2();
		}
		this.drawInfo(g2);

		g.drawImage(bi, 0, 0, null);
	}

	/**
	 * ������ص���Ϸ��Ϣ
	 * 
	 * @param g
	 *            ����
	 */
	public void drawInfo(Graphics g) {
		g.setFont(new Font("����", Font.BOLD, 20));// ������Ϸ��Ϣ
		g.setColor(Color.BLACK);
		g.drawString("��Ϸ��Ϣ:", 50, 35);
		if (changeColor == true) {
			g.setColor(Color.RED);
			g.drawString(message, 154, 35);
			changeColor = false;
		} else {
			g.drawString(message, 154, 35);

		}

		g.setFont(new Font("����", Font.BOLD, 30));// ����������Ŀ
		g.setColor(Color.RED);
		g.drawString("" + ch.countBlack, 140, 460);
		g.drawString(":", 180, 455);
		g.drawString("" + ch.countWhite, 220, 460);
		g.setFont(new Font("����", Font.BOLD, 17));
		g.setColor(Color.WHITE);// ����ʱ����Ϣ
		g.drawString(go.blackMessage, 490, 203);
		g.drawString(go.whiteMessage, 490, 309);
		g.drawString("" + go.blackOneTime, 500, 223);
		g.drawString("" + go.whiteOneTime, 500, 329);
		g.drawString("" + this.blackWin, 500, 245);
		g.drawString("" + this.whiteWin, 500, 350);

	}

	/**
	 * ʵ��������Ĺ���
	 */
	public void mousePressed(MouseEvent e) {
		x = e.getX();
		y = e.getY();
		// ȷ���������λ��
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
			// �͵��Զ�ս
			if (computerPlay == true) {
				if (allChess[m][n] != 0) {// �µ��Ѿ������ӵĵط�����������
					voice.play("wrong");
					JOptionPane.showMessageDialog(this, "��ǰλ���Ѿ������ӣ����������ӡ�");
					return;
				}
				if(ch.isWhiteStop==true){
					ch.isWhiteStop=false;
				}
				ch.gameRule(m, n);
			}
			// ����Ҷ�ս
			if (computerPlay == false) {
				if (isWatcher == true) { // ����ǹ�ս�ߣ��޷�����
					JOptionPane.showMessageDialog(this, "���ǹ�ս�ߣ��޷����壡");
					return;
				}
				if (canPlay == true && isOpposite == false) {
					if (allChess[m][n] != 0) {// �µ��Ѿ������ӵĵط�����������
						voice.play("wrong");
						JOptionPane.showMessageDialog(this, "��ǰλ���Ѿ������ӣ����������ӡ�");
						return;
					}
					if (ch.isRight(m, n) == false) {// �µ������Ϲ涨�ĵط�����������
						return;
					}
					if (cc.isPlayer1 == true && cc.isPlayer2 == false
							&& isPlayer1Played == false) {// ���1������
						isPlayer1Played = true;
						try {
							cc.dos.writeUTF("player1played");
							isPlayer1Played = true;
						} catch (IOException e1) {
							e1.printStackTrace();
						}
					}
					if (isPlayer1Played == false) {// �ж����1�Ƿ��Ѿ�����
						return;
					}
					ch.gameRule(m, n);
					this.send(cc.dos);

				}
			}
		}

	}

	/**
	 * ������Ϸ���ݵ�������
	 * 
	 * @param dos
	 */
	public void send(DataOutputStream dos) {// ��������
		try {
			dos.writeUTF("chess");// ���ͱ������
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
	 * ��ȡ����������������Ϣ
	 * 
	 * @param dis
	 */
	@SuppressWarnings("deprecation")
	public void read(DataInputStream dis) {// ��������
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
	 * ʵ������ɿ�ʱ����������Ĺ���
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
	 * ��ʼ����Ϸ
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
		this.message = "�ڷ�����";
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
