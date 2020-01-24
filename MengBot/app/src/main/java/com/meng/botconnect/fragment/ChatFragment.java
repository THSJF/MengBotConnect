package com.meng.botconnect.fragment;

import android.app.*;
import android.os.*;
import android.view.*;
import android.view.View.*;
import android.widget.*;
import com.meng.botconnect.*;
import com.meng.botconnect.adapter.*;
import com.meng.botconnect.bean.*;
import com.meng.botconnect.lib.*;

public class ChatFragment extends Fragment {

	private Group group;
	public ListView lv;
	private EditText et;
	private ImageButton ib;
	public ChatFragment(Group g) {
		group = g;
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
		lv.setAdapter(new ChatListAdapter(this, MainActivity2.instence.botData.getGroup(group.id).messageList));
		ib.setOnClickListener(new OnClickListener(){

				@Override
				public void onClick(View p1) {
					LogTool.t(getActivity(), et.getText().toString());
					MainActivity2.instence.cq.sendGroupMsg(group.id,et.getText().toString());
				}
			});
	}
}
