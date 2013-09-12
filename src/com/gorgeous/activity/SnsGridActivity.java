package com.gorgeous.activity;

import android.app.Activity;
import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;

import com.gorgeous.R;
import com.gorgeous.adapter.SnsAdapter;

public class SnsGridActivity extends Activity {

	private String[] snsName = { "Sina", "Renren", "Tencent", "Instagram" };

	private Context mContext;

	private GridView snsGridView;
	private SnsAdapter snsGridAdapter;
	private Button okButton;

	private int mAppWidgetId;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mContext = getBaseContext();
		initLayout();
	}

	private void initLayout() {
		DisplayMetrics displayMetrics = new DisplayMetrics();
		this.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

		setContentView(R.layout.sns_grid);
		okButton = (Button) findViewById(R.id.finish_button);
		okButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		snsGridView = (GridView) findViewById(R.id.sns_name);
		snsGridAdapter = new SnsAdapter(mContext, snsName,
				displayMetrics.widthPixels);
		snsGridView.setAdapter(snsGridAdapter);
	}

	@Override
	protected void onResume() {
		super.onResume();
		Intent intent = getIntent();
		Bundle extras = intent.getExtras();
		if (extras != null) {
			mAppWidgetId = extras.getInt(AppWidgetManager.EXTRA_APPWIDGET_ID,
					AppWidgetManager.INVALID_APPWIDGET_ID);
		}

		Intent resultValue = new Intent();
		resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, mAppWidgetId);
		setResult(RESULT_OK, resultValue);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
	}
}
