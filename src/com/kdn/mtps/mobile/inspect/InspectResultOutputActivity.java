package com.kdn.mtps.mobile.inspect;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import jxl.Workbook;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.kdn.mtps.mobile.BaseActivity;
import com.kdn.mtps.mobile.MainActivity;
import com.kdn.mtps.mobile.R;
import com.kdn.mtps.mobile.TitleManager;
import com.kdn.mtps.mobile.aria.Cipher;
import com.kdn.mtps.mobile.constant.ConstSP;
import com.kdn.mtps.mobile.constant.ConstVALUE;
import com.kdn.mtps.mobile.datasync.DataSync;
import com.kdn.mtps.mobile.datasync.DataSync.OnSyncCompletedListener;
import com.kdn.mtps.mobile.db.InspectResultMasterDao;
import com.kdn.mtps.mobile.info.InspectOutput;
import com.kdn.mtps.mobile.info.UserInfo;
import com.kdn.mtps.mobile.input.BRInfo;
import com.kdn.mtps.mobile.input.BTInfo;
import com.kdn.mtps.mobile.input.HJInfo;
import com.kdn.mtps.mobile.input.HKInfo;
import com.kdn.mtps.mobile.input.JJInfo;
import com.kdn.mtps.mobile.input.JSInfo;
import com.kdn.mtps.mobile.input.KBInfo;
import com.kdn.mtps.mobile.util.AnsemUtil;
import com.kdn.mtps.mobile.util.AppUtil;
import com.kdn.mtps.mobile.util.Shared;
import com.kdn.mtps.mobile.util.ToastUtil;

public class InspectResultOutputActivity extends BaseActivity implements TitleManager, OnSyncCompletedListener{

	Button btnOutput;
	Button btnOutputSend;
	ListView listInspect;
	TextView tvNoData;
	InspectResultOutputBaseAdapter adapter;
	ArrayList<InspectInfo> inspectList;
	ArrayList<InspectOutput> outputList;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_inspect_result_output);
		
		setTitle();
		
		setInit();
		
	}

	@Override
	public void setTitle() {
		Button btnHeaderTitle = (Button)findViewById(R.id.btnHeaderTitle);
		Button btnHeaderLeft = (Button)findViewById(R.id.btnHeaderLeft);
		Button btnHeaderRight = (Button)findViewById(R.id.btnHeaderRight);
		
		btnHeaderTitle.setText("순시결과 추출목록");
		
		btnHeaderLeft.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		
		btnHeaderRight.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				AppUtil.startActivity(InspectResultOutputActivity.this, new Intent(InspectResultOutputActivity.this, MainActivity.class));
			}
		});
	}
	
	public void setInit() {
		Button btnOutput = (Button)findViewById(R.id.btnOutput);
		btnOutput.setOnClickListener(new  OnClickListener() {
			@Override
			public void onClick(View v) {
				writeExcel();
			}
		});

		Button btnOutputSend = (Button)findViewById(R.id.btnOutputSend);
		btnOutputSend.setOnClickListener(new  OnClickListener() {
			@Override
			public void onClick(View v) {
				sendInspection();
			}
		});
		
		listInspect = (ListView)findViewById(R.id.listInspect);
		adapter = new InspectResultOutputBaseAdapter(this);
		
		tvNoData = (TextView)findViewById(R.id.tvNoData);
		
		listInspect.setAdapter(adapter);
	}
	
	public void setData() {
		adapter.setListClear();
		
		InspectResultMasterDao insRetDao = InspectResultMasterDao.getInstance(this);
		inspectList = insRetDao.selecInsCompleteList();
		adapter.setList(inspectList);
		
		if (adapter.getCount() <= 0 )
			tvNoData.setVisibility(View.VISIBLE);
		else
			tvNoData.setVisibility(View.GONE);
	}
	
	public void writeExcel() {
		
		InspectResultMasterDao insRetDao = InspectResultMasterDao.getInstance(this);
		outputList = insRetDao.selectAllOutputList(this);
		
		if (outputList.isEmpty()) {
			ToastUtil.show(this, "추출할 데이터가 없습니다.");
			return;
		}
		
		WritableWorkbook workbook;
		try {
			String tgtDir = ConstVALUE.PATH_SDCARD_KDN_ROOT;
			File dir = new File(tgtDir);
			if (!dir.exists())
				dir.mkdirs();
			
			String strDate = new SimpleDateFormat("yyyyMMddHHmm").format(System.currentTimeMillis());
			File outputFile = new File(dir, "output_data_"+strDate+".xls");
			workbook = Workbook.createWorkbook(outputFile);
		
			String strBT = ConstVALUE.CODE_NO_INSPECT_BT;
			String strJS = ConstVALUE.CODE_NO_INSPECT_JS;
			String strJJ = ConstVALUE.CODE_NO_INSPECT_JJ;
			String strHK = ConstVALUE.CODE_NO_INSPECT_HK;
			String strKB = ConstVALUE.CODE_NO_INSPECT_KB;
			String strHJ = ConstVALUE.CODE_NO_INSPECT_HJ;
			String strBR = ConstVALUE.CODE_NO_INSPECT_BR;
			String strJP = ConstVALUE.CODE_NO_INSPECT_JP;
			
			//워크북 생성
	        WritableSheet s1 = workbook.createSheet(strBT,  0); //시트생성
	        WritableSheet s2 = workbook.createSheet(strJS,  1);
	        WritableSheet s3 = workbook.createSheet(strJJ,  2);
	        WritableSheet s4 = workbook.createSheet(strHK,  3);
	        WritableSheet s5 = workbook.createSheet(strKB,  4);
	        WritableSheet s6 = workbook.createSheet(strHJ,  5);
	        WritableSheet s7 = workbook.createSheet(strBR,  6);
	        WritableSheet s8 = workbook.createSheet(strJP,  7);
	        
	        saveTitle(s1, s2, s3, s4, s5, s6, s7, s8);
	        
	        int btIdx=0, jsIdx=0, jjIdx=0, hkIdx=0, kbIdx=0, hjIdx=0, brIdx=0, jpIdx=0;
	        
	        for(int i=0; i<outputList.size(); i++) {
	        	// 보통순시 데이터
	        	BTInfo btInfo = outputList.get(i).btInfo;
	        	if (btInfo != null) {
	        		// 스케줄 데이터
	        		int j = 0;
	        		btIdx++;
		        	InspectInfo inspectInfo = outputList.get(i).inspectInfo;
		        	
		        	try {
			        	byte[] masterKey= Cipher.createMasterKey();
			        	j = saveScheduleInfo(s1, j, btIdx, inspectInfo, masterKey);
			        	s1.addCell(new Label(j++, btIdx, Cipher.encrypt(btInfo.etc, masterKey)));
			        	
			        	s1.addCell(new Label(j++, btIdx, Cipher.encrypt(btInfo.weather, masterKey)));
			        	s1.addCell(new Label(j++, btIdx, Cipher.encrypt(btInfo.claim_content, masterKey)));//순시결과구분코드
			        	s1.addCell(new Label(j++, btIdx, Cipher.encrypt(btInfo.check_result, masterKey)));//조사내용
			        	s1.addCell(new Label(j++, btIdx, ""));
			        	s1.addCell(new Label(j++, btIdx, ""));
			        	s1.addCell(new Label(j++, btIdx, ""));
			        	s1.addCell(new Label(j++, btIdx, ""));
			        	s1.addCell(new Label(j++, btIdx, Cipher.getEnMasterKey()));
		        	} catch (Exception e) {
		        		e.printStackTrace();
		        	}
		        	continue;
	        	}
	        	
	        	ArrayList<JSInfo> jsInfoList = outputList.get(i).jsInfoList;
	        	if (jsInfoList != null && !jsInfoList.isEmpty()) {
	        		InspectInfo inspectInfo = outputList.get(i).inspectInfo;
	        		
	        		try {
			        	byte[] masterKey= Cipher.createMasterKey();
			        	
			        	for (int idx=0; idx<jsInfoList.size(); idx++) {
			        		jsIdx++;
			        		int j = 0;
			        		
			        		j = saveScheduleInfo(s2, j, jsIdx, inspectInfo, masterKey);
			        		s2.addCell(new Label(j++, jsIdx, ""));
		        		
		        			JSInfo jsInfo = jsInfoList.get(idx);
		        			
	        				s2.addCell(new Label(j++, jsIdx, Cipher.encrypt(jsInfo.weather, masterKey)));
		        			s2.addCell(new Label(j++, jsIdx, Cipher.encrypt(jsInfo.circuit_no, masterKey)));
				        	s2.addCell(new Label(j++, jsIdx, Cipher.encrypt(jsInfo.current_load, masterKey)));
				        	s2.addCell(new Label(j++, jsIdx, Cipher.encrypt(jsInfo.conductor_cnt, masterKey)));
				        	s2.addCell(new Label(j++, jsIdx, Cipher.encrypt(jsInfo.location, masterKey)));
				        	s2.addCell(new Label(j++, jsIdx, Cipher.encrypt(jsInfo.c1_power_no, masterKey)));
				        	s2.addCell(new Label(j++, jsIdx, Cipher.encrypt(jsInfo.c1_js, masterKey)));
				        	s2.addCell(new Label(j++, jsIdx, Cipher.encrypt(jsInfo.c1_jsj, masterKey)));
				        	s2.addCell(new Label(j++, jsIdx, Cipher.encrypt(jsInfo.c1_yb_result, masterKey)));
				        	s2.addCell(new Label(j++, jsIdx, Cipher.encrypt(jsInfo.c2_power_no, masterKey)));
				        	s2.addCell(new Label(j++, jsIdx, Cipher.encrypt(jsInfo.c2_js, masterKey)));
				        	s2.addCell(new Label(j++, jsIdx, Cipher.encrypt(jsInfo.c2_jsj, masterKey)));
				        	s2.addCell(new Label(j++, jsIdx, Cipher.encrypt(jsInfo.c2_yb_result, masterKey)));
				        	s2.addCell(new Label(j++, jsIdx, Cipher.encrypt(jsInfo.c3_power_no, masterKey)));
				        	s2.addCell(new Label(j++, jsIdx, Cipher.encrypt(jsInfo.c3_js, masterKey)));
				        	s2.addCell(new Label(j++, jsIdx, Cipher.encrypt(jsInfo.c3_jsj, masterKey)));
				        	s2.addCell(new Label(j++, jsIdx, Cipher.encrypt(jsInfo.c3_yb_result, masterKey)));
				        	s2.addCell(new Label(j++, jsIdx, Cipher.getEnMasterKey()));
		        		}
	        		} catch (Exception e) {
		        		e.printStackTrace();
		        	}
		        	continue;
		        }
	        	
	        	
	        	JJInfo jjInfo = outputList.get(i).jjInfo;
	        	if (jjInfo != null) {
	        		jjIdx++;
	        		int j = 0;
	        		InspectInfo inspectInfo = outputList.get(i).inspectInfo;
	        		
	        		try {
			        	byte[] masterKey= Cipher.createMasterKey();
		        		j = saveScheduleInfo(s3, j, jjIdx, inspectInfo, masterKey);
			        	
		        		s3.addCell(new Label(j++, jjIdx, ""));
			        	s3.addCell(new Label(j++, jjIdx, Cipher.encrypt(jjInfo.weather, masterKey)));
			        	s3.addCell(new Label(j++, jjIdx, Cipher.encrypt(jjInfo.check_result, masterKey)));
			        	s3.addCell(new Label(j++, jjIdx, Cipher.encrypt(jjInfo.remarks, masterKey)));
			        	s3.addCell(new Label(j++, jjIdx, Cipher.encrypt(jjInfo.count_1, masterKey)));
			        	s3.addCell(new Label(j++, jjIdx, Cipher.encrypt(jjInfo.terminal1_1, masterKey)));
			        	s3.addCell(new Label(j++, jjIdx, Cipher.encrypt(jjInfo.terminal1_2, masterKey)));
			        	s3.addCell(new Label(j++, jjIdx, Cipher.encrypt(jjInfo.terminal1_3, masterKey)));
			        	s3.addCell(new Label(j++, jjIdx, Cipher.encrypt(jjInfo.terminal1_5, masterKey)));
			        	s3.addCell(new Label(j++, jjIdx, Cipher.encrypt(jjInfo.terminal1_10, masterKey)));
			        	s3.addCell(new Label(j++, jjIdx, Cipher.getEnMasterKey()));
	        		} catch (Exception e) {
		        		e.printStackTrace();
		        	}
		        	continue;
	        	}
	        	
	        	// 항공등 점등점검 데이터
	        	ArrayList<HKInfo> hkInfoList = outputList.get(i).hkInfoList;
	        	if (hkInfoList != null && !hkInfoList.isEmpty()) {
	        		InspectInfo inspectInfo = outputList.get(i).inspectInfo;
	        		
	        		try {
			        	byte[] masterKey= Cipher.createMasterKey();
			        	
			        	for (int idx=0; idx<hkInfoList.size(); idx++) {
			        		hkIdx++;
			        		int j = 0;
			        		j = saveScheduleInfo(s4, j, hkIdx, inspectInfo, masterKey);
			        		s4.addCell(new Label(j++, hkIdx, ""));
		        		
		        			HKInfo hkInfo = hkInfoList.get(idx);
		        			
	        				s4.addCell(new Label(j++, hkIdx, Cipher.encrypt(hkInfo.weather, masterKey)));
				        	s4.addCell(new Label(j++, hkIdx, Cipher.encrypt(hkInfo.lightType, masterKey)));
				        	s4.addCell(new Label(j++, hkIdx, Cipher.encrypt(hkInfo.light_no, masterKey)));
				        	s4.addCell(new Label(j++, hkIdx, Cipher.encrypt(hkInfo.power, masterKey)));
				        	s4.addCell(new Label(j++, hkIdx, Cipher.encrypt(hkInfo.light_cnt, masterKey)));
				        	s4.addCell(new Label(j++, hkIdx, Cipher.encrypt(hkInfo.light_state, masterKey)));
				        	s4.addCell(new Label(j++, hkIdx, Cipher.encrypt(hkInfo.yb_result, masterKey)));
				        	s4.addCell(new Label(j++, hkIdx, Cipher.getEnMasterKey()));
				        	
		        		}
	        		} catch (Exception e) {
		        		e.printStackTrace();
		        	}
	        		continue;
	        	}
	        	
	        	// 기별
	        	KBInfo kbInfo = outputList.get(i).kbInfo;
	        	if (kbInfo != null) {
	        		kbIdx++;
	        		int j = 0;
	        		InspectInfo inspectInfo = outputList.get(i).inspectInfo;
	        		
	        		try {
			        	byte[] masterKey= Cipher.createMasterKey();
		        		j = saveScheduleInfo(s5, j, kbIdx, inspectInfo, masterKey);
			        	
		        		s5.addCell(new Label(j++, kbIdx, "null"));
			        	s5.addCell(new Label(j++, kbIdx, Cipher.encrypt(kbInfo.weather, masterKey)));
			        	s5.addCell(new Label(j++, kbIdx, Cipher.encrypt(kbInfo.item_fac_1, masterKey)));
			        	s5.addCell(new Label(j++, kbIdx, Cipher.encrypt(kbInfo.item_fac_2, masterKey)));
			        	s5.addCell(new Label(j++, kbIdx, Cipher.encrypt(kbInfo.item_fac_3, masterKey)));
			        	s5.addCell(new Label(j++, kbIdx, Cipher.encrypt(kbInfo.item_fac_4, masterKey)));
			        	s5.addCell(new Label(j++, kbIdx, Cipher.encrypt(kbInfo.item_fac_5, masterKey)));
			        	s5.addCell(new Label(j++, kbIdx, Cipher.encrypt(kbInfo.item_fac_6, masterKey)));
			        	s5.addCell(new Label(j++, kbIdx, Cipher.encrypt(kbInfo.item_fac_7, masterKey)));
			        	s5.addCell(new Label(j++, kbIdx, Cipher.encrypt(kbInfo.item_fac_8, masterKey)));
			        	s5.addCell(new Label(j++, kbIdx, Cipher.encrypt(kbInfo.item_fac_9, masterKey)));
			        	s5.addCell(new Label(j++, kbIdx, Cipher.encrypt(kbInfo.item_fac_10, masterKey)));
			        	s5.addCell(new Label(j++, kbIdx, Cipher.encrypt(kbInfo.item_fac_11, masterKey)));
			        	s5.addCell(new Label(j++, kbIdx, Cipher.encrypt(kbInfo.item_fac_12, masterKey)));
			        	s5.addCell(new Label(j++, kbIdx, Cipher.encrypt(kbInfo.item_sett_1, masterKey)));
			        	s5.addCell(new Label(j++, kbIdx, Cipher.encrypt(kbInfo.item_sett_2, masterKey)));
			        	s5.addCell(new Label(j++, kbIdx, Cipher.encrypt(kbInfo.item_sett_3, masterKey)));
			        	s5.addCell(new Label(j++, kbIdx, Cipher.encrypt(kbInfo.item_sett_4, masterKey)));
			        	s5.addCell(new Label(j++, kbIdx, Cipher.encrypt(kbInfo.item_etc_1, masterKey)));
			        	s5.addCell(new Label(j++, kbIdx, Cipher.encrypt(kbInfo.item_etc_2, masterKey)));
			        	s5.addCell(new Label(j++, kbIdx, Cipher.getEnMasterKey()));
	        		} catch (Exception e) {
		        		e.printStackTrace();
		        	}
		        	continue;
	        	}
	        	
	        	// 항공장애
	        	ArrayList<HJInfo> hjInfoList = outputList.get(i).hjInfoList;
	        	if (hjInfoList != null && !hjInfoList.isEmpty()) {
	        		
	        		InspectInfo inspectInfo = outputList.get(i).inspectInfo;
	        		
	        		try {
			        	byte[] masterKey= Cipher.createMasterKey();
			        	for (int idx=0; idx<hjInfoList.size(); idx++) {
			        		hjIdx++;
			        		int j = 0;
			        		j = saveScheduleInfo(s6, j, hjIdx, inspectInfo, masterKey);
			        		s6.addCell(new Label(j++, hjIdx, "null"));
		        		
		        			HJInfo hjInfo = hjInfoList.get(idx);
		        			
		        			s6.addCell(new Label(j++, hjIdx, Cipher.encrypt(hjInfo.weather, masterKey)));
				        	
				        	s6.addCell(new Label(j++, hjIdx, Cipher.encrypt(hjInfo.light_type, masterKey)));
				        	s6.addCell(new Label(j++, hjIdx, Cipher.encrypt(hjInfo.light_no, masterKey)));
				        	s6.addCell(new Label(j++, hjIdx, Cipher.encrypt(hjInfo.power, masterKey)));
				        	s6.addCell(new Label(j++, hjIdx, Cipher.encrypt(hjInfo.control, masterKey)));
				        	s6.addCell(new Label(j++, hjIdx, Cipher.encrypt(hjInfo.sun_battery, masterKey)));
				        	s6.addCell(new Label(j++, hjIdx, Cipher.encrypt(hjInfo.storage_battery, masterKey)));
				        	s6.addCell(new Label(j++, hjIdx, Cipher.encrypt(hjInfo.light_item, masterKey)));
				        	s6.addCell(new Label(j++, hjIdx, Cipher.encrypt(hjInfo.cable, masterKey)));
				        	s6.addCell(new Label(j++, hjIdx, Cipher.encrypt(hjInfo.yb_result, masterKey)));
				        	s6.addCell(new Label(j++, hjIdx, Cipher.getEnMasterKey()));
				        	
		        		}
	        		} catch (Exception e) {
		        		e.printStackTrace();
		        	}
	        		continue;
	        	}
	        	
	        	// 불량애자
	        	ArrayList<BRInfo> brInfoList = outputList.get(i).brInfoList;
	        	if (brInfoList != null && !brInfoList.isEmpty()) {
	        		InspectInfo inspectInfo = outputList.get(i).inspectInfo;
	        		
	        		try {
			        	byte[] masterKey= Cipher.createMasterKey();
			        	for (int idx=0; idx<brInfoList.size(); idx++) {
			        		brIdx++;
			        		int j = 0;
			        		
				        	j = saveScheduleInfo(s7, j, brIdx, inspectInfo, masterKey);
			        		s7.addCell(new Label(j++, brIdx, "null"));
		        		
		        			BRInfo brInfo = brInfoList.get(idx);
		        			
	        				s7.addCell(new Label(j++, brIdx, Cipher.encrypt(brInfo.weather, masterKey)));
		        			s7.addCell(new Label(j++, brIdx, Cipher.encrypt(brInfo.circuit_no, masterKey)));
		        			s7.addCell(new Label(j++, brIdx, Cipher.encrypt(brInfo.insr_eqp_no, masterKey)));
		        			s7.addCell(new Label(j++, brIdx, Cipher.encrypt(brInfo.insbty_lft, masterKey)));
		        			s7.addCell(new Label(j++, brIdx, Cipher.encrypt(brInfo.insbty_rit, masterKey)));
		        			s7.addCell(new Label(j++, brIdx, Cipher.encrypt(brInfo.ty_secd, masterKey)));
		        			s7.addCell(new Label(j++, brIdx, Cipher.encrypt(brInfo.phs_secd, masterKey)));
		        			s7.addCell(new Label(j++, brIdx, Cipher.encrypt(brInfo.ej_cnt, masterKey)));
		        			s7.addCell(new Label(j++, brIdx, Cipher.encrypt(brInfo.br_cnt, masterKey)));
		        			s7.addCell(new Label(j++, brIdx, Cipher.encrypt(brInfo.make_date, masterKey)));
		        			s7.addCell(new Label(j++, brIdx, Cipher.encrypt(brInfo.make_company, masterKey)));
		        			s7.addCell(new Label(j++, brIdx, Cipher.encrypt(brInfo.yb_result, masterKey)));
		        			s7.addCell(new Label(j++, brIdx, Cipher.getEnMasterKey()));
		        		}
	        		} catch (Exception e) {
		        		e.printStackTrace();
		        	}
	        		continue;
	        	}
	        	
	        	ArrayList<JSInfo> jpInfoList = outputList.get(i).jpInfoList;
	        	if (jpInfoList != null && !jpInfoList.isEmpty()) {
	        		InspectInfo inspectInfo = outputList.get(i).inspectInfo;
	        		
	        		try {
			        	byte[] masterKey= Cipher.createMasterKey();
			        	for (int idx=0; idx<jpInfoList.size(); idx++) {
			        		jpIdx++;
			        		int j = 0;
			        		j = saveScheduleInfo(s8, j, jpIdx, inspectInfo, masterKey);
			        		s8.addCell(new Label(j++, jpIdx, "null"));
		        		
		        			JSInfo jpInfo = jpInfoList.get(idx);
		        			
	        				s8.addCell(new Label(j++, jpIdx, Cipher.encrypt(jpInfo.weather, masterKey)));
		        			s8.addCell(new Label(j++, jpIdx, Cipher.encrypt(jpInfo.circuit_no, masterKey)));
				        	s8.addCell(new Label(j++, jpIdx, Cipher.encrypt(jpInfo.current_load, masterKey)));
				        	s8.addCell(new Label(j++, jpIdx, Cipher.encrypt(jpInfo.conductor_cnt, masterKey)));
				        	s8.addCell(new Label(j++, jpIdx, Cipher.encrypt(jpInfo.location, masterKey)));
				        	s8.addCell(new Label(j++, jpIdx, Cipher.encrypt(jpInfo.c1_power_no, masterKey)));
				        	s8.addCell(new Label(j++, jpIdx, Cipher.encrypt(jpInfo.c1_js, masterKey)));
				        	s8.addCell(new Label(j++, jpIdx, Cipher.encrypt(jpInfo.c1_jsj, masterKey)));
				        	s8.addCell(new Label(j++, jpIdx, Cipher.encrypt(jpInfo.c1_yb_result, masterKey)));
				        	s8.addCell(new Label(j++, jpIdx, Cipher.encrypt(jpInfo.c2_power_no, masterKey)));
				        	s8.addCell(new Label(j++, jpIdx, Cipher.encrypt(jpInfo.c2_js, masterKey)));
				        	s8.addCell(new Label(j++, jpIdx, Cipher.encrypt(jpInfo.c2_jsj, masterKey)));
				        	s8.addCell(new Label(j++, jpIdx, Cipher.encrypt(jpInfo.c2_yb_result, masterKey)));
				        	s8.addCell(new Label(j++, jpIdx, Cipher.encrypt(jpInfo.c3_power_no, masterKey)));
				        	s8.addCell(new Label(j++, jpIdx, Cipher.encrypt(jpInfo.c3_js, masterKey)));
				        	s8.addCell(new Label(j++, jpIdx, Cipher.encrypt(jpInfo.c3_jsj, masterKey)));
				        	s8.addCell(new Label(j++, jpIdx, Cipher.encrypt(jpInfo.c3_yb_result, masterKey)));
				        	s8.addCell(new Label(j++, jpIdx, Cipher.getEnMasterKey()));
		        		}
	        		} catch (Exception e) {
		        		e.printStackTrace();
		        	}
	        		continue;
	        	}
	        }
	        
//	        for (int i=0;i<100;i++){
//	            //jxl.write.Label.Label(int c, int r, String cont) : //열, 행, 내용 
//	            Label label = new Label(0,i, "데이터..."+i); //라벨객체 생성. 
//	            s1.addCell(label); //셀에 라벨 추가 
//	        }
	        
	        workbook.write(); //파일로 쓰기
	        workbook.close(); //닫기
	        
	        Uri contentUri = Uri.fromFile(outputFile);
			Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
			mediaScanIntent.setData(contentUri);
			sendBroadcast(mediaScanIntent);
			
//	        MediaScannerConnection.scanFile(this, new String[]{tgtDir + "/" + "output_data.xls"}, null,
//					new MediaScannerConnection.OnScanCompletedListener() {
//						public void onScanCompleted(String path, Uri uri) {
//						}
//					});
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();

		} catch (RowsExceededException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (WriteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		ToastUtil.show(this, "추출이 완료되었습니다.");
		setData();
	}

	@Override
	protected void onResume() {
		super.onResume();
		
		setData();
	}
	
	public void saveTitle(WritableSheet s1, WritableSheet s2, WritableSheet s3, WritableSheet s4, WritableSheet s5, WritableSheet s6, WritableSheet s7, WritableSheet s8) {
		
		try {
			
			// 스케줄 데이터 제목
//			String strInspect[] = {"선로명","지지물번호","날짜","순시종류","태그 위도","태그 경도"};
	    	String strInspect[] = {"UNITY_INS_NO","INS_PLAN_NO","FNCT_EQP_NO","INS_SN","CYCLE_YM","FST_BIZPLC_CD","SCD_BIZPLC_CD","THD_BIZPLC_CD","INS_TY_CD","SIMPLCTY_INST_AT","INS_RESN","INS_DT","STRT_DTM","ARVL_DTM","STRCFM_MAN","ARVLCFM_MAN","INSCTR_1","INSCTR_2","INSCTR_USER_IDS_1","INSCTR_USER_IDS_2","INS_CCPY_NM","DETECT_MTHCD","BEGIN_SPOT","END_SPOT","ATEMP","HUMID","GNRLZ_JDGMNT_GDBD","ATTFL_NO","APRV_NO","IMPRMN_REQUST_NO","CRT_DTM","CRT_USER","UPD_DTM","UPD_USER","UNITY_BIZPLC_CD","GUBUN","TID","JUNC_CODE","OBJECTID","MANGR_ID","TAG","TAG_YN","LATITUDE","LONGITUDE","EQP_NO"};
	        for(int i=0; i<strInspect.length; i++) {
	        	Label label = new Label(i,0, strInspect[i]);
				s1.addCell(label);//셀에 라벨 추가 
	        }
	        for(int i=0; i<strInspect.length; i++) {
	        	Label label = new Label(i,0, strInspect[i]);
	        	s2.addCell(label); //셀에 라벨 추가 
	        }
	        for(int i=0; i<strInspect.length; i++) {
	        	Label label = new Label(i,0, strInspect[i]);
	        	s3.addCell(label); //셀에 라벨 추가 
	        }
	        for(int i=0; i<strInspect.length; i++) {
	        	Label label = new Label(i,0, strInspect[i]);
	        	s4.addCell(label); //셀에 라벨 추가 
	        }
	        for(int i=0; i<strInspect.length; i++) {
	        	Label label = new Label(i,0, strInspect[i]);
	        	s5.addCell(label); //셀에 라벨 추가 
	        }
	        for(int i=0; i<strInspect.length; i++) {
	        	Label label = new Label(i,0, strInspect[i]);
	        	s6.addCell(label); //셀에 라벨 추가 
	        }
	        for(int i=0; i<strInspect.length; i++) {
	        	Label label = new Label(i,0, strInspect[i]);
	        	s7.addCell(label); //셀에 라벨 추가 
	        }
	        for(int i=0; i<strInspect.length; i++) {
	        	Label label = new Label(i,0, strInspect[i]);
	        	s8.addCell(label); //셀에 라벨 추가 
	        }
	        
	        String strBtItems[] = {"GNRLZ_OPNIN","WEATH","TINS_RESULT_SECD","EXAMIN_CN","ATTFL_NO","IMPRMN_REQUST_NO","GUBUN","TID","MASTER_KEY"};
	        for(int i=0; i<strBtItems.length; i++) {
	        	Label label = new Label(strInspect.length+i,0, strBtItems[i]);
	        	s1.addCell(label); //셀에 라벨 추가 
	        }
	        
	        String strJSItems[] = {"GNRLZ_OPNIN","WEATH","CL_NO","TTM_LOAD","CNDCTR_CO","CNDCTR_SN","PWLN_EQP_NO_1","CABL_TP_1","CNPT_TP_1","GOOD_SECD_1","PWLN_EQP_NO_2","CABL_TP_2","CNPT_TP_2","GOOD_SECD_2","PWLN_EQP_NO_3","CABL_TP_3","CNPT_TP_3","GOOD_SECD_3","MASTER_KEY"};
	        for(int i=0; i<strJSItems.length; i++) {
	        	Label label = new Label(strInspect.length+i,0, strJSItems[i]);
	        	s2.addCell(label); //셀에 라벨 추가 
	        }
	        
	        String strJJItems[] = {"GNRLZ_OPNIN","WEATH","GOOD_SECD","PMTTR","MSR_CO","MSR_RS_1","MSR_RS_2","MSR_RS_3","MSR_RS_5","MSR_RS_10","MASTER_KEY"};
	        for(int i=0; i<strJJItems.length; i++) {
	        	Label label = new Label(strInspect.length+i,0, strJJItems[i]);
	        	s3.addCell(label); //셀에 라벨 추가 
	        }
	        
	        String strHKItems[] = {"GNRLZ_OPNIN","WEATH","FLIGHT_LMP_KND","FLIGHT_LMP_NO","SRCELCT_KND","FLCK_CO","LIGHTG_STADIV_CD","GOOD_SECD","MASTER_KEY"};
	        for(int i=0; i<strHKItems.length; i++) {
	        	Label label = new Label(strInspect.length+i,0, strHKItems[i]);
	        	s4.addCell(label); //셀에 라벨 추가 
	        }
        
	        String strKBItems[] = {"GNRLZ_OPNIN","WEATH","ITEM_00001","ITEM_00002","ITEM_00003","ITEM_00004","ITEM_00005","ITEM_00006","ITEM_00007","ITEM_00008","ITEM_00009","ITEM_00010","ITEM_00011","ITEM_00012","ITEM_00013","ITEM_00014","ITEM_00015","ITEM_00016","ITEM_00017","ITEM_00018","MASTER_KEY"};
	        for(int i=0; i<strKBItems.length; i++) {
	        	Label label = new Label(strInspect.length+i,0, strKBItems[i]);
	        	s5.addCell(label); //셀에 라벨 추가 
	        }
	        
	        
	        String strHJItems[] = {"GNRLZ_OPNIN","WEATH","FLIGHT_LMP_KND","FLIGHT_LMP_NO","SRCELCT_KND","CTRL_BAN_GDBD_SECD","SLRCL_GDBD_SECD","SRGBTRY_GDBD_SECD","RGIST_GU_GDBD_SECD","CABL_GDBD_SECD","GOOD_SECD","MASTER_KEY"};
//	        String strHJItems[] = {"날씨","항공등종류","전원","제어반_1","태양전지_1","축전지_1","등기구_1","전선_1","양부판정_1","제어반_2","태양전지_2","축전지_2","등기구_2","전선_2","양부판정_2","비고"};
	        for(int i=0; i<strHJItems.length; i++) {
	        	Label label = new Label(strInspect.length+i,0, strHJItems[i]);
	        	s6.addCell(label); //셀에 라벨 추가 
	        }
	        
	        String strBRItems[] = {"GNRLZ_OPNIN","WEATH","CL_NO","INSR_EQP_NO","INSBTY_LFT","INSBTY_RIT","TY_SECD","PHS_SECD","INS_CNT","BAD_INS_CNT","PROD_YMD","PROD_COMP","GOOD_SECD","MASTER_KEY"};
	        
	        for(int i=0; i<strBRItems.length; i++) {
	        	Label label = new Label(strInspect.length+i,0, strBRItems[i]);
	        	s7.addCell(label); //셀에 라벨 추가 
	        }
	        
	        for(int i=0; i<strJSItems.length; i++) {
	        	Label label = new Label(strInspect.length+i,0, strJSItems[i]);
	        	s8.addCell(label); //셀에 라벨 추가 
	        }
	        
		} catch (RowsExceededException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (WriteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public int saveScheduleInfo(WritableSheet ws, int j, int idx, InspectInfo inspectInfo, byte[] masterKey) throws RowsExceededException, WriteException, InvalidKeyException, UnsupportedEncodingException {
		String inspect_name_1 = Shared.getString(this, ConstSP.SETTING_INSPECTER_NAME_1);
    	String inspect_name_2 = Shared.getString(this, ConstSP.SETTING_INSPECTER_NAME_2);
    	String inspect_id_1 = Shared.getString(this, ConstSP.SETTING_INSPECTER_ID_1);
    	String inspect_id_2 = Shared.getString(this, ConstSP.SETTING_INSPECTER_ID_2);
    	
		ws.addCell(new Label(j++, idx, Cipher.encrypt(inspectInfo.unity_ins_no, masterKey)));
    	ws.addCell(new Label(j++, idx, Cipher.encrypt(inspectInfo.ins_plan_no, masterKey)));
    	ws.addCell(new Label(j++, idx, Cipher.encrypt(inspectInfo.fnct_lc_no, masterKey)));
    	ws.addCell(new Label(j++, idx, Cipher.encrypt(inspectInfo.ins_sn, masterKey)));
    	ws.addCell(new Label(j++, idx, Cipher.encrypt(inspectInfo.date, masterKey)));
    	ws.addCell(new Label(j++, idx, Cipher.encrypt(UserInfo.getSessionData(this).fst_bizplc_cd, masterKey)));
    	ws.addCell(new Label(j++, idx, Cipher.encrypt(UserInfo.getSessionData(this).scd_bizplc_cd, masterKey)));
    	ws.addCell(new Label(j++, idx, ""));
    	ws.addCell(new Label(j++, idx, Cipher.encrypt(inspectInfo.type, masterKey)));
    	ws.addCell(new Label(j++, idx, ""));
    	ws.addCell(new Label(j++, idx, Cipher.encrypt(inspectInfo.typeName, masterKey)));
    	ws.addCell(new Label(j++, idx, Cipher.encrypt(inspectInfo.ins_input_date, masterKey)));
    	ws.addCell(new Label(j++, idx, ""));
    	ws.addCell(new Label(j++, idx, ""));
    	ws.addCell(new Label(j++, idx, ""));
    	ws.addCell(new Label(j++, idx, ""));
    	ws.addCell(new Label(j++, idx, Cipher.encrypt(inspect_name_1, masterKey)));//점검자1
    	ws.addCell(new Label(j++, idx, Cipher.encrypt(inspect_name_2, masterKey)));//점검자2
    	ws.addCell(new Label(j++, idx, Cipher.encrypt(inspect_id_1, masterKey)));//점검자1id
    	ws.addCell(new Label(j++, idx, Cipher.encrypt(inspect_id_2, masterKey)));//점검자2id
    	ws.addCell(new Label(j++, idx, ""));//점검협력사명
    	ws.addCell(new Label(j++, idx, ""));
    	ws.addCell(new Label(j++, idx, ""));
    	ws.addCell(new Label(j++, idx, ""));
    	ws.addCell(new Label(j++, idx, ""));//온도
    	ws.addCell(new Label(j++, idx, ""));
    	ws.addCell(new Label(j++, idx, ""));
    	ws.addCell(new Label(j++, idx, ""));
    	ws.addCell(new Label(j++, idx, ""));
    	ws.addCell(new Label(j++, idx, ""));
    	ws.addCell(new Label(j++, idx, ""));
    	ws.addCell(new Label(j++, idx, Cipher.encrypt(UserInfo.getSessionData(this).user_id, masterKey)));
    	ws.addCell(new Label(j++, idx, ""));
    	ws.addCell(new Label(j++, idx, ""));
    	ws.addCell(new Label(j++, idx, Cipher.encrypt(UserInfo.getSessionData(this).fst_bizplc_cd+UserInfo.getSessionData(this).scd_bizplc_cd, masterKey)));
    	ws.addCell(new Label(j++, idx, ""));
    	ws.addCell(new Label(j++, idx, ""));
    	ws.addCell(new Label(j++, idx, ""));
    	ws.addCell(new Label(j++, idx, ""));
    	ws.addCell(new Label(j++, idx, ""));
    	ws.addCell(new Label(j++, idx, Cipher.encrypt(inspectInfo.nfc_tag_id, masterKey)));
    	ws.addCell(new Label(j++, idx, Cipher.encrypt(inspectInfo.nfc_tag_yn, masterKey)));
    	ws.addCell(new Label(j++, idx, Cipher.encrypt(inspectInfo.nfcTagLatitude, masterKey)));
    	ws.addCell(new Label(j++, idx, Cipher.encrypt(inspectInfo.nfcTagLongitude, masterKey)));
    	ws.addCell(new Label(j++, idx, Cipher.encrypt(inspectInfo.eqpNo, masterKey)));
    	
    	return j;
	}
	
	public void sendInspection() {
		InspectResultMasterDao insRetDao = InspectResultMasterDao.getInstance(this);
		ArrayList<InspectOutput> outputList = insRetDao.selectAllOutputList(this);
		
		if (outputList.isEmpty()) {
			ToastUtil.show(this, "추출할 데이터가 없습니다.");
			return;
		}
		
		if (!AnsemUtil.isConnected(this))
			AnsemUtil.showAnsemLogin(this);
		else {
			DataSync.getInstance(InspectResultOutputActivity.this).setOnSyncResult(InspectResultOutputActivity.this);
			DataSync.getInstance(this).startSendInspectResult(outputList);
		}
		
	}
	
	@Override
	public void onSyncCompleted(int callType, boolean isComplete) {
		
		if (isComplete)
			ToastUtil.show(this, "데이터 전송이 완료되었습니다.");
		else
			ToastUtil.show(this, "데이터 전송 중 오류가 발생하였습니다.");
		
		setData();
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		if (requestCode == AnsemUtil.REQUEST_ANSEM_CODE) {
			if (AnsemUtil.isConnected(this)) {
				DataSync.getInstance(InspectResultOutputActivity.this).setOnSyncResult(InspectResultOutputActivity.this);
				DataSync.getInstance(this).startSendInspectResult(outputList);
			}
		}
		
	}
}