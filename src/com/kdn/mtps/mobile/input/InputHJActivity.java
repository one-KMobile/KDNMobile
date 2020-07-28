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
import com.kdn.mtps.mobile.db.InputHJDao;
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

public class InputHJActivity extends BaseActivity implements TitleManager, OnClickListener {
	LinearLayout llParent;
	ScrollView svParent;
	
	TextView tvName;
	TextView tvDate;
	TextView tvEqpNm;
	TextView tvType;

	String strWeather;
	
	Button btnAdd;
	Button btnEdit;
	Button btnDelete;
	Button btnCamera;
	
	ImageView ivComplte;
	
	LinearLayout llLayout;
	
	ArrayList<HJInfo> hjInfoList;
	HJInfo selectHJInfo;
	
	InspectInfo mInfo;
	
	int selectedWeatherNo = 0;
	
	public LinkedHashMap<Integer, ViewHolder> hjList = new LinkedHashMap<Integer, ViewHolder>();
	
	final String strWeatherItems[] = CodeInfo.getInstance(this).getNames(ConstVALUE.CODE_TYPE_WEATHER);
	final String strLightTypeItems[] = CodeInfo.getInstance(this).getNames(ConstVALUE.CODE_TYPE_FLIGHT_LMP);
	final String strPowerItems[] = CodeInfo.getInstance(this).getNames(ConstVALUE.CODE_TYPE_HK_TYPE);
	final String strYBItems[] = CodeInfo.getInstance(this).getNames(ConstVALUE.CODE_TYPE_GOOD_SECD);
	final String strYBTwoResultItems[] = CodeInfo.getInstance(this).getYBNames();
	
	boolean isAdd;
	String strCurrentDate;
	LocationManager locationManager;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_input_hj);
		
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
			ivComplte.setBackgroundResource(R.drawable.input_hj_add);
			
			strWeather = Shared.getString(this, ConstSP.SETTING_WEATHER);
			
			for (int i=0; i<list.size(); i++) {
				addItem(i, list.get(i));
			}
			
			addBottomPadding();
		} else {
			ivComplte.setBackgroundResource(R.drawable.input_hj_edit);
			
			InputHJDao inputHJDao = InputHJDao.getInstance(this);
			hjInfoList = inputHJDao.selectHJ(mInfo.master_idx);
			
			selectHJInfo = hjInfoList.get(0);
			
			strWeather = CodeInfo.getInstance(this).getValue(ConstVALUE.CODE_TYPE_WEATHER, selectHJInfo.weather);
			selectedWeatherNo = StringUtil.getIndex(strWeatherItems, strWeather);
			
			for (int i=0; i<list.size(); i++) {
				addItem(i, hjInfoList.get(i));
			}
			
			addBottomPadding();
		}
		
//		if (mInfo.send_yn_hj != null && !"".equals(mInfo.send_yn_hj)) {
//			ivComplte.setBackgroundResource(R.drawable.input_hj_send);
//		}
	}
	
	public void addBottomPadding() {
		LinearLayout linearPadding = new LinearLayout(this);
		linearPadding.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, (int) UIUtil.pxFromDp(this, 50)));
		llLayout.addView(linearPadding);		
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
				AppUtil.startActivity(InputHJActivity.this, new Intent(InputHJActivity.this, MainActivity.class));
			}
		});
	}

	@Override
	public void onClick(View v) {
		Button btn = (Button)v;
		
		switch (v.getId()) {
		case R.id.btnAdd:
			update(false);
			break;
		case R.id.btnEdit:
			update(true);
			break;
		case R.id.btnDelete:
			InputHJDao inputHJDao = InputHJDao.getInstance(this);
			inputHJDao.Delete(mInfo.master_idx);
			InspectResultMasterDao insRetDao = InspectResultMasterDao.getInstance(this);
			insRetDao.updateComplete(mInfo.master_idx, "N", ConstVALUE.CODE_NO_INSPECT_HJ);
			setResult();
			finish();
			break;
		case R.id.btnCamera:
			Intent intent = new Intent(InputHJActivity.this, CameraManageActivity.class);
			intent.putExtra("inspect", mInfo);
			startActivity(intent);
			break;
		default:
			break;
		}
	}

	
	public void setInputType() {
		InputHJDao inputHJDao = InputHJDao.getInstance(this);
		if (inputHJDao.existHJ(mInfo.master_idx))
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
		InputHJDao inputHJDao = InputHJDao.getInstance(this);
		inputHJDao.Delete(mInfo.master_idx);
		
		for (Entry<Integer, ViewHolder> entry : hjList.entrySet()) {
			ViewHolder viewHolder = entry.getValue();
			
			String strLightType = viewHolder.tvLightType.getText().toString();
			String strPower = viewHolder.tvPower.getText().toString();
			String strNo = viewHolder.tvNo.getText().toString();
			String strControl = viewHolder.btnControl.getText().toString();
			String strSunBattery = viewHolder.btnSunBattery.getText().toString();
			String strStorageBattery = viewHolder.btnStorageBattery.getText().toString();
			String strLightItem = viewHolder.btnLightItem.getText().toString();
			String strCable = viewHolder.btnCable.getText().toString();
			String strYBResult = viewHolder.btnYbResult.getText().toString();
			
			int tagControl = (Integer)viewHolder.btnControl.getTag();
			int tagSunBattery = (Integer)viewHolder.btnSunBattery.getTag();
			int tagStorageBattery = (Integer)viewHolder.btnStorageBattery.getTag();
			int tagLightItem = (Integer)viewHolder.btnLightItem.getTag();
			int tagCable = (Integer)viewHolder.btnCable.getTag();
			
			HJInfo log = new HJInfo();
			log.masterIdx = mInfo.master_idx;
			log.weather = CodeInfo.getInstance(this).getKey(ConstVALUE.CODE_TYPE_WEATHER, strWeather);
			log.light_type = CodeInfo.getInstance(this).getKey(ConstVALUE.CODE_TYPE_FLIGHT_LMP, strLightType);;
			log.power = CodeInfo.getInstance(this).getKey(ConstVALUE.CODE_TYPE_HK_TYPE, strPower);
			log.light_no = strNo;
			
			String item1 = CodeInfo.getInstance(this).getKey(ConstVALUE.CODE_TYPE_GOOD_SECD, strYBTwoResultItems[0]);
			String item2  = CodeInfo.getInstance(this).getKey(ConstVALUE.CODE_TYPE_GOOD_SECD, strYBTwoResultItems[1]);
			
			log.control = (tagControl == 1 ? item1 : item2);
			log.sun_battery = (tagSunBattery == 1 ? item1 : item2);
			log.storage_battery = (tagStorageBattery == 1 ? item1 : item2);
			log.light_item = (tagLightItem == 1 ? item1 : item2);
			log.cable = (tagCable == 1 ? item1 : item2);
			log.yb_result = CodeInfo.getInstance(this).getKey(ConstVALUE.CODE_TYPE_GOOD_SECD, strYBResult);
			
			if (isEdit) {
				inputHJDao.Append(log, viewHolder.hjInfo.idx);
			} else {
				inputHJDao.Append(log);
			}
		}
		
		if (!isEdit)
			LocManager.getInstance(this).startGetLocation(listener);

		setResult(RESULT_OK);
		
		InspectResultMasterDao insRetDao = InspectResultMasterDao.getInstance(this);
		insRetDao.updateComplete(mInfo.master_idx, "Y", ConstVALUE.CODE_NO_INSPECT_HJ);
		
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
			
			LocManager.getInstance(InputHJActivity.this).setGetGps(true);
			
			String currentLocation = String.format("위도:%f  경도:%f  고도:%f", location.getLatitude(), location.getLongitude(), location.getAltitude());
			Logg.d(InputHJActivity.this, "currentLocation : " + currentLocation);
            Calendar cal = new GregorianCalendar();
            String now = String.format("%d년 %d월 %d일 %d시 %d분 %d초", cal.get(Calendar.YEAR), cal.get(Calendar.MONTH)+1, 
					cal.get(Calendar.DAY_OF_MONTH), cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE),
					cal.get(Calendar.SECOND));
            Logg.d(InputHJActivity.this, "갱신 시간 : " + now);
            
            InspectResultMasterDao insRetDao = InspectResultMasterDao.getInstance(InputHJActivity.this);
            insRetDao.updateNfcTagLocation(mInfo.master_idx, "", location.getLatitude()+"", location.getLongitude()+"", false);
            
            if (locationManager != null) {
            	locationManager.removeUpdates(listener);
            }
		}
	};
	
	public void addItem(int idx, HKSubInfo hkSubInfo) {
		HJInfo hjInfo = new HJInfo();
		hjInfo.light_type = hkSubInfo.FLIGHT_LMP_KND;
		hjInfo.power = hkSubInfo.SRCELCT_KND;
		hjInfo.light_no = hkSubInfo.FLIGHT_LMP_NO;
		hjInfo.control = CodeInfo.getInstance(this).getKey(ConstVALUE.CODE_TYPE_GOOD_SECD, strYBTwoResultItems[0]);
		hjInfo.sun_battery = CodeInfo.getInstance(this).getKey(ConstVALUE.CODE_TYPE_GOOD_SECD, strYBTwoResultItems[0]);
		hjInfo.storage_battery = CodeInfo.getInstance(this).getKey(ConstVALUE.CODE_TYPE_GOOD_SECD, strYBTwoResultItems[0]);
		hjInfo.light_item = CodeInfo.getInstance(this).getKey(ConstVALUE.CODE_TYPE_GOOD_SECD, strYBTwoResultItems[0]);
		hjInfo.cable = CodeInfo.getInstance(this).getKey(ConstVALUE.CODE_TYPE_GOOD_SECD, strYBTwoResultItems[0]);
		hjInfo.yb_result = CodeInfo.getInstance(this).getKey(ConstVALUE.CODE_TYPE_GOOD_SECD, strYBTwoResultItems[0]);
		
		addItem(idx, hjInfo);
	}
	
	public void addItem(int idx, HJInfo hjInfo) {
		
		final ViewHolder viewHolder = new ViewHolder();
		View view = LayoutInflater.from(this).inflate(R.layout.item_hj, null);
		LinearLayout llItemParent = (LinearLayout) view.findViewById(R.id.llItemParent);
		
		UIUtil.setFont(this, (ViewGroup)llItemParent);
		
		llItemParent.setOnTouchListener(hideKeyboardListener);
		viewHolder.tvLightType = (TextView) view.findViewById(R.id.tvLightType);
		viewHolder.tvPower = (TextView) view.findViewById(R.id.tvPower);
		viewHolder.tvNo = (TextView) view.findViewById(R.id.tvNo);
		viewHolder.btnControl  = (Button)view.findViewById(R.id.btnControl);
		viewHolder.btnControl.setBackgroundResource(R.drawable.btn_good);
		viewHolder.btnSunBattery  = (Button)view.findViewById(R.id.btnSunBattery);
		viewHolder.btnSunBattery.setBackgroundResource(R.drawable.btn_good);
		viewHolder.btnStorageBattery  = (Button)view.findViewById(R.id.btnStorageBattery);
		viewHolder.btnStorageBattery.setBackgroundResource(R.drawable.btn_good);
		viewHolder.btnLightItem  = (Button)view.findViewById(R.id.btnLightItem);
		viewHolder.btnLightItem.setBackgroundResource(R.drawable.btn_good);
		viewHolder.btnCable  = (Button)view.findViewById(R.id.btnCable);
		viewHolder.btnCable.setBackgroundResource(R.drawable.btn_good);
		viewHolder.btnControl.setOnClickListener(YBListener);
		viewHolder.btnSunBattery.setOnClickListener(YBListener);
		viewHolder.btnStorageBattery.setOnClickListener(YBListener);
		viewHolder.btnLightItem.setOnClickListener(YBListener);
		viewHolder.btnCable.setOnClickListener(YBListener);
		viewHolder.btnYbResult  = (Button)view.findViewById(R.id.btnYBresult);
        viewHolder.btnYbResult.setId(0);
        viewHolder.btnYbResult.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				showYBResultDialog((Button)v);
			}
		});
        
        viewHolder.btnControl.setTag(1);
        viewHolder.btnSunBattery.setTag(1);
        viewHolder.btnStorageBattery.setTag(1);
        viewHolder.btnLightItem.setTag(1);
        viewHolder.btnCable.setTag(1);
        viewHolder.btnYbResult.setTag(1);
		
		viewHolder.tvNo.setText((idx + 1) + "");
		
		if (hjInfo != null) {
			viewHolder.hjInfo = hjInfo;
			
			String strLightType = CodeInfo.getInstance(this).getValue(ConstVALUE.CODE_TYPE_FLIGHT_LMP, hjInfo.light_type);
			viewHolder.tvLightType.setText(strLightType);
			
			String strPower = CodeInfo.getInstance(this).getValue(ConstVALUE.CODE_TYPE_HK_TYPE, hjInfo.power);
			viewHolder.tvPower.setText(strPower);
			
//			viewHolder.tvNo.setText(hjInfo.c2_js);
			
			if (!"001".equals(hjInfo.control)) {
				viewHolder.btnControl.setTag(0);
				viewHolder.btnControl.setBackgroundResource(R.drawable.btn_bad);
			}
			
			if (!"001".equals(hjInfo.sun_battery)) {
				viewHolder.btnSunBattery.setTag(0);
				viewHolder.btnSunBattery.setBackgroundResource(R.drawable.btn_bad);
			}
			
			if (!"001".equals(hjInfo.storage_battery)) {
				viewHolder.btnStorageBattery.setTag(0);
				viewHolder.btnStorageBattery.setBackgroundResource(R.drawable.btn_bad);
			}
			
			if (!"001".equals(hjInfo.light_item)) {
				viewHolder.btnLightItem.setTag(0);
				viewHolder.btnLightItem.setBackgroundResource(R.drawable.btn_bad);
			}
			
			if (!"001".equals(hjInfo.cable)) {
				viewHolder.btnCable.setTag(0);
				viewHolder.btnCable.setBackgroundResource(R.drawable.btn_bad);
			}
			
			String strYBResult = CodeInfo.getInstance(this).getValue(ConstVALUE.CODE_TYPE_GOOD_SECD, hjInfo.yb_result);
			viewHolder.btnYbResult.setText(strYBResult);
			viewHolder.btnYbResult.setId(StringUtil.getIndex(strYBItems, strYBResult));
			
		}
		
		hjList.put(idx, viewHolder);
		
		llLayout.addView(view);
		
		
	}
	
	public class ViewHolder {
		TextView tvLightType;
		TextView tvPower;
		TextView tvNo;
		Button btnControl;
		Button btnSunBattery;
		Button btnStorageBattery;
		Button btnLightItem;
		Button btnCable;
		Button btnYbResult;
		
		HJInfo hjInfo;
	}
	
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

	OnTouchListener hideKeyboardListener = new OnTouchListener() {
		@Override
		public boolean onTouch(View v, MotionEvent event) {
			UIUtil.hideKeyboard(llParent);
			return false;
		}
	};
}

