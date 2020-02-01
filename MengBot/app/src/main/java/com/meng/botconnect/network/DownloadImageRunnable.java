package com.meng.botconnect.network;

import android.app.*;
import android.content.*;
import android.graphics.*;
import android.widget.*;
import com.google.gson.*;
import com.meng.botconnect.*;
import com.meng.grzxConfig.MaterialDesign.javaBean.bilibili.user.*;
import java.io.*;
import java.net.*;

public class DownloadImageRunnable implements Runnable {
    private ImageView imageView;
    private long id = 0;
    private File imageFile;
    private Context context;
	private int headType=0;

    public DownloadImageRunnable(Context context, ImageView imageView, long id, int headType) {
        this.context = context;
        this.imageView = imageView;
		this.id = id;
		this.headType = headType;
    }

    @Override
    public void run() {
        switch (headType) {
            case 0:
                imageFile = new File(MainActivity2.mainFolder + "group/" + id + ".jpg");
                if (imageFile.exists()) {
                    imageFile.delete();
                }
                downloadFile("http://p.qlogo.cn/gh/" + id + "/" + id + "/100/");
                break;
            case 1:
                imageFile = new File(MainActivity2.mainFolder + "user/" + id + ".jpg");
                if (imageFile.exists()) {
                    imageFile.delete();
                }
                downloadFile("http://q2.qlogo.cn/headimg_dl?bs=" + id + "&dst_uin=" + id + "&dst_uin=" + id + "&;dst_uin=" + id + "&spec=100&url_enc=0&referer=bu_interface&term_type=PC");
                break;
			case 2:
				imageFile = new File(MainActivity2.mainFolder + "bilibili/" + id + ".jpg");
                if (imageFile.exists()) {
                    imageFile.delete();
                }
				downloadFile(getBilibiliHeadUrl(id));
        }
    }
	
	private String getBilibiliHeadUrl(long uid) {
        try {
            URL url = new URL("https://api.bilibili.com/x/space/acc/info?mid=" + uid + "&jsonp=jsonp");
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            BufferedReader reader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
            String line;
            StringBuilder stringBuilder = new StringBuilder();
            while ((line = reader.readLine()) != null) {
                stringBuilder.append(line);
            }
            BilibiliPersonInfo bilibiliPersonInfoJavaBean = new Gson().fromJson(stringBuilder.toString(), BilibiliPersonInfo.class);
            return bilibiliPersonInfoJavaBean.data.face;
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }
	
    private void downloadFile(String url) {
        try {
            URL u = new URL(url);
            HttpURLConnection connection = (HttpURLConnection) u.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:28.0) Gecko/20100101 Firefox/28.0");
            InputStream is = connection.getInputStream();
            FileOutputStream fos = new FileOutputStream(imageFile);
            byte buf[] = new byte[4096];
            int len = 0;
            while ((len = is.read(buf)) > 0) {
                fos.write(buf, 0, len);
            }
            is.close();
            connection.disconnect();
            ((Activity)context).runOnUiThread(new Runnable() {

					@Override
					public void run() {
						imageView.setImageBitmap(BitmapFactory.decodeFile(imageFile.getAbsolutePath()));
					}
				});
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

