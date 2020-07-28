package com.kdn.mtps.mobile.gcm;

import android.app.KeyguardManager;
import android.app.KeyguardManager.KeyguardLock;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.PowerManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.kdn.mtps.mobile.BaseActivity;
import com.kdn.mtps.mobile.R;
import com.kdn.mtps.mobile.constant.ConstEKEY;
import com.kdn.mtps.mobile.notice.NoticeListActivity;
import com.kdn.mtps.mobile.util.Logg;

public class PushDialog extends BaseActivity implements OnClickListener {
	TextView txtToastFrom, txtToastMsg;
	Button btn_ok, btn_cancel;
	String title, msg, board_idx;

	private static final String TAG_NAME = "Push_Dialog";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.toast_push_layout);

		PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
		if (!pm.isScreenOn()) {
			KeyguardManager keyguardManager = (KeyguardManager) getApplicationContext().getSystemService(Context.KEYGUARD_SERVICE); 
	        KeyguardLock keyguardLock =  keyguardManager.newKeyguardLock("TAG");
	        keyguardLock.disableKeyguard();
		}
		
		title = getIntent().getStringExtra(ConstEKEY.GCM_TITLE);
		msg = getIntent().getStringExtra(ConstEKEY.GCM_MSG);
		board_idx = getIntent().getStringExtra(ConstEKEY.BOARD_IDX);
		
		setLayout();
		setData();
	}

	@Override
	protected void onDestroy() {
		PushWakeUp.releaseWakeLock();
		super.onDestroy();
	}

	private void setData() {
		Logg.d("msg : " + msg);
		
		txtToastFrom.setText(title);
		txtToastMsg.setText(msg);
	}

	private void setLayout() {
		txtToastFrom = (TextView) findViewById(R.id.txtToastFrom);
		txtToastMsg = (TextView) findViewById(R.id.txtToastMsg);

		btn_ok = (Button) findViewById(R.id.btn_ok);
		btn_ok.setOnClickListener(this);

		btn_cancel = (Button) findViewById(R.id.btn_cancel);
		btn_cancel.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.btn_ok) {
			Intent notiIntent = new Intent(this, NoticeListActivity.class);
			notiIntent.putExtra(ConstEKEY.BOARD_IDX, board_idx);
			notiIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
			startActivity(notiIntent);
			
			finish();
		} else if (v.getId() == R.id.btn_cancel) {
			finish();
		}
	}

	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		
		PushWakeUp.wakeLock(this);
		
		title = getIntent().getStringExtra(ConstEKEY.GCM_TITLE);
		msg = getIntent().getStringExtra(ConstEKEY.GCM_MSG);
		
		Logg.d("msg onNewintent  : " + msg);
		setData();
	}

	@Override
	protected void onResume() {
		super.onResume();
		
		getWindow().addFlags(
				WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED | WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD
						| WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);

		PushWakeUp.wakeLock(this);
		PushWakeUp.releaseWakeLock();
		
	}

	
}
