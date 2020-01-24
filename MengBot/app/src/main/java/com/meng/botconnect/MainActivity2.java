package com.meng.botconnect;

import android.app.*;
import android.content.*;
import android.content.res.*;
import android.os.*;
import android.support.v4.widget.*;
import android.view.*;
import android.widget.*;
import android.widget.AdapterView.*;
import com.meng.botconnect.bean.*;
import com.meng.botconnect.fragment.*;
import com.meng.botconnect.lib.*;
import java.util.*;
import java.util.concurrent.*;

public class MainActivity2 extends Activity {
    public static MainActivity2 instence;
    private final String logString = "以下为操作记录：\n";
    private DrawerLayout mDrawerLayout;
    private ListView lvDrawer;
    private RelativeLayout rt;
    private ActionBarDrawerToggle mDrawerToggle;
    private DrawerArrowDrawable drawerArrow;
    private MessageFragment messageFragment;
    private TextFragment aboutFragment;
    private SettingsFragment settingsFragment;
    public TextView rightText;

	public FragmentManager fragmentManager;

	public ExecutorService threadPool = Executors.newFixedThreadPool(5);
	public static String mainFolder;
	public static final int SELECT_FILE_REQUEST_CODE = 822;
	private final String[] menus = new String[]{"群消息", "私聊消息", "讨论组消息", "状态","设置","退出"};

    public ArrayList<Group> groupList=new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        instence = this;
		mainFolder = Environment.getExternalStorageDirectory() + "/Pictures/grzx/";
        setActionBar();
        findViews();
        rightText.setText(logString);
        initFragment();
        setListener();
        changeTheme();
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
            initWelcome(true);
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

    private void setActionBar() {
        ActionBar ab = getActionBar();
        ab.setDisplayHomeAsUpEnabled(true);
        ab.setHomeButtonEnabled(true);
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
							initWelcome(true);
							break;
						case "私聊消息":
							break;
						case "讨论组消息":
							break;
						case "状态":
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
        lvDrawer = (ExpandableListView) findViewById(R.id.navdrawer);
    }

    private void initFragment() {
        fragmentManager = getFragmentManager();
        if (MainActivity.sharedPreference.getBoolean("textFragment")) {
            initAboutFragment(false);
        }
        if (MainActivity.sharedPreference.getBoolean("settings")) {
            initSettingsFragment(false);
        }
    }

    private void initWelcome(boolean showNow) {
        FragmentTransaction transactionWelcome = fragmentManager.beginTransaction();
        if (messageFragment == null) {
            messageFragment = new MessageFragment();
            transactionWelcome.add(R.id.main_activityLinearLayout, messageFragment);
        }
        hideFragment(transactionWelcome);
        if (showNow) {
            transactionWelcome.show(messageFragment);
        }
        transactionWelcome.commit();
    }

    private void initAboutFragment(boolean showNow) {
        FragmentTransaction transactionAboutFragment = fragmentManager.beginTransaction();
        if (aboutFragment == null) {
            aboutFragment = new TextFragment(1);
            transactionAboutFragment.add(R.id.main_activityLinearLayout, aboutFragment);
        }
        hideFragment(transactionAboutFragment);
        if (showNow) {
            transactionAboutFragment.show(aboutFragment);
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
			aboutFragment,
			settingsFragment
        };
        for (Fragment f : fs) {
            if (f != null) {
                transaction.hide(f);
            }
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

