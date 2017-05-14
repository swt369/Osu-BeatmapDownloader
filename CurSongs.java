package com.swt369;

import java.io.File;

//��ȡ���е�BeatmapId��
public class CurSongs{
	//����HASH��˼���ж϶�Ӧid��Beatmap�Ƿ����
	private boolean songs[];
	
	//�Ի���������pbt
	ProgressBarThread pbt;
	
	//Songs�ļ���·��
	private String pathSongs;
	
	//����Beatmap��
	private int curmapcount;
	
	//��ǰ�����ļ�����
	private int cur;
	
	//���캯�����贫��Songs�ļ���·����ProgressBarThread����
	public CurSongs(String pathSongs,ProgressBarThread pbt){
		this.pathSongs = pathSongs;
		this.pbt = pbt;
		initealizeSongs();
	}
	
	//�����ļ�������ȡId
	private int GetId(String name){
		int id = 0;
		int cur = 0;
		while(cur<name.length() && Character.isDigit(name.charAt(cur))){
			id = id*10;
			id += name.charAt(cur++)-'0';
		}
		return id;
	}
	
	//��ȡSongs�ļ�����Ϣ
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
	
	//�ж϶�ӦId�Ƿ����
	public boolean isExist(int id){
		return songs[id];
	}
	
	//��������map����
	public int getMapNum(){
		return curmapcount;
	}

}
