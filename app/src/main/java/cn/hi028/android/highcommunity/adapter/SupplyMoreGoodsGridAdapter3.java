package cn.hi028.android.highcommunity.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.StrikethroughSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.don.tools.BpiHttpHandler;
import com.don.tools.BpiUniveralImage;

import java.util.ArrayList;
import java.util.List;

import cn.hi028.android.highcommunity.HighCommunityApplication;
import cn.hi028.android.highcommunity.R;
import cn.hi028.android.highcommunity.activity.alliance.SupplyGoodsDetailActivity2;
import cn.hi028.android.highcommunity.bean.SupplyGoodsMoreBean;
import cn.hi028.android.highcommunity.utils.Constacts;
import cn.hi028.android.highcommunity.utils.HighCommunityUtils;

/**
 * @功能：直供商品分类adapter<br>
 * @作者： Lee_yting<br>
 * @时间：2016/11/28<br>
 */
public class SupplyMoreGoodsGridAdapter3 extends RecyclerView.Adapter<SupplyMoreGoodsGridAdapter3.ViewHolder> implements View.OnClickListener {
    public static final String Tag = "直供商品更多Adapter：";
    List<SupplyGoodsMoreBean.SupplyGoodsMoreDataEntity.SupplyMoreGoodsEntity> mList
            = new ArrayList<SupplyGoodsMoreBean.SupplyGoodsMoreDataEntity.SupplyMoreGoodsEntity>();
    private Context context;
    private LayoutInflater layoutInflater;

    public SupplyMoreGoodsGridAdapter3(List<SupplyGoodsMoreBean.SupplyGoodsMoreDataEntity.SupplyMoreGoodsEntity> list, Context context) {
        super();
        this.mList = list;
        if (this.mList == null) {
            this.mList = new ArrayList<SupplyGoodsMoreBean.SupplyGoodsMoreDataEntity.SupplyMoreGoodsEntity>();
        }
        this.layoutInflater = LayoutInflater.from(context);
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.item_supplygoodsmore, null);
        ViewHolder vh = new ViewHolder(view);
        view.setOnClickListener(this);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final SupplyGoodsMoreBean.SupplyGoodsMoreDataEntity.SupplyMoreGoodsEntity mBean = mList.get(position);
        holder.itemView.setTag(mBean);
        if (mBean.getCover_pic() == null || mBean.getCover_pic().equals("")) {
            BpiUniveralImage.displayImage("drawable://" + R.mipmap.default_no_pic, holder.mGoodsimg);
        } else {

            BpiUniveralImage.displayImage(Constacts.IMAGEHTTP + mBean.getCover_pic(), holder.mGoodsimg);
        }
        holder.mTitle.setText(mBean.getName());
        holder.mNowPrice.setText("￥:" + mBean.getPrice());
        if (mBean.getLabel() != null && !mBean.getLabel().equals("")) {
            holder.mTvTag.setVisibility(View.VISIBLE);

            holder.mTvTag.setText(mBean.getLabel());

        } else {
            holder.mTvTag.setVisibility(View.GONE);
        }
        if (mBean.getOld_price() != null && !mBean.getOld_price().equals("") && !mBean.getOld_price().equals("0")) {
            holder.mOldPrice.setVisibility(View.VISIBLE);
            Spannable spanStrikethrough = new SpannableString("￥：" + mBean.getOld_price());
            StrikethroughSpan stSpan = new StrikethroughSpan();  //设置删除线样式
            try {
                spanStrikethrough.setSpan(stSpan, 0, spanStrikethrough.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);

            } catch (Exception ex) {

            }
            holder.mOldPrice.setText(spanStrikethrough);
        } else {
            holder.mOldPrice.setVisibility(View.GONE);
        }
        holder.mSaledNum.setText("已售" + mBean.getSale());

        holder.mShopcart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mIntent = new Intent(context, SupplyGoodsDetailActivity2.class);
                mIntent.putExtra("id", mBean.getId());
                context.startActivity(mIntent);
            }
        });
    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public void AddNewData(Object mObject) {
        if (mObject instanceof List<?>) {
            mList = (List<SupplyGoodsMoreBean.SupplyGoodsMoreDataEntity.SupplyMoreGoodsEntity>) mObject;
        }
        notifyDataSetChanged();
    }
    public void ClearData() {
        mList.clear();
        notifyDataSetChanged();
    }
    /**
     * 加入购物车弹窗
     */
    PopupWindow waitPop;
    /**
     * 加入购物车回调
     */
    BpiHttpHandler.IBpiHttpHandler mIbpiAddShopCar = new BpiHttpHandler.IBpiHttpHandler() {
        @Override
        public void onError(int id, String message) {
            waitPop.dismiss();
            HighCommunityUtils.GetInstantiation().ShowToast(message, 0);
        }

        @Override
        public void onSuccess(Object message) {
            waitPop.dismiss();
            HighCommunityUtils.GetInstantiation().ShowToast("成功加入购物车", 0);
        }

        @Override
        public Object onResolve(String result) {
            Log.e(Tag, "onResolve result" + result);
            return result;
        }

        @Override
        public void setAsyncTask(AsyncTask asyncTask) {

        }

        @Override
        public void cancleAsyncTask() {
            waitPop.dismiss();
        }

        @Override
        public void shouldLogin(boolean isShouldLogin) {

        }

        @Override
        public void shouldLoginAgain(boolean isShouldLogin, String msg) {
            if (isShouldLogin) {
                HighCommunityUtils.GetInstantiation().ShowToast(msg, 0);
                HighCommunityApplication.toLoginAgain(context);
            }
        }
    };


    class ViewHolder extends RecyclerView.ViewHolder {
        ImageView mGoodsimg;
        TextView mTvTag;
        TextView mTitle;
        TextView mNowPrice;
        TextView mOldPrice;
        TextView mSaledNum;
        ImageView mShopcart;

        public ViewHolder(View view) {
            super(view);
            mGoodsimg = (ImageView) view.findViewById(R.id.supplygoodsmore_goodsimg);
            mTvTag = (TextView) view.findViewById(R.id.supplygoodsmore_tv_tag);
            mTitle = (TextView) view.findViewById(R.id.supplygoodsmore_goodsTitle);
            mNowPrice = (TextView) view.findViewById(R.id.supplygoodsmore_nowPrice);
            mOldPrice = (TextView) view.findViewById(R.id.supplygoodsmore_oldPrice);
            mSaledNum = (TextView) view.findViewById(R.id.supplygoodsmore_saledNum);
            mShopcart = (ImageView) view.findViewById(R.id.supplygoodsmore_shopcart);
        }
    }

    public static interface OnRecyclerViewItemClickListener {
        void onItemClick(View view, SupplyGoodsMoreBean.SupplyGoodsMoreDataEntity.SupplyMoreGoodsEntity data);
    }

    private OnRecyclerViewItemClickListener mOnItemClickListener = null;

    public void setOnItemClickListener(OnRecyclerViewItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }


    @Override
    public void onClick(View v) {
        if (mOnItemClickListener != null) {
            mOnItemClickListener.onItemClick(v, (SupplyGoodsMoreBean.SupplyGoodsMoreDataEntity.SupplyMoreGoodsEntity) v.getTag());
        }
    }

}
