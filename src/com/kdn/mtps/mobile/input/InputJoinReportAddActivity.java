package com.kdn.mtps.mobile.input;


import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.widget.TimePicker;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Bundle;
import android.util.*;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.kdn.mtps.mobile.BaseActivity;
import com.kdn.mtps.mobile.MainActivity;
import com.kdn.mtps.mobile.R;
import com.kdn.mtps.mobile.TitleManager;
import com.kdn.mtps.mobile.camera.CameraManageActivity;
import com.kdn.mtps.mobile.constant.ConstSP;
import com.kdn.mtps.mobile.constant.ConstVALUE;
import com.kdn.mtps.mobile.custom.CustomDatePickerDialog;
import com.kdn.mtps.mobile.db.InputBTDao;
import com.kdn.mtps.mobile.db.InputJoinReportDao;
import com.kdn.mtps.mobile.db.InspectResultMasterDao;
import com.kdn.mtps.mobile.db.bean.InputBTLog;
import com.kdn.mtps.mobile.info.CodeInfo;
import com.kdn.mtps.mobile.inspect.InspectInfo;
import com.kdn.mtps.mobile.inspect.InspectResultDetailActivity;
import com.kdn.mtps.mobile.util.AppUtil;
import com.kdn.mtps.mobile.util.LocManager;
import com.kdn.mtps.mobile.util.Logg;
import com.kdn.mtps.mobile.util.Shared;
import com.kdn.mtps.mobile.util.StringUtil;
import com.kdn.mtps.mobile.util.UIUtil;
import com.kdn.mtps.mobile.util.Logg;

public class InputJoinReportAddActivity extends BaseActivity implements TitleManager, OnClickListener {

	LinearLayout llParent;
	ScrollView svParent;
	EditText editName;
	TextView tvEqpNm;

	boolean fired = false;
	int mYear, mMonth, mDay, mHour, mMinute;
	int dateGubun = 0;
	int timeGubun = 0;

	String strWeather;
	Button btnWorkerCnt;

	Button btnJoinSDate;
	Button btnJoinEDate;
	Button btnJoinSTime;
	Button btnJoinETime;
	Button btnJoinDate;
	Button btnRequestDate;

	Button btnAdd;
	Button btnEdit;
	Button btnDelete;

	EditText editLength;
	TextView tvViewPoint;
	TextView tvEndPoint;

	EditText editLocation;
	EditText editWorkNm;
	EditText editRequestJoinerCo;
	EditText editRequestJoiner;
	EditText editJoinerDept;
	EditText editJoiner;

	EditText editJoinReason;
	EditText editEtc;

	ImageView ivComplte;

	JoinReportInfo selectJoinReportInfo;

//	ListView listBT;
//	InputBTAdapter adapter;
//	ArrayList<BTInfo> BtList;

	LocationManager locationManager;

	InspectInfo mInfo;

	int selectedWeatherNo = 0;
	int selectedWorkerCntNo = 0;
	int selectedClaimContentNo = 0;

	//	final String strWeatherItems[] = {"맑음", "비", "흐림", "눈"};
	final String strWeatherItems[] = CodeInfo.getInstance(this).getNames(ConstVALUE.CODE_TYPE_WEATHER);
	final String strWorkerCntItems[] = {"0명","1명","2명","3명","4명","5명"};
	final String strClainContentItems[] = CodeInfo.getInstance(this).getNames(ConstVALUE.CODE_TYPE_GOOD_SECD);

	boolean isAdd;
	String strJoinDate;
	String strIdx;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_join_report_add);
		selectJoinReportInfo = getIntent().getParcelableExtra("joinReportInfo");
		strJoinDate = getIntent().getStringExtra("joinDate");
		strIdx = getIntent().getStringExtra("idx");
		Calendar cal = new GregorianCalendar();
		mYear = cal.get(Calendar.YEAR);
		mMonth = cal.get(Calendar.MONTH);
		mDay = cal.get(Calendar.DAY_OF_MONTH);
		mHour = cal.get(Calendar.HOUR_OF_DAY);
		mMinute = cal.get(Calendar.MINUTE);

		setInit();

		setInputType();

	}

	public void setInit() {
		llParent = (LinearLayout)findViewById(R.id.llParent);
		llParent.setOnTouchListener(hideKeyboardListener);

		svParent = (ScrollView)findViewById(R.id.svParent);
		svParent.setOnTouchListener(hideKeyboardListener);

		editName = (EditText)findViewById(R.id.editName);
		tvEqpNm = (TextView)	findViewById(R.id.tvEqpNm);

		btnWorkerCnt = (Button)findViewById(R.id.btnWorkerCnt);
		editJoinReason = (EditText)findViewById(R.id.editJoinReason);
		editEtc = (EditText)findViewById(R.id.editEtc);

		editJoinReason.setOnKeyListener(new OnKeyListener() {
			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				if (((EditText)v).getLineCount() == 4 && keyCode == event.KEYCODE_ENTER)
					return true;
				return false;
			}
		});

		editEtc.setOnKeyListener(new OnKeyListener() {
			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				if (((EditText)v).getLineCount() == 4 && keyCode == event.KEYCODE_ENTER)
					return true;
				return false;
			}
		});

		btnJoinDate = (Button)findViewById(R.id.btnJoinDate);
		btnRequestDate = (Button)findViewById(R.id.btnRequestDate);
		btnJoinSDate = (Button)findViewById(R.id.btnJoinSDate);
		btnJoinEDate = (Button)findViewById(R.id.btnJoinEDate);
		btnJoinSTime = (Button)findViewById(R.id.btnJoinSTime);
		btnJoinETime = (Button)findViewById(R.id.btnJoinETime);
		btnJoinDate.setOnClickListener(this);
		btnRequestDate.setOnClickListener(this);
		btnJoinSDate.setOnClickListener(this);
		btnJoinEDate.setOnClickListener(this);
		btnJoinSTime.setOnClickListener(this);
		btnJoinETime.setOnClickListener(this);

		btnWorkerCnt.setOnClickListener(this);

		btnAdd = (Button)findViewById(R.id.btnAdd);
		btnAdd.setOnClickListener(this);
		btnEdit = (Button)findViewById(R.id.btnEdit);
		btnDelete = (Button)findViewById(R.id.btnDelete);
		btnEdit.setOnClickListener(this);
		btnDelete.setOnClickListener(this);

		tvViewPoint  = (TextView)findViewById(R.id.tvViewPoint);
		tvEndPoint   = (TextView)findViewById(R.id.tvEndPoint);
		editLength = (EditText)findViewById(R.id.editLength);

		editLocation = (EditText)findViewById(R.id.editLocation);
		editWorkNm = (EditText)findViewById(R.id.editWorkNm);
		editRequestJoinerCo = (EditText)findViewById(R.id.editRequestJoinerCo);
		editRequestJoiner = (EditText)findViewById(R.id.editRequestJoiner);
		editJoinerDept = (EditText)findViewById(R.id.editJoinerDept);
		editJoiner = (EditText)findViewById(R.id.editJoiner);

		ivComplte = (ImageView)findViewById(R.id.ivComplte);
	}

	public void setData() {

		InputJoinReportDao inputJoinReportDao = InputJoinReportDao.getInstance(this);

		if (isAdd) {

			strWeather = Shared.getString(this, ConstSP.SETTING_WEATHER);
			btnWorkerCnt.setText(strWorkerCntItems[0]);
			editEtc.setText("");

		} else {
			selectJoinReportInfo = inputJoinReportDao.selectJoinReport(String.valueOf(selectJoinReportInfo.idx));

			strWeather = CodeInfo.getInstance(this).getValue(ConstVALUE.CODE_TYPE_WEATHER, selectJoinReportInfo.weather);

			String strJoinDate = selectJoinReportInfo.join_date.substring(0,4) + "." + selectJoinReportInfo.join_date.substring(4,6) + "." +selectJoinReportInfo.join_date.substring(6,8);
			String strRequestDate = selectJoinReportInfo.request_date.substring(0,4) + "." + selectJoinReportInfo.request_date.substring(4,6) + "." +selectJoinReportInfo.request_date.substring(6,8);
			String strJoinSDate = selectJoinReportInfo.join_s_date.substring(0,4) + "." + selectJoinReportInfo.join_s_date.substring(4,6) + "." +selectJoinReportInfo.join_s_date.substring(6,8);
			String strJoinSTime = selectJoinReportInfo.join_s_time.substring(0,2) + ":" + selectJoinReportInfo.join_s_time.substring(2,4);
			String strJoinEDate = selectJoinReportInfo.join_e_date.substring(0,4) + "." + selectJoinReportInfo.join_e_date.substring(4,6) + "." +selectJoinReportInfo.join_e_date.substring(6,8);
			String strJoinETime = selectJoinReportInfo.join_e_time.substring(0,2) + ":" + selectJoinReportInfo.join_e_time.substring(2,4);
			editName.setText(selectJoinReportInfo.name);
			editLength.setText(selectJoinReportInfo.length);
			editLocation.setText(selectJoinReportInfo.location);
			editWorkNm.setText(selectJoinReportInfo.work_nm);
			btnJoinDate.setText(strJoinDate);
			btnRequestDate.setText(strRequestDate);
			btnJoinSDate.setText(strJoinSDate);
			btnJoinSTime.setText(strJoinSTime);
			btnJoinEDate.setText(strJoinEDate);
			btnJoinETime.setText(strJoinETime);
			editRequestJoinerCo.setText(selectJoinReportInfo.request_joiner_co);
			editRequestJoiner.setText(selectJoinReportInfo.request_joiner);
			editJoinerDept.setText(selectJoinReportInfo.joiner_dept);
			editJoiner.setText(selectJoinReportInfo.joiner);
			editJoinReason.setText(selectJoinReportInfo.join_reason);
			editEtc.setText(selectJoinReportInfo.etc);
		}

//		if (mInfo.send_yn_bt != null && !"".equals(mInfo.send_yn_bt)) {
//			ivComplte.setBackgroundResource(R.drawable.input_bt_send);
//		}
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

		//String insType = CodeInfo.getInstance(this).getValue(ConstVALUE.CODE_TYPE_INS_TYPE, mInfo.type);

		if (isAdd)
			btnHeaderTitle.setText("입회보고서 등록");
		else
			btnHeaderTitle.setText("입회보고서 수정/삭제");

		btnHeaderLeft.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});

		btnHeaderRight.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				AppUtil.startActivity(InputJoinReportAddActivity.this, new Intent(InputJoinReportAddActivity.this, MainActivity.class));
			}
		});


	}

	//날짜 대화상자 리스너 부분
	DatePickerDialog.OnDateSetListener mDateSetListener =
			new DatePickerDialog.OnDateSetListener() {
				@Override
				public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
					//사용자가 입력한 값을 가져온뒤
					mYear = year;
					mMonth = monthOfYear;
					mDay = dayOfMonth;
					//텍스트뷰의 값을 업데이트함
					updateDate(mYear, mMonth, mDay);
				}
			};

//시간 대화상자 리스너 부분
/*	TimePickerDialog.OnTimeSetListener mTimeSetListener =
			new TimePickerDialog.OnTimeSetListener() {
				@Override
				public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
					//사용자가 입력한 값을 가져온뒤
					mHour = hourOfDay;
					mMinute = minute;
					//텍스트뷰의 값을 업데이트함
					UpdateNow();
				}
			};

 */
	void showTime() {
		TimePickerDialog timePickerDialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
			@Override
			public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
				String strHour = "";
				String strMinute = "";
				if(hourOfDay<10) {
					strHour += "0" + hourOfDay;
				} else strHour = String.valueOf(hourOfDay);
				if(minute<10) {
					strMinute += "0" + minute;
				} else strMinute = String.valueOf(minute);
				mHour = hourOfDay;
				mMinute = minute;
				UpdateNow(strHour, strMinute);
			}
		}, mHour, mMinute, true);
		timePickerDialog.show();
	}


	public void updateDate(int year, int month, int day) {
//		btnDate.setText(text)
		mYear = year;
		mMonth = month;
		int newMonth = month + 1;
		mDay = day;

		if (year == 0) {
			if(dateGubun == 1) btnJoinDate.setText("전체");
			else if(dateGubun == 2) btnRequestDate.setText("전체");
			else if(dateGubun == 3) btnJoinSDate.setText("전체");
			else btnJoinEDate.setText("전체");
		} else {
			String strDate = year + ".";

			if (newMonth < 10)
				strDate += "0" + newMonth;
			else
				strDate += "" + newMonth;

			if (day < 10)
				strDate += ".0" + mDay;
			else
				strDate += "." + mDay;

			//strDate += "." + "" + mDay;
			if(dateGubun == 1) btnJoinDate.setText(strDate);
			else if(dateGubun == 2) btnRequestDate.setText(strDate);
			else if(dateGubun == 3) btnJoinSDate.setText(strDate);
			else btnJoinEDate.setText(strDate);
		}

		fired = false;
		dateGubun = 0;
	}
	void UpdateNow(String hour, String minute){
		if(timeGubun == 1) btnJoinSTime.setText(hour+":"+minute);
		else btnJoinETime.setText(hour+":"+minute);
		timeGubun = 0;
		//mTxtDate.setText(String.format("%d/%d/%d", mYear,mMonth + 1, mDay));
		//mTxtTime.setText(String.format("%d:%d", mHour, mMinute));

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
				InputJoinReportDao inputJoinReportDao = InputJoinReportDao.getInstance(this);
				inputJoinReportDao.Delete(String.valueOf(selectJoinReportInfo.idx));
				//InspectResultMasterDao insRetDao = InspectResultMasterDao.getInstance(this);
				//insRetDao.updateComplete(mInfo.master_idx, "N", ConstVALUE.CODE_NO_INSPECT_BT);
				setResult();
				finish();
				break;
			case R.id.btnWorkerCnt:
				showWorkerCntDialog();
				break;
			case R.id.btnJoinDate:
				dateGubun = 1;
				new DatePickerDialog(InputJoinReportAddActivity.this, mDateSetListener, mYear,mMonth, mDay).show();
				break;
			case R.id.btnRequestDate:
				dateGubun = 2;
				new DatePickerDialog(InputJoinReportAddActivity.this, mDateSetListener, mYear,mMonth, mDay).show();
				break;
			case R.id.btnJoinSDate:
				dateGubun = 3;
				new DatePickerDialog(InputJoinReportAddActivity.this, mDateSetListener, mYear,mMonth, mDay).show();
				break;
			case R.id.btnJoinEDate:
				dateGubun = 4;
				new DatePickerDialog(InputJoinReportAddActivity.this, mDateSetListener, mYear,mMonth, mDay).show();
				break;
			case R.id.btnJoinSTime:
				timeGubun = 1;
				//new TimePickerDialog(InputJoinReportAddActivity.this, mTimeSetListener, mHour,mMinute, false).show();
				showTime();
				break;
			case R.id.btnJoinETime:
				timeGubun = 2;
				//new TimePickerDialog(InputJoinReportAddActivity.this, mTimeSetListener, mHour,mMinute, false).show();
				showTime();
				break;
			default:
				break;
		}
	}

	public void showWorkerCntDialog() {
		AlertDialog.Builder dialog = new AlertDialog.Builder(this, AlertDialog.THEME_DEVICE_DEFAULT_LIGHT);
		dialog.setTitle(getString(R.string.input_bt_worker_cnt));
		dialog.setSingleChoiceItems(strWorkerCntItems, selectedWorkerCntNo, new android.content.DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				selectedWorkerCntNo = which;
				btnWorkerCnt.setText(strWorkerCntItems[which]);
				dialog.dismiss();
			}
		}).show();
	}

	public void setInputType() {
		Logg.d("!!! strIdx : "+strIdx);
		if(strIdx == "") {
			isAdd = true;
		} else {
			InputJoinReportDao inputJoinReportDao = InputJoinReportDao.getInstance(this);
			if (inputJoinReportDao.existJoinReport(String.valueOf(strIdx)))
				isAdd = false;
			else
				isAdd = true;
		}


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
		Log.i("MyTag","############### test2 update");
		String strWorkerCnt = btnWorkerCnt.getText().toString();
		String strName = editName.getText().toString();
		String strLength = editLength.getText().toString();
		String strLocation = editLocation.getText().toString();
		String strWorkNm = editWorkNm.getText().toString();
		String strJoinDate = StringUtil.removeDot(btnJoinDate.getText().toString());
		String strRequestDate = StringUtil.removeDot(btnRequestDate.getText().toString());
		String strJoinSDate = StringUtil.removeDot(btnJoinSDate.getText().toString());
		String strJoinSTime = btnJoinSTime.getText().toString().replace(":", "");
		String strJoinEDate = StringUtil.removeDot(btnJoinEDate.getText().toString());
		String strJoinETime = btnJoinETime.getText().toString().replace(":", "");
		String strRequestJoinerCo = editRequestJoinerCo.getText().toString();
		String strRequestJoiner = editRequestJoiner.getText().toString();
		String strJoinerDept = editJoinerDept.getText().toString();
		String strJoiner = editJoiner.getText().toString();
		String strJoinReason = editJoinReason.getText().toString();
		String strEtc = editEtc.getText().toString();


		JoinReportInfo log = new JoinReportInfo();
		log.weather = CodeInfo.getInstance(this).getKey(ConstVALUE.CODE_TYPE_WEATHER, strWeather);
		log.name = strName;
		log.length = strLength;
		log.location = strLocation;
		log.work_nm = strWorkNm;
		log.join_date = strJoinDate;
		log.request_date = strRequestDate;
		log.join_s_date = strJoinSDate;
		log.join_s_time = strJoinSTime;
		log.join_e_date = strJoinEDate;
		log.join_e_time = strJoinETime;
		log.request_joiner_co = strRequestJoinerCo;
		log.request_joiner = strRequestJoiner;
		log.joiner_dept = strJoinerDept;
		log.joiner = strJoiner;
		log.join_reason = strJoinReason;
		log.etc = strEtc;
		log.etc = log.etc.replace("=", "");

		InputJoinReportDao inputJoinReportDao = InputJoinReportDao.getInstance(this);

		if (isEdit)
			inputJoinReportDao.Append(log, selectJoinReportInfo.idx);
		else {
			LocManager.getInstance(this).startGetLocation(listener);
			inputJoinReportDao.Append(log);
		}

		setResult(RESULT_OK);

		//InspectResultMasterDao insRetDao = InspectResultMasterDao.getInstance(this);
		//insRetDao.updateComplete(mInfo.master_idx, "Y", ConstVALUE.CODE_NO_INSPECT_BT);

		finish();
	}

	public void setResult() {
		Intent intent = new Intent();
		intent.putExtra("joinDate", strJoinDate);
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

			LocManager.getInstance(InputJoinReportAddActivity.this).setGetGps(true);

			String currentLocation = String.format("위도:%f  경도:%f  고도:%f", location.getLatitude(), location.getLongitude(), location.getAltitude());
			Logg.d(InputJoinReportAddActivity.this, "currentLocation : " + currentLocation);
			Calendar cal = new GregorianCalendar();
			String now = String.format("%d년 %d월 %d일 %d시 %d분 %d초", cal.get(Calendar.YEAR), cal.get(Calendar.MONTH)+1,
					cal.get(Calendar.DAY_OF_MONTH), cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE),
					cal.get(Calendar.SECOND));
			Logg.d(InputJoinReportAddActivity.this, "갱신 시간 : " + now);

			InspectResultMasterDao insRetDao = InspectResultMasterDao.getInstance(InputJoinReportAddActivity.this);
			insRetDao.updateNfcTagLocation(mInfo.master_idx, "", location.getLatitude()+"", location.getLongitude()+"", false);

			if (locationManager != null) {
				locationManager.removeUpdates(listener);
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

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);

		if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT)
		{
			Logg.d("ORIENTATION_PORTRAIT");
//        	editCheckResult.setImeOptions(EditorInfo.IME_ACTION_NONE);
		}
		else if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE)
		{
			Logg.d("ORIENTATION_LANDSCAPE");
		}
	}

}
