package cn.hi028.android.highcommunity.activity.fragment.alliance;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;

import com.don.tools.BpiHttpHandler;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import net.duohuo.dhroid.activity.BaseFragment;

import java.util.List;

import cn.hi028.android.highcommunity.HighCommunityApplication;
import cn.hi028.android.highcommunity.R;
import cn.hi028.android.highcommunity.activity.MyGoodsEvluateActivity;
import cn.hi028.android.highcommunity.activity.alliance.AllianceOderDetailActivity;
import cn.hi028.android.highcommunity.activity.alliance.AllianceOrder;
import cn.hi028.android.highcommunity.adapter.AllianceOrderAdapter;
import cn.hi028.android.highcommunity.bean.AllianceOrderBean;
import cn.hi028.android.highcommunity.lisenter.EvaluateInterface;
import cn.hi028.android.highcommunity.lisenter.OnRefreshListener;
import cn.hi028.android.highcommunity.utils.HTTPHelper;
import cn.hi028.android.highcommunity.utils.HighCommunityUtils;

/**
 * 联盟订单  未评价订单
 * Created by Administrator on 2016/8/11.
 */
public class AllianceOrderUnCommFrag extends BaseFragment implements OnRefreshListener, EvaluateInterface {
    View mProgress;
    TextView mNodata;
    PullToRefreshListView mUnCommListView;

    private AllianceOrderAdapter mUnCommAdapter = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_alliance_uncomm_order,
                container, false);
        mProgress = view.findViewById(R.id.progress_ticket_notice);
        mNodata = (TextView) view.findViewById(R.id.tv_ticket_Nodata);
        mUnCommListView = (PullToRefreshListView) view
                .findViewById(R.id.uncomm_listView);
        mUnCommListView.setVisibility(View.VISIBLE);
        mUnCommAdapter = new AllianceOrderAdapter(getActivity(),
                AllianceOrder.TAB_UNCOMM, this, this);
        mUnCommListView.setAdapter(mUnCommAdapter);
        mUnCommListView.setEmptyView(mNodata);
        mUnCommListView
                .setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ListView>() {
                    @Override
                    public void onRefresh(
                            PullToRefreshBase<ListView> refreshView) {
                        HTTPHelper.GetAllianceList(mIbpi,
                                AllianceOrder.TAB_UNCOMM);
                    }
                });
        mUnCommListView.setOnItemClickListener(listener);
        return view;
    }

    OnItemClickListener listener = new OnItemClickListener() {

        @Override
        public void onItemClick(AdapterView<?> view, View arg1, int arg2, long arg3) {
            AllianceOrderBean info = (AllianceOrderBean) view
                    .getItemAtPosition(arg2);
            String num = info.getOrder_num();
            Intent intent = new Intent(getActivity(), AllianceOderDetailActivity.class);
            intent.putExtra("order_num", num);
            getActivity().startActivity(intent);
        }
    };

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        mProgress.setVisibility(View.VISIBLE);
        HTTPHelper.GetAllianceList(mIbpi, AllianceOrder.TAB_UNCOMM);
    }

    private BpiHttpHandler.IBpiHttpHandler mIbpi = new BpiHttpHandler.IBpiHttpHandler() {

        @Override
        public void setAsyncTask(AsyncTask asyncTask) {

        }

        @Override
        public void onSuccess(Object message) {
            mProgress.setVisibility(View.GONE);
            resetUi((List<AllianceOrderBean>) message);
        }

        @Override
        public Object onResolve(String result) {
            return HTTPHelper.ResolveAllianceOrderBean(result);
        }

        @Override
        public void onError(int id, String message) {
            mProgress.setVisibility(View.GONE);
        }

        @Override
        public void cancleAsyncTask() {
            mProgress.setVisibility(View.GONE);
        }

        @Override
        public void shouldLogin(boolean isShouldLogin) {

        }

        @Override
        public void shouldLoginAgain(boolean isShouldLogin, String msg) {
            if (isShouldLogin) {
                HighCommunityUtils.GetInstantiation().ShowToast(msg, 0);
                HighCommunityApplication.toLoginAgain(getActivity());
            }
        }
    };

    private void resetUi(List<AllianceOrderBean> allianceOrderBeans) {
        if (allianceOrderBeans.size() < 1) {
            mNodata.setVisibility(View.VISIBLE);
        } else {
            mNodata.setVisibility(View.GONE);
        }
        mUnCommAdapter.AddNewData(allianceOrderBeans);
        mUnCommListView.onRefreshComplete();
    }

    @Override
    public void onRefresh() {
        HTTPHelper.GetAllianceList(mIbpi, AllianceOrder.TAB_UNCOMM);
    }

    int evaluatePosition;

    @Override
    public void goEvaluate(String id, int position) {
        evaluatePosition = position;
        Intent intent = new Intent(getActivity(), MyGoodsEvluateActivity.class);
        intent.putExtra("order_num", id);
        getActivity().startActivityForResult(intent, 900);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 900 && resultCode == Activity.RESULT_OK) {
            HTTPHelper.GetAllianceList(mIbpi, AllianceOrder.TAB_UNCOMM);
        }
    }

    public void onResume() {
        super.onResume();
        onRefresh();

    }

    ;

}
