package cn.hi028.android.highcommunity.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import cn.hi028.android.highcommunity.activity.fragment.HuiChipsFrag;
import cn.hi028.android.highcommunity.activity.fragment.newhui.NewSupplyFragment;

/**
 * 新版惠生活pager适配器
 */
public class NewHuiLifePagerAdapter extends FragmentPagerAdapter {
    NewSupplyFragment mNewSupplyFragment;
    HuiChipsFrag mHuiChipsFrag;
    final String Tag="NewHuiLifePagerAdapter";
    public NewHuiLifePagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int arg0) {
        if (arg0 == 0) {
            if (mNewSupplyFragment == null) {
                mNewSupplyFragment = new NewSupplyFragment();
            }
            return mNewSupplyFragment;
        } else {
            if (mHuiChipsFrag == null) {
                mHuiChipsFrag = new HuiChipsFrag();
            }
            return mHuiChipsFrag;
        }
    }

    @Override
    public int getCount() {
        return 2;
    }


}
