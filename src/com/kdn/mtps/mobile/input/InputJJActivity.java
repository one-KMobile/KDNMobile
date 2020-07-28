package com.kdn.mtps.mobile.input;

import java.util.Calendar;
import java.util.GregorianCalendar;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
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
import com.kdn.mtps.mobile.db.InputJJDao;
import com.kdn.mtps.mobile.db.InspectResultMasterDao;
import com.kdn.mtps.mobile.info.CodeInfo;
import com.kdn.mtps.mobile.inspect.InspectInfo;
import com.kdn.mtps.mobile.util.AppUtil;
import com.kdn.mtps.mobile.util.LocManager;
import com.kdn.mtps.mobile.util.Logg;
import com.kdn.mtps.mobile.util.Shared;
import com.kdn.mtps.mobile.util.StringUtil;
import com.kdn.mtps.mobile.util.ToastUtil;
import com.kdn.mtps.mobile.util.UIUtil;

public class InputJJActivity extends BaseActivity implements TitleManager, OnClickListener {
	LinearLayout llParent;
	ScrollView svParent;
	
	TextView tvName;
	TextView tvDate;
	TextView tvEqpNm;
	TextView tvType;

	String strWeather;
	Button btnCheckResult;
	
	EditText editRemarks;
	EditText editCount_1;
	EditText editCount_2;
	EditText editTerminal1_1;
	EditText editTerminal1_2;
	EditText editTerminal1_3;
	EditText editTerminal1_5;
	EditText editTerminal1_10;
	EditText editTerminal2_1;
	EditText editTerminal2_2;
	EditText editTerminal2_3;
	EditText editTerminal2_5;
	EditText editTerminal2_10;
	
	Button btnAdd;
	Button btnEdit;
	Button btnDelete;
	Button btnCamera;
	
	ImageView ivComplte;
	
	JJInfo selectJJInfo;
	
	InspectInfo mInfo;
	
	int selectedWeatherNo = 0;
	int selectedGroundNo = 0;
	int selectedCheckResultNo = 0;
	
	final String strWeatherItems[] = CodeInfo.getInstance(this).getNames(ConstVALUE.CODE_TYPE_WEATHER);
	final String strGroundItems[] = {"임야"};
	final String strCheckResultItems[] = CodeInfo.getInstance(this).getNames(ConstVALUE.CODE_TYPE_GOOD_SECD);
	
	boolean isAdd;
	String strCurrentDate;
	LocationManager locationManager;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_input_jj);
		
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
		
		btnCheckResult = (Button)findViewById(R.id.btnCheckResult);
		btnCheckResult.setOnClickListener(this);

		editRemarks = (EditText)findViewById(R.id.editRemarks);
		
		editCount_1 = (EditText)findViewById(R.id.editCount_1);
		editCount_2 = (EditText)findViewById(R.id.editCount_2);
		editTerminal1_1 = (EditText)findViewById(R.id.editTerminal1_1);
		editTerminal1_2 = (EditText)findViewById(R.id.editTerminal1_2);
		editTerminal1_3 = (EditText)findViewById(R.id.editTerminal1_3);
		editTerminal1_5 = (EditText)findViewById(R.id.editTerminal1_5);
		editTerminal1_10 = (EditText)findViewById(R.id.editTerminal1_10);
		editTerminal2_1 = (EditText)findViewById(R.id.editTerminal2_1);
		editTerminal2_2 = (EditText)findViewById(R.id.editTerminal2_2);
		editTerminal2_3 = (EditText)findViewById(R.id.editTerminal2_3);
		editTerminal2_5 = (EditText)findViewById(R.id.editTerminal2_5);
		editTerminal2_10 = (EditText)findViewById(R.id.editTerminal2_10);
		
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
		
		if (isAdd) {
			ivComplte.setBackgroundResource(R.drawable.input_jj_add);
			
			strWeather = Shared.getString(this, ConstSP.SETTING_WEATHER);
			btnCheckResult.setText(strCheckResultItems[0]);
		} else {
			ivComplte.setBackgroundResource(R.drawable.input_jj_edit);
			
			InputJJDao inputJJDao = InputJJDao.getInstance(this);
			selectJJInfo = inputJJDao.selectJJ(mInfo.master_idx);
			
			strWeather = CodeInfo.getInstance(this).getValue(ConstVALUE.CODE_TYPE_WEATHER, selectJJInfo.weather);
			selectedWeatherNo = StringUtil.getIndex(strWeatherItems, strWeather);
			
			selectedGroundNo = StringUtil.getIndex(strGroundItems, selectJJInfo.ground);
			
			
			String strCheckResult = CodeInfo.getInstance(this).getValue(ConstVALUE.CODE_TYPE_GOOD_SECD, selectJJInfo.check_result);
			btnCheckResult.setText(strCheckResult);
			selectedCheckResultNo = StringUtil.getIndex(strCheckResultItems, strCheckResult);
			

			editRemarks.setText(selectJJInfo.remarks);
			
			editCount_1.setText(selectJJInfo.count_1);
			editTerminal1_1.setText(selectJJInfo.terminal1_1);
			editTerminal1_2.setText(selectJJInfo.terminal1_2);
			editTerminal1_3.setText(selectJJInfo.terminal1_3);
			editTerminal1_5.setText(selectJJInfo.terminal1_5);
			editTerminal1_10.setText(selectJJInfo.terminal1_10);
		}
		
//		if (mInfo.send_yn_jj != null && !"".equals(mInfo.send_yn_jj)) {
//			ivComplte.setBackgroundResource(R.drawable.input_jj_send);
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
				AppUtil.startActivity(InputJJActivity.this, new Intent(InputJJActivity.this, MainActivity.class));
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
			InputJJDao inputJJDao = InputJJDao.getInstance(this);
			inputJJDao.Delete(mInfo.master_idx);
			InspectResultMasterDao insRetDao = InspectResultMasterDao.getInstance(this);
			insRetDao.updateComplete(mInfo.master_idx, "N", ConstVALUE.CODE_NO_INSPECT_JJ);
			setResult();
			finish();
			break;
		case R.id.btnCheckResult:
			showCheckResultDialog();
			break;
		case R.id.btnCamera:
			Intent intent = new Intent(InputJJActivity.this, CameraManageActivity.class);
			intent.putExtra("inspect", mInfo);
			startActivity(intent);
			break;
		default:
			break;
		}
	}

	
	public void showCheckResultDialog() {
		
		AlertDialog.Builder dialog = new AlertDialog.Builder(this, AlertDialog.THEME_DEVICE_DEFAULT_LIGHT);
		dialog.setTitle(getString(R.string.input_jj_check_result));
		dialog.setSingleChoiceItems(strCheckResultItems, selectedCheckResultNo, new android.content.DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				selectedCheckResultNo = which;
                btnCheckResult.setText(strCheckResultItems[which]);
                dialog.dismiss();
			}
		}).show();
	}

	public void setInputType() {
		InputJJDao inputJJDao = InputJJDao.getInstance(this);
		if (inputJJDao.existJJ(mInfo.master_idx))
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
		String strCheckResult = btnCheckResult.getText().toString();
		String strRemarks = editRemarks.getText().toString();
		
		String strCount_1 = editCount_1.getText().toString();
		String strCount_2 = editCount_2.getText().toString();
		String strTerminal1_1 = editTerminal1_1.getText().toString();
		String strTerminal1_2 = editTerminal1_2.getText().toString();
		String strTerminal1_3 = editTerminal1_3.getText().toString();
		String strTerminal1_5 = editTerminal1_5.getText().toString();
		String strTerminal1_10 = editTerminal1_10.getText().toString();
		String strTerminal2_1 = editTerminal2_1.getText().toString();
		String strTerminal2_2 = editTerminal2_2.getText().toString();
		String strTerminal2_3 = editTerminal2_3.getText().toString();
		String strTerminal2_5 = editTerminal2_5.getText().toString();
		String strTerminal2_10 = editTerminal2_10.getText().toString();
		
		if ("".equals(strCount_1) && "".equals(strTerminal1_1) && "".equals(strTerminal1_2) && "".equals(strTerminal1_3) && "".equals(strTerminal1_5) && "".equals(strTerminal1_10)) {
			ToastUtil.show(this, "정보를 입력해 주시기 바랍니다.");
			return;
		}
		
		InputJJDao inputJJDao = InputJJDao.getInstance(this);
		JJInfo log = new JJInfo();
		log.master_idx = mInfo.master_idx;
		log.weather = CodeInfo.getInstance(this).getKey(ConstVALUE.CODE_TYPE_WEATHER, strWeather);
		log.check_result = CodeInfo.getInstance(this).getKey(ConstVALUE.CODE_TYPE_GOOD_SECD, strCheckResult);;
		log.remarks = strRemarks;
		log.count_1 = strCount_1;
		log.terminal1_1 = strTerminal1_1;
		log.terminal1_2 = strTerminal1_2;
		log.terminal1_3 = strTerminal1_3;
		log.terminal1_5 = strTerminal1_5;
		log.terminal1_10 = strTerminal1_10;
		
		log.count_1 = log.count_1.replace("=", "");
		log.terminal1_1 = log.terminal1_1.replace("=", "");
		log.terminal1_2 = log.terminal1_2.replace("=", "");
		log.terminal1_3 = log.terminal1_3.replace("=", "");
		log.terminal1_5 = log.terminal1_5.replace("=", "");
		log.terminal1_10 = log.terminal1_10.replace("=", "");
		
		if (isEdit)
			inputJJDao.Append(log, selectJJInfo.idx);
		else {
			LocManager.getInstance(this).startGetLocation(listener);
			inputJJDao.Append(log);
		}
		
		setResult(RESULT_OK);
		
		InspectResultMasterDao insRetDao = InspectResultMasterDao.getInstance(this);
		insRetDao.updateComplete(mInfo.master_idx, "Y", ConstVALUE.CODE_NO_INSPECT_JJ);
		
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
			
			LocManager.getInstance(InputJJActivity.this).setGetGps(true);
			
			String currentLocation = String.format("위도:%f  경도:%f  고도:%f", location.getLatitude(), location.getLongitude(), location.getAltitude());
			Logg.d(InputJJActivity.this, "currentLocation : " + currentLocation);
            Calendar cal = new GregorianCalendar();
            String now = String.format("%d년 %d월 %d일 %d시 %d분 %d초", cal.get(Calendar.YEAR), cal.get(Calendar.MONTH)+1, 
					cal.get(Calendar.DAY_OF_MONTH), cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE),
					cal.get(Calendar.SECOND));
            Logg.d(InputJJActivity.this, "갱신 시간 : " + now);
            
            InspectResultMasterDao insRetDao = InspectResultMasterDao.getInstance(InputJJActivity.this);
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
}

