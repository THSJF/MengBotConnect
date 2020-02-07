package com.meng.botconnect.bean;
import android.graphics.*;
import android.view.*;
import android.widget.*;
import com.meng.botconnect.*;
import com.meng.botconnect.lib.*;
import com.meng.botconnect.network.*;
import java.io.*;
import java.util.*;

public class BotInfo {
	private long onLoginQQ;
	private String onLoginNick="";
	private String cookie="";
	private int csrfToken;
	private int groupCount;
	private TextView qq;
	private TextView nick;
	private TextView groups;
	private TextView sendToBot;
	private TextView recFromBot;
	private TextView msgRec;
	private TextView msgCmd;
	private TextView msgSend;
	private ImageView head;
	private BotMsgInfo[] infos=new BotMsgInfo[60];
	private int dataPointer=0;
	private BotMsgInfo infoMinute = new BotMsgInfo();
	public BotInfo(View v) {
		qq = (TextView) v.findViewById(R.id.lv_drawer_headTextView_qq);
		nick = (TextView) v.findViewById(R.id.lv_drawer_headTextView_nick);
		groups = (TextView) v.findViewById(R.id.lv_drawer_headTextView_group);
		sendToBot = (TextView) v.findViewById(R.id.lv_drawer_headTextViewSendToBot);
		recFromBot = (TextView) v.findViewById(R.id.lv_drawer_headTextViewRecFromBot);
		msgRec = (TextView) v.findViewById(R.id.lv_drawer_headTextViewMsgRec);
		msgCmd = (TextView) v.findViewById(R.id.lv_drawer_headTextViewMsgCmd);
		msgSend = (TextView) v.findViewById(R.id.lv_drawer_headTextViewMsgSend);
		head = (ImageView) v.findViewById(R.id.lv_drawer_headImageView);
		for (int i=0;i < 60;++i) {
			infos[i] = new BotMsgInfo();
		}
	}

	public void setMsgInfo(int a, int b, int c, int d, int e) {
		if (dataPointer == 60) {
			dataPointer = 0;
		}
		BotMsgInfo bmi=infos[dataPointer++];
		bmi.sendTo = a;
		bmi.recFrom = b;
		bmi.msgPerSec = c;
		bmi.msgCmdPerSec = d;
		bmi.msgSendPerSec = e;
		infoMinute.reset();
		for (BotMsgInfo bi:infos) {
			infoMinute.sendTo += bi.sendTo;
			infoMinute.recFrom += bi.recFrom;
			infoMinute.msgPerSec += bi.msgPerSec;
			infoMinute.msgCmdPerSec += bi.msgCmdPerSec;
			infoMinute.msgSendPerSec += bi.msgSendPerSec;
		}
		MainActivity2.instance.runOnUiThread(new Runnable(){

				@Override
				public void run() {
					sendToBot.setText("发送至其他bot:" + infoMinute.sendTo + "/min");
					recFromBot.setText("接收自其他bot:" + infoMinute.recFrom + "/min");
					msgRec.setText("群消息数:" + infoMinute.msgPerSec + "/min");
					msgCmd.setText("命令消息数:" + infoMinute.msgCmdPerSec + "/min");
					msgSend.setText("发送消息数:" + infoMinute.msgSendPerSec + "/min");
				}
			});
	}

	public void setGroupCount(final int groupCount) {
		this.groupCount = groupCount;
		MainActivity2.instance.runOnUiThread(new Runnable(){

				@Override
				public void run() {
					MainActivity2.instance.addHeaderView();
					groups.setText("群组数量:" + groupCount);
				}
			});
	}

	public int getGroupCount() {
		return groupCount;
	}

	public void setCsrfToken(int csrfToken) {
		this.csrfToken = csrfToken;
	}

	public int getCsrfToken() {
		return csrfToken;
	}

	public void setCookie(String cookie) {
		this.cookie = cookie;
		setCsrfToken(Tools.CQ.getG_tk(Tools.Network.cookieToMap(cookie).get("skey")));
	}

	public String getCookie() {
		return cookie;
	}

	public void setOnLoginNick(final String onLoginNick) {
		this.onLoginNick = onLoginNick;
		MainActivity2.instance.runOnUiThread(new Runnable(){

				@Override
				public void run() {
					MainActivity2.instance.addHeaderView();
					nick.setText(onLoginNick);
				}
			});
	}

	public String getOnLoginNick() {
		return onLoginNick;
	}

	public void setOnLoginQQ(final long onLoginQQ) {
		this.onLoginQQ = onLoginQQ;
		MainActivity2.instance.menusList.clear();
		if (onLoginQQ == 2528419891L) {	
			Collections.addAll(MainActivity2.instance.menusList, new String[]{
								   "群消息",
								   "状态",
								   "题库",
								   "设置",
								   "退出"});
		} else if (onLoginQQ == 2089693971) {
			Collections.addAll(MainActivity2.instance.menusList, new String[]{
								   "群消息",
								   "状态",
								   "群配置","不回复的QQ","不回复的字","飞机佬名单","Master","Admin",
								   "自动同意进群","黑名单QQ","黑名单群",
								   "设置",
								   "退出"});
		}
		MainActivity2.instance.runOnUiThread(new Runnable(){

				@Override
				public void run() {
					MainActivity2.instance.adpter.notifyDataSetChanged();
					MainActivity2.instance.addHeaderView();
					qq.setText(String.valueOf(onLoginQQ));
				}
			});
		final File fHeadImage = new File(MainActivity2.mainFolder + "user/" + onLoginQQ + ".jpg");
		if (fHeadImage.exists()) {
			MainActivity2.instance.runOnUiThread(new Runnable(){

					@Override
					public void run() {
						head.setImageBitmap(BitmapFactory.decodeFile(fHeadImage.getAbsolutePath()));
					}
				});
		} else  {
			MainActivity2.instance.threadPool.execute(new DownloadImageRunnable(MainActivity2.instance, head, onLoginQQ, 1));
		}
		head.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					MainActivity2.instance.threadPool.execute(new DownloadImageRunnable(MainActivity2.instance, head, onLoginQQ, 1));
				}
			});
	}

	public long getOnLoginQQ() {
		return onLoginQQ;
	}
}
