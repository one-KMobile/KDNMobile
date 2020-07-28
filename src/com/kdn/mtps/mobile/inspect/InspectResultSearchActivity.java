package com.kdn.mtps.mobile.inspect;

import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
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
import com.kdn.mtps.mobile.R;
import com.kdn.mtps.mobile.TitleManager;
import com.kdn.mtps.mobile.constant.ConstVALUE;
import com.kdn.mtps.mobile.custom.CustomDatePickerDialog;
import com.kdn.mtps.mobile.db.InspectResultMasterDao;
import com.kdn.mtps.mobile.info.CodeInfo;
import com.kdn.mtps.mobile.input.MyListAdapter;
import com.kdn.mtps.mobile.net.api.bean.ScheduleList;
import com.kdn.mtps.mobile.util.AppUtil;
import com.kdn.mtps.mobile.util.StringUtil;
import com.kdn.mtps.mobile.util.ToastUtil;
import com.kdn.mtps.mobile.util.UIUtil;
import com.kdn.mtps.mobile.util.thread.ATask;

public class InspectResultSearchActivity extends BaseActivity implements TitleManager, OnClickListener, OnScrollListener{

	Button btnName;
	Button btnType;
	Button btnDate;
	Button btnSearch;
	EditText editTowerNo;
	ListView listInspect;
	TextView tvNoData;
	ArrayList<InspectInfo> inspectList;
	InspectResultSearchBaseAdapter adapter;
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
		setContentView(R.layout.activity_inspect_result_search);

		setTitle();
		setInit();
		
		setData();
		
		listInspect.setAdapter(adapter);
	}

	@Override
	public void setTitle() {
		Button btnHeaderTitle= (Button)findViewById(R.id.btnHeaderTitle);
		Button btnHeaderLeft = (Button)findViewById(R.id.btnHeaderLeft);
		Button btnHeaderRight = (Button)findViewById(R.id.btnHeaderRight);
		
		btnHeaderTitle.setText("순시결과조회");
		
		btnHeaderRight.setBackgroundResource(R.drawable.selector_btn_map);
		
		btnHeaderLeft.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		
		btnHeaderRight.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				ArrayList<InspectInfo>list = (ArrayList<InspectInfo>) adapter.getList();
				
				if (list.isEmpty()) {
					ToastUtil.show(InspectResultSearchActivity.this, "검색된 데이터가 없습니다.");
					return;
				}
				
				Intent intent = new Intent(InspectResultSearchActivity.this, InspectMapListActivity.class);
				intent.putExtra("locations", list);
//				intent.putParcelableArrayListExtra("locations", infoList);
				AppUtil.startActivity(InspectResultSearchActivity.this, intent);
				
			}
		});
	}

	public void setInit() {
		btnName = (Button)findViewById(R.id.btnName);
		btnDate = (Button)findViewById(R.id.btnDate);
		btnType = (Button)findViewById(R.id.btnType);
		btnSearch = (Button)findViewById(R.id.btnSearch);
		editTowerNo = (EditText)findViewById(R.id.editTowerNo);
		
		btnName.setOnClickListener(this);
		btnDate.setOnClickListener(this);
		btnType.setOnClickListener(this);
		btnSearch.setOnClickListener(this);
		
		listInspect = (ListView)findViewById(R.id.listInspect);
		listInspect.setOnScrollListener(this);
		
		listInspect.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View view, int arg2,
					long arg3) {
				adapter.click(view);
			}
		});
		
		tvNoData = (TextView)findViewById(R.id.tvNoData);
		adapter = new InspectResultSearchBaseAdapter(this);
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
		dialog.setSingleChoiceItems(strItems, selectedNameNo, new android.content.DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				selectedNameNo = which;
                btnName.setText(strItems[which]);
                dialog.dismiss();
			}
		}).show();
	}
	
	public void showTypeDialog() {
//		final String strItems[] = {"순시 전체","보통순시","항공등 점등점검"};
		final String strItems[] = CodeInfo.getInstance(this).getNames(ConstVALUE.CODE_TYPE_INS_TYPE);
		final String newItems[] = new String[strItems.length + 1];
		
		newItems[0] = "순시 전체";
		for (int i=0; i<strItems.length; i++) {
			newItems[i+1] = strItems[i];
		}
		
		AlertDialog.Builder dialog = new AlertDialog.Builder(this, AlertDialog.THEME_DEVICE_DEFAULT_LIGHT);
		dialog.setTitle(getString(R.string.inspect_type));
		dialog.setSingleChoiceItems(newItems, selectedTypeNo, new android.content.DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				selectedTypeNo = which;
                btnType.setText(newItems[which]);
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
		case R.id.btnType:
			showTypeDialog();
			break;
		case R.id.btnSearch:
			
			startSearch();
			
			break;
		}
	}
	
	public void startSearch() {
		if (selectedNameNo == -1) {
			ToastUtil.show(InspectResultSearchActivity.this, "선로명을 선택해 주세요.");
			return;
		}
		
		UIUtil.hideKeyboard(btnSearch);
		

		isEnd = false;
		adapter.setListClear();
		
		final String strTracksName = btnName.getText().toString();
		String strInsType = btnType.getText().toString();
		final String strTowerNo = editTowerNo.getText().toString();
		final String strDate = StringUtil.removeDot(btnDate.getText().toString());
		final String insType = CodeInfo.getInstance(this).getKey(ConstVALUE.CODE_TYPE_INS_TYPE, strInsType);
		
		ATask.executeVoidProgress(InspectResultSearchActivity.this, R.string.inspect_result_searching, false, new ATask.OnTaskProgress() {
			public void onPre() {
			}

			public void onBG() {
				InspectResultMasterDao insRetDao = InspectResultMasterDao.getInstance(InspectResultSearchActivity.this);
				inspectList = insRetDao.selecInsList(strTracksName, insType, strTowerNo, strDate);
			}

			@Override
			public void onPost() {
				adapter.setList(inspectList);
					
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
