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
import android.widget.CompoundButton.*;

public class GroupConfigFragment extends Fragment {

    public ListView mListView;
    private FloatingActionButton mFab;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.home_fragment, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mListView = (ListView) view.findViewById(R.id.list);
        mFab = (FloatingActionButton) view.findViewById(R.id.fab);

		mListView.setAdapter(new GroupConfigAdapter(getActivity(), MainActivity2.instance.botData.ranConfig.groupConfigs));
        mListView.setOnItemClickListener(new OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView<?> adapterView, View view, final int position, long l) {
					final GroupConfig groupConfig = (GroupConfig) adapterView.getItemAtPosition(position);

					OnCheckedChangeListener once=new OnCheckedChangeListener(){

						@Override
						public void onCheckedChanged(CompoundButton p1, boolean p2) {
							if (groupConfig.isFunctionEnable(getFidFromSwitchId(p1.getId())) != p2) {
								switch (p1.getId()) {
									case R.id.group_func_setSwitch1:
										MainActivity2.instance.CQ.send(BotDataPack.encode(BotDataPack.opEnableFunction).write(groupConfig.n).write(1).write(p2 ?1: 0));
										break;
									case R.id.group_func_setSwitch2:
										MainActivity2.instance.CQ.send(BotDataPack.encode(BotDataPack.opEnableFunction).write(groupConfig.n).write(2).write(p2 ?1: 0));
										break;
									case R.id.group_func_setSwitch3:
										MainActivity2.instance.CQ.send(BotDataPack.encode(BotDataPack.opEnableFunction).write(groupConfig.n).write(3).write(p2 ?1: 0));
										break;
									case R.id.group_func_setSwitch4:
										MainActivity2.instance.CQ.send(BotDataPack.encode(BotDataPack.opEnableFunction).write(groupConfig.n).write(4).write(p2 ?1: 0));
										break;
									case R.id.group_func_setSwitch5:
										MainActivity2.instance.CQ.send(BotDataPack.encode(BotDataPack.opEnableFunction).write(groupConfig.n).write(5).write(p2 ?1: 0));
										break;
									case R.id.group_func_setSwitch6:
										MainActivity2.instance.CQ.send(BotDataPack.encode(BotDataPack.opEnableFunction).write(groupConfig.n).write(6).write(p2 ?1: 0));
										break;
									case R.id.group_func_setSwitch7:
										MainActivity2.instance.CQ.send(BotDataPack.encode(BotDataPack.opEnableFunction).write(groupConfig.n).write(7).write(p2 ?1: 0));
										break;
									case R.id.group_func_setSwitch8:
										MainActivity2.instance.CQ.send(BotDataPack.encode(BotDataPack.opEnableFunction).write(groupConfig.n).write(8).write(p2 ?1: 0));
										break;
									case R.id.group_func_setSwitch9:
										MainActivity2.instance.CQ.send(BotDataPack.encode(BotDataPack.opEnableFunction).write(groupConfig.n).write(9).write(p2 ?1: 0));
										break;
									case R.id.group_func_setSwitch10:
										MainActivity2.instance.CQ.send(BotDataPack.encode(BotDataPack.opEnableFunction).write(groupConfig.n).write(10).write(p2 ?1: 0));
										break;
									case R.id.group_func_setSwitch11:
										MainActivity2.instance.CQ.send(BotDataPack.encode(BotDataPack.opEnableFunction).write(groupConfig.n).write(11).write(p2 ?1: 0));
										break;
									case R.id.group_func_setSwitch12:
										MainActivity2.instance.CQ.send(BotDataPack.encode(BotDataPack.opEnableFunction).write(groupConfig.n).write(12).write(p2 ?1: 0));
										break;
									case R.id.group_func_setSwitch13:
										MainActivity2.instance.CQ.send(BotDataPack.encode(BotDataPack.opEnableFunction).write(groupConfig.n).write(13).write(p2 ?1: 0));
										break;
									case R.id.group_func_setSwitch14:
										MainActivity2.instance.CQ.send(BotDataPack.encode(BotDataPack.opEnableFunction).write(groupConfig.n).write(14).write(p2 ?1: 0));
										break;
									case R.id.group_func_setSwitch15:
										MainActivity2.instance.CQ.send(BotDataPack.encode(BotDataPack.opEnableFunction).write(groupConfig.n).write(15).write(p2 ?1: 0));
										break;
									case R.id.group_func_setSwitch16:
										MainActivity2.instance.CQ.send(BotDataPack.encode(BotDataPack.opEnableFunction).write(groupConfig.n).write(16).write(p2 ?1: 0));
										break;
									case R.id.group_func_setSwitch17:
										MainActivity2.instance.CQ.send(BotDataPack.encode(BotDataPack.opEnableFunction).write(groupConfig.n).write(17).write(p2 ?1: 0));
										break;
									case R.id.group_func_setSwitch18:
										MainActivity2.instance.CQ.send(BotDataPack.encode(BotDataPack.opEnableFunction).write(groupConfig.n).write(18).write(p2 ?1: 0));
										break;
									case R.id.group_func_setSwitch19:
										MainActivity2.instance.CQ.send(BotDataPack.encode(BotDataPack.opEnableFunction).write(groupConfig.n).write(19).write(p2 ?1: 0));
										break;
									case R.id.group_func_setSwitch20:
										MainActivity2.instance.CQ.send(BotDataPack.encode(BotDataPack.opEnableFunction).write(groupConfig.n).write(20).write(p2 ?1: 0));
										break;
									case R.id.group_func_setSwitch21:
										MainActivity2.instance.CQ.send(BotDataPack.encode(BotDataPack.opEnableFunction).write(groupConfig.n).write(21).write(p2 ?1: 0));
										break;
									case R.id.group_func_setSwitch22:
										MainActivity2.instance.CQ.send(BotDataPack.encode(BotDataPack.opEnableFunction).write(groupConfig.n).write(22).write(p2 ?1: 0));
										break;
									case R.id.group_func_setSwitch23:
										MainActivity2.instance.CQ.send(BotDataPack.encode(BotDataPack.opEnableFunction).write(groupConfig.n).write(23).write(p2 ?1: 0));
										break;
								}
								p1.setChecked(!p2);
							}
						}
					};
					ScrollView vg=(ScrollView) getActivity().getLayoutInflater().inflate(R.layout.group_func_set, null);
					MainActivity2.instance.CQ.setSv(vg);
					Switch s1=(Switch) vg.findViewById(R.id.group_func_setSwitch1);
					s1.setOnCheckedChangeListener(once);
					Switch s2=(Switch) vg.findViewById(R.id.group_func_setSwitch2);
					s2.setOnCheckedChangeListener(once);
					Switch s3=(Switch) vg.findViewById(R.id.group_func_setSwitch3);
					s3.setOnCheckedChangeListener(once);
					Switch s4=(Switch) vg.findViewById(R.id.group_func_setSwitch4);
					s4.setOnCheckedChangeListener(once);
					Switch s5=(Switch) vg.findViewById(R.id.group_func_setSwitch5);
					s5.setOnCheckedChangeListener(once);
					Switch s6=(Switch) vg.findViewById(R.id.group_func_setSwitch6);
					s6.setOnCheckedChangeListener(once);
					Switch s7=(Switch) vg.findViewById(R.id.group_func_setSwitch7);
					s7.setOnCheckedChangeListener(once);
					Switch s8=(Switch) vg.findViewById(R.id.group_func_setSwitch8);
					s8.setOnCheckedChangeListener(once);
					Switch s9=(Switch) vg.findViewById(R.id.group_func_setSwitch9);
					s9.setOnCheckedChangeListener(once);
					Switch s10=(Switch) vg.findViewById(R.id.group_func_setSwitch10);
					s10.setOnCheckedChangeListener(once);
					Switch s11=(Switch) vg.findViewById(R.id.group_func_setSwitch11);
					s11.setOnCheckedChangeListener(once);
					Switch s12=(Switch) vg.findViewById(R.id.group_func_setSwitch12);
					s12.setOnCheckedChangeListener(once);
					Switch s13=(Switch) vg.findViewById(R.id.group_func_setSwitch13);
					s13.setOnCheckedChangeListener(once);
					Switch s14=(Switch) vg.findViewById(R.id.group_func_setSwitch14);
					s14.setOnCheckedChangeListener(once);
					Switch s15=(Switch) vg.findViewById(R.id.group_func_setSwitch15);
					s15.setOnCheckedChangeListener(once);
					Switch s16=(Switch) vg.findViewById(R.id.group_func_setSwitch16);
					s16.setOnCheckedChangeListener(once);
					Switch s17=(Switch) vg.findViewById(R.id.group_func_setSwitch17);
					s17.setOnCheckedChangeListener(once);
					Switch s18=(Switch) vg.findViewById(R.id.group_func_setSwitch18);
					s18.setOnCheckedChangeListener(once);
					Switch s19=(Switch) vg.findViewById(R.id.group_func_setSwitch19);
					s19.setOnCheckedChangeListener(once);
					Switch s20=(Switch) vg.findViewById(R.id.group_func_setSwitch20);
					s20.setOnCheckedChangeListener(once);
					Switch s21=(Switch) vg.findViewById(R.id.group_func_setSwitch21);
					s21.setOnCheckedChangeListener(once);
					Switch s22=(Switch) vg.findViewById(R.id.group_func_setSwitch22);
					s22.setOnCheckedChangeListener(once);
					Switch s23=(Switch) vg.findViewById(R.id.group_func_setSwitch23);
					s23.setOnCheckedChangeListener(once);
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
					s1.setChecked(checkedItems[0]);
					s2.setChecked(checkedItems[1]);
					s3.setChecked(checkedItems[2]);
					s4.setChecked(checkedItems[3]);
					s5.setChecked(checkedItems[4]);
					s6.setChecked(checkedItems[5]);
					s7.setChecked(checkedItems[6]);
					s8.setChecked(checkedItems[7]);
					s9.setChecked(checkedItems[8]);
					s10.setChecked(checkedItems[9]);
					s11.setChecked(checkedItems[10]);
					s12.setChecked(checkedItems[11]);
					s13.setChecked(checkedItems[12]);
					s14.setChecked(checkedItems[13]);
					s15.setChecked(checkedItems[14]);
					s16.setChecked(checkedItems[15]);
					s17.setChecked(checkedItems[16]);
					s18.setChecked(checkedItems[17]);
					s19.setChecked(checkedItems[18]);
					s20.setChecked(checkedItems[19]);
					s21.setChecked(checkedItems[20]);
					s22.setChecked(checkedItems[21]);
					s23.setChecked(checkedItems[22]);
					new AlertDialog.Builder(getActivity())
						.setIcon(R.drawable.ic_launcher)
						.setTitle("设置群配置")
						.setView(vg).show();
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

	private int getFidFromSwitchId(int switchId) {
		switch (switchId) {
			case R.id.group_func_setSwitch1:
				return 1;
			case R.id.group_func_setSwitch2:
				return 2;
			case R.id.group_func_setSwitch3:
				return 3;
			case R.id.group_func_setSwitch4:
				return 4;
			case R.id.group_func_setSwitch5:
				return 5;
			case R.id.group_func_setSwitch6:
				return 6;
			case R.id.group_func_setSwitch7:
				return 7;
			case R.id.group_func_setSwitch8:
				return 8;
			case R.id.group_func_setSwitch9:
				return 9;
			case R.id.group_func_setSwitch10:
				return 10;
			case R.id.group_func_setSwitch11:
				return 11;
			case R.id.group_func_setSwitch12:
				return 12;
			case R.id.group_func_setSwitch13:
				return 13;
			case R.id.group_func_setSwitch14:
				return 14;
			case R.id.group_func_setSwitch15:
				return 15;
			case R.id.group_func_setSwitch16:
				return 16;
			case R.id.group_func_setSwitch17:
				return 17;
			case R.id.group_func_setSwitch18:
				return 18;
			case R.id.group_func_setSwitch19:
				return 19;
			case R.id.group_func_setSwitch20:
				return 20;
			case R.id.group_func_setSwitch21:
				return 21;
			case R.id.group_func_setSwitch22:
				return 22;
			case R.id.group_func_setSwitch23:
				return 23;
		}
		return 0;
	}
}
