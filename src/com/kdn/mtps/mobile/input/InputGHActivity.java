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
import android.text.Editable;
import android.text.TextWatcher;
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
import com.kdn.mtps.mobile.db.InputJGDao;
import com.kdn.mtps.mobile.db.InputJGSubInfo2Dao;
import com.kdn.mtps.mobile.db.InputJGSubInfoDao;
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

public class InputGHActivity extends BaseActivity implements TitleManager, OnClickListener {

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
	Button btnClaimContent;
	Button btnStartT;

	ImageView ivComplte;

	LinearLayout llLayout;

	ArrayList<JGInfo> jgInfoList;
	JGInfo selectJgInfo;
	LinearLayout linearDateList;

	FrameLayout layout1;

	InspectInfo mInfo;

	int selectedWeatherNo = 0;

	public LinkedHashMap<Integer, ViewHolder> jgList = new LinkedHashMap<Integer, ViewHolder>();

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
		setContentView(R.layout.activity_input_gh);

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

		llLayout = (LinearLayout)findViewById(R.id.llLayout);

		ivComplte = (ImageView)findViewById(R.id.ivComplte);


		linearDateList = (LinearLayout)findViewById(R.id.linearDateList);

		layout1 = (FrameLayout)findViewById(R.id.layout_1);
	}

	public void setData() {

		tvName.setText(mInfo.tracksName);
		tvDate.setText(StringUtil.printDate(mInfo.date));
		tvEqpNm.setText(mInfo.eqpNm);

		String insType = CodeInfo.getInstance(this).getValue(ConstVALUE.CODE_TYPE_INS_TYPE, mInfo.type);
		tvType.setText(insType);

		InputJGSubInfoDao inputJGSubInfoDao = InputJGSubInfoDao.getInstance(this);
		ArrayList<JGSubInfo> list = inputJGSubInfoDao.selectList(mInfo.eqpNo);

		Logg.d("sub info - eqpNo : " + mInfo.eqpNo);
		if (list.isEmpty()) {
			btnAdd.setEnabled(false);
		}

		for (JGSubInfo info : list) {
			Logg.d("sub info : " + info.FNCT_LC_DTLS);
		}

		if (isAdd) {
			ivComplte.setBackgroundResource(R.drawable.input_js_add);

			strWeather = Shared.getString(this, ConstSP.SETTING_WEATHER);

			btnClaimContent.setText(strClainContentItems[0]);
			for (int i=0; i<list.size(); i++) {
				addItem(i, list.get(i));
			}

			addBottomPadding();
		} else {
			ivComplte.setBackgroundResource(R.drawable.input_js_edit);

			InputJGDao inputJGDao = InputJGDao.getInstance(this);
			jgInfoList = inputJGDao.selectJG(mInfo.master_idx);

			selectJgInfo = jgInfoList.get(0);

			strWeather = CodeInfo.getInstance(this).getValue(ConstVALUE.CODE_TYPE_WEATHER, selectJgInfo.weather);
			selectedWeatherNo = StringUtil.getIndex(strWeatherItems, strWeather);

			//String strClaimContent = CodeInfo.getInstance(this).getValue(ConstVALUE.CODE_TYPE_GOOD_SECD, selectJgInfo.claim_content);
			//btnClaimContent.setText(strClaimContent);
			//selectedClaimContentNo = StringUtil.getIndex(strClainContentItems, strClaimContent);

			for (int i=0; i<jgInfoList.size(); i++) {
				addItem(i, jgInfoList.get(i));
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
				AppUtil.startActivity(InputGHActivity.this, new Intent(InputGHActivity.this, MainActivity.class));
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
			Intent intent = new Intent(InputGHActivity.this, CameraManageActivity.class);
			intent.putExtra("inspect", mInfo);
			startActivity(intent);
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
		for (Entry<Integer, ViewHolder> entry : jgList.entrySet()) {
			ViewHolder viewHolder = entry.getValue();
			String strc1_js = viewHolder.editc1_js.getText().toString();
			String strc1_jsj= viewHolder.editc1_jsj.getText().toString();
			String strc2_js = viewHolder.editc2_js.getText().toString();
			String strc2_jsj= viewHolder.editc2_jsj.getText().toString();

			if (!"".equals(strc1_js) || !"".equals(strc1_jsj) || !"".equals(strc2_js) || !"".equals(strc2_jsj) ) {
				isEmpty = false;
				break;
			}
		}
		
		if (isEmpty) {
			ToastUtil.show(this, "정보를 입력해 주시기 바랍니다.");
			return;
		}
		
		InputJGDao inputJGDao = InputJGDao.getInstance(this);
		inputJGDao.Delete(mInfo.master_idx);
		
		for (Entry<Integer, ViewHolder> entry : jgList.entrySet()) {
			ViewHolder viewHolder = entry.getValue();
			String strCircuitName = viewHolder.tvCircuitName.getText().toString();
			String strCurrentLoad = viewHolder.tvCurrentLoad.getText().toString();
			String strConductor_cnt = viewHolder.tvConductor_cnt.getText().toString();
			String strLocation = viewHolder.tvLocation.getText().toString();
					
			String strc1_js = viewHolder.editc1_js.getText().toString();
			String strc1_jsj= viewHolder.editc1_jsj.getText().toString();
			String strc2_js = viewHolder.editc2_js.getText().toString();
			String strc2_jsj= viewHolder.editc2_jsj.getText().toString();
			
			JGInfo log = new JGInfo();
			log.master_idx = mInfo.master_idx;
			log.weather = CodeInfo.getInstance(this).getKey(ConstVALUE.CODE_TYPE_WEATHER, strWeather);
			log.circuit_no = viewHolder.jgInfo.circuit_no;
			log.circuit_name = strCircuitName;
			log.circuit_name = strCircuitName;
			log.current_load = strCurrentLoad;
			log.conductor_cnt = strConductor_cnt;
			log.location = strLocation;
			log.c1_js = strc1_js;
			log.c1_js = log.c1_js.replace("=", "");
			log.c1_jsj = strc1_jsj;
			log.c1_jsj = log.c1_jsj.replace("=", "");
			log.c1_power_no = viewHolder.jgInfo.c1_power_no;
			log.c2_js = strc2_js;
			log.c2_js = log.c2_js.replace("=", "");
			log.c2_jsj = strc2_jsj;
			log.c2_jsj = log.c2_jsj.replace("=", "");
			log.c2_power_no = viewHolder.jgInfo.c2_power_no;
			
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
			
			LocManager.getInstance(InputGHActivity.this).setGetGps(true);
			
			String currentLocation = String.format("위도:%f  경도:%f  고도:%f", location.getLatitude(), location.getLongitude(), location.getAltitude());
			Logg.d(InputGHActivity.this, "currentLocation : " + currentLocation);
            Calendar cal = new GregorianCalendar();
            String now = String.format("%d년 %d월 %d일 %d시 %d분 %d초", cal.get(Calendar.YEAR), cal.get(Calendar.MONTH)+1, 
					cal.get(Calendar.DAY_OF_MONTH), cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE),
					cal.get(Calendar.SECOND));
            Logg.d(InputGHActivity.this, "갱신 시간 : " + now);
            
            InspectResultMasterDao insRetDao = InspectResultMasterDao.getInstance(InputGHActivity.this);
            insRetDao.updateNfcTagLocation(mInfo.master_idx, "", location.getLatitude()+"", location.getLongitude()+"", false);
            
            if (locationManager != null) {
            	locationManager.removeUpdates(listener);
            }
		}
	};

	/*유압 리스트*/
	public void addItem(int idx, JGSubInfo jgSubInfo) {
		JGInfo jgInfo = new JGInfo();
		jgInfo.conductor_cnt = jgSubInfo.CONT_NUM;
		jgInfo.location = jgSubInfo.SN;
		jgInfo.current_load = jgSubInfo.TTM_LOAD;
		jgInfo.circuit_name = jgSubInfo.FNCT_LC_DTLS;
		jgInfo.circuit_no = jgSubInfo.FNCT_LC_NO;
		jgInfo.c1_power_no = jgSubInfo.POWER_NO_C1;
		jgInfo.c2_power_no = jgSubInfo.POWER_NO_C2;
		jgInfo.c3_power_no = jgSubInfo.POWER_NO_C3;
				
		
		addItem(idx, jgInfo);
	}
	
	public void addItem(int idx, JGInfo jgInfo) {
		
		final ViewHolder viewHolder = new ViewHolder();
		View view = LayoutInflater.from(this).inflate(R.layout.item_jg, null);
		LinearLayout llItemParent = (LinearLayout) view.findViewById(R.id.llItemParent);
		
		UIUtil.setFont(this, (ViewGroup)llItemParent);
		
		llItemParent.setOnTouchListener(hideKeyboardListener);
		viewHolder.tvCircuitName = (TextView) view.findViewById(R.id.tvCircuitName); 
		viewHolder.tvCurrentLoad = (TextView) view.findViewById(R.id.tvCurrentLoad);
		viewHolder.tvConductor_cnt = (TextView) view.findViewById(R.id.tvConductor_cnt);
		viewHolder.tvLocation = (TextView) view.findViewById(R.id.tvLocation);
		viewHolder.tvc1_temp = (TextView) view.findViewById(R.id.tvc1_temp);
		viewHolder.tvc2_temp = (TextView) view.findViewById(R.id.tvc2_temp);
		viewHolder.tvc3_temp = (TextView) view.findViewById(R.id.tvc3_temp);

		viewHolder.editc1_js = (EditText) view.findViewById(R.id.editc1);
        viewHolder.editc1_jsj = (EditText) view.findViewById(R.id.editc1_jsj);
        viewHolder.editc2_js = (EditText) view.findViewById(R.id.editc3);
        viewHolder.editc2_jsj = (EditText) view.findViewById(R.id.editc2);
        setTextWatcher(viewHolder);

		viewHolder.tvCurrentLoad.setText(idx + "");
		viewHolder.tvConductor_cnt.setText((idx + 1) + "");
		
		if (jgInfo != null) {
			viewHolder.jgInfo = jgInfo;
			viewHolder.tvCircuitName.setText(jgInfo.circuit_name);
			viewHolder.tvCurrentLoad.setText(jgInfo.current_load);
			viewHolder.tvConductor_cnt.setText(jgInfo.conductor_cnt);
			viewHolder.tvLocation.setText(jgInfo.location);

			setDiff(jgInfo.c1_js, jgInfo.c1_jsj, viewHolder.tvc1_temp);
			setDiff(jgInfo.c2_js, jgInfo.c2_jsj, viewHolder.tvc2_temp);
			setDiff(jgInfo.c3_js, jgInfo.c3_jsj, viewHolder.tvc3_temp);

			viewHolder.editc1_js.setText(jgInfo.c1_js);
			viewHolder.editc1_jsj.setText(jgInfo.c1_jsj);
			viewHolder.editc2_js.setText(jgInfo.c2_js);
			viewHolder.editc2_jsj.setText(jgInfo.c2_jsj);
		}
		
		jgList.put(idx, viewHolder);
		
		llLayout.addView(view);
		
	}
	
	public class ViewHolder {
		TextView tvCircuitName;
		TextView tvCurrentLoad;
		TextView tvConductor_cnt;
		TextView tvLocation;
		TextView tvc1_temp;
		TextView tvc2_temp;
		TextView tvc3_temp;
        EditText editc1_js;
        EditText editc1_jsj;
        EditText editc2_js;
        EditText editc2_jsj;
        
        JGInfo jgInfo;
	}

	public void setTextWatcher(final ViewHolder viewHolder) {
		viewHolder.editc1_js.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				String str1 = viewHolder.editc1_js.getText().toString();
				String str2 = viewHolder.editc1_jsj.getText().toString();
				setDiff(str1, str2, viewHolder.tvc1_temp);
			}
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
			@Override
			public void afterTextChanged(Editable s) {}
		});
		viewHolder.editc1_jsj.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				String str1 = viewHolder.editc1_js.getText().toString();
				String str2 = viewHolder.editc1_jsj.getText().toString();
				setDiff(str1, str2, viewHolder.tvc1_temp);
			}
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
			@Override
			public void afterTextChanged(Editable s) {}
		});

		viewHolder.editc2_js.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				String str1 = viewHolder.editc2_js.getText().toString();
				String str2 = viewHolder.editc2_jsj.getText().toString();
				setDiff(str1, str2, viewHolder.tvc2_temp);
			}
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
			@Override
			public void afterTextChanged(Editable s) {}
		});
		viewHolder.editc2_jsj.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				String str1 = viewHolder.editc2_js.getText().toString();
				String str2 = viewHolder.editc2_jsj.getText().toString();
				setDiff(str1, str2, viewHolder.tvc2_temp);
			}
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
			@Override
			public void afterTextChanged(Editable s) {}
		});
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
}
