package com.kdn.mtps.mobile.input;


import java.util.Calendar;
import java.util.GregorianCalendar;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Bundle;
import android.util.*;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.kdn.mtps.mobile.BaseActivity;
import com.kdn.mtps.mobile.MainActivity;
import com.kdn.mtps.mobile.R;
import com.kdn.mtps.mobile.TitleManager;
import com.kdn.mtps.mobile.camera.CameraManageActivity;
import com.kdn.mtps.mobile.constant.ConstSP;
import com.kdn.mtps.mobile.constant.ConstVALUE;
import com.kdn.mtps.mobile.db.InputBTDao;
import com.kdn.mtps.mobile.db.InspectResultMasterDao;
import com.kdn.mtps.mobile.db.bean.InputBTLog;
import com.kdn.mtps.mobile.info.CodeInfo;
import com.kdn.mtps.mobile.inspect.InspectInfo;
import com.kdn.mtps.mobile.util.AppUtil;
import com.kdn.mtps.mobile.util.LocManager;
import com.kdn.mtps.mobile.util.Logg;
import com.kdn.mtps.mobile.util.Shared;
import com.kdn.mtps.mobile.util.StringUtil;
import com.kdn.mtps.mobile.util.UIUtil;

public class InputBTActivity extends BaseActivity implements TitleManager, OnClickListener {
	
	LinearLayout llParent;
	ScrollView svParent;
	TextView tvName;
	TextView tvDate;
	TextView tvEqpNm;
	TextView tvType;

	String strWeather;
	Button btnWorkerCnt;
	Button btnClaimContent;
	EditText editCheckResult;
	EditText editEtc;
	
	Button btnAdd;
	Button btnEdit;
	Button btnDelete;
	Button btnCamera;
	
	ImageView ivComplte;
	
	BTInfo selectBtInfo;
	
//	ListView listBT;
//	InputBTAdapter adapter;
//	ArrayList<BTInfo> BtList;
	
	LocationManager locationManager;
	
	InspectInfo mInfo;
	
	int selectedWeatherNo = 0;
	int selectedWorkerCntNo = 0;
	int selectedClaimContentNo = 0;
	
//	final String strWeatherItems[] = {"맑음", "비", "흐림", "눈"};
	final String strWeatherItems[] = CodeInfo.getInstance(this).getNames(ConstVALUE.CODE_TYPE_WEATHER);
	final String strWorkerCntItems[] = {"0명","1명","2명","3명","4명","5명"};
	final String strClainContentItems[] = CodeInfo.getInstance(this).getNames(ConstVALUE.CODE_TYPE_GOOD_SECD);
	
	boolean isAdd;
	String strCurrentDate;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_input_bt);
		
		mInfo = getIntent().getParcelableExtra("inspect");
		strCurrentDate = getIntent().getStringExtra("currentDate");
		
		setInit();
		
		setInputType();
		
	}

	public void setInit() {
		llParent = (LinearLayout)findViewById(R.id.llParent);
		llParent.setOnTouchListener(hideKeyboardListener);
		
		svParent = (ScrollView)findViewById(R.id.svParent);
		svParent.setOnTouchListener(hideKeyboardListener);
		
		tvName = (TextView)findViewById(R.id.tvName);
		tvDate = (TextView)	findViewById(R.id.tvDate);
		tvEqpNm = (TextView)	findViewById(R.id.tvEqpNm);
		tvType = (TextView)findViewById(R.id.tvType);
		
		btnWorkerCnt = (Button)findViewById(R.id.btnWorkerCnt);
		btnClaimContent = (Button)findViewById(R.id.btnClaimContent);
		editCheckResult = (EditText)findViewById(R.id.editCheckResult);
		editEtc = (EditText)findViewById(R.id.editEtc);
		
		editCheckResult.setOnKeyListener(new OnKeyListener() {
			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				if (((EditText)v).getLineCount() == 4 && keyCode == event.KEYCODE_ENTER)
					return true;
				return false;
			}
		});
		
		editEtc.setOnKeyListener(new OnKeyListener() {
			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				if (((EditText)v).getLineCount() == 4 && keyCode == event.KEYCODE_ENTER)
					return true;
				return false;
			}
		});
		
		btnWorkerCnt.setOnClickListener(this);
		btnClaimContent.setOnClickListener(this);
		
		btnAdd = (Button)findViewById(R.id.btnAdd);
		btnAdd.setOnClickListener(this);
		btnEdit = (Button)findViewById(R.id.btnEdit);
		btnDelete = (Button)findViewById(R.id.btnDelete);
		btnEdit.setOnClickListener(this);
		btnDelete.setOnClickListener(this);
		btnCamera = (Button)findViewById(R.id.btnCamera);
		btnCamera.setOnClickListener(this);
		
		ivComplte = (ImageView)findViewById(R.id.ivComplte);
	}
	
	public void setData() {
		
		tvName.setText(mInfo.tracksName);
		tvDate.setText(StringUtil.printDate(mInfo.date));
		tvEqpNm.setText(mInfo.eqpNm);
		
		String insType = CodeInfo.getInstance(this).getValue(ConstVALUE.CODE_TYPE_INS_TYPE, mInfo.type);
		tvType.setText(insType);
		
		InputBTDao inputBTDao = InputBTDao.getInstance(this);
		
		if (isAdd) {
			ivComplte.setBackgroundResource(R.drawable.input_bt_add);
			
			strWeather = Shared.getString(this, ConstSP.SETTING_WEATHER);
			
			btnWorkerCnt.setText(strWorkerCntItems[0]);
			btnClaimContent.setText(strClainContentItems[0]);
			editEtc.setText("");
		} else {
			ivComplte.setBackgroundResource(R.drawable.input_bt_edit);
			
			selectBtInfo = inputBTDao.selectBT(mInfo.master_idx);
			
			strWeather = CodeInfo.getInstance(this).getValue(ConstVALUE.CODE_TYPE_WEATHER, selectBtInfo.weather);
			
			btnWorkerCnt.setText(selectBtInfo.worker_cnt);
			selectedWorkerCntNo = StringUtil.getIndex(strWorkerCntItems, selectBtInfo.worker_cnt);
			
			String strClaimContent = CodeInfo.getInstance(this).getValue(ConstVALUE.CODE_TYPE_GOOD_SECD, selectBtInfo.claim_content);
			btnClaimContent.setText(strClaimContent);
			selectedClaimContentNo = StringUtil.getIndex(strClainContentItems, strClaimContent);
			
			editCheckResult.setText(selectBtInfo.check_result);
			
			editEtc.setText(selectBtInfo.etc);
		}
		
//		if (mInfo.send_yn_bt != null && !"".equals(mInfo.send_yn_bt)) {
//			ivComplte.setBackgroundResource(R.drawable.input_bt_send);
//		}
	}
	
	
	@Override
	protected void onResume() {
		super.onResume();
		
	}

	@Override
	public void setTitle() {
		Button btnHeaderTitle = (Button)findViewById(R.id.btnHeaderTitle);
		Button btnHeaderLeft = (Button)findViewById(R.id.btnHeaderLeft);
		Button btnHeaderRight = (Button)findViewById(R.id.btnHeaderRight);
		
		String insType = CodeInfo.getInstance(this).getValue(ConstVALUE.CODE_TYPE_INS_TYPE, mInfo.type);
		
		if (isAdd)
			btnHeaderTitle.setText(insType + " 등록");
		else
			btnHeaderTitle.setText(insType + " 수정/삭제");
		
		btnHeaderLeft.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		
		btnHeaderRight.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				AppUtil.startActivity(InputBTActivity.this, new Intent(InputBTActivity.this, MainActivity.class));
			}
		});
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btnAdd:
			update(false);
			break;
		case R.id.btnEdit:
			update(true);
			break;
		case R.id.btnDelete:
			InputBTDao inputBTDao = InputBTDao.getInstance(this);
			inputBTDao.Delete(mInfo.master_idx);
			InspectResultMasterDao insRetDao = InspectResultMasterDao.getInstance(this);
			insRetDao.updateComplete(mInfo.master_idx, "N", ConstVALUE.CODE_NO_INSPECT_BT);
			setResult();
			finish();
			break;
		case R.id.btnWorkerCnt:
			showWorkerCntDialog();
			break;
		case R.id.btnClaimContent:
			showClaimContentDialog();
			break;
		case R.id.btnCamera:
			Intent intent = new Intent(InputBTActivity.this, CameraManageActivity.class);
			intent.putExtra("inspect", mInfo);
			startActivity(intent);
			break;
		default:
			break;
		}
	}

	public void showWorkerCntDialog() {
		AlertDialog.Builder dialog = new AlertDialog.Builder(this, AlertDialog.THEME_DEVICE_DEFAULT_LIGHT);
		dialog.setTitle(getString(R.string.input_bt_worker_cnt));
		dialog.setSingleChoiceItems(strWorkerCntItems, selectedWorkerCntNo, new android.content.DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				selectedWorkerCntNo = which;
                btnWorkerCnt.setText(strWorkerCntItems[which]);
                dialog.dismiss();
			}
		}).show();
	}
	
	public void showClaimContentDialog() {
		
		AlertDialog.Builder dialog = new AlertDialog.Builder(this, AlertDialog.THEME_DEVICE_DEFAULT_LIGHT);
		dialog.setTitle(getString(R.string.input_bt_claim_content));
		dialog.setSingleChoiceItems(strClainContentItems, selectedClaimContentNo, new android.content.DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				selectedClaimContentNo = which;
                btnClaimContent.setText(strClainContentItems[which]);
                dialog.dismiss();
			}
		}).show();
	}
	
	public void setInputType() {
		InputBTDao inputBTDao = InputBTDao.getInstance(this);
		if (inputBTDao.existBT(mInfo.master_idx))
			isAdd = false;
		else
			isAdd = true;
		
		if (isAdd) {
			btnAdd.setVisibility(View.VISIBLE);
			btnEdit.setVisibility(View.GONE);
			btnDelete.setVisibility(View.GONE);
		} else {
			btnAdd.setVisibility(View.GONE);
			btnEdit.setVisibility(View.VISIBLE);
			btnDelete.setVisibility(View.VISIBLE);
		}
		
		setTitle();
		
		setData();
	}
	
	public void update(boolean isEdit) {
		Log.i("MyTag","############### test2 update");
		String strWorkerCnt = btnWorkerCnt.getText().toString();
		strWorkerCnt = strWorkerCnt.replace("명","");
		String strClaimContent = btnClaimContent.getText().toString();
		String strCheckResult = editCheckResult.getText().toString();
		String strEtc = editEtc.getText().toString();
		
		
		InputBTLog log = new InputBTLog();
		log.master_idx = mInfo.master_idx;
		log.weather = CodeInfo.getInstance(this).getKey(ConstVALUE.CODE_TYPE_WEATHER, strWeather);
		log.worker_cnt = strWorkerCnt;
		log.claim_content = CodeInfo.getInstance(this).getKey(ConstVALUE.CODE_TYPE_GOOD_SECD, strClaimContent);
		log.check_result = strCheckResult;
		log.etc = strEtc;
		log.etc = log.etc.replace("=", "");
			
		InputBTDao inputBTDao = InputBTDao.getInstance(this);
		
		if (isEdit)
			inputBTDao.Append(log, selectBtInfo.idx);
		else {
			LocManager.getInstance(this).startGetLocation(listener);
			inputBTDao.Append(log);
		}

		setResult(RESULT_OK);
		
		InspectResultMasterDao insRetDao = InspectResultMasterDao.getInstance(this);
		insRetDao.updateComplete(mInfo.master_idx, "Y", ConstVALUE.CODE_NO_INSPECT_BT);
		
		finish();
	}

	public void setResult() {
		Intent intent = new Intent();
		intent.putExtra("currentDate", strCurrentDate);
		setResult(RESULT_OK, intent);
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
			
			LocManager.getInstance(InputBTActivity.this).setGetGps(true);
			
			String currentLocation = String.format("위도:%f  경도:%f  고도:%f", location.getLatitude(), location.getLongitude(), location.getAltitude());
			Logg.d(InputBTActivity.this, "currentLocation : " + currentLocation);
            Calendar cal = new GregorianCalendar();
            String now = String.format("%d년 %d월 %d일 %d시 %d분 %d초", cal.get(Calendar.YEAR), cal.get(Calendar.MONTH)+1, 
					cal.get(Calendar.DAY_OF_MONTH), cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE),
					cal.get(Calendar.SECOND));
            Logg.d(InputBTActivity.this, "갱신 시간 : " + now);
            
            InspectResultMasterDao insRetDao = InspectResultMasterDao.getInstance(InputBTActivity.this);
            insRetDao.updateNfcTagLocation(mInfo.master_idx, "", location.getLatitude()+"", location.getLongitude()+"", false);
            
            if (locationManager != null) {
            	locationManager.removeUpdates(listener);
            }
		}
	};

	OnTouchListener hideKeyboardListener = new OnTouchListener() {
		@Override
		public boolean onTouch(View v, MotionEvent event) {
			UIUtil.hideKeyboard(llParent);
			return false;
		}
	};
	
	@Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
 
        if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) 
        {
        	Logg.d("ORIENTATION_PORTRAIT");
//        	editCheckResult.setImeOptions(EditorInfo.IME_ACTION_NONE);
        } 
        else if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) 
        {
        	Logg.d("ORIENTATION_LANDSCAPE");
        }
    }

}
