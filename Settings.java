package com.swt369;

public class Settings {
	//OSU������Beapmaps��ǩ��ַ��������ֻ�ȡָ��ҳ
	static final String URL_OSUBEATMAPS = 
			"https://osu.ppy.sh/p/beatmaplist?l=1&r=0&q=&g=0&la=0&ra=&s=4&o=1&m=-1"
			+ "&page=";
	
	//OSU����վ��ַ,���id��ȡָ��Beatmap
	static final String URL_MIRRORWEBSITE =
			"https://osu.uu.gl/s/";
	
	//���Id��
	static final int MAXID = 10000000;
	
	//���ڳ�ʼλ��
	static final int ORIGINPOS_X = 100;
	static final int ORIGINPOS_Y = 100;
	
	//���ڳߴ�
	static final int WINDOW_WIDTH = 800;
	static final int WINDOW_HEIGHT = 600;
	
	//�Ի����봰�ڼ�ƫ����
	static final int DIALOG_OFFSET_X = 150;
	static final int DIALOG_OFFSET_Y = 150;
	//�Ի���ߴ�
	static final int DIALOG_WIDTH = 300;
	static final int DIALOG_HEIGHT = 300;
	//����idƴ��URL
	public static String GetURL(int id){
		return new String(URL_MIRRORWEBSITE+id);
	}
	
}
