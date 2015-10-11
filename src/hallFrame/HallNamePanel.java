package hallFrame;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ScrollPaneConstants;
/**
 * 此类用于显示大厅中的在线玩家
 * @author ZhuYingtao
 *
 */

@SuppressWarnings("serial")
public class HallNamePanel extends JPanel{
	private JTextArea ta =null;
	private JScrollPane scr =null;
	@SuppressWarnings("unused")
	private HallClient hc=null;
    /**
     * 构造显示界面
     * @param hc
     */
	public HallNamePanel(HallClient hc) {
		this.hc=hc;
		ta= new JTextArea("   ≈≈≈≈※当前在线玩家※≈≈≈≈" + "\n");
		ta.setBackground(new Color(163, 245, 250));
		ta.setForeground(Color.BLUE);
        ta.setFont(new Font("宋体",Font.PLAIN,12));
        ta.setLineWrap(false);
		ta.setEditable(false);
		scr=new JScrollPane(ta);
		scr.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
		scr.setPreferredSize(new Dimension(200, 140));
		this.add(scr);
		this.setBounds(752, 145, 200, 150);
		this.send(hc.dos);

	}
    /**
     * 发送数据到大厅服务器
     * @param dos
     */
	public void send(DataOutputStream dos) {
		try {
			dos.writeUTF("nickname");
			dos.writeUTF(HallPlayerPanel.nickname);// 发送昵称到服务器端
			dos.writeUTF(HallPlayerPanel.score+"");
			dos.writeUTF(HallPlayerPanel.level);
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
	/**
	 * 读取从服务器端发来的数据
	 * @param dis
	 */
    public void read(DataInputStream dis){
    	try {
			int nameLength = dis.readInt();
			ta.setText("≈≈≈≈当前在线玩家≈≈≈≈" +"\n");
			for (int i = 0; i < nameLength; i++) {
				String nickname = dis.readUTF();
				int score=dis.readInt();
				String level=dis.readUTF();
				ta.setText(ta.getText() + nickname +"  "+
						"分数："+score+"  "+"级别："+level+"\n");
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
    }
	
}