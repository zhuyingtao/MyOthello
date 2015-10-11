package chessFrame;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
/**
 * ����������ʾ��Ϸ�е��������
 * @author ZhuYingtao
 *
 */
public class PlayerPanel {
	private Toolkit tk = Toolkit.getDefaultToolkit();

	private ChessBoard cb;

	private Image playerPanel = null;
	private Image man = null;
	private Image blackChess = null;
	private Image whiteChess = null;
	private Image woman = null;
    /**
     * ���1������
     */
    String player1Name = "";
    /**
     * ���1���Ա�
     */
	String player1Sex = "";
	/**
	 * ���2������
	 */
	String player2Name = "";
	/**
	 * ���2���Ա�
	 */
	String player2Sex = "";
    /**
     * ������ʾ����
     * @param cb
     */
	public PlayerPanel(ChessBoard cb) {
		this.cb = cb;
		playerPanel = tk.getImage("picture/playerPanel/playerPanel.jpg");
		man = tk.getImage("picture/playerPanel/man.jpg");
		woman = tk.getImage("picture/playerPanel/woman.jpg");
		blackChess = tk.getImage("picture/playerPanel/BlackChess.jpg");
		whiteChess = tk.getImage("picture/playerPanel/WhiteChess.jpg");
	}
    /**
     * ��������
     */
	public void draw() {
		Graphics g = cb.bi.getGraphics();
		g.drawImage(playerPanel, 380, 0, cb);
		g.drawImage(blackChess, 381, 185, cb);
		g.drawImage(whiteChess, 381, 290, cb);
	}
    /**
     * �������1
     */
	public void drawPlayer1() {
		Graphics g = cb.bi.getGraphics();
		if (player1Sex.equals("��")) {
			g.drawImage(man, 385, 5, cb);
		}
		if (player1Sex.equals("Ů") ) {
			g.drawImage(woman, 385, 5, cb);
		}
		g.setColor(Color.WHITE);
		g.setFont(new Font("����", Font.PLAIN, 15));
		g.drawString(player1Name, 460, 178);
	} 
    /**
     * �������2
     */
	public void drawPlayer2() {
		Graphics g = cb.bi.getGraphics();
		if (player2Sex.equals("��")) {
			g.drawImage(man, 385, 365, cb);
		}
		if (player2Sex.equals("Ů")) {
			g.drawImage(woman, 385, 365, cb);
		}
		g.setColor(Color.WHITE);
		g.setFont(new Font("����", Font.PLAIN, 15));
		g.drawString(player2Name, 460, 285);

	}

}
