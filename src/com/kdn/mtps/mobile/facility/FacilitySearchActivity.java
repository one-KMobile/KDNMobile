package com.kdn.mtps.mobile.facility;

import java.util.ArrayList;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.kdn.mtps.mobile.BaseActivity;
import com.kdn.mtps.mobile.R;
import com.kdn.mtps.mobile.TitleManager;
import com.kdn.mtps.mobile.db.TowerListDao;
import com.kdn.mtps.mobile.info.CodeInfo;
import com.kdn.mtps.mobile.net.api.bean.FacilityList;
import com.kdn.mtps.mobile.util.AppUtil;
import com.kdn.mtps.mobile.util.ToastUtil;
import com.kdn.mtps.mobile.util.thread.ATask;

public class FacilitySearchActivity extends BaseActivity implements TitleManager, OnClickListener, OnScrollListener{

	Button btnName;
	Button btnGubun;
	//EditText editEqpName;
	Button btnSearch;
	ListView listFacility;
	TextView tvNoData;
	ImageView ivBox;
	ArrayList<FacilityInfo> facList;
	FacilitySearchBaseAdapter adapter;
	int selectedGubunNo = -1;
	int selectedNameNo = -1;
	int selectedTypeNo = -1;

	FacilityList resultTowerList;

	boolean isLock;
	boolean isEnd;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_facility_search);
		
		setTitle();
		
		setInit();
		
		listFacility.setAdapter(adapter);
		
	}

	@Override
	public void setTitle() {
		Button btnHeaderTitle = (Button)findViewById(R.id.btnHeaderTitle);
		Button btnHeaderLeft = (Button)findViewById(R.id.btnHeaderLeft);
		Button btnHeaderRight = (Button)findViewById(R.id.btnHeaderRight);
		
		btnHeaderTitle.setText("송전설비 검색");
		//btnHeaderRight.setBackgroundResource(R.drawable.selector_btn_map);
		
		btnHeaderLeft.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		
		btnHeaderRight.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				
				ArrayList<FacilityInfo>list = (ArrayList<FacilityInfo>) adapter.getList();
				
				if (list.isEmpty()) {
					ToastUtil.show(FacilitySearchActivity.this, "검색된 데이터가 없습니다.");
					return;
				}
				
				Intent intent = new Intent(FacilitySearchActivity.this, FacilityMapListActivity.class);
				intent.putExtra("locations", list);
//				intent.putParcelableArrayListExtra("locations", infoList);
				AppUtil.startActivity(FacilitySearchActivity.this, intent);
			}
		});
	}
	
	public void setInit() {

		btnGubun = (Button)findViewById(R.id.btnGubun);
		btnName = (Button)findViewById(R.id.btnName);
		//editEqpName = (EditText)findViewById(R.id.editEqpName);
		btnSearch = (Button)findViewById(R.id.btnSearch);

		btnGubun.setOnClickListener(this);
		btnName.setOnClickListener(this);
		btnSearch.setOnClickListener(this);
		
		listFacility = (ListView)findViewById(R.id.listFacility);
		listFacility.setOnScrollListener(this);
		
		tvNoData = (TextView)findViewById(R.id.tvNoData);
		ivBox = (ImageView)findViewById(R.id.ivBox);
		
		adapter = new FacilitySearchBaseAdapter(this);
	}

	public void showNameDialog() {

		//final String strGItems[] = {"회선","전력구","관로"};
		final String strItems[] = CodeInfo.getInstance(this).getTracksNames();

		AlertDialog.Builder dialog = new AlertDialog.Builder(this, AlertDialog.THEME_DEVICE_DEFAULT_LIGHT);
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

	public void showNameDialog1() {

		final String strGItems[] = {"회선","전력구","관로"};

		AlertDialog.Builder dialog = new AlertDialog.Builder(this, AlertDialog.THEME_DEVICE_DEFAULT_LIGHT);
		dialog.setTitle(getString(R.string.facility_gubun_name));
		dialog.setSingleChoiceItems(strGItems, selectedGubunNo, new android.content.DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				selectedGubunNo = which;
				btnGubun.setText(strGItems[which]);
				dialog.dismiss();
			}
		}).show();
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btnGubun:
			showNameDialog1();
			break;
		case R.id.btnName:
			showNameDialog();
			break;
		case R.id.btnSearch:
			
			if (selectedNameNo == -1) {
				ToastUtil.show(FacilitySearchActivity.this, "선로를 선택해 주세요.");
				return;
			}
			
			isEnd = false;
			adapter.setListClear();
			apiSearchTowerList();
			break;
		}
	}
	
	public void apiSearchTowerList() {
		final String strName = btnName.getText().toString();
		final String strCode = CodeInfo.getInstance(this).getTracksCode(strName);
		//final String strEqpName = editEqpName.getText().toString();
		
		ATask.executeVoidProgress(this, R.string.facility_searching, false, new ATask.OnTaskProgress() {
			public void onPre() {
			}
	
			public void onBG() {
				TowerListDao towerListDao = TowerListDao.getInstance(FacilitySearchActivity.this);
				facList = towerListDao.selectFacList(strName, "");
			}
	
			@Override
			public void onPost() {
				
				adapter.setList(facList);
				
				if (adapter.getCount() <= 0 ) {
					tvNoData.setVisibility(View.VISIBLE);
					ivBox.setVisibility(View.GONE);
				} else {
					tvNoData.setVisibility(View.GONE);
					ivBox.setVisibility(View.VISIBLE);
				}
			}
			
			@Override
			public void onCancel() {
			}
			
		});
	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
		int count = totalItemCount - visibleItemCount;
		
//		if (firstVisibleItem >= count && totalItemCount != 0 && !isLock && !isEnd) {
//			Logg.d("onScroll : " + firstVisibleItem + " / " + count + " / " + totalItemCount);
//			apiSearchTowerList();
//		}
	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		// TODO Auto-generated method stub
		
	}

}
