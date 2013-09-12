package com.gorgeous.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

public class ColorAdapter extends BaseAdapter {
	private Context mContext;
	private int[] imageList;
	private int[] iconList;

	public ColorAdapter(Context cxt, int[] imageList, int[] iconList) {
		mContext = cxt;
		this.imageList = imageList;
		this.iconList = iconList;
	}

	@Override
	public int getCount() {
		return imageList.length;
	}

	@Override
	public Object getItem(int position) {
		return imageList[position];
	}

	@Override
	public long getItemId(int position) {
		return imageList[position];
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ImageView imageView;
		if (convertView == null) {
			imageView = new ImageView(mContext);
			imageView.setLayoutParams(new GridView.LayoutParams(50, 50));
			imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
			imageView.setPadding(8, 0, 8, 0);
		} else {
			imageView = (ImageView) convertView;
		}
		imageView.setImageResource(iconList[position]);
		return imageView;
	}

}