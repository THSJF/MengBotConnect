package com.meng.botconnect.bean;
import android.graphics.*;
import android.view.*;
import android.widget.*;
import com.meng.botconnect.*;
import com.meng.botconnect.lib.*;
import com.meng.botconnect.network.*;
import java.io.*;

public class BotInfo {
	private long onLoginQQ;
	private String onLoginNick="";
	private String cookie="";
	private int csrfToken;
	private int groupCount;

	public void setGroupCount(final int groupCount) {
		this.groupCount = groupCount;
		MainActivity2.instance.runOnUiThread(new Runnable(){

				@Override
				public void run() {
					MainActivity2.instance.addHeaderView();
					((TextView) MainActivity2.instance.headView.findViewById(R.id.lv_drawer_headTextView_group)).setText("群组数量:" + groupCount);
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
					((TextView)MainActivity2.instance.headView.findViewById(R.id.lv_drawer_headTextView_nick)).setText(onLoginNick);
				}
			});
	}

	public String getOnLoginNick() {
		return onLoginNick;
	}

	public void setOnLoginQQ(final long onLoginQQ) {
		this.onLoginQQ = onLoginQQ;
		final View v=MainActivity2.instance.headView;
		MainActivity2.instance.runOnUiThread(new Runnable(){

				@Override
				public void run() {
					MainActivity2.instance.addHeaderView();
					((TextView) v.findViewById(R.id.lv_drawer_headTextView_qq)).setText(String.valueOf(onLoginQQ));
				}
			});
		final ImageView head=(ImageView) v.findViewById(R.id.lv_drawer_headImageView);
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
