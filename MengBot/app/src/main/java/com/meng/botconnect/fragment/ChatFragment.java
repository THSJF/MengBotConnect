package com.meng.botconnect.fragment;

import android.app.*;
import android.content.*;
import android.content.res.*;
import android.graphics.*;
import android.graphics.drawable.*;
import android.os.*;
import android.view.*;
import android.view.View.*;
import android.widget.*;
import android.widget.AdapterView.*;
import com.meng.botconnect.*;
import com.meng.botconnect.adapter.*;
import com.meng.botconnect.bean.*;
import com.meng.botconnect.lib.*;

import android.view.View.OnClickListener;

public class ChatFragment extends Fragment {

	private Group group;
	public ListView lvMsg;
	public EditText etMsgToSend;
	private ImageButton ibSend;

	public ChatFragment(Group g) {
		group = g;
	}

	@Override
	public void onHiddenChanged(boolean hidden) {
		if (!hidden) {
			getActivity().runOnUiThread(new Runnable(){

					@Override
					public void run() {
						Member m=group.getMenberByQQ(MainActivity2.nowBot.getOnLoginQQ());
						if (m == null) {
							MainActivity2.instance.threadPool.execute(new Runnable(){

									@Override
									public void run() {
										while (group.getMenberByQQ(MainActivity2.nowBot.getOnLoginQQ()) == null) {
											try {
												Thread.sleep(1000);
											} catch (InterruptedException e) {}
										}
										onHiddenChanged(false);
									}
								});
							if (MainActivity.sharedPreference.getBoolean("useLightTheme")) {
								MainActivity2.instance.ab.setBackgroundDrawable(new ColorDrawable(Color.argb(0xff, 0x3f, 0x51, 0xb5)));
							} else {
								MainActivity2.instance.ab.setBackgroundDrawable(new ColorDrawable(Color.GRAY));
							}
							return;
						}
						switch (m.getAuthority()) {
							case 1:
								if (MainActivity.sharedPreference.getBoolean("useLightTheme")) {
									MainActivity2.instance.ab.setBackgroundDrawable(new ColorDrawable(Color.argb(0xff, 0x3f, 0x51, 0xb5)));
								} else {
									MainActivity2.instance.ab.setBackgroundDrawable(new ColorDrawable(Color.GRAY));
								}
								break;
							case 2:
								MainActivity2.instance.ab.setBackgroundDrawable(new ColorDrawable(Color.GREEN));
								break;
							case 3:
								MainActivity2.instance.ab.setBackgroundDrawable(new ColorDrawable(Color.YELLOW));
								break;
						}
					}
				});
		}
		super.onHiddenChanged(hidden);
	}

	/*public void setAuth(final int auth) {
	 int titleId = Resources.getSystem().getIdentifier(
	 "action_bar_title", "id", "android");
	 TextView yourTextView = (TextView)getActivity().findViewById(titleId);
	 yourTextView.setTextColor(Color.BLACK);

	 }*/

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return inflater.inflate(R.layout.chat_view, container, false);
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		lvMsg = (ListView) view.findViewById(R.id.chat_viewListView);
		etMsgToSend = (EditText) view.findViewById(R.id.chat_viewEditText);
		ibSend = (ImageButton) view.findViewById(R.id.chat_viewImageButton);
		TextView tvGName=(TextView) view.findViewById(R.id.chat_viewTextViewGname);
		TextView tvGid=(TextView) view.findViewById(R.id.chat_viewTextViewGid);
		tvGName.setText(group.name);
		tvGid.setText(String.valueOf(group.id));
		MainActivity2.instance.CQ.getGroupMemberInfo(group.id, MainActivity2.nowBot.getOnLoginQQ());
		lvMsg.setAdapter(new ChatListAdapter(this, group.messageList));
		lvMsg.setOnItemLongClickListener(new OnItemLongClickListener(){

				@Override
				public boolean onItemLongClick(AdapterView<?> p1, View p2, int p3, long p4) {
					final BotMessage bm=(BotMessage) p1.getAdapter().getItem(p3);
					final String[] cities = {"禁言","设置群名片","设置群头衔","点赞","设为管理员","取消管理员","踢出本群"};
					new AlertDialog.Builder(getActivity()).
						setIcon(R.drawable.ic_launcher).
						setTitle("选择操作").
						setItems(cities, new DialogInterface.OnClickListener(){
							@Override
							public void onClick(DialogInterface dialog, int which) {
								switch (which) {
									case 0:
										showBan(bm);
										break;
									case 1:
										showSetGroupNick(bm);
										break;
									case 2:
										showSetGroupSpecialTitle(bm);
										break;
									case 3:
										MainActivity2.instance.CQ.sendLike(bm.getFromQQ(), 10);
										break;
									case 4:
										MainActivity2.instance.CQ.setGroupAdmin(bm.getGroup(), bm.getFromQQ(), true);
										break;
									case 5:
										MainActivity2.instance.CQ.setGroupAdmin(bm.getGroup(), bm.getFromQQ(), false);
										break;
									case 6:
										new AlertDialog.Builder(getActivity())
											.setTitle("确定踢出吗")
											.setPositiveButton("是", new DialogInterface.OnClickListener() {
												@Override
												public void onClick(DialogInterface p1, int p2) {
													MainActivity2.instance.CQ.setGroupKick(bm.getGroup(), bm.getFromQQ(), false);	
												}
											}).setNegativeButton("否", null).show();
										break;
								}
							}
						}).show();
					return true;
				}
			});
		lvMsg.setOnItemClickListener(new OnItemClickListener(){

				@Override
				public void onItemClick(AdapterView<?> p1, View p2, int p3, long p4) {
					LogTool.t(getActivity(), "click");
					BotMessage bm=(BotMessage) p1.getAdapter().getItem(p3);
					showText(bm);
				}
			});
		ibSend.setOnClickListener(new OnClickListener(){

				@Override
				public void onClick(View p1) {
					MainActivity2.instance.CQ.sendGroupMsg(group.id, etMsgToSend.getText().toString());
					etMsgToSend.setText("");
				}
			});
	}

	public void showBan(final BotMessage mb) {
		final String[] cities = {"0", "60", "120", "180", "600"};
		new AlertDialog.Builder(getActivity()).
			setIcon(R.drawable.ic_launcher).
			setTitle("选择一个时间").
			setItems(cities, new DialogInterface.OnClickListener(){
				@Override
				public void onClick(DialogInterface dialog, int which) {
					Toast.makeText(getActivity(), String.format("群:%d 用户:%d 时间:%s", mb.getGroup(), mb.getFromQQ(), cities[which]), Toast.LENGTH_SHORT).show();
					MainActivity2.instance.CQ.setGroupBan(mb.getGroup(), mb.getFromQQ(), Long.parseLong(cities[which]));
				}
			}).show();
	}

	public void showSetGroupSpecialTitle(final BotMessage bm) {
		final EditText et = new EditText(MainActivity2.instance);
		et.setHint("群头衔");
		new AlertDialog.Builder(getActivity())
			.setView(et)
			.setTitle("编辑")
			.setPositiveButton("确定", new DialogInterface.OnClickListener() {

				@Override
				public void onClick(DialogInterface p1, int p2) {
					MainActivity2.instance.CQ.setGroupSpecialTitle(group.id, bm.getFromQQ(), et.getText().toString(), -1);
				}
			}).setNegativeButton("取消", null).show();
	}

	public void showSetGroupNick(final BotMessage bm) {
		final EditText et = new EditText(MainActivity2.instance);
		et.setHint("群名片");
		new AlertDialog.Builder(getActivity())
			.setView(et)
			.setTitle("编辑")
			.setPositiveButton("确定", new DialogInterface.OnClickListener() {

				@Override
				public void onClick(DialogInterface p1, int p2) {
					MainActivity2.instance.CQ.setGroupCard(group.id, bm.getFromQQ(), et.getText().toString());
				}
			}).setNegativeButton("取消", null).show();
	}

	public void showText(final BotMessage bm) {
		TextView tv=new TextView(getActivity());
		tv.setText(String.format("群:%d\n用户:%s(%d)\n内容:%s", bm.getFromQQ(), bm.getUserName(), bm.getFromQQ(), bm.getMsg()));
		new AlertDialog.Builder(getActivity()).
			setTitle("消息").
			setView(tv)
			.setPositiveButton("确定", null).show();
	}
}
