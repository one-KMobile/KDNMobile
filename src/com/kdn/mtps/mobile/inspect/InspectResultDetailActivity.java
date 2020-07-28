package com.kdn.mtps.mobile.inspect;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.TreeMap;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.kdn.mtps.mobile.BaseActivity;
import com.kdn.mtps.mobile.MainActivity;
import com.kdn.mtps.mobile.R;
import com.kdn.mtps.mobile.SplashActivity;
import com.kdn.mtps.mobile.TitleManager;
import com.kdn.mtps.mobile.camera.CameraManageActivity;
import com.kdn.mtps.mobile.constant.ConstSP;
import com.kdn.mtps.mobile.constant.ConstVALUE;
import com.kdn.mtps.mobile.db.InputBRDao;
import com.kdn.mtps.mobile.db.InputBTDao;
import com.kdn.mtps.mobile.db.InputHJDao;
import com.kdn.mtps.mobile.db.InputHKDao;
import com.kdn.mtps.mobile.db.InputJJDao;
import com.kdn.mtps.mobile.db.InputJPDao;
import com.kdn.mtps.mobile.db.InputJSDao;
import com.kdn.mtps.mobile.db.InputKBDao;
import com.kdn.mtps.mobile.db.InspectResultMasterDao;
import com.kdn.mtps.mobile.info.CodeInfo;
import com.kdn.mtps.mobile.input.InputBRActivity;
import com.kdn.mtps.mobile.input.InputBTActivity;
import com.kdn.mtps.mobile.input.InputHJActivity;
import com.kdn.mtps.mobile.input.InputHKActivity;
import com.kdn.mtps.mobile.input.InputJJActivity;
import com.kdn.mtps.mobile.input.InputJPActivity;
import com.kdn.mtps.mobile.input.InputJSActivity;
import com.kdn.mtps.mobile.input.InputKBActivity;
import com.kdn.mtps.mobile.setting.SettingActivity;
import com.kdn.mtps.mobile.util.AppUtil;
import com.kdn.mtps.mobile.util.Logg;
import com.kdn.mtps.mobile.util.Shared;
import com.kdn.mtps.mobile.util.StringUtil;

public class InspectResultDetailActivity extends BaseActivity implements TitleManager, OnClickListener{

	public final int REQ_INPUT = 100;
	
	TextView tvName;
	TextView tvDate;
	TextView tvType;
	TextView tvEqpNo;
	TextView tvWriter;
	
	LinearLayout linearDateList;
	
	FrameLayout layoutBT;
	FrameLayout layoutJP;
	FrameLayout layoutJS;
	FrameLayout layoutJJ;
	FrameLayout layoutKB;
	FrameLayout layoutHK;
	FrameLayout layoutHJ;
	FrameLayout layoutBR;
	
	TextView tvBTInput;
	TextView tvJPInput;
	TextView tvJSInput;
	TextView tvJJInput;
	TextView tvKBInput;
	TextView tvHKInput;
	TextView tvHJInput;
	TextView tvBRInput;
	
	Button btnStatus;
	Button btnEditStatus;
	Button btnFacBT;
	Button btnFacJS;
	Button btnFacJJ;
	Button btnFacKB;
	Button btnFacHK;
	Button btnFacHJ;
	Button btnFacBR;
	Button btnFacJP;
	
	Button btnFacBTCamera;
	Button btnFacJSCamera;
	Button btnFacJJCamera;
	Button btnFacKBCamera;
	Button btnFacHKCamera;
	Button btnFacHJCamera;
	Button btnFacBRCamera;
	Button btnFacJPCamera;
	
	ImageView ivBTLine;
	ImageView ivJSLine;
	ImageView ivJJLine;
	ImageView ivKBLine;
	ImageView ivHKLine;
	ImageView ivHJLine;
	ImageView ivBRLine;
	ImageView ivJPLine;
	
	InspectInfo mInfo;
	InspectInfo mSelectedInfo;
	int menuType;
	String towerNo;
	String strCurrentDate;
	
	LocationManager locationManager;
	ArrayList<InspectInfo> insList;
	
	TreeMap<String, ArrayList<InspectInfo>> dateInsList;
	String mEqpNo;
	String nfcTag;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		if (savedInstanceState != null) {
			Intent intent = new Intent(InspectResultDetailActivity.this, SplashActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			AppUtil.startActivity(this, intent);
			finish();
		}
		
		setContentView(R.layout.activity_inspect_detail);
		
		Intent intent = getIntent();
		mInfo = intent.getParcelableExtra("inspect");
		strCurrentDate = intent.getStringExtra("currentDate"); 
		nfcTag = intent.getStringExtra("nfc_tag");
		
		if (strCurrentDate == null || "".equals(strCurrentDate))
			strCurrentDate = new SimpleDateFormat("yyyyMM").format(System.currentTimeMillis());
			
		boolean isNfc = intent.getBooleanExtra("is_nfc", false);
//		towerNo = intent.getStringExtra("tower_no");
//		menuType = intent.getIntExtra(ConstVALUE.DISPLAY_MENU_TYPE, 0);
		
		setTitle();
		
		setInit();
		
		setData();
		
		if (isNfc)
			requestLocation();
	}

	@Override
	public void setTitle() {
		Button btnHeaderTitle = (Button)findViewById(R.id.btnHeaderTitle);
		Button btnHeaderLeft = (Button)findViewById(R.id.btnHeaderLeft);
		Button btnHeaderRight = (Button)findViewById(R.id.btnHeaderRight);
		
		btnHeaderTitle.setText("순시결과입력");
		
		btnHeaderLeft.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		
		btnHeaderRight.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				AppUtil.startActivity(InspectResultDetailActivity.this, new Intent(InspectResultDetailActivity.this, MainActivity.class));
			}
		});
	}

	public void setInit() {
		tvName = (TextView)findViewById(R.id.tvName);
		tvDate = (TextView)	findViewById(R.id.tvDate);
		tvType = (TextView)findViewById(R.id.tvType);
		tvEqpNo = (TextView)findViewById(R.id.tvEqpNo);
		tvWriter = (TextView)findViewById(R.id.tvWriter);
		tvWriter.setText(Shared.getString(this, ConstSP.USER_NAME));
		
		linearDateList = (LinearLayout)findViewById(R.id.linearDateList);
		
		layoutBT = (FrameLayout)findViewById(R.id.layout_bt);
		layoutJP = (FrameLayout)findViewById(R.id.layout_jp);
		layoutJS = (FrameLayout)findViewById(R.id.layout_js);
		layoutJJ = (FrameLayout)findViewById(R.id.layout_jj);
		layoutKB = (FrameLayout)findViewById(R.id.layout_kb);
		layoutHK = (FrameLayout)findViewById(R.id.layout_hk);
		layoutHJ = (FrameLayout)findViewById(R.id.layout_hj);
		layoutBR = (FrameLayout)findViewById(R.id.layout_br);
		
		tvBTInput = (TextView)findViewById(R.id.tvBTInput);
		tvJPInput = (TextView)findViewById(R.id.tvJPInput);
		tvJSInput = (TextView)findViewById(R.id.tvJSInput);
		tvJJInput = (TextView)findViewById(R.id.tvJJInput);
		tvKBInput = (TextView)findViewById(R.id.tvKBInput);
		tvHKInput = (TextView)findViewById(R.id.tvHKInput);
		tvHJInput = (TextView)findViewById(R.id.tvHJInput);
		tvBRInput = (TextView)findViewById(R.id.tvBRInput);
		
		tvBTInput.setText(CodeInfo.getInstance(this).getValue(ConstVALUE.CODE_TYPE_INS_TYPE, ConstVALUE.CODE_NO_INSPECT_BT));
		tvJPInput.setText(CodeInfo.getInstance(this).getValue(ConstVALUE.CODE_TYPE_INS_TYPE, ConstVALUE.CODE_NO_INSPECT_JP));
		tvJSInput.setText(CodeInfo.getInstance(this).getValue(ConstVALUE.CODE_TYPE_INS_TYPE, ConstVALUE.CODE_NO_INSPECT_JS));
		tvJJInput.setText(CodeInfo.getInstance(this).getValue(ConstVALUE.CODE_TYPE_INS_TYPE, ConstVALUE.CODE_NO_INSPECT_JJ));
		tvKBInput.setText(CodeInfo.getInstance(this).getValue(ConstVALUE.CODE_TYPE_INS_TYPE, ConstVALUE.CODE_NO_INSPECT_KB));
		tvHKInput.setText(CodeInfo.getInstance(this).getValue(ConstVALUE.CODE_TYPE_INS_TYPE, ConstVALUE.CODE_NO_INSPECT_HK));
		tvHJInput.setText(CodeInfo.getInstance(this).getValue(ConstVALUE.CODE_TYPE_INS_TYPE, ConstVALUE.CODE_NO_INSPECT_HJ));
		tvBRInput.setText(CodeInfo.getInstance(this).getValue(ConstVALUE.CODE_TYPE_INS_TYPE, ConstVALUE.CODE_NO_INSPECT_BR));
		
		btnStatus = (Button)findViewById(R.id.btnStatus);
		btnEditStatus = (Button)findViewById(R.id.btnEditStatus);
		btnFacBT = (Button)findViewById(R.id.btnFacBT);
		btnFacJS = (Button)findViewById(R.id.btnFacJS);
		btnFacJJ = (Button)findViewById(R.id.btnFacJJ);
		btnFacKB = (Button)findViewById(R.id.btnFacKB);
		btnFacHK = (Button)findViewById(R.id.btnFacHK);
		btnFacHJ = (Button)findViewById(R.id.btnFacHJ);
		btnFacBR = (Button)findViewById(R.id.btnFacBR);
		btnFacJP = (Button)findViewById(R.id.btnFacJP);
		
		btnFacBTCamera = (Button)findViewById(R.id.btnFacBTCamera);
		btnFacJSCamera = (Button)findViewById(R.id.btnFacJSCamera);
		btnFacJJCamera = (Button)findViewById(R.id.btnFacJJCamera);
		btnFacKBCamera = (Button)findViewById(R.id.btnFacKBCamera);
		btnFacHKCamera = (Button)findViewById(R.id.btnFacHKCamera);
		btnFacHJCamera = (Button)findViewById(R.id.btnFacHJCamera);
		btnFacBRCamera = (Button)findViewById(R.id.btnFacBRCamera);
		btnFacJPCamera = (Button)findViewById(R.id.btnFacJPCamera);
		
		ivBTLine = (ImageView)findViewById(R.id.ivBTLine);
		ivJSLine = (ImageView)findViewById(R.id.ivJSLine);
		ivJJLine = (ImageView)findViewById(R.id.ivJJLine);
		ivKBLine = (ImageView)findViewById(R.id.ivKBLine);
		ivHKLine = (ImageView)findViewById(R.id.ivHKLine);
		ivHJLine = (ImageView)findViewById(R.id.ivHJLine);
		ivBRLine = (ImageView)findViewById(R.id.ivBRLine);
		ivJPLine = (ImageView)findViewById(R.id.ivJPLine);
		
//		btnFacBTCamera.setVisibility(View.GONE);
//		btnFacJSCamera.setVisibility(View.GONE);
//		btnFacJJCamera.setVisibility(View.GONE);
//		btnFacKBCamera.setVisibility(View.GONE);
//		btnFacHKCamera.setVisibility(View.GONE);
		
		btnStatus.setOnClickListener(this);
		btnEditStatus.setOnClickListener(this);
		btnFacBT.setOnClickListener(this);
		btnFacJS.setOnClickListener(this);
		btnFacJJ.setOnClickListener(this);
		btnFacKB.setOnClickListener(this);
		btnFacHK.setOnClickListener(this);
		btnFacHJ.setOnClickListener(this);
		btnFacBR.setOnClickListener(this);
		btnFacJP.setOnClickListener(this);
		
		btnFacBTCamera.setOnClickListener(this);
		btnFacJSCamera.setOnClickListener(this);
		btnFacJJCamera.setOnClickListener(this);
		btnFacKBCamera.setOnClickListener(this);
		btnFacHKCamera.setOnClickListener(this);
		btnFacHJCamera.setOnClickListener(this);
		btnFacBRCamera.setOnClickListener(this);
		btnFacJPCamera.setOnClickListener(this);
	}
	
	public void setData() {
		
		InspectResultMasterDao insRetDao = InspectResultMasterDao.getInstance(InspectResultDetailActivity.this);
		insList =  insRetDao.selectTowerInsList(mInfo.eqpNo);
//		insList =  insRetDao.selectTowerInsList(mInfo.tracksName, mInfo.towerNo);
		if (insList.size() == 0)
			return;
		
		dateInsList = new TreeMap<String, ArrayList<InspectInfo>>(); 
		ArrayList<InspectInfo> list = null;
		for (int i=0; i<insList.size(); i++) {
			if (dateInsList.get(insList.get(i).date) == null) {
				list = new ArrayList<InspectInfo>();
				list.add(insList.get(i));
				dateInsList.put(insList.get(i).date, list);
			} else {
				ArrayList<InspectInfo> oldList = dateInsList.get(insList.get(i).date);
				oldList.add(insList.get(i));
				dateInsList.put(insList.get(i).date, oldList);
			}
			
		}
		
		setDate();
		
		linearDateList.removeAllViews();
		
		Iterator<String> it =  dateInsList.keySet().iterator();
		int idx=0;
		while (it.hasNext()) {
			String key = it.next();
			
			if (strCurrentDate == null)
				strCurrentDate = key;
			
			addItems(key);

			idx++;
		}
		
		setLayout(strCurrentDate);
	}
	
	public void setDate() {
		ArrayList<String> arrCheckDate = new ArrayList<String>();
		String firstKey = "";
		Iterator<String> it =  dateInsList.keySet().iterator();
		int idx=0;
		while (it.hasNext()) {
			String key = it.next();
			arrCheckDate.add(key);
			
			if (idx == 0)
				firstKey = key;
		}
		
		if (!arrCheckDate.contains(strCurrentDate)) {
			strCurrentDate = firstKey;
		}
	}
	
	private void addItems(String strDate) {
		
		View view = LayoutInflater.from(this).inflate(R.layout.inspect_detail_date_item, null);
		LinearLayout linearRoot = (LinearLayout) view.findViewById(R.id.linear_root);
		Button btnDate = (Button) view.findViewById(R.id.btnDate);
		btnDate.setText(StringUtil.printDate(strDate));
		
		btnDate.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				for (int i=0; i<linearDateList.getChildCount(); i++) {
					LinearLayout linear = (LinearLayout) linearDateList.getChildAt(i);
					Button btn = (Button)linear.getChildAt(0);
					btn.setBackgroundResource(R.drawable.air_date_btn_bg);
					btn.setTextColor(Color.BLACK);
				}
				((Button)v).setBackgroundResource(R.drawable.air_date_btn_bg_ov);
				((Button)v).setTextColor(Color.BLACK);
				
				setLayout(StringUtil.removeDot(((Button)v).getText().toString()));
			}
		});
		
		if (strDate.equals(strCurrentDate)) {
			btnDate.setTextColor(Color.BLACK);
			btnDate.setBackgroundResource(R.drawable.air_date_btn_bg_ov);
		}
		
		linearDateList.addView(linearRoot);
	}
	
	public void setLayout(String strDate) {
		setGoneLayout();
		
		strCurrentDate = strDate; 
				
		ArrayList list = dateInsList.get(strDate);
		
		if (list == null)
			return;
		
		for (int i=0; i<list.size(); i++) {
			mSelectedInfo = (InspectInfo) list.get(i);
			if (mSelectedInfo.type.equals(ConstVALUE.CODE_NO_INSPECT_BT)) {
				btnFacBT.setTag(mSelectedInfo);
				btnFacBTCamera.setTag(mSelectedInfo);
				layoutBT.setVisibility(View.VISIBLE);
				ivBTLine.setVisibility(View.VISIBLE);
				InputBTDao inputBTDao = InputBTDao.getInstance(this);
				if (!inputBTDao.existBT(mSelectedInfo.master_idx))
					btnFacBT.setBackgroundResource(R.drawable.selector_input_bt_insert);
				else
					btnFacBT.setBackgroundResource(R.drawable.selector_input_bt_edit);
			} else if (mSelectedInfo.type.equals(ConstVALUE.CODE_NO_INSPECT_HK)) {
				btnFacHK.setTag(mSelectedInfo);
				btnFacHKCamera.setTag(mSelectedInfo);
				layoutHK.setVisibility(View.VISIBLE);
				ivHKLine.setVisibility(View.VISIBLE);
				InputHKDao inputHKDao = InputHKDao.getInstance(this);
				if (!inputHKDao.existHK(mSelectedInfo.master_idx))
					btnFacHK.setBackgroundResource(R.drawable.selector_input_bt_insert);
				else
					btnFacHK.setBackgroundResource(R.drawable.selector_input_bt_edit);
			} else if (mSelectedInfo.type.equals(ConstVALUE.CODE_NO_INSPECT_JS)) {
				btnFacJS.setTag(mSelectedInfo);
				btnFacJSCamera.setTag(mSelectedInfo);
				layoutJS.setVisibility(View.VISIBLE);
				ivJSLine.setVisibility(View.VISIBLE);
				InputJSDao inputJSDao = InputJSDao.getInstance(this);
				if (!inputJSDao.existJS(mSelectedInfo.master_idx))
					btnFacJS.setBackgroundResource(R.drawable.selector_input_bt_insert);
				else
					btnFacJS.setBackgroundResource(R.drawable.selector_input_bt_edit);
			}  else if (mSelectedInfo.type.equals(ConstVALUE.CODE_NO_INSPECT_JJ)) {
				btnFacJJ.setTag(mSelectedInfo);
				btnFacJJCamera.setTag(mSelectedInfo);
				layoutJJ.setVisibility(View.VISIBLE);
				ivJJLine.setVisibility(View.VISIBLE);
				InputJJDao inputJJDao = InputJJDao.getInstance(this);
				if (!inputJJDao.existJJ(mSelectedInfo.master_idx))
					btnFacJJ.setBackgroundResource(R.drawable.selector_input_bt_insert);
				else
					btnFacJJ.setBackgroundResource(R.drawable.selector_input_bt_edit);
			}  else if (mSelectedInfo.type.equals(ConstVALUE.CODE_NO_INSPECT_KB)) {
				btnFacKB.setTag(mSelectedInfo);
				btnFacKBCamera.setTag(mSelectedInfo);
				layoutKB.setVisibility(View.VISIBLE);
				ivKBLine.setVisibility(View.VISIBLE);
				InputKBDao inputKBDao = InputKBDao.getInstance(this);
				if (!inputKBDao.existKB(mSelectedInfo.master_idx))
					btnFacKB.setBackgroundResource(R.drawable.selector_input_bt_insert);
				else
					btnFacKB.setBackgroundResource(R.drawable.selector_input_bt_edit);
			}  else if (mSelectedInfo.type.equals(ConstVALUE.CODE_NO_INSPECT_HJ)) {
				btnFacHJ.setTag(mSelectedInfo);
				btnFacHJCamera.setTag(mSelectedInfo);
				layoutHJ.setVisibility(View.VISIBLE);
				ivHJLine.setVisibility(View.VISIBLE);
				InputHJDao inputHJDao = InputHJDao.getInstance(this);
				if (!inputHJDao.existHJ(mSelectedInfo.master_idx))
					btnFacHJ.setBackgroundResource(R.drawable.selector_input_bt_insert);
				else
					btnFacHJ.setBackgroundResource(R.drawable.selector_input_bt_edit);
			}  else if (mSelectedInfo.type.equals(ConstVALUE.CODE_NO_INSPECT_BR)) {
				btnFacBR.setTag(mSelectedInfo);
				btnFacBRCamera.setTag(mSelectedInfo);
				layoutBR.setVisibility(View.VISIBLE);
				ivBRLine.setVisibility(View.VISIBLE);
				InputBRDao inputBRDao = InputBRDao.getInstance(this);
				if (!inputBRDao.existBR(mSelectedInfo.master_idx))
					btnFacBR.setBackgroundResource(R.drawable.selector_input_bt_insert);
				else
					btnFacBR.setBackgroundResource(R.drawable.selector_input_bt_edit);
			}  else if (mSelectedInfo.type.equals(ConstVALUE.CODE_NO_INSPECT_JP)) {
				btnFacJP.setTag(mSelectedInfo);
				btnFacJPCamera.setTag(mSelectedInfo);
				layoutJP.setVisibility(View.VISIBLE);
				ivJPLine.setVisibility(View.VISIBLE);
				InputJPDao inputJPDao = InputJPDao.getInstance(this);
				if (!inputJPDao.existJP(mSelectedInfo.master_idx))
					btnFacJP.setBackgroundResource(R.drawable.selector_input_bt_insert);
				else
					btnFacJP.setBackgroundResource(R.drawable.selector_input_bt_edit);
			}
		}
		
		setHeader();
	}
	
	public void setHeader() {
		tvName.setText(mSelectedInfo.tracksName);
		tvDate.setText(StringUtil.printDate(mSelectedInfo.date));
//		tvType.setText(mInfo.type);
//		tvEqpNo.setText(mSelectedInfo.towerNo);
		tvEqpNo.setText(mSelectedInfo.eqpNm);
	}
	
	public void setGoneLayout() {
		layoutBT.setVisibility(View.GONE);
		layoutJP.setVisibility(View.GONE);
		layoutJS.setVisibility(View.GONE);
		layoutJJ.setVisibility(View.GONE);
		layoutKB.setVisibility(View.GONE);
		layoutHK.setVisibility(View.GONE);
		layoutHJ.setVisibility(View.GONE);
		layoutBR.setVisibility(View.GONE);
		
		ivBTLine.setVisibility(View.GONE);
		ivJSLine.setVisibility(View.GONE);
		ivJJLine.setVisibility(View.GONE);
		ivKBLine.setVisibility(View.GONE);
		ivHKLine.setVisibility(View.GONE);
		ivHJLine.setVisibility(View.GONE);
		ivBRLine.setVisibility(View.GONE);
		ivJPLine.setVisibility(View.GONE);
	}

	@Override
	public void onClick(View v) {
		Intent intent;
		switch (v.getId()) {
		case R.id.btnFacBT:
			goInputActivity(v, InputBTActivity.class);
			break;
		case R.id.btnFacJP:
			goInputActivity(v, InputJPActivity.class);
			break;
		case R.id.btnFacJS:
			goInputActivity(v, InputJSActivity.class);
			break;
		case R.id.btnFacJJ:
			goInputActivity(v, InputJJActivity.class);
			break;
		case R.id.btnFacKB:
			goInputActivity(v, InputKBActivity.class);
			break;
		case R.id.btnFacHK:
			goInputActivity(v, InputHKActivity.class);
			break;
		case R.id.btnFacHJ:
			goInputActivity(v, InputHJActivity.class);
			break;
		case R.id.btnFacBR:
			goInputActivity(v, InputBRActivity.class);
			break;
		case R.id.btnFacBTCamera:
			goCameraManage(v, "보통순시");
			break;
		case R.id.btnFacJPCamera:
			goCameraManage(v, "전퍼접속개소순시");
			break;
		case R.id.btnFacJSCamera:
			goCameraManage(v, "전선접속개소순시");
			break;
		case R.id.btnFacJJCamera:
			goCameraManage(v, "접지저항 측정");
			break;
		case R.id.btnFacKBCamera:
			goCameraManage(v, "기별점검");
			break;
		case R.id.btnFacHKCamera:
			goCameraManage(v, "항공등 점등점검");
			break;
		case R.id.btnFacHJCamera:
			goCameraManage(v, "항공장애등 점검");
			break;
		case R.id.btnFacBRCamera:
			goCameraManage(v, "불량애자 점검");
			break;
		}
	}
	
	public void goCameraManage(View view, String type) {
		InspectInfo info = (InspectInfo) view.getTag();
		Intent intent = new Intent(InspectResultDetailActivity.this, CameraManageActivity.class);
		intent.putExtra("inspect", info);
		intent.putExtra("strType", type);
		startActivity(intent);
	}
	
	public void goInputActivity(View view,Class<?> cls) {
		String inspecter_1 = Shared.getString(InspectResultDetailActivity.this, ConstSP.SETTING_INSPECTER_ID_1);
		String inspecter_2 = Shared.getString(InspectResultDetailActivity.this, ConstSP.SETTING_INSPECTER_ID_2);
		
		if (inspecter_1 == null || "".equals(inspecter_1) || inspecter_2 == null || "".equals(inspecter_2))  {
			Intent intent = new Intent(InspectResultDetailActivity.this, SettingActivity.class);
			intent.putExtra("setInspecter", true);
			startActivity(intent);
			return;
		}
		
		InspectInfo info = (InspectInfo) view.getTag();
		Intent intent = new Intent(InspectResultDetailActivity.this, cls);
		intent.putExtra("inspect", info);
		intent.putExtra("currentDate", strCurrentDate);
		startActivityForResult(intent, REQ_INPUT);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		
		if (requestCode == REQ_INPUT) {
			if (data != null)
				strCurrentDate = data.getStringExtra("currentDate");
			setData();
		}
	}
	

	public void requestLocation() {
		String provider;
		locationManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER))
        {
        	provider = LocationManager.GPS_PROVIDER;
        }
        else
        {
        	provider = locationManager.getBestProvider(new Criteria(), true);
        }

        if (provider == null || "passive".equals(provider))
        	return;
        
        locationManager.requestLocationUpdates(provider, 0,0, listener);
//        mHandler.sendEmptyMessageDelayed(TAG_REMOVE_LOCATION_LISTENER, 20000);
        
	}
	
	LocationListener listener = new LocationListener() {
		public void onStatusChanged(String provider, int status, Bundle extras) {
			switch (status) {
			case LocationProvider.OUT_OF_SERVICE:
				Logg.d(InspectResultDetailActivity.this, "상태: 서비스 범위 밖입니다.");
				break;
			case LocationProvider.TEMPORARILY_UNAVAILABLE:
				Logg.d(InspectResultDetailActivity.this, "상태: 일시적으로 서비스 사용 불가능");
				break;
			case LocationProvider.AVAILABLE:
				Logg.d(InspectResultDetailActivity.this, "상태: 서비스 이용 가능");
				break;
				
			}
		}
		
		public void onProviderEnabled(String provider) {
			Logg.d(InspectResultDetailActivity.this, "상태: 서비스 사용가능");
		}
		
		public void onProviderDisabled(String provider) {
			Logg.d(InspectResultDetailActivity.this, "상태: 서비스 사용불가");
		}
		
		@Override
		public void onLocationChanged(Location location) {
			String currentLocation = String.format("위도:%f 경도:%f 고도:%f", location.getLatitude(), location.getLongitude(), location.getAltitude());
			Logg.d(InspectResultDetailActivity.this, "currentLocation : " + currentLocation);
            Calendar cal = new GregorianCalendar();
            String now = String.format("%d년 %d월 %d일 %d시 %d분 %d초", cal.get(Calendar.YEAR), cal.get(Calendar.MONTH)+1, 
					cal.get(Calendar.DAY_OF_MONTH), cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE),
					cal.get(Calendar.SECOND));
            Logg.d(InspectResultDetailActivity.this, "갱신 시간 : " + now);
            
            InspectResultMasterDao insRetDao = InspectResultMasterDao.getInstance(InspectResultDetailActivity.this);
            
            for (InspectInfo info : insList) {
            	insRetDao.updateNfcTagLocation(info.master_idx, nfcTag, location.getLatitude()+"", location.getLongitude()+"", true);
            }
            
            if (locationManager != null) {
            	locationManager.removeUpdates(listener);
            	locationManager = null;
            }
		}
	};
}
