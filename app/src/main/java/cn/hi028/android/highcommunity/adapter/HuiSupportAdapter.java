/***************************************************************************
 * Copyright (c) by raythinks.com, Inc. All Rights Reserved
 **************************************************************************/

package cn.hi028.android.highcommunity.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import net.duohuo.dhroid.util.ImageLoaderUtil;
import net.duohuo.dhroid.util.ListUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.hi028.android.highcommunity.R;
import cn.hi028.android.highcommunity.activity.PhotoScanActivity;
import cn.hi028.android.highcommunity.activity.fragment.CommunityFrag;
import cn.hi028.android.highcommunity.bean.HuiSupportBean;
import cn.hi028.android.highcommunity.bean.UrlsBean;
import cn.hi028.android.highcommunity.utils.Constacts;

/**
 * @功能：直供幻灯片<br>
 * @作者： 赵海<br>
 * @版本：1.0<br>
 * @时间：2016-01-15<br>
 */
public class HuiSupportAdapter extends PagerAdapter {
    Context context;
    Map<Integer, ImageView> viewlist = new HashMap<Integer, ImageView>();
    public List<HuiSupportBean> getData() {
        return data;
    }

    public void setData(List<HuiSupportBean> data) {
        this.data = data;
        notifyDataSetChanged();
    }

    List<HuiSupportBean> data = new ArrayList<HuiSupportBean>();

    public HuiSupportAdapter(Context context) {
        this.context = context;
    }

    @Override
    public int getCount() {
        //设置成最大，使用户看不到边界
        return ListUtils.getSize(data);//Integer.MAX_VALUE;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return (view == object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {
        ImageView view = new ImageView(context);
        viewlist.put(position, view);
        container.addView(view);
        ImageLoaderUtil.disPlay(Constacts.IMAGEHTTP + data.get(position).getPic().
                get(data.get(position).getCurrentPicPosition()).getBig(), view, R.mipmap.default_no_pic, null);
        view.setScaleType(ImageView.ScaleType.FIT_XY);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UrlsBean mUrls = new UrlsBean();
                for (int i = 0; i < data.get(position).getPic().size(); i++) {
                    mUrls.getmUrlList().add(Constacts.IMAGEHTTP + data.get(position).getPic().get(i).getBig());
                }
                CommunityFrag.isNeedRefresh = false;
                Intent mBigPhoto = new Intent(context, PhotoScanActivity.class);
                mBigPhoto.putExtra("data", mUrls);
                mBigPhoto.putExtra("ID", data.get(position).getCurrentPicPosition());
                context.startActivity(mBigPhoto);
                ((Activity) context).overridePendingTransition(R.anim.dyn_pic_scan_miss, R.anim.dyn_pic_scan_miss_no);
            }
        });
        return view;
    }

    public void updateImage(int position) {
        ImageLoaderUtil.disPlay(Constacts.IMAGEHTTP + data.get(position).getPic().
                get(data.get(position).getCurrentPicPosition()).getBig(), viewlist.get(position), R.mipmap.default_no_pic, null);
    }

    @Override
    public void startUpdate(ViewGroup container) {
        super.startUpdate(container);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }
}

