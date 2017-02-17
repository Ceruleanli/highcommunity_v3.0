package cn.hi028.android.highcommunity.adapter;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * 惠生活订单适配器
 */
public class HuiOrderAdapter  extends PagerAdapter {
	public List<View> getViewList() {
		return viewList;
	}

	public void setViewList(List<View> viewList) {
		this.viewList = viewList;
		notifyDataSetChanged();
	}

	List<View>viewList=new ArrayList<View>();
	@Override
	public int getCount() {
		return viewList.size();
	}
	@Override
	public boolean isViewFromObject(View arg0, Object arg1) {
		return arg0 == arg1;
	}
	@Override
	public void destroyItem(ViewGroup view, int position, Object object) {
		view.removeView(viewList.get(position));
	}
	@Override
	public Object instantiateItem(ViewGroup view, int position) {
		view.addView(viewList.get(position));
		return viewList.get(position);
	}
}