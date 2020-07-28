package com.kdn.mtps.mobile.input;

import java.util.LinkedHashMap;
import java.util.Map.Entry;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ScrollView;
import android.widget.TextView;

import com.kdn.mtps.mobile.BaseActivity;
import com.kdn.mtps.mobile.MainActivity;
import com.kdn.mtps.mobile.R;
import com.kdn.mtps.mobile.TitleManager;
import com.kdn.mtps.mobile.camera.CameraManageActivity;
import com.kdn.mtps.mobile.constant.ConstVALUE;
import com.kdn.mtps.mobile.info.CodeInfo;
import com.kdn.mtps.mobile.inspect.InspectInfo;
import com.kdn.mtps.mobile.net.api.ApiManager;
import com.kdn.mtps.mobile.net.api.bean.YBInfoList;
import com.kdn.mtps.mobile.util.AnsemUtil;
import com.kdn.mtps.mobile.util.AppUtil;
import com.kdn.mtps.mobile.util.Logg;
import com.kdn.mtps.mobile.util.StringUtil;
import com.kdn.mtps.mobile.util.ToastUtil;
import com.kdn.mtps.mobile.util.UIUtil;
import com.kdn.mtps.mobile.util.thread.ATask;
import com.kdn.mtps.mobile.util.thread.ATask.OnPublishProgress;

public class InputYBActivity extends BaseActivity implements TitleManager, OnClickListener{
	LinearLayout llParent;
	ScrollView svParent;
	
	public LinkedHashMap<Integer, ViewHolder> ybList = new LinkedHashMap<Integer, ViewHolder>();
	
	Button 	btnName;
	Button btnAdd;
	Button btnCamera;
	
	LinearLayout llLayout;
	
	int selectedNameNo = -1;
	
	final String strWeatherItems[] = CodeInfo.getInstance(this).getNames(ConstVALUE.CODE_TYPE_WEATHER);
	final String strInspectResultItems[] = CodeInfo.getInstance(this).getNames(ConstVALUE.CODE_TYPE_GOOD_SECD);
	
	int selectedWeatherNo = 0;
	YBInfoList ybInfoList;
	String strTracksCode;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_input_yb);
		
		setTitle();
		
		setInit();
		
		setData();
		
	}

	@Override
	public void setTitle() {
		Button btnHeaderTitle = (Button)findViewById(R.id.btnHeaderTitle);
		Button btnHeaderLeft = (Button)findViewById(R.id.btnHeaderLeft);
		Button btnHeaderRight = (Button)findViewById(R.id.btnHeaderRight);
		
		String insType = CodeInfo.getInstance(this).getValue(ConstVALUE.CODE_TYPE_INS_TYPE, ConstVALUE.CODE_NO_INSPECT_YB);
		
		btnHeaderTitle.setText(insType + " 등록");
		
		btnHeaderLeft.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		
		btnHeaderRight.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				AppUtil.startActivity(InputYBActivity.this, new Intent(InputYBActivity.this, MainActivity.class));
			}
		});
	}
	
	public void setInit() {
		llParent = (LinearLayout)findViewById(R.id.llParent);
		llParent.setOnTouchListener(hideKeyboardListener);
		
		svParent = (ScrollView)findViewById(R.id.svParent);
		svParent.setOnTouchListener(hideKeyboardListener);
		
		btnName = (Button)findViewById(R.id.btnName);
		btnAdd = (Button)findViewById(R.id.btnAdd);
		
		btnName.setOnClickListener(this);
		btnAdd.setOnClickListener(this);
		
		btnCamera = (Button)findViewById(R.id.btnCamera);
		btnCamera.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btnName:
			showNameDialog();
			break;
		case R.id.btnAdd:
			if (selectedNameNo < 0) {
				ToastUtil.show(this, "선로를 선택해 주세요.");
				return;
			}
			
			if (ybList.isEmpty()) {
				ToastUtil.show(this, "저장할 정보가 없습니다.");
				return;
			}
			
			String strTracksName = btnName.getText().toString();
			strTracksCode = CodeInfo.getInstance(InputYBActivity.this).getKey(ConstVALUE.CODE_TYPE_TRACKS, strTracksName);
			
			for (Entry<Integer, ViewHolder> entry : ybList.entrySet()) {
				ViewHolder viewHolder = entry.getValue();
				String strCheckResult = viewHolder.btnCheckResult.getText().toString();
				String strContents = viewHolder.editContent.getText().toString();
				
				YBInfo info = new YBInfo();
				info.RGT_SN = viewHolder.ybInfo.RGT_SN;
				info.TINS_RESULT_SECD = CodeInfo.getInstance(this).getKey(ConstVALUE.CODE_TYPE_GOOD_SECD, strCheckResult);
				info.TINS_RESULT = strContents;
				info.TINS_RESULT = info.TINS_RESULT.replace("=", "");
				
				apiInsertYB(info, strTracksCode);
			}
				
			ToastUtil.show(this, "저장이 완료되었습니다.");
			break;
		case R.id.btnCamera:
			if (selectedNameNo < 0) {
				ToastUtil.show(this, "선로를 선택해 주세요.");
				return;
			}
			
			Intent intent = new Intent(InputYBActivity.this, CameraManageActivity.class);
			InspectInfo info = new InspectInfo();
			info.master_idx = strTracksCode;
			info.setType(ConstVALUE.CODE_NO_INSPECT_YB);
			intent.putExtra("inspect", info);
			startActivity(intent);
			break;
		}
		
	}

	public void setData() {
		llLayout = (LinearLayout)findViewById(R.id.llLayout);
	}
	
	public void showNameDialog() {
		
//		final String strItems[] = {"선로1","선로2","선로3"};
		final String strItems[] = CodeInfo.getInstance(this).getTracksNames();
		
		AlertDialog.Builder dialog = new AlertDialog.Builder(this, AlertDialog.THEME_DEVICE_DEFAULT_LIGHT);
		dialog.setTitle(getString(R.string.facility_name));
		dialog.setSingleChoiceItems(strItems, selectedNameNo, new android.content.DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				selectedNameNo = which;
                btnName.setText(strItems[which]);
                strTracksCode = CodeInfo.getInstance(InputYBActivity.this).getKey(ConstVALUE.CODE_TYPE_TRACKS, strItems[which]);
                
                if (!AnsemUtil.isConnected(InputYBActivity.this))
    				AnsemUtil.showAnsemLogin(InputYBActivity.this);
    			else {
    				getYBList(strTracksCode);
    			}
                
                
                dialog.dismiss();
			}
		}).show();
	}

	public void getYBList(final String tracksCode) {
		
		ATask.executeVoidPublishProgress(new ATask.OnTaskPublishProgress() {
			public void onPre() {
			}
	
			@Override
			public void onBG(OnPublishProgress onPublish) {
				ybInfoList = ApiManager.ybInfoList(tracksCode);
			}
	
			@Override
			public void onPost() {
				ybList = new LinkedHashMap<Integer, ViewHolder>(); 
				llLayout.removeAllViews();
				
				if (ybInfoList == null || ybInfoList.preventingInspection.isEmpty()) {
					ToastUtil.show(InputYBActivity.this, "순시 정보가 없습니다.");
					return;
				}
				
				for (int idx=0; idx<ybInfoList.preventingInspection.size(); idx ++) {
					YBInfo info = ybInfoList.preventingInspection.get(idx);
					addItem(idx, info);
				}
				
				addBottomPadding();
			}

			@Override
			public void onProgress(int progress) {
				// TODO Auto-generated method stub
				
			}
			
		});
	}
	
	public void apiInsertYB(final YBInfo info, final String tracksCode) {
		
		ATask.executeVoidPublishProgress(new ATask.OnTaskPublishProgress() {
			public void onPre() {
			}
	
			@Override
			public void onBG(OnPublishProgress onPublish) {
				ApiManager.ybInsert(InputYBActivity.this, info, tracksCode);
			}
	
			@Override
			public void onPost() {
			}

			@Override
			public void onProgress(int progress) {
				// TODO Auto-generated method stub
				
			}
			
		});
	}

	public void addItem(int idx, YBInfo info) {
		View view = LayoutInflater.from(this).inflate(R.layout.listview_input_yb_item_, null);
		LinearLayout llItemParent = (LinearLayout) view.findViewById(R.id.llItemParent);
		
		final ViewHolder viewHolder = new ViewHolder();
		
		UIUtil.setFont(this, (ViewGroup)llItemParent);
		
		viewHolder.ybInfo = info;
		viewHolder.tvNo = (TextView)view.findViewById(R.id.tvNo);
		viewHolder.tvTowerNo1 = (TextView)view.findViewById(R.id.tvTowerNo1);
		viewHolder.tvTowerNo2 = (TextView)view.findViewById(R.id.tvTowerNo2);
		viewHolder.tvPlaceName = (TextView)view.findViewById(R.id.tvPlaceName);
		viewHolder.btnCheckResult = (Button)view.findViewById(R.id.btnCheckResult);
		viewHolder.editContent = (EditText)view.findViewById(R.id.editContent);
		
		viewHolder.tvNo.setText((idx+1)+"");
		viewHolder.tvTowerNo1.setText(info.BEGIN_EQP_NM);
		viewHolder.tvTowerNo2.setText(info.END_EQP_NM);
		viewHolder.tvPlaceName.setText(info.CWRK_NM);
		
		if (info.TINS_RESULT_SECD == null || "".equals( info.TINS_RESULT_SECD)) {
			viewHolder.btnCheckResult.setText(strInspectResultItems[0]);
			viewHolder.btnCheckResult.setId(0);
		} else {
			String strResultSecd = CodeInfo.getInstance(this).getValue(ConstVALUE.CODE_TYPE_GOOD_SECD, info.TINS_RESULT_SECD);
			viewHolder.btnCheckResult.setText(strResultSecd);
			int index = StringUtil.getIndex(strInspectResultItems, strResultSecd);
			viewHolder.btnCheckResult.setId(index);
		}
		
		viewHolder.editContent.setText(info.TINS_RESULT);
		
		viewHolder.btnCheckResult.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				showInspectResultDialog((Button)v);
			}
		});
		
		ybList.put(idx, viewHolder);
		llLayout.addView(view);
	}
	
	public class ViewHolder {
		TextView tvNo;
		TextView tvTowerNo1;
		TextView tvTowerNo2;
        TextView tvPlaceName;
        Button btnCheckResult;
        EditText editContent;
        
		public YBInfo ybInfo = null;
	}

	public void showInspectResultDialog(final Button btn) {
		
		AlertDialog.Builder dialog = new AlertDialog.Builder(this, AlertDialog.THEME_DEVICE_DEFAULT_LIGHT);
		dialog.setTitle(this.getString(R.string.input_bt_weather));
		Logg.d("gid : " + btn.getId());
		dialog.setSingleChoiceItems(strInspectResultItems, btn.getId(), new android.content.DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				btn.setId(which);
				btn.setText(strInspectResultItems[which]);
                dialog.dismiss();
			}
		}).show();
		
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		if (requestCode == AnsemUtil.REQUEST_ANSEM_CODE) {
			if (AnsemUtil.isConnected(this)) {
				getYBList(strTracksCode);
			}
		}
		
	}
	
	OnTouchListener hideKeyboardListener = new OnTouchListener() {
		@Override
		public boolean onTouch(View v, MotionEvent event) {
			UIUtil.hideKeyboard(llParent);
			return false;
		}
	};
	
	public void addBottomPadding() {
		LinearLayout linearPadding = new LinearLayout(this);
		linearPadding.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, (int) UIUtil.pxFromDp(this, 50)));
		llLayout.addView(linearPadding);		
	}
}
