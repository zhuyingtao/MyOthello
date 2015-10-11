package chessFrame;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
/**
 * 此类用于显示游戏中的玩家信心
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
     * 玩家1的姓名
     */
    String player1Name = "";
    /**
     * 玩家1的性别
     */
	String player1Sex = "";
	/**
	 * 玩家2的姓名
	 */
	String player2Name = "";
	/**
	 * 玩家2的性别
	 */
	String player2Sex = "";
    /**
     * 构造显示界面
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
     * 画出背景
     */
	public void draw() {
		Graphics g = cb.bi.getGraphics();
		g.drawImage(playerPanel, 380, 0, cb);
		g.drawImage(blackChess, 381, 185, cb);
		g.drawImage(whiteChess, 381, 290, cb);
	}
    /**
     * 画出玩家1
     */
	public void drawPlayer1() {
		Graphics g = cb.bi.getGraphics();
		if (player1Sex.equals("男")) {
			g.drawImage(man, 385, 5, cb);
		}
		if (player1Sex.equals("女") ) {
			g.drawImage(woman, 385, 5, cb);
		}
		g.setColor(Color.WHITE);
		g.setFont(new Font("宋体", Font.PLAIN, 15));
		g.drawString(player1Name, 460, 178);
	} 
    /**
     * 画出玩家2
     */
	public void drawPlayer2() {
		Graphics g = cb.bi.getGraphics();
		if (player2Sex.equals("男")) {
			g.drawImage(man, 385, 365, cb);
		}
		if (player2Sex.equals("女")) {
			g.drawImage(woman, 385, 365, cb);
		}
		g.setColor(Color.WHITE);
		g.setFont(new Font("宋体", Font.PLAIN, 15));
		g.drawString(player2Name, 460, 285);

	}

}
