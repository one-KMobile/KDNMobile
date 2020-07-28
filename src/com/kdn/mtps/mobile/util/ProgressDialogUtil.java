package com.kdn.mtps.mobile.util;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Handler;
import android.os.Message;

public class ProgressDialogUtil {
	private static ProgressDialog pd;
	
	static Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			
			dismissDialog();
		}
	};
	
	public static void showDialog(Context context, String msg) {
		
		if (pd != null)
			pd.dismiss();
		
		pd = new ProgressDialog(context);
		pd.setMessage(msg);
		pd.setCancelable(false);
		pd.setCanceledOnTouchOutside(false);
		pd.show();
		
	}

	public static void showDialog(Context context, String msg, int delayTime) {
		
		if (pd != null)
			pd.dismiss();
		
		pd = new ProgressDialog(context);
		pd.setMessage(msg);
		pd.setCancelable(false);
		pd.setCanceledOnTouchOutside(false);
		pd.show();
		
		handler.sendEmptyMessageDelayed(0, delayTime);
	}
	
	public static void dismissDialog() {
		if (pd != null)
			pd.dismiss();
		
		pd = null;
		
	}
}
