package com.gorgeous.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.database.ContentObserver;
import android.os.Handler;
import android.widget.RemoteViews;

import com.gorgeous.R;
import com.gorgeous.activity.Loge;
import com.gorgeous.activity.MainDrawing;
import com.gorgeous.activity.SnsGridActivity;

public class DrawBlogWidgetProvider extends AppWidgetProvider {

	private static DrawBlogWidgetObserver sDataObserver;
	private static Handler sWorkerQueue;

	public static String CLICK_ACTION1 = "com.gorgeous.CLICK1";
	public static String CLICK_ACTION2 = "com.gorgeous.CLICK2";

	public DrawBlogWidgetProvider() {

	}

	@Override
	public void onEnabled(Context context) {
		Loge.i("onEnabled");
		if (sDataObserver == null) {
			final AppWidgetManager mgr = AppWidgetManager.getInstance(context);
			final ComponentName cn = new ComponentName(context,
					DrawBlogWidgetProvider.class);
			sDataObserver = new DrawBlogWidgetObserver(mgr, cn, sWorkerQueue);
		}
	}

	@Override
	public void onReceive(Context ctx, Intent intent) {
		Loge.i("onReceive");
		final String action = intent.getAction();
		Loge.i("action: " + action);
		if (action.equals(CLICK_ACTION1)) {
			Intent i = new Intent();
			i.setClass(ctx, MainDrawing.class);
			i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			ctx.startActivity(i);
		} else if (action.equals(CLICK_ACTION2)) {
			Intent i = new Intent();
			i.setClass(ctx, SnsGridActivity.class);
			i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			ctx.startActivity(i);
		}
		super.onReceive(ctx, intent);
	}

	@Override
	public void onUpdate(Context context, AppWidgetManager appWidgetManager,
			int[] appWidgetIds) {
		Loge.i("onUpdate");
		for (int i = 0; i < appWidgetIds.length; ++i) {
			Loge.i("appWidgetIds: " + appWidgetIds);

			final RemoteViews rv = new RemoteViews(context.getPackageName(),
					R.layout.widget_layout);
			
			final Intent onClickIntent1 = new Intent(context,
					DrawBlogWidgetProvider.class);
			onClickIntent1.setAction(DrawBlogWidgetProvider.CLICK_ACTION1);
			onClickIntent1.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,
					appWidgetIds[i]);
			final PendingIntent click1PendingIntent1 = PendingIntent
					.getBroadcast(context, 0, onClickIntent1,
							PendingIntent.FLAG_UPDATE_CURRENT);
			rv.setOnClickPendingIntent(R.id.empty_view, click1PendingIntent1);

			final Intent onClickIntent2 = new Intent(context,
					DrawBlogWidgetProvider.class);
			onClickIntent2.setAction(DrawBlogWidgetProvider.CLICK_ACTION2);
			onClickIntent2.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,
					appWidgetIds[i]);
			final PendingIntent click1PendingIntent2 = PendingIntent
					.getBroadcast(context, 0, onClickIntent2,
							PendingIntent.FLAG_UPDATE_CURRENT);
			rv.setOnClickPendingIntent(R.id.button1, click1PendingIntent2);

			appWidgetManager.updateAppWidget(appWidgetIds[i], rv);
		}
		super.onUpdate(context, appWidgetManager, appWidgetIds);
	}

}

class DrawBlogWidgetObserver extends ContentObserver {

	DrawBlogWidgetObserver(AppWidgetManager mgr, ComponentName cn, Handler h) {
		super(h);

	}

	@Override
	public void onChange(boolean selfChange) {
	}
}