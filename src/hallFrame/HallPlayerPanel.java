package hallFrame;

import java.awt.Color;
import java.awt.Font;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.border.LineBorder;

import server.LoginServer;
import server.Server;
/**
 * ���������ڴ�����ʾ��ҵĸ�����Ϣ
 * @author ZhuYingtao
 *
 */
@SuppressWarnings("serial")
public class HallPlayerPanel extends JPanel {
	private JTextArea ta = new JTextArea();
    private Socket s = null;
	private DataOutputStream dos = null;
	private DataInputStream dis = null;
	/**
	 * ��ҵ��ǳ� 
	 */
	public static String nickname=null;
	/**
	 * ��ҵ��Ա�
	 */
	public static String sex=null;
	/**
	 * ��ҵ�ʤ����
	 */
	public static int win=0;
	/**
	 * ��ҵĸ�����
	 */
	public static int lose=0;
	/**
	 * ��ҵ�ƽ����
	 */
	public static int draw=0;
	/**
	 * ��ҵ���Ϸ����
	 */
	public static int score=0;
	/**
	 * ��ҵ���Ϸ�ȼ�
	 */
	public static String level=null;
    /**
     * ������ʾ����
     */
	public HallPlayerPanel() {
		this.setBorder(new LineBorder(Color.GRAY, 1));
		this.setBackground(Color.GREEN);
		this.setLayout(null);
		
		JLabel string=new JLabel("  ����������Ϣ�����");
		string.setForeground(Color.RED);
		string.setFont(new Font("����",Font.PLAIN,14));
		string.setBounds(5, 0, 200, 35);
		this.add(string);
		
		ImageIcon img=new ImageIcon("picture\\GameHall\\face2.jpg");
		JLabel face=new JLabel(img);
		face.setBounds(5, 50, 49, 49);
		this.add(face);
		
		ta.setEditable(false);
		ta.setBackground(Color.GREEN);
		ta.setBounds(68, 37, 120,90 );
		this.add(ta);
		
		this.setBounds(751, 1, 200, 144);
		this.getPlayerInfo();
	}
    /**
     * ���ӵ�¼���������Եõ���ҵľ�����Ϣ
     */
	public void getPlayerInfo() {
		try {
			s = new Socket(Server.IP, LoginServer.TCP_PORT);
			dos = new DataOutputStream(s.getOutputStream());
			dis = new DataInputStream(s.getInputStream());
			dos.writeUTF("getPlayer");// ���ͱ����Ϣ

			nickname = dis.readUTF();
			sex=dis.readUTF();
			win=dis.readInt();
			lose=dis.readInt();
			draw=dis.readInt();
			score=dis.readInt();
			if(score>=0&&score<100){
				level="����é®";
			}
			if(score>=100&&score<200){
				level="����СϺ";
			}
			if(score>=200&&score<300){
				level="����֮��";
			}
			if(score>=300&&score<400){
				level="�������";
			}
			if(score>=400&&score<500){
				level="��������";
			}
			if(score>=500&&score<1000){
				level="���޵���";
			}
			if(score>=-100&&score<0){
				level="1��";
			}
			if(score>=-200&&score<-100){
				level="2��";
			}
			if(score>=-300&&score<-200){
				level="3��";
			}
			
			ta.setText(ta.getText() + "�ǳƣ�   "+nickname+ "\n"+
					"ʤ��"+win+"    "+"����"+lose+"    "+"ƽ��"+draw+"    "+"\n"
					+"������   "+score+"    "+"\n"+"����   "+level);
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (dis != null) {
					dis.close();
				}
				if (dos != null) {
					dos.close();
				}
				s.close();
			} catch (IOException e) {
				e.printStackTrace();
			}

		}
	}

}
