package hallFrame;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import chessFrame.Game;
/**
 * 通过此类实现进入游戏的功能
 * @author ZhuYingtao
 *
 */

@SuppressWarnings("serial")

public class Room extends JPanel {
	private ImageIcon chairOff = new ImageIcon("picture\\GameHall\\chairOff.jpg");
	private ImageIcon chairOn = new ImageIcon("picture\\GameHall\\chairOn.jpg");
	private ImageIcon deskOn = new ImageIcon("picture\\GameHall\\deskOn.jpg");
	private ImageIcon deskOff = new ImageIcon("picture\\GameHall\\deskOff.jpg");
	
   /**
    * 检测是否能进入游戏
    */
	public static boolean canEnter = true;
    /**
     * 房间桌子的总数
     */
	int totalDesk = 50;
	/**
	 * 各个桌子的类
	 */
	DeskPanel[] deskPanels = new DeskPanel[totalDesk];
	/**
	 * 各个椅子的状态，如果是0则未被占用，如果是1表示已被占用
	 */
	int[][] chairStates = new int[totalDesk][2];
	
	private HallClient hc = null;
	/**
	 * 构造房间
	 * @param hc 大厅服务器的引用
	 */
	public Room(HallClient hc) {
		this.hc = hc;
		this.setLayout(new GridLayout(10, 5));
		
		for (int i = 0; i < totalDesk; i++) {
			deskPanels[i] = new DeskPanel(i);
			this.add(deskPanels[i]);
		}
		try {
			hc.dos.writeUTF("enterRoom");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	class DeskPanel extends JPanel implements ActionListener {
		JButton[] chairs = new JButton[2];
		JLabel deskLabel;
		int tableNo;

		public DeskPanel(int number) {
			this.setLayout(new FlowLayout());
			this.setPreferredSize(new Dimension(145, 130));
			tableNo = number;
			this.setBackground(new Color(81, 113, 158)); // 自定义颜色
			chairs[0] = new JButton(chairOff);
			chairs[1] = new JButton(chairOff);
			chairs[0].setToolTipText("点击进入");
			chairs[1].setToolTipText("点击进入");
			chairs[0].addActionListener(this);
			chairs[1].addActionListener(this);
			deskLabel = new JLabel("- " + (number + 1) + " -", deskOff,
					JLabel.CENTER);
			deskLabel.setForeground(Color.WHITE);
			deskLabel.setVerticalTextPosition(JLabel.BOTTOM);
			deskLabel.setHorizontalTextPosition(JLabel.CENTER);
			chairs[0].setPreferredSize(new Dimension(32, 32));
			chairs[1].setPreferredSize(new Dimension(32, 32));
			add(chairs[0]);
			add(deskLabel);
			add(chairs[1]);
		}

		public void actionPerformed(ActionEvent e) {
			if (canEnter == false) {
				JOptionPane.showMessageDialog(this, "您已经进入到游戏中去了," 
						+ "不能重复进入！");
			}
			if (canEnter == true) {
				send(hc.dos);// 发送到服务器端是否开启Game服务器
				if (e.getSource() == chairs[0]) {
					if (chairStates[tableNo][0] == 1) {
						if (chairStates[tableNo][1] == 0) {
							JOptionPane.showMessageDialog(this,
									"对方正在等你呢，请选择对面座位!");
							return;
						} else {
							int result = JOptionPane.showConfirmDialog(this,
									"此位已经有人了!是否进行观战?");
							if (result == 1 || result == 2) {
								return;
							} else {
								new Game(hc, tableNo, 0, true);
							}
						}
					} else {
						send(hc.dos, 0);
						new Game(hc, tableNo, 0, false);
					}
				}
				if (e.getSource() == chairs[1]) {
					if (chairStates[tableNo][1] == 1) {
						if (chairStates[tableNo][0] == 0) {
							JOptionPane.showMessageDialog(this,
									"对方正在等你呢，请选择对面座位!");
							return;
						}
						int result = JOptionPane.showConfirmDialog(this,
								"此位已经有人了!是否进行观战?");
						if (result == 1 || result == 2) {
							return;
						} else {
							new Game(hc, tableNo, 1, true);
						}

					} else {
						send(hc.dos, 1);
						new Game(hc, tableNo, 1, false);
					}
				}
				canEnter = false;
			}
		}

		public void send(DataOutputStream dos) {
			try {
				dos.writeUTF("firstEnter");
				dos.writeInt(tableNo);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		public void send(DataOutputStream dos, int chairNo) {
			try {
				dos.writeUTF("sit");
				dos.writeInt(tableNo);
				dos.writeInt(chairNo);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}
	/**
	 * 读取大厅服务器发来的信息，并显示各个Chair的状态
	 * @param dis 大厅客户端DataInputStream的引用
	 */
    public void read(DataInputStream dis) {
		try {
			for (int i = 0; i < totalDesk; i++) {
				for (int j = 0; j < 2; j++) {
					chairStates[i][j] = dis.readInt();
					if (chairStates[i][j] == 1) {
						deskPanels[i].chairs[j].setIcon(chairOn);
						deskPanels[i].deskLabel.setIcon(deskOn);
					}
					if (chairStates[i][j] == 0) {
						deskPanels[i].chairs[j].setIcon(chairOff);
					}
					if (chairStates[i][0] == 0 && chairStates[i][1] == 0) {
						deskPanels[i].deskLabel.setIcon(deskOff);
					}
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
