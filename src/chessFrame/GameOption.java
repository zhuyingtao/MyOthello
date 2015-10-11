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
 * 此类用于实现一系列的游戏选项，包括悔棋，求和，认输，存储，计时等功能
 * 
 * @author ZhuYingtao
 * 
 */
public class GameOption implements Runnable {
	/**
	 * 最大游戏时间，默认为600秒，即10分钟
	 */
	int maxtime = 600;
	/**
	 * 单步最大游戏时间，默认为30秒
	 */
	int maxOneStepTime = 30;
	/**
	 * 黑方的单步时间
	 */
	int blackOneTime = 30; // 设定倒计时
	/**
	 * 白方的单步时间
	 */
	int whiteOneTime = 30;
	/**
	 * 黑方的最大时间
	 */
	int blacktime = 600; // 设定初始游戏总用时
	/**
	 * 白方的最大时间
	 */
	int whitetime = 600;

	private final int BLACK = 1;

	private final int WHITE = 2;

	private int answer = 3;
	private String time;
	private String oneTime;
	/**
	 * 显示黑方的计时信息
	 */
	String blackMessage = this.initTime(blacktime);
	/**
	 * 显示白方的计时信息
	 */
	String whiteMessage = this.initTime(whitetime);
	/**
	 * 标记是否画提示边框
	 */
	static boolean drawBorder = true;

	private int[][] allChess;

	private ChessBoard cb;

	private Game gc;

	private ChessClient cc;
	private ReadyPanel rp;
	private Voice v = new Voice();
	/**
	 * 实例化计时线程
	 */
	Thread t = new Thread(this);

	/**
	 * 启动计时线程
	 */
	public void start() {
		t.start();
	}

	/**
	 * 得到ChessBoard类的实例化对象
	 * 
	 * @param cb
	 */
	public void getChessBoard(ChessBoard cb) {
		this.cb = cb;
		this.allChess = cb.allChess;
	}

	/**
	 * 得到GameClient类的实例化对象
	 * 
	 * @param gc
	 */
	public void getGameClient(Game gc) {
		this.gc = gc;
		this.cc = gc.cc;
	}

	/**
	 * 得到ReadyPanel类的实例化对象
	 * 
	 * @param rp
	 */
	public void getReadyPanel(ReadyPanel rp) {
		this.rp = rp;
	}

	/**
	 * 事项'游戏'菜单内的一系列功能
	 * 
	 * @param regret
	 *            '悔棋选项'
	 * @param giveUp
	 *            '认输选项'
	 * @param draw
	 *            '求和选项'
	 */
	public void setGame(JMenuItem restart, JMenuItem regret, JMenuItem giveUp,
			JMenuItem draw) {
		restart.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if (cb.isWatcher == true) {
					JOptionPane.showMessageDialog(cb, "您是观战者，无法进行此操作！");
					return;
				}
				if (cb.computerPlay == true) {
					JOptionPane.showMessageDialog(cb, "不是吧，练习还悔棋？");
					return;
				}
				if (cb.canPlay == false) {
					JOptionPane.showMessageDialog(cb, "游戏已经结束，无法悔棋");
				} else {
					int result = JOptionPane.showConfirmDialog(cb, "是否重新开始游戏?");
					if (result == 0) {
						send(cc.dos, "restart");
					}
				}

			}
		});
		regret.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (cb.isWatcher == true) {
					JOptionPane.showMessageDialog(cb, "您是观战者，无法进行此操作！");
					return;
				}
				if (cb.computerPlay == true) {
					JOptionPane.showMessageDialog(cb, "不是吧，练习还悔棋？");
					return;
				}
				if (cb.canPlay == false) {
					JOptionPane.showMessageDialog(cb, "游戏已经结束，无法悔棋");
				} else {
					int result = JOptionPane.showConfirmDialog(cb, "是否悔棋?");
					if (result == 0) {// 点击确认，发送数据到服务器端
						send(cc.dos, "regret");
					}

				}
			}

		});

		giveUp.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (cb.isWatcher == true) {
					JOptionPane.showMessageDialog(cb, "您是观战者，无法进行此操作！");
					return;
				}
				if (cb.computerPlay == true) {
					JOptionPane.showMessageDialog(cb, "不是吧，练习还认输？");
					return;
				}
				if (cb.canPlay == false) {
					JOptionPane.showMessageDialog(cb, "游戏已经结束，无法放弃");

				} else {
					int result = JOptionPane.showConfirmDialog(cb, "是否认输？");
					if (result == 0) {
						send(cc.dos, "giveUp");
					}
					if (result == 1) {
						JOptionPane.showMessageDialog(cb, "请继续游戏");
					}
					cb.repaint();
				}
			}
		});

		draw.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (cb.isWatcher == true) {
					JOptionPane.showMessageDialog(cb, "您是观战者，无法进行此操作！");
					return;
				}
				if (cb.computerPlay == true) {
					JOptionPane.showMessageDialog(cb, "不是吧，练习还求和？");
					return;
				}
				if (cb.canPlay == false) {
					JOptionPane.showMessageDialog(cb, "游戏已经结束，无法求和");
				} else {
					int result = JOptionPane.showConfirmDialog(cb, "是否请求和棋？");
					if (result == 0) {
						JOptionPane.showMessageDialog(cb, "请求已经发出，请等待对方回应");
						send(cc.dos, "draw");
					}
					if (result == 1) {
						JOptionPane.showMessageDialog(cb, "请继续游戏");
					}
				}
			}
		});
	}

	/**
	 * 实现'帮助'菜单内的功能
	 * 
	 * @param gameInstructor
	 *            '游戏说明'
	 */
	public void setHelp(JMenuItem gameInstructor) {

		gameInstructor.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JOptionPane.showMessageDialog(cb, "这是一个黑白棋游戏程序，双方轮流下棋，游戏结束时，"
						+ "子数多的一方胜。");
			}
		});
	}

	/**
	 * 实现'选项'菜单内的一系列功能
	 * 
	 * @param cancelsug
	 *            '取消提示'
	 * @param addsug
	 *            '增加提示'
	 * @param wholeTime
	 *            '最大游戏时间'
	 * @param onestepTime
	 *            '单步游戏时间'
	 */
	public void setOption(JMenuItem cancelsug, JMenuItem addsug,
			JMenuItem wholeTime, JMenuItem onestepTime) {

		cancelsug.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int result = JOptionPane.showConfirmDialog(cb, "是否取消提示");
				if (result == 0) {
					drawBorder = false;
					JOptionPane.showMessageDialog(cb, "提示已经取消");
				}
				if (result == 1) {
					JOptionPane.showMessageDialog(cb, "请继续游戏");
				}
				cb.repaint();
			}
		});

		addsug.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int result = JOptionPane.showConfirmDialog(cb, "是否增加提示");
				if (result == 0) {
					drawBorder = true;
					JOptionPane.showMessageDialog(cb, "提示已经增加");
				}
				if (result == 1) {
					JOptionPane.showMessageDialog(cb, "请继续游戏");
				}
				cb.repaint();
			}
		});

		wholeTime.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (cb.isWatcher == true) {
					JOptionPane.showMessageDialog(cb, "您是观战者，无法进行此操作！");
					return;
				}
				if (cb.computerPlay == true) {
					JOptionPane.showMessageDialog(cb, "练习不能设定时间!");
					return;
				}
				if (cb.canPlay == true) {
					JOptionPane.showMessageDialog(cb, "游戏已经开始，无法进行此操作");
					return;
				}
				time = JOptionPane.showInputDialog("请输入游戏的最大时间（分钟）");
				if (Integer.parseInt(time) <= 0) {
					JOptionPane.showMessageDialog(cb, "请输入正确时间！");
				}
				send(cc.dos, "time");
				JOptionPane.showMessageDialog(cb, "请求已经发出，请等待对方回复");

			}
		});

		onestepTime.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (cb.isWatcher == true) {
					JOptionPane.showMessageDialog(cb, "您是观战者，无法进行此操作！");
					return;
				}
				if (cb.computerPlay == true) {
					JOptionPane.showMessageDialog(cb, "练习不能设定时间");
					return;
				}
				if (cb.canPlay == true) {
					JOptionPane.showMessageDialog(cb, "游戏已经开始，无法进行此操作");
					return;
				}
				oneTime = JOptionPane.showInputDialog("请输入游戏单步的最大时间（秒）");
				if (Integer.parseInt(oneTime) <= 0) {
					JOptionPane.showMessageDialog(cb, "请输入正确时间！");
				}
				send(cc.dos, "oneTime");
				JOptionPane.showMessageDialog(cb, "请求已经发出，请等待对方回复");

			}

		});

	}

	/**
	 * 实现'文件'菜单里的一系列功能
	 * 
	 * @param save
	 *            '保存'
	 * @param open
	 *            '读取'
	 */
	public void setDoc(JMenuItem save, JMenuItem open) {

		save.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				File file = null;
				JFileChooser fileChooser = new JFileChooser();
				fileChooser.setApproveButtonText("确定");
				fileChooser.setDialogTitle("保存文件");
				fileChooser.setCurrentDirectory(new File("SavaAndOpen"));
				int result = fileChooser.showOpenDialog(gc); // 显示选择框
				if (result == JFileChooser.APPROVE_OPTION) { // 选择的是确定按钮
					file = fileChooser.getSelectedFile(); // 得到选择的文件
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
				int result = JOptionPane.showConfirmDialog(cb, "是否继续上次游戏？");
				if (result == 0 && cb.computerPlay == false) {
					JOptionPane.showMessageDialog(cb, "请求已发出，请等待对方回复！");
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
	 * 实现对认输的处理
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
			rp.ready.setText("准备");
			rp.isReady = false;
			if (isplayer1 == true && isplayer2 == false) {
				JOptionPane.showMessageDialog(cb, "黑方已经认输，白方胜");
				cb.message = "黑方已经认输，白方胜";
				cb.whiteWin++;
			}
			if (isplayer2 == true && isplayer1 == false) {
				JOptionPane.showMessageDialog(cb, "白方已经认输，黑方胜");
				cb.message = "白方已经认输，黑方胜";
				cb.blackWin++;
			}

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	/**
	 * 实现对悔棋的处理
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
				cb.message = "轮到白方";
			} else {
				cb.isBlack = true;
				cb.message = "轮到黑方";
			}
			cb.isOpposite = false;
			cb.ch.countBlack = cb.ch.lastCountBlack;
			cb.ch.countWhite = cb.ch.lastCountWhite;
			cb.repaint();
			JOptionPane.showMessageDialog(cb, "对方同意了你的悔棋！");
		}
		if (answer != 0) {
			JOptionPane.showMessageDialog(cb, "对方不同意悔棋！");
		}
		answer = 3; // 初始化answer

	}

	/**
	 * 实现对求和的处理
	 */
	@SuppressWarnings("deprecation")
	public void drawAnswer() {
		if (answer == 0) {
			cb.message = "双方言和，平局！";
			cb.changeColor = true;
			cb.canPlay = false;
			cb.su.update(0, 0, HallPlayerPanel.nickname);
			rp.ready.setText("准备");
			rp.isReady = false;
			t.suspend();
			v.play("end");
			JOptionPane.showMessageDialog(cb, "对方同意了你的求和！");
		}
		if (answer != 0) {
			JOptionPane.showMessageDialog(cb, "对方拒绝了你的请求！");
		}
		answer = 3;
	}

	/**
	 * 实现对打开文件的处理
	 */
	public void openAnswer() {
		if (answer == 0) {
			cb.canPlay = true;
			File file = null;
			JFileChooser fileChooser = new JFileChooser();
			fileChooser.setApproveButtonText("确定");
			fileChooser.setDialogTitle("打开文件");
			fileChooser.setCurrentDirectory(new File("SavaAndOpen"));
			int result = fileChooser.showOpenDialog(gc); // 显示选择框
			if (result == JFileChooser.APPROVE_OPTION) { // 选择的是确定按钮
				file = fileChooser.getSelectedFile(); // 得到选择的文件
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
					if (cb.isBlack == false) { // 如果是和电脑下且轮到白子下
						cb.ch.playWithCom();
					}
					cb.changeColor = dis.readBoolean();
					cb.isStop = dis.readBoolean();
					if (cb.computerPlay == false) {// 如果不和电脑下，发送信息
						cb.send(cc.dos);
					}
					dis.close();
					cb.repaint();
				} catch (Exception e1) {
				}
			}
		}
		if (answer != 0) {
			JOptionPane.showMessageDialog(cb, "对方拒绝了你的请求！");
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
				JOptionPane.showMessageDialog(cb,"对方同意了你的请求！");
				cb.repaint();
			} catch (Exception e1) {
				e1.printStackTrace();
			}

		}
		if (answer != 0) {
			JOptionPane.showMessageDialog(cb, "对方拒绝了你的请求！");
		}
		answer = 3;

	}

	public void oneTimeAnswer() {
		if (answer == 0) {
			maxOneStepTime = Integer.parseInt(oneTime);
			blackOneTime = maxOneStepTime;
			whiteOneTime = maxOneStepTime;
			JOptionPane.showMessageDialog(cb,"对方同意了你的请求！");
			cb.repaint();
		}
		if (answer != 0) {
			JOptionPane.showMessageDialog(cb, "对方拒绝了你的请求！");
		}
		answer = 3;
	}

	/**
	 * 格式化游戏时间
	 * 
	 * @param time
	 *            以秒为单位的游戏时间
	 * @return 得到格式化后的时间
	 */
	public String initTime(int time) {
		int minutes = time / 60;
		int tenSeconds = (time % 60) / 10;
		int seconds = (time % 60) % 10;
		return minutes + ":" + tenSeconds + seconds;
	}

	/**
	 * 发送点击的请求，如悔棋，认输，求和等
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
	 * 读取收到的回复
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
	 * 实现计时功能
	 */
	@SuppressWarnings("deprecation")
	public void run() {
		try {
			Thread.sleep(1000); // 延迟1s
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
				JOptionPane.showMessageDialog(cb, "黑方时间到,白方胜");
				cb.message = "黑方时间到,白方胜";
				v.play("end");
				cb.changeColor = true;
				cb.canPlay = false;
				cb.whiteWin++;
				rp.ready.setText("准备");
				rp.isReady = false;
				t.suspend();
			}
			if (whiteOneTime == 0 || whitetime == 0) {
				JOptionPane.showMessageDialog(cb, "白方时间到，黑方胜");
				cb.message = "白方时间到，黑方胜";
				v.play("end");
				cb.changeColor = true;
				cb.canPlay = false;
				cb.blackWin++;
				rp.ready.setText("准备");
				rp.isReady = false;
				t.suspend();
			}

			cb.repaint();

		}
	}
}
