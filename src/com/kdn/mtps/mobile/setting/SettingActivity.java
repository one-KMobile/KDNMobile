package com.kdn.mtps.mobile.setting;

import java.util.LinkedHashMap;
import java.util.Map.Entry;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.kdn.mtps.mobile.BaseActivity;
import com.kdn.mtps.mobile.MainActivity;
import com.kdn.mtps.mobile.R;
import com.kdn.mtps.mobile.TitleManager;
import com.kdn.mtps.mobile.constant.ConstSP;
import com.kdn.mtps.mobile.constant.ConstVALUE;
import com.kdn.mtps.mobile.info.CodeInfo;
import com.kdn.mtps.mobile.net.api.ApiManager;
import com.kdn.mtps.mobile.net.api.bean.PatrolmansList;
import com.kdn.mtps.mobile.net.api.bean.PatrolmansList.PatrolMansInfo;
import com.kdn.mtps.mobile.util.AnsemUtil;
import com.kdn.mtps.mobile.util.AppUtil;
import com.kdn.mtps.mobile.util.Shared;
import com.kdn.mtps.mobile.util.StringUtil;
import com.kdn.mtps.mobile.util.ToastUtil;
import com.kdn.mtps.mobile.util.thread.ATask;

public class SettingActivity extends BaseActivity implements TitleManager, OnClickListener{

	Button btnWeather;
	Button btnInspecter_1;
	Button btnInspecter_2;
	Button btnCompany;

	int selectedWeatherNo = 0;
	int selectedInspecterNo = 0;
	int selectedCompanyNo = 0;
	
	final String strWeatherItems[] = CodeInfo.getInstance(this).getNames(ConstVALUE.CODE_TYPE_WEATHER);
	final String strInspecerItems[] = CodeInfo.getInstance(this).getNames(ConstVALUE.CODE_TYPE_WEATHER);
	final String strCompanyItems[] = {"KEPCO","한전KDN","한전KPS"};
	
	public  LinkedHashMap<String, String> PatrolMansMap = new LinkedHashMap<String, String>();
	PatrolmansList patrolmansList;
	boolean isFirst = false;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setting);
		
		Intent intent = getIntent();
		boolean isSetInspecter = intent.getBooleanExtra("setInspecter", false);
		
		if (isSetInspecter)
			ToastUtil.show(this, "순시자(정/부)를 선택해 주세요.");
		
		setTitle();
		
		setInit();
		
		setData();
	}

	@Override
	public void setTitle() {
		Button btnHeaderTitle = (Button)findViewById(R.id.btnHeaderTitle);
		Button btnHeaderLeft = (Button)findViewById(R.id.btnHeaderLeft);
		Button btnHeaderRight = (Button)findViewById(R.id.btnHeaderRight);
		
		btnHeaderTitle.setText("설정");
		
		btnHeaderLeft.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		
		btnHeaderRight.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				AppUtil.startActivity(SettingActivity.this, new Intent(SettingActivity.this, MainActivity.class));
			}
		});
	}
	
	public void setInit() {
		btnWeather = (Button)findViewById(R.id.btnWeather);
		btnInspecter_1 = (Button)findViewById(R.id.btnInspecter_1);
		btnInspecter_2 = (Button)findViewById(R.id.btnInspecter_2);
		btnCompany = (Button)findViewById(R.id.btnCompany);
		btnCompany.setOnClickListener(this);
		
		btnWeather.setOnClickListener(this);
		btnInspecter_1.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (PatrolMansMap.isEmpty()) {
					isFirst = true;
					if (!AnsemUtil.isConnected(SettingActivity.this))
						AnsemUtil.showAnsemLogin(SettingActivity.this);
					else {
						apiPatrolMansList_1();
					}
					
				} else
					showInspecterDialog_1();
			}
		});
		btnInspecter_2.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (PatrolMansMap.isEmpty()) {
					if (!AnsemUtil.isConnected(SettingActivity.this))
						AnsemUtil.showAnsemLogin(SettingActivity.this);
					else {
						apiPatrolMansList_2();
					}
					
				} else
					showInspecterDialog_2();
			}
		});
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btnWeather:
			showWeatherDialog();
			break;
		case R.id.btnCompany:
			showCompanyDialog();
			break;
		}
		
	}
	
	public void setData() {
		String strWeather = Shared.getString(this, ConstSP.SETTING_WEATHER);
		if ("".equals(strWeather))
			btnWeather.setText(strWeatherItems[0]);
		else {
			selectedWeatherNo = StringUtil.getIndex(strWeatherItems, strWeather);
			btnWeather.setText(strWeather);
		}
		
		String inspecter_1 = Shared.getString(this, ConstSP.SETTING_INSPECTER_NAME_1);
		String inspecter_2 = Shared.getString(this, ConstSP.SETTING_INSPECTER_NAME_2);
		btnInspecter_1.setText(inspecter_1);
		btnInspecter_2.setText(inspecter_2);
		
		String strCompany = Shared.getString(this, ConstSP.SETTING_COMPANY);
		selectedCompanyNo = StringUtil.getIndex(strCompanyItems, strCompany);
		
		btnCompany.setText(strCompanyItems[selectedCompanyNo]);
	}

	public void showWeatherDialog() {
		
		AlertDialog.Builder dialog = new AlertDialog.Builder(this, AlertDialog.THEME_DEVICE_DEFAULT_LIGHT);
		dialog.setTitle(getString(R.string.input_bt_weather));
		dialog.setSingleChoiceItems(strWeatherItems, selectedWeatherNo, new android.content.DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				Shared.set(SettingActivity.this, ConstSP.SETTING_WEATHER, strWeatherItems[which]);
				
				selectedWeatherNo = which;
                btnWeather.setText(strWeatherItems[which]);
                dialog.dismiss();
			}
		}).show();
		
	}

	public void showCompanyDialog() {
		
		AlertDialog.Builder dialog = new AlertDialog.Builder(this, AlertDialog.THEME_DEVICE_DEFAULT_LIGHT);
		dialog.setTitle(getString(R.string.input_bt_set_company));
		dialog.setSingleChoiceItems(strCompanyItems, selectedCompanyNo, new android.content.DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				Shared.set(SettingActivity.this, ConstSP.SETTING_COMPANY, strCompanyItems[which]);
				
				selectedCompanyNo = which;
                btnCompany.setText(strCompanyItems[which]);
                dialog.dismiss();
			}
		}).show();
		
	}

	public void showInspecterDialog_1() {
		
		final String[] strInspecterItems = getInspecterNames();
		
		String inspecter_1 = Shared.getString(this, ConstSP.SETTING_INSPECTER_NAME_1);
		int idx = StringUtil.getIndex(strInspecterItems, inspecter_1, -1);
		
		AlertDialog.Builder dialog = new AlertDialog.Builder(this, AlertDialog.THEME_DEVICE_DEFAULT_LIGHT);
		dialog.setTitle(getString(R.string.inspecter_1));
		dialog.setSingleChoiceItems(strInspecterItems, idx, new android.content.DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				String inspecter_2= Shared.getString(SettingActivity.this, ConstSP.SETTING_INSPECTER_ID_2);
				String key = CodeInfo.getInstance(SettingActivity.this).searchKey(PatrolMansMap, strInspecterItems[which]);
				
				if (key.equals(inspecter_2)) {
					ToastUtil.show(SettingActivity.this, "순시자(부)와 다른 순시자를 선택해 주세요.");
					dialog.dismiss();
					return;
				}
				
				Shared.set(SettingActivity.this, ConstSP.SETTING_INSPECTER_ID_1, key);
				Shared.set(SettingActivity.this, ConstSP.SETTING_INSPECTER_NAME_1, strInspecterItems[which]);
				
				btnInspecter_1.setText(strInspecterItems[which]);
                dialog.dismiss();
			}
		}).show();
		
	}
	
	public void showInspecterDialog_2() {
		
		final String[] strInspecterItems = getInspecterNames();
		
		String inspecter_2 = Shared.getString(this, ConstSP.SETTING_INSPECTER_NAME_2);
		int idx = StringUtil.getIndex(strInspecterItems, inspecter_2, -1);
		
		AlertDialog.Builder dialog = new AlertDialog.Builder(this, AlertDialog.THEME_DEVICE_DEFAULT_LIGHT);
		dialog.setTitle(getString(R.string.inspecter_2));
		dialog.setSingleChoiceItems(strInspecterItems, idx, new android.content.DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				String inspecter_1 = Shared.getString(SettingActivity.this, ConstSP.SETTING_INSPECTER_ID_1);
				String key = CodeInfo.getInstance(SettingActivity.this).searchKey(PatrolMansMap, strInspecterItems[which]);
				
				if (key.equals(inspecter_1)) {
					ToastUtil.show(SettingActivity.this, "순시자(정)과 다른 순시자를 선택해 주세요.");
					dialog.dismiss();
					return;
				}
					
				Shared.set(SettingActivity.this, ConstSP.SETTING_INSPECTER_ID_2, key);
				Shared.set(SettingActivity.this, ConstSP.SETTING_INSPECTER_NAME_2, strInspecterItems[which]);
				
				btnInspecter_2.setText(strInspecterItems[which]);
                dialog.dismiss();
			}
		}).show();
		
	}
	
	public void apiPatrolMansList_1() {
		
		ATask.executeVoid(new ATask.OnTask() {
			public void onPre() {
			}

			public void onBG() {
				patrolmansList = ApiManager.patrolMansList();
			}

			@Override
			public void onPost() {
				if (patrolmansList != null && patrolmansList.patrolmans != null) {
					if (ApiManager.RESULT_OK.equals(patrolmansList.code)) {
						for (PatrolMansInfo info : patrolmansList.patrolmans) {
							PatrolMansMap.put(info.USER_ID, info.USER_NAME);
						}
						
						showInspecterDialog_1();
					}
				}
			}
		});
	}
	
	public void apiPatrolMansList_2() {
		
		ATask.executeVoid(new ATask.OnTask() {
			public void onPre() {
			}

			public void onBG() {
				patrolmansList = ApiManager.patrolMansList();
			}

			@Override
			public void onPost() {
				if (patrolmansList != null && patrolmansList.patrolmans != null) {
					if (ApiManager.RESULT_OK.equals(patrolmansList.code)) {
						for (PatrolMansInfo info : patrolmansList.patrolmans) {
							PatrolMansMap.put(info.USER_ID, info.USER_NAME);
						}
						
						showInspecterDialog_2();
					}
				}
			}
		});
	}
	
	public String[] getInspecterNames() {
		
		String strArray[] = new String[PatrolMansMap.size()];
		int index = 0;
		for (Entry<String, String> entry : PatrolMansMap.entrySet()) {
				strArray[index++] = entry.getValue();
	    }
	    return strArray;
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		if (requestCode == AnsemUtil.REQUEST_ANSEM_CODE) {
			if (AnsemUtil.isConnected(this)) {
				if (isFirst)
					showInspecterDialog_1();
				else
					showInspecterDialog_2();
			}
		}
		
	}
}
