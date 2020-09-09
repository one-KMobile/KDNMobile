package com.kdn.mtps.mobile;

import java.util.Calendar;
import java.util.GregorianCalendar;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Rect;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationProvider;
import android.net.Uri;
import android.os.Bundle;
import android.util.*;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

import com.kdn.mtps.mobile.constant.ConstApiResult;
import com.kdn.mtps.mobile.constant.ConstSP;
import com.kdn.mtps.mobile.constant.ConstVALUE;
import com.kdn.mtps.mobile.datasync.DataSync;
import com.kdn.mtps.mobile.datasync.DataSync.OnSyncCompletedListener;
import com.kdn.mtps.mobile.db.CameraLogDao;
import com.kdn.mtps.mobile.db.DBHelper;
import com.kdn.mtps.mobile.db.InputBRDao;
import com.kdn.mtps.mobile.db.InputBRSubInfoDao;
import com.kdn.mtps.mobile.db.InputBTDao;
import com.kdn.mtps.mobile.db.InputHJDao;
import com.kdn.mtps.mobile.db.InputHKDao;
import com.kdn.mtps.mobile.db.InputHKSubInfoDao;
import com.kdn.mtps.mobile.db.InputJGUDao;
import com.kdn.mtps.mobile.db.InputJGUSubInfoDao;
import com.kdn.mtps.mobile.db.InputJJDao;
import com.kdn.mtps.mobile.db.InputJPDao;
import com.kdn.mtps.mobile.db.InputJSDao;
import com.kdn.mtps.mobile.db.InputJSSubInfoDao;
import com.kdn.mtps.mobile.db.InputKBDao;
import com.kdn.mtps.mobile.db.InspectResultMasterDao;
import com.kdn.mtps.mobile.db.MyLocationLogDao;
import com.kdn.mtps.mobile.info.CodeInfo;
import com.kdn.mtps.mobile.info.UserInfo;
import com.kdn.mtps.mobile.input.InputBTActivity;
import com.kdn.mtps.mobile.net.api.ApiManager;
import com.kdn.mtps.mobile.net.api.bean.CodeList;
import com.kdn.mtps.mobile.net.api.bean.NoticeList;
import com.kdn.mtps.mobile.net.api.bean.TracksList;
import com.kdn.mtps.mobile.net.http.HttpUtil;
import com.kdn.mtps.mobile.service.KDNService;
import com.kdn.mtps.mobile.util.AlertUtil;
import com.kdn.mtps.mobile.util.AnsemUtil;
import com.kdn.mtps.mobile.util.AppUtil;
import com.kdn.mtps.mobile.util.LocManager;
import com.kdn.mtps.mobile.util.Logg;
import com.kdn.mtps.mobile.util.NetUtil;
import com.kdn.mtps.mobile.util.Shared;
import com.kdn.mtps.mobile.util.ToastUtil;
import com.kdn.mtps.mobile.util.thread.ATask;

public class LoginActivity extends BaseActivity implements OnClickListener, OnSyncCompletedListener{

	boolean isRunning = true; 	
	boolean isReady = false;

	
	EditText etId;
	EditText etPassward;
	Button btnLogin;
	ImageView ivTitleId;
	TextView tvVersion;
	
	int resultLogin;
	TracksList tracksList;
	CodeList codeList;
	NoticeList noticeList;
	int devCount; 
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		
		setInit();
	}

	public void setInit() {
		
		etId = (EditText)findViewById(R.id.etId);
		etPassward = (EditText)findViewById(R.id.etPassward);
		
		etPassward.setOnEditorActionListener(new OnEditorActionListener() {
			@Override
			public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
				if (actionId == EditorInfo.IME_ACTION_DONE) {
					clickLogin();
				}
				return false;
			}
		});
		
		btnLogin = (Button)findViewById(R.id.btnLogin);
		btnLogin.setOnClickListener(this);
		
		ivTitleId = (ImageView)findViewById(R.id.ivTitleId);
		ivTitleId.setOnClickListener(this);
		
		// 저장된 id, pwd 를 가져옴
		String loginId = Shared.getString(this, ConstSP.LOING_ID_KEY);
//		String loginPw = Shared.getString(this, ConstSP.LOING_PW_KEY);
		
		etId.setText(loginId);
//		etPassward.setText(loginPw);
		
		tvVersion = (TextView)findViewById(R.id.tvVersion);
		tvVersion.setText("v" + ConstVALUE.APP_CLIENT_VERSION);
		
	}
	
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		
	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.ivTitleId: 
			devCount++;
			
			if (devCount  == 5) {
				ToastUtil.show(this, "데이터 동기화 설정 해제!");
				Shared.set(LoginActivity.this, "AUTO_SYNC", true);
				devCount = 0;
				ToastUtil.show(this, "실서버 적용!!");
//				ConstVALUE.PREFIX_API_SERVER = "http://kdn.testbed.kr/";
//				ConstVALUE.PREFIX_API_SERVER = "http://10.0.2.2:8080/kdnweb/";
				ConstVALUE.PREFIX_API_SERVER = "http://172.30.1.35:8080/kdnweb/";
			} else if (devCount == 10){
				ToastUtil.show(this, "192.168.0.12 서버 적용!");
				ConstVALUE.PREFIX_API_SERVER = "http://192.168.0.12:8080/";
			}
			
			break;
		case R.id.btnLogin:
			
			clickLogin();
			
			break;
		}
	}
	
	public boolean checkInputLogin() {
		// 사번 입력
		String strId = etId.getText().toString();
		if ("".equals(strId)) {
			ToastUtil.show(this,	"ID(사번)를 입력해 주세요!");
			return false;
		}
		
		// 비번 입력
		String strPw = etPassward.getText().toString();
		if ("".equals(strPw)) {
			ToastUtil.show(this,	"비밀번호를 입력해 주세요");
			return false;
		}
		
		return true;
	}
	
	public void clickLogin() {
		//Log.i("MyTag","checkInputLogin : "+checkInputLogin());
		if ( !checkInputLogin() )
			return;

		if (NetUtil.isDisconnect(this)) {
			// 처음 시작여부는 첫 동기화가 되었는지에 대한 성공여부
			boolean isFirst = Shared.getBoolean(LoginActivity.this, ConstSP.FIRST_START_APP, true);
			if (isFirst) {
				ToastUtil.show(this, "로그인 한적이 없고 현재 인터넷이 연결되어있지 않아서 앱을 실행할 수 없습니다.");
				return;
			}
			
			boolean isPass = Shared.getBoolean(LoginActivity.this, ConstSP.LOING_PASS_KEY);
			if (isPass) {
				AppUtil.startActivity(LoginActivity.this, new Intent(LoginActivity.this, MainActivity.class));
				finish();
			}
			return;
		}
		
//		startLogin(etId.getText().toString(), etPassward.getText().toString());
		
		if (!AnsemUtil.isConnected(this)) {
			AnsemUtil.showAnsemLogin(this);
		} else {
			startLogin(etId.getText().toString(), etPassward.getText().toString());
		}
		
		isReady = true;
	}
	
	public void startLogin(final String id, final String pwd) {
		MyLocationLogDao myLocationLogDao = MyLocationLogDao.getInstance(this);
		myLocationLogDao.dbCheck();
		
		final String device_token = Shared.getString(LoginActivity.this, ConstSP.GCM_REGID_KEY);

		ATask.executeVoidProgress(this, R.string.loging, false, new ATask.OnTaskProgress() {
			public void onPre() {
			}

			public void onBG() {
				resultLogin = ApiManager.login(LoginActivity.this, id, pwd, device_token);
			}

			@Override
			public void onPost() {
				if (resultLogin == ConstApiResult.LOGIN_RESULT_OK) {
					
					String appServerVersion = UserInfo.getSessionData(LoginActivity.this).appVersion;
					
					// 클라이언트 버전과 서버 버전이 다르면 버전 업데이트 다이얼로그 활성. 버전이 다르면 앱 실행 불가
					if (!ConstVALUE.APP_CLIENT_VERSION.equals(appServerVersion)) {
						AlertUtil.showNoTitleAlertOK(LoginActivity.this, "업데이트 버전이 존재합니다.\n앱을 업데이트 하시기 바랍니다.", new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int which) {
								String appUrl = UserInfo.getSessionData(LoginActivity.this).appUrl;
								
								Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(appUrl));
								startActivity(intent);
							}
						});
						
						return;
					}
					
					String oldId = Shared.getString(LoginActivity.this, ConstSP.LOING_ID_KEY);
					String newId = etId.getText().toString();
					
					// 다른 아이디로 로그인 시 데이터 초기화
					if (oldId != null && !oldId.equals(newId)) {
						Shared.set(LoginActivity.this, ConstSP.FIRST_START_APP, true);
						Shared.set(LoginActivity.this, ConstSP.SETTING_WEATHER, "맑음");
						Shared.set(LoginActivity.this, ConstSP.SETTING_INSPECTER_ID_1, "");
						Shared.set(LoginActivity.this, ConstSP.SETTING_INSPECTER_NAME_1, "");
						Shared.set(LoginActivity.this, ConstSP.SETTING_INSPECTER_ID_2, "");
						Shared.set(LoginActivity.this, ConstSP.SETTING_INSPECTER_NAME_2, "");
						Shared.set(LoginActivity.this, ConstSP.SETTING_COMPANY, "");
						
						DBHelper.getInstance(LoginActivity.this).beginTransaction();
						MyLocationLogDao.getInstance(LoginActivity.this).DeleteAll();
						CameraLogDao.getInstance(LoginActivity.this).DeleteAll();
						InputBTDao.getInstance(LoginActivity.this).DeleteAll();
						InputJSDao.getInstance(LoginActivity.this).DeleteAll();
						InputJPDao.getInstance(LoginActivity.this).DeleteAll();
						InputJSSubInfoDao.getInstance(LoginActivity.this).DeleteAll();
						InputJJDao.getInstance(LoginActivity.this).DeleteAll();
						InputKBDao.getInstance(LoginActivity.this).DeleteAll();
						InputHKDao.getInstance(LoginActivity.this).DeleteAll();
						InputHJDao.getInstance(LoginActivity.this).DeleteAll();
						InputHKSubInfoDao.getInstance(LoginActivity.this).DeleteAll();
						InputBRDao.getInstance(LoginActivity.this).DeleteAll();
						InputBRSubInfoDao.getInstance(LoginActivity.this).DeleteAll();
						InspectResultMasterDao.getInstance(LoginActivity.this).DeleteAll();

						InputJGUDao.getInstance(LoginActivity.this).DeleteAll();
						InputJGUSubInfoDao.getInstance(LoginActivity.this).DeleteAll();
						
						DBHelper.getInstance(LoginActivity.this).setTransactionSuccessful();
						DBHelper.getInstance(LoginActivity.this).endTransaction();
						
						
//						AppUtil.clearApplicationData(LoginActivity.this);
					}
					
					String strWeather = Shared.getString(LoginActivity.this, ConstSP.SETTING_WEATHER);
					if (strWeather == null || strWeather.equals("")) {
						Shared.set(LoginActivity.this, ConstSP.SETTING_WEATHER, "맑음");
					}
					
					Shared.set(LoginActivity.this, ConstSP.LOING_ID_KEY, etId.getText().toString());
					Shared.set(LoginActivity.this, ConstSP.LOING_PW_KEY, etPassward.getText().toString());
					Shared.set(LoginActivity.this, ConstSP.LOING_PASS_KEY, true);
					
					DataSync.getInstance(LoginActivity.this).setOnSyncResult(LoginActivity.this);
					DataSync.getInstance(LoginActivity.this).startSync(true);
					
				} else if (resultLogin == ConstApiResult.LOGIN_RESULT_FAIL_APPROVE) {
					ToastUtil.show(LoginActivity.this, "관리자 승인이 필요합니다.");
				} else if (resultLogin == ConstApiResult.LOGIN_RESULT_FAIL_PASSWORD) {
					ToastUtil.show(LoginActivity.this, "비밀번호가 일치하지 않습니다.");
				} else if (resultLogin == ConstApiResult.LOGIN_RESULT_FAIL_ID) {
					ToastUtil.show(LoginActivity.this, "아이디가 존재하지 않습니다.");
				} else {
					ToastUtil.show(LoginActivity.this, "로그인 실패하였습니다.");
				}
			}
			
			@Override
			public void onCancel() {
			}
			
		});
		
	}
	
	
	@Override
	public void onSyncCompleted(int callType, boolean isComplete) {
		Logg.d(this, "onSyncCompleted : " + isComplete );
		
		if (isComplete) {
			Shared.set(LoginActivity.this, ConstSP.FIRST_START_APP, false);
			startMain();
		}
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		Logg.d("requestCode : " + requestCode);
		
		if (requestCode == AnsemUtil.REQUEST_ANSEM_CODE) {
			if (AnsemUtil.isConnected(this))
				startLogin(etId.getText().toString(), etPassward.getText().toString());
		}
	}

	public void startMain() {
		LocManager.getInstance(this).startGetLocation(listener);
		
		AppUtil.startActivity(LoginActivity.this, new Intent(LoginActivity.this, MainActivity.class));
		finish();
	}

	LocationListener listener = new LocationListener() {
		public void onStatusChanged(String provider, int status, Bundle extras) {
			switch (status) {
			case LocationProvider.OUT_OF_SERVICE:
				break;
			case LocationProvider.TEMPORARILY_UNAVAILABLE:
				break;
			case LocationProvider.AVAILABLE:
				break;
				
			}
		}
		
		public void onProviderEnabled(String provider) {
		}
		
		public void onProviderDisabled(String provider) {
		}
		
		@Override
		public void onLocationChanged(Location location) {
		
			String loginId = Shared.getString(LoginActivity.this, ConstSP.LOING_ID_KEY);
            if (loginId != null && loginId.length() > 0)
            	insertLocation(loginId, location.getLatitude()+"", location.getLongitude()+"");
		}
	};
	
	public void insertLocation(final String loginId, final String latitude, final String longitude) {
		try {
			
			final String device_token = Shared.getString(LoginActivity.this, ConstSP.GCM_REGID_KEY);
			
			ATask.executeVoid(new ATask.OnTask() {

				@Override
				public void onPre() {
					
				}

				@Override
				public void onBG() {
					ApiManager.saveTracking(loginId, latitude, longitude, device_token);
				}

				@Override
				public void onPost() {
					
				}
			});
		} catch (Exception e) {
			
		}
	}
}
