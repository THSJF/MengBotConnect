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
	public ListView lv;
	private EditText et;
	private ImageButton ib;
	private int auth=1;
	public ChatFragment(Group g) {
		group = g;
	}

	public void setAuth(int auth) {
	/*	int titleId = Resources.getSystem().getIdentifier(
			"action_bar_title", "id", "android");
		TextView yourTextView = (TextView)getActivity().findViewById(titleId);
		yourTextView.setTextColor(Color.BLACK);
	*/	switch (auth) {
			case 1:
				MainActivity2.instence.ab.setBackgroundDrawable(new ColorDrawable(Color.GRAY));
				break;
			case 2:
				MainActivity2.instence.ab.setBackgroundDrawable(new ColorDrawable(Color.GREEN));
				break;
			case 3:
				MainActivity2.instence.ab.setBackgroundDrawable(new ColorDrawable(Color.YELLOW));
				break;
		}
		this.auth = auth;
	}

	public int getAuth() {
		return auth;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// TODO: Implement this method
		return inflater.inflate(R.layout.chat_view, container, false);
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		lv = (ListView) view.findViewById(R.id.chat_viewListView);
		et = (EditText) view.findViewById(R.id.chat_viewEditText);
		ib = (ImageButton) view.findViewById(R.id.chat_viewImageButton);
		MainActivity2.instence.threadPool.execute(new Runnable(){

				@Override
				public void run() {
					final int au=MainActivity2.instence.cq.getGroupMemberInfo(group.id,MainActivity2.onLoginQQ).getAuthority();
					getActivity().runOnUiThread(new Runnable(){

							@Override
							public void run() {
								setAuth(au);
							}
						});
				}
			});
		lv.setAdapter(new ChatListAdapter(this, MainActivity2.instence.botData.getGroup(group.id).messageList));
		lv.setOnItemClickListener(new OnItemClickListener(){

				@Override
				public void onItemClick(AdapterView<?> p1, View p2, int p3, long p4) {
					final BotMessage mb=(BotMessage) p1.getAdapter().getItem(p3);
					AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
					builder.setIcon(R.drawable.ic_launcher);
					builder.setTitle("选择一个时间");
					//    指定下拉列表的显示数据
					final String[] cities = {"0", "60", "120", "180", "600"};
					//    设置一个下拉的列表选择项
					builder.setItems(cities, new DialogInterface.OnClickListener(){
							@Override
							public void onClick(DialogInterface dialog, int which) {
								Toast.makeText(getActivity(), String.format("群:%d 用户:%d 时间:%s", mb.getGroup(), mb.getFromQQ(), cities[which]), Toast.LENGTH_SHORT).show();
								MainActivity2.instence.cq.setGroupBan(mb.getGroup(), mb.getFromQQ(), Long.parseLong(cities[which]));
							}
						});
					builder.show();

				}
			});
		ib.setOnClickListener(new OnClickListener(){

				@Override
				public void onClick(View p1) {
					LogTool.t(getActivity(), et.getText().toString());
					MainActivity2.instence.cq.sendGroupMsg(group.id, et.getText().toString());
				}
			});
	}
}
