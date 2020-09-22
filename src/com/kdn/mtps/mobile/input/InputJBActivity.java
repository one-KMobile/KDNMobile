package com.kdn.mtps.mobile.input;

import android.app.AlertDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.TimePicker;

import com.kdn.mtps.mobile.BaseActivity;
import com.kdn.mtps.mobile.MainActivity;
import com.kdn.mtps.mobile.R;
import com.kdn.mtps.mobile.TitleManager;
import com.kdn.mtps.mobile.camera.CameraManageActivity;
import com.kdn.mtps.mobile.constant.ConstSP;
import com.kdn.mtps.mobile.constant.ConstVALUE;
import com.kdn.mtps.mobile.db.InputJBDao;
import com.kdn.mtps.mobile.db.InputMHBSubInfoDao;
import com.kdn.mtps.mobile.db.InputMHDao;
import com.kdn.mtps.mobile.db.InputMHGSubInfoDao;
import com.kdn.mtps.mobile.db.InputMHISubInfoDao;
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

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.LinkedHashMap;
import java.util.Map.Entry;

public class InputJBActivity extends BaseActivity implements TitleManager, OnClickListener {

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
	Button btnTab1;
	Button btnTab2;
	Button btnTab3;
	Button btnClaimContent;
	Button btnStartT;

	ImageView ivComplte;

	LinearLayout llLayout;
	LinearLayout llLayout2;
	LinearLayout llLayout3;

	ArrayList<JBInfo> jbInfoList1;
	ArrayList<JBInfo> jbInfoList2;
	ArrayList<JBInfo> jbInfoList3;
	JBInfo selectjbInfo;
	LinearLayout linearDateList;

	FrameLayout layout1;
	FrameLayout layout2;
	FrameLayout layout3;

	InspectInfo mInfo;

	int selectedWeatherNo = 0;

	public LinkedHashMap<Integer, ViewHolder> jbsList = new LinkedHashMap<Integer, ViewHolder>();
	public LinkedHashMap<Integer, ViewHolder> jbjList = new LinkedHashMap<Integer, ViewHolder>();
	public LinkedHashMap<Integer, ViewHolder> jbyList = new LinkedHashMap<Integer, ViewHolder>();

	int selectedClaimContentNo = 0;

	final String strWeatherItems[] = CodeInfo.getInstance(this).getNames(ConstVALUE.CODE_TYPE_WEATHER);
	final String strClainContentItems[] = CodeInfo.getInstance(this).getNames(ConstVALUE.CODE_TYPE_GOOD_SECD);
	final String strYBItems[] = CodeInfo.getInstance(this).getNames(ConstVALUE.CODE_TYPE_GOOD_SECD);

	boolean isAdd;
	String strCurrentDate;
	LocationManager locationManager;

	int sHour;
	int sMin;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_input_jb);

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


		btnClaimContent = (Button)findViewById(R.id.btnClaimContent);
		btnClaimContent.setOnClickListener(this);
		btnAdd = (Button)findViewById(R.id.btnAdd);
		btnAdd.setOnClickListener(this);
		btnEdit = (Button)findViewById(R.id.btnEdit);
		btnDelete = (Button)findViewById(R.id.btnDelete);
		btnEdit.setOnClickListener(this);
		btnDelete.setOnClickListener(this);
		btnCamera = (Button)findViewById(R.id.btnCamera);
		btnCamera.setOnClickListener(this);

		btnTab1 = (Button)findViewById(R.id.btnTab1);
		btnTab1.setTextColor(Color.BLACK);
		btnTab1.setOnClickListener(this);
		btnTab1.setBackgroundResource(R.drawable.tab_jb_1_on);
		btnTab2 = (Button)findViewById(R.id.btnTab2);
		btnTab2.setTextColor(Color.BLACK);
		btnTab2.setOnClickListener(this);
		btnTab2.setBackgroundResource(R.drawable.tab_jb_2_off);
		btnTab3 = (Button)findViewById(R.id.btnTab3);
		btnTab3.setTextColor(Color.BLACK);
		btnTab3.setOnClickListener(this);
		btnTab3.setBackgroundResource(R.drawable.tab_jb_3_off);

		llLayout = (LinearLayout)findViewById(R.id.llLayout);
		llLayout2 = (LinearLayout)findViewById(R.id.llLayout2);
		llLayout3 = (LinearLayout)findViewById(R.id.llLayout3);

		ivComplte = (ImageView)findViewById(R.id.ivComplte);


		linearDateList = (LinearLayout)findViewById(R.id.linearDateList);

		layout1 = (FrameLayout)findViewById(R.id.layout_1);
		layout2 = (FrameLayout)findViewById(R.id.layout_2);
		layout3 = (FrameLayout)findViewById(R.id.layout_3);
	}

	public void setData() {

		tvName.setText(mInfo.tracksName);
		tvDate.setText(StringUtil.printDate(mInfo.date));
		tvEqpNm.setText(mInfo.eqpNm);

		String insType = CodeInfo.getInstance(this).getValue(ConstVALUE.CODE_TYPE_INS_TYPE, mInfo.type);
		tvType.setText(insType);

		InputJBDao inputJBDao = InputJBDao.getInstance(this);
		ArrayList<JBInfo> list = inputJBDao.selectInspectNoList("1");
		ArrayList<JBInfo> list2 = inputJBDao.selectInspectNoList("2");
		ArrayList<JBInfo> list3 = inputJBDao.selectInspectNoList("3");

		Logg.d("sub info - eqpNo : " + mInfo.eqpNo);
		if (list.isEmpty()) {
			btnAdd.setEnabled(false);
		}

		for (JBInfo info : list) {
			Logg.d("sub info : " + info.inspect_no);
		}

		for (JBInfo info : list2) {
			Logg.d("sub info2 : " + info.inspect_no);
		}

		for (JBInfo info : list3) {
			Logg.d("sub info3 : " + info.inspect_no);
		}

		if (isAdd) {
			ivComplte.setBackgroundResource(R.drawable.input_js_add);

			strWeather = Shared.getString(this, ConstSP.SETTING_WEATHER);

			btnClaimContent.setText(strClainContentItems[0]);

			for (int i=0; i<list.size(); i++) {
				addItem(i, list.get(i));
			}
			for (int i=0; i<list2.size(); i++) {
				addItem2(i, list2.get(i));
			}
			for (int i=0; i<list3.size(); i++) {
				addItem3(i, list3.get(i));
			}

			addBottomPadding();
		} else {
			ivComplte.setBackgroundResource(R.drawable.input_js_edit);

			jbInfoList1 = inputJBDao.selectJBS(mInfo.master_idx);
			jbInfoList2 = inputJBDao.selectJBJ(mInfo.master_idx);
			jbInfoList3 = inputJBDao.selectJBY(mInfo.master_idx);

			selectjbInfo = jbInfoList1.get(0);

			strWeather = CodeInfo.getInstance(this).getValue(ConstVALUE.CODE_TYPE_WEATHER, selectjbInfo.weather);
			selectedWeatherNo = StringUtil.getIndex(strWeatherItems, strWeather);

			String strClaimContent = CodeInfo.getInstance(this).getValue(ConstVALUE.CODE_TYPE_GOOD_SECD, selectjbInfo.claim_content);
			btnClaimContent.setText(strClaimContent);
			selectedClaimContentNo = StringUtil.getIndex(strClainContentItems, strClaimContent);

			for (int i=0; i<jbInfoList1.size(); i++) {
				Logg.d("jbInfoList1 !!! : " + jbInfoList1.get(i).circuit_name);
				addItem(i, jbInfoList1.get(i));
			}
			for (int i=0; i<jbInfoList2.size(); i++) {
				Logg.d("jbInfoList2 !!! : " + jbInfoList2.get(i).circuit_name);
				addItem2(i, jbInfoList2.get(i));
			}
			for (int i=0; i<jbInfoList3.size(); i++) {
				Logg.d("mhInfoList3 !!! : " + jbInfoList3.get(i).circuit_name);
				addItem3(i, jbInfoList3.get(i));
			}

			addBottomPadding();
		}

//		if (mInfo.send_yn_js != null && !"".equals(mInfo.send_yn_js)) {
//			ivComplte.setBackgroundResource(R.drawable.input_js_send);
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
			btnHeaderTitle.setText("전력구소방시설 "+insType + " 등록");
		else
			btnHeaderTitle.setText("전력구소방시설 "+insType + " 수정/삭제");

		btnHeaderLeft.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});

		btnHeaderRight.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				AppUtil.startActivity(InputJBActivity.this, new Intent(InputJBActivity.this, MainActivity.class));
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
			InputMHDao inputMHDao = InputMHDao.getInstance(this);
			inputMHDao.Delete(mInfo.master_idx);
			InspectResultMasterDao insRetDao = InspectResultMasterDao.getInstance(this);
			insRetDao.updateComplete(mInfo.master_idx, "N", ConstVALUE.CODE_NO_INSPECT_JS);
			setResult();
			finish();
			break;
		case R.id.btnCamera:
			Intent intent = new Intent(InputJBActivity.this, CameraManageActivity.class);
			intent.putExtra("inspect", mInfo);
			startActivity(intent);
			break;
		case R.id.btnTab1:
			setLayout("1");
			break;
		case R.id.btnTab2:
			setLayout("2");
			break;
		case R.id.btnTab3:
			setLayout("3");
			break;
		case R.id.btnStartS:
			Logg.d("########### btnStartS : ");
			TimePickerDialog dialog = new TimePickerDialog(this, tlistener, 15, 24, false);
			dialog.show();
			break;
		case R.id.btnClaimContent:
			showClaimContentDialog();
			break;
		default:
			break;
		}
	}

	private TimePickerDialog.OnTimeSetListener tlistener = new TimePickerDialog.OnTimeSetListener() {

		public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
			btnStartT.setText(hourOfDay + "시 " + minute + "분");
		}
	};

	public void showClaimContentDialog() {

		AlertDialog.Builder dialog = new AlertDialog.Builder(this, AlertDialog.THEME_DEVICE_DEFAULT_LIGHT);
		dialog.setTitle(getString(R.string.input_bt_claim_content));
		dialog.setSingleChoiceItems(strClainContentItems, selectedClaimContentNo, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				selectedClaimContentNo = which;
				btnClaimContent.setText(strClainContentItems[which]);
				dialog.dismiss();
			}
		}).show();
	}

	public void setInputType() {
		InputJBDao inputJBDao = InputJBDao.getInstance(this);
		if (inputJBDao.existJB(mInfo.master_idx))
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
		boolean isEmpty = true;
		Log.i("MyTag","############### btnClaimContent update : "+ btnClaimContent);
		String strClaimContent = btnClaimContent.getText().toString();
		if("".equals(strClaimContent)) {
			ToastUtil.show(this, "종합판정을 입력해 주시기 바랍니다.");
			return;
		}
		/*
		for (Entry<Integer, ViewHolder> entry : mhiList.entrySet()) {
			ViewHolder viewHolder = entry.getValue();
			String strc1 = viewHolder.t1_editc1.getText().toString();
			String strc2= viewHolder.t1_editc2.getText().toString();
			String strc3 = viewHolder.t1_editc3.getText().toString();
			String strc4= viewHolder.t1_editc4.getText().toString();

			if (!"".equals(strc1) || !"".equals(strc2) || !"".equals(strc3) || !"".equals(strc4) ) {
				isEmpty = false;
				break;
			}
		}

		for (Entry<Integer, ViewHolder> entry : mhbList.entrySet()) {
			ViewHolder viewHolder = entry.getValue();
			String strc1 = viewHolder.t2_editc1.getText().toString();
			String strc2= viewHolder.t2_editc2.getText().toString();
			String strc3 = viewHolder.t2_editc3.getText().toString();

			if (!"".equals(strc1) || !"".equals(strc2) || !"".equals(strc3) ) {
				isEmpty = false;
				break;
			}
		}

		for (Entry<Integer, ViewHolder> entry : mhgList.entrySet()) {
			ViewHolder viewHolder = entry.getValue();
			String strc1 = viewHolder.t2_editc1.getText().toString();
			String strc2= viewHolder.t2_editc2.getText().toString();
			String strc3 = viewHolder.t2_editc3.getText().toString();

			if (!"".equals(strc1) || !"".equals(strc2) || !"".equals(strc3) ) {
				isEmpty = false;
				break;
			}
		}
		*/
		//if (isEmpty) {
		//	ToastUtil.show(this, "정보를 입력해 주시기 바랍니다.");
		//	return;
		//}

		InputJBDao inputJBDao = InputJBDao.getInstance(this);
		inputJBDao.Delete(mInfo.master_idx);

		for (Entry<Integer, ViewHolder> entry : jbsList.entrySet()) {
			ViewHolder viewHolder = entry.getValue();
			Logg.d("1. jbsList !!!! : " + viewHolder.tvCircuitName.getText().toString());
			String strCircuitName = viewHolder.tvCircuitName.getText().toString();
			String strTNo = viewHolder.tvT1No.getText().toString();
			String str_c1 = viewHolder.t1_c.getText().toString();

			JBInfo log = new JBInfo();
			log.master_idx = mInfo.master_idx;
			log.weather = CodeInfo.getInstance(this).getKey(ConstVALUE.CODE_TYPE_WEATHER, strWeather);
			log.circuit_name = strCircuitName;
			log.claim_content = CodeInfo.getInstance(this).getKey(ConstVALUE.CODE_TYPE_GOOD_SECD, strClaimContent);
			log.t_gubun = "1";
			log.t1_no = strTNo;
			log.t1_c = CodeInfo.getInstance(this).getKey(ConstVALUE.CODE_TYPE_GOOD_SECD, str_c1);

			if (isEdit) {
				inputJBDao.Append(log, viewHolder.jbInfo.idx);
			} else {
				inputJBDao.Append(log);
			}
		}

		for (Entry<Integer, ViewHolder> entry : jbjList.entrySet()) {
			ViewHolder viewHolder = entry.getValue();
			Logg.d("2. jbjList !!!! : " + viewHolder.tvCircuitName.getText().toString());
			String strCircuitName = viewHolder.tvCircuitName.getText().toString();
			String strTNo = viewHolder.tvT2No.getText().toString();
			String str_c1 = viewHolder.t2_c.getText().toString();

			JBInfo log = new JBInfo();
			log.master_idx = mInfo.master_idx;
			log.weather = CodeInfo.getInstance(this).getKey(ConstVALUE.CODE_TYPE_WEATHER, strWeather);
			log.circuit_name = strCircuitName;
			log.claim_content = CodeInfo.getInstance(this).getKey(ConstVALUE.CODE_TYPE_GOOD_SECD, strClaimContent);
			log.t_gubun = "2";
			log.t2_no = strTNo;
			log.t2_c = CodeInfo.getInstance(this).getKey(ConstVALUE.CODE_TYPE_GOOD_SECD, str_c1);

			if (isEdit) {
				inputJBDao.Append(log, viewHolder.jbInfo.idx);
			} else {
				inputJBDao.Append(log);
			}
		}

		for (Entry<Integer, ViewHolder> entry : jbyList.entrySet()) {
			ViewHolder viewHolder = entry.getValue();
			Logg.d("3. jbyList !!!! : " + viewHolder.tvCircuitName.getText().toString());
			String strCircuitName = viewHolder.tvCircuitName.getText().toString();
			String strTNo = viewHolder.tvT3No.getText().toString();
			String str_c1 = viewHolder.t3_c.getText().toString();

			JBInfo log = new JBInfo();
			log.master_idx = mInfo.master_idx;
			log.weather = CodeInfo.getInstance(this).getKey(ConstVALUE.CODE_TYPE_WEATHER, strWeather);
			log.circuit_name = strCircuitName;
			log.claim_content = CodeInfo.getInstance(this).getKey(ConstVALUE.CODE_TYPE_GOOD_SECD, strClaimContent);
			log.t_gubun = "3";
			log.t3_no = strTNo;
			log.t3_c = CodeInfo.getInstance(this).getKey(ConstVALUE.CODE_TYPE_GOOD_SECD, str_c1);

			if (isEdit) {
				inputJBDao.Append(log, viewHolder.jbInfo.idx);
			} else {
				inputJBDao.Append(log);
			}
		}

		if (!isEdit)
			LocManager.getInstance(this).startGetLocation(listener);

		InspectResultMasterDao insRetDao = InspectResultMasterDao.getInstance(this);
		insRetDao.updateComplete(mInfo.master_idx, "Y", ConstVALUE.CODE_NO_INSPECT_JS);

		setResult(RESULT_OK);

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

			LocManager.getInstance(InputJBActivity.this).setGetGps(true);

			String currentLocation = String.format("위도:%f  경도:%f  고도:%f", location.getLatitude(), location.getLongitude(), location.getAltitude());
			Logg.d(InputJBActivity.this, "currentLocation : " + currentLocation);
            Calendar cal = new GregorianCalendar();
            String now = String.format("%d년 %d월 %d일 %d시 %d분 %d초", cal.get(Calendar.YEAR), cal.get(Calendar.MONTH)+1,
					cal.get(Calendar.DAY_OF_MONTH), cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE),
					cal.get(Calendar.SECOND));
            Logg.d(InputJBActivity.this, "갱신 시간 : " + now);

            InspectResultMasterDao insRetDao = InspectResultMasterDao.getInstance(InputJBActivity.this);
            insRetDao.updateNfcTagLocation(mInfo.master_idx, "", location.getLatitude()+"", location.getLongitude()+"", false);

            if (locationManager != null) {
            	locationManager.removeUpdates(listener);
            }
		}
	};

	/*소화기 리스트*/
	public void addItem(int idx, JBInfo jbInfo) {

		final ViewHolder viewHolder = new ViewHolder();
		View view = LayoutInflater.from(this).inflate(R.layout.item_jb_s, null);
		LinearLayout llItemParent = (LinearLayout) view.findViewById(R.id.llItemParent);

		UIUtil.setFont(this, (ViewGroup)llItemParent);

		llItemParent.setOnTouchListener(hideKeyboardListener);

		viewHolder.tvCircuitName = (TextView) view.findViewById(R.id.tvCircuitName);
		viewHolder.tvInspectNo = (TextView) view.findViewById(R.id.tvInspectNo);
		viewHolder.tvT1No = (TextView) view.findViewById(R.id.tvT1No);
		viewHolder.t1_c  = (Button)view.findViewById(R.id.btnT1C);
		viewHolder.t1_c.setId(0);
		viewHolder.t1_c.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				showTCDialog((Button)v);
			}
		});

		viewHolder.t1_c.setTag(1);

		if (jbInfo != null) {
			viewHolder.jbInfo = jbInfo;
			viewHolder.tvCircuitName.setText(tvEqpNm.getText());
			viewHolder.tvInspectNo.setText(jbInfo.inspect_no);
			viewHolder.tvT1No.setText(jbInfo.t_no);
			if(jbInfo.t1_c=="") {
				viewHolder.t1_c.setText("");
			} else {
				String strt1_c = CodeInfo.getInstance(this).getValue(ConstVALUE.CODE_TYPE_GOOD_SECD, jbInfo.t1_c);
				viewHolder.t1_c.setText(strt1_c);
				viewHolder.t1_c.setId(StringUtil.getIndex(strYBItems, strt1_c));
			}

		}

		jbsList.put(idx, viewHolder);
		llLayout.addView(view);
	}

	public class ViewHolder {
		TextView tvCircuitName;
		TextView tvCurrentLoad;
		TextView tvInspectNo;
		TextView tvT1No;
		Button t1_c;
		TextView tvT2No;
		Button t2_c;
		TextView tvT3No;
		Button t3_c;
        JBInfo jbInfo;
	}

	/*자동화재탐지설비/자동화재속보설비/비상경보설비 리스트*/
	public void addItem2(int idx, JBInfo jbInfo) {

		final ViewHolder viewHolder = new ViewHolder();
		View view = LayoutInflater.from(this).inflate(R.layout.item_jb_j, null);
		LinearLayout llItemParent = (LinearLayout) view.findViewById(R.id.llItemParent);

		UIUtil.setFont(this, (ViewGroup)llItemParent);

		llItemParent.setOnTouchListener(hideKeyboardListener);

		viewHolder.tvCircuitName = (TextView) view.findViewById(R.id.tvCircuitName);
		viewHolder.tvInspectNo = (TextView) view.findViewById(R.id.tvInspectNo);
		viewHolder.tvT2No = (TextView) view.findViewById(R.id.tvT2No);
		viewHolder.t2_c  = (Button)view.findViewById(R.id.btnT2C);
		viewHolder.t2_c.setId(0);
		viewHolder.t2_c.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				showTCDialog((Button)v);
			}
		});

		viewHolder.t2_c.setTag(0);

		if (jbInfo != null) {
			viewHolder.jbInfo = jbInfo;
			viewHolder.tvCircuitName.setText(tvEqpNm.getText());
			viewHolder.tvInspectNo.setText(jbInfo.inspect_no);
			viewHolder.tvT2No.setText(jbInfo.t_no);
			if(jbInfo.t2_c=="") {
				viewHolder.t2_c.setText("");
			} else {
				String strt1_c = CodeInfo.getInstance(this).getValue(ConstVALUE.CODE_TYPE_GOOD_SECD, jbInfo.t2_c);
				viewHolder.t2_c.setText(strt1_c);
				viewHolder.t2_c.setId(StringUtil.getIndex(strYBItems, strt1_c));
			}
		}

		jbjList.put(idx, viewHolder);
		llLayout2.addView(view);

	}

	/*가스탐지기록 리스트*/
	public void addItem3(int idx, JBInfo jbInfo) {

		final ViewHolder viewHolder = new ViewHolder();
		View view = LayoutInflater.from(this).inflate(R.layout.item_jb_y, null);
		LinearLayout llItemParent = (LinearLayout) view.findViewById(R.id.llItemParent);

		UIUtil.setFont(this, (ViewGroup)llItemParent);

		llItemParent.setOnTouchListener(hideKeyboardListener);

		viewHolder.tvCircuitName = (TextView) view.findViewById(R.id.tvCircuitName);
		viewHolder.tvInspectNo = (TextView) view.findViewById(R.id.tvInspectNo);
		viewHolder.tvT3No = (TextView) view.findViewById(R.id.tvT3No);
		viewHolder.t3_c  = (Button)view.findViewById(R.id.btnT3C);
		viewHolder.t3_c.setId(0);
		viewHolder.t3_c.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				showTCDialog((Button)v);
			}
		});

		viewHolder.t3_c.setTag(0);

		if (jbInfo != null) {
			viewHolder.jbInfo = jbInfo;
			viewHolder.tvCircuitName.setText(tvEqpNm.getText());
			viewHolder.tvInspectNo.setText(jbInfo.inspect_no);
			viewHolder.tvT3No.setText(jbInfo.t_no);
			if(jbInfo.t3_c=="") {
				viewHolder.t3_c.setText("");
			} else {
				String strt1_c = CodeInfo.getInstance(this).getValue(ConstVALUE.CODE_TYPE_GOOD_SECD, jbInfo.t3_c);
				viewHolder.t3_c.setText(strt1_c);
				viewHolder.t3_c.setId(StringUtil.getIndex(strYBItems, strt1_c));
			}
		}

		jbyList.put(idx, viewHolder);
		llLayout3.addView(view);

	}

	public void setDiff(String str1, String str2, TextView tv) {
		try {
			tv.setText("");

			if (str1 == null && str2 == null)
				return;

			double ret = 0;
			if (!"".equals(str1) && !"".equals(str2)) {
				ret = Double.valueOf(str1) - Double.valueOf(str2);
				ret = Math.abs(ret);
				tv.setText(ret+"");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	OnTouchListener hideKeyboardListener = new OnTouchListener() {
		@Override
		public boolean onTouch(View v, MotionEvent event) {
			UIUtil.hideKeyboard(llParent);
			return false;
		}
	};

	public void setLayout(String strTab) {
		if(strTab == "1") {
			layout1.setVisibility(View.VISIBLE);
			layout2.setVisibility(View.GONE);
			layout3.setVisibility(View.GONE);
			btnTab1.setBackgroundResource(R.drawable.tab_jb_1_on);
			btnTab2.setBackgroundResource(R.drawable.tab_jb_2_off);
			btnTab3.setBackgroundResource(R.drawable.tab_jb_3_off);
		} else if(strTab == "2") {
			layout1.setVisibility(View.GONE);
			layout2.setVisibility(View.VISIBLE);
			layout3.setVisibility(View.GONE);
			btnTab1.setBackgroundResource(R.drawable.tab_jb_1_off);
			btnTab2.setBackgroundResource(R.drawable.tab_jb_2_on);
			btnTab3.setBackgroundResource(R.drawable.tab_jb_3_off);
		} else {
			layout1.setVisibility(View.GONE);
			layout2.setVisibility(View.GONE);
			layout3.setVisibility(View.VISIBLE);
			btnTab1.setBackgroundResource(R.drawable.tab_jb_1_off);
			btnTab2.setBackgroundResource(R.drawable.tab_jb_2_off);
			btnTab3.setBackgroundResource(R.drawable.tab_jb_3_on);
		}
	}

	public void showTCDialog(final Button btn) {

		AlertDialog.Builder dialog = new AlertDialog.Builder(this, AlertDialog.THEME_DEVICE_DEFAULT_LIGHT);
		dialog.setTitle(getString(R.string.yb_result));
		dialog.setSingleChoiceItems(strYBItems, btn.getId(), new DialogInterface.OnClickListener() {
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
}
