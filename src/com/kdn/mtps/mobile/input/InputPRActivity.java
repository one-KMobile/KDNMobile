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
import com.kdn.mtps.mobile.db.InputPRDao;
import com.kdn.mtps.mobile.db.InputPRKSubInfoDao;
import com.kdn.mtps.mobile.db.InputPRPSubInfoDao;
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

public class InputPRActivity extends BaseActivity implements TitleManager, OnClickListener {

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
	Button btnClaimContent;
	Button btnStartT;

	ImageView ivComplte;

	LinearLayout llLayout;
	LinearLayout llLayout2;

	ArrayList<PRInfo> prInfoList1;
	ArrayList<PRInfo> prInfoList2;
	PRInfo selectPrInfo;
	LinearLayout linearDateList;

	FrameLayout layout1;
	FrameLayout layout2;

	InspectInfo mInfo;

	int selectedWeatherNo = 0;

	public LinkedHashMap<Integer, ViewHolder> prpList = new LinkedHashMap<Integer, ViewHolder>();
	public LinkedHashMap<Integer, ViewHolder> prkList = new LinkedHashMap<Integer, ViewHolder>();

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
		setContentView(R.layout.activity_input_pr);

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
		btnTab1.setBackgroundResource(R.drawable.tab_pr_1_on);
		btnTab2 = (Button)findViewById(R.id.btnTab2);
		btnTab2.setTextColor(Color.BLACK);
		btnTab2.setOnClickListener(this);
		btnTab2.setBackgroundResource(R.drawable.tab_pr_2_off);

		llLayout = (LinearLayout)findViewById(R.id.llLayout);
		llLayout2 = (LinearLayout)findViewById(R.id.llLayout2);

		ivComplte = (ImageView)findViewById(R.id.ivComplte);


		linearDateList = (LinearLayout)findViewById(R.id.linearDateList);

		layout1 = (FrameLayout)findViewById(R.id.layout_1);
		layout2 = (FrameLayout)findViewById(R.id.layout_2);
	}

	public void setData() {

		tvName.setText(mInfo.tracksName);
		tvDate.setText(StringUtil.printDate(mInfo.date));
		tvEqpNm.setText(mInfo.eqpNm);

		String insType = CodeInfo.getInstance(this).getValue(ConstVALUE.CODE_TYPE_INS_TYPE, mInfo.type);
		tvType.setText(insType);

		InputPRPSubInfoDao inputPRPSubInfoDao = InputPRPSubInfoDao.getInstance(this);
		ArrayList<PRPSubInfo> list = inputPRPSubInfoDao.selectList(mInfo.eqpNo);

		InputPRKSubInfoDao inputPRKSubInfoDao = InputPRKSubInfoDao.getInstance(this);
		ArrayList<PRKSubInfo> list2 = inputPRKSubInfoDao.selectList(mInfo.eqpNo);

		Logg.d("sub info - eqpNo : " + mInfo.eqpNo);
		if (list.isEmpty()) {
			btnAdd.setEnabled(false);
		}

		for (PRPSubInfo info : list) {
			Logg.d("sub info : " + info.FNCT_LC_DTLS);
		}

		for (PRKSubInfo info : list2) {
			Logg.d("sub info2 : " + info.FNCT_LC_DTLS);
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

			addBottomPadding();
		} else {
			ivComplte.setBackgroundResource(R.drawable.input_js_edit);

			InputPRDao inputPRDao = InputPRDao.getInstance(this);
			prInfoList1 = inputPRDao.selectPRP(mInfo.master_idx);
			prInfoList2 = inputPRDao.selectPRK(mInfo.master_idx);

			selectPrInfo = prInfoList1.get(0);

			strWeather = CodeInfo.getInstance(this).getValue(ConstVALUE.CODE_TYPE_WEATHER, selectPrInfo.weather);
			selectedWeatherNo = StringUtil.getIndex(strWeatherItems, strWeather);

			String strClaimContent = CodeInfo.getInstance(this).getValue(ConstVALUE.CODE_TYPE_GOOD_SECD, selectPrInfo.claim_content);
			btnClaimContent.setText(strClaimContent);
			selectedClaimContentNo = StringUtil.getIndex(strClainContentItems, strClaimContent);

			for (int i=0; i<prInfoList1.size(); i++) {
				addItem(i, prInfoList1.get(i));
			}
			for (int i=0; i<prInfoList2.size(); i++) {
				addItem2(i, prInfoList2.get(i));
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
				AppUtil.startActivity(InputPRActivity.this, new Intent(InputPRActivity.this, MainActivity.class));
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
			InputPRDao inputPRDao = InputPRDao.getInstance(this);
			inputPRDao.Delete(mInfo.master_idx);
			InspectResultMasterDao insRetDao = InspectResultMasterDao.getInstance(this);
			insRetDao.updateComplete(mInfo.master_idx, "N", ConstVALUE.CODE_NO_INSPECT_JS);
			setResult();
			finish();
			break;
		case R.id.btnCamera:
			Intent intent = new Intent(InputPRActivity.this, CameraManageActivity.class);
			intent.putExtra("inspect", mInfo);
			startActivity(intent);
			break;
		case R.id.btnTab1:
			setLayout("1");
			break;
		case R.id.btnTab2:
			setLayout("2");
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
		InputPRDao inputPRDao = InputPRDao.getInstance(this);
		if (inputPRDao.existPR(mInfo.master_idx))
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
		for (Entry<Integer, ViewHolder> entry : prpList.entrySet()) {
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

		for (Entry<Integer, ViewHolder> entry : prkList.entrySet()) {
			ViewHolder viewHolder = entry.getValue();
			String strc1 = viewHolder.t2_editc1.getText().toString();
			String strc2= viewHolder.t2_editc2.getText().toString();
			String strc3 = viewHolder.t2_editc3.getText().toString();

			if (!"".equals(strc1) || !"".equals(strc2) || !"".equals(strc3) ) {
				isEmpty = false;
				break;
			}
		}

		//if (isEmpty) {
		//	ToastUtil.show(this, "정보를 입력해 주시기 바랍니다.");
		//	return;
		//}
		
		InputPRDao inputPRDao = InputPRDao.getInstance(this);
		inputPRDao.Delete(mInfo.master_idx);

		for (Entry<Integer, ViewHolder> entry : prpList.entrySet()) {
			ViewHolder viewHolder = entry.getValue();
			String strCircuitName = viewHolder.tvCircuitName.getText().toString();
			String strCurrentLoad = viewHolder.tvCurrentLoad.getText().toString();
			String strConductor_cnt = viewHolder.tvConductor_cnt.getText().toString();
			String strLocation = viewHolder.tvLocation.getText().toString();

			String strt1_c1 = viewHolder.t1_editc1.getText().toString();
			String strt1_c2= viewHolder.t1_editc2.getText().toString();
			String strt1_c3 = viewHolder.t1_editc3.getText().toString();
			String strt1_c4= viewHolder.t1_editc4.getText().toString();
			String strYBResult = viewHolder.btnYbResult.getText().toString();

			PRInfo log = new PRInfo();
			log.master_idx = mInfo.master_idx;
			log.weather = CodeInfo.getInstance(this).getKey(ConstVALUE.CODE_TYPE_WEATHER, strWeather);
			log.circuit_no = viewHolder.prInfo.circuit_no;
			log.circuit_name = strCircuitName;
			log.circuit_name = strCircuitName;
			log.current_load = strCurrentLoad;
			log.conductor_cnt = strConductor_cnt;
			log.location = strLocation;
			log.claim_content = CodeInfo.getInstance(this).getKey(ConstVALUE.CODE_TYPE_GOOD_SECD, strClaimContent);
			log.t_gubun = "1";
			log.t1_c1 = strt1_c1;
			log.t1_c1 = log.t1_c1.replace("=", "");
			log.t1_c2 = strt1_c2;
			log.t1_c2 = log.t1_c2.replace("=", "");
			log.t1_c3 = strt1_c3;
			log.t1_c3 = log.t1_c3.replace("=", "");
			log.t1_c4 = strt1_c4;
			log.t1_c4 = log.t1_c4.replace("=", "");
			log.yb_result = CodeInfo.getInstance(this).getKey(ConstVALUE.CODE_TYPE_GOOD_SECD, strYBResult);

			if (isEdit) {
				inputPRDao.Append(log, viewHolder.prInfo.idx);
			} else {
				inputPRDao.Append(log);
			}
		}

		for (Entry<Integer, ViewHolder> entry : prkList.entrySet()) {
			ViewHolder viewHolder = entry.getValue();
			String strCircuitName = viewHolder.tvCircuitName.getText().toString();
			String strCurrentLoad = viewHolder.tvCurrentLoad.getText().toString();
			String strConductor_cnt = "";
			String strLocation = "";

			String strt1_c1 = viewHolder.t2_editc1.getText().toString();
			String strt1_c2= viewHolder.t2_editc2.getText().toString();
			String strt1_c3 = viewHolder.t2_editc3.getText().toString();
			String strYBResult = viewHolder.btnYbResult2.getText().toString();

			PRInfo log = new PRInfo();
			log.master_idx = mInfo.master_idx;
			log.weather = CodeInfo.getInstance(this).getKey(ConstVALUE.CODE_TYPE_WEATHER, strWeather);
			log.circuit_no = viewHolder.prInfo.circuit_no;
			log.circuit_name = strCircuitName;
			log.circuit_name = strCircuitName;
			log.current_load = strCurrentLoad;
			log.conductor_cnt = strConductor_cnt;
			log.location = strLocation;
			log.claim_content = CodeInfo.getInstance(this).getKey(ConstVALUE.CODE_TYPE_GOOD_SECD, strClaimContent);
			log.yb_result = CodeInfo.getInstance(this).getKey(ConstVALUE.CODE_TYPE_GOOD_SECD, strYBResult);
			log.t_gubun = "2";
			log.t2_c1 = strt1_c1;
			log.t2_c1 = log.t2_c1.replace("=", "");
			log.t2_c2 = strt1_c2;
			log.t2_c2 = log.t2_c2.replace("=", "");
			log.t2_c3 = strt1_c3;
			log.t2_c3 = log.t2_c3.replace("=", "");

			if (isEdit) {
				inputPRDao.Append(log, viewHolder.prInfo.idx);
			} else {
				inputPRDao.Append(log);
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
			
			LocManager.getInstance(InputPRActivity.this).setGetGps(true);
			
			String currentLocation = String.format("위도:%f  경도:%f  고도:%f", location.getLatitude(), location.getLongitude(), location.getAltitude());
			Logg.d(InputPRActivity.this, "currentLocation : " + currentLocation);
            Calendar cal = new GregorianCalendar();
            String now = String.format("%d년 %d월 %d일 %d시 %d분 %d초", cal.get(Calendar.YEAR), cal.get(Calendar.MONTH)+1, 
					cal.get(Calendar.DAY_OF_MONTH), cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE),
					cal.get(Calendar.SECOND));
            Logg.d(InputPRActivity.this, "갱신 시간 : " + now);
            
            InspectResultMasterDao insRetDao = InspectResultMasterDao.getInstance(InputPRActivity.this);
            insRetDao.updateNfcTagLocation(mInfo.master_idx, "", location.getLatitude()+"", location.getLongitude()+"", false);
            
            if (locationManager != null) {
            	locationManager.removeUpdates(listener);
            }
		}
	};

	/*피뢰기점검 리스트*/
	public void addItem(int idx, PRPSubInfo prpSubInfo) {
		PRInfo prInfo = new PRInfo();
		prInfo.conductor_cnt = prpSubInfo.CONT_NUM;
		prInfo.location = prpSubInfo.SN;
		prInfo.current_load = prpSubInfo.TTM_LOAD;
		prInfo.circuit_name = prpSubInfo.FNCT_LC_DTLS;
		prInfo.circuit_no = prpSubInfo.FNCT_LC_NO;
		
		addItem(idx, prInfo);
	}
	
	public void addItem(int idx, PRInfo prInfo) {
		
		final ViewHolder viewHolder = new ViewHolder();
		View view = LayoutInflater.from(this).inflate(R.layout.item_pr_p, null);
		LinearLayout llItemParent = (LinearLayout) view.findViewById(R.id.llItemParent);
		
		UIUtil.setFont(this, (ViewGroup)llItemParent);
		
		llItemParent.setOnTouchListener(hideKeyboardListener);
		viewHolder.tvCircuitName = (TextView) view.findViewById(R.id.tvCircuitName);
		viewHolder.tvCurrentLoad = (TextView) view.findViewById(R.id.tvCurrentLoad);
		viewHolder.tvConductor_cnt = (TextView) view.findViewById(R.id.tvConductor_cnt);
		viewHolder.tvLocation = (TextView) view.findViewById(R.id.tvLocation);
		viewHolder.btnYbResult  = (Button)view.findViewById(R.id.btnYBresult);
		viewHolder.btnYbResult.setId(0);
		viewHolder.btnYbResult.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				showYBResultDialog((Button)v);
			}
		});

		viewHolder.t1_editc1 = (EditText) view.findViewById(R.id.editc1);
		viewHolder.t1_editc2 = (EditText) view.findViewById(R.id.editc2);
        viewHolder.t1_editc3 = (EditText) view.findViewById(R.id.editc3);
		viewHolder.t1_editc4 = (EditText) view.findViewById(R.id.editc4);
        //setTextWatcher(viewHolder);

		viewHolder.tvCurrentLoad.setText(idx + "");
		viewHolder.tvConductor_cnt.setText((idx + 1) + "");
		viewHolder.btnYbResult.setTag(1);
		
		if (prInfo != null) {
			viewHolder.prInfo = prInfo;
			viewHolder.tvCircuitName.setText(prInfo.circuit_name);
			viewHolder.tvCurrentLoad.setText(prInfo.current_load);
			viewHolder.tvConductor_cnt.setText(prInfo.conductor_cnt);
			viewHolder.tvLocation.setText(prInfo.location);

			viewHolder.t1_editc1.setText(prInfo.t1_c1);
			viewHolder.t1_editc2.setText(prInfo.t1_c2);
			viewHolder.t1_editc3.setText(prInfo.t1_c3);
			viewHolder.t1_editc4.setText(prInfo.t1_c4);
			String strYBResult = CodeInfo.getInstance(this).getValue(ConstVALUE.CODE_TYPE_GOOD_SECD, prInfo.yb_result);
			viewHolder.btnYbResult.setText(strYBResult);
			viewHolder.btnYbResult.setId(StringUtil.getIndex(strYBItems, strYBResult));
		}

		prpList.put(idx, viewHolder);

		Logg.d("$$$$$$$$$$$$$ prpList t1_editc1 : " + viewHolder.t1_editc1);
		Logg.d("$$$$$$$$$$$$$ prpList prInfo : " + prInfo);
		llLayout.addView(view);
		
	}
	
	public class ViewHolder {
		TextView tvCircuitName;
		TextView tvCurrentLoad;
		TextView tvConductor_cnt;
		TextView tvLocation;
		EditText t1_editc1;
        EditText t1_editc2;
        EditText t1_editc3;
        EditText t1_editc4;
		EditText t2_editc1;
		EditText t2_editc2;
		EditText t2_editc3;
		Button btnYbResult;
		Button btnYbResult2;
        
        PRInfo prInfo;
	}

	/*케이블헤드 리스트*/
	public void addItem2(int idx, PRKSubInfo prkSubInfo) {
		PRInfo prInfo = new PRInfo();
		prInfo.current_load = prkSubInfo.FNCT_LC_DTLS;
		prInfo.circuit_name = prkSubInfo.FNCT_LC_NO;

		addItem2(idx, prInfo);
	}

	public void addItem2(int idx, PRInfo prInfo) {

		final ViewHolder viewHolder = new ViewHolder();
		View view = LayoutInflater.from(this).inflate(R.layout.item_pr_k, null);
		LinearLayout llItemParent = (LinearLayout) view.findViewById(R.id.llItemParent);

		UIUtil.setFont(this, (ViewGroup)llItemParent);

		llItemParent.setOnTouchListener(hideKeyboardListener);
		viewHolder.tvCircuitName = (TextView) view.findViewById(R.id.tvCircuitName);
		viewHolder.tvCurrentLoad = (TextView) view.findViewById(R.id.tvCurrentLoad);
		//viewHolder.tvConductor_cnt = (TextView) view.findViewById(R.id.tvConductor_cnt);
		//viewHolder.tvLocation = (TextView) view.findViewById(R.id.tvLocation);

		viewHolder.t2_editc1 = (EditText) view.findViewById(R.id.editc1);
		viewHolder.t2_editc2 = (EditText) view.findViewById(R.id.editc2);
		viewHolder.t2_editc3 = (EditText) view.findViewById(R.id.editc3);
		viewHolder.btnYbResult2  = (Button)view.findViewById(R.id.btnYBresult2);
		viewHolder.btnYbResult2.setId(0);
		viewHolder.btnYbResult2.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				showYBResultDialog((Button)v);
			}
		});

		viewHolder.tvCurrentLoad.setText(idx + "");
		viewHolder.btnYbResult2.setTag(1);

		if (prInfo != null) {
			viewHolder.prInfo = prInfo;
			viewHolder.tvCircuitName.setText(prInfo.circuit_name);
			viewHolder.tvCurrentLoad.setText(prInfo.current_load);
			//viewHolder.tvConductor_cnt.setText("");
			//viewHolder.tvLocation.setText("");

			viewHolder.t2_editc1.setText(prInfo.t2_c1);
			viewHolder.t2_editc2.setText(prInfo.t2_c2);
			viewHolder.t2_editc3.setText(prInfo.t2_c3);
			String strYBResult = CodeInfo.getInstance(this).getValue(ConstVALUE.CODE_TYPE_GOOD_SECD, prInfo.yb_result);
			viewHolder.btnYbResult2.setText(strYBResult);
			viewHolder.btnYbResult2.setId(StringUtil.getIndex(strYBItems, strYBResult));
		}

		prkList.put(idx, viewHolder);

		llLayout2.addView(view);

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
			btnTab1.setBackgroundResource(R.drawable.tab_pr_1_on);
			btnTab2.setBackgroundResource(R.drawable.tab_pr_2_off);
		} else {
			layout2.setVisibility(View.VISIBLE);
			layout1.setVisibility(View.GONE);
			btnTab1.setBackgroundResource(R.drawable.tab_pr_1_off);
			btnTab2.setBackgroundResource(R.drawable.tab_pr_2_on);
		}
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
}
