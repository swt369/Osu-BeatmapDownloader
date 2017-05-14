package com.swt369;

import javax.swing.JProgressBar;
import javax.swing.SwingUtilities;

//�������߳�
public class ProgressBarThread implements Runnable{
	//������
	private JProgressBar jbar;
	//��ǰ����
	private int progress;
	//�Ƿ���������
	private boolean run = true;
	
	//���캯��
	public ProgressBarThread(JProgressBar jbar){
		this.jbar = jbar;
		progress = jbar.getValue();
	}

	@Override
	public void run() {
		// TODO �Զ����ɵķ������
		while(this.run){
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				// TODO �Զ����ɵ� catch ��
				e.printStackTrace();
			}
			setJBar(progress);
		}
		
	}
	
	//���½���
	public void updateProgress(int progress){
		this.progress = progress;
	}
	
	//ֹͣ����
	public void stopThread(){
		this.run = false;
	}
	
	//����EDT�޸Ľ�����״̬
	private void setJBar(int value){
		SwingUtilities.invokeLater(new Runnable(){
			@Override
			public void run() {
				jbar.setValue(value);
			}
		});
	}
}
