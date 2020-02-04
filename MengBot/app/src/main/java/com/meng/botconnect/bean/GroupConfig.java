package com.meng.botconnect.bean;

import java.util.*;

public class GroupConfig extends Object {

	public static final int ID_MainSwitch=0;
	public static final int ID_Repeater = 1;
	public static final int ID_MoShenFuSong=2;
	public static final int ID_BilibiliNewUpdate=3;
	public static final int ID_Dice=4;
	public static final int ID_SpellCollect=5;
	public static final int ID_OCR=6;
	public static final int ID_Barcode=7;
	public static final int ID_Banner=8;
	public static final int ID_CQCode=9;
	public static final int ID_Music=10;
	public static final int ID_PicSearch=11;
	public static final int ID_BiliLink=12;
	public static final int ID_Setu=13;
	public static final int ID_PoHaiTu=14;
	public static final int ID_NvZhuang=15;
	public static final int ID_GuanZhuangBingDu=16;
	public static final int ID_Seq=17;
	public static final int ID_GroupDic=18;
	public static final int ID_CheHuiMotu=19;
	public static final int ID_PicEdit=20;
	public static final int ID_UserCount=21;
	public static final int ID_GroupCount=22;
	public static final int ID_GroupCountChart=23;

	public long n=0;
	public int s1=0;
	public int f1=0;

	public boolean isFunctionEnable(int functionID) {
		return (f1 & (1 << functionID)) != 0;
	}

	public void setFunctionEnabled(int functionID, boolean enable) {
		if (enable) {
			f1 |= (1 << functionID);
		} else {
			f1 &= ~(1 << functionID);
		}
	}

	@Override
	public int hashCode() {
		int i=0;
		return super.hashCode();
	}

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof GroupConfig)) {
            return false;
        }
        GroupConfig p = (GroupConfig) obj;
        return this.n == p.n;
    }
}
