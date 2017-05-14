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

//用于获取需要抓取的页数
public class DialogForPageCount extends JDialog implements ActionListener{
	
	//页数
	private int pagestart = -1;
	private int pageend = -1;
	private JTextField tf;
	private JTextField tf2;
	private JTextField tf3;
	private JTextField tf4;
	private JPanel jp;
	private JButton jb;
	
	//构造函数，需输入父窗口
	public DialogForPageCount(JFrame frame){
		//父类构造函数
		super(frame,"DiaLog",true);
		
		//单行文本
		tf = new JTextField("抓取页数:");
		tf.setEditable(false);
		tf.setHorizontalAlignment(JTextField.CENTER);
		
		//单行文本，用于输入开始页数
		tf2 = new JTextField("1");
		tf2.setHorizontalAlignment(JTextField.CENTER);
		
		//单行文本
		tf3 = new JTextField("~");
		tf3.setEditable(false);
		tf3.setHorizontalAlignment(JTextField.CENTER);
		
		//单行文本，用于输入终止页数
		tf4 = new JTextField("1");
		tf4.setHorizontalAlignment(JTextField.CENTER);
		
		//面板，存放两个单行文本
		jp = new JPanel();
		jp.setLayout(new GridLayout(1,4));
		jp.add(tf);
		jp.add(tf2);
		jp.add(tf3);
		jp.add(tf4);
		
		//确定按钮
		jb = new JButton("确定");
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
		// TODO 自动生成的方法存根
		if(e.getSource() == jb){
			this.pagestart = textField2int(tf2);
			this.pageend = textField2int(tf4);
			if(pagestart > pageend){
				JOptionPane.showMessageDialog(null,
						"出错啦", "开始页号不能大于结束页号", JOptionPane.ERROR_MESSAGE);
				return;
			}
			if(pagestart > 0 && pageend > 0){
				this.setVisible(false);
			}
		}
	}
	
	//将指定文本框内容转化为数字
	private int textField2int(JTextField tf){
		String str = tf.getText();
		int sum = 0;
		for(int i=0;i<str.length();i++){
			if(!(str.charAt(i)>='0' && str.charAt(i)<='9')){
				JOptionPane.showMessageDialog(null,
						"出错啦", "请输入数字", JOptionPane.ERROR_MESSAGE);
				return -1;
			}
			sum = sum*10;
			sum += str.charAt(i)-'0';
		}
		return sum;
	}
	
	//获取开始页
	public int getPageStart(){
		return pagestart;
	}
	
	//获取结束页
	public int getPageEnd(){
		return pageend;
	}
}