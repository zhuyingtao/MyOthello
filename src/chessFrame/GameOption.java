package chessFrame;

import hallFrame.HallPlayerPanel;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.swing.JFileChooser;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;

/**
 * ��������ʵ��һϵ�е���Ϸѡ��������壬��ͣ����䣬�洢����ʱ�ȹ���
 * 
 * @author ZhuYingtao
 * 
 */
public class GameOption implements Runnable {
	/**
	 * �����Ϸʱ�䣬Ĭ��Ϊ600�룬��10����
	 */
	int maxtime = 600;
	/**
	 * ���������Ϸʱ�䣬Ĭ��Ϊ30��
	 */
	int maxOneStepTime = 30;
	/**
	 * �ڷ��ĵ���ʱ��
	 */
	int blackOneTime = 30; // �趨����ʱ
	/**
	 * �׷��ĵ���ʱ��
	 */
	int whiteOneTime = 30;
	/**
	 * �ڷ������ʱ��
	 */
	int blacktime = 600; // �趨��ʼ��Ϸ����ʱ
	/**
	 * �׷������ʱ��
	 */
	int whitetime = 600;

	private final int BLACK = 1;

	private final int WHITE = 2;

	private int answer = 3;
	private String time;
	private String oneTime;
	/**
	 * ��ʾ�ڷ��ļ�ʱ��Ϣ
	 */
	String blackMessage = this.initTime(blacktime);
	/**
	 * ��ʾ�׷��ļ�ʱ��Ϣ
	 */
	String whiteMessage = this.initTime(whitetime);
	/**
	 * ����Ƿ���ʾ�߿�
	 */
	static boolean drawBorder = true;

	private int[][] allChess;

	private ChessBoard cb;

	private Game gc;

	private ChessClient cc;
	private ReadyPanel rp;
	private Voice v = new Voice();
	/**
	 * ʵ������ʱ�߳�
	 */
	Thread t = new Thread(this);

	/**
	 * ������ʱ�߳�
	 */
	public void start() {
		t.start();
	}

	/**
	 * �õ�ChessBoard���ʵ��������
	 * 
	 * @param cb
	 */
	public void getChessBoard(ChessBoard cb) {
		this.cb = cb;
		this.allChess = cb.allChess;
	}

	/**
	 * �õ�GameClient���ʵ��������
	 * 
	 * @param gc
	 */
	public void getGameClient(Game gc) {
		this.gc = gc;
		this.cc = gc.cc;
	}

	/**
	 * �õ�ReadyPanel���ʵ��������
	 * 
	 * @param rp
	 */
	public void getReadyPanel(ReadyPanel rp) {
		this.rp = rp;
	}

	/**
	 * ����'��Ϸ'�˵��ڵ�һϵ�й���
	 * 
	 * @param regret
	 *            '����ѡ��'
	 * @param giveUp
	 *            '����ѡ��'
	 * @param draw
	 *            '���ѡ��'
	 */
	public void setGame(JMenuItem restart, JMenuItem regret, JMenuItem giveUp,
			JMenuItem draw) {
		restart.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if (cb.isWatcher == true) {
					JOptionPane.showMessageDialog(cb, "���ǹ�ս�ߣ��޷����д˲�����");
					return;
				}
				if (cb.computerPlay == true) {
					JOptionPane.showMessageDialog(cb, "���ǰɣ���ϰ�����壿");
					return;
				}
				if (cb.canPlay == false) {
					JOptionPane.showMessageDialog(cb, "��Ϸ�Ѿ��������޷�����");
				} else {
					int result = JOptionPane.showConfirmDialog(cb, "�Ƿ����¿�ʼ��Ϸ?");
					if (result == 0) {
						send(cc.dos, "restart");
					}
				}

			}
		});
		regret.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (cb.isWatcher == true) {
					JOptionPane.showMessageDialog(cb, "���ǹ�ս�ߣ��޷����д˲�����");
					return;
				}
				if (cb.computerPlay == true) {
					JOptionPane.showMessageDialog(cb, "���ǰɣ���ϰ�����壿");
					return;
				}
				if (cb.canPlay == false) {
					JOptionPane.showMessageDialog(cb, "��Ϸ�Ѿ��������޷�����");
				} else {
					int result = JOptionPane.showConfirmDialog(cb, "�Ƿ����?");
					if (result == 0) {// ���ȷ�ϣ��������ݵ���������
						send(cc.dos, "regret");
					}

				}
			}

		});

		giveUp.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (cb.isWatcher == true) {
					JOptionPane.showMessageDialog(cb, "���ǹ�ս�ߣ��޷����д˲�����");
					return;
				}
				if (cb.computerPlay == true) {
					JOptionPane.showMessageDialog(cb, "���ǰɣ���ϰ�����䣿");
					return;
				}
				if (cb.canPlay == false) {
					JOptionPane.showMessageDialog(cb, "��Ϸ�Ѿ��������޷�����");

				} else {
					int result = JOptionPane.showConfirmDialog(cb, "�Ƿ����䣿");
					if (result == 0) {
						send(cc.dos, "giveUp");
					}
					if (result == 1) {
						JOptionPane.showMessageDialog(cb, "�������Ϸ");
					}
					cb.repaint();
				}
			}
		});

		draw.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (cb.isWatcher == true) {
					JOptionPane.showMessageDialog(cb, "���ǹ�ս�ߣ��޷����д˲�����");
					return;
				}
				if (cb.computerPlay == true) {
					JOptionPane.showMessageDialog(cb, "���ǰɣ���ϰ����ͣ�");
					return;
				}
				if (cb.canPlay == false) {
					JOptionPane.showMessageDialog(cb, "��Ϸ�Ѿ��������޷����");
				} else {
					int result = JOptionPane.showConfirmDialog(cb, "�Ƿ�������壿");
					if (result == 0) {
						JOptionPane.showMessageDialog(cb, "�����Ѿ���������ȴ��Է���Ӧ");
						send(cc.dos, "draw");
					}
					if (result == 1) {
						JOptionPane.showMessageDialog(cb, "�������Ϸ");
					}
				}
			}
		});
	}

	/**
	 * ʵ��'����'�˵��ڵĹ���
	 * 
	 * @param gameInstructor
	 *            '��Ϸ˵��'
	 */
	public void setHelp(JMenuItem gameInstructor) {

		gameInstructor.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JOptionPane.showMessageDialog(cb, "����һ���ڰ�����Ϸ����˫���������壬��Ϸ����ʱ��"
						+ "�������һ��ʤ��");
			}
		});
	}

	/**
	 * ʵ��'ѡ��'�˵��ڵ�һϵ�й���
	 * 
	 * @param cancelsug
	 *            'ȡ����ʾ'
	 * @param addsug
	 *            '������ʾ'
	 * @param wholeTime
	 *            '�����Ϸʱ��'
	 * @param onestepTime
	 *            '������Ϸʱ��'
	 */
	public void setOption(JMenuItem cancelsug, JMenuItem addsug,
			JMenuItem wholeTime, JMenuItem onestepTime) {

		cancelsug.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int result = JOptionPane.showConfirmDialog(cb, "�Ƿ�ȡ����ʾ");
				if (result == 0) {
					drawBorder = false;
					JOptionPane.showMessageDialog(cb, "��ʾ�Ѿ�ȡ��");
				}
				if (result == 1) {
					JOptionPane.showMessageDialog(cb, "�������Ϸ");
				}
				cb.repaint();
			}
		});

		addsug.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int result = JOptionPane.showConfirmDialog(cb, "�Ƿ�������ʾ");
				if (result == 0) {
					drawBorder = true;
					JOptionPane.showMessageDialog(cb, "��ʾ�Ѿ�����");
				}
				if (result == 1) {
					JOptionPane.showMessageDialog(cb, "�������Ϸ");
				}
				cb.repaint();
			}
		});

		wholeTime.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (cb.isWatcher == true) {
					JOptionPane.showMessageDialog(cb, "���ǹ�ս�ߣ��޷����д˲�����");
					return;
				}
				if (cb.computerPlay == true) {
					JOptionPane.showMessageDialog(cb, "��ϰ�����趨ʱ��!");
					return;
				}
				if (cb.canPlay == true) {
					JOptionPane.showMessageDialog(cb, "��Ϸ�Ѿ���ʼ���޷����д˲���");
					return;
				}
				time = JOptionPane.showInputDialog("��������Ϸ�����ʱ�䣨���ӣ�");
				if (Integer.parseInt(time) <= 0) {
					JOptionPane.showMessageDialog(cb, "��������ȷʱ�䣡");
				}
				send(cc.dos, "time");
				JOptionPane.showMessageDialog(cb, "�����Ѿ���������ȴ��Է��ظ�");

			}
		});

		onestepTime.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (cb.isWatcher == true) {
					JOptionPane.showMessageDialog(cb, "���ǹ�ս�ߣ��޷����д˲�����");
					return;
				}
				if (cb.computerPlay == true) {
					JOptionPane.showMessageDialog(cb, "��ϰ�����趨ʱ��");
					return;
				}
				if (cb.canPlay == true) {
					JOptionPane.showMessageDialog(cb, "��Ϸ�Ѿ���ʼ���޷����д˲���");
					return;
				}
				oneTime = JOptionPane.showInputDialog("��������Ϸ���������ʱ�䣨�룩");
				if (Integer.parseInt(oneTime) <= 0) {
					JOptionPane.showMessageDialog(cb, "��������ȷʱ�䣡");
				}
				send(cc.dos, "oneTime");
				JOptionPane.showMessageDialog(cb, "�����Ѿ���������ȴ��Է��ظ�");

			}

		});

	}

	/**
	 * ʵ��'�ļ�'�˵����һϵ�й���
	 * 
	 * @param save
	 *            '����'
	 * @param open
	 *            '��ȡ'
	 */
	public void setDoc(JMenuItem save, JMenuItem open) {

		save.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				File file = null;
				JFileChooser fileChooser = new JFileChooser();
				fileChooser.setApproveButtonText("ȷ��");
				fileChooser.setDialogTitle("�����ļ�");
				fileChooser.setCurrentDirectory(new File("SavaAndOpen"));
				int result = fileChooser.showOpenDialog(gc); // ��ʾѡ���
				if (result == JFileChooser.APPROVE_OPTION) { // ѡ�����ȷ����ť
					file = fileChooser.getSelectedFile(); // �õ�ѡ����ļ�
					cb.repaint();
				}
				if (file != null) {
					try {
						DataOutputStream dos = new DataOutputStream(
								new FileOutputStream(file));
						for (int i = 0; i < 8; i++) {
							for (int j = 0; j < 8; j++) {
								dos.writeInt(allChess[i][j]);
							}
						}
						dos.writeUTF(cb.message);
						dos.writeBoolean(cb.isBlack);
						dos.writeBoolean(cb.changeColor);
						dos.writeBoolean(cb.isStop);
						dos.close();
					} catch (Exception e1) {
						e1.printStackTrace();
					}
				}
			}
		});

		open.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int result = JOptionPane.showConfirmDialog(cb, "�Ƿ�����ϴ���Ϸ��");
				if (result == 0 && cb.computerPlay == false) {
					JOptionPane.showMessageDialog(cb, "�����ѷ�������ȴ��Է��ظ���");
					send(cc.dos, "open");
				}
				if (result == 0 && cb.computerPlay == true) {
					answer = 0;
					openAnswer();
				}

			}
		});

	}

	/**
	 * ʵ�ֶ�����Ĵ���
	 * 
	 * @param dis
	 */
	@SuppressWarnings("deprecation")
	public void giveUpAnswer(DataInputStream dis) {
		try {
			boolean isplayer1 = dis.readBoolean();
			boolean isplayer2 = dis.readBoolean();
			cb.changeColor = true;
			cb.canPlay = false;
			v.play("end");
			t.suspend();
			rp.ready.setText("׼��");
			rp.isReady = false;
			if (isplayer1 == true && isplayer2 == false) {
				JOptionPane.showMessageDialog(cb, "�ڷ��Ѿ����䣬�׷�ʤ");
				cb.message = "�ڷ��Ѿ����䣬�׷�ʤ";
				cb.whiteWin++;
			}
			if (isplayer2 == true && isplayer1 == false) {
				JOptionPane.showMessageDialog(cb, "�׷��Ѿ����䣬�ڷ�ʤ");
				cb.message = "�׷��Ѿ����䣬�ڷ�ʤ";
				cb.blackWin++;
			}

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	/**
	 * ʵ�ֶԻ���Ĵ���
	 */
	public void regretAnswer() {
		if (answer == 0) {
			for (int i = 0; i < 8; i++) {
				for (int j = 0; j < 8; j++) {
					if (cb.regChess[i][j] == 2) {
						cb.allChess[i][j] = 0;
					}
					if (cb.regChess[i][j] == 1) {
						if (cb.allChess[i][j] == BLACK) {
							cb.allChess[i][j] = WHITE;
						} else {
							cb.allChess[i][j] = BLACK;
						}
					}
				}
			}
			if (cb.isBlack == true) {
				cb.isBlack = false;
				cb.message = "�ֵ��׷�";
			} else {
				cb.isBlack = true;
				cb.message = "�ֵ��ڷ�";
			}
			cb.isOpposite = false;
			cb.ch.countBlack = cb.ch.lastCountBlack;
			cb.ch.countWhite = cb.ch.lastCountWhite;
			cb.repaint();
			JOptionPane.showMessageDialog(cb, "�Է�ͬ������Ļ��壡");
		}
		if (answer != 0) {
			JOptionPane.showMessageDialog(cb, "�Է���ͬ����壡");
		}
		answer = 3; // ��ʼ��answer

	}

	/**
	 * ʵ�ֶ���͵Ĵ���
	 */
	@SuppressWarnings("deprecation")
	public void drawAnswer() {
		if (answer == 0) {
			cb.message = "˫���Ժͣ�ƽ�֣�";
			cb.changeColor = true;
			cb.canPlay = false;
			cb.su.update(0, 0, HallPlayerPanel.nickname);
			rp.ready.setText("׼��");
			rp.isReady = false;
			t.suspend();
			v.play("end");
			JOptionPane.showMessageDialog(cb, "�Է�ͬ���������ͣ�");
		}
		if (answer != 0) {
			JOptionPane.showMessageDialog(cb, "�Է��ܾ����������");
		}
		answer = 3;
	}

	/**
	 * ʵ�ֶԴ��ļ��Ĵ���
	 */
	public void openAnswer() {
		if (answer == 0) {
			cb.canPlay = true;
			File file = null;
			JFileChooser fileChooser = new JFileChooser();
			fileChooser.setApproveButtonText("ȷ��");
			fileChooser.setDialogTitle("���ļ�");
			fileChooser.setCurrentDirectory(new File("SavaAndOpen"));
			int result = fileChooser.showOpenDialog(gc); // ��ʾѡ���
			if (result == JFileChooser.APPROVE_OPTION) { // ѡ�����ȷ����ť
				file = fileChooser.getSelectedFile(); // �õ�ѡ����ļ�
				cb.repaint();
			}
			if (file != null) {
				try {
					DataInputStream dis = new DataInputStream(
							new FileInputStream(file));
					for (int i = 0; i < 8; i++) {
						for (int j = 0; j < 8; j++) {
							allChess[i][j] = dis.readInt();
						}
					}
					cb.message = dis.readUTF();
					cb.isBlack = dis.readBoolean();
					if (cb.isBlack == false) { // ����Ǻ͵��������ֵ�������
						cb.ch.playWithCom();
					}
					cb.changeColor = dis.readBoolean();
					cb.isStop = dis.readBoolean();
					if (cb.computerPlay == false) {// ������͵����£�������Ϣ
						cb.send(cc.dos);
					}
					dis.close();
					cb.repaint();
				} catch (Exception e1) {
				}
			}
		}
		if (answer != 0) {
			JOptionPane.showMessageDialog(cb, "�Է��ܾ����������");
		}
		answer = 3;

	}

	public void timeAnswer() {
		if (answer == 0) {
			try {
				maxtime = Integer.parseInt(time) * 60;
				blacktime=maxtime;
				whitetime=maxtime;
				this.blackMessage=this.initTime(blacktime);
				this.whiteMessage=this.initTime(whitetime);
				JOptionPane.showMessageDialog(cb,"�Է�ͬ�����������");
				cb.repaint();
			} catch (Exception e1) {
				e1.printStackTrace();
			}

		}
		if (answer != 0) {
			JOptionPane.showMessageDialog(cb, "�Է��ܾ����������");
		}
		answer = 3;

	}

	public void oneTimeAnswer() {
		if (answer == 0) {
			maxOneStepTime = Integer.parseInt(oneTime);
			blackOneTime = maxOneStepTime;
			whiteOneTime = maxOneStepTime;
			JOptionPane.showMessageDialog(cb,"�Է�ͬ�����������");
			cb.repaint();
		}
		if (answer != 0) {
			JOptionPane.showMessageDialog(cb, "�Է��ܾ����������");
		}
		answer = 3;
	}

	/**
	 * ��ʽ����Ϸʱ��
	 * 
	 * @param time
	 *            ����Ϊ��λ����Ϸʱ��
	 * @return �õ���ʽ�����ʱ��
	 */
	public String initTime(int time) {
		int minutes = time / 60;
		int tenSeconds = (time % 60) / 10;
		int seconds = (time % 60) % 10;
		return minutes + ":" + tenSeconds + seconds;
	}

	/**
	 * ���͵������������壬���䣬��͵�
	 * 
	 * @param dos
	 * @param option
	 */
	public void send(DataOutputStream dos, String option) {
		try {
			dos.writeUTF(option);
			dos.writeBoolean(cc.isPlayer1);
			dos.writeBoolean(cc.isPlayer2);
			if (option.equals("time")) {
				dos.writeUTF(time);
			}
			if (option.equals("oneTime")) {
				dos.writeUTF(oneTime);
			}
			System.out.println("option send!");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * ��ȡ�յ��Ļظ�
	 * 
	 * @param dis
	 */
	public void read(DataInputStream dis) {
		try {
			answer = dis.readInt();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * ʵ�ּ�ʱ����
	 */
	@SuppressWarnings("deprecation")
	public void run() {
		try {
			Thread.sleep(1000); // �ӳ�1s
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}
		while (true) {
			if (blacktime > 0 && whitetime > 0 && blackOneTime > 0
					&& whiteOneTime > 0) {
				if (cb.isBlack) {
					whiteOneTime = maxOneStepTime;
					blacktime--;
					blackOneTime--;
				} else {
					blackOneTime = maxOneStepTime;
					whitetime--;
					whiteOneTime--;
				}
				blackMessage = this.initTime(blacktime);
				whiteMessage = this.initTime(whitetime);
				if (blackOneTime < 5 || whiteOneTime < 5) {
					v.play("clock");
				}
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			if (blackOneTime == 0 || blacktime == 0) {
				JOptionPane.showMessageDialog(cb, "�ڷ�ʱ�䵽,�׷�ʤ");
				cb.message = "�ڷ�ʱ�䵽,�׷�ʤ";
				v.play("end");
				cb.changeColor = true;
				cb.canPlay = false;
				cb.whiteWin++;
				rp.ready.setText("׼��");
				rp.isReady = false;
				t.suspend();
			}
			if (whiteOneTime == 0 || whitetime == 0) {
				JOptionPane.showMessageDialog(cb, "�׷�ʱ�䵽���ڷ�ʤ");
				cb.message = "�׷�ʱ�䵽���ڷ�ʤ";
				v.play("end");
				cb.changeColor = true;
				cb.canPlay = false;
				cb.blackWin++;
				rp.ready.setText("׼��");
				rp.isReady = false;
				t.suspend();
			}

			cb.repaint();

		}
	}
}
