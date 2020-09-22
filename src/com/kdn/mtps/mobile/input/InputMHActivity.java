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

public class InputMHActivity extends BaseActivity implements TitleManager, OnClickListener {

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

	ArrayList<MHInfo> mhInfoList1;
	ArrayList<MHInfo> mhInfoList2;
	ArrayList<MHInfo> mhInfoList3;
	MHInfo selectmhInfo;
	LinearLayout linearDateList;

	FrameLayout layout1;
	FrameLayout layout2;
	FrameLayout layout3;

	InspectInfo mInfo;

	int selectedWeatherNo = 0;

	public LinkedHashMap<Integer, ViewHolder> mhiList = new LinkedHashMap<Integer, ViewHolder>();
	public LinkedHashMap<Integer, ViewHolder> mhbList = new LinkedHashMap<Integer, ViewHolder>();
	public LinkedHashMap<Integer, ViewHolder> mhgList = new LinkedHashMap<Integer, ViewHolder>();

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
		setContentView(R.layout.activity_input_mh);

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
		btnTab1.setBackgroundResource(R.drawable.tab_mh_1_on);
		btnTab2 = (Button)findViewById(R.id.btnTab2);
		btnTab2.setTextColor(Color.BLACK);
		btnTab2.setOnClickListener(this);
		btnTab2.setBackgroundResource(R.drawable.tab_mh_2_off);
		btnTab3 = (Button)findViewById(R.id.btnTab3);
		btnTab3.setTextColor(Color.BLACK);
		btnTab3.setOnClickListener(this);
		btnTab3.setBackgroundResource(R.drawable.tab_mh_3_off);

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

		InputMHISubInfoDao inputMHISubInfoDao = InputMHISubInfoDao.getInstance(this);
		ArrayList<MHISubInfo> list = inputMHISubInfoDao.selectList(mInfo.eqpNo);

		InputMHBSubInfoDao inputMHBSubInfoDao = InputMHBSubInfoDao.getInstance(this);
		ArrayList<MHBSubInfo> list2 = inputMHBSubInfoDao.selectList(mInfo.eqpNo);

		InputMHGSubInfoDao inputMHGSubInfoDao = InputMHGSubInfoDao.getInstance(this);
		ArrayList<MHGSubInfo> list3 = inputMHGSubInfoDao.selectList(mInfo.eqpNo);

		Logg.d("sub info - eqpNo : " + mInfo.eqpNo);
		if (list.isEmpty()) {
			btnAdd.setEnabled(false);
		}

		for (MHISubInfo info : list) {
			Logg.d("sub info : " + info.FNCT_LC_DTLS);
		}

		for (MHBSubInfo info : list2) {
			Logg.d("sub info2 : " + info.FNCT_LC_DTLS);
		}

		for (MHGSubInfo info : list3) {
			Logg.d("sub info3 : " + info.FNCT_LC_DTLS);
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

			InputMHDao inputMHDao = InputMHDao.getInstance(this);
			mhInfoList1 = inputMHDao.selectMHI(mInfo.master_idx);
			mhInfoList2 = inputMHDao.selectMHB(mInfo.master_idx);
			mhInfoList3 = inputMHDao.selectMHG(mInfo.master_idx);

			selectmhInfo = mhInfoList1.get(0);

			strWeather = CodeInfo.getInstance(this).getValue(ConstVALUE.CODE_TYPE_WEATHER, selectmhInfo.weather);
			selectedWeatherNo = StringUtil.getIndex(strWeatherItems, strWeather);

			String strClaimContent = CodeInfo.getInstance(this).getValue(ConstVALUE.CODE_TYPE_GOOD_SECD, selectmhInfo.claim_content);
			btnClaimContent.setText(strClaimContent);
			selectedClaimContentNo = StringUtil.getIndex(strClainContentItems, strClaimContent);

			for (int i=0; i<mhInfoList1.size(); i++) {
				Logg.d("mhInfoList1 !!! : " + mhInfoList1.get(i).circuit_name);
				addItem(i, mhInfoList1.get(i));
			}
			for (int i=0; i<mhInfoList2.size(); i++) {
				Logg.d("mhInfoList2 !!! : " + mhInfoList2.get(i).circuit_name);
				addItem2(i, mhInfoList2.get(i));
			}
			for (int i=0; i<mhInfoList3.size(); i++) {
				Logg.d("mhInfoList3 !!! : " + mhInfoList3.get(i).circuit_name);
				addItem3(i, mhInfoList3.get(i));
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
				AppUtil.startActivity(InputMHActivity.this, new Intent(InputMHActivity.this, MainActivity.class));
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
			Intent intent = new Intent(InputMHActivity.this, CameraManageActivity.class);
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
		InputMHDao inputMHDao = InputMHDao.getInstance(this);
		if (inputMHDao.existMH(mInfo.master_idx))
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

		InputMHDao inputMHDao = InputMHDao.getInstance(this);
		inputMHDao.Delete(mInfo.master_idx);

		for (Entry<Integer, ViewHolder> entry : mhiList.entrySet()) {
			ViewHolder viewHolder = entry.getValue();
			Logg.d("mhiList !!!! : " + viewHolder.tvCircuitName.getText().toString());
			String strCircuitName = viewHolder.tvCircuitName.getText().toString();
			String strCurrentLoad = viewHolder.tvCurrentLoad.getText().toString();

			String str_c1 = viewHolder.t1_c1.getText().toString();
			String str_c2= viewHolder.t1_c2.getText().toString();
			String str_c3 = viewHolder.t1_c3.getText().toString();
			String str_c4= viewHolder.t1_c4.getText().toString();
			String str_c5 = viewHolder.t1_c5.getText().toString();
			String str_c6= viewHolder.t1_c6.getText().toString();
			String str_c7 = viewHolder.t1_c7.getText().toString();
			String str_c8= viewHolder.t1_c8.getText().toString();

			MHInfo log = new MHInfo();
			log.master_idx = mInfo.master_idx;
			log.weather = CodeInfo.getInstance(this).getKey(ConstVALUE.CODE_TYPE_WEATHER, strWeather);
			log.circuit_no = viewHolder.mhInfo.circuit_no;
			log.circuit_name = strCircuitName;
			log.current_load = strCurrentLoad;
			log.claim_content = CodeInfo.getInstance(this).getKey(ConstVALUE.CODE_TYPE_GOOD_SECD, strClaimContent);
			log.t_gubun = "1";
			log.t1_c1 = CodeInfo.getInstance(this).getKey(ConstVALUE.CODE_TYPE_GOOD_SECD, str_c1);
			log.t1_c2 = CodeInfo.getInstance(this).getKey(ConstVALUE.CODE_TYPE_GOOD_SECD, str_c2);
			log.t1_c3 = CodeInfo.getInstance(this).getKey(ConstVALUE.CODE_TYPE_GOOD_SECD, str_c3);
			log.t1_c4 = CodeInfo.getInstance(this).getKey(ConstVALUE.CODE_TYPE_GOOD_SECD, str_c4);
			log.t1_c5 = CodeInfo.getInstance(this).getKey(ConstVALUE.CODE_TYPE_GOOD_SECD, str_c5);
			log.t1_c6 = CodeInfo.getInstance(this).getKey(ConstVALUE.CODE_TYPE_GOOD_SECD, str_c6);
			log.t1_c7 = CodeInfo.getInstance(this).getKey(ConstVALUE.CODE_TYPE_GOOD_SECD, str_c7);
			log.t1_c8 = CodeInfo.getInstance(this).getKey(ConstVALUE.CODE_TYPE_GOOD_SECD, str_c8);

			if (isEdit) {
				inputMHDao.Append(log, viewHolder.mhInfo.idx);
			} else {
				inputMHDao.Append(log);
			}
		}

		for (Entry<Integer, ViewHolder> entry : mhbList.entrySet()) {
			ViewHolder viewHolder = entry.getValue();
			Logg.d("mhbList !!!! : " + viewHolder.tvCircuitName.getText().toString());
			String strCircuitName = viewHolder.tvCircuitName.getText().toString();
			String strCurrentLoad = viewHolder.tvCurrentLoad.getText().toString();

			String str_c1 = viewHolder.t2_c1.getText().toString();
			String str_c2= viewHolder.t2_c2.getText().toString();
			String str_c3 = viewHolder.t2_c3.getText().toString();
			String str_c4= viewHolder.t2_c4.getText().toString();
			String str_c5 = viewHolder.t2_c5.getText().toString();
			String str_c6= viewHolder.t2_c6.getText().toString();
			String str_c7 = viewHolder.t2_c7.getText().toString();
			String str_c8= viewHolder.t2_c8.getText().toString();

			MHInfo log = new MHInfo();
			log.master_idx = mInfo.master_idx;
			log.weather = CodeInfo.getInstance(this).getKey(ConstVALUE.CODE_TYPE_WEATHER, strWeather);
			log.circuit_no = viewHolder.mhInfo.circuit_no;
			log.circuit_name = strCircuitName;
			log.current_load = strCurrentLoad;
			log.claim_content = CodeInfo.getInstance(this).getKey(ConstVALUE.CODE_TYPE_GOOD_SECD, strClaimContent);
			log.t_gubun = "2";
			log.t2_c1 = CodeInfo.getInstance(this).getKey(ConstVALUE.CODE_TYPE_GOOD_SECD, str_c1);
			log.t2_c2 = CodeInfo.getInstance(this).getKey(ConstVALUE.CODE_TYPE_GOOD_SECD, str_c2);
			log.t2_c3 = CodeInfo.getInstance(this).getKey(ConstVALUE.CODE_TYPE_GOOD_SECD, str_c3);
			log.t2_c4 = CodeInfo.getInstance(this).getKey(ConstVALUE.CODE_TYPE_GOOD_SECD, str_c4);
			log.t2_c5 = CodeInfo.getInstance(this).getKey(ConstVALUE.CODE_TYPE_GOOD_SECD, str_c5);
			log.t2_c6 = CodeInfo.getInstance(this).getKey(ConstVALUE.CODE_TYPE_GOOD_SECD, str_c6);
			log.t2_c7 = CodeInfo.getInstance(this).getKey(ConstVALUE.CODE_TYPE_GOOD_SECD, str_c7);
			log.t2_c8 = str_c8;
			log.t2_c8 = log.t2_c8.replace("=", "");

			if (isEdit) {
				inputMHDao.Append(log, viewHolder.mhInfo.idx);
			} else {
				inputMHDao.Append(log);
			}
		}

		for (Entry<Integer, ViewHolder> entry : mhgList.entrySet()) {
			ViewHolder viewHolder = entry.getValue();
			Logg.d("mhgList !!!! : " + viewHolder.tvGubun.getText().toString());
			String strCircuitName = viewHolder.tvCircuitName.getText().toString();
			String strCurrentLoad = viewHolder.tvCurrentLoad.getText().toString();
			String strGGubun = viewHolder.tvGubun.getText().toString();

			String str_c1 = viewHolder.t3_c1.getText().toString();
			String str_c2= viewHolder.t3_c2.getText().toString();
			String str_c3 = viewHolder.t3_c3.getText().toString();
			String str_c4= viewHolder.t3_c4.getText().toString();
			String str_c5 = viewHolder.t3_c5.getText().toString();
			String str_c6= viewHolder.t3_c6.getText().toString();
			String str_c7 = viewHolder.t3_c7.getText().toString();
			String str_c8= viewHolder.t3_c8.getText().toString();
			String str_c9 = viewHolder.t3_c9.getText().toString();
			String str_c10= viewHolder.t3_c10.getText().toString();

			MHInfo log = new MHInfo();
			log.master_idx = mInfo.master_idx;
			log.weather = CodeInfo.getInstance(this).getKey(ConstVALUE.CODE_TYPE_WEATHER, strWeather);
			log.circuit_no = viewHolder.mhInfo.circuit_no;
			log.circuit_name = strCircuitName;
			log.current_load = strCurrentLoad;
			log.claim_content = CodeInfo.getInstance(this).getKey(ConstVALUE.CODE_TYPE_GOOD_SECD, strClaimContent);
			log.t_gubun = "3";
			log.g_gubun = strGGubun;
			log.t3_c1 = str_c1;
			log.t3_c1 = log.t3_c1.replace("=", "");
			log.t3_c2 = str_c2;
			log.t3_c2 = log.t3_c2.replace("=", "");
			log.t3_c3 = str_c3;
			log.t3_c3 = log.t3_c3.replace("=", "");
			log.t3_c4 = str_c4;
			log.t3_c4 = log.t3_c4.replace("=", "");
			log.t3_c5 = str_c5;
			log.t3_c5 = log.t3_c5.replace("=", "");
			log.t3_c6 = str_c6;
			log.t3_c6 = log.t3_c6.replace("=", "");
			log.t3_c7 = str_c7;
			log.t3_c7 = log.t3_c7.replace("=", "");
			log.t3_c8 = str_c8;
			log.t3_c8 = log.t3_c8.replace("=", "");
			log.t3_c9 = str_c9;
			log.t3_c9 = log.t3_c9.replace("=", "");
			log.t3_c10 = str_c10;
			log.t3_c10 = log.t3_c10.replace("=", "");

			if (isEdit) {
				inputMHDao.Append(log, viewHolder.mhInfo.idx);
			} else {
				inputMHDao.Append(log);
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

			LocManager.getInstance(InputMHActivity.this).setGetGps(true);

			String currentLocation = String.format("위도:%f  경도:%f  고도:%f", location.getLatitude(), location.getLongitude(), location.getAltitude());
			Logg.d(InputMHActivity.this, "currentLocation : " + currentLocation);
            Calendar cal = new GregorianCalendar();
            String now = String.format("%d년 %d월 %d일 %d시 %d분 %d초", cal.get(Calendar.YEAR), cal.get(Calendar.MONTH)+1,
					cal.get(Calendar.DAY_OF_MONTH), cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE),
					cal.get(Calendar.SECOND));
            Logg.d(InputMHActivity.this, "갱신 시간 : " + now);

            InspectResultMasterDao insRetDao = InspectResultMasterDao.getInstance(InputMHActivity.this);
            insRetDao.updateNfcTagLocation(mInfo.master_idx, "", location.getLatitude()+"", location.getLongitude()+"", false);

            if (locationManager != null) {
            	locationManager.removeUpdates(listener);
            }
		}
	};

	/*이상유무 리스트*/
	public void addItem(int idx, MHISubInfo mhiSubInfo) {
		MHInfo mhInfo = new MHInfo();
		mhInfo.current_load = mhiSubInfo.FNCT_LC_DTLS;
		mhInfo.circuit_name = mhiSubInfo.FNCT_LC_NO;

		addItem(idx, mhInfo);
	}

	public void addItem(int idx, MHInfo mhInfo) {

		final ViewHolder viewHolder = new ViewHolder();
		View view = LayoutInflater.from(this).inflate(R.layout.item_mh_i, null);
		LinearLayout llItemParent = (LinearLayout) view.findViewById(R.id.llItemParent);

		UIUtil.setFont(this, (ViewGroup)llItemParent);

		llItemParent.setOnTouchListener(hideKeyboardListener);
		viewHolder.tvCircuitName = (TextView) view.findViewById(R.id.tvCircuitName);
		viewHolder.tvCurrentLoad = (TextView) view.findViewById(R.id.tvCurrentLoad);
		viewHolder.t1_c1  = (Button)view.findViewById(R.id.btnT1C1);
		viewHolder.t1_c1.setId(0);
		viewHolder.t1_c1.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				showTCDialog((Button)v);
			}
		});
		viewHolder.t1_c2  = (Button)view.findViewById(R.id.btnT1C2);
		viewHolder.t1_c2.setId(0);
		viewHolder.t1_c2.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				showTCDialog((Button)v);
			}
		});
		viewHolder.t1_c3  = (Button)view.findViewById(R.id.btnT1C3);
		viewHolder.t1_c3.setId(0);
		viewHolder.t1_c3.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				showTCDialog((Button)v);
			}
		});
		viewHolder.t1_c4  = (Button)view.findViewById(R.id.btnT1C4);
		viewHolder.t1_c4.setId(0);
		viewHolder.t1_c4.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				showTCDialog((Button)v);
			}
		});
		viewHolder.t1_c5  = (Button)view.findViewById(R.id.btnT1C5);
		viewHolder.t1_c5.setId(0);
		viewHolder.t1_c5.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				showTCDialog((Button)v);
			}
		});
		viewHolder.t1_c6  = (Button)view.findViewById(R.id.btnT1C6);
		viewHolder.t1_c6.setId(0);
		viewHolder.t1_c6.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				showTCDialog((Button)v);
			}
		});
		viewHolder.t1_c7  = (Button)view.findViewById(R.id.btnT1C7);
		viewHolder.t1_c7.setId(0);
		viewHolder.t1_c7.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				showTCDialog((Button)v);
			}
		});
		viewHolder.t1_c8  = (Button)view.findViewById(R.id.btnT1C8);
		viewHolder.t1_c8.setId(0);
		viewHolder.t1_c8.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				showTCDialog((Button)v);
			}
		});
        //setTextWatcher(viewHolder);

		viewHolder.t1_c1.setTag(1);
		viewHolder.t1_c2.setTag(1);
		viewHolder.t1_c3.setTag(1);
		viewHolder.t1_c4.setTag(1);
		viewHolder.t1_c5.setTag(1);
		viewHolder.t1_c6.setTag(1);
		viewHolder.t1_c7.setTag(1);
		viewHolder.t1_c8.setTag(1);

		if (mhInfo != null) {
			viewHolder.mhInfo = mhInfo;
			viewHolder.tvCircuitName.setText(mhInfo.circuit_name);
			viewHolder.tvCurrentLoad.setText(mhInfo.current_load);

			String strt1_c1 = CodeInfo.getInstance(this).getValue(ConstVALUE.CODE_TYPE_GOOD_SECD, mhInfo.t1_c1);
			viewHolder.t1_c1.setText(strt1_c1);
			viewHolder.t1_c1.setId(StringUtil.getIndex(strYBItems, strt1_c1));
			String strt1_c2 = CodeInfo.getInstance(this).getValue(ConstVALUE.CODE_TYPE_GOOD_SECD, mhInfo.t1_c2);
			viewHolder.t1_c2.setText(strt1_c2);
			viewHolder.t1_c2.setId(StringUtil.getIndex(strYBItems, strt1_c2));
			String strt1_c3 = CodeInfo.getInstance(this).getValue(ConstVALUE.CODE_TYPE_GOOD_SECD, mhInfo.t1_c3);
			viewHolder.t1_c3.setText(strt1_c3);
			viewHolder.t1_c3.setId(StringUtil.getIndex(strYBItems, strt1_c3));
			String strt1_c4 = CodeInfo.getInstance(this).getValue(ConstVALUE.CODE_TYPE_GOOD_SECD, mhInfo.t1_c4);
			viewHolder.t1_c4.setText(strt1_c4);
			viewHolder.t1_c4.setId(StringUtil.getIndex(strYBItems, strt1_c4));
			String strt1_c5 = CodeInfo.getInstance(this).getValue(ConstVALUE.CODE_TYPE_GOOD_SECD, mhInfo.t1_c5);
			viewHolder.t1_c5.setText(strt1_c5);
			viewHolder.t1_c5.setId(StringUtil.getIndex(strYBItems, strt1_c5));
			String strt1_c6 = CodeInfo.getInstance(this).getValue(ConstVALUE.CODE_TYPE_GOOD_SECD, mhInfo.t1_c6);
			viewHolder.t1_c6.setText(strt1_c6);
			viewHolder.t1_c6.setId(StringUtil.getIndex(strYBItems, strt1_c6));
			String strt1_c7 = CodeInfo.getInstance(this).getValue(ConstVALUE.CODE_TYPE_GOOD_SECD, mhInfo.t1_c7);
			viewHolder.t1_c7.setText(strt1_c7);
			viewHolder.t1_c7.setId(StringUtil.getIndex(strYBItems, strt1_c7));
			String strt1_c8 = CodeInfo.getInstance(this).getValue(ConstVALUE.CODE_TYPE_GOOD_SECD, mhInfo.t1_c8);
			viewHolder.t1_c8.setText(strt1_c8);
			viewHolder.t1_c8.setId(StringUtil.getIndex(strYBItems, strt1_c8));
		}

		mhiList.put(idx, viewHolder);
		llLayout.addView(view);
	}

	public class ViewHolder {
		TextView tvCircuitName;
		TextView tvCurrentLoad;
		TextView tvGubun;
		Button t1_c1;
		Button t1_c2;
		Button t1_c3;
		Button t1_c4;
		Button t1_c5;
		Button t1_c6;
		Button t1_c7;
		Button t1_c8;

		Button t2_c1;
		Button t2_c2;
		Button t2_c3;
		Button t2_c4;
		Button t2_c5;
		Button t2_c6;
		Button t2_c7;
		EditText t2_c8;

		EditText t3_c1;
		EditText t3_c2;
		EditText t3_c3;
		EditText t3_c4;
		EditText t3_c5;
		EditText t3_c6;
		EditText t3_c7;
		EditText t3_c8;
		EditText t3_c9;
		EditText t3_c10;
        MHInfo mhInfo;
	}

	/*발생유무 리스트*/
	public void addItem2(int idx, MHBSubInfo mhbSubInfo) {
		MHInfo mhInfo = new MHInfo();
		mhInfo.current_load = mhbSubInfo.FNCT_LC_DTLS;
		mhInfo.circuit_name = mhbSubInfo.FNCT_LC_NO;

		addItem2(idx, mhInfo);
	}

	public void addItem2(int idx, MHInfo mhInfo) {

		final ViewHolder viewHolder = new ViewHolder();
		View view = LayoutInflater.from(this).inflate(R.layout.item_mh_b, null);
		LinearLayout llItemParent = (LinearLayout) view.findViewById(R.id.llItemParent);

		UIUtil.setFont(this, (ViewGroup)llItemParent);

		llItemParent.setOnTouchListener(hideKeyboardListener);
		viewHolder.tvCircuitName = (TextView) view.findViewById(R.id.tvCircuitName);
		viewHolder.tvCurrentLoad = (TextView) view.findViewById(R.id.tvCurrentLoad);

		viewHolder.t2_c1  = (Button)view.findViewById(R.id.btnT2C1);
		viewHolder.t2_c1.setId(0);
		viewHolder.t2_c1.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				showTCDialog((Button)v);
			}
		});
		viewHolder.t2_c2  = (Button)view.findViewById(R.id.btnT2C2);
		viewHolder.t2_c2.setId(0);
		viewHolder.t2_c2.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				showTCDialog((Button)v);
			}
		});
		viewHolder.t2_c3  = (Button)view.findViewById(R.id.btnT2C3);
		viewHolder.t2_c3.setId(0);
		viewHolder.t2_c3.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				showTCDialog((Button)v);
			}
		});
		viewHolder.t2_c4  = (Button)view.findViewById(R.id.btnT2C4);
		viewHolder.t2_c4.setId(0);
		viewHolder.t2_c4.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				showTCDialog((Button)v);
			}
		});
		viewHolder.t2_c5  = (Button)view.findViewById(R.id.btnT2C5);
		viewHolder.t2_c5.setId(0);
		viewHolder.t2_c5.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				showTCDialog((Button)v);
			}
		});
		viewHolder.t2_c6  = (Button)view.findViewById(R.id.btnT2C6);
		viewHolder.t2_c6.setId(0);
		viewHolder.t2_c6.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				showTCDialog((Button)v);
			}
		});
		viewHolder.t2_c7  = (Button)view.findViewById(R.id.btnT2C7);
		viewHolder.t2_c7.setId(0);
		viewHolder.t2_c7.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				showTCDialog((Button)v);
			}
		});
		viewHolder.t2_c8 = (EditText) view.findViewById(R.id.editT2C8);
		viewHolder.t2_c1.setTag(1);
		viewHolder.t2_c2.setTag(1);
		viewHolder.t2_c3.setTag(1);
		viewHolder.t2_c4.setTag(1);
		viewHolder.t2_c5.setTag(1);
		viewHolder.t2_c6.setTag(1);
		viewHolder.t2_c7.setTag(1);

		if (mhInfo != null) {
			viewHolder.mhInfo = mhInfo;
			viewHolder.tvCircuitName.setText(mhInfo.circuit_name);
			viewHolder.tvCurrentLoad.setText(mhInfo.current_load);
			//viewHolder.tvConductor_cnt.setText(mhInfo.conductor_cnt);
			//viewHolder.tvLocation.setText(mhInfo.location);


			String strt2_c1 = CodeInfo.getInstance(this).getValue(ConstVALUE.CODE_TYPE_GOOD_SECD, mhInfo.t2_c1);
			viewHolder.t2_c1.setText(strt2_c1);
			viewHolder.t2_c1.setId(StringUtil.getIndex(strYBItems, strt2_c1));
			String strt2_c2 = CodeInfo.getInstance(this).getValue(ConstVALUE.CODE_TYPE_GOOD_SECD, mhInfo.t2_c2);
			viewHolder.t2_c2.setText(strt2_c2);
			viewHolder.t2_c2.setId(StringUtil.getIndex(strYBItems, strt2_c2));
			String strt2_c3 = CodeInfo.getInstance(this).getValue(ConstVALUE.CODE_TYPE_GOOD_SECD, mhInfo.t2_c3);
			viewHolder.t2_c3.setText(strt2_c3);
			viewHolder.t2_c3.setId(StringUtil.getIndex(strYBItems, strt2_c3));
			String strt2_c4 = CodeInfo.getInstance(this).getValue(ConstVALUE.CODE_TYPE_GOOD_SECD, mhInfo.t2_c4);
			viewHolder.t2_c4.setText(strt2_c4);
			viewHolder.t2_c4.setId(StringUtil.getIndex(strYBItems, strt2_c4));
			String strt2_c5 = CodeInfo.getInstance(this).getValue(ConstVALUE.CODE_TYPE_GOOD_SECD, mhInfo.t2_c5);
			viewHolder.t2_c5.setText(strt2_c5);
			viewHolder.t2_c5.setId(StringUtil.getIndex(strYBItems, strt2_c5));
			String strt2_c6 = CodeInfo.getInstance(this).getValue(ConstVALUE.CODE_TYPE_GOOD_SECD, mhInfo.t2_c6);
			viewHolder.t2_c6.setText(strt2_c6);
			viewHolder.t2_c6.setId(StringUtil.getIndex(strYBItems, strt2_c6));
			String strt2_c7 = CodeInfo.getInstance(this).getValue(ConstVALUE.CODE_TYPE_GOOD_SECD, mhInfo.t2_c7);
			viewHolder.t2_c7.setText(strt2_c7);
			viewHolder.t2_c7.setId(StringUtil.getIndex(strYBItems, strt2_c7));

			viewHolder.t2_c8.setText(mhInfo.t2_c8);
		}

		mhbList.put(idx, viewHolder);

		llLayout2.addView(view);

	}

	/*가스탐지기록 리스트*/
	public void addItem3(int idx, MHGSubInfo mhgSubInfo) {
		MHInfo mhInfo = new MHInfo();
		mhInfo.current_load = mhgSubInfo.FNCT_LC_DTLS;
		mhInfo.circuit_name = mhgSubInfo.FNCT_LC_NO;
		mhInfo.g_gubun = mhgSubInfo.G_GUBUN;

		addItem3(idx, mhInfo);
	}

	public void addItem3(int idx, MHInfo mhInfo) {


		final ViewHolder viewHolder = new ViewHolder();
		View view = LayoutInflater.from(this).inflate(R.layout.item_mh_g, null);
		LinearLayout llItemParent = (LinearLayout) view.findViewById(R.id.llItemParent);

		UIUtil.setFont(this, (ViewGroup)llItemParent);

		llItemParent.setOnTouchListener(hideKeyboardListener);
		viewHolder.tvCircuitName = (TextView) view.findViewById(R.id.tvCircuitName);
		viewHolder.tvCurrentLoad = (TextView) view.findViewById(R.id.tvCurrentLoad);
		viewHolder.tvGubun = (TextView) view.findViewById(R.id.tvGubun);
		//viewHolder.tvLocation = (TextView) view.findViewById(R.id.tvLocation);

		viewHolder.t3_c1 = (EditText) view.findViewById(R.id.editT3C1);
		viewHolder.t3_c2 = (EditText) view.findViewById(R.id.editT3C2);
		viewHolder.t3_c3 = (EditText) view.findViewById(R.id.editT3C3);
		viewHolder.t3_c4 = (EditText) view.findViewById(R.id.editT3C4);
		viewHolder.t3_c5 = (EditText) view.findViewById(R.id.editT3C5);
		viewHolder.t3_c6 = (EditText) view.findViewById(R.id.editT3C6);
		viewHolder.t3_c7 = (EditText) view.findViewById(R.id.editT3C7);
		viewHolder.t3_c8 = (EditText) view.findViewById(R.id.editT3C8);
		viewHolder.t3_c9 = (EditText) view.findViewById(R.id.editT3C9);
		viewHolder.t3_c10 = (EditText) view.findViewById(R.id.editT3C10);
		Logg.d("mhInfo ===== : " + mhInfo.g_gubun);
		if (mhInfo != null) {
			viewHolder.mhInfo = mhInfo;
			viewHolder.tvCircuitName.setText(mhInfo.circuit_name);
			viewHolder.tvCurrentLoad.setText(mhInfo.current_load);
			viewHolder.tvGubun.setText(mhInfo.g_gubun);
			//viewHolder.tvLocation.setText("");

			viewHolder.t3_c1.setText(mhInfo.t3_c1);
			viewHolder.t3_c2.setText(mhInfo.t3_c2);
			viewHolder.t3_c3.setText(mhInfo.t3_c3);
			viewHolder.t3_c4.setText(mhInfo.t3_c4);
			viewHolder.t3_c5.setText(mhInfo.t3_c5);
			viewHolder.t3_c6.setText(mhInfo.t3_c6);
			viewHolder.t3_c7.setText(mhInfo.t3_c7);
			viewHolder.t3_c8.setText(mhInfo.t3_c8);
			viewHolder.t3_c9.setText(mhInfo.t3_c9);
			viewHolder.t3_c10.setText(mhInfo.t3_c10);
		}

		mhgList.put(idx, viewHolder);

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
			btnTab1.setBackgroundResource(R.drawable.tab_mh_1_on);
			btnTab2.setBackgroundResource(R.drawable.tab_mh_2_off);
			btnTab3.setBackgroundResource(R.drawable.tab_mh_3_off);
		} else if(strTab == "2") {
			layout1.setVisibility(View.GONE);
			layout2.setVisibility(View.VISIBLE);
			layout3.setVisibility(View.GONE);
			btnTab1.setBackgroundResource(R.drawable.tab_mh_1_off);
			btnTab2.setBackgroundResource(R.drawable.tab_mh_2_on);
			btnTab3.setBackgroundResource(R.drawable.tab_mh_3_off);
		} else {
			layout1.setVisibility(View.GONE);
			layout2.setVisibility(View.GONE);
			layout3.setVisibility(View.VISIBLE);
			btnTab1.setBackgroundResource(R.drawable.tab_mh_1_off);
			btnTab2.setBackgroundResource(R.drawable.tab_mh_2_off);
			btnTab3.setBackgroundResource(R.drawable.tab_mh_3_on);
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
