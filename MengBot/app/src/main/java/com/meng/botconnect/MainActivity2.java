package com.meng.botconnect;

import android.app.*;
import android.content.*;
import android.content.res.*;
import android.os.*;
import android.support.v4.widget.*;
import android.view.*;
import android.widget.*;
import android.widget.AdapterView.*;
import com.google.gson.*;
import com.google.gson.reflect.*;
import com.meng.botconnect.bean.*;
import com.meng.botconnect.fragment.*;
import com.meng.botconnect.lib.*;
import com.meng.botconnect.network.*;
import java.io.*;
import java.lang.reflect.*;
import java.nio.charset.*;
import java.util.*;
import java.util.concurrent.*;

import com.meng.botconnect.bean.Member;

public class MainActivity2 extends Activity {
	public static long onLoginQQ = 2528419891L;
	public static String onLoginNick="Sanae";
    public static MainActivity2 instence;
    private final String logString = "以下为操作记录：\n";
    private DrawerLayout mDrawerLayout;
    private ListView lvDrawer;
    private RelativeLayout rt;
    private ActionBarDrawerToggle mDrawerToggle;
    private DrawerArrowDrawable drawerArrow;
    private GroupListFragment messageFragment;
    private StatusFragment statusFragment;
    private SettingsFragment settingsFragment;
    public TextView rightText;
	public ConcurrentHashMap<Long,ChatFragment> chatFragments=new ConcurrentHashMap<>();
	public FragmentManager fragmentManager;
	public CoolQ cq;
	public ExecutorService threadPool = Executors.newCachedThreadPool();
	public static String mainFolder;
	public static final int SELECT_FILE_REQUEST_CODE = 822;
	private final String[] menus = new String[]{"群消息", "状态","设置","退出"};
	public ActionBar ab;
	public BotData botData=new BotData();
	private File botDataFile;
	public Gson gson;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        instence = this;
		mainFolder = Environment.getExternalStorageDirectory() + "/Pictures/grzx/";
		ab = getActionBar();
        ab.setDisplayHomeAsUpEnabled(true);
        ab.setHomeButtonEnabled(true);
		findViews();
        rightText.setText(logString);
        fragmentManager = getFragmentManager();
        setListener();
        changeTheme();
		GsonBuilder gb = new GsonBuilder();
		gb.setLongSerializationPolicy(LongSerializationPolicy.STRING);
		gson = gb.create();
		Type type = new TypeToken<BotData>() {
		}.getType();
		botDataFile = new File(Environment.getExternalStorageDirectory() + "/SanaeConfig.json");
		if (!botDataFile.exists()) {
			saveSanaeConfig();
		}
    //    botData = gson.fromJson(Tools.FileTool.readString(botDataFile), type);
		try {
			LogTool.t(MainActivity2.instence,String.format("ws://%s:%s", CoolQ.ip, CoolQ.port));
			cq = new CoolQ();
			cq.connect();
		} catch (Exception e) {
			LogTool.e(this, e);
		}
		threadPool.execute(new Runnable(){

				@Override
				public void run() {
					while (true) {
						try {
							Thread.sleep(10000);
						} catch (InterruptedException e) {}
						saveSanaeConfig();
					}
				}
			});
    }
	public void addMsg(final BotMessage bm) {
		runOnUiThread(new Runnable(){

				@Override
				public void run() {
					Group g=botData.getGroup(bm.getGroup());
					g.messageList.add(bm);
					((BaseAdapter) messageFragment.lv.getAdapter()).notifyDataSetChanged();
					ChatFragment cf=chatFragments.get(bm.getGroup());
					if (cf == null) {
						return;
					}
					((BaseAdapter)cf.lv.getAdapter()).notifyDataSetChanged();
				}
			});
	}
	public void addMsg(final long group, final long qq, final String msg) {
		addMsg(new BotMessage(1, group, qq, msg, new Random().nextInt()));
	}

	public void addGroupMember(long group, long qq, Member m) {
		Group g=botData.getGroup(group);
		g.memberSet.put(qq, m);
	}

	public void selectImage(Fragment f) {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("image/*");
        f.startActivityForResult(intent, SELECT_FILE_REQUEST_CODE);
    }

    private void changeTheme() {
        if (MainActivity.sharedPreference.getBoolean("useLightTheme", true)) {
            lvDrawer.setBackgroundColor(getResources().getColor(android.R.color.background_light));
            rt.setBackgroundColor(getResources().getColor(android.R.color.background_light));
        } else {
            lvDrawer.setBackgroundColor(getResources().getColor(android.R.color.background_dark));
            rt.setBackgroundColor(getResources().getColor(android.R.color.background_dark));
        }
        if (getIntent().getBooleanExtra("setTheme", false)) {
            initSettingsFragment(true);
        } else {
            initGroupList(true);
            if (MainActivity.sharedPreference.getBoolean("opendraw", true)) {
                mDrawerLayout.openDrawer(lvDrawer);
            }
        }
    }

    @Override
    public void setTheme(int resid) {
        if (MainActivity.sharedPreference.getBoolean("useLightTheme", true)) {
            super.setTheme(R.style.AppThemeLight);
        } else {
            super.setTheme(R.style.AppThemeDark);
        }
    }

    private void setListener() {
        drawerArrow = new DrawerArrowDrawable(this) {
            @Override
            public boolean isLayoutRtl() {
                return false;
            }
        };
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, drawerArrow, R.string.open, R.string.close) {

            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                invalidateOptionsMenu();
            }

            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                invalidateOptionsMenu();
            }

            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                super.onDrawerSlide(drawerView, slideOffset);
            }
        };
        mDrawerLayout.setDrawerListener(mDrawerToggle);
        mDrawerToggle.syncState();
		lvDrawer.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, android.R.id.text1, menus));

		lvDrawer.setOnItemClickListener(new OnItemClickListener(){

				@Override
				public void onItemClick(AdapterView<?> p1, View p2, int p3, long p4) {
					LogTool.i(MainActivity2.this, menus[p3]);
					switch (menus[p3]) {
						case "群消息":
							initGroupList(true);
							break;
						case "状态":
							initStatusFragment(true);
							break;
						case "设置":
							initSettingsFragment(true);
							break;
						case "退出":
							if (MainActivity.sharedPreference.getBoolean("exitsettings")) {
								System.exit(0);
							} else {
								finish();
							}
							break;
					}
					mDrawerToggle.syncState();
					mDrawerLayout.closeDrawer(lvDrawer);
				}
			});
    }

    private void findViews() {
        rt = (RelativeLayout) findViewById(R.id.right_drawer);
        rightText = (TextView) findViewById(R.id.main_activityTextViewRight);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        lvDrawer = (ListView) findViewById(R.id.navdrawer);
    }

    private void initGroupList(boolean showNow) {
        FragmentTransaction transactionWelcome = fragmentManager.beginTransaction();
        if (messageFragment == null) {
            messageFragment = new GroupListFragment();
            transactionWelcome.add(R.id.main_activityLinearLayout, messageFragment);
        }
        hideFragment(transactionWelcome);
        if (showNow) {
            transactionWelcome.show(messageFragment);
        }
        transactionWelcome.commit();
    }

    private void initStatusFragment(boolean showNow) {
        FragmentTransaction transactionAboutFragment = fragmentManager.beginTransaction();
        if (statusFragment == null) {
            statusFragment = new StatusFragment();
            transactionAboutFragment.add(R.id.main_activityLinearLayout, statusFragment);
        }
        hideFragment(transactionAboutFragment);
        if (showNow) {
            transactionAboutFragment.show(statusFragment);
        }
        transactionAboutFragment.commit();
    }

    private void initSettingsFragment(boolean showNow) {
        FragmentTransaction transactionsettings = fragmentManager.beginTransaction();
        if (settingsFragment == null) {
            settingsFragment = new SettingsFragment();
            transactionsettings.add(R.id.main_activityLinearLayout, settingsFragment);
        }
        hideFragment(transactionsettings);
        if (showNow) {
            transactionsettings.show(settingsFragment);
        }
        transactionsettings.commit();
    }

    public void hideFragment(FragmentTransaction transaction) {
        Fragment fs[] = {
			messageFragment,
			statusFragment,
			settingsFragment
        };
        for (Fragment f : fs) {
            if (f != null) {
                transaction.hide(f);
            }
        }
		for (Fragment f : chatFragments.values()) {
			transaction.hide(f);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            if (mDrawerLayout.isDrawerOpen(lvDrawer)) {
                mDrawerLayout.closeDrawer(lvDrawer);
            } else {
                mDrawerLayout.openDrawer(lvDrawer);
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

	public void saveSanaeConfig() {
        try {
            FileOutputStream fos = new FileOutputStream(botDataFile);
            OutputStreamWriter writer = new OutputStreamWriter(fos, StandardCharsets.UTF_8);
            writer.write(gson.toJson(botData));
            writer.flush();
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // TODO: Implement this method
        if (keyCode == KeyEvent.KEYCODE_BACK || keyCode == KeyEvent.KEYCODE_MENU) {
            if (mDrawerLayout.isDrawerOpen(lvDrawer)) {
                mDrawerLayout.closeDrawer(lvDrawer);
            } else {
                mDrawerLayout.openDrawer(lvDrawer);
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}

