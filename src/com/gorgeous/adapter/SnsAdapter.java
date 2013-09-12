package com.gorgeous.adapter;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView.LayoutParams;
import android.widget.BaseAdapter;

import com.gorgeous.R;
import com.gorgeous.activity.Loge;
import com.gorgeous.view.SnsBoardView;

public class SnsAdapter extends BaseAdapter {

	private Context mContext;
	private String[] snsName;
	private int snsWidth;

	public SnsAdapter(Context ctx, String[] name, int width) {
		mContext = ctx;
		snsName = name;
		snsWidth = width / 2 - 20;
	}

	@Override
	public int getCount() {
		if (snsName != null) {
			return snsName.length;
		}
		return 0;
	}

	@Override
	public Object getItem(int position) {
		if (snsName != null) {
			return snsName[position];
		}
		return null;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		SnsBoardView bView;
		if (convertView == null) {
			bView = new SnsBoardView(mContext, snsWidth, snsWidth);
			bView.setLayoutParams(new LayoutParams(snsWidth, snsWidth));
		} else {
			bView = (SnsBoardView) convertView;
		}

		if (snsName != null) {
			bView.setScrText(snsName[position]);
			switch (position) {
			case 0:
				bView.setFootColor(Color.YELLOW);
				bView.setSrcPic(BitmapFactory.decodeResource(
						mContext.getResources(), R.drawable.icon));
				break;
			case 1:
				bView.setFootColor(Color.BLUE);
				bView.setSrcPic(BitmapFactory.decodeResource(
						mContext.getResources(), R.drawable.icon));
				break;
			case 2:
				bView.setFootColor(Color.RED);
				bView.setSrcPic(BitmapFactory.decodeResource(
						mContext.getResources(), R.drawable.icon));
				break;
			case 3:
				bView.setFootColor(Color.MAGENTA);
				bView.setSrcPic(BitmapFactory.decodeResource(
						mContext.getResources(), R.drawable.icon));
				break;
			default:
				break;
			}
		}

		return bView;
	}

}
