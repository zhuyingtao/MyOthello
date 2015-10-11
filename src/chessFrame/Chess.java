package chessFrame;

import hallFrame.HallPlayerPanel;

import java.awt.Graphics;
import javax.swing.JOptionPane;

/**
 * ����ʵ����Ϸ�е�һϵ�й��������壬�ж��Ƿ����£��ж��Ƿ�ͣ���� �ж��Ƿ���Ϸ������ ͳ�ƺڰ��ӵȹ���
 * 
 * @author ZhuYingtao
 * 
 */
public class Chess {
	private int m, n;

	private int mPlus = 0;

	private int nPlus = 0;
	/**
	 * �����1����������
	 */
	final int BLACK = 1;
	/**
	 * �����2����������
	 */
	final int WHITE = 2;
	/**
	 * ��¼������Ŀ��Ĭ����2
	 */
	int countBlack = 2;
	/**
	 * ��¼������Ŀ��Ĭ����2
	 */
	int countWhite = 2;
	/**
	 * ��¼�ϲ�ͳ�Ƶĺ�����Ŀ
	 */
	int lastCountBlack;
	/**
	 * ��¼�ϲ�ͳ�Ƶİ�����Ŀ
	 */
	int lastCountWhite;

	private boolean isRight = false;
	boolean isStop = true;
	boolean isWhiteStop=false;
	private boolean isStop2 = true;
	/**
	 * �޶��ж���Ӯ����
	 */
	boolean oneCheck = true;

	private int[][] allChess;
	private int[][] regChess;
	private int[][] midChess = new int[8][8];

	private ChessBoard cb;
	private ReadyPanel rp;

	private Voice voice = new Voice();

	/**
	 * �õ�ChessBoard�������
	 * 
	 * @param cb
	 *            ChessBoard�������
	 */
	public Chess(ChessBoard cb) {
		this.cb = cb;
		this.allChess = cb.allChess;
		this.regChess = cb.regChess;

	}

	/**
	 * �õ�ReadyPanel�������
	 * 
	 * @param rp
	 *            ReadyPanel�������
	 */
	public void getReadyPanel(ReadyPanel rp) {
		this.rp = rp;
	}

	/**
	 * ʵ�ֻ������ӵĹ���
	 */
	public void draw() {
		// ������������
		Graphics g = cb.bi.getGraphics();
		for (int i = 0; i < 8; i++) {
			for (int j = 0; j < 8; j++) {

				if (allChess[i][j] == BLACK) {
					// ����
					int tempX = 30 + i * 40;
					int tempY = 75 + j * 40;
					g.drawImage(cb.images[1], tempX + 1, tempY + 1, null);
				}
				if (allChess[i][j] == WHITE) {
					// ����
					int tempX = 30 + i * 40;
					int tempY = 75 + j * 40;
					g.drawImage(cb.images[2], tempX, tempY + 1, null);
				}
			}
		}
	}

	/*public void drawMiddle() {  //�����м�״̬����δʵ��
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
	 * ʵ�ֻ�����ʾ�߿�Ĺ���
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
	 * �ж����������Ƿ���Ϲ���
	 * 
	 * @param M
	 *            �����������������������ڵ�����
	 * @param N
	 *            �����������������������ڵ�����
	 * @return ����Ϸ��򷵻�true�����Ϸ��򷵻�false.
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
	 * ʵ�ֳ��ӵĹ���
	 * 
	 * @param M
	 *            �����������������������ڵ�����
	 * @param N
	 *            �����������������������ڵ�����
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
			JOptionPane.showMessageDialog(cb, "��ǰλ���Ѿ������ӣ����������ӡ�");
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
					cb.message = "�ֵ��׷�";

				} else {
					cb.isBlack = true;
					cb.message = "�ֵ��ڷ�";
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
	 * ʵ��ͳ��������Ŀ�Ĺ���
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
	 * ��¼�ϲ�ͳ�Ƶĺڰ�����Ŀ
	 */
	public void countLast() {
		lastCountBlack = countBlack;
		lastCountWhite = countWhite;
	}

	/**
	 * �ж��Ƿ�ͣ��
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
				cb.message = "�ڷ�ͣ�����ֵ��׷���";
				if (cb.computerPlay == true) {// ����Ĭ���ǰ׷�
					this.playWithCom();
				}
			} else {
				cb.isBlack = true;
				cb.isOpposite = false;
				cb.message = "�׷�ͣ�����ֵ��ڷ���";
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
					cb.message = "�ڷ�ͣ�����ֵ��׷�";
				} else {
					cb.message = "�׷�ͣ�����ֵ��ڷ�";
				}
				cb.message = "˫��������ߣ���Ϸ����";
				cb.go.t.suspend();
				cb.canPlay = false;
			}
			cb.changeColor = true;
		}

	}

	/**
	 * �ж���Ӯ
	 * 
	 * @return ������ж���Ӯ�򷵻�true�����򷵻�false
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
				cb.message = "��Ϸ����,�ڷ�ʤ!";
				if (cb.computerPlay == false) {
					cb.blackWin++;
				}
			} else if (countBlack < countWhite) {
				cb.message = "��Ϸ����,�׷�ʤ!";
				if (cb.computerPlay == false) {
					cb.whiteWin++;
				}
			} else {
				cb.message = "��Ϸ������ƽ�֣�";
			}
			if (cb.cc.isPlayer1 == true && cb.cc.isPlayer2 == false
					&& cb.computerPlay == false) {
				int winOrLose=0;
				JOptionPane.showMessageDialog(cb, "��Ϸ�������ڷ���" + countBlack
						+ " ,�׷���" + countWhite + "\n�������"
						+ (countBlack - countWhite) + "�֣���");
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
				JOptionPane.showMessageDialog(cb, "��Ϸ�������ڷ���" + countBlack
						+ " ,�׷���" + countWhite + "\n�������"
						+ (countWhite - countBlack) + "�֣���");
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

			rp.ready.setText("׼��");
			rp.isReady = false;
			oneCheck = false;
			return true;

		}

		return false;
	}

	/**
	 * �ж����������Ƿ�����
	 * 
	 * @return �����򷵻�true,�����򷵻�false.
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
	 * ��ʼ�����ӹ�����ж�����
	 * 
	 * @param direction
	 *            �����������
	 * @param m
	 *            �����������������������ڵ�����
	 * @param n
	 *            �����������������������ڵ�����
	 */
	public void initDirection(int direction, int m, int n) {
		switch (direction) {
		case 0:// �·���
			mPlus = 1;
			break;
		case 1:// ���·���
			mPlus = 1;
			nPlus = 1;
			break;
		case 2:// �ҷ���
			nPlus = 1;
			break;
		case 3:// ���Ϸ���
			mPlus = -1;
			nPlus = 1;
			break;
		case 4:// �Ϸ���
			mPlus = -1;
			break;
		case 5:// ���Ϸ���
			mPlus = -1;
			nPlus = -1;
			break;
		case 6:// ����
			nPlus = -1;
			break;
		case 7:// ���·���
			mPlus = 1;
			nPlus = -1;
			break;
		}

	}

	/**
	 * ʵ�ֺ͵�������Ĺ���
	 */
	public void playWithCom() {
		try {
			Thread.sleep(500);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		boolean computerPlayed = false;// ��������Ƿ��Ѿ��¹�
		// �������壬���ж��ĸ����Ƿ�����
		if (allChess[0][0] == 0) {
			computerPlayed = this.gameRule(0, 0);
			if (computerPlayed == true) { // ��������Ѿ����˾ͷ���
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
			// ���ж��������Ƿ�����
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
			// ���ж϶Խ����Ƿ�������
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
		// ����ж�ʣ�µ��ܷ�����
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
