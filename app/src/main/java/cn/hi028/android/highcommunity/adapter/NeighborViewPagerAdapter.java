package cn.hi028.android.highcommunity.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import cn.hi028.android.highcommunity.activity.fragment.ActFrag_NewV;
import cn.hi028.android.highcommunity.activity.fragment.CommunityFrag;
import cn.hi028.android.highcommunity.activity.fragment.GroupFrag;

/**
 * 邻里适配器
 */
public class NeighborViewPagerAdapter extends FragmentPagerAdapter {
    CommunityFrag mCommuFrag;
    GroupFrag mGroupFrag;
    ActFrag_NewV mActFrag;
    final String Tag = "-NeighborFrag";

    public NeighborViewPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int arg0) {
        if (arg0 == 0) {
            if (mCommuFrag == null) {
                mCommuFrag = new CommunityFrag();
            }
            return mCommuFrag;
        } else if (arg0 == 1) {
            if (mGroupFrag == null) {
                mGroupFrag = new GroupFrag();
            }
            return mGroupFrag;
        } else {
            if (mActFrag == null) {
                mActFrag = new ActFrag_NewV();
            }
            return mActFrag;

        }
    }

    @Override
    public int getCount() {
        return 3;
    }


}
