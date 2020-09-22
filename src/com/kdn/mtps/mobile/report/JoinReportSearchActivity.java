package com.kdn.mtps.mobile.report;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.kdn.mtps.mobile.BaseActivity;
import com.kdn.mtps.mobile.MainActivity;
import com.kdn.mtps.mobile.R;
import com.kdn.mtps.mobile.TitleManager;
import com.kdn.mtps.mobile.constant.ConstSP;
import com.kdn.mtps.mobile.constant.ConstVALUE;
import com.kdn.mtps.mobile.custom.CustomDatePickerDialog;
import com.kdn.mtps.mobile.db.InputJoinReportDao;
import com.kdn.mtps.mobile.db.InspectResultMasterDao;
import com.kdn.mtps.mobile.info.CodeInfo;
import com.kdn.mtps.mobile.input.InputGHActivity;
import com.kdn.mtps.mobile.input.InputJoinReportAddActivity;
import com.kdn.mtps.mobile.input.JoinReportInfo;
import com.kdn.mtps.mobile.inspect.InspectInfo;
import com.kdn.mtps.mobile.inspect.InspectMapListActivity;
import com.kdn.mtps.mobile.inspect.InspectResultDetailActivity;
import com.kdn.mtps.mobile.inspect.InspectResultSearchActivity;
import com.kdn.mtps.mobile.report.JoinReportSearchBaseAdapter;
import com.kdn.mtps.mobile.net.api.bean.ScheduleList;
import com.kdn.mtps.mobile.setting.SettingActivity;
import com.kdn.mtps.mobile.util.AppUtil;
import com.kdn.mtps.mobile.util.Logg;
import com.kdn.mtps.mobile.util.Shared;
import com.kdn.mtps.mobile.util.StringUtil;
import com.kdn.mtps.mobile.util.ToastUtil;
import com.kdn.mtps.mobile.util.UIUtil;
import com.kdn.mtps.mobile.util.thread.ATask;

import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class JoinReportSearchActivity extends BaseActivity implements TitleManager, OnClickListener, OnScrollListener{

	public final int REQ_INPUT = 100;

	Button btnName;
	EditText editWorkNm;
	Button btnDate;
	Button btnSearch;
	Button btnAdd;
	ListView listJoinReport;
	TextView tvNoData;
	ArrayList<InspectInfo> inspectList;
	ArrayList<JoinReportInfo> joinReportList;
	JoinReportSearchBaseAdapter adapter;
	int selectedNameNo = -1;
	int selectedTypeNo = -1;

	boolean fired = false;
	int mYear;
	int mMonth;

	ScheduleList scheduleList;
	boolean isLock;
	boolean isEnd;
	ListAdapter myListAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_join_report_search);

		setTitle();
		setInit();

		setData();

		listJoinReport.setAdapter(adapter);
	}

	@Override
	public void setTitle() {
		Button btnHeaderTitle= (Button)findViewById(R.id.btnHeaderTitle);
		Button btnHeaderLeft = (Button)findViewById(R.id.btnHeaderLeft);
		Button btnHeaderRight = (Button)findViewById(R.id.btnHeaderRight);

		btnHeaderTitle.setText("입회보고서");

		btnHeaderLeft.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});

		btnHeaderRight.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				AppUtil.startActivity(JoinReportSearchActivity.this, new Intent(JoinReportSearchActivity.this, MainActivity.class));
			}
		});
	}

	public void setInit() {
		btnName = (Button)findViewById(R.id.btnName);
		btnDate = (Button)findViewById(R.id.btnDate);
		editWorkNm = (EditText)findViewById(R.id.editWorkNm);
		btnAdd = (Button)findViewById(R.id.btnAdd);
		btnSearch = (Button)findViewById(R.id.btnSearch);
		//editTowerNo = (EditText)findViewById(R.id.editTowerNo);

		btnName.setOnClickListener(this);
		btnDate.setOnClickListener(this);
		btnAdd.setOnClickListener(this);
		btnSearch.setOnClickListener(this);

		listJoinReport = (ListView)findViewById(R.id.listJoinReport);
		listJoinReport.setOnScrollListener(this);

		listJoinReport.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View view, int arg2,
					long arg3) {
				adapter.click(view);
			}
		});

		tvNoData = (TextView)findViewById(R.id.tvNoData);
		adapter = new JoinReportSearchBaseAdapter(this);
	}

	public void showNameDialog() {


		final String strItems[] = CodeInfo.getInstance(this).getTracksNames();

		AlertDialog.Builder dialog = new AlertDialog.Builder(this, AlertDialog.THEME_DEVICE_DEFAULT_LIGHT);
//
//		AlertDialog dialog2 = dialog.create();
//
//		myListAdapter = new MyListAdapter(this, dialog2);
//		ArrayList tracksList = CodeInfo.getInstance(this).getTracksList();
//		((MyListAdapter) myListAdapter).setList(selectedNameNo, tracksList);
//
//		dialog.setTitle(getString(R.string.facility_name));
//		dialog.setSingleChoiceItems(myListAdapter, selectedNameNo,  new android.content.DialogInterface.OnClickListener() {
//			@Override
//			public void onClick(DialogInterface dialog, int which) {
//				selectedNameNo = which;
//                btnName.setText(strItems[which]);
//                dialog.dismiss();
//			}
//		}).show();

		dialog.setTitle(getString(R.string.facility_name));
		dialog.setSingleChoiceItems(strItems, selectedNameNo, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				selectedNameNo = which;
                btnName.setText(strItems[which]);
                dialog.dismiss();
			}
		}).show();
	}


	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btnName:
			showNameDialog();
			break;
		case R.id.btnDate:
//			showDatePicker();
			showDateDialog();
			break;
		case R.id.btnSearch:

			startSearch();

			break;
		case R.id.btnAdd:
			goInputActivity(v, InputJoinReportAddActivity.class);
			break;
		}
	}

	public void goInputActivity(View view,Class<?> cls) {
		String inspecter_1 = Shared.getString(JoinReportSearchActivity.this, ConstSP.SETTING_INSPECTER_ID_1);
		String inspecter_2 = Shared.getString(JoinReportSearchActivity.this, ConstSP.SETTING_INSPECTER_ID_2);

		if (inspecter_1 == null || "".equals(inspecter_1) || inspecter_2 == null || "".equals(inspecter_2))  {
			Intent intent = new Intent(JoinReportSearchActivity.this, SettingActivity.class);
			intent.putExtra("setInspecter", true);
			startActivity(intent);
			return;
		}

		InspectInfo info = (InspectInfo) view.getTag();
		Intent intent = new Intent(JoinReportSearchActivity.this, cls);
		intent.putExtra("inspect", info);
		//intent.putExtra("currentDate", "");
		startActivityForResult(intent, REQ_INPUT);
	}

	public void startSearch() {
/*
		if (selectedNameNo == -1) {
			ToastUtil.show(JoinReportSearchActivity.this, "선로명을 선택해 주세요.");
			return;
		}

 */
		
		UIUtil.hideKeyboard(btnSearch);
		

		isEnd = false;
		adapter.setListClear();

		final String strTracksName = btnName.getText().toString();
		final String strWorkNm = editWorkNm.getText().toString();
		//final String strTowerNo = editTowerNo.getText().toString();
		final String strDate = StringUtil.removeDot(btnDate.getText().toString());
		final String strSDate = strDate+"01";
		final String strEDate = strDate+"31";
		ATask.executeVoidProgress(JoinReportSearchActivity.this, R.string.inspect_result_searching, false, new ATask.OnTaskProgress() {
			public void onPre() {
			}

			public void onBG() {
				InputJoinReportDao insRetDao = InputJoinReportDao.getInstance(JoinReportSearchActivity.this);
				joinReportList = insRetDao.selectJoinReport(strTracksName, strWorkNm, strSDate, strEDate);
			}

			@Override
			public void onPost() {
				adapter.setList(joinReportList);
				if (adapter.getCount() <= 0 )
					tvNoData.setVisibility(View.VISIBLE);
				else
					tvNoData.setVisibility(View.GONE);
				}

			@Override
			public void onCancel() {
				// TODO Auto-generated method stub
				
			}
		});
	}
	
	private DatePickerDialog.OnDateSetListener mDateListener = new DatePickerDialog.OnDateSetListener() {

		@Override
		public void onDateSet(DatePicker view, int year, int month, int day) {
			if (day != 99)
				return;
			
			if (fired == true) {
	            return;
	        } else {
	        	fired = true;
	        }
			updateDate(year, month, day);
		}
		
	};
	
	public void updateDate(int year, int month, int day) {
//		btnDate.setText(text)
		mYear = year;
		mMonth = month;
		int newMonth = month + 1;

		if (year == 0) {
			btnDate.setText("전체");
		} else {
			String strDate = year + ".";
			
			if (newMonth < 10)
				strDate += "0" + newMonth;
			else
				strDate += "" + newMonth;
			btnDate.setText(strDate);
		}
		
		fired = false;
	}
	
	public void showDatePicker() {

		try {

			Calendar c = Calendar.getInstance();
			int year = c.get(Calendar.YEAR);
			int cmonth = c.get(Calendar.MONTH);
			int cday = c.get(Calendar.DAY_OF_MONTH);
			
			DatePickerDialog datePickerDialog = new DatePickerDialog(this, mDateListener, year, cmonth, cday);

			Field[] f = datePickerDialog.getClass().getDeclaredFields();
			for (Field dateField : f) {
				if (dateField.getName().equals("mDatePicker")) {
					dateField.setAccessible(true);

					DatePicker datePicker = (DatePicker) dateField
							.get(datePickerDialog);

					Field datePickerFields[] = dateField.getType()
							.getDeclaredFields();

					for (Field datePickerField : datePickerFields) {
						if ("mDayPicker".equals(datePickerField.getName()) || 
								"mDayDecrementButton".equals(datePickerField.getName()) || 
								"mDayIncrementButton".equals(datePickerField.getName()) || 
								"mDaySpinner".equals(datePickerField.getName()) || 
								"mDaySpinnerInput".equals(datePickerField.getName()) || 
								"PICKER_DAY".equals(datePickerField.getName()) ) {
							
							datePickerField.setAccessible(true);
							Object dayPicker = new Object();
							dayPicker = datePickerField.get(datePicker);
							
							if (dayPicker instanceof View)
								((View) dayPicker).setVisibility(View.GONE);
						}
					}
				}
			}

			datePickerDialog.show();

		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}

	}
	
	public void setData() {
		String strDate = new SimpleDateFormat("yyyy.MM").format(System.currentTimeMillis());
		btnDate.setText(strDate);
		
	}
	
	public void showDateDialog() {
		Calendar c = Calendar.getInstance();
		
		if (mYear == 0)
			mYear = c.get(Calendar.YEAR);
		if  (mMonth == 0)
			mMonth = c.get(Calendar.MONTH);
		
		CustomDatePickerDialog dp = new CustomDatePickerDialog(this, android.R.style.Theme_Holo_Light_Dialog_NoActionBar,  mDateListener, mYear, mMonth, 1);

        DatePickerDialog obj = dp.getPicker();
        
       try{
                  Field[] datePickerDialogFields = obj.getClass().getDeclaredFields();
                  for (Field datePickerDialogField : datePickerDialogFields) { 
                      if (datePickerDialogField.getName().equals("mDatePicker")) {
                          datePickerDialogField.setAccessible(true);
                          DatePicker datePicker = (DatePicker) datePickerDialogField.get(obj);
                          Field datePickerFields[] = datePickerDialogField.getType().getDeclaredFields();
                          for (Field datePickerField : datePickerFields) {
                             if ("mDayPicker".equals(datePickerField.getName()) || "mDaySpinner".equals(datePickerField
                               .getName())) {
                                datePickerField.setAccessible(true);
                                Object dayPicker = new Object();
                                dayPicker = datePickerField.get(datePicker);
                                ((View) dayPicker).setVisibility(View.GONE);
                             }
                          }
                       }

                    }
                  }catch(Exception ex){
                  }
       obj.show();
	}
	
//	public void getScheduleList(final int offset, final int limit) {
//		isLock = true;
//		String strTracksName = btnName.getText().toString();
//		String strInsType = btnType.getText().toString();
//		String strDate = btnDate.getText().toString();
//		final String paramDate;
//		
//		if (!strDate.contains("."))
//			paramDate = "";
//		else
//			paramDate = strDate.replace(".", "");
//		
//		final String eqpName = editTowerNo.getText().toString();
//		
//		final String tracksCode = CodeInfo.getInstance(this).getTracksCode(strTracksName);
//		final String insType = CodeInfo.getInstance(this).getKey(ConstVALUE.CODE_TYPE_INS_TYPE, strInsType);
//		
//		ATask.executeVoidProgress(InspectResultSearchActivity.this, R.string.inspect_result_searching, false, new ATask.OnTaskProgress() {
//			public void onPre() {
//			}
//
//			public void onBG() {
//				scheduleList = ApiManager.scheduleList(tracksCode, insType, eqpName, paramDate);
//			}
//
//			@Override
//			public void onPost() {
//				if (scheduleList != null && scheduleList.schedule != null) {
//					
//					List<ScheduleInfo> schList = scheduleList.schedule;
//					
//					if (schList.size() < 10)
//						isEnd = true;
//					
//					inspectList = new ArrayList<InspectInfo>(); 
//					
//					for (int i=0; i<schList.size(); i++) {
//						ScheduleInfo schInfo = schList.get(i);
//						
//						InspectInfo insInfo = new InspectInfo();
//						insInfo.tracksName = schInfo.FNCT_LC_DTLS;
//						insInfo.towerNo = schInfo.TOWER_IDX;
//						insInfo.type = schInfo.INS_TY_CD;
//						insInfo.typeName = schInfo.NM;
//						insInfo.date = schInfo.CYCLE_YM;
//						insInfo.latitude = schInfo.LATITUDE;
//						insInfo.longitude = schInfo.LONGITUDE;
//						insInfo.eqpNm = schInfo.EQP_NM;
//						insInfo.eqpNo = schInfo.EQP_NO;
//						
//						InspectResultMasterDao inspectResultMasterDao = InspectResultMasterDao.getInstance(InspectResultSearchActivity.this);
////						boolean exist = inspectResultMasterDao.existIns(insInfo.tracksName, insInfo.towerNo, insInfo.type, insInfo.date);
//						InspectInfo info = inspectResultMasterDao.selectIns(insInfo.tracksName, insInfo.towerNo, insInfo.type, insInfo.date);
//						
//						if (info == null) {
//							inspectResultMasterDao.Append(insInfo);
//							info = inspectResultMasterDao.selectIns(insInfo.tracksName, insInfo.towerNo, insInfo.type, insInfo.date);
//						}
//						
//						inspectList.add(info);
//					}
//					
////					String strTracksName = btnName.getText().toString();
////					String strInsType = btnType.getText().toString();
////					String strTowerNo = editTowerNo.getText().toString();
////					String strDate = btnDate.getText().toString();
//					
////					InspectResultMasterDao insRetDao = InspectResultMasterDao.getInstance(InspectResultSearchActivity.this);
////					inspectList = insRetDao.selecInsList(strTracksName, strInsType, strTowerNo, strDate);
//					adapter.setList(inspectList);
//					isLock = false;
//					
//					if (adapter.getCount() <= 0 )
//						tvNoData.setVisibility(View.VISIBLE);
//					else
//						tvNoData.setVisibility(View.GONE);
//				}
//			}
//
//			@Override
//			public void onCancel() {
//				// TODO Auto-generated method stub
//				
//			}
//			
//		});
//	}
	
	@Override
	public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
		
		int count = totalItemCount - visibleItemCount;
		
		if (firstVisibleItem >= count && totalItemCount != 0 && !isLock && !isEnd) {
//			Logg.d("onScroll : " + firstVisibleItem + " / " + count + " / " + totalItemCount);
//			getScheduleList(totalItemCount+1, totalItemCount + ConstVALUE.LISTVIEW_LIMIT);
		}
		
	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		
		if (requestCode == ConstVALUE.REQUEST_CODE_INSPECT_SEARCH) {
			startSearch();
		}
	}
	
	public void setNameNo(int position) {
		selectedNameNo = position;
	}
}
