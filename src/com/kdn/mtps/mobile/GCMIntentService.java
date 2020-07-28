package com.kdn.mtps.mobile;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.google.android.gcm.GCMBaseIntentService;
import com.kdn.mtps.mobile.constant.ConstEKEY;
import com.kdn.mtps.mobile.constant.ConstSP;
import com.kdn.mtps.mobile.gcm.PushDialog;
import com.kdn.mtps.mobile.net.api.ApiManager;
import com.kdn.mtps.mobile.util.AppUtil;
import com.kdn.mtps.mobile.util.Logg;
import com.kdn.mtps.mobile.util.Shared;
import com.kdn.mtps.mobile.util.thread.ATask;

public class GCMIntentService extends GCMBaseIntentService {
	public static final String SEND_ID = "1031281967800";// GCM Project Number
	
	public static final String TAG_NAME = "GCMIntentService";

	public String groupFrom;
	
	public GCMIntentService() {
		this(SEND_ID);
	}

	public GCMIntentService(String senderId) {
		super(senderId);
	}

	Handler handler = new Handler(Looper.getMainLooper());

	@Override
	protected void onMessage(Context context, Intent intent) {
		Log.d(TAG_NAME, "onMessage");
		
		try {
			//푸쉬를 받는다
			if (intent != null) {
				String title = intent.getStringExtra("title");
				String msg = intent.getStringExtra("msg");
				String device_del = intent.getStringExtra("device_del");
				String device_token = intent.getStringExtra("device_token");
				String board_idx = intent.getStringExtra("board_idx");
				
				if (msg != null) {
					Log.d(TAG_NAME, "msg:" + title + " / " + msg + " / " + device_del + " / " + device_token + " / " + board_idx);
					
					//삭제요청이 오면 앱에 있는 모든 데이터 삭제
					if ("Y".equalsIgnoreCase(device_del)) {
						deviceDelConfirm();
					} else { // 아니면 푸쉬 메시지를 보여준다.
						goPush_Dialog(title, msg, board_idx, context);
					}
					
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void goPush_Dialog(final String title, final String msg, final String board_idx, final Context mContext) {
		handler.post(new Runnable() {
			public void run() {
				try
				{
					Intent intent = new Intent(mContext, PushDialog.class);
					intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP);

					Logg.d("msg00 : " + msg);
					intent.putExtra(ConstEKEY.GCM_TITLE, title);
					intent.putExtra(ConstEKEY.GCM_MSG, msg);
					intent.putExtra(ConstEKEY.BOARD_IDX, board_idx);
					mContext.startActivity(intent);
				}
				catch(Exception e)
				{
					Log.e(TAG_NAME, e.getMessage(), e);
				}
			}
		});
	}
	
	@Override
	protected void onError(Context context, String errorId) {
		Logg.d("onError. errorId : " + errorId);
	}

	@Override
	protected void onRegistered(Context context, String regId) {
		Logg.d("onRegistered. regId : " + regId);
	}

	@Override
	protected void onUnregistered(Context context, String regId) {
		Logg.d("onUnregistered. regId : " + regId);
	}

	@Override
	protected boolean onRecoverableError(Context context, String errorId) {
		Logg.d("onRecoverableError. errorId : " + errorId);
		return super.onRecoverableError(context, errorId);
	}
	
	public void deviceDelConfirm() {
		
		final String loginId = Shared.getString(this, ConstSP.LOING_ID_KEY);
		
		ATask.executeVoid(new ATask.OnTask() {
			public void onPre() {
			}

			public void onBG() {
				 ApiManager.deviceDelConfirm(loginId);
			}

			@Override
			public void onPost() {
				AppUtil.clearApplicationData(GCMIntentService.this);
				android.os.Process.killProcess(android.os.Process.myPid());
			}
		});
		
	}
}