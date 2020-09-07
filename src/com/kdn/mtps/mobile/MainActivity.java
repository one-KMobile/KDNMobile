package com.kdn.mtps.mobile;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.*;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.kdn.mtps.mobile.constant.ConstSP;
import com.kdn.mtps.mobile.constant.ConstVALUE;
import com.kdn.mtps.mobile.datasync.DataSyncActivity;
import com.kdn.mtps.mobile.db.InspectResultMasterDao;
import com.kdn.mtps.mobile.facility.FacilitySearchActivity;
import com.kdn.mtps.mobile.input.InputYBActivity;
import com.kdn.mtps.mobile.inspect.InspectResultOutputActivity;
import com.kdn.mtps.mobile.inspect.InspectResultSearchActivity;
import com.kdn.mtps.mobile.net.api.ApiManager;
import com.kdn.mtps.mobile.net.api.bean.LoginData;
import com.kdn.mtps.mobile.nfc.NfcActivity;
import com.kdn.mtps.mobile.notice.NoticeListActivity;
import com.kdn.mtps.mobile.setting.SettingActivity;
import com.kdn.mtps.mobile.util.*;
import com.kdn.mtps.mobile.util.thread.ATask;

public class MainActivity extends BaseActivity implements OnClickListener {

	ImageView ivLogo;
	TextView tvWelcome;
	Button btnNotice;
	Button btnSetting;
	Button btnFacility;
	Button btnInspectSearch;
	Button btnDataSync;
	Button btnNfc;
	Button btnInspectRegist;
	boolean mIsFinish = false;
	Context mContext;
	LoginData loginData;
	
	Handler mHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			Logg.d(mContext, "mIsFinish = false");
			mIsFinish = false;
		}
		
	};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		setInit();
		
		setData();
		
//		TelephonyManager m_telephonyManager    =  (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
//		WifiManager wfmanager     = (WifiManager)this.getSystemService(Context.WIFI_SERVICE);
//        WifiInfo info    = wfmanager.getConnectionInfo();
//        Logg.d("mac :"+ info.getMacAddress());
		
		// 1주일 지난 순시결과 파일 삭제
		InspectResultMasterDao.getInstance(this).deleteOldData();
		
		String tgtDir = ConstVALUE.PATH_SDCARD_KDN_ROOT;
		
		File dir = new File(tgtDir);
		File files[] = dir.listFiles();

		long currentTime = System.currentTimeMillis();
		
		// 1주일 지난 엑셀 파일 삭제
		for (File file : files) {
			String fileName = file.getName();
			
			String outputData = "output_data_";
			
			if (fileName.contains(outputData)) {
				String fileName2[] = fileName.split("\\.");
				if (fileName2.length == 2) {
					
					String fileName3[] = fileName2[0].split(outputData);
					
					if (fileName3.length == 2) {
						String fileDate = fileName3[1];
						
						SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmm"); 
						Date date;
						try {
							date = simpleDateFormat.parse(fileDate);
							
							long targetTime = date.getTime();
							
							long diffTime = currentTime - targetTime; 
							long delTime = 1000*60*60*24 * ConstVALUE.DELETE_DAY;
							
							if (diffTime > delTime) {
								File delFile = new File(fileName);
								if (file != null && file.exists()) {
									Logg.d("file delete : " + fileName);
									file.delete();
								}
							}
						} catch (ParseException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} 
						
					}
				}
				
			}
		}
		
	}

	
	@Override
	protected void onResume() {
		super.onResume();
		
		String syncDate = Shared.getString(this, ConstSP.SYNC_DATE);
//		tvSyncDate.setText("마지막 정보 갱신시각 : " + syncDate);
		String userName = Shared.getString(this, ConstSP.USER_NAME);
		
		tvWelcome.setText("안녕하세요. " + userName + " 님 서비스이용 감사합니다.");
		
		setData();
	
	}
	
	public void setInit() {
		mContext = this;
		ivLogo = (ImageView)findViewById(R.id.ivLogo);
		tvWelcome = (TextView)findViewById(R.id.tvWelcome);
		btnSetting = (Button)findViewById(R.id.btnSetting);
		btnSetting.setOnClickListener(this);
		//btnNotice = (Button)findViewById(R.id.btnSettin);
		//btnNotice.setOnClickListener(this);
		btnFacility = (Button)findViewById(R.id.btnFacility);
		btnFacility.setOnClickListener(this);
		btnInspectSearch = (Button)findViewById(R.id.btnInspectSearch);
		btnInspectSearch.setOnClickListener(this);
		btnDataSync = (Button)findViewById(R.id.btnDataSync);
		btnDataSync.setOnClickListener(this);
		//btnNfc = (Button)findViewById(R.id.btnNfc);
		//btnNfc.setOnClickListener(this);
		btnInspectRegist = (Button)findViewById(R.id.btnInspectRegist);
		btnInspectRegist.setOnClickListener(this);
	}
	
	public void setData() {
		
		String strCompany = Shared.getString(this, ConstSP.SETTING_COMPANY);
		
		// 설정-업체설정에서 설정한 업체이미지가 표시된다.
		if (strCompany != null) {
			if ("한전KPS".equals(strCompany))
				ivLogo.setBackgroundResource(R.drawable.main_logo_kps);
			else if ("한전KDN".equals(strCompany))
				ivLogo.setBackgroundResource(R.drawable.main_logo_kdn);
			else if ("KEPCO".equals(strCompany))
				ivLogo.setBackgroundResource(R.drawable.main_logo_kepco);
		}
			
//		if (exist)
//			ivMainNew.setVisibility(View.VISIBLE);
//		else
//			ivMainNew.setVisibility(View.GONE);
//		int noticeCount = GData.getNoticeList().size();
//		btnNotice.setHint("공지사항 및\n알림("+ noticeCount + ")");
	}
	
	public static final int MENU_SETTING = Menu.FIRST;
	public static final int MENU_LOGOUT = Menu.FIRST + 1;
	public static final int MENU_INPUT_YB = Menu.FIRST + 2;

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		
		super.onCreateOptionsMenu(menu);
		
		menu.add(Menu.NONE, MENU_SETTING, Menu.NONE, "설정");
		menu.add(Menu.NONE, MENU_INPUT_YB, Menu.NONE, "예방순시");
	    menu.add(Menu.NONE, MENU_LOGOUT, Menu.NONE, "로그아웃");
	    

		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch(item.getItemId())
        {
            case MENU_SETTING:
            	AppUtil.startActivity(MainActivity.this, new Intent(MainActivity.this, SettingActivity.class));
            	break;
            case MENU_LOGOUT:
            	startLogout();
            	break;
            case MENU_INPUT_YB:
            	AppUtil.startActivity(MainActivity.this, new Intent(MainActivity.this, InputYBActivity.class));
            	break;
        default:
            return super.onOptionsItemSelected(item);
        }

		return true;
	}


	@Override
	public void onBackPressed() {
		Logg.d(this, "onBackPressed mIsFinish :" + mIsFinish);
		if (mIsFinish) {
			finish();
			android.os.Process.killProcess(android.os.Process.myPid());
		}
		else {
			Toast.makeText(this, R.string.app_exit, Toast.LENGTH_SHORT).show();
			mIsFinish = true;
			mHandler.sendEmptyMessageDelayed(0, 3000);
		}
	}

	@Override
	public void onClick(View v) {
		Intent intent = null;
		switch (v.getId()) {
			case R.id.btnSetting:
				AppUtil.startActivity(MainActivity.this, new Intent(MainActivity.this, SettingActivity.class));
				break;
			case R.id.btnFacility:
				AppUtil.startActivity(MainActivity.this, new Intent(MainActivity.this, FacilitySearchActivity.class));
				break;
			case R.id.btnInspectSearch:
				intent = new Intent(MainActivity.this, InspectResultSearchActivity.class);
				AppUtil.startActivity(MainActivity.this, intent);
				break;
			case R.id.btnInspectRegist:
				intent = new Intent(MainActivity.this, InspectResultOutputActivity.class);
				AppUtil.startActivity(MainActivity.this, intent);
				break;
			case R.id.btnDataSync:
				AppUtil.startActivity(MainActivity.this, new Intent(MainActivity.this, DataSyncActivity.class));
				break;
			//case R.id.btnNfc:
			//	AppUtil.startActivity(MainActivity.this, new Intent(MainActivity.this, NfcActivity.class));
			//	break;
		}
		
	}
	
	public void startLogout() {
		
		ATask.executeVoidProgress(this, R.string.logoutting, true, new ATask.OnTaskProgress() {
			public void onPre() {
			}

			public void onBG() {
				loginData = ApiManager.logout();
			}

			@Override
			public void onPost() {
				if (loginData != null && ApiManager.RESULT_OK.equals(loginData.code)) {
					AppUtil.startActivity(MainActivity.this, new Intent(MainActivity.this, LoginActivity.class));
					finish();
				}
			}
			
			@Override
			public void onCancel() {
			}
			
		});
		
	}
	
}
