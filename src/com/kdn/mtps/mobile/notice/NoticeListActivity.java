package com.kdn.mtps.mobile.notice;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;

import com.kdn.mtps.mobile.BaseActivity;
import com.kdn.mtps.mobile.MainActivity;
import com.kdn.mtps.mobile.R;
import com.kdn.mtps.mobile.TitleManager;
import com.kdn.mtps.mobile.constant.ConstEKEY;
import com.kdn.mtps.mobile.constant.ConstVALUE;
import com.kdn.mtps.mobile.util.AppUtil;

public class NoticeListActivity extends BaseActivity implements TitleManager{

	WebView webView;
	String board_idx;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_notice_list);
		
		board_idx = getIntent().getStringExtra(ConstEKEY.BOARD_IDX);
		
		setInit();
		
		setTitle();
		
	}
	
	@Override
	public void setTitle() {
		Button btnHeaderTitle = (Button)findViewById(R.id.btnHeaderTitle);
		Button btnHeaderLeft = (Button)findViewById(R.id.btnHeaderLeft);
		Button btnHeaderRight = (Button)findViewById(R.id.btnHeaderRight);
		
		btnHeaderTitle.setText("공지사항");
		
		btnHeaderLeft.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		
		btnHeaderRight.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				AppUtil.startActivity(NoticeListActivity.this, new Intent(NoticeListActivity.this, MainActivity.class));
			}
		});
	}

	public void setInit() {
		webView = (WebView)findViewById(R.id.webView);
		
		webView.getSettings().setJavaScriptEnabled(true);

		final Activity activity = this;
		webView.setWebChromeClient(new WebChromeClient() {
			   public void onProgressChanged(WebView view, int progress) {
			     // Activities and WebViews measure progress with different scales.
			     // The progress meter will automatically disappear when we reach 100%
			     activity.setProgress(progress * 1000);
			   }
			 });
		webView.setWebViewClient(new WebViewClient() {
			
			   public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
			   }
			 });

		if (board_idx != null && !"".equals(board_idx))
			webView.loadUrl(ConstVALUE.NOTICE_DETAIL_URL + board_idx);
		else
			webView.loadUrl(ConstVALUE.NOTICE_LIST_URL);

	}
	
//	@Override
//	public void onBackPressed() {
//		if (webView.canGoBack())
//			webView.goBack();
//		else
//			super.onBackPressed();
//	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

	@Override
	protected void onPause() {
		super.onPause();
	}

	@Override
	protected void onResume() {
		super.onResume();
	}

}
