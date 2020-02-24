package com.meng.botconnect.fragment;

import android.app.*;
import android.content.*;
import android.content.pm.*;
import android.net.*;
import android.os.*;
import android.text.*;
import android.view.*;
import android.view.View.*;
import android.widget.*;
import com.meng.botconnect.*;
import com.meng.botconnect.lib.*;
import com.meng.botconnect.network.*;
import java.io.*;

public class UploadApkFragment extends Fragment {
    private final int requestFileCode = 1001;
    private Button btnSend;
	private Button btnSelect;
	private TextView text;
	private EditText et;
	private String path;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.crash_upload, container, false);
	}

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
		text = (TextView)view.findViewById(R.id.crash_uploadTextView);
        btnSend = (Button) view.findViewById(R.id.crash_uploadButton_upload);
		btnSelect = (Button) view.findViewById(R.id.crash_uploadButton_select);
		et = (EditText) view.findViewById(R.id.crash_uploadEdittext);
		btnSelect.setOnClickListener(onClick);
		btnSend.setOnClickListener(onClick);
	}

	OnClickListener onClick=new OnClickListener() {
		@Override
		public void onClick(View v) {
			switch (v.getId()) {
				case R.id.crash_uploadButton_select:
					Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
					intent.addCategory(Intent.CATEGORY_OPENABLE);
					intent.setType("*/*");
					startActivityForResult(intent, requestFileCode);
					break;
				case R.id.crash_uploadButton_upload:
					BotDataPack bdp=BotDataPack.encode(BotDataPack.opUploadApk);
					PackageInfo packageInfo=null;
					packageInfo = MainActivity2.instance.getPackageManager().getPackageArchiveInfo(path, 0);
					bdp.write(packageInfo.packageName);
					bdp.write(packageInfo.versionCode);
					bdp.write(packageInfo.versionName);
					bdp.write(et.getText().toString());
					bdp.write(new File(path));
					MainActivity2.instance.CQ.send(bdp.getData());
					//LogTool.t("发送成功");
					v.setEnabled(false);
					break;	
			}
		}
	};

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK && data != null && requestCode == requestFileCode) {
            Uri inputUri = data.getData();
            path = Tools.ContentHelper.absolutePathFromUri(getActivity(), inputUri);
            if (TextUtils.isEmpty(path)) {
				LogTool.t(MainActivity2.instance, "路径未找到");
				return;
			}
			PackageInfo packageInfo=null;
			try {
				packageInfo = MainActivity2.instance.getPackageManager().getPackageArchiveInfo(path, 0);
				text.setText(String.format("path:%s\npackage name:%s\nversion name:%s\nversion code:%d", path, packageInfo.packageName, packageInfo.versionName, packageInfo.versionCode));
			} catch (Exception e) {
				LogTool.i(MainActivity2.instance, "不是合法的apk文件");
			}
		}
	}
}

