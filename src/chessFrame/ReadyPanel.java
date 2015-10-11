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
 * ��������ʵ����Ϸ�е�׼���͵�����ϰ����
 * @author ZhuYingtao
 *
 */
@SuppressWarnings("serial")
public class ReadyPanel extends JPanel implements ActionListener {
	/**
	 * ʵ����'׼��'��ť
	 */
	JButton ready = new JButton("׼��");
    /**
     * ʵ����'������ϰ'��ť
     */
	JButton playWithCom=new JButton("������ϰ");

    private ChessClient cc;
	private ChessBoard cb;
	private boolean isWatcher = false;
	/**
	 * ����Ƿ�׼��
	 */
	boolean isReady = false;
	/**
	 * ������ʾ����
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
     * ���Ӱ�ť�����¼���ʵ��׼���͵�����ϰ����
     */
	public void actionPerformed(ActionEvent e) {
		if(e.getActionCommand().endsWith("������ϰ")){
			if(isReady==true){
				JOptionPane.showMessageDialog(this, "���Ѿ�׼���ˣ����ܽ��е�����ϰ��");
				return;
			}
			cb.computerPlay=true;
			cb.initChessBoard();
		    
		}
		if (isWatcher == true) {
			JOptionPane.showMessageDialog(this, "���ǹ�ս�ߣ�����ִ�д˲���!");
			return;
		}
		if (e.getActionCommand().equals("׼��")) {
			isReady = true;
			this.send(cc.dos);
			ready.setText("ȡ��׼��");
			cb.computerPlay=false;
			cb.canPlay=false;
			
		}
		if (e.getActionCommand().equals("ȡ��׼��")) {
			if (cb.canPlay == true) {
				JOptionPane.showMessageDialog(cb, "��Ϸ�Ѿ���ʼ������ȡ��׼��");
				return;
			}
			isReady = false;
			this.send(cc.dos);
			ready.setText("׼��");
		}
	}
    /**
     * ������Ϣ��������
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
