package com.kdn.mtps.mobile.input;

import android.app.AlertDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import java.util.Calendar;
import java.util.GregorianCalendar;
import android.content.Intent;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
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
import com.kdn.mtps.mobile.db.InputJGDao;
import com.kdn.mtps.mobile.db.InputJGPSubInfoDao;
import com.kdn.mtps.mobile.db.InputJGUSubInfoDao;
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

import android.widget.FrameLayout;
import android.widget.TimePicker;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map.Entry;

public class InputJGActivity extends BaseActivity implements TitleManager, OnClickListener {

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

	ArrayList<JGInfo> jgInfoList1;
	ArrayList<JGInfo> jgInfoList2;
	JGInfo selectJgInfo;
	LinearLayout linearDateList;

	FrameLayout layout1;
	FrameLayout layout2;

	InspectInfo mInfo;

	int selectedWeatherNo = 0;

	public LinkedHashMap<Integer, ViewHolder> jguList = new LinkedHashMap<Integer, ViewHolder>();
	public LinkedHashMap<Integer, ViewHolder> jgpList = new LinkedHashMap<Integer, ViewHolder>();

	int selectedClaimContentNo = 0;

	final String strWeatherItems[] = CodeInfo.getInstance(this).getNames(ConstVALUE.CODE_TYPE_WEATHER);
	final String strClainContentItems[] = CodeInfo.getInstance(this).getNames(ConstVALUE.CODE_TYPE_GOOD_SECD);

	boolean isAdd;
	String strCurrentDate;
	LocationManager locationManager;

	int sHour;
	int sMin;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_input_jg);

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
		btnTab1.setBackgroundResource(R.drawable.tab_icon_back_on);
		btnTab2 = (Button)findViewById(R.id.btnTab2);
		btnTab2.setTextColor(Color.BLACK);
		btnTab2.setOnClickListener(this);
		btnTab2.setBackgroundResource(R.drawable.tab_jg_2_off);

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

		InputJGUSubInfoDao inputJGUSubInfoDao = InputJGUSubInfoDao.getInstance(this);
		ArrayList<JGUSubInfo> list = inputJGUSubInfoDao.selectList(mInfo.eqpNo);

		InputJGPSubInfoDao inputJGPSubInfoDao = InputJGPSubInfoDao.getInstance(this);
		ArrayList<JGPSubInfo> list2 = inputJGPSubInfoDao.selectList(mInfo.eqpNo);

		Logg.d("sub info - eqpNo : " + mInfo.eqpNo);
		if (list.isEmpty()) {
			btnAdd.setEnabled(false);
		}

		for (JGUSubInfo info : list) {
			Logg.d("sub info : FNCT_LC_DTLS " + info.FNCT_LC_DTLS);
			Logg.d("sub info : FNCT_LC_NO " + info.FNCT_LC_NO);
			Logg.d("sub info : FNCT_LC_DTLS " + info.EQP_NO);
			Logg.d("sub info : FNCT_LC_NO " + info.EQP_NM);
		}

		for (JGPSubInfo info : list2) {
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

			InputJGDao inputJGDao = InputJGDao.getInstance(this);
			jgInfoList1 = inputJGDao.selectJGU(mInfo.master_idx);
			jgInfoList2 = inputJGDao.selectJGP(mInfo.master_idx);

			selectJgInfo = jgInfoList1.get(0);

			strWeather = CodeInfo.getInstance(this).getValue(ConstVALUE.CODE_TYPE_WEATHER, selectJgInfo.weather);
			selectedWeatherNo = StringUtil.getIndex(strWeatherItems, strWeather);

			String strClaimContent = CodeInfo.getInstance(this).getValue(ConstVALUE.CODE_TYPE_GOOD_SECD, selectJgInfo.claim_content);
			btnClaimContent.setText(strClaimContent);
			selectedClaimContentNo = StringUtil.getIndex(strClainContentItems, strClaimContent);

			for (int i=0; i<jgInfoList1.size(); i++) {
				addItem(i, jgInfoList1.get(i));
			}
			for (int i=0; i<jgInfoList2.size(); i++) {
				addItem2(i, jgInfoList2.get(i));
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
				AppUtil.startActivity(InputJGActivity.this, new Intent(InputJGActivity.this, MainActivity.class));
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
			InputJGDao inputJGDao = InputJGDao.getInstance(this);
			inputJGDao.Delete(mInfo.master_idx);
			InspectResultMasterDao insRetDao = InspectResultMasterDao.getInstance(this);
			insRetDao.updateComplete(mInfo.master_idx, "N", ConstVALUE.CODE_NO_INSPECT_JS);
			setResult();
			finish();
			break;
		case R.id.btnCamera:
			Intent intent = new Intent(InputJGActivity.this, CameraManageActivity.class);
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
		InputJGDao inputJGDao = InputJGDao.getInstance(this);
		if (inputJGDao.existJG(mInfo.master_idx))
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
		for (Entry<Integer, ViewHolder> entry : jguList.entrySet()) {
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

		for (Entry<Integer, ViewHolder> entry : jgpList.entrySet()) {
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
		
		InputJGDao inputJGDao = InputJGDao.getInstance(this);
		inputJGDao.Delete(mInfo.master_idx);

		for (Entry<Integer, ViewHolder> entry : jguList.entrySet()) {
			ViewHolder viewHolder = entry.getValue();
			String strEqpNm = viewHolder.tvEqpNm.getText().toString();
			String strMng01 = viewHolder.tvMng01.getText().toString();
			String strMng02 = viewHolder.tvMng02.getText().toString();
			String strUptlvlUplmt = viewHolder.tvUptlvlUplmt.getText().toString();
			String strUptlvlLwlt = viewHolder.tvUptlvlLwlt.getText().toString();
			String strUptlvlIntrcp = viewHolder.tvUptlvlIntrcp.getText().toString();
			String strSd = viewHolder.tvSd.getText().toString();

			String strt1_c1 = viewHolder.t1_editc1.getText().toString();
			String strt1_c2= viewHolder.t1_editc2.getText().toString();
			String strt1_c3 = viewHolder.t1_editc3.getText().toString();
			String strt1_c4= viewHolder.t1_editc4.getText().toString();

			JGInfo log = new JGInfo();
			log.master_idx = mInfo.master_idx;
			log.weather = CodeInfo.getInstance(this).getKey(ConstVALUE.CODE_TYPE_WEATHER, strWeather);
			log.claim_content = CodeInfo.getInstance(this).getKey(ConstVALUE.CODE_TYPE_GOOD_SECD, strClaimContent);;
			log.t_gubun = "1";
			//log.fnct_lc_no = viewHolder.jgInfo.fnct_lc_no;
			//log.fnct_lc_dtls = viewHolder.jgInfo.fnct_lc_dtls;
			//log.eqp_no = viewHolder.jgInfo.eqp_no;
			log.eqp_nm = strEqpNm;
			log.uptlvl_uplmt = strUptlvlUplmt;
			log.uptlvl_lwlt = strUptlvlLwlt;
			log.uptlvl_intrcp = strUptlvlIntrcp;
			log.mng_01 = strMng01;
			log.mng_02 = strMng02;
			log.sd = strSd;
			log.t1_c1 = strt1_c1;
			log.t1_c1 = log.t1_c1.replace("=", "");
			log.t1_c2 = strt1_c2;
			log.t1_c2 = log.t1_c2.replace("=", "");
			log.t1_c3 = strt1_c3;
			log.t1_c3 = log.t1_c3.replace("=", "");
			log.t1_c4 = strt1_c4;
			log.t1_c4 = log.t1_c4.replace("=", "");

			if (isEdit) {
				inputJGDao.Append(log, viewHolder.jgInfo.idx);
			} else {
				inputJGDao.Append(log);
			}
		}

		for (Entry<Integer, ViewHolder> entry : jgpList.entrySet()) {
			ViewHolder viewHolder = entry.getValue();
			String strCircuitName = viewHolder.tvFnctLcNo.getText().toString();
			String strCurrentLoad = viewHolder.tvFnctLcDtls.getText().toString();
			String strConductor_cnt = "";
			String strLocation = "";

			String strt1_c1 = viewHolder.t2_editc1.getText().toString();
			String strt1_c2= viewHolder.t2_editc2.getText().toString();
			String strt1_c3 = viewHolder.t2_editc3.getText().toString();

			JGInfo log = new JGInfo();
			log.master_idx = mInfo.master_idx;
			log.weather = CodeInfo.getInstance(this).getKey(ConstVALUE.CODE_TYPE_WEATHER, strWeather);
			log.fnct_lc_no = viewHolder.jgInfo.fnct_lc_no;
			log.fnct_lc_dtls = strCircuitName;
			log.eqp_no = strCircuitName;
			log.eqp_nm = strCurrentLoad;
			log.claim_content = CodeInfo.getInstance(this).getKey(ConstVALUE.CODE_TYPE_GOOD_SECD, strClaimContent);
			log.t_gubun = "2";
			log.t2_c1 = strt1_c1;
			log.t2_c1 = log.t2_c1.replace("=", "");
			log.t2_c2 = strt1_c2;
			log.t2_c2 = log.t2_c2.replace("=", "");
			log.t2_c3 = strt1_c3;
			log.t2_c3 = log.t2_c3.replace("=", "");

			if (isEdit) {
				inputJGDao.Append(log, viewHolder.jgInfo.idx);
			} else {
				inputJGDao.Append(log);
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
			
			LocManager.getInstance(InputJGActivity.this).setGetGps(true);
			
			String currentLocation = String.format("위도:%f  경도:%f  고도:%f", location.getLatitude(), location.getLongitude(), location.getAltitude());
			Logg.d(InputJGActivity.this, "currentLocation : " + currentLocation);
            Calendar cal = new GregorianCalendar();
            String now = String.format("%d년 %d월 %d일 %d시 %d분 %d초", cal.get(Calendar.YEAR), cal.get(Calendar.MONTH)+1, 
					cal.get(Calendar.DAY_OF_MONTH), cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE),
					cal.get(Calendar.SECOND));
            Logg.d(InputJGActivity.this, "갱신 시간 : " + now);
            
            InspectResultMasterDao insRetDao = InspectResultMasterDao.getInstance(InputJGActivity.this);
            insRetDao.updateNfcTagLocation(mInfo.master_idx, "", location.getLatitude()+"", location.getLongitude()+"", false);
            
            if (locationManager != null) {
            	locationManager.removeUpdates(listener);
            }
		}
	};

	/*유압 리스트*/
	public void addItem(int idx, JGUSubInfo jguSubInfo) {
		JGInfo jgInfo = new JGInfo();
		jgInfo.fnct_lc_no = jguSubInfo.FNCT_LC_NO;
		jgInfo.fnct_lc_dtls = jguSubInfo.FNCT_LC_DTLS;
		jgInfo.eqp_no = jguSubInfo.EQP_NO;
		jgInfo.eqp_nm = jguSubInfo.EQP_NM;
		jgInfo.uptlvl_uplmt = jguSubInfo.UPTLVL_UPLMT;
		jgInfo.uptlvl_lwlt = jguSubInfo.UPTLVL_LWLT;
		jgInfo.uptlvl_intrcp = jguSubInfo.UPTLVL_INTRCP;
		jgInfo.mng_01 = jguSubInfo.MNG_01;
		jgInfo.mng_02 = jguSubInfo.MNG_02;
		jgInfo.sd = jguSubInfo.SD;
		
		addItem(idx, jgInfo);
	}
	
	public void addItem(int idx, JGInfo jgInfo) {
		
		final ViewHolder viewHolder = new ViewHolder();
		View view = LayoutInflater.from(this).inflate(R.layout.item_jg_u, null);
		LinearLayout llItemParent = (LinearLayout) view.findViewById(R.id.llItemParent);
		
		UIUtil.setFont(this, (ViewGroup)llItemParent);
		
		llItemParent.setOnTouchListener(hideKeyboardListener);
		//viewHolder.tvFnctLcNo = (TextView) view.findViewById(R.id.tvFnctLcNo);
		//viewHolder.tvFnctLcDtls = (TextView) view.findViewById(R.id.tvFnctLcDtls);
		//viewHolder.tvEqpNo = (TextView) view.findViewById(R.id.tvEqpNo);
		viewHolder.tvEqpNm = (TextView) view.findViewById(R.id.tvEqpNm);
		viewHolder.tvMng01 = (TextView) view.findViewById(R.id.tvMng01);
		viewHolder.tvMng02 = (TextView) view.findViewById(R.id.tvMng02);
		viewHolder.tvUptlvlUplmt = (TextView) view.findViewById(R.id.tvUptlvlUplmt);
		viewHolder.tvUptlvlLwlt = (TextView) view.findViewById(R.id.tvUptlvlLwlt);
		viewHolder.tvUptlvlIntrcp = (TextView) view.findViewById(R.id.tvUptlvlIntrcp);
		viewHolder.tvSd = (TextView) view.findViewById(R.id.tvSd);

		viewHolder.t1_editc1 = (EditText) view.findViewById(R.id.editc1);
		viewHolder.t1_editc2 = (EditText) view.findViewById(R.id.editc2);
        viewHolder.t1_editc3 = (EditText) view.findViewById(R.id.editc3);
		viewHolder.t1_editc4 = (EditText) view.findViewById(R.id.editc4);
        //setTextWatcher(viewHolder);

		//viewHolder.tvCurrentLoad.setText(idx + "");
		//viewHolder.tvConductor_cnt.setText((idx + 1) + "");
		
		if (jgInfo != null) {
			viewHolder.jgInfo = jgInfo;
			//viewHolder.tvFnctLcNo.setText(jgInfo.fnct_lc_no);
			//viewHolder.tvFnctLcDtls.setText(jgInfo.fnct_lc_dtls);
			//viewHolder.tvEqpNo.setText(jgInfo.eqp_no);
			viewHolder.tvEqpNm.setText(jgInfo.eqp_nm);
			viewHolder.tvUptlvlUplmt.setText(jgInfo.uptlvl_uplmt);
			viewHolder.tvUptlvlLwlt.setText(jgInfo.uptlvl_lwlt);
			viewHolder.tvUptlvlIntrcp.setText(jgInfo.uptlvl_intrcp);
			viewHolder.tvMng01.setText(jgInfo.mng_01);
			viewHolder.tvMng02.setText(jgInfo.mng_02);
			viewHolder.tvSd.setText(jgInfo.sd);

			viewHolder.t1_editc1.setText(jgInfo.t1_c1);
			viewHolder.t1_editc2.setText(jgInfo.t1_c2);
			viewHolder.t1_editc3.setText(jgInfo.t1_c3);
			viewHolder.t1_editc4.setText(jgInfo.t1_c4);
		}

		jguList.put(idx, viewHolder);

		Logg.d("$$$$$$$$$$$$$ jguList t1_editc1 : " + viewHolder.t1_editc1);
		Logg.d("$$$$$$$$$$$$$ jguList jgInfo : " + jgInfo);
		llLayout.addView(view);
		
	}
	
	public class ViewHolder {
		TextView tvFnctLcNo;
		TextView tvFnctLcDtls;
		TextView tvEqpNo;
		TextView tvEqpNm;
		TextView tvMng01;
		TextView tvMng02;
		TextView tvUptlvlUplmt;
		TextView tvUptlvlLwlt;
		TextView tvUptlvlIntrcp;
		TextView tvSd;
		EditText t1_editc1;
        EditText t1_editc2;
        EditText t1_editc3;
        EditText t1_editc4;
		EditText t2_editc1;
		EditText t2_editc2;
		EditText t2_editc3;
        
        JGInfo jgInfo;
	}

	/*피뢰기 리스트*/
	public void addItem2(int idx, JGPSubInfo jgpSubInfo) {
		JGInfo jgInfo = new JGInfo();
		jgInfo.fnct_lc_no = jgpSubInfo.FNCT_LC_DTLS;
		jgInfo.fnct_lc_dtls = jgpSubInfo.FNCT_LC_NO;

		addItem2(idx, jgInfo);
	}

	public void addItem2(int idx, JGInfo jgInfo) {

		final ViewHolder viewHolder = new ViewHolder();
		View view = LayoutInflater.from(this).inflate(R.layout.item_jg_p, null);
		LinearLayout llItemParent = (LinearLayout) view.findViewById(R.id.llItemParent);

		UIUtil.setFont(this, (ViewGroup)llItemParent);

		llItemParent.setOnTouchListener(hideKeyboardListener);
		viewHolder.tvFnctLcNo = (TextView) view.findViewById(R.id.tvCircuitName);
		viewHolder.tvFnctLcDtls = (TextView) view.findViewById(R.id.tvCurrentLoad);
		//viewHolder.tvConductor_cnt = (TextView) view.findViewById(R.id.tvConductor_cnt);
		//viewHolder.tvLocation = (TextView) view.findViewById(R.id.tvLocation);

		viewHolder.t2_editc1 = (EditText) view.findViewById(R.id.editc1);
		viewHolder.t2_editc2 = (EditText) view.findViewById(R.id.editc2);
		viewHolder.t2_editc3 = (EditText) view.findViewById(R.id.editc3);


		if (jgInfo != null) {
			viewHolder.jgInfo = jgInfo;
			viewHolder.tvFnctLcNo.setText(jgInfo.fnct_lc_no);
			viewHolder.tvFnctLcDtls.setText(jgInfo.fnct_lc_dtls);
			//viewHolder.tvConductor_cnt.setText("");
			//viewHolder.tvLocation.setText("");

			viewHolder.t2_editc1.setText(jgInfo.t2_c1);
			viewHolder.t2_editc2.setText(jgInfo.t2_c2);
			viewHolder.t2_editc3.setText(jgInfo.t2_c3);
		}

		jgpList.put(idx, viewHolder);

		llLayout2.addView(view);

	}

	public class ViewHolder2 {
		TextView tvCircuitName;
		TextView tvCurrentLoad;
		EditText editc1;
		EditText editc2;
		EditText editc3;

		JGInfo jgInfo;
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
			btnTab1.setBackgroundResource(R.drawable.tab_icon_back_on);
			btnTab2.setBackgroundResource(R.drawable.tab_icon_back_off);
		} else {
			layout2.setVisibility(View.VISIBLE);
			layout1.setVisibility(View.GONE);
			btnTab1.setBackgroundResource(R.drawable.tab_icon_back_off);
			btnTab2.setBackgroundResource(R.drawable.tab_icon_back_on);
		}
	}
}
