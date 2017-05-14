package com.swt369;

public class Settings {
	//OSU官网下Beapmaps标签网址，后加数字获取指定页
	static final String URL_OSUBEATMAPS = 
			"https://osu.ppy.sh/p/beatmaplist?l=1&r=0&q=&g=0&la=0&ra=&s=4&o=1&m=-1"
			+ "&page=";
	
	//OSU镜像站网址,后加id获取指定Beatmap
	static final String URL_MIRRORWEBSITE =
			"https://osu.uu.gl/s/";
	
	//最大Id号
	static final int MAXID = 10000000;
	
	//窗口初始位置
	static final int ORIGINPOS_X = 100;
	static final int ORIGINPOS_Y = 100;
	
	//窗口尺寸
	static final int WINDOW_WIDTH = 800;
	static final int WINDOW_HEIGHT = 600;
	
	//对话框与窗口间偏移量
	static final int DIALOG_OFFSET_X = 150;
	static final int DIALOG_OFFSET_Y = 150;
	//对话框尺寸
	static final int DIALOG_WIDTH = 300;
	static final int DIALOG_HEIGHT = 300;
	//根据id拼接URL
	public static String GetURL(int id){
		return new String(URL_MIRRORWEBSITE+id);
	}
	
}
