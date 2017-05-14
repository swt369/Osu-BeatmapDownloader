package com.swt369;

import java.io.File;

//获取已有的BeatmapId号
public class CurSongs{
	//利用HASH的思想判断对应id的Beatmap是否存在
	private boolean songs[];
	
	//对话框管理对象pbt
	ProgressBarThread pbt;
	
	//Songs文件夹路径
	private String pathSongs;
	
	//已有Beatmap数
	private int curmapcount;
	
	//当前访问文件夹数
	private int cur;
	
	//构造函数，需传入Songs文件夹路径与ProgressBarThread对象
	public CurSongs(String pathSongs,ProgressBarThread pbt){
		this.pathSongs = pathSongs;
		this.pbt = pbt;
		initealizeSongs();
	}
	
	//根据文件夹名获取Id
	private int GetId(String name){
		int id = 0;
		int cur = 0;
		while(cur<name.length() && Character.isDigit(name.charAt(cur))){
			id = id*10;
			id += name.charAt(cur++)-'0';
		}
		return id;
	}
	
	//读取Songs文件夹信息
	private void initealizeSongs(){
		songs = new boolean[Settings.MAXID];
		File files = new File(pathSongs);
		File[] songslist = files.listFiles();
		curmapcount = songslist.length;
		for(cur=0;cur<curmapcount;cur++){
			if(songslist[cur].isDirectory()){
				int curid = GetId(songslist[cur].getName());
				if(curid >= 0 && curid < Settings.MAXID){
					songs[curid] = true;
				}
			}
		pbt.updateProgress((int)(100*(cur)/(curmapcount+1))+1);
		}
		pbt.updateProgress(100);
	}
	
	//判断对应Id是否存在
	public boolean isExist(int id){
		return songs[id];
	}
	
	//返回已有map张数
	public int getMapNum(){
		return curmapcount;
	}

}
