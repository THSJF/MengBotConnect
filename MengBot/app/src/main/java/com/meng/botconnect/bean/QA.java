package com.meng.botconnect.bean;

import java.util.*;

public class QA {
	public static final int touhouBase=1;
	public static final int _2unDanmakuIntNew=2;
	public static final int _2unDanmakuAll=3;
	public static final int _2unNotDanmaku=4;
	public static final int _2unAll=5;
	public static final int otherDanmaku=6;
	
	private int flag=0;
	//flag: id(16bit)					type(8bit)		diffculty(8bit)
	//	0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0|0 0 0 0 0 0 0 0|0 0 0 0 0 0 0 0
	public int l=0;//file length
	public String q;
	public ArrayList<String> a = new ArrayList<>();
	private int t;//trueAns
	public String r;

	public void exangeAnswer() {
		Random r=new Random();
		int index1=r.nextInt(a.size() - 1);
		int index2=r.nextInt(a.size() - 1);
		boolean is1F=bitIs1(index1);
		setBit(index1, bitIs1(index2));
		setBit(index2, is1F);
		String ele1 = a.get(index1);
		a.set(index1, a.get(index2));
		a.set(index2, ele1);
	}

	private boolean bitIs1(int shift) {
		return (t & (1 << shift)) != 0;
	}

	private void setBit(int shift, boolean v) {
		if (v) {
			t |= (1 << shift);
		} else {
			t &= ~(1 << shift);
		}
	}

	public void setTrueAnsFlag(int t) {
		this.t = t;
	}

	public int getTrueAnsFlag() {
		return t;
	}

	public void setTrueAns(int... ts) {
		t = 0;
		for (int i:ts) {
			t |= (1 << i);
		}
	}

	public HashSet<Integer> getTrueAns() {
		HashSet<Integer> intList=new HashSet<>(32);
		for (int i=0;i < 32;++i) {
			if ((t & (1 << i)) != 0) {
				intList.add(i);
			}
		}
		return intList;
	}

	public void setFlag(int flag) {
		this.flag = flag;
	}

	public int getFlag() {
		return flag;
	}

	public void setDifficulty(int d) {
		flag &= 0xffffff00;
		flag |= d;
	}

	public int getDifficulty() {
		return flag & 0xff;
	}

	public void setId(int id) {
		flag &= 0x0000ffff;
		flag |= (id << 16);
	}

	public int getId() {
		return (flag >> 16) & 0xff;
	}

	public void setType(int type) {
		flag &= 0xffff00ff;
		flag |= (type << 8);
	}

	public int getType() {
		return (flag >> 8) & 0xff;
	}

	/*
	 @Override

	 public String toString() {
	 StringBuilder sb=new StringBuilder("题目ID:").append(id).append("\n");
	 sb.append("难度:");
	 switch (d) {
	 case 0:
	 sb.append("easy");
	 break;
	 case 1:
	 sb.append("normal");
	 break;
	 case 2:
	 sb.append("hard");
	 break;
	 case 3:
	 sb.append("lunatic");
	 break;
	 case 4:
	 sb.append("overdrive");
	 break;
	 case 5:
	 sb.append("k");
	 }
	 sb.append("\n");
	 sb.append("\n分类:");
	 switch (type) {
	 case 0:
	 sb.append("未定义");
	 break;
	 case touhouBase:
	 sb.append("东方project基础");
	 break;
	 case _2unDanmakuIntNew:
	 sb.append("整数作");
	 break;
	 case _2unDanmakuAll:
	 sb.append("官方弹幕作");
	 break;
	 case _2unNotDanmaku:
	 sb.append("官方格斗作");
	 break;
	 case _2unAll:
	 sb.append("官方作");
	 break;
	 case otherDanmaku:
	 sb.append("同人弹幕");
	 }
	 sb.append("\n").append(q).append("\n");
	 int i=1;
	 for (String s:a) {
	 if (s.equals("")) {
	 continue;
	 }
	 sb.append(i++).append(": ").append(s).append("\n");
	 }
	 return sb.toString();
	 }*/
}

