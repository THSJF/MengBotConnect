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
    public GroupListFragment messageFragment;
    private StatusFragment statusFragment;
    private SettingsFragment settingsFragment;
	public QuestionFragment quesFragment;
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


	public GroupConfigAdapter groupConfigAdapter;
    public QQAccountAdapter qqNotReplyAdapter;
    public ArrayAdapter wordNotReplyAdapter;
    public PersonInfoAdapter personInfoAdapter;
    public QQAccountAdapter masterAdapter;
    public QQAccountAdapter adminAdapter;
    public QQAccountAdapter groupAutoAllowAdapter;

    public QQAccountAdapter blackQQAdapter;
    public QQAccountAdapter blackGroupAdapter;

    public GroupConfigFragment groupConfigFragment;
    public QQNotReplyFragment qqNotReplyFragment;
    public WordNotReplyFragment wordNotReplyFragment;
    public PersonInfoFragment personInfoFragment;
    public MasterFragment masterFragment;
    public AdminFragment adminFragment;
    public GroupAutoAllowFragment groupAutoAllowFragment;
    public BlackQQFragment blackQQFragment;
    public BlackGroupFragment blackGroupFragment;

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
		initQQFragment(false);
        initWordFragment(false);
        initPersonFragment(false);
        initMasterFragment(false);
        initAdminFragment(false);
        initAllowFragment(false);
        initBlackQQFragment(false);
        initBlackGroupFragment(false);
        initSettingsFragment(false);
        initGroupConfigFragment(false);
		try {
			LogTool.t(MainActivity2.instance, String.format("ws://%s:%s", CoolQ.ip, CoolQ.port));
			CQ = new CoolQ();
			CQ.connect();
		} catch (Exception e) {
			LogTool.e(this, e);
		}
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
					((BaseAdapter) messageFragment.lv.getAdapter()).notifyDataSetChanged();
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
		adpter=new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, android.R.id.text1, menusList);
		lvDrawer.setAdapter(adpter);

		lvDrawer.setOnItemClickListener(new OnItemClickListener(){

				@Override
				public void onItemClick(AdapterView<?> p1, View p2, int p3, long p4) {
					switch ((String)p1.getAdapter().getItem(p3)) {
						case "群消息":
							initGroupList(true);
							break;
						case "状态":
							initStatusFragment(true);
							break;
						case "题库":
							initQuesFragment(true);
							break;
						case "群配置":
							initGroupConfigFragment(true);
							break;
						case "不回复的QQ":
							initQQFragment(true);
							break;
						case "不回复的字":
							initWordFragment(true);
							break;
						case "飞机佬名单":
							initPersonFragment(true);
							break;
						case "Master":
							initMasterFragment(true);
							break;
						case "Admin":
							initAdminFragment(true);
							break;
						case "自动同意进群":
							initAllowFragment(true);
							break;
						case "黑名单QQ":
							initBlackQQFragment(true);
							break;
						case "黑名单群":
							initBlackGroupFragment(true);
							break;	
						case "群配置":
							initGroupConfigFragment(true);
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

	public void initGroupConfigFragment(boolean showNow) {
        FragmentTransaction transactionWelcome = getFragmentManager().beginTransaction();
        if (groupConfigFragment == null) {
            groupConfigFragment = new GroupConfigFragment();
            transactionWelcome.add(R.id.main_activityLinearLayout, groupConfigFragment);
        }
        hideFragment(transactionWelcome);
        if (showNow) {
            transactionWelcome.show(groupConfigFragment);
        }
        transactionWelcome.commit();
    }

    public void initQQFragment(boolean showNow) {
        FragmentTransaction transactionWelcome = getFragmentManager().beginTransaction();
        if (qqNotReplyFragment == null) {
            qqNotReplyFragment = new QQNotReplyFragment();
            transactionWelcome.add(R.id.main_activityLinearLayout, qqNotReplyFragment);
        }
        hideFragment(transactionWelcome);
        if (showNow) {
            transactionWelcome.show(qqNotReplyFragment);
        }
        transactionWelcome.commit();
    }

    public void initWordFragment(boolean showNow) {
        FragmentTransaction transactionWelcome = getFragmentManager().beginTransaction();
        if (wordNotReplyFragment == null) {
            wordNotReplyFragment = new WordNotReplyFragment();
            transactionWelcome.add(R.id.main_activityLinearLayout, wordNotReplyFragment);
        }
        hideFragment(transactionWelcome);
        if (showNow) {
            transactionWelcome.show(wordNotReplyFragment);
        }
        transactionWelcome.commit();
    }

    public void initPersonFragment(boolean showNow) {
        FragmentTransaction transactionWelcome = getFragmentManager().beginTransaction();
        if (personInfoFragment == null) {
            personInfoFragment = new PersonInfoFragment();
            transactionWelcome.add(R.id.main_activityLinearLayout, personInfoFragment);
        }
        hideFragment(transactionWelcome);
        if (showNow) {
            transactionWelcome.show(personInfoFragment);
        }
        transactionWelcome.commit();
    }

    public void initMasterFragment(boolean showNow) {
        FragmentTransaction transactionWelcome = getFragmentManager().beginTransaction();
        if (masterFragment == null) {
            masterFragment = new MasterFragment();
            transactionWelcome.add(R.id.main_activityLinearLayout, masterFragment);
        }
        hideFragment(transactionWelcome);
        if (showNow) {
            transactionWelcome.show(masterFragment);
        }
        transactionWelcome.commit();
    }

    public void initAdminFragment(boolean showNow) {
        FragmentTransaction transactionWelcome = getFragmentManager().beginTransaction();
        if (adminFragment == null) {
            adminFragment = new AdminFragment();
            transactionWelcome.add(R.id.main_activityLinearLayout, adminFragment);
        }
        hideFragment(transactionWelcome);
        if (showNow) {
            transactionWelcome.show(adminFragment);
        }
        transactionWelcome.commit();
    }

    public void initAllowFragment(boolean showNow) {
        FragmentTransaction transactionWelcome = getFragmentManager().beginTransaction();
        if (groupAutoAllowFragment == null) {
            groupAutoAllowFragment = new GroupAutoAllowFragment();
            transactionWelcome.add(R.id.main_activityLinearLayout, groupAutoAllowFragment);
        }
        hideFragment(transactionWelcome);
        if (showNow) {
            transactionWelcome.show(groupAutoAllowFragment);
        }
        transactionWelcome.commit();
    }

    public void initBlackQQFragment(boolean showNow) {
        FragmentTransaction transactionWelcome = getFragmentManager().beginTransaction();
        if (blackQQFragment == null) {
            blackQQFragment = new BlackQQFragment();
            transactionWelcome.add(R.id.main_activityLinearLayout, blackQQFragment);
        }
        hideFragment(transactionWelcome);
        if (showNow) {
            transactionWelcome.show(blackQQFragment);
        }
        transactionWelcome.commit();
    }

    public void initBlackGroupFragment(boolean showNow) {
        FragmentTransaction transactionWelcome = getFragmentManager().beginTransaction();
        if (blackGroupFragment == null) {
            blackGroupFragment = new BlackGroupFragment();
            transactionWelcome.add(R.id.main_activityLinearLayout, blackGroupFragment);
        }
        hideFragment(transactionWelcome);
        if (showNow) {
            transactionWelcome.show(blackGroupFragment);
        }
        transactionWelcome.commit();
    }


    public void initGroupList(boolean showNow) {
		onBackPressRunable = null;
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

	private void initQuesFragment(boolean showNow) {
        FragmentTransaction transactionsettings = fragmentManager.beginTransaction();
        if (quesFragment == null) {
            quesFragment = new QuestionFragment();
            transactionsettings.add(R.id.main_activityLinearLayout, quesFragment);
        }
        hideFragment(transactionsettings);
        if (showNow) {
            transactionsettings.show(quesFragment);
        }
        transactionsettings.commit();
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
			quesFragment,
			groupConfigFragment,
			qqNotReplyFragment,
			wordNotReplyFragment,
			personInfoFragment,
			masterFragment,
			adminFragment,
			groupAutoAllowFragment,
			blackQQFragment,
			blackGroupFragment,
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

