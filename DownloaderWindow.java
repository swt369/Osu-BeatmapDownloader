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

	//窗口组件
	private JPanel jp; 			//面板，放置启动与停止按钮,放置在左上角
	private JTextArea ta; 		//文本域，用于显示信息
	private JScrollPane sp;		//滚动面板，用于文本域ta
	private JTextField tf; 		//单行文本，用于显示路径
	private JButton jb_open; 	//打开按钮，用于选择路径
	private JButton jb_start; 	//开始按钮，用于开始下载
	private JButton jb_stop; 	//停止按钮，用于停止下载
	private JProgressBar jbar;  //进度条，用于指示下载进度
	
	//Osu!文件夹路径
	private String pathOsu = null;
	
	//Osu!Downloads文件夹路径
	private String pathDownloads = null;
	
	//Osu!Songs文件夹
	private String pathSongs = null;
	
	//Songs文件夹查看器
	private CurSongs cursongs = null;
	
	//BeatmapId抓取器
	private WebPageSource wb;
	
	//抓取页
	private int pagestart = -1;
	private int pageend = -1;
	
	//下载线程
	private Thread thread_download;
	
	//进度条线程
	private ProgressBarThread pbt;
	private Thread thread_jbar;
	
	//已下载文件大小
	private int len = 0;
	
	//文件总长
	private int filelength = 0;
	
	//初始化各个组件
	private void initializeComponents(){
		//初始化各个按钮
		jb_open = new JButton("打开");
		jb_open.addActionListener(this);
		jb_start = new JButton("开始");
		jb_start.addActionListener(this);
		jb_stop = new JButton("停止");
		jb_stop.addActionListener(this);
		
		//初始化面板并加入按钮
		jp = new JPanel();
		jp.setLayout(new GridLayout(2,1));
		jp.add(jb_start);
		jp.add(jb_stop);
		
		//初始化文本域与滚动面板
		ta = new JTextArea(20,40);
		ta.setEditable(false);
		Font font = new Font(null,Font.PLAIN,15);
		ta.setFont(font);
		sp = new JScrollPane();
		sp.setBorder(new BevelBorder(BevelBorder.RAISED));
		sp.setViewportView(ta);
		
		//初始化单行文本
		tf = new JTextField("请选择Osu!文件夹路径");
		tf.setHorizontalAlignment(JTextField.CENTER);
		
		//初始化进度条
		jbar = new JProgressBar();
		jbar.setStringPainted(true);
		jbar.setValue(0);
	}
	
	//排列各个组件
	private void arrangeComponents(){
		//设置布局方式为网格组方式
		this.setLayout(new GridBagLayout());
		
		//设置面板jp
		final GridBagConstraints constraints_jp = new GridBagConstraints();
		constraints_jp.gridx = 0;
		constraints_jp.gridy = 0;
		constraints_jp.gridwidth = 1;
		constraints_jp.gridheight = 4;
		constraints_jp.weightx = 20;
		constraints_jp.weighty = 90;
		constraints_jp.fill = constraints_jp.BOTH;
		this.getContentPane().add(jp, constraints_jp);
		
		//设置滚动面板sp
		final GridBagConstraints constraints_sp = new GridBagConstraints();
		constraints_sp.gridx = 1;
		constraints_sp.gridy = 0;
		constraints_sp.gridwidth = 4;
		constraints_sp.gridheight = 3;
		constraints_sp.weightx = 80;
		constraints_sp.fill = constraints_sp.BOTH;
		this.getContentPane().add(sp, constraints_sp);
		
		//设置打开按钮jb_open
		final GridBagConstraints constraints_jb_open = new GridBagConstraints();
		constraints_jb_open.gridx = 0;
		constraints_jb_open.gridy = 4;
		constraints_jb_open.gridwidth = 1;
		constraints_jb_open.gridheight = 1;
		constraints_jb_open.weighty = 10;
		constraints_jb_open.fill = constraints_jb_open.BOTH;
		this.getContentPane().add(jb_open, constraints_jb_open);
		
		//设置单行文本tf
		final GridBagConstraints constraints_tf = new GridBagConstraints();
		constraints_tf.gridx = 1;
		constraints_tf.gridy = 4;
		constraints_tf.gridwidth = 4;
		constraints_tf.gridheight = 1;
		constraints_tf.fill = constraints_tf.BOTH;
		this.getContentPane().add(tf, constraints_tf);
		
		//设置进度条
		final GridBagConstraints constraints_jbar = new GridBagConstraints();
		constraints_jbar.gridx = 1;
		constraints_jbar.gridy = 3;
		constraints_jbar.gridwidth = 4;
		constraints_jbar.gridheight = 1;
		constraints_jbar.fill = constraints_jbar.BOTH;
		this.getContentPane().add(jbar, constraints_jbar);
	}
	
	//构造函数
	public DownloaderWindow(){
		//初始化组件
		initializeComponents();
		//排列组件
		arrangeComponents();
		//设置窗口位置与窗口大小
		setBounds(Settings.ORIGINPOS_X,Settings.ORIGINPOS_Y
				,Settings.WINDOW_WIDTH,Settings.WINDOW_HEIGHT);
		//显示窗口
		setVisible(true);
		//设置标题
		setTitle("Osu!Beatmap下载器");
		//设置关闭方式
		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		//禁止改变窗口大小
		setResizable(false);
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		//打开按钮方法
		if(e.getSource() == jb_open){
			//在下载时禁用该按钮
			if(thread_download != null &&thread_download.isAlive()){
				JOptionPane.showMessageDialog(null,
						"请勿在下载时变更路径！", "出错啦", JOptionPane.ERROR_MESSAGE);
				return;
			}
			
			//打开资源管理器选择Osu!文件夹
			JFileChooser fc=new JFileChooser();
			fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
			fc.showOpenDialog(this);
			if(fc.getSelectedFile() == null){
				return ;
			}
			
			//设置Osu!目录下Songs文件夹与Downloads文件夹路径
			pathOsu = fc.getSelectedFile().getPath();
			pathDownloads = new String(pathOsu+File.separatorChar+"Downloads");
			pathSongs = new String(pathOsu+File.separatorChar+"Songs");
			cursongs = null;
			
			//提示
			SwingUtilities.invokeLater(new Runnable(){
				@Override
				public void run() {
					tf.setText(pathOsu);
					ta.append("路径已选择\n");
				}
			});
		}
		
		//开始按钮方法
		if(e.getSource() == jb_start){
			//若还未选择路径则报错
			if(pathOsu == null){
				JOptionPane.showMessageDialog(null,
						"请先选择路径", "出错啦", JOptionPane.ERROR_MESSAGE);
				return;
			}
			
			//启动进度条进程
			pbt= new ProgressBarThread(jbar);
			thread_jbar = new Thread(pbt);
			thread_jbar.setPriority(6);
			thread_jbar.start();
			
			//初始化Song文件夹查看器
			if(!new File(pathSongs).exists()){
				new File(pathSongs).mkdirs();
				ta.append("未找到Songs文件夹，已在目录内创建\n");
			}
			if(cursongs == null){
				appendLineTota("正在查看Songs文件夹，避免重复下载...\n");
				cursongs = new CurSongs(pathSongs,pbt);
				appendLineTota("Songs文件夹查看完毕，已有"+cursongs.getMapNum()
				+"张Beatmap\n");
			}
			
			//弹出对话框选择页数
			DialogForPageCount dialog = new DialogForPageCount(this);
			pagestart = dialog.getPageStart();
			pageend = dialog.getPageEnd();
			
			//启动下载线程
			if(pagestart != -1 && pageend != -1){
				thread_download = new Thread(new Runnable(){
					@Override
					public void run() {
						try{
							appendLineTota("选定抓取页："+pagestart+"~"+pageend
									+"，开始获取BeatmapId号...\n");
							
							wb = new WebPageSource(pagestart,pageend,pbt);
							appendLineTota("BeatmapId号获取完毕，开始下载...\n");
							
							LinkedList<Integer> beatmapids = wb.getIds();
							int curnum = 0;
							int downloadcount = 0;
							while(curnum<beatmapids.size()){
								int curid = beatmapids.get(curnum++);
								
								if(cursongs.isExist(curid)){
									appendLineTota("Beatmap"+curid+"已存在\n");
									continue;
								}
								appendLineTota("正在下载Beatmap"+curid+"\n");
								
								System.out.println(Settings.GetURL(curid)); 
								File beatmapfile = downloadFile
										(Settings.GetURL(curid),curid);
								if(beatmapfile.exists()){
									appendLineTota("Beatmap"+curid+"下载完毕\n");
									downloadcount++;
								}else{
									appendLineTota("Beatmap"+curid+"下载失败\n");
								}
							}
							
							appendLineTota("所有Beatmap下载完毕，共下载了"
							+downloadcount+"张图\n");
							appendLineTota("感谢您的使用！\n");
							pbt.stopThread();
						
						}catch(Exception e){
							e.printStackTrace();
							appendLineTota("下载已停止\n");
						}
					}
				});
				thread_download.setPriority(5);
				thread_download.start();
				
			}
		}
		
		//停止按钮方法
		if(e.getSource() == jb_stop){
			if(thread_download != null &&thread_download.isAlive()){
				thread_download.stop();
				appendLineTota("下载已停止\n");
			}
			if(pbt != null){
				pbt.stopThread();
			}
		}
	}
	
	//根据指定URL下载文件至download文件夹
	private File downloadFile(String urlpath,int id){
		File file = null;
		//文件名,扩展名为.osz
		String filename = Integer.toString(id);
		file = new File(pathOsu+File.separatorChar+"Downloads"
		+File.separatorChar+filename+".osz");
		//若未找到Downloads文件夹则自动创建
		if(!file.getParentFile().exists()){
			ta.append("未找到Downloads文件夹，已在目录内创建\n");
			file.getParentFile().mkdirs();
		}
		try{
			// 统一资源
			URL url = new URL(urlpath);
			// http的连接类
			HttpURLConnection urlconnection = (HttpURLConnection)
					url.openConnection();
			//设置访问方式
			urlconnection.setRequestMethod("POST");
			//设置字符编码
			urlconnection.setRequestProperty("Charset", "UTF-8");
			int responsecode = urlconnection.getResponseCode();
			//200代表成功连接
			if(responsecode == 200){
				//输入流
				BufferedInputStream bin = 
						new BufferedInputStream(urlconnection.getInputStream());
				//文件大小
				filelength = urlconnection.getContentLength();
				System.out.println(filelength);
				OutputStream out = new FileOutputStream(file);
				int size = 0;
				len = 0;
				byte[] buf = new byte[1024];
				
				//开始下载
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
	
	//利用EDT向ta中添加文本
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
	
	//测试用
	public static void main(String[] args) {
		
	}
}
