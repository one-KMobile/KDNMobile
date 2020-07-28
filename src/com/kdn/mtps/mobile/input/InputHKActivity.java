package com.kdn.mtps.mobile.input;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.LinkedHashMap;
import java.util.Map.Entry;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ScrollView;
import android.widget.TextView;

import com.kdn.mtps.mobile.BaseActivity;
import com.kdn.mtps.mobile.MainActivity;
import com.kdn.mtps.mobile.R;
import com.kdn.mtps.mobile.TitleManager;
import com.kdn.mtps.mobile.camera.CameraManageActivity;
import com.kdn.mtps.mobile.constant.ConstSP;
import com.kdn.mtps.mobile.constant.ConstVALUE;
import com.kdn.mtps.mobile.db.InputHKDao;
import com.kdn.mtps.mobile.db.InputHKSubInfoDao;
import com.kdn.mtps.mobile.db.InspectResultMasterDao;
import com.kdn.mtps.mobile.info.CodeInfo;
import com.kdn.mtps.mobile.inspect.InspectInfo;
import com.kdn.mtps.mobile.util.AppUtil;
import com.kdn.mtps.mobile.util.LocManager;
import com.kdn.mtps.mobile.util.Logg;
import com.kdn.mtps.mobile.util.Shared;
import com.kdn.mtps.mobile.util.StringUtil;
import com.kdn.mtps.mobile.util.UIUtil;

public class InputHKActivity extends BaseActivity implements TitleManager, OnClickListener {
	LinearLayout llParent;
	ScrollView svParent;
	
	TextView tvName;
	TextView tvDate;
	TextView tvEqpNm;
	TextView tvType;

	Button btnAdd;
	Button btnEdit;
	Button btnDelete;
	Button btnCamera;
	
	ImageView ivComplte;
	
	LinearLayout llLayout;
	
	String strWeather;
	
//	ListView listBT;
//	InputBTAdapter adapter;
//	ArrayList<BTInfo> BtList;
	
	InspectInfo mInfo;
	
	ArrayList<HKInfo> hkInfoList;
	HKInfo selectHKInfo;
	boolean isAdd;
	boolean isStartDate;
	
	int selectedWeatherNo = 0;
	
	public LinkedHashMap<Integer, ViewHolder> hkList = new LinkedHashMap<Integer, ViewHolder>();
	
	final String strWeatherItems[] = CodeInfo.getInstance(this).getNames(ConstVALUE.CODE_TYPE_WEATHER);
	final String strLightTypeItems[] = CodeInfo.getInstance(this).getNames(ConstVALUE.CODE_TYPE_HK_TYPE);
	final String strLightCateItems[] = CodeInfo.getInstance(this).getNames(ConstVALUE.CODE_TYPE_HK_CATEGORY);
	final String strYBItems[] = CodeInfo.getInstance(this).getNames(ConstVALUE.CODE_TYPE_GOOD_SECD);
	final String strYBTwoResultItems[] = CodeInfo.getInstance(this).getYBNames();
	
	String strCurrentDate;
	LocationManager locationManager;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_input_hk);
		
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
		
		btnAdd = (Button)findViewById(R.id.btnAdd);
		btnAdd.setOnClickListener(this);
		btnEdit = (Button)findViewById(R.id.btnEdit);
		btnDelete = (Button)findViewById(R.id.btnDelete);
		btnEdit.setOnClickListener(this);
		btnDelete.setOnClickListener(this);
		btnCamera = (Button)findViewById(R.id.btnCamera);
		btnCamera.setOnClickListener(this);
		
		llLayout = (LinearLayout)findViewById(R.id.llLayout);
		
		ivComplte = (ImageView)findViewById(R.id.ivComplte);
	}
	
	public void setData() {
		
		tvName.setText(mInfo.tracksName);
		tvDate.setText(StringUtil.printDate(mInfo.date));
		tvEqpNm.setText(mInfo.eqpNm);
		
		String insType = CodeInfo.getInstance(this).getValue(ConstVALUE.CODE_TYPE_INS_TYPE, mInfo.type);
		tvType.setText(insType);
		
		InputHKSubInfoDao inputHKSubInfoDao = InputHKSubInfoDao.getInstance(this);
		ArrayList<HKSubInfo> list = inputHKSubInfoDao.selectList(mInfo.eqpNo);
		
		if (list.isEmpty()) {
			btnAdd.setEnabled(false);
		}
		
		if (isAdd) {
			ivComplte.setBackgroundResource(R.drawable.input_hk_add);
			
			strWeather = Shared.getString(this, ConstSP.SETTING_WEATHER);
			
			for (int i=0; i<list.size(); i++) {
				addItem(i, list.get(i));
			}
			
			addBottomPadding();
			
		} else {
			ivComplte.setBackgroundResource(R.drawable.input_hk_edit);
			
			InputHKDao inputHKDao = InputHKDao.getInstance(this);
			hkInfoList = inputHKDao.selectHK(mInfo.master_idx);
			
			selectHKInfo = hkInfoList.get(0);
			
			strWeather = CodeInfo.getInstance(this).getValue(ConstVALUE.CODE_TYPE_WEATHER, selectHKInfo.weather);
			selectedWeatherNo = StringUtil.getIndex(strWeatherItems, strWeather);
			
			for (int i=0; i<list.size(); i++) {
				addItem(i, hkInfoList.get(i));
			}
			
			addBottomPadding();
			
		}
		
//		if (mInfo.send_yn_hk != null && !"".equals(mInfo.send_yn_hk)) {
//			ivComplte.setBackgroundResource(R.drawable.input_hk_send);
//		}
	}
	
	public void addBottomPadding() {
		LinearLayout linearPadding = new LinearLayout(this);
		linearPadding.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, (int) UIUtil.pxFromDp(this, 50)));
		llLayout.addView(linearPadding);		
	}
	
	public void setTestData() {
		
//		BtList = new ArrayList<BTInfo>();
//		
//		BTInfo info = new BTInfo();
//		info.no = "1111";
//		info.result = "이상없음";
//		info.type = "지지물";
//		info.detail = "표시찰류";
//		info.result2 = "파손";
//		info.proceed = "현장조치";
//		info.etc = "특이사항없음";
//		
//		BtList.add(info);
//		adapter.setList(BtList);
		
//		String type = Shared.getString(InputBTActivity.this, "input_bt_type");
//		String detail = Shared.getString(InputBTActivity.this, "input_bt_detail");
//		String result = Shared.getString(InputBTActivity.this, "input_bt_result");
//		String proceed = Shared.getString(InputBTActivity.this, "input_bt_proceed");
//		String etc = Shared.getString(InputBTActivity.this, "input_bt_etc");
//		
//		if (!"".equals(type)) {
//			BtList = new ArrayList<BTInfo>();
//			
//			BTInfo info = new BTInfo();
//			info.no = "1111";
//			info.result = "이상없음";
//			info.type = type;
//			info.detail = detail;
//			info.result2 = result;
//			info.proceed = proceed;
//			info.etc = etc;
//			
//			BtList.add(info);
//			
//			adapter.setList(BtList);
//		}
		
		
//		InputBTDao inputBTDao = InputBTDao.getInstance(this);
//		BtList =  inputBTDao.selectBTList(mInfo.master_idx);
//		adapter.setList(BtList);
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
				AppUtil.startActivity(InputHKActivity.this, new Intent(InputHKActivity.this, MainActivity.class));
			}
		});
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btnAdd:
//			String strStartDate = btnStartDate.getText().toString();
//			String strEndDate = btnEndDate.getText().toString();
//			if ("".equals(strStartDate) || "".equals(strEndDate)) {
//				ToastUtil.show(this, "점검시간을 입력해 주세요.");
//				return;
//			}
						
			update(false);
//			Intent intent = new Intent(InputHKActivity.this, InputBTEditActivity.class);
//			intent.putExtra("inspect", mInfo);
//			intent.putExtra(ConstVALUE.ACTIVITY_INPUT_TYPE, ConstVALUE.ACTIVITY_INPUT_TYPE_ADD);
//			startActivity(intent);
			break;
		case R.id.btnEdit:
			update(true);
			break;
		case R.id.btnDelete:
			InputHKDao inputHKDao = InputHKDao.getInstance(this);
			inputHKDao.Delete(mInfo.master_idx);
			InspectResultMasterDao insRetDao = InspectResultMasterDao.getInstance(this);
			insRetDao.updateComplete(mInfo.master_idx, "N", ConstVALUE.CODE_NO_INSPECT_HK);
			setResult(RESULT_OK);
			finish();
			break;
//		case R.id.btnStartDate:
//			isStartDate = true;
//			
//			if (sHour == 0) {
//				Calendar c = Calendar.getInstance();
//				sHour = c.get(Calendar.HOUR_OF_DAY);
//				sMin = c.get(Calendar.MINUTE);
//			}
//			
//			TimePickerDialog timePick = new TimePickerDialog(this, mTimeListener, sHour, sMin, false);
//			timePick.show();
//			break;
//		case R.id.btnEndDate:
//			isStartDate = false;
//			
//			if (eHour == 0) {
//				Calendar c = Calendar.getInstance();
//				eHour = c.get(Calendar.HOUR_OF_DAY);
//				eMin = c.get(Calendar.MINUTE);
//			}
//			
//			TimePickerDialog timePick2 = new TimePickerDialog(this, mTimeListener, eHour, eMin, false);
//			timePick2.show();
//			break;
//		case R.id.btnType:
//			showLightTypeDialog();
//			break;
		case R.id.btnCamera:
			Intent intent = new Intent(InputHKActivity.this, CameraManageActivity.class);
			intent.putExtra("inspect", mInfo);
			startActivity(intent);
			break;
		default:
			break;
		}
	}

	public void update(boolean isEdit) {
		InputHKDao inputHKDao = InputHKDao.getInstance(this);
		inputHKDao.Delete(mInfo.master_idx);
		
		for (Entry<Integer, ViewHolder> entry : hkList.entrySet()) {
			ViewHolder viewHolder = entry.getValue();
			
			String strLightType = viewHolder.tvLightType.getText().toString();
			String strNo = viewHolder.tvNo.getText().toString();
			String strPower = viewHolder.tvPower.getText().toString();
			String strLightCnt = viewHolder.editLightCnt.getText().toString();
			String strLightState = viewHolder.btnLightState.getText().toString();
			String strYBResult = viewHolder.btnYBresult.getText().toString();
			
			String item1 = CodeInfo.getInstance(this).getKey(ConstVALUE.CODE_TYPE_GOOD_SECD, strYBTwoResultItems[0]);
			String item2  = CodeInfo.getInstance(this).getKey(ConstVALUE.CODE_TYPE_GOOD_SECD, strYBTwoResultItems[1]);
			int tagLightState = (Integer)viewHolder.btnLightState.getTag();
			
			HKInfo info = new HKInfo();
			info.masterIdx = mInfo.master_idx;
			info.weather = CodeInfo.getInstance(this).getKey(ConstVALUE.CODE_TYPE_WEATHER, strWeather);
			info.lightType = CodeInfo.getInstance(this).getKey(ConstVALUE.CODE_TYPE_FLIGHT_LMP, strLightType);;
			info.light_no = strNo;
			info.power = CodeInfo.getInstance(this).getKey(ConstVALUE.CODE_TYPE_HK_TYPE, strPower);
			info.light_cnt = strLightCnt;
			info.light_cnt = info.light_cnt.replace("=", "");
			info.light_state = (tagLightState == 1 ? item1 : item2);
			info.yb_result = CodeInfo.getInstance(this).getKey(ConstVALUE.CODE_TYPE_GOOD_SECD, strYBResult);
			
			if (isEdit)
				inputHKDao.Append(info, viewHolder.hkInfo.idx);
			else {
				inputHKDao.Append(info);
			}
		}
			
	
		if (!isEdit)
			LocManager.getInstance(this).startGetLocation(listener);
		
		InspectResultMasterDao insRetDao = InspectResultMasterDao.getInstance(this);
		insRetDao.updateComplete(mInfo.master_idx, "Y", ConstVALUE.CODE_NO_INSPECT_HK);
		
		setResult(RESULT_OK);
		finish();
	}
	
	public void setInputType() {
		InputHKDao inputHKDao = InputHKDao.getInstance(this);
		if (inputHKDao.existHK(mInfo.master_idx))
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
			
			LocManager.getInstance(InputHKActivity.this).setGetGps(true);
			
			String currentLocation = String.format("위도:%f  경도:%f  고도:%f", location.getLatitude(), location.getLongitude(), location.getAltitude());
			Logg.d(InputHKActivity.this, "currentLocation : " + currentLocation);
            Calendar cal = new GregorianCalendar();
            String now = String.format("%d년 %d월 %d일 %d시 %d분 %d초", cal.get(Calendar.YEAR), cal.get(Calendar.MONTH)+1, 
					cal.get(Calendar.DAY_OF_MONTH), cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE),
					cal.get(Calendar.SECOND));
            Logg.d(InputHKActivity.this, "갱신 시간 : " + now);
            
            InspectResultMasterDao insRetDao = InspectResultMasterDao.getInstance(InputHKActivity.this);
            insRetDao.updateNfcTagLocation(mInfo.master_idx, "", location.getLatitude()+"", location.getLongitude()+"", false);
            
            if (locationManager != null) {
            	locationManager.removeUpdates(listener);
            }
		}
	};
	
	public void addItem(int idx, HKSubInfo hkSubInfo) {
		HKInfo hkInfo = new HKInfo();
		hkInfo.lightType = hkSubInfo.FLIGHT_LMP_KND;
		hkInfo.power = hkSubInfo.SRCELCT_KND;
		hkInfo.light_no = hkSubInfo.FLIGHT_LMP_NO;
		hkInfo.light_state = CodeInfo.getInstance(this).getKey(ConstVALUE.CODE_TYPE_GOOD_SECD, strYBTwoResultItems[0]);
		hkInfo.yb_result = CodeInfo.getInstance(this).getKey(ConstVALUE.CODE_TYPE_GOOD_SECD, strYBItems[0]);
		
		addItem(idx, hkInfo);
	}
	
	public void addItem(int idx, HKInfo hkInfo) {
		
		final ViewHolder viewHolder = new ViewHolder();
		View view = LayoutInflater.from(this).inflate(R.layout.item_hk, null);
		LinearLayout llItemParent = (LinearLayout) view.findViewById(R.id.llItemParent);
		
		UIUtil.setFont(this, (ViewGroup)llItemParent);
		
		llItemParent.setOnTouchListener(hideKeyboardListener);
		viewHolder.tvLightType = (TextView) view.findViewById(R.id.tvLightType);
		viewHolder.tvNo = (TextView) view.findViewById(R.id.tvNo);
		viewHolder.tvPower = (TextView) view.findViewById(R.id.tvPower);
		viewHolder.editLightCnt = (EditText) view.findViewById(R.id.editLightCnt);
		viewHolder.btnLightState = (Button) view.findViewById(R.id.btnLightState);
		viewHolder.btnLightState.setBackgroundResource(R.drawable.btn_good);
		viewHolder.btnLightState.setOnClickListener(YBListener);
		viewHolder.btnYBresult = (Button) view.findViewById(R.id.btnYBresult);
        viewHolder.btnYBresult.setId(0);
        viewHolder.btnYBresult.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				showYBResultDialog((Button)v);
			}
		});
        
        viewHolder.btnLightState.setTag(1);
        viewHolder.btnYBresult.setText(strYBItems[0]);
		viewHolder.tvNo.setText(idx + "");
		
		if (hkInfo != null) {
			viewHolder.hkInfo = hkInfo;
			
			String strLightType = CodeInfo.getInstance(this).getValue(ConstVALUE.CODE_TYPE_FLIGHT_LMP, hkInfo.lightType);
			viewHolder.tvLightType.setText(strLightType);
			
			String strNo = hkInfo.light_no;
			viewHolder.tvNo.setText(strNo);
			
			String strPower = CodeInfo.getInstance(this).getValue(ConstVALUE.CODE_TYPE_HK_TYPE, hkInfo.power);
			viewHolder.tvPower.setText(strPower);
			
			viewHolder.editLightCnt.setText( hkInfo.light_cnt);
			
			String strLightState = CodeInfo.getInstance(this).getValue(ConstVALUE.CODE_TYPE_GOOD_SECD, hkInfo.light_state);
			if (!"001".equals(hkInfo.light_state)) {
				viewHolder.btnLightState.setTag(0);
				viewHolder.btnLightState.setBackgroundResource(R.drawable.btn_bad);
			}
			
			String strYBResult = CodeInfo.getInstance(this).getValue(ConstVALUE.CODE_TYPE_GOOD_SECD, hkInfo.yb_result);
			viewHolder.btnYBresult.setText(strYBResult);
			viewHolder.btnYBresult.setId(StringUtil.getIndex(strYBItems, strYBResult));
			
			
		}
		
		hkList.put(idx, viewHolder);
		
		llLayout.addView(view);
		
	}
	
	public class ViewHolder {
		TextView tvLightType;
		TextView tvNo;
		TextView tvPower;
        EditText editLightCnt;
        Button btnLightState;
        Button btnYBresult;
        
        HKInfo hkInfo;
	}
	
	OnClickListener YBListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			Button btn = (Button)v;
			String strYB = btn.getText().toString();
			
			int tag = (Integer)btn.getTag();
			if (tag == 1) {
				btn.setTag(0);
				btn.setBackgroundResource(R.drawable.btn_bad);
			} else {
				btn.setTag(1);
				btn.setBackgroundResource(R.drawable.btn_good);
			}
		}
	};
	
	public void showYBResultDialog(final Button btn) {
		
		AlertDialog.Builder dialog = new AlertDialog.Builder(this, AlertDialog.THEME_DEVICE_DEFAULT_LIGHT);
		dialog.setTitle(getString(R.string.yb_result));
		dialog.setSingleChoiceItems(strYBItems, btn.getId(), new android.content.DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				btn.setId(which);
				btn.setText(strYBItems[which]);
                dialog.dismiss();
			}
		}).show();
	}
	
	OnTouchListener hideKeyboardListener = new OnTouchListener() {
		@Override
		public boolean onTouch(View v, MotionEvent event) {
			UIUtil.hideKeyboard(llParent);
			return false;
		}
	};
}
