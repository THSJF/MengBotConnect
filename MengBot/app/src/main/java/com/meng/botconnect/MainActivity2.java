package com.meng.botconnect;

import android.app.*;
import android.content.*;
import android.content.res.*;
import android.graphics.*;
import android.graphics.drawable.*;
import android.os.*;
import android.support.v4.widget.*;
import android.view.*;
import android.widget.*;
import android.widget.AdapterView.*;
import com.google.gson.*;
import com.meng.botconnect.bean.*;
import com.meng.botconnect.fragment.*;
import com.meng.botconnect.lib.*;
import com.meng.botconnect.network.*;
import com.meng.grzxConfig.MaterialDesign.adapters.*;
import java.util.*;
import java.util.concurrent.*;

public class MainActivity2 extends Activity {
	public static MainActivity2 instance;
	public static BotInfo nowBot;
    private final String logString = "以下为操作记录：\n";
    private DrawerLayout mDrawerLayout;
    private ListView lvDrawer;
	public ArrayAdapter<String> adpter;
	
	public View headView;
    private RelativeLayout rightRelativeLayout;
    private ActionBarDrawerToggle mDrawerToggle;
    private DrawerArrowDrawable drawerArrow;
    public TextView rightText;
	public ConcurrentHashMap<Long,ChatFragment> chatFragments=new ConcurrentHashMap<>();
	public FragmentManager fragmentManager;
	public CoolQ CQ;
	public ExecutorService threadPool = Executors.newFixedThreadPool(5);
	public static String mainFolder;
	public static final int SELECT_FILE_REQUEST_CODE = 822;

	public ArrayList<String> menusList=new ArrayList<String>();
	
	public ActionBar ab;
	public BotQQMsgData botData=new BotQQMsgData();
	public Gson gson;
	public Runnable onBackPressRunable=null;

	private HashMap<String,Fragment> fragments=new HashMap<>();
	public GroupConfigAdapter groupConfigAdapter;
    public QQAccountAdapter qqNotReplyAdapter;
    public ArrayAdapter wordNotReplyAdapter;
    public PersonInfoAdapter personInfoAdapter;
    public QQAccountAdapter masterAdapter;
    public QQAccountAdapter adminAdapter;
    public QQAccountAdapter groupAutoAllowAdapter;

    public QQAccountAdapter blackQQAdapter;
    public QQAccountAdapter blackGroupAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        instance = this;
		menusList.add("群消息");
		menusList.add("设置");
		menusList.add("退出");
		mainFolder = Environment.getExternalStorageDirectory() + "/Pictures/grzx/";
		ab = getActionBar();
        ab.setDisplayHomeAsUpEnabled(true);
        ab.setHomeButtonEnabled(true);
		findViews();
		headView = getLayoutInflater().inflate(R.layout.lv_drawer_head, null);
        nowBot = new BotInfo(headView);
		rightText.setText(logString);
        fragmentManager = getFragmentManager();
        setListener();
        changeTheme();
		GsonBuilder gb = new GsonBuilder();
		gb.setLongSerializationPolicy(LongSerializationPolicy.STRING);
		gson = gb.create();
		try {
			LogTool.t(MainActivity2.instance, String.format("ws://%s:%s", CoolQ.ip, CoolQ.port));
			CQ = new CoolQ();
			CQ.connect();
		} catch (Exception e) {
			LogTool.e(this, e);
		}
		showFragment(GroupConfigFragment.class);
		showFragment(QQNotReplyFragment.class);
		showFragment(WordNotReplyFragment.class);
		showFragment(PersonInfoFragment.class);
		showFragment(MasterFragment.class);
		showFragment(AdminFragment.class);
		showFragment(GroupAutoAllowFragment.class);
		showFragment(BlackQQFragment.class);
		showFragment(BlackGroupFragment.class);
		showFragment(GroupListFragment.class);
    }

	public void addHeaderView() {
		if (lvDrawer.getHeaderViewsCount() < 1) {
			lvDrawer.addHeaderView(headView);
		}
	}
	public void addMsg(final BotMessage bm) {

//		List<Long> ats = new CQCode().getAts(bm.getMsg());
//		for (long l:ats) {
//			LogTool.i(this, "获取到at:" + l);
//		}
//		List<CQImage> images = new CQCode().getCQImages(bm.getMsg());
//        if (images.size() != 0) {
//            File[] imageFiles = new File[images.size()];
//            for (int i = 0, imagesSize = images.size(); i < imagesSize; i++) {
//				CQImage image = images.get(i);
//                try {
//					LogTool.t(this, "开始下载图片");
//					imageFiles[i] = image.download(MainActivity2.mainFolder + "/downloadImages/", image.getMd5());
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//        }

		runOnUiThread(new Runnable(){

				@Override
				public void run() {
					Group g=botData.getGroup(bm.getGroup());
					if (g == null) {
						g = new Group(bm.getGroup(), "群" + bm.getGroup());
						botData.addGroup(g);
					}
					g.addMessage(bm);
					g.removeByUser = false;
					botData.groupList.add(0, botData.groupList.remove(botData.groupList.indexOf(g)));
					if (g.getMenberByQQ(bm.getFromQQ()) == null) {
						CQ.getGroupMemberInfo(g.id, bm.getFromQQ());
					}
					((BaseAdapter) getFragment(GroupListFragment.class).lv.getAdapter()).notifyDataSetChanged();
					ChatFragment cf=chatFragments.get(bm.getGroup());
					if (cf == null) {
						return;
					}
					((BaseAdapter)cf.lvMsg.getAdapter()).notifyDataSetChanged();
				}
			});
	}
	public void addMsg(final long group, final long qq, final String msg) {
		addMsg(new BotMessage(1, group, qq, msg, new Random().nextInt()));
	}

	public void addGroupMember(Member m) {
		Group g=botData.getGroup(m.getGroupId());
		if (g == null) {
			g = new Group(m.getGroupId(), "群" + m.getGroupId());
			botData.addGroup(g);
		}
		g.addMember(m);
		g.onGettingInfo.remove(m.getQqId());
	}

	public <T extends Fragment> T getFragment(Class<T> c) {
		return (T)fragments.get(c.getName());
	}

	public <T extends Fragment> void showFragment(Class<T> c) {
		FragmentTransaction trans = fragmentManager.beginTransaction();
		Fragment frag=fragments.get(c.getName());
		if (frag == null) {
			try {
				Class<?> cls = Class.forName(c.getName());
				frag = (Fragment) cls.newInstance();
				fragments.put(c.getName(), frag);
				trans.add(R.id.main_activityLinearLayout, frag);
			} catch (Exception e) {
				throw new RuntimeException("反射爆炸");
			}
		}
        hideFragment(trans);
		trans.show(frag);
        trans.commit();
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
            rightRelativeLayout.setBackgroundColor(getResources().getColor(android.R.color.background_light));
        } else {
            lvDrawer.setBackgroundColor(getResources().getColor(android.R.color.background_dark));
            rightRelativeLayout.setBackgroundColor(getResources().getColor(android.R.color.background_dark));
        }
        if (getIntent().getBooleanExtra("setTheme", false)) {
            showFragment(SettingsFragment.class);
        } else {
            showFragment(GroupListFragment.class);
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
		adpter=new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, android.R.id.text1, menusList);
		lvDrawer.setAdapter(adpter);

		lvDrawer.setOnItemClickListener(new OnItemClickListener(){

				@Override
				public void onItemClick(AdapterView<?> p1, View p2, int p3, long p4) {
					switch ((String)p1.getAdapter().getItem(p3)) {
						case "群消息":
							showFragment(GroupListFragment.class);
							break;
						case "状态":
							showFragment(StatusFragment.class);
							break;
						case "题库":
							showFragment(QuestionFragment.class);
							break;
						case "群配置":
							showFragment(GroupConfigFragment.class);
							break;
						case "不回复的QQ":
							showFragment(QQNotReplyFragment.class);
							break;
						case "不回复的字":
							showFragment(WordNotReplyFragment.class);
							break;
						case "飞机佬名单":
							showFragment(PersonInfoFragment.class);
							break;
						case "Master":
							showFragment(MasterFragment.class);
							break;
						case "Admin":
							showFragment(AdminFragment.class);
							break;
						case "自动同意进群":
							showFragment(GroupAutoAllowFragment.class);
							break;
						case "黑名单QQ":
							showFragment(BlackQQFragment.class);
							break;
						case "黑名单群":
							showFragment(BlackGroupFragment.class);
							break;
						case "软件新版本上传":
							showFragment(UploadApkFragment.class);
							break;
						case "设置":
							showFragment(SettingsFragment.class);
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
					if (MainActivity.sharedPreference.getBoolean("useLightTheme")) {
						MainActivity2.instance.ab.setBackgroundDrawable(new ColorDrawable(Color.argb(0xff, 0x3f, 0x51, 0xb5)));
					} else {
						MainActivity2.instance.ab.setBackgroundDrawable(new ColorDrawable(Color.GRAY));
					}
				}
			});
    }

    private void findViews() {
        rightRelativeLayout = (RelativeLayout) findViewById(R.id.right_drawer);
        rightText = (TextView) findViewById(R.id.main_activityTextViewRight);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        lvDrawer = (ListView) findViewById(R.id.navdrawer);
    }

	
    public void hideFragment(FragmentTransaction transaction) {
        for (Fragment f : fragments.values()) {
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

	@Override
	protected void onResume() {
		if (CQ.isClosed()) {
			CQ.reconnect();
		}
		super.onResume();
	}


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (onBackPressRunable == null) {
			if (keyCode == KeyEvent.KEYCODE_BACK || keyCode == KeyEvent.KEYCODE_MENU) {
				if (mDrawerLayout.isDrawerOpen(lvDrawer)) {
					mDrawerLayout.closeDrawer(lvDrawer);
				} else {
					mDrawerLayout.openDrawer(lvDrawer);
				}
			}
		} else {
			runOnUiThread(onBackPressRunable);
		}
        return true;
    }
}

