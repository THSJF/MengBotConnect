package com.meng.botconnect.adapter;

import android.app.*;
import android.graphics.*;
import android.view.*;
import android.widget.*;
import com.meng.botconnect.*;
import com.meng.botconnect.bean.*;
import com.meng.botconnect.network.*;
import java.io.*;
import java.util.*;

public class GroupMsgListAdapter extends BaseAdapter {
	private Fragment context;
	private ArrayList<BotMessage> infosList;

	public GroupMsgListAdapter(Fragment context, ArrayList<BotMessage> infosSet) {
		this.context = context;
		this.infosList = infosSet;
	}

	public int getCount() {
		return infosList.size();
	}

	public Object getItem(int position) {
		return infosList.get(position);
	}

	public long getItemId(int position) {
		return infosList.get(position).hashCode();
	}

	public View getView(final int position, View convertView, ViewGroup parent) {

		final ViewHolder holder;
		if (convertView == null) {
			convertView = context.getActivity().getLayoutInflater().inflate(R.layout.msg_list, null);
			holder = new ViewHolder();
			holder.imageViewQQHead = (ImageView) convertView.findViewById(R.id.msg_listImageView);
			holder.textViewName = (TextView) convertView.findViewById(R.id.msg_listTextViewUserName);
			holder.textViewMsg = (TextView) convertView.findViewById(R.id.msg_listTextViewMsg);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		final BotMessage msg = infosList.get(position);
		holder.textViewName.setText(msg.getUserName());
		holder.textViewMsg.setText(msg.getMsg());
		File qqImageFile = new File(MainActivity2.mainFolder + "user/" + msg.getFromQQ() + ".jpg");
		if (qqImageFile.exists()) {
			holder.imageViewQQHead.setImageBitmap(BitmapFactory.decodeFile(qqImageFile.getAbsolutePath()));
		} else  {
			MainActivity2.instence.threadPool.execute(new DownloadImageRunnable(context, holder.imageViewQQHead, msg.getFromQQ(), 1));
		}
		holder.imageViewQQHead.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					MainActivity2.instence.threadPool.execute(new DownloadImageRunnable(context, holder.imageViewQQHead, msg.getFromQQ(), 1));
				}
			});

		return convertView;
	}

	private final class ViewHolder {
		private ImageView imageViewQQHead;
		private TextView textViewName;
		private TextView textViewMsg;
	}
}
