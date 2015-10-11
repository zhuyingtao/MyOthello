package chessFrame;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.io.DataInputStream;
import java.io.IOException;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ScrollPaneConstants;
/**
 * 此类用于显示游戏中的在线玩家
 * @author ZhuYingtao
 *
 */

@SuppressWarnings("serial")
public class NamePanel extends JPanel {
	@SuppressWarnings("unused")
	private ChessClient cc = null;
	private JTextArea ta =null; 
	private JScrollPane scr =null; 
    /**
     * 构造显示界面
     * @param cc
     */
	public NamePanel(ChessClient cc) {
		this.cc = cc;
		ta=new JTextArea(" ≈≈≈≈当前在线玩家≈≈≈≈ "+ "\n");
		ta.setBackground(new Color(143, 249, 254));
        ta.setFont(new Font("宋体",Font.PLAIN,12));
		ta.setLineWrap(false);
		ta.setEditable(false);
		scr=new JScrollPane(ta);
		scr.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
		scr.setPreferredSize(new Dimension(195, 140));
		this.add(scr);
		this.setBounds(572, 70, 200, 150);
		//this.send(cc.dos);

	}
    /**
     * 读取得到的玩家信息，并显示
     * @param dis
     */
	public void read(DataInputStream dis) {
		try {
			int nameLength = dis.readInt();
			ta.setText(" ≈≈≈≈当前在线玩家≈≈≈≈ " + "\n");
			for (int i = 0; i < nameLength; i++) {
				String nickname = dis.readUTF();
				int score=dis.readInt();
				String level=dis.readUTF();
				ta.setText(ta.getText() + nickname + "  分数："+score+"  级别："+level+"\n");
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
