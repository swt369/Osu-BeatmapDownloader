package com.swt369;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

//���ڻ�ȡ��Ҫץȡ��ҳ��
public class DialogForPageCount extends JDialog implements ActionListener{
	
	//ҳ��
	private int pagestart = -1;
	private int pageend = -1;
	private JTextField tf;
	private JTextField tf2;
	private JTextField tf3;
	private JTextField tf4;
	private JPanel jp;
	private JButton jb;
	
	//���캯���������븸����
	public DialogForPageCount(JFrame frame){
		//���๹�캯��
		super(frame,"DiaLog",true);
		
		//�����ı�
		tf = new JTextField("ץȡҳ��:");
		tf.setEditable(false);
		tf.setHorizontalAlignment(JTextField.CENTER);
		
		//�����ı����������뿪ʼҳ��
		tf2 = new JTextField("1");
		tf2.setHorizontalAlignment(JTextField.CENTER);
		
		//�����ı�
		tf3 = new JTextField("~");
		tf3.setEditable(false);
		tf3.setHorizontalAlignment(JTextField.CENTER);
		
		//�����ı�������������ֹҳ��
		tf4 = new JTextField("1");
		tf4.setHorizontalAlignment(JTextField.CENTER);
		
		//��壬������������ı�
		jp = new JPanel();
		jp.setLayout(new GridLayout(1,4));
		jp.add(tf);
		jp.add(tf2);
		jp.add(tf3);
		jp.add(tf4);
		
		//ȷ����ť
		jb = new JButton("ȷ��");
		jb.addActionListener(this);
		this.setLayout(new GridLayout(2,1));
		this.add(jp);
		this.add(jb);
		
		this.setBounds
		((int)(frame.getBounds().getMinX()+Settings.DIALOG_OFFSET_X)
				,(int)(frame.getBounds().getMinY()+Settings.DIALOG_OFFSET_Y)
				,Settings.DIALOG_WIDTH,Settings.DIALOG_HEIGHT);
		this.setVisible(true);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO �Զ����ɵķ������
		if(e.getSource() == jb){
			this.pagestart = textField2int(tf2);
			this.pageend = textField2int(tf4);
			if(pagestart > pageend){
				JOptionPane.showMessageDialog(null,
						"������", "��ʼҳ�Ų��ܴ��ڽ���ҳ��", JOptionPane.ERROR_MESSAGE);
				return;
			}
			if(pagestart > 0 && pageend > 0){
				this.setVisible(false);
			}
		}
	}
	
	//��ָ���ı�������ת��Ϊ����
	private int textField2int(JTextField tf){
		String str = tf.getText();
		int sum = 0;
		for(int i=0;i<str.length();i++){
			if(!(str.charAt(i)>='0' && str.charAt(i)<='9')){
				JOptionPane.showMessageDialog(null,
						"������", "����������", JOptionPane.ERROR_MESSAGE);
				return -1;
			}
			sum = sum*10;
			sum += str.charAt(i)-'0';
		}
		return sum;
	}
	
	//��ȡ��ʼҳ
	public int getPageStart(){
		return pagestart;
	}
	
	//��ȡ����ҳ
	public int getPageEnd(){
		return pageend;
	}
}