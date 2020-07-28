package com.kdn.mtps.mobile.util;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;

import com.kdn.mtps.mobile.R;

public class AlertUtil {

	static public AlertDialog showNoTitleAlertOK(Context context, String message) {
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setMessage(message);
		builder.setPositiveButton(android.R.string.ok, null);
		return builder.show();
	}
	
	static public AlertDialog showNoTitleAlertOK(Context context, String message, DialogInterface.OnClickListener listener) {
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setMessage(message);
		builder.setPositiveButton(android.R.string.ok, listener);
		return builder.show();
	}
	
	static public AlertDialog showNoTitleAlertOK(Context context, String message, DialogInterface.OnClickListener listener, DialogInterface.OnClickListener cancelListener) {
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setMessage(message);
		builder.setPositiveButton(android.R.string.ok, listener);
		builder.setNegativeButton(android.R.string.cancel, cancelListener);
		return builder.show();
	}
	
	static public AlertDialog showNoTitleAlertOK(Context context, String message, DialogInterface.OnClickListener listener, String posStr, DialogInterface.OnClickListener cancelListener, String negStr) {
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setMessage(message);
		builder.setPositiveButton(posStr, listener);
		builder.setNegativeButton(negStr, cancelListener);
		return builder.show();
	}
	
	static public AlertDialog showAlertOK(Context context, String title, String message) {
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setTitle(title);
		builder.setMessage(message);
		builder.setPositiveButton(android.R.string.ok, null);
		return builder.show();
	}

	static public AlertDialog showAlertOK(Context context, String title, String message, DialogInterface.OnClickListener listener) {
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setTitle(title);
		builder.setMessage(message);
		builder.setPositiveButton(android.R.string.ok, listener);
		return builder.show();
	}

	static public AlertDialog showAlertConfirm(Context context, String title, String message, DialogInterface.OnClickListener okListener, DialogInterface.OnClickListener cancelListener) {
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setTitle(title);
		builder.setMessage(message);
		builder.setPositiveButton(android.R.string.ok, okListener);
		builder.setNegativeButton(android.R.string.cancel, cancelListener);
		return builder.show();
	}
	
	static public AlertDialog showAlertOK(Context context, String title, View layout, DialogInterface.OnClickListener okListener, DialogInterface.OnClickListener cancelListener) {
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setView(layout);
		builder.setPositiveButton(android.R.string.ok, okListener);
		builder.setNegativeButton(android.R.string.cancel, cancelListener);
		return builder.show();
	}
	
	static public AlertDialog showAlert(Context context, View layout, int x, int y) {
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setView(layout);
		AlertDialog alert = builder.create();
//		alert.getWindow().setGravity(Gravity.BOTTOM);
		
//		LayoutParams params = alert.getWindow().getAttributes();
//		params.width = 480;
//		params.height = 800;
//		alert.getWindow().setAttributes(params);
		WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
		
		lp.copyFrom(alert.getWindow().getAttributes());
		lp.height = WindowManager.LayoutParams.FILL_PARENT;
		lp.x = x;
		lp.y = y;
		
		alert.show();
		
		alert.getWindow().setAttributes(lp);
		alert.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
//		alert.getWindow().setBackgroundDrawableResource(R.drawable.gift_transparent);
		
		return alert;
	}

	static ProgressDialog progressDialog = null;
	
	static public ProgressDialog showProgress(Context context, String title, String message) {
		if(progressDialog != null)
			return progressDialog;
		
		progressDialog = new ProgressDialog(context);
		progressDialog.setCancelable(false);
		progressDialog.setTitle(title);
		progressDialog.setMessage(message);
		progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		progressDialog.show();
		return progressDialog;
	}
	
	static public void hideProgress() {
		if(progressDialog == null)
			return;
		progressDialog.dismiss();
		progressDialog = null;
	}
	
}
