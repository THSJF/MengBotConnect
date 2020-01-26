package com.meng.botconnect.fragment;

import android.app.*;
import android.content.*;
import android.os.*;
import android.view.*;
import android.widget.*;
import android.widget.AdapterView.*;
import com.meng.botconnect.*;
import com.meng.botconnect.adapter.*;
import com.meng.botconnect.bean.*;
import com.meng.botconnect.lib.*;

public class GroupListFragment extends Fragment {

	public ListView lv;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return inflater.inflate(R.layout.groups_list, container, false);
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		lv = (ListView) view.findViewById(R.id.groups_listListView);
		lv.setAdapter(new GroupListAdapter(this.getActivity(), MainActivity2.instance.botData.groupList));

		lv.setOnItemLongClickListener(new OnItemLongClickListener(){

				@Override
				public boolean onItemLongClick(AdapterView<?> p1, View p2, int p3, long p4) {
					final Group g=(Group) p1.getAdapter().getItem(p3);
					String[] cities = {"从列表移除","复制群名","复制群号","退出群"};
					new AlertDialog.Builder(getActivity()).
						setIcon(R.drawable.ic_launcher).
						setTitle("选择操作").
						setItems(cities, new DialogInterface.OnClickListener(){
							@Override
							public void onClick(DialogInterface dialog, int which) {
								switch (which) {
									case 0:
										g.removeByUser = true;
										((BaseAdapter)MainActivity2.instance.messageFragment.lv.getAdapter()).notifyDataSetChanged();
										break;
									case 1:
										ClipboardManager cm2 = (ClipboardManager)getActivity().getSystemService(Context.CLIPBOARD_SERVICE);
										cm2.setText(g.name);
										LogTool.t(getActivity(), "复制群名" + g.name + "成功");
										break;
									case 2:
										ClipboardManager cm = (ClipboardManager)getActivity().getSystemService(Context.CLIPBOARD_SERVICE);
										cm.setText(String.valueOf(g.id));
										LogTool.t(getActivity(), "复制群号" + g.id + "成功");
										break;
									case 3:
										new AlertDialog.Builder(getActivity())
											.setTitle(String.format("确定退出群%s(%d)吗", g.name, g.id))
											.setPositiveButton("是", new DialogInterface.OnClickListener() {
												@Override
												public void onClick(DialogInterface p1, int p2) {
													MainActivity2.instance.CQ.setGroupLeave(g.id, false);	
												}
											}).setNegativeButton("否", null).show();
										break;
								}
							}
						}).show();
					return true;
				}
			});

		lv.setOnItemClickListener(new OnItemClickListener(){

				@Override
				public void onItemClick(final AdapterView<?> p1, View p2, final int p3, long p4) {
					Group g=(Group) p1.getAdapter().getItem(p3);
					FragmentTransaction transaction =MainActivity2.instance.fragmentManager.beginTransaction();
					MainActivity2.instance.CQ.getGroupMemberList(g.id);
					ChatFragment cf=MainActivity2.instance.chatFragments.get(g.id);
					if (cf == null) {
						cf = new ChatFragment(g);
						MainActivity2.instance.chatFragments.put(g.id, cf);
						transaction.add(R.id.main_activityLinearLayout, cf);
					}
					MainActivity2.instance.hideFragment(transaction);
					transaction.show(cf).commit();
					MainActivity2.instance.onBackPressRunable = new Runnable(){

						@Override
						public void run() {
							MainActivity2.instance.initGroupList(true);
						}
					};
				}
			});
	}

	/*public void showSetGroupNick(final BotMessage bm) {
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
	 */
}
