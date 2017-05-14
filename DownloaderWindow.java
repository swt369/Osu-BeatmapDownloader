package com.swt369;

import javax.swing.*;
import javax.swing.border.BevelBorder;

import java.awt.*;
import java.awt.event.*;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.LinkedList;

public class DownloaderWindow extends JFrame implements ActionListener{

	//�������
	private JPanel jp; 			//��壬����������ֹͣ��ť,���������Ͻ�
	private JTextArea ta; 		//�ı���������ʾ��Ϣ
	private JScrollPane sp;		//������壬�����ı���ta
	private JTextField tf; 		//�����ı���������ʾ·��
	private JButton jb_open; 	//�򿪰�ť������ѡ��·��
	private JButton jb_start; 	//��ʼ��ť�����ڿ�ʼ����
	private JButton jb_stop; 	//ֹͣ��ť������ֹͣ����
	private JProgressBar jbar;  //������������ָʾ���ؽ���
	
	//Osu!�ļ���·��
	private String pathOsu = null;
	
	//Osu!Downloads�ļ���·��
	private String pathDownloads = null;
	
	//Osu!Songs�ļ���
	private String pathSongs = null;
	
	//Songs�ļ��в鿴��
	private CurSongs cursongs = null;
	
	//BeatmapIdץȡ��
	private WebPageSource wb;
	
	//ץȡҳ
	private int pagestart = -1;
	private int pageend = -1;
	
	//�����߳�
	private Thread thread_download;
	
	//�������߳�
	private ProgressBarThread pbt;
	private Thread thread_jbar;
	
	//�������ļ���С
	private int len = 0;
	
	//�ļ��ܳ�
	private int filelength = 0;
	
	//��ʼ���������
	private void initializeComponents(){
		//��ʼ��������ť
		jb_open = new JButton("��");
		jb_open.addActionListener(this);
		jb_start = new JButton("��ʼ");
		jb_start.addActionListener(this);
		jb_stop = new JButton("ֹͣ");
		jb_stop.addActionListener(this);
		
		//��ʼ����岢���밴ť
		jp = new JPanel();
		jp.setLayout(new GridLayout(2,1));
		jp.add(jb_start);
		jp.add(jb_stop);
		
		//��ʼ���ı�����������
		ta = new JTextArea(20,40);
		ta.setEditable(false);
		Font font = new Font(null,Font.PLAIN,15);
		ta.setFont(font);
		sp = new JScrollPane();
		sp.setBorder(new BevelBorder(BevelBorder.RAISED));
		sp.setViewportView(ta);
		
		//��ʼ�������ı�
		tf = new JTextField("��ѡ��Osu!�ļ���·��");
		tf.setHorizontalAlignment(JTextField.CENTER);
		
		//��ʼ��������
		jbar = new JProgressBar();
		jbar.setStringPainted(true);
		jbar.setValue(0);
	}
	
	//���и������
	private void arrangeComponents(){
		//���ò��ַ�ʽΪ�����鷽ʽ
		this.setLayout(new GridBagLayout());
		
		//�������jp
		final GridBagConstraints constraints_jp = new GridBagConstraints();
		constraints_jp.gridx = 0;
		constraints_jp.gridy = 0;
		constraints_jp.gridwidth = 1;
		constraints_jp.gridheight = 4;
		constraints_jp.weightx = 20;
		constraints_jp.weighty = 90;
		constraints_jp.fill = constraints_jp.BOTH;
		this.getContentPane().add(jp, constraints_jp);
		
		//���ù������sp
		final GridBagConstraints constraints_sp = new GridBagConstraints();
		constraints_sp.gridx = 1;
		constraints_sp.gridy = 0;
		constraints_sp.gridwidth = 4;
		constraints_sp.gridheight = 3;
		constraints_sp.weightx = 80;
		constraints_sp.fill = constraints_sp.BOTH;
		this.getContentPane().add(sp, constraints_sp);
		
		//���ô򿪰�ťjb_open
		final GridBagConstraints constraints_jb_open = new GridBagConstraints();
		constraints_jb_open.gridx = 0;
		constraints_jb_open.gridy = 4;
		constraints_jb_open.gridwidth = 1;
		constraints_jb_open.gridheight = 1;
		constraints_jb_open.weighty = 10;
		constraints_jb_open.fill = constraints_jb_open.BOTH;
		this.getContentPane().add(jb_open, constraints_jb_open);
		
		//���õ����ı�tf
		final GridBagConstraints constraints_tf = new GridBagConstraints();
		constraints_tf.gridx = 1;
		constraints_tf.gridy = 4;
		constraints_tf.gridwidth = 4;
		constraints_tf.gridheight = 1;
		constraints_tf.fill = constraints_tf.BOTH;
		this.getContentPane().add(tf, constraints_tf);
		
		//���ý�����
		final GridBagConstraints constraints_jbar = new GridBagConstraints();
		constraints_jbar.gridx = 1;
		constraints_jbar.gridy = 3;
		constraints_jbar.gridwidth = 4;
		constraints_jbar.gridheight = 1;
		constraints_jbar.fill = constraints_jbar.BOTH;
		this.getContentPane().add(jbar, constraints_jbar);
	}
	
	//���캯��
	public DownloaderWindow(){
		//��ʼ�����
		initializeComponents();
		//�������
		arrangeComponents();
		//���ô���λ���봰�ڴ�С
		setBounds(Settings.ORIGINPOS_X,Settings.ORIGINPOS_Y
				,Settings.WINDOW_WIDTH,Settings.WINDOW_HEIGHT);
		//��ʾ����
		setVisible(true);
		//���ñ���
		setTitle("Osu!Beatmap������");
		//���ùرշ�ʽ
		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		//��ֹ�ı䴰�ڴ�С
		setResizable(false);
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		//�򿪰�ť����
		if(e.getSource() == jb_open){
			//����Դ������ѡ��Osu!�ļ���
			JFileChooser fc=new JFileChooser();
			fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
			fc.showOpenDialog(this);
			if(fc.getSelectedFile() == null){
				return ;
			}
			
			//����Osu!Ŀ¼��Songs�ļ�����Downloads�ļ���·��
			pathOsu = fc.getSelectedFile().getPath();
			pathDownloads = new String(pathOsu+File.separatorChar+"Downloads");
			pathSongs = new String(pathOsu+File.separatorChar+"Songs");
			cursongs = null;
			
			//��ʾ
			SwingUtilities.invokeLater(new Runnable(){
				@Override
				public void run() {
					tf.setText(pathOsu);
					ta.append("·����ѡ��\n");
				}
			});
		}
		
		//��ʼ��ť����
		if(e.getSource() == jb_start){
			//����δѡ��·���򱨴�
			if(pathOsu == null){
				JOptionPane.showMessageDialog(null,
						"����ѡ��·��", "������", JOptionPane.ERROR_MESSAGE);
				return;
			}
			
			//��������������
			pbt= new ProgressBarThread(jbar);
			thread_jbar = new Thread(pbt);
			thread_jbar.setPriority(6);
			thread_jbar.start();
			
			//��ʼ��Song�ļ��в鿴��
			if(!new File(pathSongs).exists()){
				new File(pathSongs).mkdirs();
				ta.append("δ�ҵ�Songs�ļ��У�����Ŀ¼�ڴ���\n");
			}
			if(cursongs == null){
				appendLineTota("���ڲ鿴Songs�ļ��У������ظ�����...\n");
				cursongs = new CurSongs(pathSongs,pbt);
				appendLineTota("Songs�ļ��в鿴��ϣ�����"+cursongs.getMapNum()
				+"��Beatmap\n");
			}
			
			//�����Ի���ѡ��ҳ��
			DialogForPageCount dialog = new DialogForPageCount(this);
			pagestart = dialog.getPageStart();
			pageend = dialog.getPageEnd();
			
			//���������߳�
			if(pagestart != -1 && pageend != -1){
				thread_download = new Thread(new Runnable(){
					@Override
					public void run() {
						try{
							appendLineTota("ѡ��ץȡҳ��"+pagestart+"~"+pageend
									+"����ʼ��ȡBeatmapId��...\n");
							
							wb = new WebPageSource(pagestart,pageend,pbt);
							appendLineTota("BeatmapId�Ż�ȡ��ϣ���ʼ����...\n");
							
							LinkedList<Integer> beatmapids = wb.getIds();
							int curnum = 0;
							int downloadcount = 0;
							while(curnum<beatmapids.size()){
								int curid = beatmapids.get(curnum++);
								
								if(cursongs.isExist(curid)){
									appendLineTota("Beatmap"+curid+"�Ѵ���\n");
									continue;
								}
								appendLineTota("��������Beatmap"+curid+"\n");
								
								System.out.println(Settings.GetURL(curid)); 
								File beatmapfile = downloadFile
										(Settings.GetURL(curid),curid);
								if(beatmapfile.exists()){
									appendLineTota("Beatmap"+curid+"�������\n");
									downloadcount++;
								}else{
									appendLineTota("Beatmap"+curid+"����ʧ��\n");
								}
							}
							
							appendLineTota("����Beatmap������ϣ���������"
							+downloadcount+"��ͼ\n");
							appendLineTota("��л����ʹ�ã�\n");
							pbt.stopThread();
						
						}catch(Exception e){
							e.printStackTrace();
							appendLineTota("������ֹͣ\n");
						}
					}
				});
				thread_download.setPriority(5);
				thread_download.start();
				
			}
		}
		
		//ֹͣ��ť����
		if(e.getSource() == jb_stop){
			if(thread_download != null &&thread_download.isAlive()){
				thread_download.stop();
				appendLineTota("������ֹͣ\n");
			}
			if(pbt != null){
				pbt.stopThread();
			}
		}
	}
	
	//����ָ��URL�����ļ���download�ļ���
	private File downloadFile(String urlpath,int id){
		File file = null;
		//�ļ���,��չ��Ϊ.osz
		String filename = Integer.toString(id);
		file = new File(pathOsu+File.separatorChar+"Downloads"
		+File.separatorChar+filename+".osz");
		//��δ�ҵ�Downloads�ļ������Զ�����
		if(!file.getParentFile().exists()){
			ta.append("δ�ҵ�Downloads�ļ��У�����Ŀ¼�ڴ���\n");
			file.getParentFile().mkdirs();
		}
		try{
			// ͳһ��Դ
			URL url = new URL(urlpath);
			// http��������
			HttpURLConnection urlconnection = (HttpURLConnection)
					url.openConnection();
			//���÷��ʷ�ʽ
			urlconnection.setRequestMethod("POST");
			//�����ַ�����
			urlconnection.setRequestProperty("Charset", "UTF-8");
			int responsecode = urlconnection.getResponseCode();
			//200����ɹ�����
			if(responsecode == 200){
				//������
				BufferedInputStream bin = 
						new BufferedInputStream(urlconnection.getInputStream());
				//�ļ���С
				filelength = urlconnection.getContentLength();
				System.out.println(filelength);
				OutputStream out = new FileOutputStream(file);
				int size = 0;
				len = 0;
				byte[] buf = new byte[1024];
				
				//��ʼ����
				while((size = bin.read(buf)) != -1){
					len += size;
					out.write(buf,0,size);
					pbt.updateProgress((int)(len*100/filelength));
				}
				bin.close();
				out.close();
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		System.out.println(len);
		return file;
	}
	
	//����EDT��ta������ı�
	private void appendLineTota(String line){
		SwingUtilities.invokeLater(new Runnable(){
			@Override
			public void run() {
				ta.append(line);
				ta.setCaretPosition(ta.getDocument()
						.getLength());
				try {
					Thread.sleep(10);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	//������
	public static void main(String[] args) {
		
	}
}
