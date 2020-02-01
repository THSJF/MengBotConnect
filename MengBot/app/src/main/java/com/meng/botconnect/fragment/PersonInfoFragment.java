package com.meng.botconnect.fragment;

import android.app.*;
import android.content.*;
import android.os.*;
import android.support.annotation.*;
import android.view.*;
import android.widget.*;
import android.widget.AdapterView.*;
import com.github.clans.fab.*;
import com.meng.botconnect.*;
import com.meng.botconnect.bean.*;
import com.meng.botconnect.network.*;
import java.io.*;
import java.net.*;

public class PersonInfoFragment extends Fragment {

    public ListView mListView;
    private FloatingActionButton mFab;
	public EditText editTextName, editTextQQNumber, editTextBilibiliId, editTextBilibiliLiveRoom, etTipInGroup;
    public CheckBox cbLive, cbVideo, cbAction;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.home_fragment, container, false);
	}

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mListView = (ListView) view.findViewById(R.id.list);
        mFab = (FloatingActionButton) view.findViewById(R.id.fab);
		mListView.setOnItemClickListener(new OnItemClickListener() {
				@Override
				public void onItemClick(final AdapterView<?> adapterView, View p2, final int position, long p4) {
					View view = getActivity().getLayoutInflater().inflate(R.layout.person_info_edit_view, null);
					editTextName = (EditText) view.findViewById(R.id.edit_viewEditText_name);
					editTextQQNumber = (EditText) view.findViewById(R.id.edit_viewEditText_qq);
					editTextBilibiliId = (EditText) view.findViewById(R.id.edit_viewEditText_bid);
					editTextBilibiliLiveRoom = (EditText) view.findViewById(R.id.edit_viewEditText_b_live_room);
					etTipInGroup = (EditText) view.findViewById(R.id.edit_viewEditText_tipInGroup);
					cbLive = (CheckBox) view.findViewById(R.id.edit_viewCheckbox_live);
					cbVideo = (CheckBox) view.findViewById(R.id.edit_viewCheckbox_video);
					cbAction = (CheckBox) view.findViewById(R.id.edit_viewCheckbox_action);

					final PersonInfo personInfo = (PersonInfo) adapterView.getItemAtPosition(position);
					editTextName.setText(personInfo.name);
					editTextQQNumber.setText(String.valueOf(personInfo.qq));
					editTextBilibiliId.setText(String.valueOf(personInfo.bid));
					editTextBilibiliLiveRoom.setText(String.valueOf(personInfo.bliveRoom));
					cbLive.setChecked(personInfo.isTipLive());
					cbVideo.setChecked(personInfo.isTipVideo());
					cbAction.setChecked(personInfo.isTipAction());
					if (personInfo.tipIn.size() > 0) {
						StringBuilder stringBuilder = new StringBuilder();
						for (long group : personInfo.tipIn) {
							stringBuilder.append(group).append(",");
						}
						String str = stringBuilder.toString();
						etTipInGroup.setText(str.substring(0, str.length() - 1));
					}
					final String oldPersonInfo = MainActivity2.instance.gson.toJson(personInfo);
					new AlertDialog.Builder(getActivity())
						.setView(view)
						.setTitle("编辑")
						.setPositiveButton("确定", new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialogInterface, int which) {
								new AlertDialog.Builder(getActivity())
									.setTitle("确定修改吗")
									.setPositiveButton("确定", new DialogInterface.OnClickListener() {
										@Override
										public void onClick(DialogInterface dialogInterface, int which) {
											personInfo.name = editTextName.getText().toString();
											personInfo.qq = Long.parseLong(editTextQQNumber.getText().toString());
											personInfo.bid = Integer.parseInt(getUId(editTextBilibiliId.getText().toString()));
											personInfo.bliveRoom = Integer.parseInt(getLiveId(editTextBilibiliLiveRoom.getText().toString()));
											personInfo.setTipLive(cbLive.isChecked());
											personInfo.setTipVideo(cbVideo.isChecked());
											personInfo.setTipAction(cbAction.isChecked());
											personInfo.tipIn.clear();
											if (!etTipInGroup.getText().toString().equals("")) {
												String[] strs = etTipInGroup.getText().toString().split(",");
												for (int i = 0, strsLength = strs.length; i < strsLength; i++) {
													String s = strs[i];
													personInfo.tipIn.add(Long.parseLong(s));
												}
											}
											MainActivity2.instance.CQ.send(BotDataPack.encode(BotDataPack.removePersonInfo).write(oldPersonInfo));	
											MainActivity2.instance.CQ.send(BotDataPack.encode(BotDataPack.addPersonInfo).write(MainActivity2.instance.gson.toJson(personInfo)));
										}
									}).setNegativeButton("取消", null).show();
							}
						}).setNegativeButton("取消", null).show();
				}
			});

        mListView.setOnItemLongClickListener(new OnItemLongClickListener() {

				@Override
				public boolean onItemLongClick(final AdapterView<?> adapterView, View view, final int position, long id) {
					new AlertDialog.Builder(getActivity())
						.setTitle("确定删除吗")
						.setPositiveButton("确定", new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialogInterface, int which2) {
								PersonInfo personInfo = (PersonInfo) adapterView.getItemAtPosition(position);
								MainActivity2.instance.CQ.send(BotDataPack.encode(BotDataPack.removePersonInfo).write(MainActivity2.instance.gson.toJson(personInfo)));
							}
						}).setNegativeButton("取消", null).show();
					return true;
				}
			});
		mFab.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View p1) {
					View view = getActivity().getLayoutInflater().inflate(R.layout.person_info_edit_view, null);
					editTextName = (EditText) view.findViewById(R.id.edit_viewEditText_name);
					editTextQQNumber = (EditText) view.findViewById(R.id.edit_viewEditText_qq);
					editTextBilibiliId = (EditText) view.findViewById(R.id.edit_viewEditText_bid);
					editTextBilibiliLiveRoom = (EditText) view.findViewById(R.id.edit_viewEditText_b_live_room);
					cbLive = (CheckBox) view.findViewById(R.id.edit_viewCheckbox_live);
					cbVideo = (CheckBox) view.findViewById(R.id.edit_viewCheckbox_video);
					cbAction = (CheckBox) view.findViewById(R.id.edit_viewCheckbox_action);
					etTipInGroup = (EditText) view.findViewById(R.id.edit_viewEditText_tipInGroup);

					new AlertDialog.Builder(getActivity())
						.setView(view)
						.setTitle("编辑")
						.setPositiveButton("确定", new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface p11, int p2) {

								new AlertDialog.Builder(getActivity())
									.setTitle("确定添加吗")
									.setPositiveButton("确定", new DialogInterface.OnClickListener() {
										@Override
										public void onClick(DialogInterface p11, int p2) {
											for (PersonInfo user : MainActivity2.instance.botData.ranConfig.personInfo) {
												if (Long.parseLong(getUId(editTextBilibiliId.getText().toString())) == user.bid &&
													Long.parseLong(editTextQQNumber.getText().toString()) == user.qq &&
													editTextName.getText().toString().equals(user.name) &&
													Integer.parseInt(getLiveId(editTextBilibiliLiveRoom.getText().toString())) == user.bliveRoom) {
													return;
												}
											}

											MainActivity2.instance.threadPool.execute(new Runnable() {
													@Override
													public void run() {
														final PersonInfo user = new PersonInfo();
														user.name = editTextName.getText().toString();
														user.qq = Long.parseLong(editTextQQNumber.getText().toString());
														user.bid = Integer.parseInt(getUId(editTextBilibiliId.getText().toString()));
														user.bliveRoom = Integer.parseInt(getLiveId(editTextBilibiliLiveRoom.getText().toString()));
														user.setTipLive(cbLive.isChecked());
														user.setTipVideo(cbVideo.isChecked());
														user.setTipAction(cbAction.isChecked());
														String[] strs = etTipInGroup.getText().toString().split(",");
														for (int i = 0, strsLength = strs.length; i < strsLength; i++) {
															String s = strs[i];
															if (s.equals("")) {
																break;
															}
															user.tipIn.add(Long.parseLong(s));
														}

														MainActivity2.instance.CQ.send(BotDataPack.encode(BotDataPack.addPersonInfo).write(MainActivity2.instance.gson.toJson(user)));
																				}
												});
										}
									}).setNegativeButton("取消", null).show();
							}
						}).setNegativeButton("取消", null).show();
				}
			});
	}

	private String getLiveId(String url) {
		if (url.startsWith("live.bilibili.com/")) {
			StringBuilder stringBuilder = new StringBuilder();
			for (int i = url.indexOf("live.bilibili.com/") + 18; i < url.length(); ++i) {
				if (url.charAt(i) >= 48 && url.charAt(i) <= 57) {
					stringBuilder.append(url.charAt(i));
				} else {
					break;
				}
			}
			return stringBuilder.toString();
		} else {
			return url;
		}
	}

	private String getUId(String url) {
		if (url.startsWith("UID:")) {
			return url.substring(4);
		} else {
			return url;
		}
	}

    public String readCode(String url) {
        try {
            HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
            connection.setRequestMethod("GET");
            connection.setConnectTimeout(10000);
            connection.setReadTimeout(10000);
            InputStream in = connection.getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(in));
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line);
			}
            return sb.toString();
		} catch (Exception e) {
            return e.toString();
		}
	}
}
