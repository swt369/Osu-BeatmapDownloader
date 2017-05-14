package com.swt369;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.LinkedList;

//提取网页源代码，并返回BeatmapId号
public class WebPageSource {
	
	//进度条管理对象pbt
	private ProgressBarThread pbt;
	
	//正则表达式，用于在网页源码中抓取Id号
	private String regex =
			"</div><div class='beatmap' id='\\d{1,7}' style='width:\\d{1,4}px;'>";
	//Id链表
	private LinkedList<Integer> beatmapids;
	//待提取页
	private int pagestart;
	private int pageend;
	//当前页
	private int cur;
	
	//判断某一行是否与正则表达式匹配，若匹配则返回id号,不匹配返回-1
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
	
	//读入指定页的所有Id号
	private void readPage(int pagenum){
		try {
			URL url = new URL(Settings.URL_OSUBEATMAPS+pagenum);
			HttpURLConnection urlconnection = 
					(HttpURLConnection)url.openConnection();
			int responsecode = urlconnection.getResponseCode();
			BufferedReader reader;
			String line;
			if(responsecode == 200){
				System.out.printf("正在访问第%d页\n",pagenum);
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
			System.out.println("路径错误，无法获取网页源码");
		} catch (IOException e) {
			System.out.println("IO异常");
		}
	}
	
	//读入所有页面
	private void readAllPages(){
		pbt.updateProgress(0);
		for(cur=pagestart;cur<=pageend;cur++){
			readPage(cur);
			pbt.updateProgress((int)(100*(cur-pagestart+1)/(pageend-pagestart+1)));
		}
		pbt.updateProgress(100);
	}
	
	//构造函数，传入待提取的页与进度条管理对象
	public WebPageSource(int pagestart,int pageend,ProgressBarThread pbt){
		this.pagestart = pagestart;
		this.pageend = pageend;
		this.pbt = pbt;
		beatmapids = new LinkedList<Integer>();
		readAllPages();
	}

	//获取所有id
	public LinkedList<Integer> getIds(){
		return beatmapids;
	}
	
	//测试用
	public static void main(String[] args) {
		
	}

}
