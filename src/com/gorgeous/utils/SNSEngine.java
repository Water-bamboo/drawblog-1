package com.gorgeous.utils;

import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

import android.content.Context;

public class SNSEngine {

	private final static AtomicLong count = new AtomicLong(0);
	
	private static SNSEngine mSnsEngine;
	private String mClientId;
	private String mClientKey;

	public static synchronized SNSEngine getInstance(Context ctx,
			String accout_type) {
		try {
			if (mSnsEngine == null) {
				count.incrementAndGet();
			}
			return mSnsEngine;
		} catch (Exception e) {
		}
		return mSnsEngine;
	}

	public SNSEngine(String client_id, String client_key) {
		mClientId = client_id;
		mClientKey = client_key;
	}

	public static String buildParams(HashMap<String, Object> params,
			String splitter) {
		StringBuffer buf = new StringBuffer();
		if (params == null) {
			params = new HashMap<String, Object>();
		}
		Iterator<String> itrs = params.keySet().iterator();
		List<String> sortKeyList = new ArrayList<String>();
		while (itrs.hasNext()) {
			sortKeyList.add(itrs.next());
		}
		Collections.sort(sortKeyList);
		for (String key : sortKeyList) {
			if (buf.length() != 0) {
				buf.append(splitter);
			}
			buf.append(key).append("=");
			buf.append(params.get(key));
		}
		return buf.toString();
	}

	public URL getLoginUrl(String auth_url, String re_uri, String type) {
		try {
			HashMap<String, Object> params = new HashMap<String, Object>();
			params.put("client_id", mClientId);
			params.put("redirect_uri", re_uri);
			params.put("response_type", type);
			String loginUrl = auth_url + "?" + buildParams(params, "&");
			return new URL(loginUrl);
		} catch (Exception e) {
		}
		return null;
	}

}
