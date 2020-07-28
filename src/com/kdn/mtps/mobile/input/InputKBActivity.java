package com.kdn.mtps.mobile.input;

import java.util.Calendar;
import java.util.GregorianCalendar;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.Button;
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
import com.kdn.mtps.mobile.db.InputKBDao;
import com.kdn.mtps.mobile.db.InspectResultMasterDao;
import com.kdn.mtps.mobile.info.CodeInfo;
import com.kdn.mtps.mobile.inspect.InspectInfo;
import com.kdn.mtps.mobile.util.AppUtil;
import com.kdn.mtps.mobile.util.LocManager;
import com.kdn.mtps.mobile.util.Logg;
import com.kdn.mtps.mobile.util.Shared;
import com.kdn.mtps.mobile.util.StringUtil;
import com.kdn.mtps.mobile.util.UIUtil;

public class InputKBActivity extends BaseActivity implements TitleManager, OnClickListener {
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
	
	Button 	btnItem_fac_1;
	Button btnItem_fac_2;
	Button btnItem_fac_3;
	Button btnItem_fac_4;
	Button btnItem_fac_5;
	Button btnItem_fac_6;
	Button btnItem_fac_7;
	Button btnItem_fac_8;
	Button btnItem_fac_9;
	Button btnItem_fac_10;
	Button btnItem_fac_11;
	Button btnItem_fac_12;
	Button btnItem_sett_1;
	Button btnItem_sett_2;
	Button btnItem_sett_3;
	Button btnItem_sett_4;
	Button btnItem_etc_1;
	Button btnItem_etc_2;
	
	KBInfo selectKBInfo;
	
	InspectInfo mInfo;
	
	int selectedWeatherNo = 0;
	int selectedItem_Fac_1 = 0;
	int selectedItem_Fac_2 = 0;
	int selectedItem_Fac_3 = 0;
	int selectedItem_Fac_4 = 0;
	int selectedItem_Fac_5 = 0;
	int selectedItem_Fac_6 = 0;
	int selectedItem_Fac_7 = 0;
	int selectedItem_Fac_8 = 0;
	int selectedItem_Fac_9 = 0;
	int selectedItem_Fac_10 = 0;
	int selectedItem_Fac_11 = 0;
	int selectedItem_Fac_12 = 0;
	int selectedItem_Sett_1 = 0;
	int selectedItem_Sett_2= 0;
	int selectedItem_Sett_3 = 0;
	int selectedItem_Sett_4 = 0;
	int selectedItem_Etc_1 = 0;
	int selectedItem_Etc_2 = 0;
	
	final String strWeatherItems[] = CodeInfo.getInstance(this).getNames(ConstVALUE.CODE_TYPE_WEATHER);
	final String strResultItems[] = CodeInfo.getInstance(this).getNames(ConstVALUE.CODE_TYPE_GOOD_SECD);
	
	boolean isAdd;
	String strCurrentDate;
	LocationManager locationManager;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_input_kb);
		
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
		
		btnItem_fac_1 = (Button)findViewById(R.id.btnItem_fac_1);
		btnItem_fac_2 = (Button)findViewById(R.id.btnItem_fac_2);
		btnItem_fac_3 = (Button)findViewById(R.id.btnItem_fac_3);
		btnItem_fac_4 = (Button)findViewById(R.id.btnItem_fac_4);
		btnItem_fac_5 = (Button)findViewById(R.id.btnItem_fac_5);
		btnItem_fac_6 = (Button)findViewById(R.id.btnItem_fac_6);
		btnItem_fac_7 = (Button)findViewById(R.id.btnItem_fac_7);
		btnItem_fac_8 = (Button)findViewById(R.id.btnItem_fac_8);
		btnItem_fac_9 = (Button)findViewById(R.id.btnItem_fac_9);
		btnItem_fac_10 = (Button)findViewById(R.id.btnItem_fac_10);
		btnItem_fac_11 = (Button)findViewById(R.id.btnItem_fac_11);
		btnItem_fac_12 = (Button)findViewById(R.id.btnItem_fac_12);
		btnItem_sett_1 = (Button)findViewById(R.id.btnItem_sett_1);
		btnItem_sett_2 = (Button)findViewById(R.id.btnItem_sett_2);
		btnItem_sett_3 = (Button)findViewById(R.id.btnItem_sett_3);
		btnItem_sett_4 = (Button)findViewById(R.id.btnItem_sett_4);
		btnItem_etc_1 = (Button)findViewById(R.id.btnItem_etc_1);
		btnItem_etc_2 = (Button)findViewById(R.id.btnItem_etc_2);
		btnItem_fac_1.setOnClickListener(this);
		btnItem_fac_2.setOnClickListener(this);
		btnItem_fac_3.setOnClickListener(this);
		btnItem_fac_4.setOnClickListener(this);
		btnItem_fac_5.setOnClickListener(this);
		btnItem_fac_6.setOnClickListener(this);
		btnItem_fac_7.setOnClickListener(this);
		btnItem_fac_8.setOnClickListener(this);
		btnItem_fac_9.setOnClickListener(this);
		btnItem_fac_10.setOnClickListener(this);
		btnItem_fac_11.setOnClickListener(this);
		btnItem_fac_12.setOnClickListener(this);
		btnItem_sett_1.setOnClickListener(this);
		btnItem_sett_2.setOnClickListener(this);
		btnItem_sett_3.setOnClickListener(this);
		btnItem_sett_4.setOnClickListener(this);
		btnItem_etc_1.setOnClickListener(this);
		btnItem_etc_2.setOnClickListener(this);
		
		btnAdd = (Button)findViewById(R.id.btnAdd);
		btnAdd.setOnClickListener(this);
		btnEdit = (Button)findViewById(R.id.btnEdit);
		btnDelete = (Button)findViewById(R.id.btnDelete);
		btnEdit.setOnClickListener(this);
		btnDelete.setOnClickListener(this);
		btnCamera = (Button)findViewById(R.id.btnCamera);
		btnCamera.setOnClickListener(this);
		
		ivComplte = (ImageView)findViewById(R.id.ivComplte);
		
	}
	
	public void setData() {
		
		tvName.setText(mInfo.tracksName);
		tvDate.setText(StringUtil.printDate(mInfo.date));
		tvEqpNm.setText(mInfo.eqpNm);
		
		String insType = CodeInfo.getInstance(this).getValue(ConstVALUE.CODE_TYPE_INS_TYPE, mInfo.type);
		tvType.setText(insType);
		
		if (isAdd) {
			ivComplte.setBackgroundResource(R.drawable.input_kb_add);
			
			strWeather = Shared.getString(this, ConstSP.SETTING_WEATHER);
			
			btnItem_fac_1.setText(strResultItems[0]);
			btnItem_fac_2.setText(strResultItems[0]);
			btnItem_fac_3.setText(strResultItems[0]);
			btnItem_fac_4.setText(strResultItems[0]);
			btnItem_fac_5.setText(strResultItems[0]);
			btnItem_fac_6.setText(strResultItems[0]);
			btnItem_fac_7.setText(strResultItems[0]);
			btnItem_fac_8.setText(strResultItems[0]);
			btnItem_fac_9.setText(strResultItems[0]);
			btnItem_fac_10.setText(strResultItems[0]);
			btnItem_fac_11.setText(strResultItems[0]);
			btnItem_fac_12.setText(strResultItems[0]);
			btnItem_sett_1.setText(strResultItems[0]);
			btnItem_sett_2.setText(strResultItems[0]);
			btnItem_sett_3.setText(strResultItems[0]);
			btnItem_sett_4.setText(strResultItems[0]);
			btnItem_etc_1.setText(strResultItems[0]);
			btnItem_etc_2.setText(strResultItems[0]);
		} else {
			ivComplte.setBackgroundResource(R.drawable.input_kb_edit);
			
			InputKBDao inputKBDao = InputKBDao.getInstance(this);
			selectKBInfo = inputKBDao.selectKB(mInfo.master_idx);
			
			strWeather = CodeInfo.getInstance(this).getValue(ConstVALUE.CODE_TYPE_WEATHER, selectKBInfo.weather);
			selectedWeatherNo = StringUtil.getIndex(strWeatherItems, strWeather);
			
			String str_fac_1 = CodeInfo.getInstance(this).getValue(ConstVALUE.CODE_TYPE_GOOD_SECD, selectKBInfo.item_fac_1);
			btnItem_fac_1.setText(str_fac_1);
			selectedItem_Fac_1 = StringUtil.getIndex(strResultItems, str_fac_1);
			
			String str_fac_2 = CodeInfo.getInstance(this).getValue(ConstVALUE.CODE_TYPE_GOOD_SECD, selectKBInfo.item_fac_2);
			btnItem_fac_2.setText(str_fac_2);
			selectedItem_Fac_2 = StringUtil.getIndex(strResultItems, str_fac_2);
			
			String str_fac_3 = CodeInfo.getInstance(this).getValue(ConstVALUE.CODE_TYPE_GOOD_SECD, selectKBInfo.item_fac_3);
			btnItem_fac_3.setText(str_fac_3);
			selectedItem_Fac_3 = StringUtil.getIndex(strResultItems, str_fac_3);
			
			String str_fac_4 = CodeInfo.getInstance(this).getValue(ConstVALUE.CODE_TYPE_GOOD_SECD, selectKBInfo.item_fac_4);
			btnItem_fac_4.setText(str_fac_4);
			selectedItem_Fac_4 = StringUtil.getIndex(strResultItems, str_fac_4);
			
			String str_fac_5 = CodeInfo.getInstance(this).getValue(ConstVALUE.CODE_TYPE_GOOD_SECD, selectKBInfo.item_fac_5);
			btnItem_fac_5.setText(str_fac_5);
			selectedItem_Fac_5 = StringUtil.getIndex(strResultItems, str_fac_5);
			
			String str_fac_6 = CodeInfo.getInstance(this).getValue(ConstVALUE.CODE_TYPE_GOOD_SECD, selectKBInfo.item_fac_6);
			btnItem_fac_6.setText(str_fac_6);
			selectedItem_Fac_6 = StringUtil.getIndex(strResultItems, str_fac_6);
			
			String str_fac_7 = CodeInfo.getInstance(this).getValue(ConstVALUE.CODE_TYPE_GOOD_SECD, selectKBInfo.item_fac_7);
			btnItem_fac_7.setText(str_fac_7);
			selectedItem_Fac_7 = StringUtil.getIndex(strResultItems, str_fac_7);
			
			String str_fac_8 = CodeInfo.getInstance(this).getValue(ConstVALUE.CODE_TYPE_GOOD_SECD, selectKBInfo.item_fac_8);
			btnItem_fac_8.setText(str_fac_8);
			selectedItem_Fac_8 = StringUtil.getIndex(strResultItems, str_fac_8);
			
			String str_fac_9 = CodeInfo.getInstance(this).getValue(ConstVALUE.CODE_TYPE_GOOD_SECD, selectKBInfo.item_fac_9);
			btnItem_fac_9.setText(str_fac_9);
			selectedItem_Fac_9 = StringUtil.getIndex(strResultItems, str_fac_9);
			
			String str_fac_10 = CodeInfo.getInstance(this).getValue(ConstVALUE.CODE_TYPE_GOOD_SECD, selectKBInfo.item_fac_10);
			btnItem_fac_10.setText(str_fac_10);
			selectedItem_Fac_10 = StringUtil.getIndex(strResultItems, str_fac_10);
			
			String str_fac_11 = CodeInfo.getInstance(this).getValue(ConstVALUE.CODE_TYPE_GOOD_SECD, selectKBInfo.item_fac_11);
			btnItem_fac_11.setText(str_fac_11);
			selectedItem_Fac_11 = StringUtil.getIndex(strResultItems, str_fac_11);
			
			String str_fac_12 = CodeInfo.getInstance(this).getValue(ConstVALUE.CODE_TYPE_GOOD_SECD, selectKBInfo.item_fac_12);
			btnItem_fac_12.setText(str_fac_12);
			selectedItem_Fac_12 = StringUtil.getIndex(strResultItems, str_fac_12);
			
			String str_sett_1 = CodeInfo.getInstance(this).getValue(ConstVALUE.CODE_TYPE_GOOD_SECD, selectKBInfo.item_sett_1);
			btnItem_sett_1.setText(str_sett_1);
			selectedItem_Sett_1 = StringUtil.getIndex(strResultItems, str_sett_1);
			
			String str_sett_2 = CodeInfo.getInstance(this).getValue(ConstVALUE.CODE_TYPE_GOOD_SECD, selectKBInfo.item_sett_2);
			btnItem_sett_2.setText(str_sett_2);
			selectedItem_Sett_2 = StringUtil.getIndex(strResultItems, str_sett_2);
			
			String str_sett_3 = CodeInfo.getInstance(this).getValue(ConstVALUE.CODE_TYPE_GOOD_SECD, selectKBInfo.item_sett_3);
			btnItem_sett_3.setText(str_sett_3);
			selectedItem_Sett_3 = StringUtil.getIndex(strResultItems, str_sett_3);
			
			String str_sett_4 = CodeInfo.getInstance(this).getValue(ConstVALUE.CODE_TYPE_GOOD_SECD, selectKBInfo.item_sett_4);
			btnItem_sett_4.setText(str_sett_4);
			selectedItem_Sett_4 = StringUtil.getIndex(strResultItems, str_sett_4);
			
			String str_etc_1 = CodeInfo.getInstance(this).getValue(ConstVALUE.CODE_TYPE_GOOD_SECD, selectKBInfo.item_etc_1);
			btnItem_etc_1.setText(str_etc_1);
			selectedItem_Etc_1 = StringUtil.getIndex(strResultItems, str_etc_1);
			
			String str_etc_2 = CodeInfo.getInstance(this).getValue(ConstVALUE.CODE_TYPE_GOOD_SECD, selectKBInfo.item_etc_2);
			btnItem_etc_2.setText(str_etc_2);
			selectedItem_Etc_2 = StringUtil.getIndex(strResultItems, str_etc_2);
			
		}
		
//		if (mInfo.send_yn_kb != null && !"".equals(mInfo.send_yn_kb)) {
//			ivComplte.setBackgroundResource(R.drawable.input_kb_send);
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
				AppUtil.startActivity(InputKBActivity.this, new Intent(InputKBActivity.this, MainActivity.class));
			}
		});
	}

	@Override
	public void onClick(View v) {
		Button btn = (Button)v;
		
		switch (v.getId()) {
		case R.id.btnAdd:
			update(false);
			break;
		case R.id.btnEdit:
			update(true);
			break;
		case R.id.btnDelete:
			InputKBDao inputKBDao = InputKBDao.getInstance(this);
			inputKBDao.Delete(mInfo.master_idx);
			InspectResultMasterDao insRetDao = InspectResultMasterDao.getInstance(this);
			insRetDao.updateComplete(mInfo.master_idx, "N", ConstVALUE.CODE_NO_INSPECT_KB);
			setResult();
			finish();
			break;
		case R.id.btnItem_fac_1:
			showItemDialog(btn, v.getId(), selectedItem_Fac_1);
			break;
		case R.id.btnItem_fac_2:
			showItemDialog(btn, v.getId(), selectedItem_Fac_2);
			break;
		case R.id.btnItem_fac_3:
			showItemDialog(btn, v.getId(), selectedItem_Fac_3);
			break;
		case R.id.btnItem_fac_4:
			showItemDialog(btn, v.getId(), selectedItem_Fac_4);
			break;
		case R.id.btnItem_fac_5:
			showItemDialog(btn, v.getId(), selectedItem_Fac_5);
			break;
		case R.id.btnItem_fac_6:
			showItemDialog(btn, v.getId(), selectedItem_Fac_6);
			break;
		case R.id.btnItem_fac_7:
			showItemDialog(btn, v.getId(), selectedItem_Fac_7);
			break;
		case R.id.btnItem_fac_8:
			showItemDialog(btn, v.getId(), selectedItem_Fac_8);
			break;
		case R.id.btnItem_fac_9:
			showItemDialog(btn, v.getId(), selectedItem_Fac_9);
			break;
		case R.id.btnItem_fac_10:
			showItemDialog(btn, v.getId(), selectedItem_Fac_10);
			break;
		case R.id.btnItem_fac_11:
			showItemDialog(btn, v.getId(), selectedItem_Fac_11);
			break;
		case R.id.btnItem_fac_12:
			showItemDialog(btn, v.getId(), selectedItem_Fac_12);
			break;
		case R.id.btnItem_sett_1:
			showItemDialog(btn, v.getId(), selectedItem_Sett_1);
			break;
		case R.id.btnItem_sett_2:
			showItemDialog(btn, v.getId(), selectedItem_Sett_2);
			break;
		case R.id.btnItem_sett_3:
			showItemDialog(btn, v.getId(), selectedItem_Sett_3);
			break;
		case R.id.btnItem_sett_4:
			showItemDialog(btn, v.getId(), selectedItem_Sett_4);
			break;
		case R.id.btnItem_etc_1:
			showItemDialog(btn, v.getId(), selectedItem_Etc_1);
			break;
		case R.id.btnItem_etc_2:
			showItemDialog(btn, v.getId(), selectedItem_Etc_2);
			break;
		case R.id.btnCamera:
			Intent intent = new Intent(InputKBActivity.this, CameraManageActivity.class);
			intent.putExtra("inspect", mInfo);
			startActivity(intent);
			break;
		default:
			break;
		}
	}

	public void showItemDialog(final Button btn, final int itemNo, int selectedItemNo) {
		AlertDialog.Builder dialog = new AlertDialog.Builder(this, AlertDialog.THEME_DEVICE_DEFAULT_LIGHT);
		dialog.setTitle(getString(R.string.input_hj_yb_result));
		
		dialog.setSingleChoiceItems(strResultItems, selectedItemNo, new android.content.DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				switch (itemNo) {
				case R.id.btnItem_fac_1:
					selectedItem_Fac_1 = which;
					break;
				case R.id.btnItem_fac_2:
					selectedItem_Fac_2 = which;
					break;
				case R.id.btnItem_fac_3:
					selectedItem_Fac_3 = which;
					break;
				case R.id.btnItem_fac_4:
					selectedItem_Fac_4 = which;
					break;
				case R.id.btnItem_fac_5:
					selectedItem_Fac_5 = which;
					break;
				case R.id.btnItem_fac_6:
					selectedItem_Fac_6 = which;
					break;
				case R.id.btnItem_fac_7:
					selectedItem_Fac_7 = which;
					break;
				case R.id.btnItem_fac_8:
					selectedItem_Fac_8 = which;
					break;
				case R.id.btnItem_fac_9:
					selectedItem_Fac_9 = which;
					break;
				case R.id.btnItem_fac_10:
					selectedItem_Fac_10 = which;
					break;
				case R.id.btnItem_fac_11:
					selectedItem_Fac_11 = which;
					break;
				case R.id.btnItem_fac_12:
					selectedItem_Fac_12 = which;
					break;
				case R.id.btnItem_sett_1:
					selectedItem_Sett_1 = which;
					break;
				case R.id.btnItem_sett_2:
					selectedItem_Sett_2 = which;
					break;
				case R.id.btnItem_sett_3:
					selectedItem_Sett_3 = which;
					break;
				case R.id.btnItem_sett_4:
					selectedItem_Sett_4 = which;
					break;
				case R.id.btnItem_etc_1:
					selectedItem_Etc_1 = which;
					break;
				case R.id.btnItem_etc_2:
					selectedItem_Etc_2 = which;
					break;
				}
								
				btn.setText(strResultItems[which]);
                dialog.dismiss();
			}
		}).show();
	}
	
	
	public void setInputType() {
		InputKBDao inputKBDao = InputKBDao.getInstance(this);
		if (inputKBDao.existKB(mInfo.master_idx))
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
		String strItem_fac_1 = btnItem_fac_1.getText().toString();
		String strItem_fac_2 = btnItem_fac_2.getText().toString();
		String strItem_fac_3 = btnItem_fac_3.getText().toString();
		String strItem_fac_4 = btnItem_fac_4.getText().toString();
		String strItem_fac_5 = btnItem_fac_5.getText().toString();
		String strItem_fac_6 = btnItem_fac_6.getText().toString();
		String strItem_fac_7 = btnItem_fac_7.getText().toString();
		String strItem_fac_8 = btnItem_fac_8.getText().toString();
		String strItem_fac_9 = btnItem_fac_9.getText().toString();
		String strItem_fac_10 = btnItem_fac_10.getText().toString();
		String strItem_fac_11 = btnItem_fac_11.getText().toString();
		String strItem_fac_12 = btnItem_fac_12.getText().toString();
		String strItem_sett_1 = btnItem_sett_1.getText().toString();
		String strItem_sett_2 = btnItem_sett_2.getText().toString();
		String strItem_sett_3 = btnItem_sett_3.getText().toString();
		String strItem_sett_4 = btnItem_sett_4.getText().toString();
		String strItem_etc_1 = btnItem_etc_1.getText().toString();
		String strItem_etc_2 = btnItem_etc_2.getText().toString();
		
		InputKBDao inputKBDao = InputKBDao.getInstance(this);
		KBInfo log = new KBInfo();
		log.master_idx = mInfo.master_idx;
		log.weather =CodeInfo.getInstance(this).getKey(ConstVALUE.CODE_TYPE_WEATHER, strWeather);
		
		log.item_fac_1 = CodeInfo.getInstance(this).getKey(ConstVALUE.CODE_TYPE_GOOD_SECD, strItem_fac_1);
		log.item_fac_2 = CodeInfo.getInstance(this).getKey(ConstVALUE.CODE_TYPE_GOOD_SECD, strItem_fac_2);
		log.item_fac_3 = CodeInfo.getInstance(this).getKey(ConstVALUE.CODE_TYPE_GOOD_SECD, strItem_fac_3);
		log.item_fac_4 = CodeInfo.getInstance(this).getKey(ConstVALUE.CODE_TYPE_GOOD_SECD, strItem_fac_4);
		log.item_fac_5 = CodeInfo.getInstance(this).getKey(ConstVALUE.CODE_TYPE_GOOD_SECD, strItem_fac_5);
		log.item_fac_6 = CodeInfo.getInstance(this).getKey(ConstVALUE.CODE_TYPE_GOOD_SECD, strItem_fac_6);
		log.item_fac_7 = CodeInfo.getInstance(this).getKey(ConstVALUE.CODE_TYPE_GOOD_SECD, strItem_fac_7);
		log.item_fac_8 = CodeInfo.getInstance(this).getKey(ConstVALUE.CODE_TYPE_GOOD_SECD, strItem_fac_8);
		log.item_fac_9 = CodeInfo.getInstance(this).getKey(ConstVALUE.CODE_TYPE_GOOD_SECD, strItem_fac_9);
		log.item_fac_10 = CodeInfo.getInstance(this).getKey(ConstVALUE.CODE_TYPE_GOOD_SECD, strItem_fac_10);
		log.item_fac_11 = CodeInfo.getInstance(this).getKey(ConstVALUE.CODE_TYPE_GOOD_SECD, strItem_fac_11);
		log.item_fac_12 = CodeInfo.getInstance(this).getKey(ConstVALUE.CODE_TYPE_GOOD_SECD, strItem_fac_12);
		log.item_sett_1 = CodeInfo.getInstance(this).getKey(ConstVALUE.CODE_TYPE_GOOD_SECD, strItem_sett_1);
		log.item_sett_2 = CodeInfo.getInstance(this).getKey(ConstVALUE.CODE_TYPE_GOOD_SECD, strItem_sett_2);
		log.item_sett_3 = CodeInfo.getInstance(this).getKey(ConstVALUE.CODE_TYPE_GOOD_SECD, strItem_sett_3);
		log.item_sett_4 = CodeInfo.getInstance(this).getKey(ConstVALUE.CODE_TYPE_GOOD_SECD, strItem_sett_4);
		log.item_etc_1 = CodeInfo.getInstance(this).getKey(ConstVALUE.CODE_TYPE_GOOD_SECD, strItem_etc_1);
		log.item_etc_2 = CodeInfo.getInstance(this).getKey(ConstVALUE.CODE_TYPE_GOOD_SECD, strItem_etc_2);
		
		
		if (isEdit)
			inputKBDao.Append(log, selectKBInfo.idx);
		else {
			LocManager.getInstance(this).startGetLocation(listener);
			inputKBDao.Append(log);
		}
	
		setResult(RESULT_OK);
		
		InspectResultMasterDao insRetDao = InspectResultMasterDao.getInstance(this);
		insRetDao.updateComplete(mInfo.master_idx, "Y", ConstVALUE.CODE_NO_INSPECT_KB);
		
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
			
			LocManager.getInstance(InputKBActivity.this).setGetGps(true);
			
			String currentLocation = String.format("위도:%f  경도:%f  고도:%f", location.getLatitude(), location.getLongitude(), location.getAltitude());
			Logg.d(InputKBActivity.this, "currentLocation : " + currentLocation);
            Calendar cal = new GregorianCalendar();
            String now = String.format("%d년 %d월 %d일 %d시 %d분 %d초", cal.get(Calendar.YEAR), cal.get(Calendar.MONTH)+1, 
					cal.get(Calendar.DAY_OF_MONTH), cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE),
					cal.get(Calendar.SECOND));
            Logg.d(InputKBActivity.this, "갱신 시간 : " + now);
            
            InspectResultMasterDao insRetDao = InspectResultMasterDao.getInstance(InputKBActivity.this);
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
}
