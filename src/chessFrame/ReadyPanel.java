package chessFrame;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataOutputStream;
import java.io.IOException;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
/**
 * 此类用于实现游戏中的准备和单人练习功能
 * @author ZhuYingtao
 *
 */
@SuppressWarnings("serial")
public class ReadyPanel extends JPanel implements ActionListener {
	/**
	 * 实例化'准备'按钮
	 */
	JButton ready = new JButton("准备");
    /**
     * 实例化'单人练习'按钮
     */
	JButton playWithCom=new JButton("单人练习");

    private ChessClient cc;
	private ChessBoard cb;
	private boolean isWatcher = false;
	/**
	 * 标记是否准备
	 */
	boolean isReady = false;
	/**
	 * 构造显示界面
	 * @param gc
	 */
    public ReadyPanel(Game gc) {
		this.isWatcher = gc.isWatcher;
		this.cc = gc.cc;
		this.cb = gc.cb;
		ready.setPreferredSize(new Dimension(90, 40));
		playWithCom.setPreferredSize(new Dimension(90, 40));
	    ready.addActionListener(this);
	    playWithCom.addActionListener(this);
        this.add(ready);
		this.add(playWithCom);
		this.setBounds(570, 10, 200, 60);
	}
    /**
     * 增加按钮处理事件，实现准备和单人练习功能
     */
	public void actionPerformed(ActionEvent e) {
		if(e.getActionCommand().endsWith("单人练习")){
			if(isReady==true){
				JOptionPane.showMessageDialog(this, "您已经准备了，不能进行单人练习！");
				return;
			}
			cb.computerPlay=true;
			cb.initChessBoard();
		    
		}
		if (isWatcher == true) {
			JOptionPane.showMessageDialog(this, "您是观战者，不能执行此操作!");
			return;
		}
		if (e.getActionCommand().equals("准备")) {
			isReady = true;
			this.send(cc.dos);
			ready.setText("取消准备");
			cb.computerPlay=false;
			cb.canPlay=false;
			
		}
		if (e.getActionCommand().equals("取消准备")) {
			if (cb.canPlay == true) {
				JOptionPane.showMessageDialog(cb, "游戏已经开始，不能取消准备");
				return;
			}
			isReady = false;
			this.send(cc.dos);
			ready.setText("准备");
		}
	}
    /**
     * 发送信息到服务器
     * @param dos
     */
	public void send(DataOutputStream dos) {
		try {
			dos.writeUTF("ready");
			dos.writeBoolean(cc.isPlayer1);
			dos.writeBoolean(cc.isPlayer2);
			dos.writeBoolean(isReady);
System.out.println("ready send");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
