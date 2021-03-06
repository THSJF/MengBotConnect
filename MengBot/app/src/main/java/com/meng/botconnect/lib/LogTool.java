package com.meng.botconnect.lib;

import android.app.*;
import android.widget.*;
import com.meng.botconnect.*;

public class LogTool {

	public static String getExceptionAllinformation(Exception ex){
        StringBuilder sOut = new StringBuilder();
        sOut.append(ex.getMessage() + "\r\n");
        StackTraceElement[] trace = ex.getStackTrace();
        for (StackTraceElement s : trace) {
            sOut.append("\tat " + s + "\r\n");
        }
        return sOut.toString();
    }
	
    public static void e(final Activity a, final Exception o) {
        a.runOnUiThread(new Runnable() {

				@Override
				public void run() {
					// TODO: Implement this method
					Toast.makeText(a, "发生错误:" + o.toString(), Toast.LENGTH_SHORT).show();
						i(a, "发生错误:" +getExceptionAllinformation(o));
					}
			});
    }

    public static void c(final Activity a, final Object o) {

        a.runOnUiThread(new Runnable() {

				@Override
				public void run() {
					// TODO: Implement this method
					MainActivity2.instance.rightText.setText(
                        MainActivity2.instance.rightText.getText().toString() +
						"点击:" + o.toString() + "\n"
					);
				}
			});
    }

    public static void i(final Activity a, final Object o) {
        a.runOnUiThread(new Runnable() {

				@Override
				public void run() {
					// TODO: Implement this method
					MainActivity2.instance.rightText.setText(
                        MainActivity2.instance.rightText.getText().toString() +
						o.toString() + "\n"
					);
				}
			});
    }

    public static void t(final Activity a, final Object o) {
        a.runOnUiThread(new Runnable() {

				@Override
				public void run() {
					// TODO: Implement this method
					Toast.makeText(a, o.toString(), Toast.LENGTH_SHORT).show();
					i(a, o.toString());
				}
			});
    }
}
