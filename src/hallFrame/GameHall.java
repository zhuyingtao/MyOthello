package hallFrame;

import java.awt.Color;
import java.awt.Container;
import java.awt.Toolkit;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.border.LineBorder;
/**
 * ʵ����Ϸ�����Ĺ��ܣ��������䣬�����Ϣ��������ң�����ȹ���
 * @author ZhuYingtao
 *
 */
@SuppressWarnings("serial")
public class GameHall extends JFrame {
	
	private Toolkit tk = Toolkit.getDefaultToolkit();
    /**
     * �����Ļ�Ŀ��
     */
	int width = tk.getScreenSize().width;
    /**
     * �����Ļ�ĸ߶�
     */
	int height = tk.getScreenSize().height;

	private Room room = null;
	private HallChatClient hcc = null;
    private	HallPlayerPanel hpp = null;
	private HallNamePanel hnp = null;
	private HallClient hc=null;
	private JScrollPane jsp=null;
    /**
     * ������Ϸ����
     */
	public GameHall() {
        hc=new HallClient();
        hpp = new HallPlayerPanel();
        room = new Room(hc);
		hc.getRoom(room);
		hnp = new HallNamePanel(hc);
        hc.getHallNamePanel(hnp);
		hcc = new HallChatClient(this);
		new Thread(hc).start();
	    
		jsp=new JScrollPane(room,ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS,
				ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		jsp.setBounds(0,0,750,590);
		jsp.setBorder(new LineBorder(new Color(81,113,158)));
		
		this.setTitle("��Ϸ����");
		this.setSize(960, 620);
		this.setLocation(width / 2 - 480, height / 2 - 310);
		this.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		this.setLayout(null);
		this.setResizable(false);
		Container con=this.getContentPane();
		con.setBackground(new Color(81, 113, 158));
		
		this.add(jsp);
		this.add(hcc);
		this.add(hnp);
		this.add(hpp);
		
		this.setVisible(true);

		this.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				try {
					hc.dos.writeUTF("exit");
					hcc.dos.writeUTF(HallPlayerPanel.nickname + "�˳���!");
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}

		});

	}
}
