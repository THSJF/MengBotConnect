package com.meng.botconnect.fragment;

import android.app.*;
import android.content.*;
import android.os.*;
import android.view.*;
import android.view.View.*;
import android.widget.*;
import android.widget.AdapterView.*;
import com.meng.botconnect.*;
import com.meng.botconnect.adapter.*;
import com.meng.botconnect.bean.*;
import com.meng.botconnect.lib.*;
import com.meng.botconnect.network.*;
import com.meng.botconnect.views.*;
import java.io.*;
import java.util.*;

import android.view.View.OnClickListener;

public class QuestionFragment extends Fragment {

    public static final int touhouBase=1;
	public static final int _2unDanmakuIntNew=2;
	public static final int _2unDanmakuAll=3;
	public static final int _2unNotDanmaku=4;
	public static final int _2unAll=5;
	public static final int otherDanmaku=6;

	public TabHost tab;

	public LinearLayout llAnswers;
	public Button btnSend,btnClean,btnImage,btnAdd,btnSub;
	public ImageEdittext etQues;
	public EditText etReason;
	public Spinner spDiffcult,spType,spFiDiff,spFiType;

	public int mode=0;
	public QA onEdit;

	public ArrayList<QA> alAllQa=new ArrayList<>();
	public ArrayList<QA> nowQaList=new ArrayList<>();
	private ListView lvAllQa;
	public QuestionAdapter quesAdapter;

	public File chooseFilePath;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        MainActivity2.instance.CQ.send(BotDataPack.encode(BotDataPack.opAllQuestion));
        return inflater.inflate(R.layout.tab_activity, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        // TODO: Implement this method
        super.onViewCreated(view, savedInstanceState);
        tab = (TabHost) view.findViewById(android.R.id.tabhost);
		tab.setup();
        LayoutInflater layoutInflater=LayoutInflater.from(getActivity()); 
		layoutInflater.inflate(R.layout.add_ques_activity, tab.getTabContentView()); 
		layoutInflater.inflate(R.layout.all_ques_activity, tab.getTabContentView());
		btnClean = (Button) view.findViewById(R.id.clean);
		btnSend = (Button) view.findViewById(R.id.mainButtonSend);
		btnAdd = (Button) view.findViewById(R.id.add_ques_activityButtonAddAns);
		btnSub = (Button) view.findViewById(R.id.add_ques_activityButtonSubAns);
		btnImage = (Button) view.findViewById(R.id.add_ques_activityButton_addPic);
		etQues = (ImageEdittext) view.findViewById(R.id.ques);
		spDiffcult = (Spinner) view.findViewById(R.id.diff);
		spType = (Spinner) view.findViewById(R.id.type);
		spFiDiff = (Spinner) view.findViewById(R.id.all_ques_activitySpinner_diff);
		spFiType = (Spinner) view.findViewById(R.id.all_ques_activitySpinner_type);
		llAnswers = (LinearLayout) view.findViewById(R.id.add_ques_activityLinearLayoutAnswer_view);

		etReason = (EditText)view.findViewById(R.id.reason);
		btnSend.setOnClickListener(onClick);
		btnClean.setOnClickListener(onClick);
		btnImage.setOnClickListener(onClick);
		btnAdd.setOnClickListener(onClick);
		btnSub.setOnClickListener(onClick);
		FileInputStream fis;
		File dat=new File(Environment.getExternalStorageDirectory() + "/botkey.dat");
		byte[] bys=new byte[(int)dat.length()];
		try {
			fis = new FileInputStream(dat);
			fis.read(bys);
		} catch (Exception e) {
			LogTool.e(getActivity(), e);
			return;
		}
		/*	BotDataPack de=BotDataPack.decode(bys);
		 ArrayList<String> diffList=new ArrayList<>();
		 System.out.println(de.readString());
		 int le=de.readInt();
		 for (int i=0;i < le;++i) {
		 diffList.add(de.readString());
		 }

		 ArrayList<String> typeList=new ArrayList<>();
		 int le2=de.readInt();
		 for (int i=0;i < le2;++i) {
		 typeList.add(de.readString());
		 }

		 spDiffcult.setAdapter(new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item, diffList));
		 spType.setAdapter(new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item, typeList));
		 */
		spDiffcult.setAdapter(new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item, new String[]{"easy","normal","hard","lunatic","overdrive","kidding"}));
		spType.setAdapter(new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item, new String[]{"未分类","车万基础","新作整数作","官方弹幕作","官方非弹幕","官方所有","同人弹幕","luastg"}));
		spFiDiff.setAdapter(new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item, new String[]{"all","easy","normal","hard","lunatic","overdrive","kidding"}));
		spFiType.setAdapter(new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item, new String[]{"全部分类","未分类","车万基础","新作整数作","官方弹幕作","官方非弹幕","官方所有","同人弹幕","luastg"}));
		spFiType.setOnItemSelectedListener(onItemSelect);
		spFiDiff.setOnItemSelectedListener(onItemSelect);
        tab.addTab(tab.newTabSpec("tab1").setIndicator("添加问题" , null).setContent(R.id.add_ques_activityLinearLayout));
        tab.addTab(tab.newTabSpec("tab2").setIndicator("浏览问题" , null).setContent(R.id.all_ques_activityLinearLayout));
		lvAllQa = (ListView) view.findViewById(R.id.all_quesListView);
		nowQaList.addAll(alAllQa);
		quesAdapter = new QuestionAdapter(getActivity(), nowQaList);
		lvAllQa.setAdapter(quesAdapter);
		lvAllQa.setOnItemClickListener(new OnItemClickListener(){

				@Override
				public void onItemClick(AdapterView<?> p1, View p2, final int p3, long p4) {
					llAnswers.removeAllViews();
					btnClean.setText("退出修改模式");
					QA qa = nowQaList.get(p3);
					tab.setCurrentTab(0);
					mode = 1;
					onEdit = qa;
					spType.setSelection(qa.getType());
					spDiffcult.setSelection(qa.getDifficulty());
					etQues.setText(qa.q);
					HashSet<Integer> trueAnswers=onEdit.getTrueAns();
					for (int i=0;i < onEdit.a.size();++i) {
						AnswerView ansv=new AnswerView(getActivity(), i, onEdit.a.get(i), trueAnswers.contains(i));
						llAnswers.addView(ansv);
					}
					etReason.setText(qa.r);
					chooseFilePath = new File(Environment.getExternalStorageDirectory() + "/Pictures/sanae/questions/" + qa.getId() + ".jpg");
					etQues.replaceDrawable(chooseFilePath);
				}
			});

	}
	OnItemSelectedListener onItemSelect=new OnItemSelectedListener(){

		@Override
		public void onItemSelected(AdapterView<?> p1, View p2, int p3, long p4) {
			refresh();
		}

		@Override
		public void onNothingSelected(AdapterView<?> p1) {
			// TODO: Implement this method
		}
	};

	private void refresh() {
		nowQaList.clear();
		int dif = spFiDiff.getSelectedItemPosition() - 1;
		int ty = spFiType.getSelectedItemPosition() - 1;
		if (ty == -1) {
			if (dif == -1) {
				nowQaList.addAll(alAllQa);
			} else {	
				for (QA qa:alAllQa) {
					if (qa.getDifficulty() == dif) {
						nowQaList.add(qa);	
					}
				}
			}
		} else {
			if (dif == -1) {
				for (QA qa:alAllQa) {
					if (qa.getType() == ty) {
						nowQaList.add(qa);	
					}
				}
			} else {
				for (QA qa:alAllQa) {
					if (qa.getType() == ty && qa.getDifficulty() == dif) {
						nowQaList.add(qa);	
					}
				}
			}	
		}
		quesAdapter.notifyDataSetChanged();
	}

	OnClickListener onClick=new OnClickListener(){

		@Override
		public void onClick(View p1) {
			switch (p1.getId()) {
				case R.id.mainButtonSend:
					if (mode == 0) {
						QA qa = new QA();
						qa.setId(alAllQa.size());
						qa.setType(spType.getSelectedItemPosition());
						qa.setDifficulty(spDiffcult.getSelectedItemPosition());
						qa.q = etQues.getText().toString();
						qa.setTrueAnsFlag(getTrueAnsFlag());
						int all=llAnswers.getChildCount();
						for (int i=0;i < all;++i) {
							AnswerView asv=(AnswerView) llAnswers.getChildAt(i);
							qa.a.add(asv.getAnswer());
						}
						qa.r = etReason.getText().toString();
						alAllQa.add(qa);
						refresh();
						BotDataPack sdp=BotDataPack.encode(BotDataPack.opAddQuestion);
						sdp.write(qa.getFlag());
						sdp.write(qa.q);
						sdp.write(qa.a.size());
						sdp.write(qa.getTrueAnsFlag());
						for (String s:qa.a) {
							sdp.write(s);
						}
						sdp.write(qa.r);
						if (chooseFilePath != null) {
							sdp.write(chooseFilePath);
						}
						try {
							chooseFilePath = null;
							MainActivity2.instance.CQ.send(sdp.getData());
						} catch (Exception e) {
							LogTool.e(getActivity(), e);
						}
						clean();
						LogTool.t(getActivity(), "正在发送");	
					} else if (mode == 1) {
						onEdit.setType(spType.getSelectedItemPosition());
						onEdit.setDifficulty(spDiffcult.getSelectedItemPosition());
						onEdit.q = etQues.getText().toString();
						onEdit.setTrueAnsFlag(getTrueAnsFlag());
						onEdit.a.clear();
						int all=llAnswers.getChildCount();
						for (int i=0;i < all;++i) {
							AnswerView asv=(AnswerView) llAnswers.getChildAt(i);
							onEdit.a.add(asv.getAnswer());
						}
						onEdit.r = etReason.getText().toString();
						refresh();
						BotDataPack sdp=BotDataPack.encode(BotDataPack.opSetQuestion);
						sdp.write(onEdit.getFlag());
						sdp.write(onEdit.q);
						sdp.write(onEdit.a.size());
						sdp.write(onEdit.getTrueAnsFlag());
						for (String s:onEdit.a) {
							sdp.write(s);
						}
						sdp.write(onEdit.r);
						if (chooseFilePath != null && etQues.getText().toString().contains("(image)")) {
							sdp.write(chooseFilePath);
						}
						chooseFilePath = null;
						try {
							MainActivity2.instance.CQ.send(sdp.getData());
						} catch (Exception e) {
							LogTool.t(getActivity(), e.toString());
						}
						LogTool.t(getActivity(), "正在发送");	
						mode = 0;
						onEdit = null;
						clean();
						refresh();
						tab.setCurrentTab(1);
					}
					break;
				case R.id.clean:
					clean();
					chooseFilePath = null;
					break;
				case R.id.add_ques_activityButton_addPic:
					if (chooseFilePath == null || !etQues.getText().toString().contains("(image)")) {
						Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
						intent.setType("image/*");
						intent.addCategory(Intent.CATEGORY_OPENABLE);
						startActivityForResult(intent, 234);
					} else {
						LogTool.t(getActivity(), "仅可以添加一张图片");
					}
					break;
				case R.id.add_ques_activityButtonAddAns:
					if (llAnswers.getChildCount() >= 32) {
						LogTool.t(getActivity(), "最多添加32个答案");
						return;
					}
					llAnswers.addView(new AnswerView(getActivity(), llAnswers.getChildCount(), "", false));
					break;
				case R.id.add_ques_activityButtonSubAns:
					llAnswers.removeViewAt(llAnswers.getChildCount() - 1);
					break;
			}
		}

		private int getTrueAnsFlag() {
			int flag=0;
			int all=llAnswers.getChildCount();
			for (int i=0;i < all;++i) {
				if (((AnswerView) llAnswers.getChildAt(i)).isTrueAnswer()) {
					flag |= (1 << i);
				}
			}
			return flag;
		}
	};

	private void clean() {
		etQues.setText("");
		llAnswers.removeAllViews();
		etReason.setText("");
		mode = 0;
		onEdit = null;
		btnClean.setText("清空");
		llAnswers.addView(new AnswerView(getActivity(), llAnswers.getChildCount(), "", false));
		llAnswers.addView(new AnswerView(getActivity(), llAnswers.getChildCount(), "", false));
	}


	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == Activity.RESULT_OK) {
			switch (requestCode) {
				case 234:
					chooseFilePath = new File(FileChooseUtil.getInstance(getActivity()).getChooseFileResultPath(data.getData()));
					//etQues.setText(etQues.getText().toString());
					etQues.insertImage(chooseFilePath);
					break;
			}
		}
	}
}
