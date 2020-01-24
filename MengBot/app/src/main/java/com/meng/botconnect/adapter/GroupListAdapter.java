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

public class GroupListAdapter extends BaseAdapter {
	private Fragment context;
	private ArrayList<Group> infosList;

	public GroupListAdapter(Fragment context, ArrayList<Group> infosSet) {
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
			convertView = context.getActivity().getLayoutInflater().inflate(R.layout.group_list, null);
			holder = new ViewHolder();
			holder.imageViewQQHead = (ImageView) convertView.findViewById(R.id.group_listImageView);
			holder.textViewName = (TextView) convertView.findViewById(R.id.group_listTextView);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		final Group group=infosList.get(position);
		holder.textViewName.setText(group.getName());
		File qqImageFile = new File(MainActivity2.mainFolder + "user/" + group.getId() + ".jpg");
		if (qqImageFile.exists()) {
			holder.imageViewQQHead.setImageBitmap(BitmapFactory.decodeFile(qqImageFile.getAbsolutePath()));
		} else {
			MainActivity2.instence.threadPool.execute(new DownloadImageRunnable(context, holder.imageViewQQHead, group.getId(), 0));
		}
		holder.imageViewQQHead.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					MainActivity2.instence.threadPool.execute(new DownloadImageRunnable(context, holder.imageViewQQHead, group.getId(), 0));
				}
			});
		return convertView;
	}

	private final class ViewHolder {
		private ImageView imageViewQQHead;
		private TextView textViewName;
	}
}
