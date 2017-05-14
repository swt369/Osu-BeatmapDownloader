package com.swt369;

import javax.swing.JProgressBar;
import javax.swing.SwingUtilities;

//进度条线程
public class ProgressBarThread implements Runnable{
	//进度条
	private JProgressBar jbar;
	//当前进度
	private int progress;
	//是否正在运行
	private boolean run = true;
	
	//构造函数
	public ProgressBarThread(JProgressBar jbar){
		this.jbar = jbar;
		progress = jbar.getValue();
	}

	@Override
	public void run() {
		// TODO 自动生成的方法存根
		while(this.run){
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				// TODO 自动生成的 catch 块
				e.printStackTrace();
			}
			setJBar(progress);
		}
		
	}
	
	//更新进度
	public void updateProgress(int progress){
		this.progress = progress;
	}
	
	//停止进程
	public void stopThread(){
		this.run = false;
	}
	
	//利用EDT修改进度条状态
	private void setJBar(int value){
		SwingUtilities.invokeLater(new Runnable(){
			@Override
			public void run() {
				jbar.setValue(value);
			}
		});
	}
}
