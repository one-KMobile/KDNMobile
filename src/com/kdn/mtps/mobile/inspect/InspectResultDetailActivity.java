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
import com.kdn.mtps.mobile.db.InputGHDao;
import com.kdn.mtps.mobile.db.InputHJDao;
import com.kdn.mtps.mobile.db.InputHKDao;
import com.kdn.mtps.mobile.db.InputJBDao;
import com.kdn.mtps.mobile.db.InputJGDao;
import com.kdn.mtps.mobile.db.InputJJDao;
import com.kdn.mtps.mobile.db.InputJPDao;
import com.kdn.mtps.mobile.db.InputJSDao;
import com.kdn.mtps.mobile.db.InputKBDao;
import com.kdn.mtps.mobile.db.InputMHDao;
import com.kdn.mtps.mobile.db.InputPRDao;
import com.kdn.mtps.mobile.db.InspectResultMasterDao;
import com.kdn.mtps.mobile.info.CodeInfo;
import com.kdn.mtps.mobile.input.InputBRActivity;
import com.kdn.mtps.mobile.input.InputBTActivity;
import com.kdn.mtps.mobile.input.InputGHActivity;
import com.kdn.mtps.mobile.input.InputHJActivity;
import com.kdn.mtps.mobile.input.InputHKActivity;
import com.kdn.mtps.mobile.input.InputJBActivity;
import com.kdn.mtps.mobile.input.InputJGActivity;
import com.kdn.mtps.mobile.input.InputJJActivity;
import com.kdn.mtps.mobile.input.InputJPActivity;
import com.kdn.mtps.mobile.input.InputJSActivity;
import com.kdn.mtps.mobile.input.InputKBActivity;
import com.kdn.mtps.mobile.input.InputMHActivity;
import com.kdn.mtps.mobile.input.InputPRActivity;
import com.kdn.mtps.mobile.input.InputYBActivity;
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
	FrameLayout layoutJG;
	FrameLayout layoutYB;
	FrameLayout layoutMH;
	FrameLayout layoutGH;
	FrameLayout layoutPR;
	FrameLayout layoutJB;
	FrameLayout layoutJS;


	TextView tvBTInput;
	TextView tvJGInput;
	TextView tvYBInput;
	TextView tvMHInput;
	TextView tvGHInput;
	TextView tvPRInput;
	TextView tvJBInput;
	TextView tvJSInput;


	Button btnStatus;
	Button btnEditStatus;
	Button btnFacBT;
	Button btnFacJG;
	Button btnFacYB;
	Button btnFacMH;
	Button btnFacGH;
	Button btnFacPR;
	Button btnFacJB;
	Button btnFacJS;

	Button btnFacBTCamera;
	Button btnFacJGCamera;
	Button btnFacYBCamera;
	Button btnFacMHCamera;
	Button btnFacGHCamera;
	Button btnFacPRCamera;
	Button btnFacJBCamera;
	Button btnFacJSCamera;
	
	ImageView ivBTLine;
	ImageView ivJGLine;
	ImageView ivYBLine;
	ImageView ivMHLine;
	ImageView ivGHLine;
	ImageView ivPRLine;
	ImageView ivJBLine;
	ImageView ivJSLine;
	
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
		layoutJG = (FrameLayout)findViewById(R.id.layout_jg);
		layoutYB = (FrameLayout)findViewById(R.id.layout_yb);
		layoutMH = (FrameLayout)findViewById(R.id.layout_mh);
		layoutGH = (FrameLayout)findViewById(R.id.layout_gh);
		layoutPR = (FrameLayout)findViewById(R.id.layout_pr);
		layoutJB = (FrameLayout)findViewById(R.id.layout_jb);
		layoutJS = (FrameLayout)findViewById(R.id.layout_js);

		
		tvBTInput = (TextView)findViewById(R.id.tvBTInput);
		tvJGInput = (TextView)findViewById(R.id.tvJGInput);
		tvYBInput = (TextView)findViewById(R.id.tvYBInput);
		tvMHInput = (TextView)findViewById(R.id.tvMHInput);
		tvGHInput = (TextView)findViewById(R.id.tvGHInput);
		tvPRInput = (TextView)findViewById(R.id.tvPRInput);
		tvJBInput = (TextView)findViewById(R.id.tvJBInput);
		tvJSInput = (TextView)findViewById(R.id.tvJSInput);


	//	tvBTInput.setText(CodeInfo.getInstance(this).getValue(ConstVALUE.CODE_TYPE_INS_TYPE, ConstVALUE.CODE_NO_INSPECT_BT));
		tvJGInput.setText(CodeInfo.getInstance(this).getValue(ConstVALUE.CODE_TYPE_INS_TYPE, ConstVALUE.CODE_NO_INSPECT_JG));
		tvYBInput.setText(CodeInfo.getInstance(this).getValue(ConstVALUE.CODE_TYPE_INS_TYPE, ConstVALUE.CODE_NO_INSPECT_YB));
		tvMHInput.setText(CodeInfo.getInstance(this).getValue(ConstVALUE.CODE_TYPE_INS_TYPE, ConstVALUE.CODE_NO_INSPECT_MH));
		tvGHInput.setText(CodeInfo.getInstance(this).getValue(ConstVALUE.CODE_TYPE_INS_TYPE, ConstVALUE.CODE_NO_INSPECT_GH));
		tvPRInput.setText(CodeInfo.getInstance(this).getValue(ConstVALUE.CODE_TYPE_INS_TYPE, ConstVALUE.CODE_NO_INSPECT_PR));
		tvJBInput.setText(CodeInfo.getInstance(this).getValue(ConstVALUE.CODE_TYPE_INS_TYPE, ConstVALUE.CODE_NO_INSPECT_JB));
		tvJSInput.setText(CodeInfo.getInstance(this).getValue(ConstVALUE.CODE_TYPE_INS_TYPE, ConstVALUE.CODE_NO_INSPECT_JS));
		
		btnStatus = (Button)findViewById(R.id.btnStatus);
		btnEditStatus = (Button)findViewById(R.id.btnEditStatus);
		btnFacBT = (Button)findViewById(R.id.btnFacBT);
		btnFacJG = (Button)findViewById(R.id.btnFacJG);
		btnFacYB = (Button)findViewById(R.id.btnFacYB);
		btnFacMH = (Button)findViewById(R.id.btnFacMH);
		btnFacGH = (Button)findViewById(R.id.btnFacGH);
		btnFacPR = (Button)findViewById(R.id.btnFacPR);
		btnFacJB = (Button)findViewById(R.id.btnFacJB);
		btnFacJS = (Button)findViewById(R.id.btnFacJS);


		btnFacBTCamera = (Button)findViewById(R.id.btnFacBTCamera);
		btnFacJGCamera = (Button)findViewById(R.id.btnFacJGCamera);
		btnFacYBCamera = (Button)findViewById(R.id.btnFacYBCamera);
		btnFacMHCamera = (Button)findViewById(R.id.btnFacMHCamera);
		btnFacGHCamera = (Button)findViewById(R.id.btnFacGHCamera);
		btnFacPRCamera = (Button)findViewById(R.id.btnFacPRCamera);
		btnFacJBCamera = (Button)findViewById(R.id.btnFacJBCamera);
		btnFacJSCamera = (Button)findViewById(R.id.btnFacJSCamera);
		
		ivBTLine = (ImageView)findViewById(R.id.ivBTLine);
		ivJGLine = (ImageView)findViewById(R.id.ivJGLine);
		ivYBLine = (ImageView)findViewById(R.id.ivYBLine);
		ivMHLine = (ImageView)findViewById(R.id.ivMHLine);
		ivGHLine = (ImageView)findViewById(R.id.ivGHLine);
		ivPRLine = (ImageView)findViewById(R.id.ivPRLine);
		ivJBLine = (ImageView)findViewById(R.id.ivJBLine);
		ivJSLine = (ImageView)findViewById(R.id.ivJSLine);
		
//		btnFacBTCamera.setVisibility(View.GONE);
//		btnFacJSCamera.setVisibility(View.GONE);
//		btnFacJJCamera.setVisibility(View.GONE);
//		btnFacKBCamera.setVisibility(View.GONE);
//		btnFacHKCamera.setVisibility(View.GONE);
		
		btnStatus.setOnClickListener(this);
		btnEditStatus.setOnClickListener(this);
		btnFacBT.setOnClickListener(this);
		btnFacJG.setOnClickListener(this);
		btnFacYB.setOnClickListener(this);
		btnFacMH.setOnClickListener(this);
		btnFacGH.setOnClickListener(this);
		btnFacPR.setOnClickListener(this);
		btnFacJB.setOnClickListener(this);
		btnFacJS.setOnClickListener(this);
		
		btnFacBTCamera.setOnClickListener(this);
		btnFacJGCamera.setOnClickListener(this);
		btnFacYBCamera.setOnClickListener(this);
		btnFacMHCamera.setOnClickListener(this);
		btnFacGHCamera.setOnClickListener(this);
		btnFacPRCamera.setOnClickListener(this);
		btnFacJBCamera.setOnClickListener(this);
		btnFacJSCamera.setOnClickListener(this);
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
			/*if (mSelectedInfo.type.equals(ConstVALUE.CODE_NO_INSPECT_BT)) {
				btnFacBT.setTag(mSelectedInfo);
				btnFacBTCamera.setTag(mSelectedInfo);
				layoutBT.setVisibility(View.VISIBLE);
				ivBTLine.setVisibility(View.VISIBLE);
				InputBTDao inputBTDao = InputBTDao.getInstance(this);
				if (!inputBTDao.existBT(mSelectedInfo.master_idx))
					btnFacBT.setBackgroundResource(R.drawable.selector_input_bt_insert);
				else
					btnFacBT.setBackgroundResource(R.drawable.selector_input_bt_edit);
			} else */
			if (mSelectedInfo.type.equals(ConstVALUE.CODE_NO_INSPECT_JG)) {
				btnFacJG.setTag(mSelectedInfo);
				btnFacJGCamera.setTag(mSelectedInfo);
				layoutJG.setVisibility(View.VISIBLE);
				ivJGLine.setVisibility(View.VISIBLE);
				InputJGDao inputJGDao = InputJGDao.getInstance(this);
				if (!inputJGDao.existJG(mSelectedInfo.master_idx))
					btnFacJG.setBackgroundResource(R.drawable.selector_input_bt_insert);
				else
					btnFacJG.setBackgroundResource(R.drawable.selector_input_bt_edit);
			} else if (mSelectedInfo.type.equals(ConstVALUE.CODE_NO_INSPECT_YB)) {
				btnFacYB.setTag(mSelectedInfo);
				btnFacYBCamera.setTag(mSelectedInfo);
				layoutYB.setVisibility(View.VISIBLE);
				ivYBLine.setVisibility(View.VISIBLE);
				InputJSDao inputJSDao = InputJSDao.getInstance(this);
				if (!inputJSDao.existJS(mSelectedInfo.master_idx))
					btnFacYB.setBackgroundResource(R.drawable.selector_input_bt_insert);
				else
					btnFacYB.setBackgroundResource(R.drawable.selector_input_bt_edit);
			}  else if (mSelectedInfo.type.equals(ConstVALUE.CODE_NO_INSPECT_MH)) {
				btnFacMH.setTag(mSelectedInfo);
				btnFacMHCamera.setTag(mSelectedInfo);
				layoutMH.setVisibility(View.VISIBLE);
				ivMHLine.setVisibility(View.VISIBLE);
				InputMHDao inputMHDao = InputMHDao.getInstance(this);
				if (!inputMHDao.existMH(mSelectedInfo.master_idx))
					btnFacMH.setBackgroundResource(R.drawable.selector_input_bt_insert);
				else
					btnFacMH.setBackgroundResource(R.drawable.selector_input_bt_edit);
			}  else if (mSelectedInfo.type.equals(ConstVALUE.CODE_NO_INSPECT_GH)) {
				btnFacGH.setTag(mSelectedInfo);
				btnFacGHCamera.setTag(mSelectedInfo);
				layoutGH.setVisibility(View.VISIBLE);
				ivGHLine.setVisibility(View.VISIBLE);
				InputGHDao inputGHDao = InputGHDao.getInstance(this);
				if (!inputGHDao.existGH(mSelectedInfo.master_idx))
					btnFacGH.setBackgroundResource(R.drawable.selector_input_bt_insert);
				else
					btnFacGH.setBackgroundResource(R.drawable.selector_input_bt_edit);
			}  else if (mSelectedInfo.type.equals(ConstVALUE.CODE_NO_INSPECT_PR)) {
				btnFacPR.setTag(mSelectedInfo);
				btnFacPRCamera.setTag(mSelectedInfo);
				layoutPR.setVisibility(View.VISIBLE);
				ivPRLine.setVisibility(View.VISIBLE);
				InputPRDao inputPRDao = InputPRDao.getInstance(this);
				if (!inputPRDao.existPR(mSelectedInfo.master_idx))
					btnFacPR.setBackgroundResource(R.drawable.selector_input_bt_insert);
				else
					btnFacPR.setBackgroundResource(R.drawable.selector_input_bt_edit);
			}  else if (mSelectedInfo.type.equals(ConstVALUE.CODE_NO_INSPECT_MH)) {
				btnFacMH.setTag(mSelectedInfo);
				btnFacMHCamera.setTag(mSelectedInfo);
				layoutMH.setVisibility(View.VISIBLE);
				ivMHLine.setVisibility(View.VISIBLE);
				InputMHDao inputMHDao = InputMHDao.getInstance(this);
				if (!inputMHDao.existMH(mSelectedInfo.master_idx))
					btnFacMH.setBackgroundResource(R.drawable.selector_input_bt_insert);
				else
					btnFacMH.setBackgroundResource(R.drawable.selector_input_bt_edit);
			}  else if (mSelectedInfo.type.equals(ConstVALUE.CODE_NO_INSPECT_JB)) {
				btnFacJB.setTag(mSelectedInfo);
				btnFacJBCamera.setTag(mSelectedInfo);
				layoutJB.setVisibility(View.VISIBLE);
				ivJBLine.setVisibility(View.VISIBLE);
				InputJBDao inputJBDao = InputJBDao.getInstance(this);
				if (!inputJBDao.existJB(mSelectedInfo.master_idx))
					btnFacJB.setBackgroundResource(R.drawable.selector_input_bt_insert);
				else
					btnFacJB.setBackgroundResource(R.drawable.selector_input_bt_edit);
			}else if (mSelectedInfo.type.equals(ConstVALUE.CODE_NO_INSPECT_JS)) {
				btnFacJS.setTag(mSelectedInfo);
				btnFacJSCamera.setTag(mSelectedInfo);
				layoutJS.setVisibility(View.VISIBLE);
				ivJSLine.setVisibility(View.VISIBLE);
				InputJSDao inputJSDao = InputJSDao.getInstance(this);
				if (!inputJSDao.existJS(mSelectedInfo.master_idx))
					btnFacJB.setBackgroundResource(R.drawable.selector_input_bt_insert);
				else
					btnFacJB.setBackgroundResource(R.drawable.selector_input_bt_edit);
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
		layoutJG.setVisibility(View.GONE);
		layoutYB.setVisibility(View.GONE);
		layoutMH.setVisibility(View.GONE);
		layoutGH.setVisibility(View.GONE);
		layoutPR.setVisibility(View.GONE);
		layoutJB.setVisibility(View.GONE);
		layoutJS.setVisibility(View.GONE);

		
		ivBTLine.setVisibility(View.GONE);
		ivJGLine.setVisibility(View.GONE);
		ivYBLine.setVisibility(View.GONE);
		ivMHLine.setVisibility(View.GONE);
		ivGHLine.setVisibility(View.GONE);
		ivPRLine.setVisibility(View.GONE);
		ivJBLine.setVisibility(View.GONE);
		ivJSLine.setVisibility(View.GONE);
	}

	@Override
	public void onClick(View v) {
		Intent intent;
		switch (v.getId()) {
		case R.id.btnFacBT:
			//goInputActivity(v, InputBTActivity.class);
			//goInputActivity(v, InputJGActivity.class);
			break;
		case R.id.btnFacJG:
			goInputActivity(v, InputJGActivity.class);
			break;
		case R.id.btnFacYB:
			goInputActivity(v, InputYBActivity.class);
			break;
		case R.id.btnFacMH:
			goInputActivity(v, InputMHActivity.class);
			break;
		case R.id.btnFacGH:
			goInputActivity(v, InputGHActivity.class);
			break;
		case R.id.btnFacPR:
			goInputActivity(v, InputPRActivity.class);
			break;
		case R.id.btnFacJB:
			goInputActivity(v, InputJBActivity.class);
			break;
		case R.id.btnFacJS:
			goInputActivity(v, InputJSActivity.class);
		break;
		case R.id.btnFacBTCamera:
			goCameraManage(v, "보통순시");
			break;
		case R.id.btnFacJGCamera:
			goCameraManage(v, "정시순시");
			break;
		case R.id.btnFacYBCamera:
			goCameraManage(v, "예방순시");
			break;
		case R.id.btnFacMHCamera:
			goCameraManage(v, "맨홀점검");
			break;
		case R.id.btnFacGHCamera:
			goCameraManage(v, "경보회로점검");
			break;
		case R.id.btnFacPRCamera:
			goCameraManage(v, "피뢰기점검");
			break;
		case R.id.btnFacJBCamera:
			goCameraManage(v, "전력구소방시설 보통점검");
			break;
		case R.id.btnFacJSCamera:
			goCameraManage(v, "전선접속개소점검");
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

