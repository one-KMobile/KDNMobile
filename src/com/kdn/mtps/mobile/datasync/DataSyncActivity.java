package com.kdn.mtps.mobile.datasync;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.kdn.mtps.mobile.BaseActivity;
import com.kdn.mtps.mobile.MainActivity;
import com.kdn.mtps.mobile.R;
import com.kdn.mtps.mobile.TitleManager;
import com.kdn.mtps.mobile.constant.ConstSP;
import com.kdn.mtps.mobile.constant.ConstVALUE;
import com.kdn.mtps.mobile.datasync.DataSync.OnSyncCompletedListener;
import com.kdn.mtps.mobile.db.InputBRSubInfoDao;
import com.kdn.mtps.mobile.db.InputHKSubInfoDao;
import com.kdn.mtps.mobile.db.InputJSSubInfoDao;
import com.kdn.mtps.mobile.db.InspectResultMasterDao;
import com.kdn.mtps.mobile.db.ManageCodeDao;
import com.kdn.mtps.mobile.db.TowerListDao;
import com.kdn.mtps.mobile.db.bean.ManageCodeLog;
import com.kdn.mtps.mobile.facility.FacilityInfo;
import com.kdn.mtps.mobile.input.BRSubInfo;
import com.kdn.mtps.mobile.input.HKSubInfo;
import com.kdn.mtps.mobile.input.JSSubInfo;
import com.kdn.mtps.mobile.inspect.InspectInfo;
import com.kdn.mtps.mobile.util.AnsemUtil;
import com.kdn.mtps.mobile.util.AppUtil;
import com.kdn.mtps.mobile.util.Shared;
import com.kdn.mtps.mobile.util.ToastUtil;
import com.kdn.mtps.mobile.util.thread.ATask;

public class DataSyncActivity extends BaseActivity implements TitleManager, OnClickListener, OnSyncCompletedListener{

	Button btnStartSync;
	Button btnCheckSync;
	Button btnLoadData;
	TextView tvSyncDate;
	Context mContext;
      
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_datasync);
		
		setTitle();
		
		setInit();
		
	}

	@Override
	public void setTitle() {
		Button btnHeaderTitle = (Button)findViewById(R.id.btnHeaderTitle);
		Button btnHeaderLeft = (Button)findViewById(R.id.btnHeaderLeft);
		Button btnHeaderRight = (Button)findViewById(R.id.btnHeaderRight);
		
		btnHeaderTitle.setText("정보 동기화");
		
		btnHeaderLeft.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		
		btnHeaderRight.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				AppUtil.startActivity(DataSyncActivity.this, new Intent(DataSyncActivity.this, MainActivity.class));
			}
		});
	}

	public void setInit() {
		mContext = this;
		
		btnStartSync = (Button)findViewById(R.id.btnStartSync);
		btnStartSync.setOnClickListener(this);
		
		btnCheckSync = (Button)findViewById(R.id.btnCheckSync);
		btnCheckSync.setOnClickListener(this);
		
		btnLoadData = (Button)findViewById(R.id.btnLoadData);
		btnLoadData.setOnClickListener(this);
		
		tvSyncDate = (TextView)findViewById(R.id.tvSyncDate);
		
		String syncDate = Shared.getString(this, ConstSP.SYNC_DATE);
		tvSyncDate.setText(syncDate);
		
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btnStartSync:
//			Intent intent = new Intent(DataSyncActivity.this, InspectResultSearchActivity.class);
//			intent.putExtra(ConstVALUE.DISPLAY_MENU_TYPE, ConstVALUE.DISPLAY_MENU_INSPECT_REGIST_READY);
//			AppUtil.startActivity(DataSyncActivity.this, intent);
			if (!AnsemUtil.isConnected(this))
				AnsemUtil.showAnsemLogin(this);
			else {
				DataSync.getInstance(mContext).setOnSyncResult(DataSyncActivity.this);
				DataSync.getInstance(mContext).startSync(false);
			}
			break;
		case R.id.btnLoadData:
			startLoadExcel();
			break;
		default:
			break;
		}
	}

	@Override
	public void onSyncCompleted(int callType, boolean isComplete) {
		if (isComplete)
			ToastUtil.show(this, "데이터 동기화가 완료되었습니다.");
		
		String syncDate = Shared.getString(this, ConstSP.SYNC_DATE);
		//tvSyncDate.setText("마지막 정보 갱신시각 : " + syncDate);
		tvSyncDate.setText(syncDate);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		if (requestCode == AnsemUtil.REQUEST_ANSEM_CODE) {
			if (AnsemUtil.isConnected(this)) {
				DataSync.getInstance(mContext).setOnSyncResult(DataSyncActivity.this);
				DataSync.getInstance(mContext).startSync(false);
			}
		}
		
	}
	
	public void startLoadExcel() {
		
		String tgtDir = ConstVALUE.PATH_SDCARD_KDN_ROOT;
		File dir = new File(tgtDir);
		if (!dir.exists()) {
			ToastUtil.show(this, "읽을 파일이 없습니다.");
			return;
		}
		
		File loadFile = new File(dir, "input_data.xls");
		if (!loadFile.exists()) {
			ToastUtil.show(this, "읽을 파일이 없습니다.");
			return;
		}
		
		ATask.executeVoidProgress(DataSyncActivity.this, R.string.load_excel, false, new ATask.OnTaskProgress() {
			public void onPre() {
			}

			public void onBG() {
				loadExcel();
			}

			@Override
			public void onPost() {
				ToastUtil.show(DataSyncActivity.this, "데이터 가져오기가 완료되었습니다.");
			}
			
			@Override
			public void onCancel() {
			}
			
		});
		
	}

	public void loadExcel() {
		
		Workbook myWorkbook;
		try {
			String tgtDir = ConstVALUE.PATH_SDCARD_KDN_ROOT;
			File dir = new File(tgtDir);
			
			myWorkbook = Workbook.getWorkbook(new File(dir, "input_data.xls"));
			
			Sheet mySheet = myWorkbook.getSheet(0);// 파일을 열고 시트를 지정해준다.

			int col = mySheet.getColumns(); // 열을 읽어온다
	        int row = mySheet.getRows(); // 행을 읽어 온다

	        //스케줄
	        InspectResultMasterDao inspectResultMasterDao = InspectResultMasterDao.getInstance(this);
			inspectResultMasterDao.DeleteAll();
			
			for( int i = 1; i < row; i++ ) { // 열
				InspectInfo insInfo = new InspectInfo();
				int j=0;
	            	insInfo.ins_plan_no = mySheet.getCell(j++, i).getContents();
	            	insInfo.towerNo = mySheet.getCell(j++, i).getContents();
	            	insInfo.date = mySheet.getCell(j++, i).getContents();
	            	insInfo.ins_sn = mySheet.getCell(j++, i).getContents();
	            	insInfo.fnct_lc_no = mySheet.getCell(j++, i).getContents();
	            	insInfo.tracksName = mySheet.getCell(j++, i).getContents();
	            	insInfo.type = mySheet.getCell(j++, i).getContents();
	            	insInfo.typeName = mySheet.getCell(j++, i).getContents();
	            	insInfo.eqpNo = mySheet.getCell(j++, i).getContents();
	            	insInfo.eqpNm = mySheet.getCell(j++, i).getContents();
	            	insInfo.latitude = mySheet.getCell(j++, i).getContents();
	            	insInfo.longitude = mySheet.getCell(j++, i).getContents();
	            	insInfo.address = mySheet.getCell(j++, i).getContents();
	            	insInfo.unity_ins_no = mySheet.getCell(j++, i).getContents();
	            	
	            	String ins_ty_cd_count = mySheet.getCell(j++, i).getContents();
	            	
	            	if (ConstVALUE.CODE_NO_INSPECT_HK.equals(insInfo.type) || ConstVALUE.CODE_NO_INSPECT_HJ.equals(insInfo.type)) {
						if (Integer.valueOf(ins_ty_cd_count) == 0)
							continue;
					}
	            	
					inspectResultMasterDao.Append(insInfo);
			}
			
			mySheet = myWorkbook.getSheet(1);// 파일을 열고 시트를 지정해준다.

			col = mySheet.getColumns(); // 열을 읽어온다
	        row = mySheet.getRows(); // 행을 읽어 온다

	        //코드
	        ManageCodeDao manageCodeDao = ManageCodeDao.getInstance(this);
	        manageCodeDao.DeleteAll();
	        
	        ArrayList<ManageCodeLog> logs = new ArrayList<ManageCodeLog>();
	        
			for( int i = 1; i < row; i++ ) { // 열
				ManageCodeLog log = new ManageCodeLog();
				int j=0;
	            	log.code_type = mySheet.getCell(j++, i).getContents();
	            	mySheet.getCell(j++, i).getContents();
	            	log.code_key = mySheet.getCell(j++, i).getContents();
	            	log.code_value = mySheet.getCell(j++, i).getContents();
	            	logs.add(log);
			}
			manageCodeDao.Append(logs);
			
			mySheet = myWorkbook.getSheet(2);// 파일을 열고 시트를 지정해준다.

			col = mySheet.getColumns(); // 열을 읽어온다
	        row = mySheet.getRows(); // 행을 읽어 온다

	        logs = new ArrayList<ManageCodeLog>();
	        
	        //선로
			for( int i = 1; i < row; i++ ) { // 열
				ManageCodeLog log = new ManageCodeLog();
				int j=0;
	            	log.code_type = ConstVALUE.CODE_TYPE_TRACKS;
	            	log.code_key = mySheet.getCell(j++, i).getContents();
	            	log.code_value = mySheet.getCell(j++, i).getContents();
	            	logs.add(log);
			}
			manageCodeDao.Append(logs);
			
			mySheet = myWorkbook.getSheet(3);// 파일을 열고 시트를 지정해준다.

			col = mySheet.getColumns(); // 열을 읽어온다
	        row = mySheet.getRows(); // 행을 읽어 온다

	        //송전설비
	        TowerListDao towerListDao = TowerListDao.getInstance(this);
	        towerListDao.DeleteAll();
	        
			for( int i = 1; i < row; i++ ) { // 열
				FacilityInfo facInfo = new FacilityInfo();
				int j=0;
				facInfo.FNCT_LC_DTLS = mySheet.getCell(j++, i).getContents();
				facInfo.TOWER_IDX = mySheet.getCell(j++, i).getContents();
				facInfo.EQP_NO = mySheet.getCell(j++, i).getContents();
				mySheet.getCell(j++, i).getContents();
				facInfo.EQP_NM = mySheet.getCell(j++, i).getContents();
				facInfo.LATITUDE = mySheet.getCell(j++, i).getContents();
				facInfo.LONGITUDE = mySheet.getCell(j++, i).getContents();
				facInfo.ADDRESS = mySheet.getCell(j++, i).getContents();
				facInfo.EQP_TY_CD_NM = mySheet.getCell(j++, i).getContents();
				facInfo.CONT_NUM = mySheet.getCell(j++, i).getContents();
				
				towerListDao.Append(facInfo);
			}
			
			mySheet = myWorkbook.getSheet(4);// 파일을 열고 시트를 지정해준다.
			col = mySheet.getColumns(); // 열을 읽어온다
	        row = mySheet.getRows(); // 행을 읽어 온다
	        
	        //전선접속
	        InputJSSubInfoDao inputJSSubInfoDao = InputJSSubInfoDao.getInstance(this);
	        inputJSSubInfoDao.DeleteAll();
	        
	        for( int i = 1; i < row; i++ ) { // 열
				JSSubInfo subInfo = new JSSubInfo();
				int j=0;
				subInfo.EQP_NO = mySheet.getCell(j++, i).getContents();
				subInfo.EQP_NM = mySheet.getCell(j++, i).getContents();
				subInfo.FNCT_LC_NO = mySheet.getCell(j++, i).getContents();
				subInfo.FNCT_LC_DTLS = mySheet.getCell(j++, i).getContents();
				subInfo.TOWER_IDX = mySheet.getCell(j++, i).getContents();
				subInfo.TTM_LOAD = mySheet.getCell(j++, i).getContents();
				subInfo.CONT_NUM = mySheet.getCell(j++, i).getContents();
				subInfo.SN = mySheet.getCell(j++, i).getContents();
				subInfo.POWER_NO_C1 = mySheet.getCell(j++, i).getContents();
				subInfo.POWER_NO_C2 = mySheet.getCell(j++, i).getContents();
				subInfo.POWER_NO_C3 = mySheet.getCell(j++, i).getContents();
				inputJSSubInfoDao.Append(subInfo);
			}
	        
	        mySheet = myWorkbook.getSheet(5);// 파일을 열고 시트를 지정해준다.
			col = mySheet.getColumns(); // 열을 읽어온다
	        row = mySheet.getRows(); // 행을 읽어 온다
	        
	        //불량애자
	        InputBRSubInfoDao inputBRSubInfoDao = InputBRSubInfoDao.getInstance(this);
	        inputBRSubInfoDao.DeleteAll();
	        
	        for( int i = 1; i < row; i++ ) { // 열
				BRSubInfo subInfo = new BRSubInfo();
				int j=0;
				subInfo.EQP_NO = mySheet.getCell(j++, i).getContents();
				mySheet.getCell(j++, i).getContents();
				subInfo.TOWER_IDX = mySheet.getCell(j++, i).getContents();
				subInfo.INSBTY_LFT = mySheet.getCell(j++, i).getContents();
				subInfo.INSBTY_RIT = mySheet.getCell(j++, i).getContents();
				subInfo.CL_NO = mySheet.getCell(j++, i).getContents();
				subInfo.CL_NM = mySheet.getCell(j++, i).getContents();
				subInfo.TY_SECD = mySheet.getCell(j++, i).getContents();
				subInfo.TY_SECD_NM = mySheet.getCell(j++, i).getContents();
				subInfo.PHS_SECD = mySheet.getCell(j++, i).getContents();
				subInfo.INSR_EQP_NO = mySheet.getCell(j++, i).getContents();
				mySheet.getCell(j++, i).getContents();
				subInfo.INS_CNT = mySheet.getCell(j++, i).getContents();
				mySheet.getCell(j++, i).getContents();
				mySheet.getCell(j++, i).getContents();
				subInfo.PHS_SECD_NM = mySheet.getCell(j++, i).getContents();
				inputBRSubInfoDao.Append(subInfo);
			}
	        
	        mySheet = myWorkbook.getSheet(6);// 파일을 열고 시트를 지정해준다.
			col = mySheet.getColumns(); // 열을 읽어온다
	        row = mySheet.getRows(); // 행을 읽어 온다
	        
	        //항공장애
	        InputHKSubInfoDao inputHKSubInfoDao = InputHKSubInfoDao.getInstance(this);
	        inputHKSubInfoDao.DeleteAll();
	        
	        for( int i = 1; i < row; i++ ) { // 열
				HKSubInfo subInfo = new HKSubInfo();
				int j=0;
				subInfo.EQP_NO = mySheet.getCell(j++, i).getContents();
				subInfo.TOWER_IDX = mySheet.getCell(j++, i).getContents();
				subInfo.FLIGHT_LMP_KND = mySheet.getCell(j++, i).getContents();
				subInfo.FLIGHT_LMP_KND_NM = mySheet.getCell(j++, i).getContents();
				subInfo.SRCELCT_KND = mySheet.getCell(j++, i).getContents();
				subInfo.SRCELCT_KND_NM = mySheet.getCell(j++, i).getContents();
				subInfo.FLIGHT_LMP_NO = mySheet.getCell(j++, i).getContents();
				inputHKSubInfoDao.Append(subInfo);
			}
		} catch (BiffException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		
	}
}
