package com.kdn.mtps.mobile.net.api;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.security.InvalidKeyException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.message.BasicNameValuePair;

import android.content.Context;

import com.google.gson.Gson;
import com.kdn.mtps.mobile.aria.Cipher;
import com.kdn.mtps.mobile.constant.ConstApiResult;
import com.kdn.mtps.mobile.constant.ConstSP;
import com.kdn.mtps.mobile.constant.ConstVALUE;
import com.kdn.mtps.mobile.db.CameraLogDao;
import com.kdn.mtps.mobile.db.bean.CameraLog;
import com.kdn.mtps.mobile.info.CodeInfo;
import com.kdn.mtps.mobile.info.UserInfo;
import com.kdn.mtps.mobile.input.BRInfo;
import com.kdn.mtps.mobile.input.BTInfo;
import com.kdn.mtps.mobile.input.HJInfo;
import com.kdn.mtps.mobile.input.HKInfo;
import com.kdn.mtps.mobile.input.JJInfo;
import com.kdn.mtps.mobile.input.JSInfo;
import com.kdn.mtps.mobile.input.KBInfo;
import com.kdn.mtps.mobile.input.YBInfo;
import com.kdn.mtps.mobile.inspect.InspectInfo;
import com.kdn.mtps.mobile.net.api.bean.BRSubInfoList;
import com.kdn.mtps.mobile.net.api.bean.BaseResult;
import com.kdn.mtps.mobile.net.api.bean.CodeList;
import com.kdn.mtps.mobile.net.api.bean.FacilityList;
import com.kdn.mtps.mobile.net.api.bean.HKSubInfoList;
import com.kdn.mtps.mobile.net.api.bean.JSSubInfoList;
import com.kdn.mtps.mobile.net.api.bean.LoginData;
import com.kdn.mtps.mobile.net.api.bean.NfcTagInfo;
import com.kdn.mtps.mobile.net.api.bean.PatrolmansList;
import com.kdn.mtps.mobile.net.api.bean.ScheduleList;
import com.kdn.mtps.mobile.net.api.bean.TowerList;
import com.kdn.mtps.mobile.net.api.bean.TracksList;
import com.kdn.mtps.mobile.net.api.bean.YBInfoList;
import com.kdn.mtps.mobile.net.http.HttpUtil;
import com.kdn.mtps.mobile.util.Logg;
import com.kdn.mtps.mobile.util.Shared;

public class ApiManager {
	
	public static final String RESULT_OK = "001";
	public static final String RESULT_FAIL = "999";
	
	public static final String API_PREFIX = ConstVALUE.PREFIX_API_SERVER + "api/";
//	public static final String API_PREFIX_HD = ConstVALUE.PREFIX_API_SERVER + "kdnweb/api/";
	public static final String API_PREFIX_GLOBAL = "";
	
	/** 로그인 */
	public static final String API_LOGIN= API_PREFIX + "login.json";
	
	/** 로그아웃 */
	public static final String API_LOGOUT= API_PREFIX + "logout.json";
	
	/** 코드 **/
	public static final String API_CODE_INFO_LIST= API_PREFIX + "apiCodeInfoList.json";
	
	/** 스케줄 **/
	public static final String API_SCHEDULE_LIST= API_PREFIX + "inspection/result/in/schedule.json";
	
	/** 송전설비 검색 **/
	public static final String API_FACILITY_LIST= API_PREFIX + "search/trans/tower.json";
			
	/** 선로 목록 **/
	public static final String API_TRACKS_LIST= API_PREFIX + "tracks/list.json";
	
	/** 지지물 목록 **/
	public static final String API_TOWER_LIST= API_PREFIX + "tracks/list.json";
	
	/** 앱 삭제 확인 ***/
	public static final String API_DEVICE_DEL_CONFIRM= API_PREFIX + "deviceDelConfirm.json";
	
	/** 순시자 트래킹 ***/
	public static final String API_TRACKING_SAVE= API_PREFIX + "traking/user/save.json";
	
	/** 태그 정보 가져오기 ***/
	public static final String API_TAG_INFO= API_PREFIX + "nfc/tag/info.json";
	
	/** 부순시자 목록 **/
	public static final String API_PATROLMANS_LIST= API_PREFIX + "sub/patrolmans.json";
	
	/** 전선접속개소 정보 **/
	public static final String API_INPUT_JS_SUB_LIST= API_PREFIX + "circuit/in/schedule.json";
	
	/** 불량애자 정보 **/
	public static final String API_INPUT_BR_SUB_LIST= API_PREFIX + "poor/insulators.json";
	
	/** 항공등 정보 **/
	public static final String API_INPUT_HK_SUB_LIST= API_PREFIX + "error/air.json";
	
	/** 순시결과 전송 **/
	public static final String API_INPUT_BT= API_PREFIX + "result/normal/inspection.json";
	public static final String API_INPUT_JJ = API_PREFIX + "ground/resistance/measurements.json";
	public static final String API_INPUT_KB = API_PREFIX + "message/and/thorough/inspection.json";
	public static final String API_INPUT_JS= API_PREFIX + "connection/point/wire/insert.json";
	public static final String API_INPUT_BR = API_PREFIX + "agile/poor/detection.json";
	public static final String API_INPUT_HJ = API_PREFIX + "check/air/failure.json";
	public static final String API_INPUT_HK = API_PREFIX + "check/air/failure/light.json";
	public static final String API_INPUT_JP = API_PREFIX + "connection/point/jumper/insert.json";
	
	/** 예방순시 목록 **/
	public static final String API_INPUT_YB_LIST = API_PREFIX + "preventing/inspection.json";
	
	/** 예방순시 저장 **/
	public static final String API_INSERT_YB = API_PREFIX + "preventing/inspection/insert.json";
	
	
	public static int login(Context ctx, String id, String pwd, String device_token) {
		try {
			List<NameValuePair> getParameters = new ArrayList<NameValuePair>();
			getParameters.add(new BasicNameValuePair("user_id", id));
			getParameters.add(new BasicNameValuePair("user_pwd", pwd));
			
			if (device_token == null || "".equals(device_token))
				device_token = "1234";
			
			getParameters.add(new BasicNameValuePair("device_token", device_token));
	
			Logg.d("device_token : " + device_token);
			String response = HttpUtil.connectPost(API_LOGIN, getParameters);
			Logg.d("getParameters : " + getParameters);
			Logg.d("response : " + response);
			if (response == null) {// 서버에서 받아오지 못했다면
				return ConstApiResult.LOGIN_RESULT_FAIL;
			} else {
				LoginData loginData = new Gson().fromJson(response, LoginData.class);
				
				if (loginData == null)
					return ConstApiResult.LOGIN_RESULT_FAIL; 
							
				if (RESULT_OK.equals(loginData.code)) {
					UserInfo.setLoginData(ctx, loginData);
					return ConstApiResult.LOGIN_RESULT_OK;
				} else if ("013".equals(loginData.code)) {
					return ConstApiResult.LOGIN_RESULT_FAIL_APPROVE;
				} else if ("005".equals(loginData.code)) {
					return ConstApiResult.LOGIN_RESULT_FAIL_PASSWORD;
				} else if ("004".equals(loginData.code)) {
					return ConstApiResult.LOGIN_RESULT_FAIL_ID;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ConstApiResult.LOGIN_RESULT_FAIL;
	}
	
	public static LoginData logout() {
		try {
			List<NameValuePair> getParameters = new ArrayList<NameValuePair>();
			
			String response = HttpUtil.connectPost(API_LOGOUT, getParameters);
			Logg.d(response);
			if (response == null) {// 서버에서 받아오지 못했다면
				return null;
			} else {
				LoginData loginData = new Gson().fromJson(response, LoginData.class);
				
				if (loginData == null)
					return null; 

				return loginData;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static CodeList codeInfoList() {
		try {
			List<NameValuePair> getParameters = new ArrayList<NameValuePair>();
	
			String response = HttpUtil.connectPost(API_CODE_INFO_LIST, getParameters);
			Logg.d(response);
			if (response == null) {// 서버에서 받아오지 못했다면
				return null;
			} else {
				CodeList codeList = new Gson().fromJson(response, CodeList.class);
				return codeList;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static ScheduleList scheduleList(boolean isFirst, String insPlanNo) {
		try {
			List<NameValuePair> getParameters = new ArrayList<NameValuePair>();
	
			getParameters.add(new BasicNameValuePair("first", isFirst+""));
			if (!isFirst)
				getParameters.add(new BasicNameValuePair("insplan_no", insPlanNo));
			
			String response = HttpUtil.connectPost(API_SCHEDULE_LIST, getParameters, 1000 * 3000);
			
			Logg.d(response);
			if (response == null) {// 서버에서 받아오지 못했다면
				return null;
			} else {
				ScheduleList scheduleList = new Gson().fromJson(response, ScheduleList.class);
				return scheduleList;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static FacilityList facilityList(String tracksCode, String towerNo) {
		try {
			List<NameValuePair> getParameters = new ArrayList<NameValuePair>();
			getParameters.add(new BasicNameValuePair("fnct_lc_no", tracksCode));
			getParameters.add(new BasicNameValuePair("eqp_nm", towerNo));
	
			String response = HttpUtil.connectPost(API_FACILITY_LIST, getParameters, 1000 * 300);
			Logg.d(response);
			if (response == null) {// 서버에서 받아오지 못했다면
				return null;
			} else {
				FacilityList facilityList = new Gson().fromJson(response, FacilityList.class);
				return facilityList;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static TracksList tracksList() {
		try {
			List<NameValuePair> getParameters = new ArrayList<NameValuePair>();
	
			String response = HttpUtil.connectPost(API_TRACKS_LIST, getParameters);
			Logg.d(response);
			
			if (response == null) {// 서버에서 받아오지 못했다면
				return null;
			} else {
				TracksList tracksList = new Gson().fromJson(response, TracksList.class);
				return tracksList;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static TowerList towerList() {
		try {
			List<NameValuePair> getParameters = new ArrayList<NameValuePair>();
	
			String response = HttpUtil.connectPost(API_TOWER_LIST, getParameters);
			Logg.d(response);
			
			if (response == null) {// 서버에서 받아오지 못했다면
				return null;
			} else {
				TowerList towerList = new Gson().fromJson(response, TowerList.class);
				return towerList;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static void deviceDelConfirm(String  loginId) {
		try {
			List<NameValuePair> getParameters = new ArrayList<NameValuePair>();
			getParameters.add(new BasicNameValuePair("user_id", loginId));
	
			String response = HttpUtil.connectPost(API_DEVICE_DEL_CONFIRM, getParameters);
			Logg.d(response);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void saveTracking(String  loginId, String latitude, String longitude, String device_token) {
		try {
			if (device_token == null || "".equals(device_token))
				device_token = "1234";
			
			List<NameValuePair> getParameters = new ArrayList<NameValuePair>();
			getParameters.add(new BasicNameValuePair("user_id", loginId));
			getParameters.add(new BasicNameValuePair("latitude", latitude));
			getParameters.add(new BasicNameValuePair("longitude", longitude));
			getParameters.add(new BasicNameValuePair("device_token", device_token));
	
			String response = HttpUtil.connectPost(API_TRACKING_SAVE, getParameters);
			Logg.d(response);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static NfcTagInfo tagInfo(String eqpNo) {
		try {

			List<NameValuePair> getParameters = new ArrayList<NameValuePair>();
			getParameters.add(new BasicNameValuePair("eqp_no", eqpNo));
			
			String response = HttpUtil.connectPost(API_TAG_INFO, getParameters);
			Logg.d(response);
			if (response == null)
				return null;
			else {
				NfcTagInfo tagInfo = new Gson().fromJson(response,NfcTagInfo.class);
				return tagInfo;
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	public static BaseResult apiInputBT(Context context, InspectInfo inspectInfo, BTInfo btInfo) throws Exception {

		List<NameValuePair> getParameters = new ArrayList<NameValuePair>();
		MultipartEntity mpEntity = new MultipartEntity();
		
    	byte[] masterKey= Cipher.createMasterKey();
		setInspectInfo(context, mpEntity, inspectInfo, masterKey);
		mpEntity.addPart("remarks", new StringBody(Cipher.encrypt(btInfo.etc, masterKey)));
		
		mpEntity.addPart("weather_code", new StringBody(Cipher.encrypt(btInfo.weather, masterKey)));
		mpEntity.addPart("tins_result_secd", new StringBody(Cipher.encrypt(btInfo.claim_content, masterKey)));
		mpEntity.addPart("examin_cn", new StringBody(Cipher.encrypt(btInfo.check_result, masterKey)));
		
		CameraLogDao cameraLogDao = CameraLogDao.getInstance(context);
		ArrayList<CameraLog> list = cameraLogDao.selectPickList(inspectInfo.master_idx, inspectInfo.type);
		
		if (!list.isEmpty()) {
			for (int i=0; i<list.size(); i++) {
				CameraLog log = list.get(i);
				Logg.d("ipath : " + log.img_path + "/" + log.subject + "/" + log.content);
				File file = new File(log.img_path);
				if (file.exists()) {
					mpEntity.addPart("result_file"+(i+1), new FileBody(file, "image/png"));
					mpEntity.addPart("file_subject"+(i+1), new StringBody(Cipher.encrypt(log.subject, masterKey)));
					mpEntity.addPart("file_contents"+(i+1), new StringBody(Cipher.encrypt(log.content, masterKey)));
				}
			}
		}
		
		String response = HttpUtil.connectMultipartPost(API_INPUT_BT, mpEntity);
		
		Logg.d(response);
		if (response == null)
			return null;
		
		return new Gson().fromJson(response,BaseResult.class);
	}
	
	public static BaseResult apiInputJJ(Context context, InspectInfo inspectInfo, JJInfo jjInfo) throws Exception {
		MultipartEntity mpEntity = new MultipartEntity();
		
		byte[] masterKey= Cipher.createMasterKey();
		setInspectInfo(context, mpEntity, inspectInfo, masterKey);
		mpEntity.addPart("remarks", new StringBody(""));
		mpEntity.addPart("pmttr", new StringBody(Cipher.encrypt(jjInfo.remarks, masterKey)));
		
		mpEntity.addPart("weather_code", new StringBody(Cipher.encrypt(jjInfo.weather, masterKey)));
		
		mpEntity.addPart("msr_co", new StringBody(Cipher.encrypt(jjInfo.count_1, masterKey)));
		mpEntity.addPart("msr_rs1", new StringBody(Cipher.encrypt(jjInfo.terminal1_1, masterKey)));
		mpEntity.addPart("msr_rs2", new StringBody(Cipher.encrypt(jjInfo.terminal1_2, masterKey)));
		mpEntity.addPart("msr_rs3", new StringBody(Cipher.encrypt(jjInfo.terminal1_3, masterKey)));
		mpEntity.addPart("msr_rs4", new StringBody(Cipher.encrypt(jjInfo.terminal1_5, masterKey)));
		mpEntity.addPart("msr_rs5", new StringBody(Cipher.encrypt(jjInfo.terminal1_10, masterKey)));
		mpEntity.addPart("good_secd", new StringBody(Cipher.encrypt(jjInfo.check_result, masterKey)));

		CameraLogDao cameraLogDao = CameraLogDao.getInstance(context);
		ArrayList<CameraLog> list = cameraLogDao.selectPickList(inspectInfo.master_idx, inspectInfo.type);
		
		if (!list.isEmpty()) {
			for (int i=0; i<list.size(); i++) {
				CameraLog log = list.get(i);
				Logg.d("ipath : " + log.img_path + "/" + log.subject + "/" + log.content);
				File file = new File(log.img_path);
				if (file.exists()) {
					mpEntity.addPart("result_file"+(i+1), new FileBody(file, "image/png"));
					mpEntity.addPart("file_subject"+(i+1), new StringBody(Cipher.encrypt(log.subject, masterKey)));
					mpEntity.addPart("file_contents"+(i+1), new StringBody(Cipher.encrypt(log.content, masterKey)));
				}
			}
		}
		
		String response = HttpUtil.connectMultipartPost(API_INPUT_JJ, mpEntity);
		
		Logg.d(response);
		if (response == null)
			return null;
		
		return new Gson().fromJson(response,BaseResult.class);
	}
	
	public static BaseResult apiInputKB(Context context, InspectInfo inspectInfo, KBInfo kbInfo) throws Exception {

		MultipartEntity mpEntity = new MultipartEntity();
		
		byte[] masterKey= Cipher.createMasterKey();
		setInspectInfo(context, mpEntity, inspectInfo, masterKey);
		mpEntity.addPart("remarks", new StringBody("", Charset.forName("utf-8")));
		
		mpEntity.addPart("weather_code", new StringBody(Cipher.encrypt(kbInfo.weather, masterKey)));
		
		mpEntity.addPart("item1", new StringBody(Cipher.encrypt(kbInfo.item_fac_1, masterKey)));
		mpEntity.addPart("item2", new StringBody(Cipher.encrypt(kbInfo.item_fac_2, masterKey)));
		mpEntity.addPart("item3", new StringBody(Cipher.encrypt(kbInfo.item_fac_3, masterKey)));
		mpEntity.addPart("item4", new StringBody(Cipher.encrypt(kbInfo.item_fac_4, masterKey)));
		mpEntity.addPart("item5", new StringBody(Cipher.encrypt(kbInfo.item_fac_5, masterKey)));
		mpEntity.addPart("item6", new StringBody(Cipher.encrypt(kbInfo.item_fac_6, masterKey)));
		mpEntity.addPart("item7", new StringBody(Cipher.encrypt(kbInfo.item_fac_7, masterKey)));
		mpEntity.addPart("item8", new StringBody(Cipher.encrypt(kbInfo.item_fac_8, masterKey)));
		mpEntity.addPart("item9", new StringBody(Cipher.encrypt(kbInfo.item_fac_9, masterKey)));
		mpEntity.addPart("item10", new StringBody(Cipher.encrypt(kbInfo.item_fac_10, masterKey)));
		mpEntity.addPart("item11", new StringBody(Cipher.encrypt(kbInfo.item_fac_11, masterKey)));
		mpEntity.addPart("item12", new StringBody(Cipher.encrypt(kbInfo.item_fac_12, masterKey)));
		mpEntity.addPart("item13", new StringBody(Cipher.encrypt(kbInfo.item_sett_1, masterKey)));
		mpEntity.addPart("item14", new StringBody(Cipher.encrypt(kbInfo.item_sett_2, masterKey)));
		mpEntity.addPart("item15", new StringBody(Cipher.encrypt(kbInfo.item_sett_3, masterKey)));
		mpEntity.addPart("item16", new StringBody(Cipher.encrypt(kbInfo.item_sett_4, masterKey)));
		mpEntity.addPart("item17", new StringBody(Cipher.encrypt(kbInfo.item_etc_1, masterKey)));
		mpEntity.addPart("item18", new StringBody(Cipher.encrypt(kbInfo.item_etc_2, masterKey)));
		
		CameraLogDao cameraLogDao = CameraLogDao.getInstance(context);
		ArrayList<CameraLog> list = cameraLogDao.selectPickList(inspectInfo.master_idx, inspectInfo.type);
		
		if (!list.isEmpty()) {
			for (int i=0; i<list.size(); i++) {
				CameraLog log = list.get(i);
				Logg.d("ipath : " + log.img_path + "/" + log.subject + "/" + log.content);
				File file = new File(log.img_path);
				if (file.exists()) {
					mpEntity.addPart("result_file"+(i+1), new FileBody(file, "image/png"));
					mpEntity.addPart("file_subject"+(i+1), new StringBody(Cipher.encrypt(log.subject, masterKey)));
					mpEntity.addPart("file_contents"+(i+1), new StringBody(Cipher.encrypt(log.content, masterKey)));
				}
			}
		}
		
		String response = HttpUtil.connectMultipartPost(API_INPUT_KB, mpEntity);
		
		Logg.d(response);
		if (response == null)
			return null;
		
		return new Gson().fromJson(response,BaseResult.class);
	}
	
	public static BaseResult apiInputJS(Context context, InspectInfo inspectInfo, JSInfo jsInfo) throws Exception {
			
		MultipartEntity mpEntity = new MultipartEntity();
		
		byte[] masterKey= Cipher.createMasterKey();
		setInspectInfo(context, mpEntity, inspectInfo, masterKey);
		mpEntity.addPart("remarks", new StringBody(""));
		
		mpEntity.addPart("weather_code", new StringBody(Cipher.encrypt(jsInfo.weather, masterKey)));
		
//			mpEntity.addPart("out_temp", new StringBody(Cipher.encrypt(jsInfo.area_temp));
		mpEntity.addPart("cl_no", new StringBody(Cipher.encrypt(jsInfo.circuit_no, masterKey)));
		mpEntity.addPart("ttm_load", new StringBody(Cipher.encrypt(jsInfo.current_load, masterKey)));
		mpEntity.addPart("cndctr_co", new StringBody(Cipher.encrypt(jsInfo.conductor_cnt, masterKey)));
		mpEntity.addPart("cndctr_sn", new StringBody(Cipher.encrypt(jsInfo.location, masterKey)));
		mpEntity.addPart("pwln_eqp_no1 ", new StringBody(Cipher.encrypt(jsInfo.c1_power_no, masterKey)));
		mpEntity.addPart("cabl_tp1 ", new StringBody(Cipher.encrypt(jsInfo.c1_js, masterKey)));
		mpEntity.addPart("cnpt_tp1 ", new StringBody(Cipher.encrypt(jsInfo.c1_jsj, masterKey)));
		mpEntity.addPart("good_secd1 ", new StringBody(Cipher.encrypt(jsInfo.c1_yb_result, masterKey)));
		mpEntity.addPart("pwln_eqp_no2 ", new StringBody(Cipher.encrypt(jsInfo.c2_power_no, masterKey)));
		mpEntity.addPart("cabl_tp2", new StringBody(Cipher.encrypt(jsInfo.c2_js, masterKey))); 
		mpEntity.addPart("cnpt_tp2", new StringBody(Cipher.encrypt(jsInfo.c2_jsj, masterKey))); 
		mpEntity.addPart("good_secd2 ", new StringBody(Cipher.encrypt(jsInfo.c2_yb_result, masterKey)));
		mpEntity.addPart("pwln_eqp_no3 ", new StringBody(Cipher.encrypt(jsInfo.c3_power_no, masterKey)));
		mpEntity.addPart("cabl_tp3", new StringBody(Cipher.encrypt(jsInfo.c3_js, masterKey)));
		mpEntity.addPart("cnpt_tp3", new StringBody(Cipher.encrypt(jsInfo.c3_jsj, masterKey))); 
		mpEntity.addPart("good_secd3", new StringBody(Cipher.encrypt(jsInfo.c3_yb_result, masterKey)));
		
		
		CameraLogDao cameraLogDao = CameraLogDao.getInstance(context);
		ArrayList<CameraLog> list = cameraLogDao.selectPickList(inspectInfo.master_idx, inspectInfo.type);
		
		if (!list.isEmpty()) {
			for (int i=0; i<list.size(); i++) {
				CameraLog log = list.get(i);
				Logg.d("ipath : " + log.img_path + "/" + log.subject + "/" + log.content);
				File file = new File(log.img_path);
				if (file.exists()) {
					mpEntity.addPart("result_file"+(i+1), new FileBody(file, "image/png"));
					mpEntity.addPart("file_subject"+(i+1), new StringBody(Cipher.encrypt(log.subject, masterKey)));
					mpEntity.addPart("file_contents"+(i+1), new StringBody(Cipher.encrypt(log.content, masterKey)));
				}
			}
		}
		
		String response = HttpUtil.connectMultipartPost(API_INPUT_JS, mpEntity);
		
		Logg.d(response);
		if (response == null)
			return null;
		
		BaseResult baseResult = new Gson().fromJson(response,BaseResult.class);
		if (baseResult != null && !RESULT_OK.equals(baseResult.code))
			throw new Exception();
		
		return baseResult;
	}
	
	public static BaseResult apiInputJP(Context context, InspectInfo inspectInfo, JSInfo jpInfo) throws Exception {
			
		MultipartEntity mpEntity = new MultipartEntity();
		
		byte[] masterKey= Cipher.createMasterKey();
		setInspectInfo(context, mpEntity, inspectInfo, masterKey);
		mpEntity.addPart("remarks", new StringBody(""));
		
		mpEntity.addPart("weather_code", new StringBody(Cipher.encrypt(jpInfo.weather, masterKey)));
		
		mpEntity.addPart("cl_no", new StringBody(Cipher.encrypt(jpInfo.circuit_no, masterKey)));
		mpEntity.addPart("ttm_load", new StringBody(Cipher.encrypt(jpInfo.current_load, masterKey)));
		mpEntity.addPart("cndctr_co", new StringBody(Cipher.encrypt(jpInfo.conductor_cnt, masterKey)));
		mpEntity.addPart("cndctr_sn", new StringBody(Cipher.encrypt(jpInfo.location, masterKey)));
		mpEntity.addPart("pwln_eqp_no1 ", new StringBody(Cipher.encrypt(jpInfo.c1_power_no, masterKey)));
		mpEntity.addPart("cabl_tp1 ", new StringBody(Cipher.encrypt(jpInfo.c1_js, masterKey)));
		mpEntity.addPart("cnpt_tp1 ", new StringBody(Cipher.encrypt(jpInfo.c1_jsj, masterKey)));
		mpEntity.addPart("good_secd1 ", new StringBody(Cipher.encrypt(jpInfo.c1_yb_result, masterKey)));
		mpEntity.addPart("pwln_eqp_no2 ", new StringBody(Cipher.encrypt(jpInfo.c2_power_no, masterKey)));
		mpEntity.addPart("cabl_tp2", new StringBody(Cipher.encrypt(jpInfo.c2_js, masterKey)));
		mpEntity.addPart("cnpt_tp2", new StringBody(Cipher.encrypt(jpInfo.c2_jsj, masterKey)));
		mpEntity.addPart("good_secd2 ", new StringBody(Cipher.encrypt(jpInfo.c2_yb_result, masterKey)));
		mpEntity.addPart("pwln_eqp_no3 ", new StringBody(Cipher.encrypt(jpInfo.c3_power_no, masterKey)));
		mpEntity.addPart("cabl_tp3", new StringBody(Cipher.encrypt(jpInfo.c3_js, masterKey)));
		mpEntity.addPart("cnpt_tp3", new StringBody(Cipher.encrypt(jpInfo.c3_jsj, masterKey))); 
		mpEntity.addPart("good_secd3", new StringBody(Cipher.encrypt(jpInfo.c3_yb_result, masterKey)));
		
		
		CameraLogDao cameraLogDao = CameraLogDao.getInstance(context);
		ArrayList<CameraLog> list = cameraLogDao.selectPickList(inspectInfo.master_idx, inspectInfo.type);
		
		if (!list.isEmpty()) {
			for (int i=0; i<list.size(); i++) {
				CameraLog log = list.get(i);
				Logg.d("ipath : " + log.img_path + "/" + log.subject + "/" + log.content);
				File file = new File(log.img_path);
				if (file.exists()) {
					mpEntity.addPart("result_file"+(i+1), new FileBody(file, "image/png"));
					mpEntity.addPart("file_subject"+(i+1), new StringBody(Cipher.encrypt(log.subject, masterKey)));
					mpEntity.addPart("file_contents"+(i+1), new StringBody(Cipher.encrypt(log.content, masterKey)));
				}
			}
		}
		
		String response = HttpUtil.connectMultipartPost(API_INPUT_JP, mpEntity);
		
		Logg.d(response);
		if (response == null)
			return null;
		
		return new Gson().fromJson(response,BaseResult.class);
	}
	
	public static BaseResult apiInputBR(Context context, InspectInfo inspectInfo, BRInfo brInfo) throws Exception {

		MultipartEntity mpEntity = new MultipartEntity();
		
		byte[] masterKey= Cipher.createMasterKey();
		setInspectInfo(context, mpEntity, inspectInfo, masterKey);
		mpEntity.addPart("remarks", new StringBody(""));
		
		mpEntity.addPart("weather_code", new StringBody(Cipher.encrypt(brInfo.weather, masterKey)));
		mpEntity.addPart("ty_secd", new StringBody(Cipher.encrypt(brInfo.ty_secd, masterKey)));
		mpEntity.addPart("cl_no", new StringBody(Cipher.encrypt(brInfo.circuit_no, masterKey)));
		mpEntity.addPart("phs_secd", new StringBody(Cipher.encrypt(brInfo.phs_secd, masterKey)));
		mpEntity.addPart("insbty_lft", new StringBody(Cipher.encrypt(brInfo.insbty_lft, masterKey)));
		mpEntity.addPart("insbty_rit", new StringBody(Cipher.encrypt(brInfo.insbty_rit, masterKey)));
		mpEntity.addPart("insr_qy", new StringBody(Cipher.encrypt(brInfo.ej_cnt, masterKey)));
		mpEntity.addPart("bad_insr_qy ", new StringBody(Cipher.encrypt(brInfo.br_cnt, masterKey)));
		mpEntity.addPart("good_secd", new StringBody(Cipher.encrypt(brInfo.yb_result, masterKey)));
		mpEntity.addPart("prod_ymd", new StringBody(Cipher.encrypt(brInfo.make_date, masterKey)));
		mpEntity.addPart("prod_comp", new StringBody(Cipher.encrypt(brInfo.make_company, masterKey)));
		mpEntity.addPart("insr_eqp_no", new StringBody(Cipher.encrypt(brInfo.insr_eqp_no, masterKey)));
		

		CameraLogDao cameraLogDao = CameraLogDao.getInstance(context);
		ArrayList<CameraLog> list = cameraLogDao.selectPickList(inspectInfo.master_idx, inspectInfo.type);
		
		if (!list.isEmpty()) {
			for (int i=0; i<list.size(); i++) {
				CameraLog log = list.get(i);
				Logg.d("ipath : " + log.img_path + "/" + log.subject + "/" + log.content);
				File file = new File(log.img_path);
				if (file.exists()) {
					mpEntity.addPart("result_file"+(i+1), new FileBody(file, "image/png"));
					mpEntity.addPart("file_subject"+(i+1), new StringBody(Cipher.encrypt(log.subject, masterKey)));
					mpEntity.addPart("file_contents"+(i+1), new StringBody(Cipher.encrypt(log.content, masterKey)));
				}
			}
		}
		
		String response = HttpUtil.connectMultipartPost(API_INPUT_BR, mpEntity);
		
		Logg.d(response);
		if (response == null)
			return null;
		
		return new Gson().fromJson(response,BaseResult.class);
	}
	
	public static BaseResult apiInputHJ(Context context, InspectInfo inspectInfo, HJInfo hjInfo) throws Exception {

		MultipartEntity mpEntity = new MultipartEntity();
		
		byte[] masterKey= Cipher.createMasterKey();
		setInspectInfo(context, mpEntity, inspectInfo, masterKey);
		mpEntity.addPart("remarks", new StringBody(""));
		
		mpEntity.addPart("weather_code", new StringBody(Cipher.encrypt(hjInfo.weather, masterKey)));
		mpEntity.addPart("flight_lmp_knd", new StringBody(Cipher.encrypt(hjInfo.light_type, masterKey)));
		mpEntity.addPart("flight_lmp_no", new StringBody(Cipher.encrypt(hjInfo.light_no, masterKey)));
		mpEntity.addPart("srcelct_knd", new StringBody(Cipher.encrypt(hjInfo.power, masterKey)));
		mpEntity.addPart("ctrl_ban_gdbd_secd", new StringBody(Cipher.encrypt(hjInfo.control, masterKey)));
		mpEntity.addPart("slrcl_gdbd_secd", new StringBody(Cipher.encrypt(hjInfo.sun_battery, masterKey)));
		mpEntity.addPart("rgist_gu_gdbd_secd", new StringBody(Cipher.encrypt(hjInfo.storage_battery, masterKey)));
		mpEntity.addPart("srgbtry_gdbd_secd", new StringBody(Cipher.encrypt(hjInfo.light_item, masterKey)));
		mpEntity.addPart("cabl_gdbd_secd", new StringBody(Cipher.encrypt(hjInfo.cable, masterKey)));
		mpEntity.addPart("good_secd", new StringBody(Cipher.encrypt(hjInfo.yb_result, masterKey)));

		CameraLogDao cameraLogDao = CameraLogDao.getInstance(context);
		ArrayList<CameraLog> list = cameraLogDao.selectPickList(inspectInfo.master_idx, inspectInfo.type);
		
		if (!list.isEmpty()) {
			for (int i=0; i<list.size(); i++) {
				CameraLog log = list.get(i);
				Logg.d("ipath : " + log.img_path + "/" + log.subject + "/" + log.content);
				File file = new File(log.img_path);
				if (file.exists()) {
					mpEntity.addPart("result_file"+(i+1), new FileBody(file, "image/png"));
					mpEntity.addPart("file_subject"+(i+1), new StringBody(Cipher.encrypt(log.subject, masterKey)));
					mpEntity.addPart("file_contents"+(i+1), new StringBody(Cipher.encrypt(log.content, masterKey)));
				}
			}
		}
		
		String response = HttpUtil.connectMultipartPost(API_INPUT_HJ, mpEntity);
		
		Logg.d(response);
		if (response == null)
			return null;
		
		return new Gson().fromJson(response,BaseResult.class);
	}
	
	public static BaseResult apiInputHK(Context context, InspectInfo inspectInfo, HKInfo hkInfo) throws Exception {

		MultipartEntity mpEntity = new MultipartEntity();
		
		byte[] masterKey= Cipher.createMasterKey();
		setInspectInfo(context, mpEntity, inspectInfo, masterKey);
		mpEntity.addPart("remarks", new StringBody(""));
		
		mpEntity.addPart("weather_code", new StringBody(Cipher.encrypt(hkInfo.weather, masterKey)));
		
		mpEntity.addPart("flight_lmp_knd", new StringBody(Cipher.encrypt(hkInfo.lightType, masterKey)));
		mpEntity.addPart("flight_lmp_no", new StringBody(Cipher.encrypt(hkInfo.light_no, masterKey)));
		mpEntity.addPart("srcelct_knd", new StringBody(Cipher.encrypt(hkInfo.power, masterKey)));
		mpEntity.addPart("flck_co", new StringBody(Cipher.encrypt(hkInfo.light_cnt, masterKey)));
		mpEntity.addPart("lightg_stadiv_cd", new StringBody(Cipher.encrypt(hkInfo.light_state, masterKey)));
		mpEntity.addPart("good_secd", new StringBody(Cipher.encrypt(hkInfo.yb_result, masterKey)));

		CameraLogDao cameraLogDao = CameraLogDao.getInstance(context);
		ArrayList<CameraLog> list = cameraLogDao.selectPickList(inspectInfo.master_idx, inspectInfo.type);
		
		if (!list.isEmpty()) {
			for (int i=0; i<list.size(); i++) {
				CameraLog log = list.get(i);
				Logg.d("ipath : " + log.img_path + "/" + log.subject + "/" + log.content);
				File file = new File(log.img_path);
				if (file.exists()) {
					mpEntity.addPart("result_file"+(i+1), new FileBody(file, "image/png"));
					mpEntity.addPart("file_subject"+(i+1), new StringBody(Cipher.encrypt(log.subject, masterKey)));
					mpEntity.addPart("file_contents"+(i+1), new StringBody(Cipher.encrypt(log.content, masterKey)));
				}
			}
		}
		
		String response = HttpUtil.connectMultipartPost(API_INPUT_HK, mpEntity);
		
		Logg.d(response);
		if (response == null)
			return null;
		
		return new Gson().fromJson(response,BaseResult.class);
	}
	
	public static void setInspectInfo(Context context, MultipartEntity mpEntity, InspectInfo inspectInfo, byte[] masterKey) throws UnsupportedEncodingException, InvalidKeyException {
		String loginId = Shared.getString(context, ConstSP.LOING_ID_KEY);
		
		String inspecter_1 = Shared.getString(context, ConstSP.SETTING_INSPECTER_ID_1);
		String inspecter_2 = Shared.getString(context, ConstSP.SETTING_INSPECTER_ID_2);
		
		String tracksCode = CodeInfo.getInstance(context).getKey(ConstVALUE.CODE_TYPE_TRACKS, inspectInfo.tracksName);
		
		mpEntity.addPart("schedule_id", new StringBody(Cipher.encrypt(inspectInfo.unity_ins_no, masterKey)));
		mpEntity.addPart("fnct_lc_no", new StringBody(Cipher.encrypt(tracksCode, masterKey)));
		mpEntity.addPart("eqp_no", new StringBody(Cipher.encrypt(inspectInfo.eqpNo, masterKey)));
		mpEntity.addPart("cycle_ym", new StringBody(Cipher.encrypt(inspectInfo.date, masterKey)));
		mpEntity.addPart("ins_type_code", new StringBody(Cipher.encrypt(inspectInfo.type, masterKey)));
		mpEntity.addPart("latitude", new StringBody(Cipher.encrypt(inspectInfo.nfcTagLatitude, masterKey)));
		mpEntity.addPart("longitude", new StringBody(Cipher.encrypt(inspectInfo.nfcTagLongitude, masterKey)));
		mpEntity.addPart("tag_yn", new StringBody(Cipher.encrypt(inspectInfo.nfc_tag_yn, masterKey)));
		mpEntity.addPart("nfc_id", new StringBody(Cipher.encrypt(inspectInfo.nfc_tag_id, masterKey)));
		mpEntity.addPart("check_ins_name_a", new StringBody(Cipher.encrypt(inspecter_1, masterKey)));
		mpEntity.addPart("check_ins_name_b", new StringBody(Cipher.encrypt(inspecter_2, masterKey)));
		mpEntity.addPart("imprmn_request_no", new StringBody(""));
		mpEntity.addPart("gubun", new StringBody(""));
		mpEntity.addPart("tid", new StringBody(""));
		mpEntity.addPart("reg_id", new StringBody(Cipher.encrypt(loginId, masterKey)));
		mpEntity.addPart("master_key", new StringBody(Cipher.getEnMasterKey()));
		
	}
	
	public static PatrolmansList patrolMansList() {
		List<NameValuePair> getParameters = new ArrayList<NameValuePair>();

		String response = HttpUtil.connectPost(API_PATROLMANS_LIST, getParameters);
		Logg.d(response);
		if (response == null) {// 서버에서 받아오지 못했다면
			return null;
		} else {
			PatrolmansList patrolmansList = new Gson().fromJson(response, PatrolmansList.class);
			return patrolmansList;
		}
	}
	
	public static JSSubInfoList jsInfoSubList() {
		try {
			List<NameValuePair> getParameters = new ArrayList<NameValuePair>();
	
			String response = HttpUtil.connectPost(API_INPUT_JS_SUB_LIST, getParameters, 1000 * 300);
			Logg.d(response);
			if (response == null) {// 서버에서 받아오지 못했다면
				return null;
			} else {
				JSSubInfoList jsSubInfoList = new Gson().fromJson(response, JSSubInfoList.class);
				return jsSubInfoList;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static BRSubInfoList brInfoSubList() {
		try {
			List<NameValuePair> getParameters = new ArrayList<NameValuePair>();
	
			String response = HttpUtil.connectPost(API_INPUT_BR_SUB_LIST, getParameters, 1000 * 300);
			Logg.d(response);
			if (response == null) {// 서버에서 받아오지 못했다면
				return null;
			} else {
				BRSubInfoList brSubInfoList = new Gson().fromJson(response, BRSubInfoList.class);
				return brSubInfoList;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static HKSubInfoList hkInfoSubList() {
		try {
			List<NameValuePair> getParameters = new ArrayList<NameValuePair>();
	
			String response = HttpUtil.connectPost(API_INPUT_HK_SUB_LIST, getParameters, 1000 * 300);
			Logg.d(response);
			if (response == null) {// 서버에서 받아오지 못했다면
				return null;
			} else {
				HKSubInfoList hkSubInfoList = new Gson().fromJson(response, HKSubInfoList.class);
				return hkSubInfoList;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static YBInfoList ybInfoList(String tracksCode) {
		try {
			List<NameValuePair> getParameters = new ArrayList<NameValuePair>();
			getParameters.add(new BasicNameValuePair("fnct_lc_no", tracksCode));
			
			String response = HttpUtil.connectPost(API_INPUT_YB_LIST, getParameters);
			Logg.d(response);
			if (response == null) {// 서버에서 받아오지 못했다면
				return null;
			} else {
				YBInfoList ybInfoList = new Gson().fromJson(response, YBInfoList.class);
				return ybInfoList;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static void ybInsert(Context context, YBInfo info, String tracksCode) {
		try {
			
			String tins_result = URLEncoder.encode(info.TINS_RESULT, "UTF-8");
					
			
//			StringBody tins_result3 = new StringBody(info.TINS_RESULT, Charset.forName("utf-8"));
//			
//			Logg.d("aaa : " + tins_result3.toString());
					
			MultipartEntity mpEntity = new MultipartEntity();
			mpEntity.addPart("rgt_sn", new StringBody(info.RGT_SN));
			mpEntity.addPart("tins_result_secd", new StringBody(info.TINS_RESULT_SECD));
			mpEntity.addPart("tins_result", new StringBody(info.TINS_RESULT, Charset.forName("utf-8")));
			mpEntity.addPart("fnct_lc_no", new StringBody(tracksCode));
			
			CameraLogDao cameraLogDao = CameraLogDao.getInstance(context);
			ArrayList<CameraLog> list = cameraLogDao.selectPickList(tracksCode, ConstVALUE.CODE_NO_INSPECT_YB);
			
			if (!list.isEmpty()) {
				for (int i=0; i<list.size(); i++) {
					CameraLog log = list.get(i);
					Logg.d("ipath : " + log.img_path + "/" + log.subject + "/" + log.content);
					File file = new File(log.img_path);
					if (file.exists()) {
						mpEntity.addPart("result_file"+(i+1), new FileBody(file, "image/png"));
						mpEntity.addPart("file_subject"+(i+1), new StringBody(log.subject, Charset.forName("utf-8")));
						mpEntity.addPart("file_contents"+(i+1), new StringBody(log.content, Charset.forName("utf-8")));
					}
				}
			}
			
			String response = HttpUtil.connectMultipartPost(API_INSERT_YB, mpEntity);
			
//			List<NameValuePair> getParameters = new ArrayList<NameValuePair>();
//			getParameters.add(new BasicNameValuePair("rgt_sn", info.RGT_SN));
//			getParameters.add(new BasicNameValuePair("tins_result_secd", info.TINS_RESULT_SECD));
//			getParameters.add(new BasicNameValuePair("tins_result", info.TINS_RESULT));
//			getParameters.add(new BasicNameValuePair("tins_result2", tins_result));
//			getParameters.add(new BasicNameValuePair("fnct_lc_no", tracksCode));
//			
//			new UrlEncodedFormEntity(getParameters, "UTF-8");
//			
//			String response = HttpUtil.connectPost(API_INSERT_YB, getParameters);
			Logg.d(response);
			if (response == null) {// 서버에서 받아오지 못했다면
			} else {
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
