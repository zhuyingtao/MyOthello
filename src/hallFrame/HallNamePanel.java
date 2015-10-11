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
 * ����������ʾ�����е��������
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
     * ������ʾ����
     * @param hc
     */
	public HallNamePanel(HallClient hc) {
		this.hc=hc;
		ta= new JTextArea("   �֡֡֡֡���ǰ������ҡ��֡֡֡�" + "\n");
		ta.setBackground(new Color(163, 245, 250));
		ta.setForeground(Color.BLUE);
        ta.setFont(new Font("����",Font.PLAIN,12));
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
     * �������ݵ�����������
     * @param dos
     */
	public void send(DataOutputStream dos) {
		try {
			dos.writeUTF("nickname");
			dos.writeUTF(HallPlayerPanel.nickname);// �����ǳƵ���������
			dos.writeUTF(HallPlayerPanel.score+"");
			dos.writeUTF(HallPlayerPanel.level);
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
	/**
	 * ��ȡ�ӷ������˷���������
	 * @param dis
	 */
    public void read(DataInputStream dis){
    	try {
			int nameLength = dis.readInt();
			ta.setText("�ֵ֡֡֡�ǰ������ҡ֡֡֡�" +"\n");
			for (int i = 0; i < nameLength; i++) {
				String nickname = dis.readUTF();
				int score=dis.readInt();
				String level=dis.readUTF();
				ta.setText(ta.getText() + nickname +"  "+
						"������"+score+"  "+"����"+level+"\n");
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
    }
	
}