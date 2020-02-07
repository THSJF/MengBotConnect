package com.meng.grzxConfig.MaterialDesign.adapters;

import android.app.*;
import android.graphics.*;
import android.view.*;
import android.widget.*;
import android.widget.CompoundButton.*;
import com.meng.botconnect.*;
import com.meng.botconnect.bean.*;
import com.meng.botconnect.network.*;
import java.io.*;
import java.util.*;

public class GroupConfigAdapter extends BaseAdapter {
    private Activity context;
    private ArrayList<GroupConfig> groupRepliesList;
    private HashSet<GroupConfig> groupReplySet;
    // private HashMap<Long, Bitmap> hashMap = new HashMap<>();

    public GroupConfigAdapter(Activity context, HashSet<GroupConfig> groupReplySet) {
        this.context = context;
        this.groupReplySet = groupReplySet;
        ArrayList<GroupConfig> arrayList = new ArrayList<>(groupReplySet);
        quickSort(arrayList, 0, arrayList.size() - 1);
        this.groupRepliesList = arrayList;
    }

    private void quickSort(ArrayList<GroupConfig> array, int low, int high) {// 传入low=0，high=array.length-1;
        long pivot;
        GroupConfig t;
        int p_pos, i;// pivot->位索引;p_pos->轴值。
        if (low < high) {
            p_pos = low;
            pivot = array.get(p_pos).n;
            for (i = low + 1; i <= high; i++)
                if (array.get(i).n < pivot) {
                    p_pos++;
                    t = array.get(p_pos);
                    array.set(p_pos, array.get(i));
                    array.set(i, t);
                }
            t = array.get(low);
            array.set(low, array.get(p_pos));
            array.set(p_pos, t);
            quickSort(array, low, p_pos - 1);
            quickSort(array, p_pos + 1, high);
        }
    }

    @Override
    public void notifyDataSetChanged() {
        ArrayList<GroupConfig> arrayList = new ArrayList<>(groupReplySet);
        quickSort(arrayList, 0, arrayList.size() - 1);
        this.groupRepliesList = arrayList;
        super.notifyDataSetChanged();
    }

    public int getCount() {
        return groupRepliesList.size();
    }

    public Object getItem(int position) {
        return groupRepliesList.get(position);
    }

    public long getItemId(int position) {
        return groupRepliesList.get(position).hashCode();
    }

    public View getView(final int position, View convertView, ViewGroup parent) {

        final ViewHolder holder;
        if (convertView == null) {
            convertView = context.getLayoutInflater().inflate(R.layout.list_item_image_text_switch, null);
            holder = new ViewHolder();
            holder.n = (TextView) convertView.findViewById(R.id.group_reply_list_itemTextView);
            holder.replySwitch = (Switch) convertView.findViewById(R.id.group_reply_list_itemSwitch);
            holder.imageView = (ImageView) convertView.findViewById(R.id.group_reply_list_itemImageView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        final GroupConfig groupReply = groupRepliesList.get(position);
        holder.n.setText(String.valueOf(groupReply.n));
        holder.replySwitch.setOnCheckedChangeListener(new OnCheckedChangeListener() {

				@Override
				public void onCheckedChanged(CompoundButton p1, boolean p2) {
					if (groupReply.isFunctionEnable(GroupConfig.ID_MainSwitch) != p2) {
						MainActivity2.instance.CQ.send(BotDataPack.encode(BotDataPack.opEnableFunction).write(groupReply.n).write(GroupConfig.ID_MainSwitch).write(p2 ?1: 0));
						p1.setChecked(!p2);
					}
				}
			});

        holder.replySwitch.setChecked(groupReply.isFunctionEnable(GroupConfig.ID_MainSwitch));
        //    if (hashMap.get(groupReply.n) == null) {
        File imageFile = new File(MainActivity2.mainFolder + "group/" + groupReply.n + ".jpg");
        if (imageFile.exists()) {
            holder.imageView.setImageBitmap(BitmapFactory.decodeFile(imageFile.getAbsolutePath()));
        } else {
			MainActivity2.instance.threadPool.execute(new DownloadImageRunnable(context, holder.imageView, groupReply.n, 0));
        }
        holder.imageView.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					MainActivity2.instance.threadPool.execute(new DownloadImageRunnable(context, holder.imageView, groupReply.n, 0));
				}
			});
        //     }
        //      holder.imageView.setImageBitmap(hashMap.get(groupReply.n));
        return convertView;
    }

    private class ViewHolder {
        private ImageView imageView;
        private TextView n;
        private Switch replySwitch;
    }
}
