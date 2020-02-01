import java.util.*;
import java.io.*;

public class Main {
	public static void main(String[] args) {

		BotDataPack bdp=BotDataPack.encode(BotDataPack.storeKeys);

		bdp.
			write(Tools.Hash.MD5("此生无悔入东方 来世愿生幻想乡 红魔地灵夜神雪 永夜风神星莲船 非想天则文花贴 萃梦神灵绯想天 冥界地狱异变起 樱下华胥主谋现 净罪无改渡黄泉 华鸟风月是非辨 境界颠覆入迷途 幻想花开啸风弄 二色花蝶双生缘 前缘未尽今生还 星屑洒落雨霖铃 虹彩彗光银尘耀 无寿迷蝶彼岸归 幻真如画妖如月 永劫夜宵哀伤起 幼社灵中幻似梦 追忆往昔巫女缘 须弥之间冥梦现 仁榀华诞井中天 歌雅风颂心无念")).
			write(6).
			write("easy").
			write("normal").
			write("hard").
			write("lunatic").
			write("overdrive").
			write("kidding");

		bdp.write(8).
			write("未分类").
			write("车万基础").
			write("新作整数作").
			write("官方弹幕作").
			write("官方非弹幕").
			write("官方所有").
			write("同人弹幕").
			write("luastg");

		try {
			FileOutputStream fos=new FileOutputStream(new File("/storage/emulated/0/botkey.dat"));
			fos.write(bdp.getData());
			fos.flush();
			fos.close();
		} catch (Exception e) {}

		BotDataPack de=BotDataPack.decode((bdp.getData()));
		ArrayList<String> hm=new ArrayList<>();
		System.out.println(de.readString());
		int le=de.readInt();
		for (int i=0;i < le;++i) {
			hm.add(de.readString());
		}

		ArrayList<String> hmm=new ArrayList<>();
		int le2=de.readInt();
		for (int i=0;i < le2;++i) {
			hmm.add(de.readString());
		}
		System.out.println(hm);
		System.out.println(hmm);
	}




}
