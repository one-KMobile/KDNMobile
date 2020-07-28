package com.kdn.mtps.mobile.util;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.kdn.mtps.mobile.R;


public class ToastUtil {
	static int DEFAULT_DURATION = Toast.LENGTH_SHORT;

	static Toast toast = null;

	public static void show(Context context, String msg) {
		if (toast != null)
			toast.cancel();
		toast = Toast.makeText(context, msg, DEFAULT_DURATION);
		toast.show();
	}

	public static void show(Context context, String msg, int duration) {
		if (toast != null)
			toast.cancel();
		toast = Toast.makeText(context, msg, duration);
		toast.show();
	}

	public static void show(Context context, int msgId) {
		if (toast != null)
			toast.cancel();
		toast = Toast.makeText(context, msgId, DEFAULT_DURATION);
		toast.show();
	}

	public static void show(Context context, int msgId, int duration) {
		if (toast != null)
			toast.cancel();
		toast = Toast.makeText(context, msgId, duration);
		toast.show();
	}

	public static void showPushMsg(Context context) {
		if (toast != null)
			toast.cancel();
		toast = new KDNToast(context, "", "");
		toast.show();
	}
	
	static class KDNToast extends Toast {
		public KDNToast(Context context, String title, String msg) {
			super(context);

			LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			View layout = inflater.inflate(R.layout.toast_layout, null);

			LinearLayout rootLayout = (LinearLayout) layout.findViewById(R.id.toast_layout_root);
			rootLayout.setBackgroundResource(android.R.drawable.toast_frame);

			ImageView image = (ImageView) layout.findViewById(R.id.imgToast);

			TextView txtToastFrom = (TextView) layout.findViewById(R.id.txtToastFrom);
			txtToastFrom.setText("aaa");

			TextView txtToastMsg = (TextView) layout.findViewById(R.id.txtToastMsg);
			txtToastMsg.setText("bbb");

//			setGravity(Gravity.TOP | Gravity.CENTER_HORIZONTAL, 0, 100);
			setDuration(this.LENGTH_SHORT);

			setView(layout);
		}

	}
}
