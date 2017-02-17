package cn.hi028.android.highcommunity.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import cn.hi028.android.highcommunity.R;
import cn.hi028.android.highcommunity.bean.MerchantGoodTitleBean;

public class MerchantGoodLeftAdapter extends MyBaseAdapter<MerchantGoodTitleBean> {
	int index= 0;
	public void setIndex(int index) {
		this.index = index;
	}

	public MerchantGoodLeftAdapter(Context context,
			List<MerchantGoodTitleBean> data) {
		super(context, data);
	}
	@Override
	public int getItemResource(int pos) {
		return R.layout.item_merchant_goods_left_list;
	}

	@Override
	public View getItemView(int position, View convertView,
			MyBaseAdapter<MerchantGoodTitleBean>.ViewHolder holder,
			ViewGroup parent) {
		MerchantGoodTitleBean info = data.get(position);
		TextView content = holder
				.getView(R.id.item_merchant_left_list_content_tv);
		content.setText(info.getName());
		content.setTextColor(Color.parseColor(index==position?"#1FC796":"#4A4A4A"));
		content.setTextColor(Color.parseColor(index==position?"#1FC796":"#4A4A4A"));
		if (info.isIscheck()) {

		}
		return convertView;
	}

}
