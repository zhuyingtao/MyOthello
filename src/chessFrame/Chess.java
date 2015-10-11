package chessFrame;

import hallFrame.HallPlayerPanel;

import java.awt.Graphics;
import javax.swing.JOptionPane;

/**
 * 用于实现游戏中的一系列规则，如下棋，判断是否能下，判断是否停步， 判断是否游戏结束， 统计黑白子等功能
 * 
 * @author ZhuYingtao
 * 
 */
public class Chess {
	private int m, n;

	private int mPlus = 0;

	private int nPlus = 0;
	/**
	 * 如果是1，则代表黑子
	 */
	final int BLACK = 1;
	/**
	 * 如果是2，则代表白子
	 */
	final int WHITE = 2;
	/**
	 * 记录黑子数目，默认是2
	 */
	int countBlack = 2;
	/**
	 * 记录白子数目，默认是2
	 */
	int countWhite = 2;
	/**
	 * 记录上步统计的黑子数目
	 */
	int lastCountBlack;
	/**
	 * 记录上步统计的白子数目
	 */
	int lastCountWhite;

	private boolean isRight = false;
	boolean isStop = true;
	boolean isWhiteStop=false;
	private boolean isStop2 = true;
	/**
	 * 限定判断输赢次数
	 */
	boolean oneCheck = true;

	private int[][] allChess;
	private int[][] regChess;
	private int[][] midChess = new int[8][8];

	private ChessBoard cb;
	private ReadyPanel rp;

	private Voice voice = new Voice();

	/**
	 * 得到ChessBoard类的引用
	 * 
	 * @param cb
	 *            ChessBoard类的引用
	 */
	public Chess(ChessBoard cb) {
		this.cb = cb;
		this.allChess = cb.allChess;
		this.regChess = cb.regChess;

	}

	/**
	 * 得到ReadyPanel类的引用
	 * 
	 * @param rp
	 *            ReadyPanel类的引用
	 */
	public void getReadyPanel(ReadyPanel rp) {
		this.rp = rp;
	}

	/**
	 * 实现绘制棋子的功能
	 */
	public void draw() {
		// 绘制所有棋子
		Graphics g = cb.bi.getGraphics();
		for (int i = 0; i < 8; i++) {
			for (int j = 0; j < 8; j++) {

				if (allChess[i][j] == BLACK) {
					// 黑子
					int tempX = 30 + i * 40;
					int tempY = 75 + j * 40;
					g.drawImage(cb.images[1], tempX + 1, tempY + 1, null);
				}
				if (allChess[i][j] == WHITE) {
					// 白子
					int tempX = 30 + i * 40;
					int tempY = 75 + j * 40;
					g.drawImage(cb.images[2], tempX, tempY + 1, null);
				}
			}
		}
	}

	/*public void drawMiddle() {  //画出中间状态功能未实现
		Graphics g = cb.bi.getGraphics();
		for (int i = 0; i < 8; i++) {
			for (int j = 0; j < 8; j++) {
				if (midChess[i][j] == 1) {
					for (int m = 0; m < 50; m++) {
						int tempX = 30 + i * 40;
						int tempY = 75 + j * 40;
						g.drawImage(cb.images[8], tempX, tempY + 1, null);
					}
				}
			}
		}

		for (int i = 0; i < 8; i++) {
			for (int j = 0; j < 8; j++) {
				midChess[i][j] = 0;
			}
		}
	}*/

	/**
	 * 实现绘制提示边框的功能
	 */
	public void drawBorder() {
		Graphics g = cb.bi.getGraphics();
		for (int i = 0; i < 8; i++) {
			for (int j = 0; j < 8; j++) {
				if ((i == 3 && j == 4) || (i == 3 && j == 3)
						|| (i == 4 && j == 3) || (i == 4 && j == 4)) {
					continue;
				}
				isRight(i, j);
				if (isRight == true) {
					int tempX = 30 + i * 40;
					int tempY = 75 + j * 40;
					g.drawImage(cb.images[3], tempX, tempY + 1, null);
				}
			}
		}

	}

	/**
	 * 判断所下棋子是否符合规则
	 * 
	 * @param M
	 *            给定所下棋子在数组中所在的行数
	 * @param N
	 *            给定所下棋子在数组中所在的列数
	 * @return 如果合法则返回true，不合法则返回false.
	 */
	public boolean isRight(int M, int N) {
		int color = 0;
		isRight = false;
		if (allChess[M][N] == 0) {
			if (cb.isBlack == true) {
				color = BLACK;

			} else {
				color = WHITE;
			}
		} else {
			return isRight;
		}

		for (int i = 0; i < 8; i++) {
			m = M;
			n = N;
			mPlus = 0;
			nPlus = 0;
			initDirection(i, m, n);
			try {
				if (allChess[m + mPlus][n + nPlus] != color
						&& allChess[m + mPlus][n + nPlus] != 0) {

					for (int j = 0; j < 8; j++) {
						m += mPlus;
						n += nPlus;
						if (allChess[m][n] == 0) {
							break;
						}
						if (allChess[m][n] == color) {
							isRight = true;
							return isRight;
						}
					}
				}

			} catch (Exception e) {
			}

		}
		return isRight;
	}

	/**
	 * 实现吃子的功能
	 * 
	 * @param M
	 *            给定所下棋子在数组中所在的行数
	 * @param N
	 *            给定所下棋子在数组中所在的列数
	 * @return
	 */
	public boolean gameRule(int M, int N) {

		for (int i = 0; i < 8; i++) {
			for (int j = 0; j < 8; j++) {
				regChess[i][j] = 0;
			}
		}
		if (allChess[M][N] != 0) {
			voice.play("wrong");
			JOptionPane.showMessageDialog(cb, "当前位置已经有棋子，请重新落子。");
			return false;
		} else {

			this.isRight(M, N);
			m = M;
			n = N;
			if (cb.isBlack == true) {
				allChess[m][n] = BLACK;
			} else {
				allChess[m][n] = WHITE;
			}
			int color = allChess[m][n];
			if (isRight == false) {
				allChess[M][N] = 0;
				return false;
			}
			if (isRight == true) {
				voice.play("play");
				if (cb.isBlack == true) {
					cb.isBlack = false;
					cb.message = "轮到白方";

				} else {
					cb.isBlack = true;
					cb.message = "轮到黑方";
				}

				for (int d = 0; d < 8; d++) {
					mPlus = 0;
					nPlus = 0;
					int newM = m;
					int newN = n;
					initDirection(d, newM, newN);
					int step = 0;
					try {
						do {
							newM += mPlus;
							newN += nPlus;
							step++;

						} while (allChess[newM][newN] != color
								&& allChess[newM][newN] != 0);

					} catch (Exception e) {
						if (newM == -1) {
							if (newN == -1) {
								newN = 0;
							}
							newM = 0;
						}
						if (newM != -1 && newN == -1) {
							newN = 0;
						}
						if (newM == 8) {
							if (newN == 8) {
								newN = newN - 1;
							}

							newM = newM - 1;
						}
						if (newM != 8 && newN == 8) {
							newN = newN - 1;
						}

					}
					if (allChess[newM][newN] == color) {
						for (int i = 1; i < step; i++) {
							allChess[m + i * mPlus][n + i * nPlus] = color;
							regChess[m + i * mPlus][n + i * nPlus] = 1;
							midChess[m + i * mPlus][n + i * nPlus] = 1;
						}
						regChess[M][N] = 2;
					}
				}
			}
		}
		countLast();
		countChess();
		isWin();
		cb.repaint();
		return true;
	}

	/**
	 * 实现统计棋子数目的功能
	 */
	public void countChess() {
		countBlack = 0;
		countWhite = 0;
		for (int i = 0; i < 8; i++) {
			for (int j = 0; j < 8; j++) {

				if (allChess[i][j] == BLACK) {
					countBlack++;
				}
				if (allChess[i][j] == WHITE) {
					countWhite++;
				}

			}
		}
	}

	/**
	 * 记录上步统计的黑白子数目
	 */
	public void countLast() {
		lastCountBlack = countBlack;
		lastCountWhite = countWhite;
	}

	/**
	 * 判断是否停步
	 */
	@SuppressWarnings( { "deprecation" })
	public void isStop() {
		isStop = true;
		isStop2 = true;
		for (int i = 0; i < 8; i++) {
			for (int j = 0; j < 8; j++) {
				if ((i == 3 && j == 4) || (i == 3 && j == 3)
						|| (i == 4 && j == 3) || (i == 4 && j == 4)) {
					continue;
				}
				isRight(i, j);
				if (isRight == true) {
					isStop = false;

				}
			}

		}

		if (isStop == true && cb.canPlay == true) {
			if (cb.isBlack == true) {
				cb.isBlack = false;
				cb.isOpposite = false;
				cb.message = "黑方停步，轮到白方！";
				if (cb.computerPlay == true) {// 电脑默认是白方
					this.playWithCom();
				}
			} else {
				cb.isBlack = true;
				cb.isOpposite = false;
				cb.message = "白方停步，轮到黑方！";
				isWhiteStop=true;
			}

			for (int i = 0; i < 8; i++) {
				for (int j = 0; j < 8; j++) {
					if ((i == 3 && j == 4) || (i == 3 && j == 3)
							|| (i == 4 && j == 3) || (i == 4 && j == 4)) {
						continue;
					}
					isRight(i, j);
					if (isRight == true) {
						isStop2 = false;

					}
				}

			}

			if (isStop2 == true && cb.canPlay == true) {
				if (cb.isBlack == true) {
					cb.message = "黑方停步！轮到白方";
				} else {
					cb.message = "白方停步！轮到黑方";
				}
				cb.message = "双方无棋可走，游戏结束";
				cb.go.t.suspend();
				cb.canPlay = false;
			}
			cb.changeColor = true;
		}

	}

	/**
	 * 判断输赢
	 * 
	 * @return 如果能判定输赢则返回true，否则返回false
	 */

	@SuppressWarnings( { "deprecation" })
	public boolean isWin() {

		this.isStop();
		this.isOver();
		if (cb.canPlay == false && oneCheck == true) {
			voice.play("end");
			cb.go.t.suspend();
			cb.changeColor = true;
			this.countChess();
			if (countBlack > countWhite) {
				cb.message = "游戏结束,黑方胜!";
				if (cb.computerPlay == false) {
					cb.blackWin++;
				}
			} else if (countBlack < countWhite) {
				cb.message = "游戏结束,白方胜!";
				if (cb.computerPlay == false) {
					cb.whiteWin++;
				}
			} else {
				cb.message = "游戏结束，平局！";
			}
			if (cb.cc.isPlayer1 == true && cb.cc.isPlayer2 == false
					&& cb.computerPlay == false) {
				int winOrLose=0;
				JOptionPane.showMessageDialog(cb, "游戏结束，黑方：" + countBlack
						+ " ,白方：" + countWhite + "\n您获得了"
						+ (countBlack - countWhite) + "分！！");
				cb.whiteScore = countWhite - countBlack;
				cb.blackScore = countBlack - countWhite;
				if (cb.whiteScore > cb.blackScore) {
					winOrLose = -1;
				} else if (cb.whiteScore < cb.blackScore) {
				   winOrLose = 1;
				}else{
					winOrLose=0;
				}
				cb.su.update(winOrLose, cb.blackScore, HallPlayerPanel.nickname);
				cb.cc.update(cb.blackScore);
				

			} else if (cb.cc.isPlayer1 == false && cb.cc.isPlayer2 == true
					&& cb.computerPlay == false) {
				int winOrLose=0;
				JOptionPane.showMessageDialog(cb, "游戏结束，黑方：" + countBlack
						+ " ,白方：" + countWhite + "\n您获得了"
						+ (countWhite - countBlack) + "分！！");
				cb.whiteScore = countWhite - countBlack;
				cb.blackScore = countBlack - countWhite;
				if (cb.whiteScore > cb.blackScore) {
					winOrLose = 1;
				} else if (cb.whiteScore < cb.blackScore) {
					winOrLose = -1;
				}else{
					winOrLose=0;
				}
				cb.su.update(winOrLose,cb.whiteScore, HallPlayerPanel.nickname);
				cb.cc.update(cb.blackScore);
			}

			rp.ready.setText("准备");
			rp.isReady = false;
			oneCheck = false;
			return true;

		}

		return false;
	}

	/**
	 * 判断整个棋盘是否下满
	 * 
	 * @return 下满则返回true,否则则返回false.
	 */
	public boolean isOver() {
		for (int i = 0; i < 8; i++) {
			for (int j = 0; j < 8; j++) {
				if (allChess[i][j] == 0) {
					return false;
				}
			}
		}
		cb.canPlay = false;
		return true;
	}

	/**
	 * 初始化吃子规则的判定方向
	 * 
	 * @param direction
	 *            代表方向的数字
	 * @param m
	 *            给定所下棋子在数组中所在的行数
	 * @param n
	 *            给定所下棋子在数组中所在的列数
	 */
	public void initDirection(int direction, int m, int n) {
		switch (direction) {
		case 0:// 下方向
			mPlus = 1;
			break;
		case 1:// 右下方向
			mPlus = 1;
			nPlus = 1;
			break;
		case 2:// 右方向
			nPlus = 1;
			break;
		case 3:// 右上方向
			mPlus = -1;
			nPlus = 1;
			break;
		case 4:// 上方向
			mPlus = -1;
			break;
		case 5:// 左上方向
			mPlus = -1;
			nPlus = -1;
			break;
		case 6:// 左方向
			nPlus = -1;
			break;
		case 7:// 左下方向
			mPlus = 1;
			nPlus = -1;
			break;
		}

	}

	/**
	 * 实现和电脑下棋的功能
	 */
	public void playWithCom() {
		try {
			Thread.sleep(500);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		boolean computerPlayed = false;// 定义电脑是否已经下过
		// 电脑下棋，先判断四个角是否能下
		if (allChess[0][0] == 0) {
			computerPlayed = this.gameRule(0, 0);
			if (computerPlayed == true) { // 如果电脑已经下了就返回
				return;
			}
		}
		if (allChess[7][0] == 0) {
			computerPlayed = this.gameRule(7, 0);
			if (computerPlayed == true) {
				return;
			}
		}
		if (allChess[0][7] == 0) {
			computerPlayed = this.gameRule(0, 7);
			if (computerPlayed == true) {
				return;
			}
		}
		if (allChess[7][7] == 0) {
			computerPlayed = this.gameRule(7, 7);
			if (computerPlayed == true) {
				return;
			}
		}

		for (int i = 0; i < 7; i++) {
			// 再判断四条边是否能下
			if (allChess[i][0] == 0 && i != 0 && i != 7) {
				computerPlayed = this.gameRule(i, 0);
				if (computerPlayed == true) {
					return;
				}
			}
			if (allChess[0][i] == 0 && i != 7 && i != 0) {
				computerPlayed = this.gameRule(0, i);
				if (computerPlayed == true) {
					return;
				}
			}
			if (allChess[7][i] == 0 && i != 7 && i != 0) {
				computerPlayed = this.gameRule(7, i);
				if (computerPlayed == true) {
					return;
				}
			}
			if (allChess[i][7] == 0 && i != 7 && i != 0) {
				computerPlayed = this.gameRule(i, 7);
				if (computerPlayed == true) {
					return;
				}
			}
			// 再判断对角线是否能下子
			if (allChess[i][i] == 0 && i != 0 && i != 7) {
				computerPlayed = this.gameRule(i, i);
				if (computerPlayed == true) {
					return;
				}
			}
			if (allChess[i][7 - i] == 0 && i != 0 && i != 7) {
				computerPlayed = this.gameRule(i, 7 - i);
				if (computerPlayed == true) {
					return;
				}
			}

		}
		// 最后判断剩下的能否下子
		for (int i = 0; i < 7; i++) {
			for (int j = 0; j < 7; j++) {
				if (allChess[i][j] == 0 && i != 7 && i != 0 && j != 7 && j != 0
						&& i != j) {
					computerPlayed = this.gameRule(i, j);
					if (computerPlayed == true) {
						return;
					}
				}
			}
		}
	}
}
