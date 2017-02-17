package cn.hi028.android.highcommunity.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.LocationClient;
import com.don.tools.BpiHttpHandler;
import com.google.gson.Gson;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;

import net.duohuo.dhroid.activity.ActivityTack;
import net.duohuo.dhroid.activity.BaseFragment;

import java.util.ArrayList;
import java.util.Set;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.hi028.android.highcommunity.HighCommunityApplication;
import cn.hi028.android.highcommunity.R;
import cn.hi028.android.highcommunity.activity.fragment.ActFrag;
import cn.hi028.android.highcommunity.activity.fragment.HuiLifeFrag;
import cn.hi028.android.highcommunity.activity.fragment.NeighborFrag;
import cn.hi028.android.highcommunity.activity.fragment.ServiceFrag;
import cn.hi028.android.highcommunity.bean.RongYunBean;
import cn.hi028.android.highcommunity.bean.UserCenterBean;
import cn.hi028.android.highcommunity.utils.Constacts;
import cn.hi028.android.highcommunity.utils.HTTPHelper;
import cn.hi028.android.highcommunity.utils.HighCommunityUtils;
import cn.hi028.android.highcommunity.utils.NToast;
import cn.hi028.android.highcommunity.utils.updateutil.UpdateUtil;
import cn.jpush.android.api.CustomPushNotificationBuilder;
import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.TagAliasCallback;
import io.rong.imkit.RongIM;
import io.rong.imlib.RongIMClient;
import photo.util.Res;

/**
 * 主界面
 * 备注： v1.0版本有侧滑菜单  2.0取消
 */
public class MainActivity extends BaseFragmentActivity implements View.OnClickListener, NeighborFrag.MyChangeListener, HuiLifeFrag.MyChangeListener4HuiLife {
    static final String Tag = "MainActivity->";
    public static String TAG = "MainActivity->";
    /**
     * 底部tabs
     **/
    private final int[] tab_menu_text_ID = new int[]{R.id.main_tab_firth,
            R.id.main_tab_second, R.id.main_tab_fourth, R.id.main_tab_five};
    private View[] menu_tvs = null;
    private long exitTime = 0;
    /**
     * 底部tabs 名称
     **/
    private final int[] tab_menu_title = new int[]{R.string.tb_first, R.string.tb_second, R.string.tb_fourth, R.id.main_tab_five};
    //    ArrayList<BaseFragment> fragments = new ArrayList<BaseFragment>();// frags集合
    ArrayList<Fragment> fragments = new ArrayList<Fragment>();// frags集合
    BaseFragment serviceFrag, huiLifeFrag, neighborFrag;
    Fragment actFrag;
    private ImageView MiddleButton;
    private TextView mTitle;
    /**
     * 社区或是群组  惠生活：0直供 1众筹
     **/
    private RadioGroup mGroup, mNewHuiLifeGroup;
    /**
     * 社区  群组 直供  众筹
     **/
    private RadioButton mLeftButton, mRightButton, mRightButton_right, mSupplyBut, mChipsBut;
    @Bind(R.id.status_height)
    View status_height;
    @Bind(R.id.main_title_layout_tvandgroup)
    View title_layout_tvandgroup;

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
//		来自labelact
        if (intent.getIntExtra("communityFlag", 0) == 0x22) {
//            tabSelector(1);
//			NeighborFrag mFrag = (NeighborFrag) getSupportFragmentManager()
//					.findFragmentByTag(NeighborFrag.FRAGMENTTAG);
            ServiceFrag mFrag = (ServiceFrag)
                    getSupportFragmentManager().findFragmentByTag(ServiceFrag.FRAGMENTTAG);
            if (null == mFrag)
                return;
            //mFrag.setCurrentPage(0);
        } else if (intent.getIntExtra("communityFlag", 0) == 0x222) {
            tabSelector(0);
            NeighborFrag mFrag = (NeighborFrag) getSupportFragmentManager()
                    .findFragmentByTag(NeighborFrag.FRAGMENTTAG);
            if (null == mFrag)
                return;
            mFrag.setCurrentPage(0);
        } else if (intent.getIntExtra("actFlag", 0) == 0x66) {
            tabSelector(3);
            ActFrag mFrag = (ActFrag) getSupportFragmentManager()
                    .findFragmentByTag(ActFrag.FRAGMENTTAG);
            if (null == mFrag)
                return;
            // mFrag.setCurrentPage(0);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ActivityTack.getInstanse().clear();
        //requestWindowFeature(Window.FEATURE_NO_TITLE); // 隐藏标题栏
        // getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);// 隐藏状态栏
        //getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);// 透明状态栏
        // getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);//透明导航栏
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        //检查更新
        new UpdateUtil(MainActivity.this, getApplicationContext()).initUpdate();
        Res.init(this);
        mHttpUtils = new HttpUtils();
        mHttpUtils.configCurrentHttpCacheExpiry(0);
        mHttpUtils.configSoTimeout(4000);
        mHttpUtils.configTimeout(4000);
        useHttp();
        //TODO 还没调试完
//        connect("F2sMSChp+KkI6SSaEWUnITkc36aZPpJZqj81iEXN8zId1Zx5/yGTsbphE0dY6jOh96NnNqVdWJIYgvzyeiwnVg==");
        initView();
        initMenu(savedInstanceState);
        if (HighCommunityUtils.isLogin()) {
            JPushInterface.init(getApplicationContext());
            setTag();
            setStyleCustom();
            registerMessageReceiver();
            Log.d("userinfor", "用户信息：" + HighCommunityApplication.mUserInfo.toString());
        }
    }

    public void setTag() {
        Log.d(Tag, "唯一标识：" + HighCommunityApplication.mUserInfo.getId());
        JPushInterface.setAliasAndTags(getApplicationContext(),
                HighCommunityApplication.mUserInfo.getId() + "", null,
                new TagAliasCallback() {

                    @Override
                    public void gotResult(int code, String alias,
                                          Set<String> tags) {
                        String logs;
                        switch (code) {
                            case 0:
                                logs = "Set tag and alias success";
                                Log.i(TAG, logs);
                                break;
                            case 6002:
                                logs = "Failed to set alias and tags due to timeout. Try again after 60s.";
                                Log.i(TAG, logs);
                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        setTag();
                                    }
                                }, 60 * 6000);
                                break;
                            default:
                                logs = "Failed with errorCode = " + code;
                                Log.e(TAG, logs);
                        }
                    }

                });
    }

    @Override
    protected void onResume() {
        super.onResume();
        int checkNum = getIntent().getIntExtra("checkupdata", -1);
        if (checkNum == 6) {
            boolean isUpdate = new UpdateUtil(MainActivity.this, getApplicationContext()).checkIsToUpdate();
            if (isUpdate) {
                new UpdateUtil(MainActivity.this, getApplicationContext()).initUpdate();
            } else {
                Toast.makeText(MainActivity.this, "已经是最新版本了", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onDestroy() {
        if (HighCommunityUtils.isLogin()) {
            unregisterReceiver(mMessageReceiver);
        }
        super.onDestroy();
    }

    private MessageReceiver mMessageReceiver;
    public static final String MESSAGE_RECEIVED_ACTION = "cn.hi028.android.highcommunity.related";
    public static final String KEY_TITLE = "title";
    public static final String KEY_MESSAGE = "message";
    public static final String KEY_EXTRAS = "extras";

    public void registerMessageReceiver() {
        mMessageReceiver = new MessageReceiver();
        IntentFilter filter = new IntentFilter();
        filter.setPriority(IntentFilter.SYSTEM_HIGH_PRIORITY);
        filter.addAction(MESSAGE_RECEIVED_ACTION);
        registerReceiver(mMessageReceiver, filter);
    }

    @Override
    public void onHuiLifeChange(int i) {

        if (i == 0) {//直供选中
            mSupplyBut.setChecked(true);
            mChipsBut.setChecked(false);
        } else if (i == 1) {//众筹选中
            mSupplyBut.setChecked(false);
            mChipsBut.setChecked(true);
        }

    }

    /**
     * 消息接收器
     */
    public class MessageReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (MESSAGE_RECEIVED_ACTION.equals(intent.getAction())) {
                String messge = intent.getStringExtra(KEY_MESSAGE);
                int extras = intent.getIntExtra(KEY_EXTRAS, 0);
                if (extras == 1) {
//                    mRightTop.setVisibility(View.GONE);
                } else {
//                    mRightTop.setVisibility(View.GONE);
                }
            }
        }
    }

    /**
     * 设置通知栏样式 - 定义通知栏Layout
     */
    private void setStyleCustom() {
        CustomPushNotificationBuilder builder = new CustomPushNotificationBuilder(
                MainActivity.this, R.layout.customer_notitfication_layout,
                R.id.icon, R.id.title, R.id.text);
        builder.layoutIconDrawable = R.drawable.ic_launcher;
        builder.developerArg0 = "developerArg2";
        JPushInterface.setPushNotificationBuilder(2, builder);
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        if (HighCommunityApplication.mUserInfo.getId() != 0) {
            HTTPHelper.getUserCenter(mIbpi,
                    HighCommunityApplication.mUserInfo.getId() + "");
        }
    }

    void initView() {
        mTitle = (TextView) this.findViewById(R.id.tv_mainlevel_title);
        mGroup = (RadioGroup) this.findViewById(R.id.rg_maintitle_group);
        mNewHuiLifeGroup = (RadioGroup) this.findViewById(R.id.rg_title_newHuilifegroup);
        mLeftButton = (RadioButton) this.findViewById(R.id.rb_maintitle_leftbutton);
        mRightButton = (RadioButton) this.findViewById(R.id.rb_maintitle_rightbutton);
        mRightButton_right = (RadioButton) this.findViewById(R.id.rb_maintitle_rightbutton_right);
        mSupplyBut = (RadioButton) this.findViewById(R.id.rb_title_supply);
        mChipsBut = (RadioButton) this.findViewById(R.id.rb_title_chips);
        MiddleButton = (ImageView) this.findViewById(R.id.main_tab_third);
        mGroup.setVisibility(View.VISIBLE);
        mNewHuiLifeGroup.setVisibility(View.VISIBLE);
        mLeftButton.setOnClickListener(mTitleListener);
        mRightButton.setOnClickListener(mTitleListener);
        mRightButton_right.setOnClickListener(mTitleListener);
        mSupplyBut.setOnClickListener(mTitleListener);
        mChipsBut.setOnClickListener(mTitleListener);

    }

    BpiHttpHandler.IBpiHttpHandler mIbpi = new BpiHttpHandler.IBpiHttpHandler() {
        @Override
        public void onError(int id, String message) {
            HighCommunityUtils.GetInstantiation().ShowToast(message, 0);
        }

        @Override
        public void onSuccess(Object message) {
            if (null == message)
                return;
            Constacts.mUserCenter = (UserCenterBean) message;

        }

        @Override
        public Object onResolve(String result) {
            Log.e("renk", result);
            return HTTPHelper.ResolveUserCenter(result);
        }

        @Override
        public void setAsyncTask(AsyncTask asyncTask) {

        }

        @Override
        public void cancleAsyncTask() {

        }

        @Override
        public void shouldLogin(boolean isShouldLogin) {

        }

        @Override
        public void shouldLoginAgain(boolean isShouldLogin, String msg) {
            if (isShouldLogin) {
                HighCommunityUtils.GetInstantiation().ShowToast(msg, 0);
                HighCommunityApplication.toLoginAgain(MainActivity.this);
            }
        }
    };


    /**
     * 初始化底部tab
     */
    private void initMenu(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            FragmentManager manager = getSupportFragmentManager();
            manager.popBackStackImmediate(null, 1);
        }
        neighborFrag = new NeighborFrag();
        serviceFrag = new ServiceFrag();
        huiLifeFrag = new HuiLifeFrag();
        actFrag = new ActFrag();
        fragments.add(neighborFrag);
        fragments.add(serviceFrag);
        fragments.add(huiLifeFrag);
        fragments.add(actFrag);
        getSupportFragmentManager().beginTransaction()
                .add(R.id.main_Content, neighborFrag, NeighborFrag.FRAGMENTTAG)
                .show(neighborFrag)
                .add(R.id.main_Content, huiLifeFrag, HuiLifeFrag.FRAGMENTTAG)
                .hide(huiLifeFrag)
                .add(R.id.main_Content, actFrag, ActFrag.FRAGMENTTAG)
                .hide(actFrag)
                .add(R.id.main_Content, serviceFrag, ServiceFrag.FRAGMENTTAG)
                .hide(serviceFrag).commit();
        menu_tvs = new TextView[tab_menu_text_ID.length];
        for (int i = 0; i < tab_menu_text_ID.length; i++) {
            menu_tvs[i] = findViewById(tab_menu_text_ID[i]);
            menu_tvs[i].setOnClickListener(this);
        }
        MiddleButton.setOnClickListener(this);
        tabSelector(1);
        mTitle.setText("服务");
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    /**
     * 菜单模块选择
     *
     * @param position 0-邻里 1-服务 2-惠生活 3-活动
     */
    private void tabSelector(int position) {
        Log.d(Tag, "tabSelector:" + position);
        if (0 == position) {
            status_height.setVisibility(View.VISIBLE);
            title_layout_tvandgroup.setVisibility(View.VISIBLE);
            mTitle.setVisibility(View.GONE);
            mGroup.setVisibility(View.VISIBLE);
            mNewHuiLifeGroup.setVisibility(View.GONE);

        } else if (position == 2) {
            status_height.setVisibility(View.VISIBLE);
            title_layout_tvandgroup.setVisibility(View.VISIBLE);
            mTitle.setVisibility(View.GONE);
            mGroup.setVisibility(View.GONE);
            mNewHuiLifeGroup.setVisibility(View.VISIBLE);

        } else if (position == 3) {
            status_height.setVisibility(View.GONE);
            title_layout_tvandgroup.setVisibility(View.GONE);
        } else {
            status_height.setVisibility(View.VISIBLE);
            title_layout_tvandgroup.setVisibility(View.VISIBLE);

            mGroup.setVisibility(View.GONE);
            mTitle.setVisibility(View.VISIBLE);
            mNewHuiLifeGroup.setVisibility(View.GONE);

        }
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        for (int i = 0; i < tab_menu_text_ID.length; i++) {//
            if (position == i) {
                menu_tvs[i].setSelected(true);
            } else {
                menu_tvs[i].setSelected(false);
            }
            if (i == position) {
                Log.e(Tag, "选中的position：" + i + ",fragment" + fragments.get(position).getTag());
                ft.show(fragments.get(position));

            } else {
                ft.hide(fragments.get(i));
            }
        }
        ft.commit();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.main_tab_firth:
                tabSelector(0);
                break;
            case R.id.main_tab_second:
                tabSelector(1);
                mTitle.setText("服务");
                break;
            case R.id.main_tab_third:
                if (HighCommunityUtils.GetInstantiation().isLogin(MainActivity.this)) {
                    Intent mLabel = new Intent(this, LabelAct.class);
                    startActivity(mLabel);
                }
                break;
            case R.id.main_tab_fourth:
                mTitle.setText("惠生活");
                tabSelector(2);
                break;
            case R.id.main_tab_five:
//                mTitle.setText(" ");
                tabSelector(3);
                break;
            case R.id.tx_LeftFrag_userlocation_layout:
            case R.id.tx_LeftFrag_userlocation:
                VallageAct.toStartAct(MainActivity.this, 1, false);
                break;
            default:
                break;
        }
    }

    View.OnClickListener mTitleListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            NeighborFrag mFrag = (NeighborFrag) getSupportFragmentManager().findFragmentByTag(NeighborFrag.FRAGMENTTAG);
            HuiLifeFrag mHuiLifeFrag = (HuiLifeFrag) getSupportFragmentManager().findFragmentByTag(HuiLifeFrag.FRAGMENTTAG);
            if (null == mFrag)
                return;
            switch (view.getId()) {
                case R.id.rb_maintitle_leftbutton:
                    mFrag.setCurrentPage(0);
                    break;
                case R.id.rb_maintitle_rightbutton:
                    mFrag.setCurrentPage(1);
                    break;
                case R.id.rb_maintitle_rightbutton_right:
                    mFrag.setCurrentPage(2);
                    break;
                case R.id.rb_title_supply://直供
                    mHuiLifeFrag.setCurrentPage(0);
                    break;
                case R.id.rb_title_chips://众筹
                    mHuiLifeFrag.setCurrentPage(1);
                    break;
                case R.id.tv_mainlevel_LeftButton:
                    if (mLocationClient.isStarted()) {
                        mLocationClient.stop();
                    } else {
                        mLocationClient.start();
                        mLocationClient.requestLocation();
                    }
                    break;
                case R.id.iv_mainlevel_RightButton:
                    if (HighCommunityUtils.isLogin(MainActivity.this)) {
                        Intent intent = new Intent();
                        intent.setClass(MainActivity.this, MipcaActivityCapture.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                    }
                    break;
            }
        }
    };
    private final static int SCANNIN_GREQUEST_CODE = 1;
    public static LocationClient mLocationClient = null;

    @Override
    public void onChange(int flag) {
        if (flag == 0) {
            mLeftButton.setChecked(true);
            mRightButton.setChecked(false);
            mRightButton_right.setChecked(false);
        } else if (flag == 1) {
            mLeftButton.setChecked(false);
            mRightButton.setChecked(true);
            mRightButton_right.setChecked(false);
        } else if (flag == 2) {
            mRightButton_right.setChecked(true);
            mLeftButton.setChecked(false);
            mRightButton.setChecked(false);
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (((NeighborFrag) fragments.get(0)).onKeyDown(keyCode, event)) {
                return true;
            } else {
                exit();
            }
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }

    public void exit() {
        if ((System.currentTimeMillis() - exitTime) > 2000) {
            HighCommunityUtils.GetInstantiation().ShowToast(
                    getResources().getString(R.string.toast_press_to_exit), 0);
            exitTime = System.currentTimeMillis();
        } else {
            finish();
            System.exit(0);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case SCANNIN_GREQUEST_CODE:
                if (resultCode == RESULT_OK) {
                    Bundle bundle = data.getExtras();
                    Log.e(TAG, "扫描到的内容:" + bundle.getString("result"));
//                    mTextView.setText(bundle.getString("result"));
//                    //显示
//                    mImageView.setImageBitmap((Bitmap) data.getParcelableExtra("bitmap"));
                }
                break;
        }

    }

    /**
     * 融云
     *
     * @param token 从服务端获取的用户身份令牌（Token）。
     * @return RongIM  客户端核心类的实例。
     * @param——callback 连接回调。
     */
    private void connect(String token) {
        if (getApplicationInfo().packageName.equals(HighCommunityApplication.getCurProcessName(getApplicationContext()))) {
            RongIM.connect(token, new RongIMClient.ConnectCallback() {
                /**
                 * Token 错误。可以从下面两点检查 1.  Token 是否过期，如果过期您需要向 App Server 重新请求一个新的 Token
                 *                  2.  token 对应的 appKey 和工程里设置的 appKey 是否一致
                 */
                @Override
                public void onTokenIncorrect() {
                    Log.d(Tag, "--onTokenIncorrect");
                }

                /**
                 * 连接融云成功
                 * @param userid 当前 token 对应的用户 id
                 */
                @Override
                public void onSuccess(String userid) {
                    Log.d(Tag, "--onSuccess" + userid);
//                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
//                    finish();
                    // TODO: 调试完成关闭Toast
                    NToast.shortToast(MainActivity.this, "连接融云成功: userid" + userid);
                }

                /**
                 * 连接融云失败
                 * @param errorCode 错误码，可到官网 查看错误码对应的注释
                 */
                @Override
                public void onError(RongIMClient.ErrorCode errorCode) {
                    Log.d(Tag, "--onError" + errorCode);
                    // TODO: 调试完成关闭Toast
                    NToast.shortToast(MainActivity.this, "连接融云fail: errorCode" + errorCode);

                }
            });
        }
    }

    private HttpUtils mHttpUtils;

    private void useHttp() {
        String url = "http://028hi.cn/rc/chat/get-token.html";
        RequestParams params = new RequestParams();
        params.addBodyParameter("token", HighCommunityApplication.mUserInfo.getToken());
        mHttpUtils.send(HttpRequest.HttpMethod.POST, url, params, new RequestCallBack<String>() {
            @Override
            public void onFailure(HttpException arg0, String arg1) {
                Log.e(Tag, "onFailure->" + arg1.toString());
                HighCommunityUtils.GetInstantiation().ShowToast(arg1.toString(), 0);
            }

            @Override
            public void onSuccess(ResponseInfo<String> arg0) {
                String content = arg0.result;
                Log.e(Tag, "onSuccess->" + content);
                if (null == arg0) {
                    Log.d(TAG + "rong", "message  return :");
                    return;
                }
                RongYunBean data = new Gson().fromJson(content, RongYunBean.class);
                Log.d(TAG + "rong", "message  data :" + data.toString());

                HighCommunityUtils.GetInstantiation().ShowToast(data.toString(), 0);
                connect(data.getToken());
            }
        });
    }
}
