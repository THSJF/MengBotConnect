package com.meng.botconnect.adapter;

import android.graphics.*;
import android.view.*;
import android.view.View.*;
import android.widget.*;
import com.meng.botconnect.*;
import com.meng.botconnect.bean.*;
import com.meng.botconnect.fragment.*;
import com.meng.botconnect.network.*;
import java.io.*;
import java.util.*;

public class ChatListAdapter extends BaseAdapter {
	private ChatFragment fragment;
	private ArrayList<BotMessage> infosList;

	public ChatListAdapter(ChatFragment context, ArrayList<BotMessage> infosSet) {
		this.fragment = context;
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
			convertView = fragment.getActivity().getLayoutInflater().inflate(R.layout.chat_list_item, null);
			holder = new ViewHolder();
			holder.imageViewQQHead = (ImageView) convertView.findViewById(R.id.chat_list_itemImageView);
			holder.textViewName = (TextView) convertView.findViewById(R.id.chat_list_itemTextViewUserName);
			holder.textViewMsg = (TextView) convertView.findViewById(R.id.chat_list_itemTextViewMsg);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		final BotMessage msg = infosList.get(position);
		String un=msg.getUserName();
		RanConfigBean rcf=MainActivity2.instance.botData.ranConfig;
		if (rcf.masterList.contains(msg.getFromQQ())) {
			holder.textViewName.setTextColor(Color.MAGENTA);
		} else if (rcf.adminList.contains(msg.getFromQQ())) {
			holder.textViewName.setTextColor(Color.GREEN);
		} else {
			if (MainActivity.sharedPreference.getBoolean("useLightTheme", true)) {
				holder.textViewName.setTextColor(Color.BLACK);
			} else {
				holder.textViewName.setTextColor(Color.WHITE);
			}
		}
		holder.textViewName.setText(un);
		holder.textViewMsg.setText(msg.getMsg());
		File qqImageFile = new File(MainActivity2.mainFolder + "user/" + msg.getFromQQ() + ".jpg");
		if (qqImageFile.exists()) {
			holder.imageViewQQHead.setImageBitmap(BitmapFactory.decodeFile(qqImageFile.getAbsolutePath()));
		} else  {
			MainActivity2.instance.threadPool.execute(new DownloadImageRunnable(fragment.getActivity(), holder.imageViewQQHead, msg.getFromQQ(), 1));
		}
		holder.imageViewQQHead.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					MainActivity2.instance.threadPool.execute(new DownloadImageRunnable(fragment.getActivity(), holder.imageViewQQHead, msg.getFromQQ(), 1));
				}
			});
		holder.imageViewQQHead.setOnLongClickListener(new OnLongClickListener(){

				@Override
				public boolean onLongClick(View p1) {
					fragment.etMsgToSend.append("[CQ:at,qq=");
					fragment.etMsgToSend.append(String.valueOf(msg.getFromQQ()));
					fragment.etMsgToSend.append("]");
					return true;
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
