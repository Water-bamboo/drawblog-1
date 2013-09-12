package com.gorgeous.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class DrawBlogUtils {

	public static boolean isLogin() {
		return false;
	}

	public static boolean isNetworkAvailable(Context ctx) {
		final ConnectivityManager connectivity = (ConnectivityManager) ctx
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		if (connectivity != null) {
			final NetworkInfo info = connectivity.getActiveNetworkInfo();
			if (info != null) {
				return true;
			}
		}
		return false;
	}

}
