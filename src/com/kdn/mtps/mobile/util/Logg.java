package com.kdn.mtps.mobile.util;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import com.kdn.mtps.mobile.constant.ConstVALUE;

public class Logg {
	public static void d(Context context, String msg) {
		if (ConstVALUE.DEBUG)
			l(context.getClass().getSimpleName() + "@  " + msg);
	}
	
	public static void d(Activity activity, String msg) {
		if (ConstVALUE.DEBUG)
			l(activity.getClass().getSimpleName() + "@  " + msg);
	}
	
	public static void d(String msg) {
		if (ConstVALUE.DEBUG)
			l(msg);
	}
	
	public static void l(String msg) {
		Log.d("KDN_DEBUG", msg == null ? "null" : msg);
	}
	
}
