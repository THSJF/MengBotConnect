package com.meng.botconnect.fragment;

import android.app.*;
import android.graphics.*;
import android.graphics.drawable.*;
import android.os.*;
import android.view.*;
import android.widget.*;
import android.widget.AdapterView.*;
import com.meng.botconnect.*;
import com.meng.botconnect.adapter.*;
import com.meng.botconnect.bean.*;

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
				}
			});
	}
}
