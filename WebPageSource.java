package com.swt369;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.LinkedList;

//��ȡ��ҳԴ���룬������BeatmapId��
public class WebPageSource {
	
	//�������������pbt
	private ProgressBarThread pbt;
	
	//������ʽ����������ҳԴ����ץȡId��
	private String regex =
			"</div><div class='beatmap' id='\\d{1,7}' style='width:\\d{1,4}px;'>";
	//Id����
	private LinkedList<Integer> beatmapids;
	//����ȡҳ
	private int pagestart;
	private int pageend;
	//��ǰҳ
	private int cur;
	
	//�ж�ĳһ���Ƿ���������ʽƥ�䣬��ƥ���򷵻�id��,��ƥ�䷵��-1
	private int judgeAndGrabId(String line){
		if(line.matches(regex)){
			int index = line.indexOf("id=");
			int sum = 0;
			int cur = index+4;
			while(cur<line.length() && Character.isDigit(line.charAt(cur))){
				sum = sum*10;
				sum += line.charAt(cur++)-'0';
			}
			return sum;
		}else{
			return -1;
		}
	}
	
	//����ָ��ҳ������Id��
	private void readPage(int pagenum){
		try {
			URL url = new URL(Settings.URL_OSUBEATMAPS+pagenum);
			HttpURLConnection urlconnection = 
					(HttpURLConnection)url.openConnection();
			int responsecode = urlconnection.getResponseCode();
			BufferedReader reader;
			String line;
			if(responsecode == 200){
				System.out.printf("���ڷ��ʵ�%dҳ\n",pagenum);
				reader = new BufferedReader(new InputStreamReader
						(urlconnection.getInputStream()));
				while((line = reader.readLine()) != null){
					int tempId = judgeAndGrabId(line);
					if(tempId != -1){
						beatmapids.add(tempId);
						System.out.println(tempId);
					}
				}
			}
		} catch (MalformedURLException e) {
			System.out.println("·�������޷���ȡ��ҳԴ��");
		} catch (IOException e) {
			System.out.println("IO�쳣");
		}
	}
	
	//��������ҳ��
	private void readAllPages(){
		pbt.updateProgress(0);
		for(cur=pagestart;cur<=pageend;cur++){
			readPage(cur);
			pbt.updateProgress((int)(100*(cur-pagestart+1)/(pageend-pagestart+1)));
		}
		pbt.updateProgress(100);
	}
	
	//���캯�����������ȡ��ҳ��������������
	public WebPageSource(int pagestart,int pageend,ProgressBarThread pbt){
		this.pagestart = pagestart;
		this.pageend = pageend;
		this.pbt = pbt;
		beatmapids = new LinkedList<Integer>();
		readAllPages();
	}

	//��ȡ����id
	public LinkedList<Integer> getIds(){
		return beatmapids;
	}
	
	//������
	public static void main(String[] args) {
		
	}

}
