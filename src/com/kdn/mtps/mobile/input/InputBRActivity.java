package com.kdn.mtps.mobile.input;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.LinkedHashMap;
import java.util.Map.Entry;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

import com.kdn.mtps.mobile.BaseActivity;
import com.kdn.mtps.mobile.MainActivity;
import com.kdn.mtps.mobile.R;
import com.kdn.mtps.mobile.TitleManager;
import com.kdn.mtps.mobile.camera.CameraManageActivity;
import com.kdn.mtps.mobile.constant.ConstSP;
import com.kdn.mtps.mobile.constant.ConstVALUE;
import com.kdn.mtps.mobile.db.CompanyListDao;
import com.kdn.mtps.mobile.db.InputBRDao;
import com.kdn.mtps.mobile.db.InputBRSubInfoDao;
import com.kdn.mtps.mobile.db.InspectResultMasterDao;
import com.kdn.mtps.mobile.info.CodeInfo;
import com.kdn.mtps.mobile.inspect.InspectInfo;
import com.kdn.mtps.mobile.util.AppUtil;
import com.kdn.mtps.mobile.util.LocManager;
import com.kdn.mtps.mobile.util.Logg;
import com.kdn.mtps.mobile.util.Shared;
import com.kdn.mtps.mobile.util.StringUtil;
import com.kdn.mtps.mobile.util.UIUtil;

public class InputBRActivity extends BaseActivity implements TitleManager, OnClickListener {
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
	
	ArrayList<BRInfo> brInfoList;
	BRInfo selectBRInfo;
	
	InspectInfo mInfo;
	
	int selectedWeatherNo = 0;
	
	final String strWeatherItems[] = CodeInfo.getInstance(this).getNames(ConstVALUE.CODE_TYPE_WEATHER);
	final String strYBItems[] = CodeInfo.getInstance(this).getNames(ConstVALUE.CODE_TYPE_GOOD_SECD);
	
	boolean isAdd;
	String strCurrentDate;
	LocationManager locationManager;
	
	public LinkedHashMap<Integer, ViewHolder> brList = new LinkedHashMap<Integer, ViewHolder>();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_input_br);
		
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
		
		InputBRSubInfoDao inputBRSubInfoDao = InputBRSubInfoDao.getInstance(this);
		ArrayList<BRSubInfo> list = inputBRSubInfoDao.selectList(mInfo.eqpNo);
		
		if (list.isEmpty()) {
			btnAdd.setEnabled(false);
		}
		
		if (isAdd) {
			ivComplte.setBackgroundResource(R.drawable.input_br_add);
			
			strWeather = Shared.getString(this, ConstSP.SETTING_WEATHER);
			
			for (int i=0; i<list.size(); i++) {
				list.get(i).INSBTY_LFT = "좌";
				
				addItem(i, list.get(i));
			}
			
			addBottomPadding();
		} else {
			ivComplte.setBackgroundResource(R.drawable.input_br_edit);
			
			InputBRDao inputBRDao = InputBRDao.getInstance(this);
			brInfoList = inputBRDao.selectBR(mInfo.master_idx);
			
			selectBRInfo = brInfoList.get(0);
			
			strWeather = CodeInfo.getInstance(this).getValue(ConstVALUE.CODE_TYPE_WEATHER, selectBRInfo.weather);
			selectedWeatherNo = StringUtil.getIndex(strWeatherItems, strWeather);
			
			for (int i=0; i<list.size(); i++) {
				addItem(i, brInfoList.get(i));
			}
			
			addBottomPadding();
		}
		
//		if (mInfo.send_yn_br != null && !"".equals(mInfo.send_yn_br)) {
//			ivComplte.setBackgroundResource(R.drawable.input_br_send);
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
				AppUtil.startActivity(InputBRActivity.this, new Intent(InputBRActivity.this, MainActivity.class));
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
			InputBRDao inputBRDao = InputBRDao.getInstance(this);
			inputBRDao.Delete(mInfo.master_idx);
			InspectResultMasterDao insRetDao = InspectResultMasterDao.getInstance(this);
			insRetDao.updateComplete(mInfo.master_idx, "N", ConstVALUE.CODE_NO_INSPECT_BR);
			setResult();
			finish();
			break;
		case R.id.btnCamera:
			Intent intent = new Intent(InputBRActivity.this, CameraManageActivity.class);
			intent.putExtra("inspect", mInfo);
			startActivity(intent);
			break;
		default:
			break;
		}
	}

	public void setInputType() {
		InputBRDao inputBRDao = InputBRDao.getInstance(this);
		if (inputBRDao.existBR(mInfo.master_idx))
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
		InputBRDao inputBRDao = InputBRDao.getInstance(this);
		inputBRDao.Delete(mInfo.master_idx);
		
		for (Entry<Integer, ViewHolder> entry : brList.entrySet()) {
			ViewHolder viewHolder = entry.getValue();
			String strYBResult = viewHolder.btnYbResult.getText().toString();
			
			BRInfo log = new BRInfo();
			log.masterIdx = mInfo.master_idx;
			log.weather = CodeInfo.getInstance(this).getKey(ConstVALUE.CODE_TYPE_WEATHER, strWeather);
			log.circuit_no = viewHolder.brInfo.circuit_no;
			log.circuit_name = viewHolder.tvCircuitName.getText().toString();
			log.ty_secd = viewHolder.brInfo.ty_secd;
			log.ty_secd_nm = viewHolder.tvLocation.getText().toString();
			log.ej_cnt = viewHolder.editEJCnt.getText().toString();
			log.ej_cnt = log.ej_cnt.replace("=", "");
			log.br_cnt = viewHolder.editBRCnt.getText().toString();
			log.br_cnt = log.br_cnt.replace("=", "");
			log.make_date = viewHolder.btnMakeDate.getText().toString();
			log.make_company = viewHolder.editMakeCompany.getText().toString();
			log.make_company = log.make_company.replace("=", "");
			log.yb_result = CodeInfo.getInstance(this).getKey(ConstVALUE.CODE_TYPE_GOOD_SECD, strYBResult);
			log.insbty_lft = viewHolder.brInfo.insbty_lft;
			log.insbty_rit = viewHolder.brInfo.insbty_rit;
			log.phs_secd = viewHolder.brInfo.phs_secd;
			log.phs_secd_nm = viewHolder.tvPosition.getText().toString();
			log.insbty_lft = viewHolder.brInfo.insbty_lft;
			log.insbty_rit = viewHolder.brInfo.insbty_rit;
			log.insr_eqp_no = viewHolder.brInfo.insr_eqp_no;
			
			CompanyListDao companyListDao = CompanyListDao.getInstance(this);
			if (!"".equals(log.make_company) && !companyListDao.exist(log.make_company))
				companyListDao.Append(log.make_company);
			
			
			if (isEdit)
				inputBRDao.Append(log, viewHolder.brInfo.idx);
			else {
				inputBRDao.Append(log);
			}
		}
		
		if (!isEdit)
			LocManager.getInstance(this).startGetLocation(listener);
		
		InspectResultMasterDao insRetDao = InspectResultMasterDao.getInstance(this);
		insRetDao.updateComplete(mInfo.master_idx, "Y", ConstVALUE.CODE_NO_INSPECT_BR);
		
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
			
			LocManager.getInstance(InputBRActivity.this).setGetGps(true);
			
			String currentLocation = String.format("위도:%f  경도:%f  고도:%f", location.getLatitude(), location.getLongitude(), location.getAltitude());
			Logg.d(InputBRActivity.this, "currentLocation : " + currentLocation);
            Calendar cal = new GregorianCalendar();
            String now = String.format("%d년 %d월 %d일 %d시 %d분 %d초", cal.get(Calendar.YEAR), cal.get(Calendar.MONTH)+1, 
					cal.get(Calendar.DAY_OF_MONTH), cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE),
					cal.get(Calendar.SECOND));
            Logg.d(InputBRActivity.this, "갱신 시간 : " + now);
            
            InspectResultMasterDao insRetDao = InspectResultMasterDao.getInstance(InputBRActivity.this);
            insRetDao.updateNfcTagLocation(mInfo.master_idx, "", location.getLatitude()+"", location.getLongitude()+"", false);
            
            if (locationManager != null) {
            	locationManager.removeUpdates(listener);
            }
		}
	};

	public void addItem(int idx, BRSubInfo brSubInfo) {
		BRInfo brInfo = new BRInfo();
		brInfo.circuit_no = brSubInfo.CL_NO;
		brInfo.circuit_name = brSubInfo.CL_NM;
		brInfo.insbty_lft = brSubInfo.INSBTY_LFT;
		brInfo.insbty_rit = brSubInfo.INSBTY_RIT;
		brInfo.ty_secd = brSubInfo.TY_SECD;
		brInfo.ty_secd_nm = brSubInfo.TY_SECD_NM;
		brInfo.phs_secd = brSubInfo.PHS_SECD;
		brInfo.phs_secd_nm = brSubInfo.PHS_SECD_NM;
		brInfo.ej_cnt = brSubInfo.INS_CNT;
		brInfo.yb_result = CodeInfo.getInstance(this).getKey(ConstVALUE.CODE_TYPE_GOOD_SECD, strYBItems[0]);
		brInfo.insr_eqp_no = brSubInfo.INSR_EQP_NO;
		
		addItem(idx, brInfo);
	}
	
	public void addItem(int idx, BRInfo brInfo) {
		
		final ViewHolder viewHolder = new ViewHolder();
		View view = LayoutInflater.from(this).inflate(R.layout.item_br, null);
		LinearLayout llItemParent = (LinearLayout) view.findViewById(R.id.llItemParent);
		
		UIUtil.setFont(this, (ViewGroup)llItemParent);
		
		llItemParent.setOnTouchListener(hideKeyboardListener);
		viewHolder.tvLeft = (TextView) view.findViewById(R.id.tvLeft);
		viewHolder.tvRight = (TextView) view.findViewById(R.id.tvRight);
		viewHolder.tvCircuitName = (TextView) view.findViewById(R.id.tvCircuitName);
		viewHolder.tvLocation = (TextView) view.findViewById(R.id.tvLocation);
		viewHolder.tvPosition = (TextView) view.findViewById(R.id.tvPosition);
		viewHolder.editEJCnt = (EditText) view.findViewById(R.id.editEJCnt);
		viewHolder.editBRCnt  = (EditText)view.findViewById(R.id.editBRCnt);
		viewHolder.btnMakeDate  = (Button)view.findViewById(R.id.btnMakeDate);
		
		String strDate = new SimpleDateFormat("yyyy-MM-dd").format(System.currentTimeMillis());
		viewHolder.btnMakeDate.setText(strDate);
		
		viewHolder.btnMakeDate.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				showMakeDateDialog((Button)v);
			}
		});
		
		viewHolder.editMakeCompany = (EditText)view.findViewById(R.id.editMakeCompany);
		viewHolder.editMakeCompany.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if (event.getAction() == MotionEvent.ACTION_UP)
					showSetCompanyDialog((EditText)v);
				return false;
			}
		});
		
		viewHolder.editBRCnt.setNextFocusDownId(R.id.editMakeCompany);
		viewHolder.btnYbResult  = (Button)view.findViewById(R.id.btnYBresult);
        viewHolder.btnYbResult.setId(0);
        viewHolder.btnYbResult.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				showYBResultDialog((Button)v);
			}
		});
        
        viewHolder.btnYbResult.setText(strYBItems[0]);
		
        viewHolder.editMakeCompany.setTag(idx);
        viewHolder.editMakeCompany.setOnEditorActionListener(new OnEditorActionListener() {
			@Override
			public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
				if (actionId == EditorInfo.IME_ACTION_NEXT) {
					int idx = (Integer) v.getTag();
					brList.get(idx+1).editEJCnt.requestFocus();
				}
				return false;
			}
		});
        
		if (brInfo != null) {
			viewHolder.brInfo = brInfo;
			
			viewHolder.tvLeft.setText(brInfo.insbty_lft);
			viewHolder.tvRight.setText(brInfo.insbty_rit);
			viewHolder.tvCircuitName.setText(brInfo.circuit_name);
			viewHolder.tvLocation.setText(brInfo.ty_secd_nm);
			viewHolder.tvPosition.setText(brInfo.phs_secd_nm);
			viewHolder.editEJCnt.setText(brInfo.ej_cnt);
			viewHolder.editBRCnt.setText(brInfo.br_cnt);
			if (brInfo.make_date != null && !"".equals(brInfo.make_date))
				viewHolder.btnMakeDate.setText(brInfo.make_date);
			viewHolder.editMakeCompany.setText(brInfo.make_company);
			
			String strYBResult = CodeInfo.getInstance(this).getValue(ConstVALUE.CODE_TYPE_GOOD_SECD, brInfo.yb_result);
			viewHolder.btnYbResult.setText(strYBResult);
			int index = StringUtil.getIndex(strYBItems, strYBResult);
			viewHolder.btnYbResult.setId(index);
			
			brInfo.circuit_no = brInfo.circuit_no;
			brInfo.insbty_lft = brInfo.insbty_lft;
			brInfo.insbty_rit = brInfo.insbty_rit;
			brInfo.ty_secd = brInfo.ty_secd;
			brInfo.phs_secd = brInfo.phs_secd;
			brInfo.insr_eqp_no = brInfo.insr_eqp_no;
			
		}
		
		brList.put(idx, viewHolder);
		llLayout.addView(view);
		
	}
	
	public class ViewHolder {
		TextView tvLeft;
		TextView tvRight;
		TextView tvCircuitName;
		TextView tvLocation;
		TextView tvPosition;
		EditText editEJCnt;
		EditText editBRCnt;
		Button btnMakeDate;
		EditText editMakeCompany;
		Button btnYbResult;
		
		BRInfo brInfo;
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
	
	public void showMakeDateDialog(Button btn) {
		String strDate = btn.getText().toString();
		
		String[] dates = strDate.split("-");
		int nyear = Integer.valueOf(dates[0]);
		int nmonth = Integer.valueOf(dates[1]) -1;
		int nday = Integer.valueOf(dates[2]);
		
		
		DatePickerDialog datePick = new DatePickerDialog(this, mDateListener, nyear, nmonth, nday);
		DatePicker dp = datePick.getDatePicker();
		dp.setTag(btn);
		datePick.show();
	}
	
	public void showSetCompanyDialog(final EditText editText) {
		
		CompanyListDao companyListDao = CompanyListDao.getInstance(this);
		ArrayList<String> list = companyListDao.selectCompanyList();
		if (list.size() <= 1)
			return;
		
		AlertDialog dialog = new AlertDialog.Builder(this, AlertDialog.THEME_DEVICE_DEFAULT_LIGHT).create();

		View v = View.inflate(this, R.layout.input_br_make_company, null);
		ListView listview = (ListView)v.findViewById(R.id.listCompany);
		
		CompanyListBaseAdapter adapter = new CompanyListBaseAdapter(this, editText, dialog);
		listview.setAdapter(adapter);
		
		adapter.setList(list);
		
		dialog.setView(v);
		dialog.setTitle(getString(R.string.make_company));
		dialog.show();
	}

	private DatePickerDialog.OnDateSetListener mDateListener = new DatePickerDialog.OnDateSetListener() {
		@Override
		public void onDateSet(DatePicker view, int year, int month, int day) {
			
			month = month + 1;
			String newMonth = "";

			if (month < 10)
				newMonth = "0" + month;
			else
				newMonth = "" + month;
			
			Button btn = (Button)view.getTag();
			btn.setText(year + "-" + newMonth + "-" + day);
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
