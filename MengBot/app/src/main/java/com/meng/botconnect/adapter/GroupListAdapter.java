package com.meng.botconnect.adapter;

import android.app.*;
import android.content.*;
import android.graphics.*;
import android.view.*;
import android.widget.*;
import com.meng.botconnect.*;
import com.meng.botconnect.bean.*;
import com.meng.botconnect.network.*;
import java.io.*;
import java.util.*;

public class GroupListAdapter extends BaseAdapter {
	private Context context;
	private ArrayList<Group> allGroups;
	private ArrayList<Group> nowShow = new ArrayList<>();

	public GroupListAdapter(Context context, ArrayList<Group> allGroups) {
		this.context = context;
		this.allGroups = allGroups;
		for (Group g:allGroups) {
			if (g.messageList.size() == 0) {
				continue;
			}
			nowShow.add(g);
		}
	}

	@Override
	public void notifyDataSetChanged() {
		nowShow.clear();
		for (Group g:allGroups) {
			if (g.messageList.size() == 0) {
				continue;
			}
			if (!g.removeByUser) {
				nowShow.add(g);
			}
		}
		super.notifyDataSetChanged();
	}

	public int getCount() {
		return nowShow.size();
	}

	public Object getItem(int position) {
		return nowShow.get(position);
	}

	public long getItemId(int position) {
		return nowShow.get(position).hashCode();
	}

	public View getView(final int position, View convertView, ViewGroup parent) {
		final ViewHolder holder;
		if (convertView == null) {
			convertView = ((Activity)context).getLayoutInflater().inflate(R.layout.group_list_item, null);
			holder = new ViewHolder();
			holder.ivHead = (ImageView) convertView.findViewById(R.id.group_list_itemImageView);
			holder.tvGroupName = (TextView) convertView.findViewById(R.id.group_list_itemTextViewGroupName);
			holder.tvGid = (TextView) convertView.findViewById(R.id.group_list_itemTextViewGid);
			holder.tvMsg = (TextView) convertView.findViewById(R.id.group_list_itemTextViewMsg);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		final Group group=nowShow.get(position);
		BotMessage bm=group.messageList.get(group.messageList.size() - 1);

		RanConfigBean rcf=MainActivity2.instance.botData.ranConfig;
		if (rcf.masterList.contains(bm.getFromQQ())) {
			holder.tvMsg.setTextColor(Color.MAGENTA);
		} else if (rcf.adminList.contains(bm.getFromQQ())) {
			holder.tvMsg.setTextColor(Color.GREEN);
		} else {
			if (MainActivity.sharedPreference.getBoolean("useLightTheme", true)) {
				holder.tvMsg.setTextColor(Color.BLACK);
			} else {
				holder.tvMsg.setTextColor(Color.WHITE);
			}
		}
		holder.tvGroupName.setText(group.name);
		holder.tvGid.setText(String.valueOf(group.id));
		holder.tvMsg.setText(bm.getUserName() + ":" + bm.getMsg());
		File qqImageFile = new File(MainActivity2.mainFolder + "group/" + group.id + ".jpg");
		if (qqImageFile.exists()) {
			holder.ivHead.setImageBitmap(BitmapFactory.decodeFile(qqImageFile.getAbsolutePath()));
		} else {
			MainActivity2.instance.threadPool.execute(new DownloadImageRunnable(context, holder.ivHead, group.id, 0));
		}
		holder.ivHead.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					MainActivity2.instance.threadPool.execute(new DownloadImageRunnable(context, holder.ivHead, group.id, 0));
				}
			});
		return convertView;
	}

	private final class ViewHolder {
		private ImageView ivHead;
		private TextView tvGroupName;
		private TextView tvGid;
		private TextView tvMsg;
	}
}
