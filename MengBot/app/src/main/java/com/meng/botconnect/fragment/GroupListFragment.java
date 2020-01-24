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
		lv.setAdapter(new GroupListAdapter(this, MainActivity2.instence.botData.groupList));
		lv.setOnItemClickListener(new OnItemClickListener(){

				@Override
				public void onItemClick(final AdapterView<?> p1, View p2, final int p3, long p4) {
					Group g=(Group) p1.getAdapter().getItem(p3);
					FragmentTransaction transaction =MainActivity2.instence.fragmentManager.beginTransaction();
					ChatFragment cf=MainActivity2.instence.chatFragments.get(g.id);
					if (cf == null) {
						cf = new ChatFragment(g);
						MainActivity2.instence.chatFragments.put(g.id, cf);
						transaction.add(R.id.main_activityLinearLayout, cf);
					}
					MainActivity2.instence.hideFragment(transaction);
					transaction.show(cf);
					transaction.commit();
					switch (cf.getAuth()) {
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
				}
			});
	}
}
