package cn.hi028.android.highcommunity.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.hi028.android.highcommunity.R;
import cn.hi028.android.highcommunity.activity.fragment.AutonomousMainFrag;
import cn.hi028.android.highcommunity.bean.Autonomous.Auto_InitBean;

/**
 * @功能：自治大厅Act<br>
 * @作者： Lee_yting<br>
 * @时间：2016/10/9<br>
 */
public class Service_AutonomousActivity extends BaseFragmentActivity {
    String Tag = "Service_AutonomousActivity";
    public static final String ACTIVITYTAG = "Service_AutonomousActivity";
    public static final String INTENTTAG = "Service_AutonomousActivity";
    @Bind(R.id.autoAct_img_back)
    ImageView img_Back;
    @Bind(R.id.tv_secondtitle_name)
    TextView tv_title;
    @Bind(R.id.auto_identified_tomian)
    LinearLayout identifiedToFrag;
    @Bind(R.id.auto_tv_check)
    TextView tv_Check;
    @Bind(R.id.auto_checking)
    LinearLayout checking_Layout;
    @Bind(R.id.auto_commitData)
    LinearLayout commitData_Layout;
    @Bind(R.id.auto_nodata)
    TextView tv_Nodata;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_autonomous);
        ButterKnife.bind(this);
        initViews();
    }
    public Auto_InitBean.Auto_Init_DataEntity mData;

    FragmentManager fm;
    FragmentTransaction ft;
    private void initViews() {
        fm = getSupportFragmentManager();
        ft = fm.beginTransaction();
        AutonomousMainFrag mAutFrag = new AutonomousMainFrag();
        ft.replace(R.id.auto_identified_tomian, mAutFrag, AutonomousMainFrag.FRAGMENTTAG);
        ft.commit();
        img_Back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        toAutoFrag();
    }

    /**
     * 去自治大厅主界面
     */
    private void toAutoFrag() {
        identifiedToFrag.setVisibility(View.VISIBLE);
        commitData_Layout.setVisibility(View.GONE);
        checking_Layout.setVisibility(View.GONE);
    }
    @Override
    protected void onResume() {
        super.onResume();
    }
}
