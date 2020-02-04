package com.meng.botconnect.fragment;

import android.app.*;
import android.content.*;
import android.os.*;
import android.support.annotation.*;
import android.view.*;
import android.view.animation.*;
import android.widget.*;
import android.widget.AdapterView.*;
import com.github.clans.fab.*;
import com.meng.botconnect.*;
import com.meng.botconnect.bean.*;
import com.meng.botconnect.network.*;
import com.meng.grzxConfig.MaterialDesign.adapters.*;
import com.meng.botconnect.lib.*;

public class GroupConfigFragment extends Fragment {

    public ListView mListView;
    private FloatingActionButton mFab;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.home_fragment, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mListView = (ListView) view.findViewById(R.id.list);
        mFab = (FloatingActionButton) view.findViewById(R.id.fab);

		mListView.setAdapter(new GroupConfigAdapter(getActivity(), MainActivity2.instance.botData.ranConfig.groupConfigs));
        mListView.setOnItemClickListener(new OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView<?> adapterView, View view, final int position, long l) {
					final GroupConfig groupConfig = (GroupConfig) adapterView.getItemAtPosition(position);
					final String items[] = {"复读", "魔神复诵", "屑站新视频新专栏", "Dice",
						"符卡收集","OCR","条形码/二维码","禁言模块","CQ码","原曲认知","搜图","屑站链接",
						"色图","迫害图","女装","病毒情况","接龙","群词库","撤回膜图","表情包","用户统计","群统计","群统计(图表)"};
					final boolean checkedItems[] = {
						groupConfig.isFunctionEnable(GroupConfig.ID_Repeater),
						groupConfig.isFunctionEnable(GroupConfig.ID_MoShenFuSong),
						groupConfig.isFunctionEnable(GroupConfig.ID_BilibiliNewUpdate),
						groupConfig.isFunctionEnable(GroupConfig.ID_Dice),
						groupConfig.isFunctionEnable(GroupConfig.ID_SpellCollect),
						groupConfig.isFunctionEnable(GroupConfig.ID_OCR),
						groupConfig.isFunctionEnable(GroupConfig.ID_Barcode),
						groupConfig.isFunctionEnable(GroupConfig.ID_Banner),
						groupConfig.isFunctionEnable(GroupConfig.ID_CQCode),
						groupConfig.isFunctionEnable(GroupConfig.ID_Music),
						groupConfig.isFunctionEnable(GroupConfig.ID_PicSearch),
						groupConfig.isFunctionEnable(GroupConfig.ID_BiliLink),
						groupConfig.isFunctionEnable(GroupConfig.ID_Setu),
						groupConfig.isFunctionEnable(GroupConfig.ID_PoHaiTu),
						groupConfig.isFunctionEnable(GroupConfig.ID_NvZhuang),
						groupConfig.isFunctionEnable(GroupConfig.ID_GuanZhuangBingDu),
						groupConfig.isFunctionEnable(GroupConfig.ID_Seq),
						groupConfig.isFunctionEnable(GroupConfig.ID_GroupDic),
						groupConfig.isFunctionEnable(GroupConfig.ID_CheHuiMotu),
						groupConfig.isFunctionEnable(GroupConfig.ID_PicEdit),
						groupConfig.isFunctionEnable(GroupConfig.ID_UserCount),
						groupConfig.isFunctionEnable(GroupConfig.ID_GroupCount),
						groupConfig.isFunctionEnable(GroupConfig.ID_GroupCountChart)};
					new AlertDialog.Builder(getActivity())
						.setIcon(R.drawable.ic_launcher)
						//.setTitle("多选对话框")
						.setMultiChoiceItems(items, checkedItems, new DialogInterface.OnMultiChoiceClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int which, boolean isChecked) {
								MainActivity2.instance.CQ.send(BotDataPack.encode(BotDataPack.opEnableFunction).write(groupConfig.n).write(which + 1).write(isChecked ?1: 0));
							//LogTool.t(MainActivity2.instance,"发送修改配置");
								}
						}).show();
				}
			}
		);
        mListView.setOnItemLongClickListener(new OnItemLongClickListener() {

				@Override
				public boolean onItemLongClick(final AdapterView<?> adapterView, View view, final int position, long id) {

					new AlertDialog.Builder(getActivity())
                        .setTitle("确定删除吗")
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int which) {
                                GroupConfig groupConfig = (GroupConfig) adapterView.getItemAtPosition(position);
								MainActivity2.instance.CQ.send(BotDataPack.encode(BotDataPack.removeGroup).write(groupConfig.n));
							}
                        }).setNegativeButton("取消", null).show();
					return true;
				}
			});
        mFab.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View view) {
					final EditText editText = new EditText(getActivity());
					new AlertDialog.Builder(getActivity())
                        .setView(editText)
                        .setTitle("编辑")
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface p11, int p2) {

                                new AlertDialog.Builder(getActivity())
									.setTitle("确定添加吗")
									.setPositiveButton("确定", new DialogInterface.OnClickListener() {
										@Override
										public void onClick(DialogInterface p11, int p2) {
											long inputLong = Long.parseLong(editText.getText().toString());
											for (GroupConfig groupConfig : MainActivity2.instance.botData.ranConfig.groupConfigs) {
												if (groupConfig.n == inputLong) {
													return;
												}
											}
											MainActivity2.instance.CQ.send(BotDataPack.encode(BotDataPack.addGroup).write(inputLong));
										}
									}).setNegativeButton("取消", null).show();
                            }
                        }).setNegativeButton("取消", null).show();
				}
			});
        mFab.hide(false);
        mFab.show(true);
        mFab.setShowAnimation(AnimationUtils.loadAnimation(getActivity(), R.anim.show_from_bottom));
        mFab.setHideAnimation(AnimationUtils.loadAnimation(getActivity(), R.anim.hide_to_bottom));
    }
}
