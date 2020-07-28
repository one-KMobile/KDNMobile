package com.kdn.mtps.mobile.util;

import com.kdn.mtps.mobile.BaseActivity;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

public class UIUtil {
	public static void hideKeyboard(View view) {
		InputMethodManager ipm= (InputMethodManager)view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
		ipm.hideSoftInputFromWindow(view.getWindowToken(), 0);
	}
	
	public static float dpFromPx(Activity activity, float px)
	{
	    return px / activity.getResources().getDisplayMetrics().density;
	}


	public static float pxFromDp(Activity activity, float dp)
	{
	    return dp * activity.getResources().getDisplayMetrics().density;
	}
	
	public static void setFont(Context ctx, ViewGroup root) {
		Typeface mTypeface = BaseActivity.getTypeface(ctx);
		
	    for (int i = 0; i < root.getChildCount(); i++) {
	        View child = root.getChildAt(i);
	        if (child instanceof TextView)
	            ((TextView)child).setTypeface(mTypeface);
	        else if (child instanceof ViewGroup)
	        	setFont(ctx, (ViewGroup)child);
	    }
	}
}
