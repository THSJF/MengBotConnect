package com.meng.botconnect;

import android.app.*;
import android.content.*;
import android.os.*;
import com.meng.botconnect.lib.*;
import com.meng.botconnect.network.*;
import java.util.*;

public class MainActivity extends Activity {
    public static SharedPreferenceHelper sharedPreference;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
		ExceptionCatcher.getInstance().init(this);
        sharedPreference = new SharedPreferenceHelper(this, "preference");
		CoolQ.ip = sharedPreference.getValue("ip", "0.0.0.0");
		CoolQ.port = sharedPreference.getValue("port", "0");
		startActivity(new Intent(MainActivity.this, MainActivity2.class).putExtra("setTheme", getIntent().getBooleanExtra("setTheme", false)));
        finish();
        overridePendingTransition(0, 0);
    }
}
